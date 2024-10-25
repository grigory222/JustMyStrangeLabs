"use strict";

//import {drawGraph, drawPoint, handleCanvasClick} from './graph'

const state = {
    x: undefined,
    y: undefined,
    r: undefined
};

const graphScale = 100;

const table = document.getElementById("result-table");
const error = document.getElementById("error");
const possibleXs = new Set([-2, -1.5, -1, -0.5, 0, 0.5, 1.5, 2]);

document.getElementById("xs").addEventListener("change", (ev) => {
    state.x = parseFloat(ev.target.value);
});

document.getElementById("y").addEventListener("change", (ev) => {
    state.y = parseFloat(ev.target.value);
});

document.getElementById("y").addEventListener("input", (ev) => {
    controlPrecision(ev);
});

document.getElementById("r").addEventListener("change", (ev) => {
    state.r = parseFloat(ev.target.value);
});

document.getElementById("r").addEventListener("input", (ev) => {
    controlPrecision(ev)
});

function controlPrecision(ev){
    const regex = /^\d*\.?\d{0,3}$/;
    const value = ev.target.value;
    error.hidden = true;
    if (!regex.test(value)) {
        ev.target.value = value.slice(0, -1); // Удаляем последний символ
        error.hidden = false;
        error.innerText = "Введите с точностью 3 знака после запятой";
    }
}



const validateState = (state) => {
    if (isNaN(state.x) || !possibleXs.has(state.x)) {
        error.hidden = false;
        error.innerText = `x must be in [${[...possibleXs].join(" ,")}]`;
        throw new Error("Invalid state");
    }

    if (isNaN(state.y) || state.y < -5 || state.y > 5) {
        error.hidden = false;
        error.innerText = "y must be in range [-5, 5]";
        throw new Error("Invalid state");
    }
    if (isNaN(state.r) || state.r < 2 || state.r > 5) {
        error.hidden = false;
        error.innerText = "r must be in range [2, 5]";
        throw new Error("Invalid state");
    }

    error.hidden = true;
}

document.getElementById("data-form").addEventListener("submit", async function (ev) {
    console.log(`x = ${state.x}; y = ${state.y}; r = ${state.r}`);

    // Если хотя бы одно из полей пустое, показываем сообщение об ошибке
    if ((!state.x && state.x != 0) || (!state.y && state.x != 0) || !state.r) {
        error.hidden = false;
        error.innerText = "Сперва заполните все поля";
        console.log(`x = ${state.x}; y = ${state.y}; r = ${state.r}`);
        return; // Прекращаем выполнение функции
    }
});

function saveToSessionStorage(ok, data, state, localNow) {
    // Получаем текущие данные из sessionStorage
    let responses = JSON.parse(sessionStorage.getItem('allResponses')) || [];
    
    // Добавляем новый ответ в массив
    responses.push({
        ok: ok,
        state: state, 
        response: data,
        localNow: localNow.toString()
    });
    
    sessionStorage.setItem('allResponses', JSON.stringify(responses));
}

// function restoreFromSessionStorage(){
//     const savedResponses = sessionStorage.getItem('allResponses');
//     if (savedResponses) {
//         const responsesArray = JSON.parse(savedResponses);
//         responsesArray.forEach(item => {
//             const ok = item.ok;
//             const response = item.response;
//             const state_ = item.state;
//             const localNow = item.localNow;
//             console.log(localNow);
//             let newRow = createNewRow()
//
//             if (ok){
//                 setValuesToRow(newRow, state_.x, state_.y, state_.r, localNow, response.time, response.result);
//             } else {
//                 setValuesToRow(newRow, state_.x, state_.y, state_.r, localNow, "-", response.reason);
//             }
//         });
//     }
// }


function createNewRow(){
    const newRow = table.insertRow(-1);

    const rowX = newRow.insertCell(0);
    const rowY = newRow.insertCell(1);
    const rowR = newRow.insertCell(2);
    
    const rowTime = newRow.insertCell(3);
    const rowExecTime = newRow.insertCell(4);
    const rowResult = newRow.insertCell(5);

    return newRow;
}

function setValuesToRow(row, ...values){
    values.forEach((value, index) => {
        row.cells[index].textContent = value;
    });
}


document.addEventListener("DOMContentLoaded", () => {
    //restoreFromSessionStorage();
    const canvas = document.getElementById("myCanvas");

    drawGraph(canvas, graphScale);

    canvas.addEventListener("click",  async function (event) {
        if (!state.r) {
            error.hidden = false;
            error.innerText = "Сперва выберите R";
            return; // Прекращаем выполнение функции
        }
        await handleClick(event, canvas, state.r, graphScale);
    });
});