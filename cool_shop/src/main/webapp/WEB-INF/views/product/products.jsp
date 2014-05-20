<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <title>Login page</title>
    <link href="/resources/css/main.css" rel="stylesheet" type="text/css"/>
</head>
<body>
    <c:choose>
        <c:when test="${user != null}">
            Hello, ${user.fullName}
        </c:when>
        <c:otherwise>
            You are not <a href="/user/login">logged in</a>.
        </c:otherwise>
    </c:choose>
    <HR>
    <c:forEach var="product" items="${products}">
        <div id="productPanel" class="panel">
            <a href="/product?id=${product.id}">
                <div class="panelTitle"><c:out value="${product.name}"/></div>
                <HR>
                <spring:url value="{imageServer}/{imageResource}" var="imageUrl">
                    <spring:param name="imageServer" value="${globalProperties.imageServerUrl}" />
                    <spring:param name="imageResource" value="${product.imageReference}" />
                </spring:url>
                <img src="${imageUrl}" id="productImage"/>
            </a>
        </div>
    </c:forEach>

</body>
</html>