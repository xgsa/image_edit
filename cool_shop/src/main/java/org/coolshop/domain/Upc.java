package org.coolshop.domain;


public class Upc {

    private Product product;
    private Long id;
    private Long price;  // TODO: Use Currency here?

    public Upc(Product product, Long id, Long price) {
        this.product = product;
        this.id = id;
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public Long getId() {
        return id;
    }

    public Float getPrice() {
        return new Float(price / 100.0);
    }

    public Long getPriceExt() {
        return price;
    }
}
