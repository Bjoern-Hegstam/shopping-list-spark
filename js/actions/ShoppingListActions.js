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