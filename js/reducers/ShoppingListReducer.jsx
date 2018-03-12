import * as types from "../actions/types";

const initialState = {
    shoppingLists: {},
    fetching: false,
    error: null,
};

export default function (state = initialState, action) {
    switch (action.type) {
        case types.GET_SHOPPING_LISTS:
            return {
                ...state,
                fetching: true,
                error: null
            };
        case types.GET_SHOPPING_LISTS_SUCCESS:
            const {shoppingLists} = action.payload.data;
            const newState = {
                ...state,
                fetching: false
            };

            shoppingLists.forEach(list => {
                if (list.id in newState.shoppingLists) {
                    newState.shoppingLists[list.id].name = list.name;
                } else {
                    newState.shoppingLists[list.id] = {
                        ...list,
                        items: []
                    };
                }
            });
            return newState;
        case types.GET_SHOPPING_LISTS_FAIL:
            return {
                ...state,
                fetching: false,
                error: action.error
            };
        default:
            return state;
    }
}