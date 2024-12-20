const graphScale = 20;

export function drawGraph(canvas, r, rows) {
    let R = graphScale * r// тут R в пикселах

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

        ctx.strokeStyle = "#00afff"; // Голубой цвет
        ctx.stroke();
    }

    // Функция рисования области графика
    function drawShape() {
        ctx.fillStyle = "rgba(0, 170, 255, 0.5)"; // Голубой цвет фигуры

        // Прямоугольник (правый нижний угол)
        ctx.beginPath();
        ctx.rect(centerX, centerY, R / 2, R);
        ctx.fill();

        // Четверть круга (верхний левый угол)
        ctx.beginPath();
        ctx.moveTo(centerX - R, centerY);
        ctx.arc(centerX, centerY, R, Math.PI, 1.5*Math.PI , false);
        ctx.lineTo(centerX, centerY);
        ctx.lineTo(centerX - R, centerY);
        ctx.closePath();
        ctx.fill();

        // Треугольник (левый нижний угол)
        ctx.beginPath();
        ctx.moveTo(centerX, centerY);
        ctx.lineTo(centerX - R/2, centerY);
        ctx.lineTo(centerX, centerY + R);
        ctx.closePath();
        ctx.fill();
    }

    function drawPoints(){
        for (let i = 1; i < rows.length; i++) {
            const x = rows[i].x;
            const y = rows[i].y;
            const res = rows[i].result;
            const color = res === true ? "blue" : "red";
            //console.log(x, y, res, color);
            drawPoint(x, y, color);
        }
    }
    // Функция для рисования точки
    function drawPoint(mathX, mathY, color) {
        // const rect = canvas.getBoundingClientRect(); зачем это я писал?
        const {x, y} = mathToCanvas(mathX, mathY, canvas)

        const ctx = canvas.getContext("2d");
        ctx.beginPath();
        ctx.arc(x, y, 5, 0, 2 * Math.PI);
        ctx.fillStyle = color;
        ctx.fill();
    }

    function mathToCanvas(x, y, canvas) {
        const centerX = canvas.width / 2;
        const centerY = canvas.height / 2;
        const x_ = centerX + (x) * R / r;
        const y_ = centerY - (y) * R / r;
        console.log('mathToCanvas canvx, canvy: ', x_, y_)
        return { x: x_, y: y_ };
    }

    function drawLetters(){
        const ctx = canvas.getContext("2d");
        ctx.font = "18px Arial";
        ctx.fillStyle = "#00afff"; // Голубой цвет текста
        ctx.fillText(`${r/2}`, centerX + R/2 - 10, centerY + 20);
        ctx.fillText(`${r}`, centerX + R - 5, centerY + 20);
        ctx.fillText(`${-r/2}`, centerX - R/2 - 10, centerY + 20);
        ctx.fillText(`${-r}`, centerX - R - 5, centerY + 20);

        ctx.fillText(`${r/2}`, centerX + 10, centerY - R / 2);
        ctx.fillText(`${r}`, centerX + 10, centerY - R);
        ctx.fillText(`${-r/2}`, centerX + 10, centerY + R / 2 );
        ctx.fillText(`${-r}`, centerX + 10, centerY + R);
    }

    // Рисуем график
    drawAxes();
    drawShape();
    drawLetters();
    drawPoints();
}


export function canvasToMath(clientX, clientY, canvas, R, pixelsR) {
    const rect = canvas.getBoundingClientRect();
    // координаты от левого верхнего угла канваса
    const x_ = clientX - rect.x;
    const y_ = clientY - rect.y;
    // теперь переведем их в математические
    const centerX = canvas.width / 2;
    const centerY = canvas.height / 2;
    const x = ((x_ - centerX) / pixelsR) * R;
    const y = ((centerY - y_) / pixelsR) * R;
    console.log('in canvastOMATH clX, clY: ', clientX, clientY);
    console.log('in canvastOMATH: x, y', x, y);
    return {x, y};
}


//
// function redrawGraph(){
//     console.log("redrawing.....");
//     const canvas = document.getElementById("myCanvas");
//     const context = canvas.getContext("2d");
//     context.clearRect(0, 0, canvas.width, canvas.height);
//     drawGraph(canvas, state.r ? state.r : 5);
// }


// получить коорд. клика
// перевести в математические коорд.
// отправить на серв.
// а нарисует уже другая функция...
// export async function handleGraphClick(event, canvas, submitButton, R, pixelsR) {
//     const {x, y} = canvasToMath(event.clientX, event.clientY, canvas, R, pixelsR);
//     //рано рисовать. потому что не знаем попали или нет. нужно отправить запрос и получить ответ
//     document.getElementById("input-form:xValue").value = Math.round(x * 100) / 100; // округление до 2 знаков после запятой
//     document.getElementById("input-form:yValue").value = Math.round(y);
//     // программно нажимаем на кнопку отправки формы
//     submitButton.click();
// }