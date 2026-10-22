package org.treino.demo.services;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.treino.demo.dtos.requests.OrderItemRequestDTO;
import org.treino.demo.dtos.responses.OrderItemResponseDTO;
import org.treino.demo.entities.OrderEntity;
import org.treino.demo.entities.OrderItemEntity;
import org.treino.demo.entities.OrderItemId;
import org.treino.demo.entities.ProductEntity;
import org.treino.demo.enums.StatusOrderEntity;
import org.treino.demo.exceptions.InvalidOrderItemException;
import org.treino.demo.exceptions.OrderItemConflictException;
import org.treino.demo.exceptions.ResourceNotFoundException;
import org.treino.demo.repositories.OrderItemRepository;
import org.treino.demo.repositories.OrderRepository;
import org.treino.demo.repositories.ProductRepository;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderItemService(
            OrderItemRepository orderItemRepository,
            OrderRepository orderRepository,
            ProductRepository productRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public OrderItemResponseDTO createOrderItem(UUID orderId, OrderItemRequestDTO request) {
        validateRequest(request);
        OrderEntity order = findOrderByIdForUpdate(orderId);
        ensureItemsCanBeChanged(order);
        OrderItemId itemId = new OrderItemId(orderId, request.productID());

        ProductEntity product = productRepository.findById(request.productID())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with ID: " + request.productID()));

        if (!product.isActive()) {
            throw new OrderItemConflictException(
                    "Product is not available for sale: " + request.productID());
        }

        OrderItemEntity item = orderItemRepository.findById(itemId).orElse(null);
        if (item == null) {
            item = new OrderItemEntity(product, request.quantity(), product.getPrice());
            order.addItem(item);
        } else {
            try {
                item.setQuantity(Math.addExact(item.getQuantity(), request.quantity()));
            } catch (ArithmeticException exception) {
                throw new InvalidOrderItemException(
                        "Total quantity for a product exceeds the supported limit");
            }
        }
        order.setTotalAmount(calculateTotal(order));

        return toResponse(orderItemRepository.save(item));
    }

    @Transactional(readOnly = true)
    public OrderItemResponseDTO getOrderItem(UUID orderId, UUID productId) {
        findOrderById(orderId);
        return toResponse(findOrderItem(orderId, productId));
    }

    @Transactional(readOnly = true)
    public Page<OrderItemResponseDTO> getOrderItems(UUID orderId, Pageable pageable) {
        findOrderById(orderId);
        return orderItemRepository.findAllByIdOrderId(orderId, pageable)
                .map(this::toResponse);
    }

    @Transactional
    public OrderItemResponseDTO updateOrderItem(
            UUID orderId,
            UUID productId,
            OrderItemRequestDTO request) {
        validateRequest(request);
        if (productId == null || !productId.equals(request.productID())) {
            throw new InvalidOrderItemException(
                    "Product ID in request must match the item being updated");
        }

        OrderEntity order = findOrderByIdForUpdate(orderId);
        ensureItemsCanBeChanged(order);
        OrderItemEntity item = findOrderItem(orderId, productId);
        item.setQuantity(request.quantity());
        order.setTotalAmount(calculateTotal(order));

        return toResponse(orderItemRepository.save(item));
    }

    @Transactional
    public void deleteOrderItem(UUID orderId, UUID productId) {
        OrderEntity order = findOrderByIdForUpdate(orderId);
        ensureItemsCanBeChanged(order);
        OrderItemEntity item = findOrderItem(orderId, productId);

        if (order.getItems().size() == 1) {
            orderRepository.delete(order);
            return;
        }

        order.removeItem(item);
        order.setTotalAmount(calculateTotal(order));
    }

    private OrderEntity findOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with ID: " + orderId));
    }

    private OrderEntity findOrderByIdForUpdate(UUID orderId) {
        return orderRepository.findByIdForUpdate(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with ID: " + orderId));
    }

    private OrderItemEntity findOrderItem(UUID orderId, UUID productId) {
        OrderItemId itemId = new OrderItemId(orderId, productId);
        return orderItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order item not found for order " + orderId
                                + " and product " + productId));
    }

    private void validateRequest(OrderItemRequestDTO request) {
        if (request == null || request.productID() == null
                || request.quantity() == null || request.quantity() <= 0) {
            throw new InvalidOrderItemException(
                    "Product ID is required and quantity must be positive");
        }
    }

    private void ensureItemsCanBeChanged(OrderEntity order) {
        if (order.getStatus() != StatusOrderEntity.WAITING_PAYMENT) {
            throw new OrderItemConflictException(
                    "Items can only be changed while the order is waiting for payment");
        }
    }

    private BigDecimal calculateTotal(OrderEntity order) {
        return order.getItems().stream()
                .map(item -> item.getUnitPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private OrderItemResponseDTO toResponse(OrderItemEntity item) {
        BigDecimal subtotal = item.getUnitPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity()));

        return new OrderItemResponseDTO(
                item.getProduct().getId(),
                item.getQuantity(),
                item.getUnitPrice(),
                subtotal);
    }
}
