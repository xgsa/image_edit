<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Login page</title>
    <link href="/resources/css/main.css" rel="stylesheet" type="text/css"/>
</head>
<body>

<div id="loginOuter">
    <div id="loginMiddle">
        <div id="loginInner" class="panel">
            <form action="/user/process_login" method="POST">
                <div class="panelTitle">Sign in</div>
                <table class="wide">
                    <c:if test="${param.error != null}">
                        <tr>
                            <td colspan="2">
                                <span class="error">${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}</span>
                            </td>
                        </tr>
                    </c:if>
                    <tr>
                        <td><label for="user_login">Login:</label></td>
                        <td><input type="input" name="user_login" id="user_login" class="wide"/></td>
                    </tr>
                    <tr>
                        <td><label for="user_password">Passord:</label></td>
                        <td><input type="password" name="user_password" id="user_password" class="wide"/></td>
                    </tr>
                    <tr align="center">
                        <td colspan="2">
                            <input type="submit" value="Go-go-go!">
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>

</body>
</html>