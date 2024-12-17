import {createSlice} from "@reduxjs/toolkit";
import Cookies from 'js-cookie';

const initialState = {
    isLogged: true,
    refresh_token: Cookies.get('refresh_token'),
    access_token: Cookies.get('access_token'),
}

export const isLoggedSlice = createSlice({
    name: 'auth',
    initialState,
    reducers: {
        setLoggedIn: (state, action) => {
            state.isLogged = action.payload;
        },
        setAccessToken: (state, action) => {
            state.access_token = action.payload;
        },
        setRefreshToken: (state, action) => {
            state.refresh_token = action.payload;
        },
    }
})

export const {setLoggedIn, setAccessToken, setRefreshToken} = isLoggedSlice.actions;
export default isLoggedSlice.reducer;
