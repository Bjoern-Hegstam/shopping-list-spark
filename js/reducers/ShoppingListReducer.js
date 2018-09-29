import * as types from '../actions/types';

const initialState = {
    shoppingLists: {},

    fetchingShoppingLists: false,
    errorGetShoppingLists: null,

    addingShoppingList: false,
    errorAddShoppingList: null,

    updatingShoppingList: false,
    errorUpdateShoppingList: null,

    deletingShoppingList: false,
    errorDeleteShoppingList: null,

    addingShoppingListItem: false,
    errorAddShoppingListItem: null,

    updatingShoppingListItem: false,
    errorUpdateShoppingListItem: null,

    deletingShoppingListItem: false,
    errorDeleteShoppingListItem: null,

    emptyingCart: false,
    errorEmptyCart: null,
};

function sortByItemTypeName(a, b) {
    if (a.itemType.name < b.itemType.name) return -1;
    if (a.itemType.name > b.itemType.name) return 1;
    return 0;
}

export default function (state = initialState, action) {
    switch (action.type) {
    case types.GET_SHOPPING_LISTS: {
        return {
            ...state,
            fetchingShoppingLists: true,
            errorGetShoppingLists: null,
        };
    }
    case types.GET_SHOPPING_LISTS_SUCCESS: {
        const { shoppingLists } = action.payload.data;
        const newState = {
            ...state,
            shoppingLists: {},
            fetchingShoppingLists: false,
            errorGetShoppingLists: null,
        };

        shoppingLists.forEach((list) => {
            newState.shoppingLists[list.id] = {
                ...list,
                items: [],
                meta: {
                    loaded: false,
                    fetching: false,
                    error: null,
                },
            };
        });
        return newState;
    }
    case types.GET_SHOPPING_LISTS_FAIL: {
        return {
            ...state,
            fetchingShoppingLists: false,
            errorGetShoppingLists: action.error,
        };
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
                        fetching: true,
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
                        fetching: false,
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
                        fetching: false,
                        error: action.error,
                    },
                },
            },
        };
    }

    case types.ADD_SHOPPING_LIST: {
        return {
            ...state,
            addingShoppingList: true,
            errorAddShoppingList: null,
        };
    }
    case types.ADD_SHOPPING_LIST_SUCCESS: {
        return {
            ...state,
            addingShoppingList: false,
        };
    }
    case types.ADD_SHOPPING_LIST_FAIL: {
        return {
            ...state,
            addingShoppingList: false,
            errorAddShoppingList: action.error,
        };
    }

    case types.UPDATE_SHOPPING_LIST: {
        return {
            ...state,
            updatingShoppingList: true,
            errorUpdateShoppingList: null,
        };
    }
    case types.UPDATE_SHOPPING_LIST_SUCCESS: {
        return {
            ...state,
            updatingShoppingList: false,
        };
    }
    case types.UPDATE_SHOPPING_LIST_FAIL: {
        return {
            ...state,
            updatingShoppingList: false,
            errorUpdateShoppingList: action.error,
        };
    }

    case types.DELETE_SHOPPING_LIST: {
        return {
            ...state,
            deletingShoppingList: true,
            errorDeleteShoppingList: null,
        };
    }
    case types.DELETE_SHOPPING_LIST_SUCCESS: {
        return {
            ...state,
            deletingShoppingList: false,
        };
    }
    case types.DELETE_SHOPPING_LIST_FAIL: {
        return {
            ...state,
            deletingShoppingList: false,
            errorDeleteShoppingList: action.error,
        };
    }

    case types.ADD_SHOPPING_LIST_ITEM: {
        return {
            ...state,
            addingShoppingListItem: true,
            errorAddShoppingListItem: null,
        };
    }
    case types.ADD_SHOPPING_LIST_ITEM_SUCCESS: {
        return {
            ...state,
            addingShoppingListItem: false,
        };
    }
    case types.ADD_SHOPPING_LIST_ITEM_FAIL: {
        return {
            ...state,
            addingShoppingListItem: false,
            errorAddShoppingListItem: action.error,
        };
    }

    case types.UPDATE_SHOPPING_LIST_ITEM: {
        return {
            ...state,
            updatingShoppingListItem: true,
            errorUpdateShoppingListItem: null,
        };
    }
    case types.UPDATE_SHOPPING_LIST_ITEM_SUCCESS: {
        return {
            ...state,
            updatingShoppingListItem: false,
        };
    }
    case types.UPDATE_SHOPPING_LIST_ITEM_FAIL: {
        return {
            ...state,
            updatingShoppingListItem: false,
            errorUpdateShoppingListItem: action.error,
        };
    }

    case types.DELETE_SHOPPING_LIST_ITEM: {
        return {
            ...state,
            deletingShoppingListItem: true,
            errorDeleteShoppingListItem: null,
        };
    }
    case types.DELETE_SHOPPING_LIST_ITEM_SUCCESS: {
        return {
            ...state,
            deletingShoppingListItem: false,
        };
    }
    case types.DELETE_SHOPPING_LIST_ITEM_FAIL: {
        return {
            ...state,
            deletingShoppingListItem: false,
            errorDeleteShoppingListItem: action.error,
        };
    }

    case types.EMPTY_CART: {
        return {
            ...state,
            emptyingCart: true,
            errorEmptyCart: null,
        };
    }
    case types.EMPTY_CART_SUCCESS: {
        return {
            ...state,
            emptyingCart: false,
        };
    }
    case types.EMPTY_CART_FAIL: {
        return {
            ...state,
            emptyingCart: false,
            errorEmptyCart: action.error,
        };
    }

    default:
        return state;
    }
}
