package com.example.user_management_system.controller;

import com.example.user_management_system.dto.request.CheckoutRequest;
import com.example.user_management_system.dto.response.OrderResponse;
import com.example.user_management_system.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/portal")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(@RequestBody CheckoutRequest checkoutRequest, HttpServletRequest request) {
        String username = (String) request.getAttribute("authenticatedUser");
        OrderResponse created = orderService.checkout(username, checkoutRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getOrders(HttpServletRequest request) {
        String username = (String) request.getAttribute("authenticatedUser");
        return ResponseEntity.ok(orderService.getOrders(username));
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id, HttpServletRequest request) {
        String username = (String) request.getAttribute("authenticatedUser");
        return ResponseEntity.ok(orderService.getOrder(username, id));
    }

    @PostMapping("/orders/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long id, HttpServletRequest request) {
        String username = (String) request.getAttribute("authenticatedUser");
        return ResponseEntity.ok(orderService.cancelOrder(username, id));
    }
}
