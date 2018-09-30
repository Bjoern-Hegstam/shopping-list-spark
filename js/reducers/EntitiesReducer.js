import { normalize } from 'normalizr';
import * as types from '../actions/types';
import { shoppingListSchema, shoppingListsSchema } from '../schemas';

export default function (state = {}, action) {
    switch (action.type) {
        case types.GET_SHOPPING_LISTS_SUCCESS: {
            const { data } = action.payload;
            const normalizedData = normalize(data.shoppingLists, shoppingListsSchema);
            const { shoppingLists } = normalizedData.entities;

            return {
                ...state,
                shoppingLists,
            };
        }
        case types.GET_SHOPPING_LIST_SUCCESS: {
            const { data } = action.payload;
            const normalizedData = normalize(data, shoppingListSchema);

            return {
                ...state,
                shoppingLists: {
                    ...state.shoppingLists,
                    ...normalizedData.entities.shoppingLists,
                },
                items: {
                    ...state.items,
                    ...normalizedData.entities.items,
                },
                itemTypes: {
                    ...state.itemTypes,
                    ...normalizedData.entities.itemTypes,
                },
            };
        }
        default:
            return state;
    }
}
