package com.reins.bookstore.dao;

import com.reins.bookstore.entity.CartItem;
import com.reins.bookstore.entity.Order;
import com.reins.bookstore.models.OrderInfo;

import java.util.List;
import java.util.Optional;

public interface OrderDAO {
    void saveOrder(Long userId, OrderInfo orderInfo, List<CartItem> items);

    void cancelOrder(Long orderId);

    Order getOrderById(Long orderId);

    List<Order> getByUserId(Long userId);
}
