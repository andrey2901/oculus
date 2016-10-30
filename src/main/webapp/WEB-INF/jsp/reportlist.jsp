<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

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
    <h2 style="text-align: center">Ранее созданные архивы</h2>
    <c:forEach items="${names}" var="name">
        <c:set var="start" value="${fn:substring(name, 7, 17)}"/>
        <c:set var="end" value="${fn:substring(name, 18, 28)}" />
        <h4><a href="${contextPath}/reports?name=${name}">Отчет с ${start} по ${end}</a></h4>
    </c:forEach>
    <div class="fixed">
        <div class="btn btn-lg btn-primary btn-block own"><a href="${contextPath}/">Главная</a></div>
        <c:if test="${pageContext.request.userPrincipal.name != null}">
            <div class="btn btn-lg btn-primary btn-block own">
                <a onclick="document.forms['logoutForm'].submit()">Выйти</a>
            </div>
            <form id="logoutForm" method="POST" action="${contextPath}/logout">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
        </c:if>
        <a href="${contextPath}/console">Консоль</a>
    </div>
</div>
</body>
</html>