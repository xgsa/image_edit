package org.coolshop.dao;

import org.coolshop.domain.Order;
import org.coolshop.domain.User;
import org.springframework.stereotype.Repository;


@Repository
public class OrderDao extends BaseDao<Order> {

    public OrderDao() {
        super(Order.class);
    }

    public Order getUserBasket(User user) {
        // Obtain only the last order with "order by" and setMaxResults(1).
        Order basket = (Order) getCurrentSession()
                .createQuery("from XOrder as xorder where xorder.user.id = :user_id order by id desc")
                .setLong("user_id", user.getId())
                .setMaxResults(1)
                .uniqueResult();
        if (basket == null || basket.getStatus() != Order.Status.New) {
            basket = new Order(user);
            getCurrentSession().save(basket);
        }
        return basket;
    }

}
