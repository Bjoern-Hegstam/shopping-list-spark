import { AUTH_1_0 } from '../../js/actions/MimeTypes';
import { login, logout, registerUser } from '../../js/actions/UserActions';
import * as types from '../../js/actions/types';

const username = 'test-username';
const password = 'test-password';
const email = 'test-email';

it('registerUser', () => {
  // when
  const action = registerUser({ username, password, email });

  // then
  expect(action).toEqual({
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
  });
});

it('login', () => {
  // when
  const action = login({ username, password });

  // then
  expect(action).toEqual({
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
  });
});

it('logout', () => {
  // when
  const action = logout();

  // then
  expect(action).toEqual({
    type: types.LOGOUT,
  });
});
