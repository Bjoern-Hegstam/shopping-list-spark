import * as types from './types';

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