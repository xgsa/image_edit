<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <title>Login page</title>
    <link href="/resources/css/main.css" rel="stylesheet" type="text/css"/>
</head>
<body>
    <!-- TODO: Generalize & not duplicate with products.jsp -->
    <c:choose>
        <c:when test="${user != null}">
            Hello, ${user.fullName}
        </c:when>
        <c:otherwise>
            You are not <a href="/user/login">logged in</a>.
        </c:otherwise>
    </c:choose>
    <HR>

    <div id="productInfoTitle"><c:out value="${product.name}"/></div>
    <table>
        <tr valign="top">
            <td>
                <spring:url value="{imageServer}/{imageResource}" var="imageUrl">
                    <spring:param name="imageServer" value="${globalProperties.imageServerUrl}" />
                    <spring:param name="imageResource" value="${product.imageReference}" />
                </spring:url>
                <image src="${imageUrl}" id="productInfoImage"/>
            </td>
            <td width="10px"></td>
            <td class="panel">
                <div class="panelTitle">Product details:</div>
                <table>
                    <c:set var="attribute_values" value="${product.attributes}" />
                    <%@ include file="attributes_list.jspf" %>
                    <c:if test="${fn:length(upcs) == 1}" >
                        <c:set var="attribute_values" value="${upcs[0].attributes}" />
                        <%@ include file="attributes_list.jspf" %>
                    </c:if>
                </table>
            </td>
        </tr>
    </table>

    <c:if test="${fn:length(upcs) > 1}" >
        <div id="productAllUpcsPanel">
            <div class="panelTitle">Available modifications:</div>
            <c:forEach var="upc" items="${upcs}">
                <div class="panel" id="productUpcPanel">
                    <div class="panelTitle">Modifications details:</div>
                    <table>
                        <c:set var="attribute_values" value="${upc.attributes}" />
                        <%@ include file="attributes_list.jspf" %>
                    </table>
                </div>
            </c:forEach>
        </div>
    </c:if>
</body>
</html>