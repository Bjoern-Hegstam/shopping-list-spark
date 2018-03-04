import * as types from '../actions/types';

export default function (state = {}, action) {
    switch (action.type) {
        case types.LOGIN_SUCCESS:
            return {
                ...state,
                ...action.payload.data
            };
        default:
            return state;
    }
};