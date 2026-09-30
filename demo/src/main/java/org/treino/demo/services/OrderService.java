package org.treino.demo.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.treino.demo.dtos.requests.OrderRequestDTO;
import org.treino.demo.dtos.responses.OrderResponseDTO;
import org.treino.demo.entities.CustomerEntity;
import org.treino.demo.entities.OrderEntity;
import org.treino.demo.enums.StatusOrderEntity;
import org.treino.demo.mappers.OrderMapper;
import org.treino.demo.repositories.CustomerRepository;
import org.treino.demo.repositories.OrderRepository;
import org.treino.demo.exceptions.InvalidOrderStatusTransitionException;
import org.treino.demo.exceptions.OrderDeletionNotAllowedException;
import org.treino.demo.exceptions.ResourceNotFoundException;

@Service 
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional 
    public OrderResponseDTO createOrder(OrderRequestDTO requestDTO) {
        CustomerEntity customer = customerRepository.findById(requestDTO.customerID())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Customer not found with ID: " + requestDTO.customerID()));
        
        OrderEntity order = orderMapper.toEntity(requestDTO);
        order.setStatus(StatusOrderEntity.WAITING_PAYMENT);
        
        order.setCustomer(customer);
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

    @Transactional(readOnly = true)
    public Page<OrderResponseDTO> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMapper::toResponse);
    }

    @Transactional 
    public void deleteOrder(UUID orderID) {
        OrderEntity order = orderRepository.findById(orderID)
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
        OrderEntity order = orderRepository.findById(orderID)
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
        OrderEntity order = orderRepository.findById(orderID)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Order not found with ID: " + orderID));

        if (newStatus == null) {
            throw new IllegalArgumentException("New status cannot be null");
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
