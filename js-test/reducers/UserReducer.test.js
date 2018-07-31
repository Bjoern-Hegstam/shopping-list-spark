import reducer from '../../js/reducers/UserReducer';
import * as types from '../../js/actions/types';

describe('UserReducer', function () {
    const initialState = {};

    it('should return initial state for unknown action type', function () {
        // given
        const state = undefined;
        const action = {type: 'unknown'};

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual(initialState)
    });

    it('should store current user on LOGIN_SUCCESS', function () {
        // given
        const action = {
            type: types.LOGIN_SUCCESS,
            payload: {
                data: {
                    token: 'token-1',
                    user: {
                        id: 'd6c08dd9-d430-4d2c-b471-c469e79c4797',
                        username: 'Foo',
                        role: 'ADMIN'
                    }
                }
            }
        };

        // when
        const newState = reducer({}, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            token: 'token-1',
            currentUser: {
                id: 'd6c08dd9-d430-4d2c-b471-c469e79c4797',
                username: 'Foo',
                role: 'ADMIN'
            }
        })
    });

    it('should clear current user and token on LOGOUT', function () {
        // given
        const state = {
            token: 'token-1',
            currentUser: {
                id: 'd6c08dd9-d430-4d2c-b471-c469e79c4797'
            }
        };

        const action = {
            type: types.LOGOUT
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            token: undefined,
            currentUser: undefined
        })
    });
});