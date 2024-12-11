import {createSlice} from "@reduxjs/toolkit";

const initialState = {
    showPassword: false,
}
export const showPasswordSlice = createSlice({
    name: 'showPassword',
    initialState,
    reducers: {
        changeShowPassword: (state) => {
            state.showPassword = !state.showPassword;
        }
    }
})

export const {changeShowPassword} = showPasswordSlice.actions;
export default showPasswordSlice.reducer;
