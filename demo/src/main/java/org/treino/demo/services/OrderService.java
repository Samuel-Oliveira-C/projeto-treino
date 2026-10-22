package org.treino.demo.services;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.treino.demo.dtos.requests.OrderRequestDTO;
import org.treino.demo.dtos.requests.OrderItemRequestDTO;
import org.treino.demo.dtos.requests.PaymentRequestDTO;
import org.treino.demo.dtos.responses.OrderResponseDTO;
import org.treino.demo.entities.CustomerEntity;
import org.treino.demo.entities.OrderEntity;
import org.treino.demo.entities.OrderItemEntity;
import org.treino.demo.entities.ProductEntity;
import org.treino.demo.enums.StatusOrderEntity;
import org.treino.demo.exceptions.InsufficientStockException;
import org.treino.demo.exceptions.InvalidOrderItemException;
import org.treino.demo.mappers.OrderMapper;
import org.treino.demo.repositories.CustomerRepository;
import org.treino.demo.repositories.OrderRepository;
import org.treino.demo.repositories.ProductRepository;
import org.treino.demo.exceptions.InvalidOrderStatusTransitionException;
import org.treino.demo.exceptions.InvalidPaymentException;
import org.treino.demo.exceptions.OrderDeletionNotAllowedException;
import org.treino.demo.exceptions.ResourceNotFoundException;

@Service 
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    public OrderService(
            OrderRepository orderRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository,
            OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional 
    public OrderResponseDTO createOrder(OrderRequestDTO requestDTO) {
        if (requestDTO == null || requestDTO.customerID() == null
                || requestDTO.items() == null || requestDTO.items().isEmpty()) {
            throw new InvalidOrderItemException(
                    "Customer and at least one order item are required");
        }

        CustomerEntity customer = customerRepository.findById(requestDTO.customerID())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Customer not found with ID: " + requestDTO.customerID()));
        
        OrderEntity order = new OrderEntity();
        order.setStatus(StatusOrderEntity.WAITING_PAYMENT);
        order.setCustomer(customer);

        Map<UUID, Integer> requestedQuantities = new LinkedHashMap<>();
        for (OrderItemRequestDTO itemRequest : requestDTO.items()) {
            if (itemRequest == null || itemRequest.productID() == null
                    || itemRequest.quantity() == null || itemRequest.quantity() <= 0) {
                throw new InvalidOrderItemException(
                        "Each item must have a product ID and a positive quantity");
            }

            try {
                requestedQuantities.merge(
                        itemRequest.productID(),
                        itemRequest.quantity(),
                        Math::addExact);
            } catch (ArithmeticException exception) {
                throw new InvalidOrderItemException(
                        "Total quantity for a product exceeds the supported limit");
            }
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Map.Entry<UUID, Integer> entry : requestedQuantities.entrySet()) {
            ProductEntity product = productRepository.findById(entry.getKey())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product not found with ID: " + entry.getKey()));

            if (!product.isActive()) {
                throw new InvalidOrderItemException(
                        "Product is not available for sale: " + entry.getKey());
            }

            OrderItemEntity item = new OrderItemEntity(
                    product, entry.getValue(), product.getPrice());
            order.addItem(item);
            totalAmount = totalAmount.add(
                    product.getPrice().multiply(BigDecimal.valueOf(entry.getValue())));
        }

        order.setTotalAmount(totalAmount);
        OrderEntity savedOrder = orderRepository.save(order);
        
        return orderMapper.toResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderById(UUID orderID){
        OrderEntity order = orderRepository.findById(orderID)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Order not found with ID: " + orderID));
        
        return orderMapper.toResponse(order);
    }

    @Transactional
    public OrderResponseDTO confirmPayment(UUID orderID, PaymentRequestDTO request) {
        BigDecimal amountReceived = request == null ? null : request.amountReceived();
        if (amountReceived == null || amountReceived.signum() <= 0) {
            throw new InvalidPaymentException("O valor pago deve ser positivo");
        }

        OrderEntity order = orderRepository.findByIdForUpdate(orderID)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with ID: " + orderID));

        if (order.getStatus() != StatusOrderEntity.WAITING_PAYMENT) {
            throw new InvalidOrderStatusTransitionException(
                    "Payment can only be confirmed for orders waiting for payment");
        }

        if (amountReceived.compareTo(order.getTotalAmount()) != 0) {
            throw new InvalidPaymentException(
                    "O valor pago não corresponde ao total do pedido");
        }

        if (order.getItems().isEmpty()) {
            throw new InvalidPaymentException(
                    "Não é possível confirmar pagamento de um pedido sem itens");
        }

        for (OrderItemEntity item : order.getItems().stream()
                .sorted(Comparator.comparing(orderItem -> orderItem.getProduct().getId()))
                .toList()) {
            int updatedProducts = productRepository.decreaseStockIfAvailable(
                    item.getProduct().getId(),
                    item.getQuantity());
            if (updatedProducts == 0) {
                throw new InsufficientStockException(
                        "Insufficient stock for product: " + item.getProduct().getId());
            }
        }

        order.setStatus(StatusOrderEntity.PREPARING);
        return orderMapper.toResponse(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public Page<OrderResponseDTO> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMapper::toResponse);
    }

    @Transactional 
    public void deleteOrder(UUID orderID) {
        OrderEntity order = orderRepository.findByIdForUpdate(orderID)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Order not found with ID: " + orderID));

        switch (order.getStatus()) {
            case WAITING_PAYMENT:
                orderRepository.delete(order);
                break;
            case READY:
                orderRepository.delete(order);
                break;
            case PREPARING:
                orderRepository.delete(order);
                break;
            default:
                throw new OrderDeletionNotAllowedException(
                    "Status not allowed for deletion: " + order.getStatus());
        } 
    }

    @Transactional
    public OrderResponseDTO cancelOrder(UUID orderID) {
        OrderEntity order = orderRepository.findByIdForUpdate(orderID)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with ID: " + orderID));

        if (order.getStatus() == StatusOrderEntity.CANCELED) {
            return orderMapper.toResponse(order);
        }

        if (order.getStatus() != StatusOrderEntity.WAITING_PAYMENT
                && order.getStatus() != StatusOrderEntity.PREPARING
                && order.getStatus() != StatusOrderEntity.READY) {
            throw new InvalidOrderStatusTransitionException(
                    "Invalid status transition from "
                            + order.getStatus() + " to " + StatusOrderEntity.CANCELED);
        }

        order.setStatus(StatusOrderEntity.CANCELED);

        return orderMapper.toResponse(orderRepository.save(order));
    }

    @Transactional
    public OrderResponseDTO updateOrderStatus(UUID orderID, StatusOrderEntity newStatus) {
        OrderEntity order = orderRepository.findByIdForUpdate(orderID)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Order not found with ID: " + orderID));

        if (newStatus == null) {
            throw new IllegalArgumentException("New status cannot be null");
        }

        if (order.getStatus() == StatusOrderEntity.WAITING_PAYMENT
                && newStatus == StatusOrderEntity.PREPARING) {
            throw new InvalidOrderStatusTransitionException(
                    "Confirm payment before moving the order to PREPARING");
        }

        if (!isValidStatusTransition(order.getStatus(), newStatus)) {
            throw new InvalidOrderStatusTransitionException(
                    "Invalid status transition from "
                            + order.getStatus() + " to " + newStatus);
        }

        order.setStatus(newStatus);

        return orderMapper.toResponse(orderRepository.save(order));
    }

    private boolean isValidStatusTransition(
            StatusOrderEntity currentStatus,
            StatusOrderEntity newStatus) {

        if (currentStatus == newStatus) {
            return true;
        }

        switch (currentStatus) {
            case WAITING_PAYMENT:
                return newStatus == StatusOrderEntity.PREPARING;

            case PREPARING:
                return newStatus == StatusOrderEntity.READY;

            case READY:
                return newStatus == StatusOrderEntity.DELIVERED;

            default:
                return false;
        }
    }
}
