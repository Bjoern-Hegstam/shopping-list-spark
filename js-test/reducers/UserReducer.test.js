import reducer from '../../js/reducers/UserReducer';
import * as types from '../../js/actions/types';

describe('UserReducer', function () {
    const initialState = {};

    it('should return initial for unknown action type', function () {
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
                    id: '17',
                    username: 'Foo',
                    role: 'ADMIN'
                }
            }
        };

        // when
        const newState = reducer({}, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            currentUser: {
                id: 17,
                username: 'Foo',
                role: 'ADMIN'
            }
        })
    });

    it('should clear current user on LOGOUT_SUCCESS', function () {
        // given
        const state = {
            currentUser: {
                id: 17
            }
        };

        const action = {
            type: types.LOGOUT_SUCCESS
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            currentUser: undefined
        })
    });
});