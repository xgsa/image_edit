<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title>Login page</title>
    <link href="/resources/css/main.css" rel="stylesheet" type="text/css"/>
    <style>
        #outer {
            display: table;
            position: absolute;
            height: 100%;
            width: 100%;
        }

        #middle {
            display: table-cell;
            vertical-align: middle;
        }

        #inner {
            margin-left: auto;
            margin-right: auto;
            width: 300px;
            text-align: center;
        }

        .wide {
            width: 100%;
        }
        span.error {
            color: red;
            font-size: 8pt;
        }
    </style>
</head>
<body>

<div id="outer">
    <div id="middle">
        <div id="inner" class="panel">
            <form:form method="POST" modelAttribute="user">
                <div class="panelTitle">Sign in</div>
                <table class="wide">
                    <c:if test="${error != null}">
                        <tr>
                            <td colspan="2">
                                <span class="error">${error}</span>
                            </td>
                        </tr>
                    </c:if>
                    <tr>
                        <td><label for="user_login">Login:</label></td>
                        <td><form:input path="login" id="user_login" cssClass="wide"/></td>
                    </tr>
                    <tr>
                        <td><label for="user_password">Passord:</label></td>
                        <td><form:password path="password" id="user_password" cssClass="wide"/></td>
                    </tr>
                    <tr align="center">
                        <td colspan="2">
                            <input type="submit" value="Go-go-go!">
                        </td>
                    </tr>
                </table>
            </form:form>
        </div>
    </div>
</div>

</body>
</html>