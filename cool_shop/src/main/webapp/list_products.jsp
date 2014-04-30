<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<html>
<head>
    <title>Login page</title>
</head>
<body>
    <sql:setDataSource var="dbsrc" driver="com.mysql.jdbc.Driver"
                       url="jdbc:mysql://localhost/coolshop"
                       user="root" password="111"/>

    <sql:query dataSource="${dbsrc}" var="productsQuery">
        SELECT name, price
        FROM upc INNER JOIN product ON upc.product_id=product.id;
    </sql:query>

    <h1>Products list:</h1>
    <table>
        <tr>
            <th>Name</th>
            <th>Price</th>
        </tr>
        <c:forEach var="row" items="${productsQuery.rows}">
            <tr>
                <td><c:out value="${row.name}"/></td>
                <td><c:out value="${row.price}"/></td>
            </tr>
        </c:forEach>
    </table>

</body>
</html>