// Need to use the React-specific entry point to import createApi
import {createApi, fetchBaseQuery} from '@reduxjs/toolkit/query/react'
import {setLoggedIn} from "../storage/IsLoggedSlice.js";


// Создаем базовый fetch с перехватом ошибок
const baseQuery = fetchBaseQuery({
    baseUrl: 'http://localhost:8080/lab4-1.0-SNAPSHOT/api/',
    credentials: 'include', // Для передачи cookies
})

// Обертка для обработки 401 ошибок
const baseQueryWithReauth = async (args, api, extraOptions) => {
    const { url } = args;

    let result = await baseQuery(args, api, extraOptions) // Выполняем обычный запрос

    console.log("err:", result?.error);
    console.log("status: ", result?.error?.status);

    if ( url !== 'auth/login' &&
        result?.error &&
        (result.error.status === 401 || result.error.originalStatus === 401)) {
        // Если сервер вернул 401, пытаемся обновить токен
        const refreshResult = await baseQuery(
            {
                url: 'auth/refresh',
                method: 'POST',
                credentials: 'include',
            },
            api,
            extraOptions
        )

        if (refreshResult.data) {
            api.dispatch(setLoggedIn(true));

            // повторяем исходный запрос
            result = await baseQuery(args, api, extraOptions)
        } else {
            console.error('Session expired. Please log in again.')
            api.dispatch(setLoggedIn(false));
        }
    }
    return result
}


// Define a service using a base URL and expected endpoints
export const myLegendaryApi = createApi({
    reducerPath: 'myLegendaryApi',
    baseQuery: baseQueryWithReauth,
    endpoints: (builder) => ({
        getPoints: builder.query({
            query: () => ({
                url: "point/get",
                method: 'GET',
                credentials: "include"
            }),
        }),
        sendPoint: builder.mutation({
            query: (point) => ({
                url: "point/add",
                method: "POST",
                body: JSON.stringify(point),
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: "include",
            })
        }),
        login: builder.mutation({
            query: (credentials) => ({
                url: "auth/login",
                method: "POST",
                body: JSON.stringify(credentials),
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: "include",
            })
        }),
        signup: builder.mutation({
            query: (credentials) => ({
                url: "auth/signup",
                method: "POST",
                body: JSON.stringify(credentials),
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: "include",
            })
        }),
        logout: builder.mutation({
            query: () => ({
                url: "auth/logout",
                method: "POST",
                credentials: "include",
            })
        }),
    }),
})

// Export hooks for usage in functional components, which are
// auto-generated based on the defined endpoints
export const {useGetPointsQuery, useSendPointMutation, useLoginMutation, useLogoutMutation, useSignupMutation} = myLegendaryApi