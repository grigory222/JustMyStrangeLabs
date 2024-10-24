<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>My test</title>
    <link rel="stylesheet" href="static/styles/main.css">
</head>

<body>
<nav class="navbar">
    <label>ЛР №2</label>
    <a href="#">
        <img src="static/img/yoda.webp" class="logo-img">
    </a>
    <div class="my-name">
        <label>Воронов Григорий</label>
        <label>P3216</label>
    </div>
</nav>
<main class="container">
    <div class="side-image left"></div>
    <div class="content">
        <div class="inner-container">
            <div class="graph-container">
                <canvas id="myCanvas" width="300" height="300"></canvas>
            </div>
            <form action="${pageContext.request.contextPath}/controller" method="GET" id="data-form" class="form">
                <fieldset id="xs">
                    <legend>Выберите X:</legend>
                    <label><input type="radio" name="x" value="-5">-5</label>
                    <label><input type="radio" name="x" value="-4">-4</label>
                    <label><input type="radio" name="x" value="-3">-3</label>
                    <label><input type="radio" name="x" value="-2">-2</label>
                    <label><input type="radio" name="x" value="-1">-1</label>
                    <label><input type="radio" name="x" value="0">&nbsp;0</label>
                    <label><input type="radio" name="x" value="1">&nbsp;1</label>
                    <label><input type="radio" name="x" value="2">&nbsp;2</label>
                    <label><input type="radio" name="x" value="3">&nbsp;3</label>
                </fieldset>

                <label for="y" class="label-for-y">Введите Y: <input id="y" name="y" required></label>

                <fieldset id="rs">
                    <legend>Выберите R:</legend>
                    <label><input type="checkbox" name="r" value="1">&nbsp;1</label>
                    <label><input type="checkbox" name="r" value="2">&nbsp;2</label>
                    <label><input type="checkbox" name="r" value="3">&nbsp;3</label>
                    <label><input type="checkbox" name="r" value="4">&nbsp;4</label>
                    <label><input type="checkbox" name="r" value="5">&nbsp;5</label>
                </fieldset>
                <button class="btn" type="submit">Проверить</button>
            </form>
            <div id="error" hidden>
            </div>
        </div>
        <table id="result-table">
            <tr>
                <th>x</th>
                <th>y</th>
                <th>r</th>
                <th>execution time</th>
                <th>time</th>
                <th>result</th>
            </tr>
            <c:forEach var="result" items="${sessionScope.results.results}">
                <tr>
                    <td>${result.getX()}</td>
                    <td>${result.getY()}</td>
                    <td>${result.getR()}</td>
                    <td>${result.getExecutionTime()}</td>
                    <td>${result.getTime()}</td>>
                    <td><c:choose>
                        <c:when test="${result.isInside()}">
                            <span style="color:green;">Попадание</span>
                        </c:when>
                        <c:otherwise>
                            <span style="color:red;">Промах</span>
                        </c:otherwise>
                    </c:choose></td>
                </tr>>
            </c:forEach>
        </table>
    </div>
    <div class="side-image right"></div>

</main>
<footer id="copyright">
    <label></label>
</footer>
<script src="static/js/index.js"></script>
<script src="static/js/graph.js"></script>
</body>
</html>