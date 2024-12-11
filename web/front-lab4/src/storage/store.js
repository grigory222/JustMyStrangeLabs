import {combineReducers, configureStore} from "@reduxjs/toolkit";
import showPasswordReducer from "./ShowPasswordSlice";
import resultsReducer from "./ResultsSlice";
import {myLegendaryApi} from "../api/myLegendaryApi.js";
import {setupListeners} from "@reduxjs/toolkit/query";

const rootReducer = combineReducers({
    showPassword: showPasswordReducer,
    results: resultsReducer
})

export const store = configureStore({
    reducer: {
        [myLegendaryApi.reducerPath]: myLegendaryApi.reducer,
        reducer: rootReducer,
    },
    middleware: (getDefaultMiddleware) =>
        getDefaultMiddleware().concat(myLegendaryApi.middleware),
});

// see `setupListeners` docs - takes an optional callback as the 2nd arg for customization
setupListeners(store.dispatch)