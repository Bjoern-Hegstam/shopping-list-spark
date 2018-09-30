import * as types from '../actions/types';

const initialState = {
    registeringUser: false,
    errorRegisterUser: null,

    loggingIn: false,
    errorLogin: null,
};

export default function (state = initialState, action) {
    switch (action.type) {
        case types.REGISTER_USER: {
            return {
                ...state,
                registeringUser: true,
                errorRegisterUser: null,
            };
        }
        case types.REGISTER_USER_SUCCESS: {
            return {
                ...state,
                registeringUser: false,
                errorRegisterUser: null,
            };
        }
        case types.REGISTER_USER_FAIL: {
            return {
                ...state,
                registeringUser: false,
                errorRegisterUser: action.error,
            };
        }

        case types.LOGIN: {
            return {
                ...state,
                loggingIn: true,
                errorLogin: null,
            };
        }
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
        case types.LOGIN_FAIL: {
            return {
                ...state,
                loggingIn: false,
                errorLogin: action.error,
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
