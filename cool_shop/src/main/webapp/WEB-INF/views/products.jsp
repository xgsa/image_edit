<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Login page</title>
</head>
<body>
    <h1>Products list:</h1>
    <table>
        <tr>
            <th>Name</th>
            <th>Price</th>
        </tr>
        <c:forEach var="upc" items="${upcs}">
            <tr>
                <td><c:out value="${upc.product.name}"/></td>
                <td><c:out value="${upc.price}"/></td>
            </tr>
        </c:forEach>
    </table>

</body>
</html>