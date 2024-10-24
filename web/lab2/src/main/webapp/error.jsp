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
        <h1>${requestScope.errorMessage}</h1>
    </div>
    <div class="side-image right"></div>
</main>

<footer id="copyright">
    <label></label>
</footer>

<script src="index.js"></script>
</body>
</html>
