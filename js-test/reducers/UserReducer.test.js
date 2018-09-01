import reducer from '../../js/reducers/UserReducer';
import * as types from '../../js/actions/types';

const initialState = {
    registeringUser: false,
    errorRegisterUser: null,

    loggingIn: false,
    errorLogin: null,
};

it('should return initial state for unknown action type', () => {
    // given
    const state = { ...initialState };
    const action = { type: 'unknown' };

    // when
    const newState = reducer(state, action);

    // then
    expect(newState).toEqual(initialState);
});

describe('REGISTER_USER', () => {
    it('should set registeringUser flag and reset error on action REGISTER_USER', () => {
        const state = {
            ...initialState,
            errorRegisterUser: 'Error',
        };
        const action = { type: types.REGISTER_USER };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            registeringUser: true,
            errorRegisterUser: null,
        });
    });

    it('should set registeringUser flag to false on REGISTER_USER_SUCCESS', () => {
        // given
        const state = {
            ...initialState,
            registeringUser: true,
        };
        const action = {
            type: types.REGISTER_USER_SUCCESS,
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
            registeringUser: false,
            errorRegisterUser: null,
        });
    });

    it('should set error on REGISTER_USER_FAIL', () => {
        // given
        const state = {
            ...initialState,
            registeringUser: true,
        };
        const action = {
            type: types.REGISTER_USER_FAIL,
            error: 'Error while registering',
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            registeringUser: false,
            errorRegisterUser: 'Error while registering',
        });
    });
});

describe('LOGIN', () => {
    it('should set loggingIn flag and reset error on action LOGIN', () => {
        const state = {
            ...initialState,
            errorLogin: 'Error',
        };
        const action = { type: types.LOGIN };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            loggingIn: true,
            errorLogin: null,
        });
    });

    it('should store current user on LOGIN_SUCCESS', () => {
        // given
        const state = {
            ...initialState,
            loggingIn: true,
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
            loggingIn: false,
            errorLogin: null,
            token: 'token-1',
            currentUser: {
                id: 'd6c08dd9-d430-4d2c-b471-c469e79c4797',
                username: 'Foo',
                role: 'ADMIN',
            },
        });
    });

    it('should set error on LOGIN_FAIL', () => {
        // given
        const state = {
            ...initialState,
            loggingIn: true,
        };
        const action = {
            type: types.LOGIN_FAIL,
            error: 'Error while logging in',
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            loggingIn: false,
            errorLogin: 'Error while logging in',
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
