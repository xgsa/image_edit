package org.coolshop.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public class Order {

    public enum Status {
        New,
        Submitted,
        Done,
    }


    private Long id;
    private User user;
    private Status status;
    private Date createdAt;
    private Date submittedAt;
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
