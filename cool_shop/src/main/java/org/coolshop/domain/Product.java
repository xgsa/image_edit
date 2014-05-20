package org.coolshop.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<AttributeValue> attributes;

    private String imageReference;


    // For hibernate, not intended to be used by the clients.
    protected Product() {}

    public Product(String name, String imageReference) {
        this.name = name;
        this.imageReference = imageReference;
        attributes = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<AttributeValue> getAttributes() {
        return attributes;
    }

    public void setAttributes(Set<AttributeValue> attributes) {
        this.attributes = attributes;
    }

    public String getImageReference() {
        return imageReference;
    }

    public void setImageReference(String imageReference) {
        this.imageReference = imageReference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (attributes != null ? !attributes.equals(product.attributes) : product.attributes != null) return false;
        if (id != null ? !id.equals(product.id) : product.id != null) return false;
        if (imageReference != null ? !imageReference.equals(product.imageReference) : product.imageReference != null)
            return false;
        if (name != null ? !name.equals(product.name) : product.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        result = 31 * result + (imageReference != null ? imageReference.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imageReference='" + imageReference + '\'' +
                ", attributes=" + attributes +
                '}';
    }
}
