import {createSlice} from "@reduxjs/toolkit";

const initialState = {
    count: 1,
    array: [{ id: 1, x: 1, y: 1.0, r: 0, result: 'Промах' }]
}
export const resultsSlice = createSlice({
    name: 'results',
    initialState,
    reducers: {
        addResult: (state, action) => {
            state.array.push(action.payload);
        }
    }
})

export const {addResult} = resultsSlice.actions;
export default resultsSlice.reducer;
