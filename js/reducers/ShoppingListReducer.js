import * as types from "../actions/types";

const initialState = {
    shoppingLists: {},
    fetchingShoppingLists: false,
    errorGetShoppingLists: null
};

export default function (state = initialState, action) {
    switch (action.type) {
        case types.GET_SHOPPING_LISTS: {
            return {
                ...state,
                fetchingShoppingLists: true,
                errorGetShoppingLists: null
            };
        }
        case types.GET_SHOPPING_LISTS_SUCCESS: {
            const {shoppingLists} = action.payload.data;
            const newState = {
                shoppingLists: {},
                fetchingShoppingLists: false,
                errorGetShoppingLists: null
            };

            shoppingLists.forEach(list => {
                newState.shoppingLists[list.id] = {
                    ...list,
                    items: [],
                    fetching: false,
                    error: null
                };
            });
            return newState;
        }
        case types.GET_SHOPPING_LISTS_FAIL: {
            return {
                ...state,
                fetchingShoppingLists: false,
                errorGetShoppingLists: action.error
            };
        }
        case types.GET_SHOPPING_LIST: {
            const {listId} = action.queryInfo;

            const newState = {...state};

            newState.shoppingLists[listId] = {
                id: listId,
                name: '',
                items: [],
                ...state.shoppingLists[listId],
                fetching: true,
                error: null
            };
            return newState;
        }
        case types.GET_SHOPPING_LIST_SUCCESS: {
            const listId = action.payload.data.id;
            const {name, items} = action.payload.data;

            const newState = {...state};

            newState.shoppingLists[listId] = {
                id: listId,
                name,
                items,
                fetching: false,
                error: null
            };
            return newState;
        }
        case types.GET_SHOPPING_LIST_FAIL: {
            const {listId} = action.meta.previousAction.queryInfo;

            const newState = {...state};

            newState.shoppingLists[listId] = {
                ...state.shoppingLists[listId],
                fetching: false,
                error: action.error
            };
            return newState;
        }
        default:
            return state;
    }
}