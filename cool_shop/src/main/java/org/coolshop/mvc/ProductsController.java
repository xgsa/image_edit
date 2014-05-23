package org.coolshop.mvc;

import org.coolshop.domain.Order;
import org.coolshop.domain.Product;
import org.coolshop.domain.User;
import org.coolshop.security.CustomUserDetails;
import org.coolshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;


@Controller
public class ProductsController {

    @Autowired
    ProductService productService;


    private User getCurrentUser(Authentication authentication) {
        return ((CustomUserDetails)authentication.getPrincipal()).getModelUser();
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
    public String viewBasket(Model model, HttpSession session, Authentication authentication) {
        User user = getCurrentUser(authentication);
        if (user != null) {
            model.addAttribute("upcs", productService.getBasket(user).getUpcs());
        }
        return "product/basket";
    }

    @RequestMapping("/basket/add")
    public String addToBasket(Model model, HttpSession session, @RequestParam("id") Long upcId,
                              Authentication authentication) {
        User user = getCurrentUser(authentication);
        if (user != null) {
            Order basket = productService.addToBasket(user, upcId);
            model.addAttribute("upcs", basket.getUpcs());
        }
        return "product/basket";
    }

    @RequestMapping("/basket/remove")
    public String removeFromBasket(Model model, HttpSession session, @RequestParam("id") Long upcId,
                                   Authentication authentication) {
        User user = getCurrentUser(authentication);
        if (user != null) {
            Order basket = productService.removeFromBasket(user, upcId);
            model.addAttribute("upcs", basket.getUpcs());
        }
        return "product/basket";
    }

    @RequestMapping("/basket/submit")
    public String submitBasket(Model model, HttpSession session, Authentication authentication) {
        User user = getCurrentUser(authentication);
        if (user != null) {
            productService.submitBasket(user);
        }
        return "redirect:/";
    }
}
