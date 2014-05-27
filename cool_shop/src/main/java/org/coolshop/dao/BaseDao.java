package org.coolshop.dao;

import org.coolshop.domain.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public abstract class BaseDao<T> {

    @Autowired
    private SessionFactory sessionFactory;


    protected abstract Class<T> getEntityClass();

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public T get(Long id) {
        return (T) getCurrentSession().get(getEntityClass(), id);
    }

    public List<T> getAll(int maxResults) {
        return getCurrentSession()
                .createCriteria(getEntityClass())
                .setMaxResults(maxResults)
                .list();
    }
}
