<%--
  Created by IntelliJ IDEA.
  User: Vladimir
  Date: 28.03.2017
  Time: 20:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit meal</title>
</head>
<body>

    <jsp:useBean id="meal" class="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <form action="meals" method="post">
        <input type="hidden" name="id" value="${meal.id}">
        <input type="datetime-local" name="dateTime" value="${meal.dateTime}">
        <input type="text" name="description" value="${meal.description}">
        <input type="text" name="calories" value="${meal.calories}">
        <input type="submit" value="Save">
    </form>
</body>
</html>
