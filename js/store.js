import {applyMiddleware, createStore} from "redux";
import axiosMiddleware from "redux-axios-middleware";
import reducers from "./reducers";
import axios from "axios/index";

function loadState() {
    const persistedState = localStorage.getItem('STATE');
    if (persistedState) {
        return JSON.parse(persistedState);
    }
    return {};
}

function saveState(store) {
    localStorage.setItem('STATE', JSON.stringify(store.getState()));
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
