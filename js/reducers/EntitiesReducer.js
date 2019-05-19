import { normalize } from 'normalizr';
import * as types from '../actions/types';
import { itemTypeSchema, shoppingListSchema } from '../schemas';

export default function (state = {}, action) {
  switch (action.type) {
    case types.GET_SHOPPING_LISTS_SUCCESS: {
      const { data } = action.payload;
      const normalizedData = normalize(data.shoppingLists, [shoppingListSchema]);
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
    case types.ADD_ITEM_TYPE_SUCCESS: {
      const { data } = action.payload;
      const normalizedData = normalize(data, itemTypeSchema);

      return {
        ...state,
        itemTypes: {
          ...state.itemTypes,
          ...normalizedData.entities.itemTypes,
        },
      };
    }
    case types.GET_ITEM_TYPES_SUCCESS: {
      const { data } = action.payload;
      const normalizedData = normalize(data.itemTypes, [itemTypeSchema]);

      return {
        ...state,
        itemTypes: {
          ...normalizedData.entities.itemTypes,
        },
      };
    }
    case types.DELETE_ITEM_TYPE_SUCCESS: {
      const { itemTypeId } = action.meta.previousAction.queryInfo;

      return {
        ...state,
        itemTypes: {
          ...Object
            .keys(state.itemTypes)
            .map(id => (id !== itemTypeId
              ? state.itemTypes[id]
              : {
                ...state.itemTypes[id],
                deleted: true,
              }))
            .reduce((obj, itemType) => ({
              ...obj,
              [itemType.id]: itemType,
            }), {}),
        },
      };
    }
    default:
      return state;
  }
}
