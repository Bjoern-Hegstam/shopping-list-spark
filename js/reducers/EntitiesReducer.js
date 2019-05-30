import { normalize } from 'normalizr';
import * as uuid from 'uuid';
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

    case types.ADD_SHOPPING_LIST_ITEM: {
      const { listId } = action.meta;
      const { itemTypeId, quantity } = action.payload.request.data;
      const tempItemId = uuid.v4();

      return {
        ...state,
        shoppingLists: {
          ...state.shoppingLists,
          [listId]: {
            ...state.shoppingLists[listId],
            items: [
              ...state.shoppingLists[listId].items,
              tempItemId,
            ],
          },
        },
        items: {
          ...state.items,
          [tempItemId]: {
            id: tempItemId,
            itemType: itemTypeId,
            quantity,
            inCart: false,
          },
        },
      };
    }
    case types.ADD_SHOPPING_LIST_ITEM_SUCCESS: {
      const { listId } = action.meta.previousAction.meta;
      const { itemTypeId } = action.meta.previousAction.payload.request.data;
      const { id: itemId } = action.payload.data;

      const shoppingList = state.shoppingLists[listId];
      const tempItemId = shoppingList.items.find(id => state.items[id].itemType === itemTypeId);

      return {
        ...state,
        shoppingLists: {
          ...state.shoppingLists,
          [listId]: {
            ...shoppingList,
            items: [
              itemId,
              ...shoppingList.items.filter(id => id !== tempItemId),
            ],
          },
        },
        items: {
          ...state.items,
          [itemId]: {
            ...state.items[tempItemId],
            id: itemId,
          },
        },
      };
    }
    case types.ADD_SHOPPING_LIST_ITEM_FAIL: {
      const { listId } = action.meta.previousAction.meta;
      const { itemTypeId } = action.meta.previousAction.payload.request.data;

      const shoppingList = state.shoppingLists[listId];
      const tempItemId = shoppingList.items.find(id => state.items[id].itemType === itemTypeId);

      return {
        ...state,
        shoppingLists: {
          ...state.shoppingLists,
          [listId]: {
            ...shoppingList,
            items: shoppingList.items.filter(id => id !== tempItemId),
          },
        },
      };
    }

    case types.UPDATE_SHOPPING_LIST_ITEM: {
      const { itemId } = action.meta;
      const { quantity, inCart } = action.payload.request.data;

      const prevItem = state.items[itemId];
      return {
        ...state,
        items: {
          ...state.items,
          [itemId]: {
            ...prevItem,
            quantity,
            inCart,
            prevItem,
          },
        },
      };
    }
    case types.UPDATE_SHOPPING_LIST_ITEM_SUCCESS: {
      const { itemId } = action.meta.previousAction.meta;
      return {
        ...state,
        items: {
          ...state.items,
          [itemId]: {
            ...state.items[itemId],
            prevItem: undefined,
          },
        },
      };
    }
    case types.UPDATE_SHOPPING_LIST_ITEM_FAIL: {
      const { itemId } = action.meta.previousAction.meta;
      return {
        ...state,
        items: {
          ...state.items,
          [itemId]: state.items[itemId].prevItem,
        },
      };
    }

    case types.DELETE_SHOPPING_LIST_ITEM_SUCCESS: {
      const { listId, itemId } = action.meta.previousAction.meta;
      const shoppingList = state.shoppingLists[listId];
      return {
        ...state,
        shoppingLists: {
          ...state.shoppingLists,
          [listId]: {
            ...shoppingList,
            items: shoppingList.items.filter(id => id !== itemId),
          },
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
      const { itemTypeId } = action.meta.previousAction.meta;

      return {
        ...state,
        itemTypes: {
          ...Object
            .values(state.itemTypes)
            .filter(itemType => itemType.id !== itemTypeId)
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
