import * as types from './types';

export function login(username, password) {
    return {
        type: types.LOGIN,
        payload: {
            request: {
                method: 'post',
                baseURL: '/',
                url: 'login',
                data: {
                    username,
                    password
                }
            }
        }
    };
}

export function logout() {
    return {
        type: types.LOGOUT,
        payload: {
            request: {
                method: 'post',
                baseURL: '/',
                url: 'logout'
            }
        }
    };
}