import * as types from './types';

export function addItemType({token, name}) {
    return {
        type: types.GET_ITEM_TYPES,
        payload: {
            request: {
                method: 'post',
                url: 'item-type',
                headers: {
                    authorization: `Bearer ${token}`
                },
                data: {
                    name
                }
            }
        }
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
                    authorization: `Bearer ${token}`
                }
            }
        }
    };
}