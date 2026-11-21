package com.example.user_management_system.controller;

import com.example.user_management_system.dto.response.AdminOrderResponse;
import com.example.user_management_system.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<AdminOrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminOrderResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getAdminOrder(id));
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<AdminOrderResponse> confirmOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.confirmOrder(id));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<AdminOrderResponse> cancelOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.adminCancelOrder(id));
    }
}
