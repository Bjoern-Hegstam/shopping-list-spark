import * as types from './types';
import { AUTH_1_0 } from './MimeTypes';

export function registerUser({ username, password, email }) {
  return {
    type: types.REGISTER_USER,
    payload: {
      request: {
        method: 'post',
        url: 'user',
        headers: {
          accept: AUTH_1_0,
        },
        data: {
          username,
          password,
          email,
        },
      },
    },
  };
}

export function login({ username, password }) {
  return {
    type: types.LOGIN,
    payload: {
      request: {
        method: 'post',
        url: 'auth',
        headers: {
          accept: AUTH_1_0,
        },
        auth: {
          username,
          password,
        },
      },
    },
  };
}

export function logout() {
  return {
    type: types.LOGOUT,
  };
}
