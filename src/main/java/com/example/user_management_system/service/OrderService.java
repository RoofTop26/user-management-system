package com.example.user_management_system.service;

import com.example.user_management_system.dto.request.CheckoutRequest;
import com.example.user_management_system.dto.response.AdminOrderResponse;
import com.example.user_management_system.dto.response.OrderItemResponse;
import com.example.user_management_system.dto.response.OrderResponse;
import com.example.user_management_system.entity.CartItem;
import com.example.user_management_system.entity.Order;
import com.example.user_management_system.entity.OrderItem;
import com.example.user_management_system.entity.Product;
import com.example.user_management_system.entity.User;
import com.example.user_management_system.exception.CartValidationException;
import com.example.user_management_system.exception.UserNotFoundException;
import com.example.user_management_system.repository.CartItemRepository;
import com.example.user_management_system.repository.OrderItemRepository;
import com.example.user_management_system.repository.OrderRepository;
import com.example.user_management_system.repository.ProductRepository;
import com.example.user_management_system.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CartItemRepository cartItemRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public OrderResponse checkout(String username, CheckoutRequest request) {
        User user = findUser(username);

        List<CartItem> cartItems = cartItemRepository.findByUserId(user.getId());

        if (cartItems.isEmpty()) {
            throw new CartValidationException("Giỏ hàng trống");
        }

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            if (product.getStatus() != Product.Status.ACTIVE) {
                throw new CartValidationException("Sản phẩm " + product.getName() + " không còn được bán");
            }

            if (product.getStock() < cartItem.getQuantity()) {
                throw new CartValidationException("Sản phẩm " + product.getName() + " không đủ hàng trong kho");
            }
        }

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(totalAmount);
        order.setStatus(Order.Status.PENDING);
        order.setNote(request.getNote());
        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setProductName(product.getName());
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            orderItems.add(orderItemRepository.save(orderItem));
        }

        cartItemRepository.deleteAll(cartItems);

        return toResponse(savedOrder, orderItems);
    }

    public List<OrderResponse> getOrders(String username) {
        User user = findUser(username);

        return orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId()).stream()
                .map(order -> toResponse(order, orderItemRepository.findByOrderId(order.getId())))
                .toList();
    }

    public OrderResponse getOrder(String username, Long orderId) {
        User user = findUser(username);
        Order order = findOrder(user, orderId);
        return toResponse(order, orderItemRepository.findByOrderId(order.getId()));
    }

    public OrderResponse cancelOrder(String username, Long orderId) {
        User user = findUser(username);
        Order order = findOrder(user, orderId);

        if (order.getStatus() != Order.Status.PENDING) {
            throw new CartValidationException("Chỉ được hủy đơn hàng đang chờ xác nhận");
        }

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());

        for (OrderItem orderItem : orderItems) {
            Product product = orderItem.getProduct();
            product.setStock(product.getStock() + orderItem.getQuantity());
            productRepository.save(product);
        }

        order.setStatus(Order.Status.CANCELLED);
        Order savedOrder = orderRepository.save(order);

        return toResponse(savedOrder, orderItems);
    }

    public List<AdminOrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> toAdminResponse(order, orderItemRepository.findByOrderId(order.getId())))
                .toList();
    }

    public AdminOrderResponse getAdminOrder(Long orderId) {
        Order order = findOrderById(orderId);
        return toAdminResponse(order, orderItemRepository.findByOrderId(order.getId()));
    }

    public AdminOrderResponse confirmOrder(Long orderId) {
        Order order = findOrderById(orderId);

        if (order.getStatus() != Order.Status.PENDING) {
            throw new CartValidationException("Chỉ được xác nhận đơn hàng đang chờ xác nhận");
        }

        order.setStatus(Order.Status.CONFIRMED);
        Order savedOrder = orderRepository.save(order);

        return toAdminResponse(savedOrder, orderItemRepository.findByOrderId(savedOrder.getId()));
    }

    public AdminOrderResponse adminCancelOrder(Long orderId) {
        Order order = findOrderById(orderId);

        if (order.getStatus() == Order.Status.CANCELLED) {
            throw new CartValidationException("Đơn hàng đã bị hủy");
        }

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());

        for (OrderItem orderItem : orderItems) {
            Product product = orderItem.getProduct();
            product.setStock(product.getStock() + orderItem.getQuantity());
            productRepository.save(product);
        }

        order.setStatus(Order.Status.CANCELLED);
        Order savedOrder = orderRepository.save(order);

        return toAdminResponse(savedOrder, orderItems);
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    private Order findOrder(User user, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CartValidationException("Không tìm thấy đơn hàng với id: " + orderId));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new CartValidationException("Không tìm thấy đơn hàng với id: " + orderId);
        }

        return order;
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new CartValidationException("Không tìm thấy đơn hàng với id: " + orderId));
    }

    private AdminOrderResponse toAdminResponse(Order order, List<OrderItem> orderItems) {
        OrderResponse orderResponse = toResponse(order, orderItems);

        return new AdminOrderResponse(
                order.getId(),
                order.getUser().getId(),
                order.getUser().getUsername(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getNote(),
                order.getCreatedAt(),
                orderResponse.getItems());
    }

    private OrderResponse toResponse(Order order, List<OrderItem> orderItems) {
        List<OrderItemResponse> items = orderItems.stream()
                .map(orderItem -> new OrderItemResponse(
                        orderItem.getProduct().getId(),
                        orderItem.getProductName(),
                        orderItem.getPrice(),
                        orderItem.getQuantity(),
                        orderItem.getSubtotal()))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getNote(),
                order.getCreatedAt(),
                items);
    }
}
