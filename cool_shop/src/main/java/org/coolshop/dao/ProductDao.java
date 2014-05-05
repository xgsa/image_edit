package org.coolshop.dao;

import org.coolshop.domain.Product;
import org.coolshop.domain.Upc;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class ProductDao extends JdbcDaoSupport {

    public List<Upc> getUpcs() {
        return getJdbcTemplate().query(
                "SELECT product.id, name, upc.id, price FROM upc INNER JOIN product ON upc.product_id=product.id",
                new ParameterizedRowMapper<Upc>() {
                    public Upc mapRow(ResultSet rs, int rowNum)
                            throws SQLException {
                        // TODO: Implement products cache by id
                        return new Upc(new Product(rs.getLong(1), rs.getString(2)), rs.getLong(3), rs.getLong(4));
                    }
                }
        );
    }
}
