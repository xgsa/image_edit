package org.coolshop.dao;

import org.coolshop.domain.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public class UserDao extends BaseDao<User> {

    public void addUser(User user) {
        getCurrentSession().save(user);
    }

    @Transactional(readOnly = true)
    public User getUser(String login) {
        return (User) getCurrentSession()
                .createQuery("from XUser as user where user.login = :login")
                .setString("login", login)
                .uniqueResult();
    }
}
