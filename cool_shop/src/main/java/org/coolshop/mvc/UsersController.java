package org.coolshop.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(value = "/user")
public class UsersController {

    @RequestMapping(value = "/login")
    public String createUserForLogin() {
        return "user/login";
    }
}
