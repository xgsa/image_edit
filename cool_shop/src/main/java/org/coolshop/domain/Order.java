package org.coolshop.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


// NOTE: "order" is a keyword in some of DB engines.
@Entity(name = "XOrder")
public class Order implements BaseEntity {

    public enum Status {
        New,
        Submitted,
        Done,
    }


    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date submittedAt;

    @ManyToMany
    private Set<Upc> upcs;


    // For hibernate, not intended to be used by the clients.
    protected Order() {}

    public Order(User user) {
        this.user = user;
        upcs = new HashSet<>();
        setStatus(Status.New);
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
        switch (status) {
            case New:
                setCreatedAt(new Date());
                setSubmittedAt(null);
                break;
            case Submitted:
                setSubmittedAt(new Date());
        }
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Date submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Set<Upc> getUpcs() {
        return upcs;
    }

    public void setUpcs(Set<Upc> upcs) {
        this.upcs = upcs;
    }

    public Long getTotalSum() {
        long totalSum = 0;
        for (Upc upc : upcs) {
            totalSum += upc.getPrice();
        }
        return totalSum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (createdAt != null ? !createdAt.equals(order.createdAt) : order.createdAt != null) return false;
        if (id != null ? !id.equals(order.id) : order.id != null) return false;
        if (status != order.status) return false;
        if (submittedAt != null ? !submittedAt.equals(order.submittedAt) : order.submittedAt != null) return false;
        if (upcs != null ? !upcs.equals(order.upcs) : order.upcs != null) return false;
        if (user != null ? !user.equals(order.user) : order.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (submittedAt != null ? submittedAt.hashCode() : 0);
        result = 31 * result + (upcs != null ? upcs.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user=" + user +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", submittedAt=" + submittedAt +
                '}';
    }
}
