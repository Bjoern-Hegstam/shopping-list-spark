import * as types from '../actions/types';

export default function (state = {}, action) {
    switch (action.type) {
        case types.LOGIN_SUCCESS:
            const {id, username, role} = action.payload.data;

            return {
                ...state,
                currentUser: {
                    id : Number(id),
                    username,
                    role
                }
            };
        case types.LOGOUT_SUCCESS:
            return {
                ...state,
                currentUser: undefined
            };
        default:
            return state;
    }
};