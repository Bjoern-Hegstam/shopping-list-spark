import * as types from './types';

export function registerUser({ username, password, email }) {
    return {
        type: types.REGISTER_USER,
        payload: {
            request: {
                method: 'post',
                url: 'user',
                data: {
                    username,
                    password,
                    email
                }
            }
        }
    };
}

export function login({ username, password }) {
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