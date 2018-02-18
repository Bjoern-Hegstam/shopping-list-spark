import React from 'react';
import {render} from 'react-dom'
import {HashRouter} from 'react-router-dom';
import {Provider} from 'react-redux';
import {applyMiddleware, createStore} from 'redux';
import reducers from './reducers/index';
import axios from 'axios';
import axiosMiddleware from 'redux-axios-middleware';
import App from "./App";

const client = axios.create({
    baseURL: '/api'
});

let store = createStore(
    reducers,
    applyMiddleware(
        axiosMiddleware(client)
    )
);

render(
    <Provider store={store}>
        <HashRouter>
            <App/>
        </HashRouter>
    </Provider>,
    document.getElementById('root')
);