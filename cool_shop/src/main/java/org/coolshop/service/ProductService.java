package org.coolshop.service;

import org.coolshop.dao.OrderDao;
import org.coolshop.dao.ProductDao;
import org.coolshop.dao.UpcDao;
import org.coolshop.domain.Order;
import org.coolshop.domain.Product;
import org.coolshop.domain.Upc;
import org.coolshop.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ProductService {

    @Autowired
    ProductDao productDao;

    @Autowired
    UpcDao upcDao;

    @Autowired
    OrderDao orderDao;


    @Transactional(readOnly = true)
    public List<Product> getProducts(int count) {
        return productDao.getProducts(count);
    }

    @Transactional(readOnly = true)
    public Product getProduct(Long productId, boolean fetchAttributes, boolean fetchUpcs) {
        Product product = productDao.get(productId);
        if (fetchAttributes) {
            product.getAttributes().size();  // Overcome laziness
        }
        if (fetchUpcs) {
            product.getUpcs().size();  // Overcome laziness
        }
        return product;
    }

    @Transactional(readOnly = true)
    public Upc getUpc(Long upcId, boolean fetchAttributes) {
        Upc upc = upcDao.get(upcId);
        if (fetchAttributes) {
            // Overcome laziness
            upc.getAttributes().size();
            upc.getProduct().getAttributes().size();
        }
        return upc;
    }

    @Transactional(readOnly = true)
    public Order getBasket(User user) {
        Order basket = orderDao.getUserBasket(user);
        basket.getUpcs().size();  // Overcome laziness
        return basket;
    }

    @Transactional
    public Order addToBasket(User user, Long upcId) {
        Upc upc = upcDao.get(upcId);
        Order basket = orderDao.getUserBasket(user);
        basket.getUpcs().add(upc);
        return basket;
    }

    @Transactional
    public Order removeFromBasket(User user, Long upcId) {
        Upc upc = upcDao.get(upcId);
        Order basket = orderDao.getUserBasket(user);
        basket.getUpcs().remove(upc);
        return basket;
    }

    @Transactional
    public void submitBasket(User user) {
        orderDao.getUserBasket(user).setStatus(Order.Status.Submitted);
    }
}
