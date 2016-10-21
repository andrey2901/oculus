<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/resources/css/common.css" rel="stylesheet">
    <link href="${contextPath}/resources/css/style.css" rel="stylesheet">
    <title>Oculus</title>
</head>
<body>
<div class="container">
    <h2 style="text-align: center">Oculus</h2>
    <table class="table">
        <tr>
            <c:forEach items="${tableHeaders}" var="tableHeader">
                <th>${tableHeader.name}</th>
            </c:forEach>
        </tr>
        <c:forEach items="${con_ord_map}" var="entry">
            <c:set value="${entry.key}" var="constructor"/>
            <c:set var="orders" value="${entry.value}"/>
            <c:set var="counter" value="${orders.size()}"/>
            <c:choose>
                <c:when test="${counter == 0}">
                    <tr class="constructor empty">
                        <td rowspan="10"><a href="${contextPath}/archive?id=${constructor.id}">${constructor.name}</a>
                        </td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                    </tr>
                    <c:forEach begin="${counter + 1}" end="9">
                        <tr class="empty">
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:when test="${counter == 1}">
                    <tr class="constructor">
                        <td rowspan="10"><a href="${contextPath}/archive?id=${constructor.id}">${constructor.name}</a>
                        </td>
                        <td>${orders[0].orderNumber}</td>
                        <td>${orders[0].getFormattedPlannedDate()}</td>
                        <td>${orders[0].getFormattedActualDate()}</td>
                        <td>${orders[0].cipher}</td>
                        <td>${orders[0].productName}</td>
                    </tr>
                    <c:forEach begin="${counter}" end="9">
                        <tr class="empty">
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr class="constructor">
                        <td rowspan="10"><a href="${contextPath}/archive?id=${constructor.id}">${constructor.name}</a>
                        </td>
                        <td>${orders[0].orderNumber}</td>
                        <td>${orders[0].getFormattedPlannedDate()}</td>
                        <td>${orders[0].getFormattedActualDate()}</td>
                        <td>${orders[0].cipher}</td>
                        <td>${orders[0].productName}</td>
                    </tr>
                    <c:forEach begin="1" end="${counter - 1}" var="i">
                        <tr>
                            <td>${orders[i].orderNumber}</td>
                            <td>${orders[i].getFormattedPlannedDate()}</td>
                            <td>${orders[i].getFormattedActualDate()}</td>
                            <td>${orders[i].cipher}</td>
                            <td>${orders[i].productName}</td>
                        </tr>
                    </c:forEach>
                    <c:forEach begin="${counter}" end="9">
                        <tr class="empty">
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </table>
    <div class="fixed">
        <div class="btn btn-lg btn-primary btn-block own"><a href="${contextPath}/all_archive">Архив</a></div>
        <c:if test="${pageContext.request.userPrincipal.name != null}">
            <form id="logoutForm" method="POST" action="${contextPath}/logout">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
            <div class="btn btn-lg btn-primary btn-block own"><a onclick="document.forms['logoutForm'].submit()">Выйти</a>
            </div>
        </c:if>
        <a href="${contextPath}/console">Консоль</a>
    </div>
</div>
</body>
</html>
