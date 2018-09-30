import * as types from '../actions/types';

const initialState = {
    shoppingLists: {},
};

function sortByItemTypeName(a, b) {
    if (a.itemType.name < b.itemType.name) return -1;
    if (a.itemType.name > b.itemType.name) return 1;
    return 0;
}

export default function (state = initialState, action) {
    switch (action.type) {
    case types.GET_SHOPPING_LISTS_SUCCESS: {
        const { shoppingLists } = action.payload.data;
        const newState = {
            ...state,
            shoppingLists: {},
        };

        shoppingLists.forEach((list) => {
            newState.shoppingLists[list.id] = {
                ...list,
                items: [],
                meta: {
                    loaded: false,
                    error: null,
                },
            };
        });
        return newState;
    }

    case types.GET_SHOPPING_LIST: {
        const { listId } = action.queryInfo;

        const shoppingList = state.shoppingLists[listId];
        return {
            ...state,
            shoppingLists: {
                ...state.shoppingLists,
                [listId]: {
                    id: listId,
                    name: '',
                    items: [],
                    ...shoppingList,
                    meta: {
                        loaded: (shoppingList ? shoppingList.meta.loaded : false),
                        error: null,
                    },
                },
            },
        };
    }
    case types.GET_SHOPPING_LIST_SUCCESS: {
        const listId = action.payload.data.id;
        const { name, items } = action.payload.data;

        items.sort(sortByItemTypeName);
        return {
            ...state,
            shoppingLists: {
                ...state.shoppingLists,
                [listId]: {
                    id: listId,
                    name,
                    items,
                    meta: {
                        loaded: true,
                        error: null,
                    },
                },
            },
        };
    }
    case types.GET_SHOPPING_LIST_FAIL: {
        const { listId } = action.meta.previousAction.queryInfo;

        return {
            ...state,
            shoppingLists: {
                ...state.shoppingLists,
                [listId]: {
                    ...state.shoppingLists[listId],
                    meta: {
                        loaded: false,
                        error: action.error,
                    },
                },
            },
        };
    }

    default:
        return state;
    }
}
