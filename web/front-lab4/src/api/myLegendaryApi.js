// Need to use the React-specific entry point to import createApi
import {createApi, fetchBaseQuery} from '@reduxjs/toolkit/query/react'

// Define a service using a base URL and expected endpoints
export const myLegendaryApi = createApi({
    reducerPath: 'myLegendaryApi',
    baseQuery: fetchBaseQuery({baseUrl: 'http://localhost:8080/lab4-1.0-SNAPSHOT/api/'}),
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
                    'Content-Type': 'application/json', // Important to specify the correct content type
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
                    'Content-Type': 'application/json', // Important to specify the correct content type
                },
                credentials: "include",
            })
        })
    }),
})

// Export hooks for usage in functional components, which are
// auto-generated based on the defined endpoints
export const {useGetPokemonByNameQuery, useSendPointMutation, useLoginMutation} = myLegendaryApi