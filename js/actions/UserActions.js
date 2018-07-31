import * as types from './types';

export function login(username, password) {
    return {
        type: types.LOGIN,
        payload: {
            request: {
                method: 'post',
                url: 'auth',
                auth: {
                    username,
                    password
                }
            }
        }
    };
}

export function logout() {
    return {
        type: types.LOGOUT
    };
}