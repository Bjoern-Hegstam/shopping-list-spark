import * as types from '../actions/types';
import { hasId, hasRequestId, not } from '../util/util';

const initialState = {
  shoppingLists: [],
};

const itemTypeNameComparator = (item1, item2) => item1.itemType.name.localeCompare(item2.itemType.name, {
  numeric: true,
  sensitivity: 'base',
});

const replaceShoppingList = (state, shoppingList) => {
  shoppingList.items.sort(itemTypeNameComparator);

  return {
    ...state,
    shoppingLists: [
      ...state.shoppingLists.filter(not(hasId(shoppingList.id))),
      shoppingList,
    ],
  };
};

export default function (state = initialState, action) {
  switch (action.type) {
    case types.GET_SHOPPING_LISTS_SUCCESS: {
      const { shoppingLists } = action.payload.data;

      return {
        ...state,
        shoppingLists: shoppingLists.map(list => ({
          id: list.id,
          name: list.name,
        })),
      };
    }

    case types.GET_SHOPPING_LIST_SUCCESS: {
      const { data: shoppingList } = action.payload;

      return replaceShoppingList(state, shoppingList);
    }

    case types.ADD_SHOPPING_LIST_ITEM: {
      const {
        meta: { requestId, listId },
        payload: { request: { data: { itemTypeId, itemTypeName, quantity } } },
      } = action;

      const shoppingList = state.shoppingLists.find(hasId(listId));

      let itemType;
      let updatedItemTypes;
      if (itemTypeId) {
        updatedItemTypes = shoppingList.itemTypes;
        itemType = updatedItemTypes.find(hasId(itemTypeId));
      } else {
        itemType = {
          requestId,
          name: itemTypeName,
        };
        updatedItemTypes = [
          ...shoppingList.itemTypes,
          itemType,
        ];
      }

      const updatedItems = [
        ...shoppingList.items,
        {
          requestId,
          itemType,
          quantity,
          inCart: false,
        },
      ];

      return replaceShoppingList(
        state,
        {
          ...shoppingList,
          itemTypes: updatedItemTypes,
          items: updatedItems,
        },
      );
    }
    case types.ADD_SHOPPING_LIST_ITEM_SUCCESS: {
      const {
        meta: { previousAction: { meta: { requestId, listId } } },
        payload: { data: { id, itemType } },
      } = action;

      const shoppingList = state.shoppingLists.find(hasId(listId));

      const itemTypeToUpdate = shoppingList.itemTypes.find(hasRequestId(requestId));
      let updatedItemTypes;
      if (itemTypeToUpdate) {
        // Item type was created as part of the add item operation. Save the item type id and remove the request id.
        updatedItemTypes = [
          ...shoppingList.itemTypes.filter(not(hasRequestId(requestId))),
          {
            ...itemTypeToUpdate,
            id: itemType.id,
            requestId: undefined,
          },
        ];
      } else {
        updatedItemTypes = shoppingList.itemTypes;
      }

      const updatedItems = [
        ...shoppingList.items.filter(not(hasRequestId(requestId))),
        {
          ...shoppingList.items.find(hasRequestId(requestId)),
          id,
          requestId: undefined,
        },
      ];

      return replaceShoppingList(
        state,
        {
          ...shoppingList,
          itemTypes: updatedItemTypes,
          items: updatedItems,
        },
      );
    }
    case types.ADD_SHOPPING_LIST_ITEM_FAIL: {
      const {
        meta: { previousAction: { meta: { requestId, listId } } },
      } = action;

      const shoppingList = state.shoppingLists.find(hasId(listId));
      const itemTypes = shoppingList.itemTypes.filter(not(hasRequestId(requestId)));
      const items = shoppingList.items.filter(not(hasRequestId(requestId)));

      return replaceShoppingList(
        state,
        {
          ...shoppingList,
          itemTypes,
          items,
        },
      );
    }

    case types.UPDATE_SHOPPING_LIST_ITEM: {
      const {
        meta: { listId, itemId },
        payload: { request: { data: { quantity, inCart } } },
      } = action;

      const shoppingList = state.shoppingLists.find(hasId(listId));
      const item = shoppingList.items.find(hasId(itemId));

      const updatedItems = [
        ...shoppingList.items.filter(not(hasId(itemId))),
        {
          ...item,
          id: itemId,
          quantity,
          inCart,
          prevItem: item,
        },
      ];

      return replaceShoppingList(
        state,
        {
          ...shoppingList,
          items: updatedItems,
        },
      );
    }
    case types.UPDATE_SHOPPING_LIST_ITEM_SUCCESS: {
      const {
        meta: { previousAction: { meta: { listId, itemId } } },
      } = action;

      const shoppingList = state.shoppingLists.find(hasId(listId));
      const item = shoppingList.items.find(hasId(itemId));

      const updatedItems = [
        ...shoppingList.items.filter(not(hasId(itemId))),
        {
          ...item,
          prevItem: undefined,
        },
      ];

      return replaceShoppingList(
        state,
        {
          ...shoppingList,
          items: updatedItems,
        },
      );
    }
    case types.UPDATE_SHOPPING_LIST_ITEM_FAIL: {
      const {
        meta: { previousAction: { meta: { listId, itemId } } },
      } = action;

      const shoppingList = state.shoppingLists.find(hasId(listId));
      const item = shoppingList.items.find(hasId(itemId));

      const updatedItems = [
        ...shoppingList.items.filter(not(hasId(itemId))),
        {
          ...item.prevItem,
        },
      ];

      return replaceShoppingList(
        state,
        {
          ...shoppingList,
          items: updatedItems,
        },
      );
    }

    case types.DELETE_SHOPPING_LIST_ITEM_SUCCESS: {
      const {
        meta: { previousAction: { meta: { listId, itemId } } },
      } = action;

      const shoppingList = state.shoppingLists.find(hasId(listId));

      return replaceShoppingList(
        state,
        {
          ...shoppingList,
          items: shoppingList.items.filter(not(hasId(itemId))),
        },
      );
    }

    case types.DELETE_ITEM_TYPE_SUCCESS: {
      return {
        ...state,
      };
    }
    default:
      return state;
  }
}
