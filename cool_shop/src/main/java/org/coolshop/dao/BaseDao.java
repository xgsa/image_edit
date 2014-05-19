package org.coolshop.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class BaseDao<EntityType> {

    private Class<EntityType> EntityClass;

    @Autowired
    private SessionFactory sessionFactory;


    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Transactional(readOnly = true)
    public EntityType get(Long id) {
        return (EntityType) getCurrentSession().get(EntityClass, id);
    }
}
