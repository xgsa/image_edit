package org.coolshop.dao;

import org.coolshop.domain.Product;
import org.coolshop.domain.Upc;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class ProductDao extends BaseDao<Product> {

    public ProductDao() {
        super(Product.class);
    }

    public List<Product> getProducts(int count) {
        return getCurrentSession()
                .createQuery("from Product")
                .setMaxResults(count)
                .list();
    }

    public List<Upc> getProductUpcs(Long product_id) {
        return getCurrentSession()
                .createQuery("from Upc as upc where upc.product.id = :product_id")
                .setLong("product_id", product_id)
                .list();
    }
}
