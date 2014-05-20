package org.coolshop.mvc;

import org.coolshop.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ProductsController {

    @Autowired
    ProductDao productDao;


    @RequestMapping("/")
    public String listProducts(Model model) {
        model.addAttribute("products", productDao.getProducts());
        return "product/products";
    }

    @RequestMapping("/product")
    public String viewProducts(Model model, @RequestParam("id")Long id) {
        model.addAttribute("product", productDao.get(id));
        model.addAttribute("upcs", productDao.getProductUpcs(id));
        return "product/product_info";
    }
}
