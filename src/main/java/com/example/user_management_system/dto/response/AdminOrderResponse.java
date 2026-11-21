package com.example.user_management_system.dto.response;

import com.example.user_management_system.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class AdminOrderResponse {

    private Long id;
    private Long userId;
    private String userName;
    private BigDecimal totalAmount;
    private Order.Status status;
    private String note;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;

    public AdminOrderResponse(Long id, Long userId, String userName, BigDecimal totalAmount, Order.Status status, String note, LocalDateTime createdAt, List<OrderItemResponse> items) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.totalAmount = totalAmount;
        this.status = status;
        this.note = note;
        this.createdAt = createdAt;
        this.items = items;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getUserName() { return userName; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public Order.Status getStatus() { return status; }
    public String getNote() { return note; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<OrderItemResponse> getItems() { return items; }
}
