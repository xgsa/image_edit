package org.coolshop.domain;

import java.util.HashSet;
import java.util.Set;


public class Upc {

    private Long id;
    private Long price;
    private Product product;
    private Set<AttributeValue> attributes;
    private Set<Store> stores;


    // For hibernate, not intended to be used by the clients.
    protected Upc() {}

    public Upc(Product product, Long id, Long price) {
        this.product = product;
        this.id = id;
        this.price = price;
        attributes = new HashSet<>();
        stores = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Set<AttributeValue> getAttributes() {
        return attributes;
    }

    public void setAttributes(Set<AttributeValue> attributes) {
        this.attributes = attributes;
    }

    void addStoreWithoutFeedback(Store store) {
        stores.add(store);
    }

    public void addStore(Store store) {
        addStoreWithoutFeedback(store);
        store.addUpcWithoutFeedback(this);
    }

    void removeStoreWithoutFeedback(Store store) {
        stores.remove(store);
    }

    public void removeStore(Store store) {
        removeStoreWithoutFeedback(store);
        store.removeUpcWithoutFeedback(this);
    }

    public Iterable<Store> listStores() {
        return stores;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Upc upc = (Upc) o;

        if (attributes != null ? !attributes.equals(upc.attributes) : upc.attributes != null) return false;
        if (id != null ? !id.equals(upc.id) : upc.id != null) return false;
        if (price != null ? !price.equals(upc.price) : upc.price != null) return false;
        if (product != null ? !product.equals(upc.product) : upc.product != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (product != null ? product.hashCode() : 0);
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Upc{" +
                "id=" + id +
                ", product=" + product +
                ", price=" + price +
                '}';
    }
}
