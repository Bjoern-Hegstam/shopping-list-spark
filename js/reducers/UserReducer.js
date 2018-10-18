import * as types from '../actions/types';

const initialState = {
    loggingIn: false,
    errorLogin: null,
};

export default function (state = initialState, action) {
    switch (action.type) {
        case types.LOGIN_SUCCESS: {
            const { token, user } = action.payload.data;

            return {
                ...state,
                loggingIn: false,
                errorLogin: null,
                token,
                currentUser: {
                    id: user.id,
                    username: user.username,
                    role: user.role,
                },
            };
        }

        case types.LOGOUT:
            return {
                ...state,
                token: undefined,
                currentUser: undefined,
            };
        default:
            return state;
    }
}
