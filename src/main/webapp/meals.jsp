<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .exceeded {
            color: red;
        }
    </style>
    <script>
        function clearFilter() {
            //document.getElementById("filter").reset();

            document.getElementById("startDate").value = "";
            document.getElementById("startTime").value = "";
            document.getElementById("endDate").value = "";
            document.getElementById("endTime").value = "";

        }
        function checkForm() {
            var startDate = document.getElementById("startDate").value;
            var startTime = document.getElementById("startTime").value;
            var endDate = document.getElementById("endDate").value;
            var endTime = document.getElementById("endTime").value;

            if (startDate == "" && startTime == "" && endDate == "" && endTime == "") {
                return false;
            } else {
                return true;
            }
        }

    </script>
</head>
<body>
<section>
    <h2><a href="index.html">Home</a></h2>
    <h2>Meal list</h2>
    <form id="filter" method="post" action="meals">
        <input type="hidden" name="action" value="filter">
        <table border="1" cellpadding="8" cellspacing="0">
            <tr>
                <td>От даты</td>
                <td><input type="date" name="startDate" id="startDate" value="${startDate}"></td>
                <td>От времени</td>
                <td><input type="time" name="startTime" id="startTime" value="${startTime}"></td>
            </tr>
            <td>До даты</td>
            <td><input type="date" name="endDate" id="endDate" value="${endDate}"></td>
            <td>До времени</td>
            <td><input type="time" name="endTime" id="endTime" value="${endTime}"></td>
            <tr>
                <td colspan="4" align="right">
                    <input type="button" value="Cancel" onclick="clearFilter()"/>
                    <input type="submit" value="Filter" onclick="/*return checkForm()*/"/>
                </td>
            </tr>
        </table>
    </form>
    <a href="meals?action=create">Add Meal</a>
    <hr>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealWithExceed"/>
            <tr class="${meal.exceed ? 'exceeded' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>
