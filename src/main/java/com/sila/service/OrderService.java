package com.sila.service;

import com.sila.dto.request.OrderReq;
import com.sila.model.Order;
import com.sila.model.User;

import java.util.List;

public interface OrderService {
    public Order createOrder(OrderReq req, User user) throws Exception;
    public Order updateOrder(Long order_id,String orderStatus)throws Exception;
    public void cancelOrder(Long order_id)throws Exception;
    public List<Order> getUserOrder(Long user_Id)throws  Exception;
    public List<Order> getRestaurantOrder(Long restaurant_Id,String order_status)throws  Exception;
    public Order getOrderById(Long order_Id)throws Exception;
}
