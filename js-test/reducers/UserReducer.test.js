import reducer from '../../js/reducers/UserReducer';
import * as types from '../../js/actions/types';

const initialState = {};

it('should return initial state for unknown action type', () => {
  // given
  const state = { ...initialState };
  const action = { type: 'unknown' };

  // when
  const newState = reducer(state, action);

  // then
  expect(newState).toEqual(initialState);
});

describe('LOGIN', () => {
  it('should store current user on LOGIN_SUCCESS', () => {
    // given
    const state = {
      ...initialState,
    };
    const action = {
      type: types.LOGIN_SUCCESS,
      payload: {
        data: {
          token: 'token-1',
          user: {
            id: 'd6c08dd9-d430-4d2c-b471-c469e79c4797',
            username: 'Foo',
            role: 'ADMIN',
          },
        },
      },
    };

    // when
    const newState = reducer(state, action);

    // then
    expect(newState).toEqual({
      ...initialState,
      token: 'token-1',
      currentUser: {
        id: 'd6c08dd9-d430-4d2c-b471-c469e79c4797',
        username: 'Foo',
        role: 'ADMIN',
      },
    });
  });
});

describe('LOGOUT', () => {
  it('should clear current user and token on LOGOUT', () => {
    // given
    const state = {
      ...initialState,
      token: 'token-1',
      currentUser: {
        id: 'd6c08dd9-d430-4d2c-b471-c469e79c4797',
      },
    };

    const action = {
      type: types.LOGOUT,
    };

    // when
    const newState = reducer(state, action);

    // then
    expect(newState).toEqual({
      ...initialState,
      token: undefined,
      currentUser: undefined,
    });
  });
});
