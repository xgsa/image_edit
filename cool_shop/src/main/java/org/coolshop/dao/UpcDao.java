package org.coolshop.dao;

import org.coolshop.domain.Upc;
import org.springframework.stereotype.Repository;


@Repository
public class UpcDao extends BaseDao<Upc> {

    public UpcDao() {
        super(Upc.class);
    }

}
