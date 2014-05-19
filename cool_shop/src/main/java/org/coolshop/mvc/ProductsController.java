package org.coolshop.mvc;

import org.coolshop.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class ProductsController {

    @Autowired
    ProductDao productDao;

    @RequestMapping({"/"})
    public String listProducts(Model model) {
        model.addAttribute("products", productDao.getProducts());
        return "products";
    }
}
