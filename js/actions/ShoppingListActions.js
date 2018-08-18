import * as types from './types';

export function getShoppingLists(token) {
    return {
        type: types.GET_SHOPPING_LISTS,
        payload: {
            request: {
                method: 'get',
                url: 'shopping-list',
                headers: {
                    authorization: `Bearer ${token}`
                }
            }
        }
    };
}

export function getShoppingList({token, id}) {
    return {
        type: types.GET_SHOPPING_LIST,
        queryInfo: {
            listId: id
        },
        payload: {
            request: {
                method: 'get',
                url: `shopping-list/${id}`,
                headers: {
                    authorization: `Bearer ${token}`
                }
            }
        }
    }
}

export function addShoppingList({token, name}) {
    return {
        type: types.ADD_SHOPPING_LIST,
        payload: {
            request: {
                method: 'post',
                url: `shopping-list`,
                headers: {
                    authorization: `Bearer ${token}`
                },
                data: {
                    name
                }
            }
        }
    }
}

export function addShoppingListItem({token, listId, itemTypeId, quantity}) {
    return {
        type: types.ADD_SHOPPING_LIST_ITEM,
        payload: {
            request: {
                method: 'post',
                url: `shopping-list/${listId}`,
                headers: {
                    authorization: `Bearer ${token}`
                },
                data: {
                    itemTypeId,
                    quantity
                }
            }
        }
    }
}

export function updateShoppingListItem({token, listId, itemId, quantity, inCart}) {
    return {
        type: types.UPDATE_SHOPPING_LIST_ITEM,
        payload: {
            request: {
                method: 'put',
                url: `shopping-list/${listId}/item/${itemId}`,
                headers: {
                    authorization: `Bearer ${token}`
                },
                data: {
                    quantity,
                    inCart
                }
            }
        }
    }
}

export function deleteShoppingListItem({token, listId, itemId}) {
    return {
        type: types.DELETE_SHOPPING_LIST_ITEM,
        payload: {
            request: {
                method: 'delete',
                url: `shopping-list/${listId}/item/${itemId}`,
                headers: {
                    authorization: `Bearer ${token}`
                }
            }
        }
    }
}

export function emptyCart({token, listId}) {
    return {
        type: types.DELETE_SHOPPING_LIST_ITEM,
        payload: {
            request: {
                method: 'delete',
                url: `shopping-list/${listId}/cart`,
                headers: {
                    authorization: `Bearer ${token}`
                }
            }
        }
    }
}
