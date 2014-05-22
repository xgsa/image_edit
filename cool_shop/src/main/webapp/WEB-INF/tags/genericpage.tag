<%@tag description="Generic Page template" pageEncoding="UTF-8" %>
<%@attribute name="footer" fragment="true" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Login page</title>
    <link href="/resources/css/main.css" rel="stylesheet" type="text/css"/>
</head>
<body>
    <div id="pageHeader">

        <a href="/"><img src="/resources/images/coolshop.png" id="logoImage"/></a>
        <span id="shopTitle">We do things right</span>

        <div id="loginPanel" class="panel">
            <c:choose>
                <c:when test="${user != null}">
                    Hi, ${user.fullName}<BR>
                    <a href="/user/logout">Logout</a><BR>
                    <c:choose>
                        <c:when test="${user.role == 'Manager'}">
                            <a href="/order/list">Look open orders</a>
                        </c:when>
                        <c:otherwise>
                            <a href="/basket/list">Look into the backet</a>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <a href="/user/login">Log in</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <HR id="headerDelimiter">
    <div id="body">
        <jsp:doBody/>
    </div>

</body>
</html>