package org.coolshop.mvc;

import org.coolshop.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;


@Controller
public class ProductsController {

    @Autowired
    ProductDao productDao;

    @RequestMapping({"/"})
    public String listProducts(Map<String, Object> model) {
        model.put("upcs", productDao.getUpcs());
        return "products";
    }
}
