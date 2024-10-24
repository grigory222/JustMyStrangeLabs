<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Result</title>
    <link rel="stylesheet" href="styles/main.css">
</head>

<body>
<nav class="navbar">
    <label>ЛР №2</label>
    <a href="#">
        <img src="img/yoda.webp" class="logo-img" alt="logo">
    </a>
    <div class="my-name">
        <label>Воронов Григорий</label>
        <label>P3216</label>
    </div>
</nav>


<main class="container">
    <div class="side-image left"></div>
    <div class="content">
        <a href="${pageContext.request.contextPath}/controller" class="return-link">Вернуться к заполнению</a>

        <table id="result-table">
            <tr>
                <th>x</th>
                <th>y</th>
                <th>r</th>
                <th>execution time</th>
                <th>time</th>
                <th>result</th>
            </tr>
            <tr>
                <td>${requestScope.x}</td>
                <td>${requestScope.y}</td>
                <td>${requestScope.r}</td>
                <td>${requestScope.executionTime}</td>
                <td>${requestScope.time}</td>
                <td>
                    <c:choose>
                        <c:when test="${requestScope.isInside}">
                            <span style="color:green;">Попадание</span>
                        </c:when>
                        <c:otherwise>
                            <span style="color:red;">Промах</span>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </table>
    </div>
    <div class="side-image right"></div>
</main>

<footer id="copyright">
    <label></label>
</footer>

<script src="index.js"></script>
</body>
</html>
