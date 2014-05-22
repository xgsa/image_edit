package org.coolshop.mvc;

import org.coolshop.domain.Order;
import org.coolshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


@Controller
@RequestMapping({"/order"})
public class OrdersController {

    @Autowired
    OrderService orderService;


    @RequestMapping(value = "/list", method = GET)
    public String viewOrders(Model model) {
        model.addAttribute("orders", orderService.getOrders(Order.Status.Submitted));
        return "order/orders_list";
    }

    @RequestMapping(value = "/done", method = GET)
    public String markOrderAsDone(Model model, @RequestParam("id") Long orderId) {
        orderService.markOrderAsDone(orderId);
        return "redirect:/order/list";
    }
}
