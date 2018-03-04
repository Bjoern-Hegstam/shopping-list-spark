import * as types from '../actions/types';

export default function (state = {}, action) {
    switch (action.type) {
        case types.LOGIN_SUCCESS:
            const {id, username, role} = action.payload.data;

            return {
                ...state,
                user: {
                    id,
                    username,
                    role
                }
            };
        default:
            return state;
    }
};