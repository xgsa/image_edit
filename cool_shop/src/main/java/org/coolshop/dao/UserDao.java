package org.coolshop.dao;

import org.coolshop.domain.User;
import org.springframework.stereotype.Repository;


@Repository
public class UserDao extends BaseDao<User> {

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    public void addUser(User user) {
        getCurrentSession().save(user);
    }

    public User get(String login) {
        return (User) getCurrentSession()
                .createQuery("from XUser as user where user.login = :login")
                .setString("login", login)
                .uniqueResult();
    }
}
