<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<t:genericpage>
    <jsp:body>
        <c:choose>
            <c:when test="${user != null}">
                <h2>There are <c:out value="${fn:length(upcs)} items in your basket"/></h2>
                <table>
                    <c:forEach var="upc" items="${upcs}">
                        <tr>
                            <td>
                                <a href="/basket/remove?id=${upc.id}">
                                    <img src="/resources/images/remove_from_basket.png" alt="[remove from basket]"
                                         title="Remove from Basket"/>
                                </a>
                            </td>
                            <td>
                                <a href="/upc?id=${upc.id}"><c:out value="${upc.product.name}"/></a>
                            </td>
                            <td>
                                <c:out value="${upc.price}"/>
                            </td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td colspan="3">
                            <a href="/basket/submit">
                                <img src="/resources/images/purchase.png" alt="[Purchase NOW!]"
                                     title="Purchase NOW!"/>Purchase NOW!</a>
                        </td>
                    </tr>
                </table>
            </c:when>
            <c:otherwise>
                You need to <a href="/user/login">log in</a> to be able to perform actions with basket
            </c:otherwise>
        </c:choose>
    </jsp:body>
</t:genericpage>
