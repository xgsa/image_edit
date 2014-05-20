package org.coolshop.dao;

import org.coolshop.domain.Product;
import org.coolshop.domain.Upc;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public class ProductDao extends BaseDao<Product> {

    public ProductDao() {
        super(Product.class);
    }

    @Transactional(readOnly = true)
    public List<Product> getProducts() {
        return getCurrentSession()
                .createQuery("from Product")
                .list();
    }

    @Transactional(readOnly = true)
    public List<Upc> getProductUpcs(Long product_id) {
        return getCurrentSession()
                .createQuery("from Upc as upc where upc.product.id = :product_id")
                .setLong("product_id", product_id)
                .list();
    }
}
