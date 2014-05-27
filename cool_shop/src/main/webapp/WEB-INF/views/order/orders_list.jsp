<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<t:genericpage>
    <jsp:body>
        <h2>There are <c:out value="${fn:length(orders)} opened orders"/></h2>
        <table>
            <tr id="orderTableHeaderRow">
                <th class="orderTableItem"></th>
                <th class="orderTableItem">Order #</th>
                <th class="orderTableItem">Customer</th>
                <th class="orderTableItem">Created</th>
                <th class="orderTableItem">Submitted</th>
                <th class="orderTableItem">Order sum</th>
            </tr>
            <c:forEach var="order" items="${orders}">
                <tr>
                    <td class="orderTableItem">
                        <a href="${pageContext.request.contextPath}/order/done?id=${order.id}">
                            <img src="${pageContext.request.contextPath}/resources/images/order_done.png"
                                 alt="[mark order as done]" title="Mark as done"/>
                        </a>
                    </td>
                    <td class="orderTableItem">
                        #<c:out value="${order.id}"/>
                    </td>
                    <td class="orderTableItem">
                        <c:out value="${order.user.fullName}"/> (<c:out value="${order.user.login}"/>)
                    </td>
                    <td class="orderTableItem">
                        <c:out value="${order.createdAt}"/>
                    </td>
                    <td class="orderTableItem">
                        <c:out value="${order.submittedAt}"/>
                    </td>
                    <td class="orderTableItem">
                        <c:out value="${order.totalSum}"/>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </jsp:body>
</t:genericpage>
