import * as types from "../actions/types";

const initialState = {
    itemTypes: [],
    fetchingItemTypes: false,
    errorGetItemTypes: null
};

export default function (state = initialState, action) {
    switch (action.type) {
        case types.GET_ITEM_TYPES: {
            return {
                ...state,
                fetchingItemTypes: true,
                errorGetItemTypes: null
            };
        }
        case types.GET_ITEM_TYPES_FAIL: {
            return {
                ...state,
                fetchingItemTypes: false,
                errorGetItemTypes: action.error
            };
        }
        case types.GET_ITEM_TYPES_SUCCESS: {
            let {itemTypes} = action.payload.data;

            return {
                ...state,
                fetchingItemTypes: false,
                itemTypes
            }
        }
        default:
            return state;
    }
}