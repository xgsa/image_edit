<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<t:genericpage>
    <jsp:body>
        <sec:authorize var="authenticated" access="isAuthenticated()" />
        <c:choose>
            <c:when test="${authenticated}">
                <h2>There are <c:out value="${fn:length(upcs)} items in your basket"/></h2>
                <table>
                    <c:forEach var="upc" items="${upcs}">
                        <tr>
                            <td>
                                <a href="${pageContext.request.contextPath}/basket/remove?id=${upc.id}">
                                    <img src="${pageContext.request.contextPath}/resources/images/remove_from_basket.png"
                                         alt="[remove from basket]" title="Remove from Basket"/>
                                </a>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/upc?id=${upc.id}">
                                    <c:out value="${upc.product.name}"/>
                                </a>
                            </td>
                            <td>
                                <c:out value="${upc.price}"/>
                            </td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td colspan="3">
                            <a href="${pageContext.request.contextPath}/basket/submit">
                                <img src="${pageContext.request.contextPath}/resources/images/purchase.png"
                                     alt="[Purchase NOW!]" title="Purchase NOW!"/>Purchase NOW!</a>
                        </td>
                    </tr>
                </table>
            </c:when>
            <c:otherwise>
                You need to <a href="${pageContext.request.contextPath}/user/login">log in</a>
                to be able to perform actions with basket
            </c:otherwise>
        </c:choose>
    </jsp:body>
</t:genericpage>
