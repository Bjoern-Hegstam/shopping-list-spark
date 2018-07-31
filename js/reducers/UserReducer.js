import * as types from '../actions/types';

export default function (state = {}, action) {
    switch (action.type) {
        case types.LOGIN_SUCCESS: {
            const {token, user} = action.payload.data;

            return {
                ...state,
                token,
                currentUser: {
                    id : Number(user.id),
                    username: user.username,
                    role: user.role
                }
            };
        }
        case types.LOGOUT:
            return {
                ...state,
                token: undefined,
                currentUser: undefined
            };
        default:
            return state;
    }
};