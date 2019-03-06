import * as types from './types';
import { SHOPPING_LIST_1_0 } from './MimeTypes';

export function addItemType({ token, name }) {
    return {
        type: types.ADD_ITEM_TYPE,
        payload: {
            request: {
                method: 'post',
                url: 'item-type',
                headers: {
                    authorization: `Bearer ${token}`,
                    accept: SHOPPING_LIST_1_0,
                },
                data: {
                    name,
                },
            },
        },
    };
}

export function deleteItemType({ token, id }) {
    return {
        type: types.DELETE_ITEM_TYPE,
        queryInfo: {
            itemTypeId: id,
        },
        payload: {
            request: {
                method: 'delete',
                url: `item-type/${id}`,
                headers: {
                    authorization: `Bearer ${token}`,
                    accept: SHOPPING_LIST_1_0,
                },
            },
        },
    };
}

export function getItemTypes(token) {
    return {
        type: types.GET_ITEM_TYPES,
        payload: {
            request: {
                method: 'get',
                url: 'item-type',
                headers: {
                    authorization: `Bearer ${token}`,
                    accept: SHOPPING_LIST_1_0,
                },
            },
        },
    };
}
