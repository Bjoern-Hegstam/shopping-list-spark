import {applyMiddleware, createStore} from "redux";
import axiosMiddleware from "redux-axios-middleware";
import reducers from "./reducers";
import axios from "axios/index";

const STATE_KEY = 'STATE';

function loadState() {
    const persistedState = localStorage.getItem(STATE_KEY);
    if (persistedState) {
        return {
            user: JSON.parse(persistedState)
        };
    }
    return {};
}

function saveState(store) {
    localStorage.setItem(STATE_KEY, JSON.stringify(store.getState().user));
}

export default () => {
    const persistedState = loadState();

    let store = createStore(
        reducers,
        persistedState,
        applyMiddleware(
            axiosMiddleware(
                axios.create({
                    baseURL: '/api',
                    responseType: 'json',
                    withCredentials: true
                })
            )
        )
    );

    store.subscribe(() => saveState(store));

    return store;
};
