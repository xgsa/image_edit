package org.imgedit.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class LoginServlet extends HttpServlet {

    private static final String PARAMETER_LOGIN = "login";
    private static final String PARAMETER_PASSWORD = "password";

    private static final String LOGIN_OK = "Login successfully";
    private static final String LOGIN_FAILED = "Login failed, <a href=\"/\">retry</a>";
    private static final String LOGIN_PAGE_TEMPLATE =
            "<html>\n" +
            "<body>\n" +
            "<h2>%s</h2>\n" +
            "</body>\n" +
            "</html>";

    private Map<String, String> LOGINS_DATA = new HashMap<String, String>() {
        {
            put("login1", "111");
            put("login2", "222");
        }
    };


    private boolean acceptLoginPair(String login, String password) {
        return password.equals(LOGINS_DATA.get(login));
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String login = request.getParameter(PARAMETER_LOGIN);
        String password = request.getParameter(PARAMETER_PASSWORD);
        if (login != null && password != null) {
            String loginMsg = acceptLoginPair(login, password) ? LOGIN_OK : LOGIN_FAILED;
            response.getWriter().write(String.format(LOGIN_PAGE_TEMPLATE, loginMsg));
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("\"login\" or \"password\" POST-parameter is absent.");
        }
    }

}
