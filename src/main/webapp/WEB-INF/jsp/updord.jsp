<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Update/delete an order</title>

    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/resources/css/common.css" rel="stylesheet">
    <link href="${contextPath}/resources/css/style.css" rel="stylesheet">
    <link href="${contextPath}/resources/css/calendar-win2k-cold-1.css" type="text/css" media="all" title="win2k-cold-1" rel="stylesheet" />
    <script src="${contextPath}/resources/js/jquery.min.js"></script>
    <script src="${contextPath}/resources/js/bootstrap.min.js"></script>
    <script src="${contextPath}/resources/js/calendar/calendar.js" type="text/javascript"></script>
    <script src="${contextPath}/resources/js/calendar/calendar-en.js" type="text/javascript"></script>
    <script src="${contextPath}/resources/js/calendar/calendar-ru.js" type="text/javascript"></script>
    <script src="${contextPath}/resources/js/calendar/calendar-setup.js" type="text/javascript"></script>
</head>

<body>

<div class="container">

    <form:form method="POST" modelAttribute="order" class="form-signin" action="${contextPath}/updord?id=${order.id}">
        <h2 class="form-signin-heading">Создать ордер:</h2>
        <spring:bind path="orderNumber">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:input type="text" path="orderNumber" class="form-control" placeholder="№ заказа" autofocus="true"/>
                <form:errors path="orderNumber"/>
            </div>
        </spring:bind>

        <spring:bind path="plannedDate">
            <div class="form-group ${status.error ? 'has-error' : ''} img_date">
                <form:input type="text" name="date" id="f_date_1" path="plannedDate" class="form-control" placeholder="Плановая дата исполнения"/>
                <img src="${contextPath}/resources/img/calendar.gif" id="trigger_1" class="img_date" title="Date selector"
                     onmouseover="this.style.background='red';" onmouseout="this.style.background=''" />
                <form:errors path="plannedDate"/>
            </div>
        </spring:bind>

        <spring:bind path="actualDate">
            <div class="form-group ${status.error ? 'has-error' : ''} img_date">
                <form:input type="text" name="date" id="f_date_2" path="actualDate" class="form-control" placeholder="Фактическая дата исполнения"/>
                <img src="${contextPath}/resources/img/calendar.gif" id="trigger_2" class="img_date" title="Date selector"
                     onmouseover="this.style.background='red';" onmouseout="this.style.background=''" />
                <form:errors path="actualDate"/>
            </div>
        </spring:bind>

        <spring:bind path="cipher">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:input type="text" path="cipher" class="form-control" placeholder="Шифр"/>
                <form:errors path="cipher"/>
            </div>
        </spring:bind>

        <spring:bind path="productName">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:input type="text" path="productName" class="form-control" placeholder="Изделие"/>
                <form:errors path="productName"/>
            </div>
        </spring:bind>

        <button name="update" value="update" class="btn btn-lg btn-primary btn-block" type="submit">Обновить</button>
        <button name="delete" value="delete" class="btn btn-lg btn-primary btn-block" type="submit">Удалить</button>
    </form:form>
    <script type="text/javascript">
        Calendar.setup({
            inputField     :    "f_date_1",
            ifFormat       :    "%d.%m.%Y",
            button         :    "trigger_1",
            singleClick    :    true
        });
    </script>
    <script type="text/javascript">
        Calendar.setup({
            inputField     :    "f_date_2",
            ifFormat       :    "%d.%m.%Y",
            button         :    "trigger_2",
            singleClick    :    true
        });
    </script>
</div>
</body>
</html>
