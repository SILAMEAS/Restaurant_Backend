package com.sila.lmp;

import com.sila.dto.request.OrderReq;
import com.sila.model.*;
import com.sila.repository.AddressRepository;
import com.sila.repository.OrderItemRepository;
import com.sila.repository.OrderRepository;
import com.sila.repository.UserRepository;
import com.sila.service.CardService;
import com.sila.service.OrderService;
import com.sila.service.RestaurantService;
import com.sila.service.UserService;
import com.sila.utlis.enums.ORDER_STATUS;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderImp implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final RestaurantService restaurantService;
    private final AddressRepository addressRepository;
    private final CardService cardService;
    @Override
    public Order createOrder(OrderReq req, User user) throws Exception {
        Address newOrderAddress = req.getDeliveryAddress();
        Address saveAddress=addressRepository.save(newOrderAddress);
        if(!user.getAddresses().contains(saveAddress)){
            user.getAddresses().add(saveAddress);
            userRepository.save(user);
        }
        Restaurant restaurant=restaurantService.findRestaurantById(req.getRestaurantId());
        Order order=new Order();
        order.setCustomer(user);
        order.setRestaurant(restaurant);
        order.setDeliveryAddress(saveAddress);
        order.setOrderStatus(String.valueOf(ORDER_STATUS.PENDING));
        Card card= cardService.findCardByUserId(user.getId());
        List<OrderItem> orderItems=new ArrayList<>();
        for (CardItem cardItem:card.getItem()){
            OrderItem orderItem=new OrderItem();
            orderItem.setFood(cardItem.getFood());
            orderItem.setQuantity(cardItem.getQuantity());
            orderItem.setIngredients(cardItem.getIngredients());
            orderItem.setTotalPrice(cardItem.getTotalPrice());
            OrderItem saveOrderItem= orderItemRepository.save(orderItem);
            orderItems.add(saveOrderItem);
        }
        Long TotalPriceAllItemInCard=cardService.calculateCardTotal(card);
        order.setOrderItems(orderItems);
        order.setCreatedAt(new Date());
        order.setTotalPrice(TotalPriceAllItemInCard);
        Order saveOrder = orderRepository.save(order);
        restaurant.getOrders().add(saveOrder);


        return saveOrder;
    }

    @Override
    public Order updateOrder(Long order_id, String orderStatus) throws Exception {
        Order order = getOrderById(order_id);
        ArrayList<ORDER_STATUS> ConditionCanUpdate = new ArrayList<>();
        ConditionCanUpdate.add(ORDER_STATUS.OUT_FOR_DELIVERY);
        ConditionCanUpdate.add(ORDER_STATUS.DELIVERY);
        ConditionCanUpdate.add(ORDER_STATUS.COMPLETED);
        ConditionCanUpdate.add(ORDER_STATUS.PENDING);
        for (ORDER_STATUS item:ConditionCanUpdate){
            if(String.valueOf(item).matches(orderStatus)){
                order.setOrderStatus(orderStatus);
                return orderRepository.save(order);
            }

        }
       throw new BadRequestException("Your status you update is invalid! Accept only : "
               +ORDER_STATUS.OUT_FOR_DELIVERY+","+ORDER_STATUS.DELIVERY+","
               +ORDER_STATUS.PENDING+","+ORDER_STATUS.COMPLETED);
    }

    @Override
    public void cancelOrder(Long order_id) throws Exception {
        Order orderFindById=getOrderById(order_id);
        orderRepository.deleteById(orderFindById.getId());
    }

    @Override
    public List<Order> getUserOrder(Long user_Id) throws Exception {
        return orderRepository.findByCustomerId(user_Id);
    }

    @Override
    public List<Order> getRestaurantOrder(Long restaurant_Id, String order_status) throws Exception {
        List<Order> findOrderByRestaurantId = orderRepository.findByRestaurantId(restaurant_Id);
        if(order_status.isEmpty()){
            return findOrderByRestaurantId;
        }
        return findOrderByRestaurantId.stream().filter(item-> item.getOrderStatus().equals(order_status)).collect(Collectors.toList());
    }
    @Override
    public Order getOrderById(Long order_Id) throws Exception {
        Optional<Order> order=orderRepository.findById(order_Id);
        if(order.isEmpty()){
            throw new BadRequestException("order id : "+order_Id+" is not found");
        }
        return order.get();
    }
}
