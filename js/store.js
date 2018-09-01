import { applyMiddleware, createStore } from 'redux';
import axiosMiddleware from 'redux-axios-middleware';
import axios from 'axios/index';
import reducers from './reducers';

const STATE_KEY = 'SHOPPING_LIST_STATE';

function loadState() {
    const persistedState = localStorage.getItem(STATE_KEY);
    if (persistedState) {
        return {
            auth: JSON.parse(persistedState),
        };
    }
    return {};
}

function saveState(store) {
    localStorage.setItem(STATE_KEY, JSON.stringify(store.getState().auth));
}

export default () => {
    const persistedState = loadState();
    const store = createStore(
        reducers,
        persistedState,
        applyMiddleware(
            axiosMiddleware(
                axios.create({
                    baseURL: `${BASE_URL}/api`,
                    responseType: 'json',
                }),
            ),
        ),
    );

    store.subscribe(() => saveState(store));

    return store;
};
