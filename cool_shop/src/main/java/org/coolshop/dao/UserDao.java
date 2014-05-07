package org.coolshop.dao;

import org.coolshop.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.sql.ResultSet;
import java.sql.SQLException;


public class UserDao extends JdbcDaoSupport {

    public void addUser(User user) {
        int rows = getJdbcTemplate().update(
                "INSERT INTO user (id, login, password, full_name) VALUES (?, ?, ?, ?)",
                user.getId(), user.getLogin(), user.getPassword(), user.getFullName());
        if (rows != 1) {
            throw new IllegalArgumentException("Unable to add "+user);
        }
    }

    public User getUser(String name) {
        try {
            return getJdbcTemplate().queryForObject(
                    "SELECT id, login, password, full_name FROM user WHERE login=?",
                    new ParameterizedRowMapper<User>() {
                        public User mapRow(ResultSet rs, int rowNum)
                                throws SQLException {
                            return new User(rs.getLong(1), rs.getString(2), rs.getBytes(3), rs.getString(4));
                        }
                    },
                    name
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
