package com.sila.service.lmp;

import com.sila.config.context.UserContext;
import com.sila.dto.response.OrderItemResponse;
import com.sila.dto.response.OrderResponse;
import com.sila.dto.response.RestaurantResponse;
import com.sila.dto.response.UserResponse;
import com.sila.exception.BadRequestException;
import com.sila.exception.NotFoundException;
import com.sila.model.Cart;
import com.sila.model.CartItem;
import com.sila.model.Order;
import com.sila.model.OrderItem;
import com.sila.model.Restaurant;
import com.sila.repository.CartRepository;
import com.sila.repository.OrderItemRepository;
import com.sila.repository.OrderRepository;
import com.sila.service.OrderService;
import com.sila.util.enums.PAYMENT_STATUS;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderImp implements OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final OrderItemRepository orderItemRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<OrderResponse> getAll() {
        return orderRepository.findAll().stream()
                .map(this::convertToOrderResponse)
                .toList();
    }

    @Override
    @Transactional
    public OrderResponse placeOrder() {
        // Get the authenticated user
        final var user = UserContext.getUser();

        // Fetch the user's cart
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException("Cart not found for user"));

        // Check if cart has items
        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cannot place order with an empty cart");
        }

        // Create a new order
        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(PAYMENT_STATUS.PENDING);

        // Assume all items in the cart are from the same restaurant
        Restaurant restaurant = cart.getItems().get(0).getFood().getRestaurant();
        order.setRestaurant(restaurant);

        // Convert CartItems to OrderItems and calculate total amount
        double totalAmount = 0.0;
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order); // Ensure the order is set
            orderItem.setFood(cartItem.getFood());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItems.add(orderItem);

            // Calculate total amount (assuming Food has a price field)
            totalAmount += cartItem.getFood().getPrice() * cartItem.getQuantity();
        }

        // Set the order items and total amount
        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);

        // Save the order first to generate its ID
        orderRepository.save(order);

        // Save each OrderItem individually to ensure IDs are generated
        orderItemRepository.saveAll(orderItems);

        // Clear the cart
        cart.getItems().clear();
        cartRepository.save(cart);

        // Convert to OrderResponse
        return convertToOrderResponse(order);
    }
    private OrderResponse convertToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setUser(this.modelMapper.map(order.getUser(), UserResponse.class));
        response.setRestaurant(this.modelMapper.map(order.getRestaurant(), RestaurantResponse.class));
        response.setTotalAmount(order.getTotalAmount());
        response.setCreatedAt(order.getCreatedAt());
        response.setStatus(order.getStatus().name());
        response.setItems(order.getItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getId(),
                        item.getFood().getId(),
                        item.getQuantity(),
                        item.getFood().getPrice() * item.getQuantity()
                ))
                .toList());
        return response;
    }

}
