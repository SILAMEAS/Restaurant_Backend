package com.sila.service.lmp;

import com.sila.dto.response.CartResponse;
import com.sila.model.Cart;
import com.sila.model.CartItem;
import com.sila.repository.CartRepository;
import com.sila.service.CartService;
import com.sila.service.FoodService;
import com.sila.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartImp implements CartService {
    final UserService userService;
    final FoodService foodService;
    final CartRepository cartRepository;
    final ModelMapper modelMapper;
    @Override
    public List<CartResponse> getAll() throws Exception {
        var categories = cartRepository.findAllByUserId(userService.getProfile().getId()).stream().toList();
        return categories.stream()
                .map(ca -> this.modelMapper.map(ca, CartResponse.class))
                .toList();
    }

    @Override
    public void addItemToCart( Long foodId, int quantity) throws Exception {
        var user = userService.getById(userService.getProfile().getId());
        var food = foodService.getById(foodId);
        Cart cart = cartRepository.findByUser(user).orElseGet(()->{
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setFood(food);
        item.setQuantity(quantity);

        cart.getItems().add(item);
        cartRepository.save(cart);
    }

}
