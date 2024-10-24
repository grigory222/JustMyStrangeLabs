"use strict";

const state = {
    x: undefined,
    y: undefined,
    r: undefined
};


const table = document.getElementById("result-table");
const error = document.getElementById("error");
const possibleXs = new Set([-5, -4, -3, -2, -1, 0, 1, 2, 3]);
const possibleRs = new Set([1.0, 2.0, 3.0, 4.0, 5.0]);

document.getElementById("xs").addEventListener("change", (ev) => {
    state.x = parseFloat(ev.target.value);
});

document.getElementById("y").addEventListener("change", (ev) => {
    state.y = parseFloat(ev.target.value);
});

document.getElementById("y").addEventListener("input", (ev) => {
    const regex = /^\d*\.?\d{0,3}$/;
    const value = ev.target.value;
    error.hidden = true;
    if (!regex.test(value)) {
        ev.target.value = value.slice(0, -1); // Удаляем последний символ
        error.hidden = false;
        error.innerText = "Введите Y с точностью 3 знака после запятой";
    }
});


document.getElementById("rs").addEventListener("change", (ev) => {
    state.r = parseFloat(ev.target.value);
    
     // Получаем все чекбоксы внутри fieldset с id="rs"
    const checkboxes = document.querySelectorAll('#rs input[type="checkbox"]');

    // Отключаем все остальные чекбоксы, кроме текущего
    checkboxes.forEach(cb => {
        if (cb !== ev.target) cb.checked = false;
    });

});


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

    if (isNaN(state.r) || !possibleRs.has(state.r)) {
        error.hidden = false;
        error.innerText = `r must be in [${[...possibleRs].join(" ,")}]`;
        throw new Error("Invalid state");
    }

    error.hidden = true;
}

// document.getElementById("data-form").addEventListener("submit", async function (ev) {
//     ev.preventDefault();
//
//
//     console.log(`x = ${state.x}; y = ${state.y}; r = ${state.r}`);
//
//     // Если хотя бы одно из полей пустое, показываем сообщение об ошибке
//     if ((!state.x && state.x != 0) || (!state.y && state.x != 0) || !state.r) {
//         error.hidden = false;
//         error.innerText = "Сперва заполните все поля";
//         console.log(`x = ${state.x}; y = ${state.y}; r = ${state.r}`);
//         return; // Прекращаем выполнение функции
//     }
//
//     try {
//         validateState(state);
//     } catch (error) {
//         return;
//     }
//
//
//     var newRow = createNewRow()
//
//     const params = new URLSearchParams(state);
//     try {
//         const localNow = new Date().toString();
//         const response = await fetch("http://localhost:8080/fcgi-bin/server.jar?" + params.toString(), {
//             method: "GET"
//         });
//
//         if (!response.ok) {
//             const errData = await response.json();
//             setValuesToRow(newRow, state.x, state.y, state.r, errData.now, "-", errData.reason);
//             saveToSessionStorage(response.ok, errData, state, localNow);
//             return;
//         }
//
//         const data = await response.json();
//         saveToSessionStorage(response.ok, data, state, localNow)
//         setValuesToRow(newRow, state.x, state.y, state.r, localNow, data.time, data.result);
//
//
//     } catch (error) {
//         // Обработка ошибок выполнения запроса
//         console.error("Ошибка при выполнении запроса:", error);
//         rowResult.textContent = "Запрос не удался";
//     }
//
//
//
//
// });

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

function restoreFromSessionStorage(){
    const savedResponses = sessionStorage.getItem('allResponses');
    if (savedResponses) {
        const responsesArray = JSON.parse(savedResponses);
        responsesArray.forEach(item => {
            const ok = item.ok;
            const response = item.response;          
            const state_ = item.state;    
            const localNow = item.localNow;
            console.log(localNow);
            var newRow = createNewRow()
            
            if (ok){
                setValuesToRow(newRow, state_.x, state_.y, state_.r, localNow, response.time, response.result);            
            } else {
                setValuesToRow(newRow, state_.x, state_.y, state_.r, localNow, "-", response.reason);
            }        
        });
    }
}


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


window.addEventListener('load', () => {
    restoreFromSessionStorage();
});