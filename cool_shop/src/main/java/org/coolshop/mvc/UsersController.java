package org.coolshop.mvc;

import org.coolshop.dao.UserDao;
import org.coolshop.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


@Controller
@RequestMapping({"/user"})
public class UsersController {

    @Autowired
    UserDao userDao;


    private static class UserLoginRequest {

        private String login;
        private String password;

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }


    @RequestMapping(value = "/login", method = GET)
    public String createUserForLogin(Model model) {
        model.addAttribute("user", new UserLoginRequest());
        return "user/login";
    }

    @RequestMapping(value = "/login", method = POST)
    public String processLogin(UserLoginRequest userLoginRequest, Model model, HttpSession session) {
        User user = userDao.getUser(userLoginRequest.getLogin());
        if (user == null || !user.checkPassword(userLoginRequest.getPassword())) {
            userLoginRequest.setPassword("");
            model.addAttribute("user", userLoginRequest);
            model.addAttribute("error", "Incorrect login or password");
            return "user/login";
        } else {
            session.setAttribute("user", user);
            return "redirect:/";
        }
    }
}
