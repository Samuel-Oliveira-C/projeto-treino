package org.treino.demo.controllers;

import java.net.URI;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.treino.demo.dtos.requests.OrderItemRequestDTO;
import org.treino.demo.dtos.responses.OrderItemResponseDTO;
import org.treino.demo.services.OrderItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders/{orderId}/items")
public class OrderItemController {
    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @PostMapping
    public ResponseEntity<OrderItemResponseDTO> create(
            @PathVariable UUID orderId,
            @Valid @RequestBody OrderItemRequestDTO request) {
        OrderItemResponseDTO createdItem = orderItemService.createOrderItem(orderId, request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{productId}")
                .buildAndExpand(createdItem.productID())
                .toUri();

        return ResponseEntity.created(location).body(createdItem);
    }

    @GetMapping
    public ResponseEntity<Page<OrderItemResponseDTO>> getAll(
            @PathVariable UUID orderId,
            Pageable pageable) {
        return ResponseEntity.ok(orderItemService.getOrderItems(orderId, pageable));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<OrderItemResponseDTO> getById(
            @PathVariable UUID orderId,
            @PathVariable UUID productId) {
        return ResponseEntity.ok(orderItemService.getOrderItem(orderId, productId));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<OrderItemResponseDTO> update(
            @PathVariable UUID orderId,
            @PathVariable UUID productId,
            @Valid @RequestBody OrderItemRequestDTO request) {
        return ResponseEntity.ok(
                orderItemService.updateOrderItem(orderId, productId, request));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID orderId,
            @PathVariable UUID productId) {
        orderItemService.deleteOrderItem(orderId, productId);
        return ResponseEntity.noContent().build();
    }
}
