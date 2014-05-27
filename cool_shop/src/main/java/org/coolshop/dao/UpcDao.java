package org.coolshop.dao;

import org.coolshop.domain.Upc;
import org.springframework.stereotype.Repository;


@Repository
public class UpcDao extends BaseDao<Upc> {

    @Override
    protected Class<Upc> getEntityClass() {
        return Upc.class;
    }

}
