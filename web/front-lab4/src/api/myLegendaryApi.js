// Need to use the React-specific entry point to import createApi
import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react'

// Define a service using a base URL and expected endpoints
export const myLegendaryApi = createApi({
    reducerPath: 'myLegendaryApi',
    baseQuery: fetchBaseQuery({ baseUrl: 'https://pokeapi.co/api/v2/' }),
    endpoints: (builder) => ({
        getPokemonByName: builder.query({
            query: (name) => `pokemon/${name}`,
        }),
        getPokemonById: builder.query({
            query: (id) => `pokemon/${id}`,
        }),
        sendPoint: builder.mutation({
            query: (point) => ({
              url: "point/add",
              method: "POST",
              body: JSON.stringify(point),
            })
        })
    }),
})

// Export hooks for usage in functional components, which are
// auto-generated based on the defined endpoints
export const { useGetPokemonByNameQuery, useSendPointMutation } = myLegendaryApi