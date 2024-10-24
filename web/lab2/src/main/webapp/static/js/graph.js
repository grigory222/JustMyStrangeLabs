
function drawGraph(canvas, R) {
    // const canvas = document.getElementById(canvasId);
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

        ctx.strokeStyle = "#0fa";
        ctx.stroke();
    }

    // Функция рисования области графика
    function drawShape() {
        ctx.fillStyle = "rgba(0, 255, 170, 0.5)"; // Цвет фигуры

        // Прямоугольник (левый верхний угол)
        ctx.beginPath();
        ctx.rect(centerX - R/2, centerY - R, R/2, R);
        ctx.fill();

        // Четверть круга (нижний левый угол)
        ctx.beginPath();
        ctx.moveTo(centerX - R / 2, centerY);
        ctx.arc(centerX, centerY, R / 2, Math.PI, Math.PI*0.5, true);
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

    // Рисуем график
    drawAxes();
    drawShape();
}


function handleCanvasClick(event, R) {
    const canvas = event.target;
    const rect = canvas.getBoundingClientRect();
    const x = event.clientX - rect.left;
    const y = event.clientY - rect.top;

    // Преобразуем координаты в систему координат графика
    const canvasCenterX = canvas.width / 2;
    const canvasCenterY = canvas.height / 2;

    const graphX = ((x - canvasCenterX) / (R / 2)).toFixed(2);
    const graphY = ((canvasCenterY - y) / (R / 2)).toFixed(2);

    return { graphX, graphY };
}

// Функция для рисования точки
function drawPoint(canvasId, x, y, color) {
    const canvas = document.getElementById(canvasId);
    const ctx = canvas.getContext("2d");
    ctx.beginPath();
    ctx.arc(x, y, 5, 0, 2 * Math.PI);
    ctx.fillStyle = color;
    ctx.fill();
}