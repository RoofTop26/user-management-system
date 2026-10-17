package com.example.user_management_system.controller;

import com.example.user_management_system.dto.request.CartItemRequest;
import com.example.user_management_system.dto.response.CartItemResponse;
import com.example.user_management_system.dto.response.CartResponse;
import com.example.user_management_system.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/portal/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(HttpServletRequest request) {
        String username = (String) request.getAttribute("authenticatedUser");
        return ResponseEntity.ok(cartService.getCart(username));
    }

    @PostMapping
    public ResponseEntity<CartItemResponse> addToCart(@Valid @RequestBody CartItemRequest cartItemRequest, HttpServletRequest request) {
        String username = (String) request.getAttribute("authenticatedUser");
        CartItemResponse created = cartService.addToCart(username, cartItemRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<CartItemResponse> updateCartItem(@PathVariable Long itemId, @Valid @RequestBody CartItemRequest cartItemRequest, HttpServletRequest request) {
        String username = (String) request.getAttribute("authenticatedUser");
        return ResponseEntity.ok(cartService.updateCartItem(username, itemId, cartItemRequest.getQuantity()));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long itemId, HttpServletRequest request) {
        String username = (String) request.getAttribute("authenticatedUser");
        cartService.removeFromCart(username, itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(HttpServletRequest request) {
        String username = (String) request.getAttribute("authenticatedUser");
        cartService.clearCart(username);
        return ResponseEntity.noContent().build();
    }
}
