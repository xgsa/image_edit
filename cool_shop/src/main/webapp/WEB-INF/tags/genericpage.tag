<%@tag description="Generic Page template" pageEncoding="UTF-8" %>
<%@attribute name="footer" fragment="true" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <title>Login page</title>
    <link href="${pageContext.request.contextPath}/resources/css/main.css" rel="stylesheet" type="text/css"/>
</head>
<body>
    <div id="pageHeader">

        <a href="${pageContext.request.contextPath}/">
            <img src="${pageContext.request.contextPath}/resources/images/coolshop.png" id="logoImage"/>
        </a>
        <span id="shopTitle">We do things right</span>

        <div id="loginPanel" class="panel">
            <sec:authorize var="authenticated" access="isAuthenticated()" />
            <c:choose>
                <c:when test="${authenticated}">
                    Hi, <sec:authentication property="principal.modelUser.fullName"/><BR>
                    <a href="${pageContext.request.contextPath}/user/logout">Logout</a><BR>
                    <sec:authorize access="hasRole('User')">
                        <a href="${pageContext.request.contextPath}/basket/list">Look into the backet</a>
                    </sec:authorize>
                    <sec:authorize access="hasRole('Manager')">
                        <a href="${pageContext.request.contextPath}/order/list">Look open orders</a>
                    </sec:authorize>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/user/login">Log in</a>
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