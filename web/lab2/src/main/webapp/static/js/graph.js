function drawGraph(canvas, R) {
    // тут R в пикселах

    const ctx = canvas.getContext("2d");
    // Центр системы координат
    const centerX = canvas.width / 2;
    const centerY = canvas.height / 2;

    // Функция рисования системы координат
    function drawAxes() {
        ctx.beginPath();
        ctx.moveTo(0, centerY);
        ctx.lineTo(canvas.width, centerY); // Ось X
        ctx.moveTo(centerX, 0);
        ctx.lineTo(centerX, canvas.height); // Ось Y
        ctx.moveTo(centerX - 5, centerY - canvas.height / 2 + 10);
        ctx.lineTo(centerX, centerY - canvas.height / 2);
        ctx.lineTo(centerX + 5, centerY - canvas.height / 2 + 10);

        ctx.moveTo(centerX + canvas.width / 2 - 10, centerY - 5);
        ctx.lineTo(centerX + canvas.width / 2, centerY);
        ctx.lineTo(centerX + canvas.width / 2 - 10, centerY + 5);

        // засечки на оси X
        ctx.moveTo(centerX + R, centerY - 5)
        ctx.lineTo(centerX + R, centerY + 5)
        ctx.moveTo(centerX + R / 2, centerY - 5)
        ctx.lineTo(centerX + R / 2, centerY + 5)
        ctx.moveTo(centerX - R, centerY - 5)
        ctx.lineTo(centerX - R, centerY + 5)
        ctx.moveTo(centerX - R / 2, centerY - 5)
        ctx.lineTo(centerX - R / 2, centerY + 5)

        // засечки на оси Y
        ctx.moveTo(centerX - 5, centerY - R)
        ctx.lineTo(centerX + 5, centerY - R)
        ctx.moveTo(centerX - 5, centerY - R / 2)
        ctx.lineTo(centerX + 5, centerY - R / 2)
        ctx.moveTo(centerX - 5, centerY + R)
        ctx.lineTo(centerX + 5, centerY + R)
        ctx.moveTo(centerX - 5, centerY + R / 2)
        ctx.lineTo(centerX + 5, centerY + R / 2)

        ctx.strokeStyle = "#0fa";
        ctx.stroke();
    }

    // Функция рисования области графика
    function drawShape() {
        ctx.fillStyle = "rgba(0, 255, 170, 0.5)"; // Цвет фигуры

        // Прямоугольник (левый верхний угол)
        ctx.beginPath();
        ctx.rect(centerX - R / 2, centerY - R, R / 2, R);
        ctx.fill();

        // Четверть круга (нижний левый угол)
        ctx.beginPath();
        ctx.moveTo(centerX - R / 2, centerY);
        ctx.arc(centerX, centerY, R / 2, Math.PI, Math.PI * 0.5, true);
        ctx.lineTo(centerX, centerY);
        ctx.lineTo(centerX - R / 2, centerY);
        ctx.closePath();
        ctx.fill();

        // Треугольник (верхний правый угол)
        ctx.beginPath();
        ctx.moveTo(centerX, centerY);
        ctx.lineTo(centerX + R, centerY);
        ctx.lineTo(centerX, centerY - R);
        ctx.closePath();
        ctx.fill();
    }

    function drawPoints(){
        const table = document.getElementById("result-table");
        const rows = table.getElementsByTagName("tr");

        // Проходим по всем строкам, пропуская заголовок
        for (let i = 1; i < rows.length; i++) {
            const cells = rows[i].getElementsByTagName("td");
            const x = parseFloat(cells[0].innerText); // Получаем значение x
            const y = parseFloat(cells[1].innerText); // Получаем значение y
            const r = parseFloat(cells[2].innerText); // Получаем значение r, если нужно
            const color = cells[5].innerText == "Попадание" ? "green" : "red";
            console.log(x, y, r, color);
            drawPoint(x, y, r, color);
        }
    }

    // Функция для рисования точки
    function drawPoint(mathX, mathY, r, color) {
        const rect = canvas.getBoundingClientRect();
        const {x, y} = mathToCanvas(mathX, mathY, canvas, r, R)

        const ctx = canvas.getContext("2d");
        ctx.beginPath();
        ctx.arc(x, y, 5, 0, 2 * Math.PI);
        ctx.fillStyle = color;
        ctx.fill();
    }

    function drawLetters(){
        const ctx = canvas.getContext("2d");
        ctx.font = "18px Arial";
        ctx.fillStyle = "#0fa";
        ctx.fillText("R/2", centerX + R/2 - 10, centerY + 20);
        ctx.fillText("R", centerX + R - 5, centerY + 20);
        ctx.fillText("-R/2", centerX - R/2 - 10, centerY + 20);
        ctx.fillText("-R", centerX - R - 5, centerY + 20);

        ctx.fillText("R/2", centerX + 10, centerY - R / 2);
        ctx.fillText("R", centerX + 10, centerY - R);
        ctx.fillText("-R/2", centerX + 10, centerY + R / 2 );
        ctx.fillText("-R", centerX + 10, centerY + R);
    }

    // Рисуем график
    drawAxes();
    drawShape();
    drawLetters();
    drawPoints();
}

function canvasToMath(clientX, clientY, canvas, R, pixelsR) {
    const rect = canvas.getBoundingClientRect();
    // координаты от левого верхнего угла канваса
    const x_ = clientX - rect.x;
    const y_ = clientY - rect.y;
    // теперь переведем их в математические
    const centerX = canvas.width / 2;
    const centerY = canvas.height / 2;
    const x = ((x_ - centerX) / pixelsR) * R;
    const y = ((centerY - y_) / pixelsR) * R;
    return {x, y};
}

function mathToCanvas(x, y, canvas, R, pixelsR) {
    const centerX = canvas.width / 2;
    const centerY = canvas.height / 2;
    const x_ = centerX + (x) * pixelsR / R;
    const y_ = centerY - (y) * pixelsR / R;
    return { x: x_, y: y_ };
}

async function handleClick(event, canvas, R, pixelsR) {
    // получить коорд. клика
    // перевести в математические коорд.
    // отправить на серв.
    // если ок, то рисуем drawPoint(canvas, event.clientX, event.clientY, "red");
    // и сохраняем в таблице (сохранится благодаря, серверу)
    const {x, y} = canvasToMath(event.clientX, event.clientY, canvas, R, pixelsR);

    try {
        const localNow = new Date().toString();
        fetch(`http://localhost:8080/lab2/controller?x=${x}&y=${y}&r=${state.r}`, {
            method: "GET"
        }).then(async response => {
            if (!response.ok) {
                throw new Error("Error: " + response.status);
            } else {
                // window.location.href = response.url;
                document.documentElement.innerHTML = await response.text();
            }
        });


    } catch (error) {
        // Обработка ошибок выполнения запроса
        console.error("Ошибка при выполнении запроса:", error);
    }

}