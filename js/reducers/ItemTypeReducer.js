import * as types from '../actions/types';

const initialState = {
    itemTypes: [],

    addingItemType: false,
    errorAddItemType: null,

    fetchingItemTypes: false,
    errorGetItemTypes: null,
};

export default function (state = initialState, action) {
    switch (action.type) {
        case types.ADD_ITEM_TYPE: {
            return {
                ...state,
                addingItemType: true,
                errorAddItemType: null,
            };
        }
        case types.ADD_ITEM_TYPE_FAIL: {
            return {
                ...state,
                addingItemType: false,
                errorAddItemType: action.error,
            };
        }
        case types.ADD_ITEM_TYPE_SUCCESS: {
            return {
                ...state,
                addingItemType: false,
            };
        }

        case types.GET_ITEM_TYPES: {
            return {
                ...state,
                fetchingItemTypes: true,
                errorGetItemTypes: null,
            };
        }
        case types.GET_ITEM_TYPES_FAIL: {
            return {
                ...state,
                fetchingItemTypes: false,
                errorGetItemTypes: action.error,
            };
        }
        case types.GET_ITEM_TYPES_SUCCESS: {
            const { itemTypes } = action.payload.data;

            return {
                ...state,
                fetchingItemTypes: false,
                itemTypes,
            };
        }
        default:
            return state;
    }
}
