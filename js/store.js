import { applyMiddleware, compose, createStore } from 'redux';
import axiosMiddleware from 'redux-axios-middleware';
import axios from 'axios/index';
import jwtDecode from 'jwt-decode';
import moment from 'moment';
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

export const logoutUserWhenTokenExpires = store => next => (action) => {
  next(action); // Allows action LOGIN_SUCCESS to store token before we inspect it

  if (action.type === types.LOGOUT) {
    clearTimeout(tokenExpirationTimer);
    tokenExpirationTimer = null;
    return;
  }

  if (!tokenExpirationTimer) {
    const state = store.getState();
    if (state.auth.token) {
      const claims = jwtDecode(state.auth.token);
      const timeout = moment.unix(claims.exp).diff(moment());
      tokenExpirationTimer = setTimeout(() => {
        tokenExpirationTimer = null; // Or else the following logout action will cause the middleware to clear the timeout
        store.dispatch(logout());
      }, timeout);
    }
  }
};

function setupMiddleware() {
  const middleware = applyMiddleware(
    axiosMiddleware(
      axios.create({
        baseURL: API_BASE_URL,
        responseType: 'json',
      }),
    ),
    logoutUserWhenTokenExpires,
  );

  if (process.env.NODE_ENV !== 'production') {
    // Needed for Redux plugin in browser
    const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

    return composeEnhancers(middleware);
  }

  return middleware;
}

export default () => {
  const store = createStore(reducers, loadState(), setupMiddleware());

  store.subscribe(() => saveState(store));

  return store;
};
