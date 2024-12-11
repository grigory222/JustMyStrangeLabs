import {configureStore} from "@reduxjs/toolkit";
import showPasswordReducer from "./ShowPasswordSlice";

export const store = configureStore({
    reducer: {
        showPassword: showPasswordReducer,
    }
});