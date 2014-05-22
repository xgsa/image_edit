package org.coolshop.mvc;

import org.coolshop.domain.Order;
import org.coolshop.domain.Product;
import org.coolshop.domain.User;
import org.coolshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;


@Controller
public class ProductsController {

    @Autowired
    ProductService productService;


    private User getSessionUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    @RequestMapping("/")
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getProducts(10));
        return "product/products";
    }

    @RequestMapping("/product")
    public String viewProduct(Model model, @RequestParam("id") Long productId) {
        Product product = productService.getProduct(productId, true, true);
        model.addAttribute("product", product);
        return "product/product_info";
    }

    @RequestMapping("/upc")
    public String viewUpc(Model model, @RequestParam("id") Long upcId) {
        model.addAttribute("upc", productService.getUpc(upcId, true));
        return "product/product_info";
    }

    @RequestMapping("/basket/list")
    public String viewBasket(Model model, HttpSession session) {
        User user = getSessionUser(session);
        if (user != null) {
            model.addAttribute("upcs", productService.getBasket(getSessionUser(session)).getUpcs());
        }
        return "product/basket";
    }

    @RequestMapping("/basket/add")
    public String addToBasket(Model model, HttpSession session, @RequestParam("id") Long upcId) {
        User user = getSessionUser(session);
        if (user != null) {
            Order basket = productService.addToBasket(getSessionUser(session), upcId);
            model.addAttribute("upcs", basket.getUpcs());
        }
        return "product/basket";
    }

    @RequestMapping("/basket/remove")
    public String removeFromBasket(Model model, HttpSession session, @RequestParam("id") Long upcId) {
        User user = getSessionUser(session);
        if (user != null) {
            Order basket = productService.removeFromBasket(getSessionUser(session), upcId);
            model.addAttribute("upcs", basket.getUpcs());
        }
        return "product/basket";
    }

    @RequestMapping("/basket/submit")
    public String submitBasket(Model model, HttpSession session) {
        User user = getSessionUser(session);
        if (user != null) {
            productService.submitBasket(user);
        }
        return "redirect:/";
    }
}
