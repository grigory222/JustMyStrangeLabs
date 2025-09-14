import {createSlice} from "@reduxjs/toolkit";
import Cookies from 'js-cookie';

const initialState = {
    isLogged: !!Cookies.get('access_token'), // на основе токена
    refresh_token: Cookies.get('refresh_token'),
    access_token: Cookies.get('access_token'),
    isLoading: true, // <== добавлено
};

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
        setLoading: (state, action) => {
            state.isLoading = action.payload;
        },
        logout: (state) => {
            state.isLogged = false;
            state.access_token = null;
            state.refresh_token = null;
            state.isLoading = false;
            Cookies.remove('access_token');
            Cookies.remove('refresh_token');
        }
    }
});

export const {
    setLoggedIn,
    setAccessToken,
    setRefreshToken,
    setLoading,
    logout
} = isLoggedSlice.actions;

export default isLoggedSlice.reducer;
