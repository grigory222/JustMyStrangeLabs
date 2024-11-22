"use strict";

//import {drawGraph, drawPoint, handleCanvasClick} from './graph'

const state = {
    x: undefined,
    y: undefined,
    r: undefined
};

const graphScale = 20;

const table = document.getElementById("results-table");
const error = document.getElementById("error");

function updateR(event){
    state.r = parseInt(event.target.value); // Обновляем значение радиуса
    console.log(`New R value: ${state.r}`); // Для отладки
    redrawGraph(); // Перерисовываем график
}

document.getElementById("input-form:xValue").addEventListener("input", (ev) => {
    controlPrecision(ev);
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
function attachObserverToResultsTable() {
    const parentElement = document.querySelector(".content"); // Родительский элемент таблицы
    if (!parentElement) {
        console.error("Parent container for results-table not found!");
        return;
    }

    const observer = new MutationObserver(() => {
        console.log("Results table updated. Redrawing graph...");
        redrawGraph(); // Ваша функция для перерисовки графика
    });

    observer.observe(parentElement, {
        childList: true, // Отслеживаем добавление/удаление дочерних элементов
        subtree: true,   // Отслеживаем изменения внутри вложенных узлов
    });

    console.log("MutationObserver attached to results-table's parent.");
}


document.addEventListener("DOMContentLoaded", () => {
    const canvas = document.getElementById("myCanvas");

    drawGraph(canvas, 5); // изначально пусть будет r=5

    canvas.addEventListener("click",  async function (event) {
        if (!state.r) {
            console.log("Сперва выберите R");
            error.hidden = false;
            error.innerText = "Сперва выберите R";
            return; // Прекращаем выполнение функции
        }
        error.hidden = true;
        const submitButton = document.getElementById("input-form:submit-button");
        await handleClick(event, canvas, submitButton, state.r, graphScale*state.r);
    });
    // чтобы обновлялся график
    attachObserverToResultsTable();
});

function checkR(){
    if (!state.r) {
        console.log("Сперва выберите R");
        error.hidden = false;
        error.innerText = "Сперва выберите R";
        return; // Прекращаем выполнение функции
    }
    error.hidden = true;
}