<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
        <div id="productPanel">
            <div id="productTitle"><c:out value="${product.name}"/></div>
            <HR>
            <%--<c:forEach var="attribute_value" items="${upc.attributes}">--%>
                <%--<c:out value="${attribute_value.attribute.name}"/>=--%>
                <%--<c:out value="${attribute_value.value}"/><BR>--%>
            <%--</c:forEach>--%>
            <img src="/resources/images/product.png" id="productImage"/>
        </div>
    </c:forEach>

</body>
</html>