import { useEffect } from "react";
import { useDispatch } from "react-redux";
import Cookies from "js-cookie";
import { setLoggedIn, setAccessToken, setRefreshToken, setLoading } from "../storage/IsLoggedSlice.js"

export const useAuthInit = () => {
    const dispatch = useDispatch();

    useEffect(() => {
        const access = Cookies.get('access_token');
        const refresh = Cookies.get('refresh_token');

        if (access && refresh) {
            dispatch(setAccessToken(access));
            dispatch(setRefreshToken(refresh));
            dispatch(setLoggedIn(true));
        } else {
            dispatch(setLoggedIn(false));
        }

        dispatch(setLoading(false)); // ВАЖНО!
    }, [dispatch]);
};
