package com.example.user_management_system.service;

import com.example.user_management_system.dto.request.CartItemRequest;
import com.example.user_management_system.dto.response.CartItemResponse;
import com.example.user_management_system.dto.response.CartResponse;
import com.example.user_management_system.entity.CartItem;
import com.example.user_management_system.entity.Product;
import com.example.user_management_system.entity.User;
import com.example.user_management_system.exception.CartItemNotFoundException;
import com.example.user_management_system.exception.CartValidationException;
import com.example.user_management_system.exception.ProductNotFoundException;
import com.example.user_management_system.exception.UserNotFoundException;
import com.example.user_management_system.repository.CartItemRepository;
import com.example.user_management_system.repository.ProductRepository;
import com.example.user_management_system.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartService(CartItemRepository cartItemRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public CartResponse getCart(String username) {
        User user = findUser(username);

        List<CartItemResponse> items = cartItemRepository.findByUserId(user.getId()).stream()
                .map(this::toResponse)
                .toList();

        BigDecimal totalAmount = items.stream()
                .map(CartItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(items, totalAmount);
    }

    public CartItemResponse addToCart(String username, CartItemRequest request) {
        User user = findUser(username);

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(request.getProductId()));

        if (product.getStatus() != Product.Status.ACTIVE) {
            throw new CartValidationException("Sản phẩm không còn được bán");
        }

        Optional<CartItem> existing = cartItemRepository.findByUserIdAndProductId(user.getId(), product.getId());

        int newQuantity = request.getQuantity();
        if (existing.isPresent()) {
            newQuantity = newQuantity + existing.get().getQuantity();
        }

        if (product.getStock() < newQuantity) {
            throw new CartValidationException("Không đủ hàng trong kho");
        }

        CartItem cartItem;
        if (existing.isPresent()) {
            cartItem = existing.get();
        } else {
            cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
        }
        cartItem.setQuantity(newQuantity);

        return toResponse(cartItemRepository.save(cartItem));
    }

    public CartItemResponse updateCartItem(String username, Long itemId, Integer quantity) {
        User user = findUser(username);
        CartItem cartItem = findCartItem(user, itemId);

        if (cartItem.getProduct().getStock() < quantity) {
            throw new CartValidationException("Không đủ hàng trong kho");
        }

        cartItem.setQuantity(quantity);
        return toResponse(cartItemRepository.save(cartItem));
    }

    public void removeFromCart(String username, Long itemId) {
        User user = findUser(username);
        CartItem cartItem = findCartItem(user, itemId);
        cartItemRepository.delete(cartItem);
    }

    public void clearCart(String username) {
        User user = findUser(username);
        cartItemRepository.deleteAll(cartItemRepository.findByUserId(user.getId()));
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    private CartItem findCartItem(User user, Long itemId) {
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new CartItemNotFoundException(itemId));

        if (!cartItem.getUser().getId().equals(user.getId())) {
            throw new CartItemNotFoundException(itemId);
        }

        return cartItem;
    }

    private CartItemResponse toResponse(CartItem cartItem) {
        Product product = cartItem.getProduct();
        BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));

        return new CartItemResponse(
                cartItem.getId(),
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                cartItem.getQuantity(),
                subtotal);
    }
}
