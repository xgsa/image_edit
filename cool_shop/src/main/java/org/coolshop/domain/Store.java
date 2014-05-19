package org.coolshop.domain;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Store {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    private String address;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Upc> upcs;


    // For hibernate, not intended to be used by the clients.
    protected Store() {}

    public Store(String name, String address) {
        this.name = name;
        this.address = address;
        upcs = new HashSet<>();
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    void addUpcWithoutFeedback(Upc upc) {
        upcs.add(upc);
    }

    public void addUpc(Upc upc) {
        addUpcWithoutFeedback(upc);
        upc.addStoreWithoutFeedback(this);
    }

    void removeUpcWithoutFeedback(Upc upc) {
        upcs.remove(upc);
    }

    public void removeUpc(Upc upc) {
        removeUpcWithoutFeedback(upc);
        upc.removeStoreWithoutFeedback(this);
    }

    public Iterable<Upc> listUpcs() {
        return upcs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Store store = (Store) o;

        if (address != null ? !address.equals(store.address) : store.address != null) return false;
        if (id != null ? !id.equals(store.id) : store.id != null) return false;
        if (name != null ? !name.equals(store.name) : store.name != null) return false;
        if (upcs != null ? !upcs.equals(store.upcs) : store.upcs != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (upcs != null ? upcs.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Store{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
