<%@ page contentType="text/html; charset=utf-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<t:genericpage>
    <jsp:body>
        <%-- It is possible to specify either product or upc to show --%>
        <c:if test="${product == null and upc != null}" >
            <c:set var="product" value="${upc.product}" />
        </c:if>
        <%-- It it is only one upc for the product -- show its properties among the product ones --%>
        <c:if test="${upc == null and product != null and fn:length(product.upcs) == 1}" >
            <c:forEach var="u" items="${product.upcs}">
                <c:set var="upc" value="${u}" />
            </c:forEach>
        </c:if>

        <div id="productInfoTitle"><c:out value="${product.name}"/></div>
        <table>
            <tr valign="top">
                <td>
                    <spring:url value="{imageServer}/{imageResource}" var="imageUrl">
                        <spring:param name="imageServer" value="${globalProperties.imageServerUrl}" />
                        <spring:param name="imageResource" value="${product.imageReference}" />
                    </spring:url>
                    <img src="${imageUrl}" id="productInfoImage"/>
                </td>
                <td width="10px"></td>
                <td>
                    <div class="panelTitle">Product details:</div>
                    <table>
                        <c:set var="attribute_values" value="${product.attributes}" />
                        <%@ include file="attributes_list.jspf" %>
                        <c:if test="${upc != null}" >
                            <c:set var="attribute_values" value="${upc.attributes}" />
                            <%@ include file="attributes_list.jspf" %>
                        </c:if>
                    </table>
                    <c:if test="${upc != null}" >
                        <div class="panel">Price: <c:out value="${upc.price}"/></div>
                        <div style="float: right;">
                            <a href="${pageContext.request.contextPath}/basket/add?id=${upc.id}">
                                <img src="${pageContext.request.contextPath}/resources/images/add_to_basket.png"
                                     alt="[add to basket]" title="Add to Basket"/>
                            </a>
                        </div>
                    </c:if>
                </td>
            </tr>
        </table>

        <c:if test="${upc == null}" >
                <div id="productAllUpcsPanel">
                    <div class="panelTitle">Available modifications:</div>
                    <c:forEach var="upc" items="${product.upcs}">
                        <div class="panel" id="productUpcPanel">
                            <div class="panelTitle">Modifications details:</div>
                            <table>
                                <c:set var="attribute_values" value="${upc.attributes}" />
                                <%@ include file="attributes_list.jspf" %>
                            </table>
                            <HR>
                            Price: <c:out value="${upc.price}"/>
                            <div style="float: right;">
                                <a href="${pageContext.request.contextPath}/basket/add?id=${upc.id}">
                                    <img src="${pageContext.request.contextPath}/resources/images/add_to_basket.png"
                                         alt="[add to basket]" title="Add to Basket"/>
                                </a>
                            </div>
                        </div>
                    </c:forEach>
                </div>
        </c:if>
    </jsp:body>
</t:genericpage>