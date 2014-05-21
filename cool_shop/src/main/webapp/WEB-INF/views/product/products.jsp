<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<t:genericpage>
    <jsp:body>
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
    </jsp:body>
</t:genericpage>
