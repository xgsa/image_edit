<%@ page contentType="text/html; charset=utf-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<t:genericpage>
    <jsp:body>
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
                <td>
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
    </jsp:body>
</t:genericpage>