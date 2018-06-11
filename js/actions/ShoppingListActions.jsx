import * as types from './types';

export function getShoppingLists() {
    return {
        type: types.GET_SHOPPING_LISTS,
        payload: {
            request: {
                method: 'get',
                url: 'shopping-list'
            }
        }
    };
}

export function getShoppingList(id) {
    return {
        type: types.GET_SHOPPING_LIST,
        payload: {
            request: {
                method: 'get',
                url: `shopping-list/${id}`
            }
        }
    }
}