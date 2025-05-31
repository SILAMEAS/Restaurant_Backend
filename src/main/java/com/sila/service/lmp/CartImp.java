package com.sila.service.lmp;

import com.sila.config.context.UserContext;
import com.sila.dto.response.CartResponse;
import com.sila.exception.BadRequestException;
import com.sila.model.Cart;
import com.sila.model.CartItem;
import com.sila.repository.CartItemRepository;
import com.sila.repository.CartRepository;
import com.sila.service.CartService;
import com.sila.service.FoodService;
import com.sila.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartImp implements CartService {
    final UserService userService;
    final FoodService foodService;
    final CartRepository cartRepository;
    final ModelMapper modelMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartResponse getAll() {
        var categories = cartRepository.findByUser(userService.getById(UserContext.getUser().getId())).orElseThrow(()->
                new BadRequestException("Cart not found with this user")
        );
        return CartResponse.toResponse(categories);
    }

    @Override
    public void addItemToCart( Long foodId, int quantity) throws Exception {
        Cart cart = findCartByUser();
        var food = foodService.getById(foodId);
        // Check if the item already exists in the cart
        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(item -> item.getFood().getId().equals(foodId))
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

        cartRepository.save(cart); // Cascade should handle CartItem persistence
    }

    @Override
    public void removeItemFromCart(Long cartItemId) throws Exception {
        Cart cart = findCartByUser();
        var exited = cartItemRepository.findById(cartItemId).isPresent();
        if(!exited){
            throw new BadRequestException("Not found item cart with this id");
        }
        cart.removeItemById(cartItemId);
        cartRepository.save(cart);
    }

    @Override
    public void updateItemFromCart(Long cartItemId, int quantity) throws Exception {
        Cart cart = findCartByUser();
        var exited = cartItemRepository.findById(cartItemId).isPresent();
        if(!exited){
            throw new BadRequestException("Not found item cart with this id");
        }
        var cartItem= cartItemRepository.findByCart(cart).orElseThrow(()->
             new BadRequestException("Not found item cart with this id")
        );
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

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
