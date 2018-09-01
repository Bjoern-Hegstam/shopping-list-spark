import { applyMiddleware, createStore } from 'redux';
import axiosMiddleware from 'redux-axios-middleware';
import axios from 'axios/index';
import reducers from './reducers';
import * as types from './actions/types';
import { logout } from './actions/UserActions';

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

let tokenExpirationTimer;

const logoutUserWhenTokenExpires = store => next => (action) => {
    next(action);

    if (action.type === types.LOGIN_SUCCESS) {
        if (tokenExpirationTimer) {
            clearTimeout(tokenExpirationTimer);
        }

        const state = store.getState();
        const { token } = state.auth;
        // TODO: Install jwt-decode once npm DNS issue resolved, extract expiration time from token and use to calculate correct timeout
        tokenExpirationTimer = setTimeout(() => {
            tokenExpirationTimer = null; // Or else the following logout action will cause the middleware to clear the timeout
            store.dispatch(logout());
        }, 216000); // 1 hour
    } else if (action.type === types.LOGOUT && tokenExpirationTimer) {
        clearTimeout(tokenExpirationTimer);
    }
};

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
            logoutUserWhenTokenExpires,
        ),
    );

    store.subscribe(() => saveState(store));

    return store;
};
