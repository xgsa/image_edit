package org.coolshop.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


public class BaseDao<T> {

    private Class<T> entityClass;

    @Autowired
    private SessionFactory sessionFactory;


    public BaseDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Transactional(readOnly = true)
    public T get(Long id) {
        return (T) getCurrentSession().get(entityClass, id);
    }
}
