import {createSlice} from "@reduxjs/toolkit";

const initialState = {
    array: []
}

export const resultsSlice = createSlice({
    name: 'results',
    initialState,
    reducers: {
        addResult: (state, action) => {
            state.array.push(action.payload);
        },
        clearResults: (state) => {
            state.array = [];
        }
    }
})

export const {clearResults, addResult} = resultsSlice.actions;
export default resultsSlice.reducer;
