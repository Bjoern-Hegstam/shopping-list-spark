import * as types from "../actions/types";

const initialState = {
    shoppingLists: {},
    fetchingLists: false,
    error: null,
};

export default function (state = initialState, action) {
    let newState, listId;

    switch (action.type) {
        case types.GET_SHOPPING_LISTS:
            return {
                ...state,
                fetchingLists: true,
                error: null
            };
        case types.GET_SHOPPING_LISTS_SUCCESS:
            const {shoppingLists} = action.payload.data;
            newState = {
                shoppingLists: {},
                fetchingLists: false,
                error: null
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
        case types.GET_SHOPPING_LISTS_FAIL:
            return {
                ...state,
                fetchingLists: false,
                error: action.error
            };
        case types.GET_SHOPPING_LIST:
            listId = action.queryInfo.listId;

            newState = {
                ...state,
                fetchingLists: false,
                error: null
            };

            newState.shoppingLists[listId] = {
                id: listId,
                name: '',
                items: [],
                ...state.shoppingLists[listId],
                fetching: true,
                error: null
            };
            return newState;
        case types.GET_SHOPPING_LIST_SUCCESS:
            listId = action.payload.data.id;
            const {name, items} = action.payload.data;

            newState = {
                ...state,
                fetchingLists: false,
                error: null
            };

            newState.shoppingLists[listId] = {
                id: listId,
                name,
                items,
                fetching: false,
                error: null
            };
            return newState;
        case types.GET_SHOPPING_LIST_FAIL:
            listId = action.meta.previousAction.queryInfo.listId;

            newState = {
                ...state,
                fetchingLists: false,
                error: null
            };

            newState.shoppingLists[listId] = {
                ...state.shoppingLists[listId],
                fetching: false,
                error: action.error
            };
            return newState;
        default:
            return state;
    }
}