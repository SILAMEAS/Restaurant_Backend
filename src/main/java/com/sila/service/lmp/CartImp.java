package com.sila.service.lmp;

import com.sila.config.context.UserContext;
import com.sila.dto.response.CartResponse;
import com.sila.exception.BadRequestException;
import com.sila.exception.NotFoundException;
import com.sila.model.Cart;
import com.sila.model.CartItem;
import com.sila.model.Food;
import com.sila.model.User;
import com.sila.repository.CartItemRepository;
import com.sila.repository.CartRepository;
import com.sila.repository.UserRepository;
import com.sila.service.CartService;
import com.sila.service.FoodService;
import com.sila.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartImp implements CartService {
    final UserService userService;
    final FoodService foodService;
    final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    @Override
    public List<CartResponse> getAll() {
        var categories = cartRepository.findAllByUser(UserContext.getUser());
        if(CollectionUtils.isEmpty(categories)){
            return new ArrayList<>();
        }
        return categories.stream().map(CartResponse::toResponse).toList();
    }

    @Override
    @Transactional
    public void addItemToCart(Long foodId, int quantity) {
        var food = foodService.getById(foodId);
        var foodRestaurant = food.getRestaurant(); // Assuming food has a restaurant field

        var carts = cartRepository.findAllByUserId(UserContext.getUser().getId());

        for(var cart:carts){
            for(var f:cart.getItems()){
                var restaruantId = f.getFood().getRestaurant().getId();
                if(Objects.equals(foodRestaurant.getId(), restaruantId)){
                    cartRepository.save(addItemToCartItemExit(cart,food,quantity));
                    return;
                }
            }
        }
        /** Add new Cart when food from new restaurant */

        User user = UserContext.getUser();
        Cart newCart = new Cart();
        newCart.setUser(user);
        cartRepository.save(addItemToCartItemExit(newCart,food,quantity));
        user.getCarts().add(newCart);
        userRepository.save(user);

    }

    private Cart addItemToCartItemExit(Cart cart, Food food, int quantity) {
        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(item -> item.getFood().getId().equals(food.getId()))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setFood(food);
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
        }

        return cart;
    }



    @Override
    public void removeItemFromCart(Long cartId,Long cartItemId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(()-> new NotFoundException("Not found with this cart id"));
        var exited = cartItemRepository.findById(cartItemId).isPresent();
        if(!exited){
            throw new BadRequestException("Not found item cart with this id");
        }
        cart.removeItemById(cartItemId);
        cartRepository.save(cart);
    }

    @Override
    public void updateItemFromCart(Long cartId,Long cartItemId, int quantity) throws Exception {
        Cart cart = cartRepository.findById(cartId).orElseThrow(()-> new NotFoundException("Not found cart with this id"+cartId)); // Get cart of currently authenticated user
        CartItem cartItem = cartItemRepository.findByIdAndCart(cartItemId, cart)
                .orElseThrow(() -> new BadRequestException("Cart item not found in your cart"));

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    @Override
    @Transactional
    public void deleteCart(Long cartId) {
        cartRepository.deleteAllByIdInBatch(Collections.singletonList(cartId));
    }

    private Cart findCartByUser() throws Exception {
        var user = userService.getById(userService.getProfile().getId());
        return cartRepository.findByUser(user).orElseGet(()->{
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });
    }
}
