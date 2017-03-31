<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %><%--
  Created by IntelliJ IDEA.
  User: Vladimir
  Date: 26.03.2017
  Time: 16:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="f" uri="http://topjava10.com/formatter" %>

<html>
<head>
    <title>Meal list</title>
    <style>
        .normal{
            color: green;
        }
        .exceed{
            color: red;
        }
    </style>
</head>
<body>
<h2><a href="index.html">Home</a></h2>
<h2>Meal list</h2>

<table border="1px">
    <tr style="font-weight: bold">
        <td>DateTime</td>
        <td>Description</td>
        <td>Calories</td>
        <td>Exceed</td>
        <td colspan="2">Action</td>
    </tr>
    <c:forEach var="meal" items="${meals}">
        <jsp:useBean id="meal" class="ru.javawebinar.topjava.model.MealWithExceed" scope="page"/>

        <%--<c:choose>--%>
            <%--<c:when test="${meal.exceed == true}">--%>
                <%--<tr style="color: red">--%>
            <%--</c:when>--%>
            <%--<c:otherwise>--%>
                <%--<tr>--%>
            <%--</c:otherwise>--%>
        <%--</c:choose>--%>

        <tr class="${meal.exceed ? "exceed" : "normal"}">
            <td>
            <!--1st approach-->
            <%--<c:set var="cleanedDateTime" value="${fn:replace(meal.dateTime, 'T', ' ')}"/>--%>
            <%--<fmt:parseDate value="${cleanedDateTime}" pattern="yyyy-MM-dd HH:mm" var="parsedDateTime" type="both"/>--%>
            <%--<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${parsedDateTime}" var="formatedDate"/>--%>
            <%--${formatedDate}--%>

            <!--2nd approach-->
            <%--<fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>--%>
            <%--<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${parsedDateTime}" var="formatedDate"/>--%>
            <%--${formatedDate}--%>

            <!--3rd approach (without var)-->
            <%--<fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>--%>
            <%--<fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${parsedDateTime}" />--%>

            <!--4th approach-->
            ${f:format(meal.dateTime, "dd-MM-yyyy HH:mm")}

            <!--5th approach-->
            <%--<%= TimeUtil.formatLocalDateTime(meal.getDateTime(),"dd-MM-yyyy HH:mm")%>--%>
            </td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td>${meal.exceed}</td>
            <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
            <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
        </tr>
    </c:forEach>
</table>
<a href="meals?action=insert">Add meal</a>
</body>
</html>

