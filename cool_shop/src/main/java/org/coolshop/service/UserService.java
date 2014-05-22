package org.coolshop.service;

import org.coolshop.dao.UserDao;
import org.coolshop.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {

    @Autowired
    UserDao userDao;


    @Transactional(readOnly = true)
    public User getUser(String login) {
        return userDao.getUser(login);
    }
}
