package org.coolshop.service;

import org.coolshop.dao.OrderDao;
import org.coolshop.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;


    @Transactional(readOnly = true)
    public List<Order> getOrders(Order.Status orderStatus) {
        List<Order> orders = orderDao.getOrders(orderStatus);
        for (Order order: orders) {
            order.getTotalSum();  // Overcome laziness
        }
        return orders;
    }

    @Transactional
    public void markOrderAsDone(Long orderId) {
        orderDao.get(orderId).setStatus(Order.Status.Done);
    }
}
