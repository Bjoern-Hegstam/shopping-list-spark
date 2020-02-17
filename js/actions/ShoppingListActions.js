import * as uuid from 'uuid';
import * as types from './types';
import { SHOPPING_LIST_1_0 } from './MimeTypes';

export function getShoppingLists(token) {
  return {
    type: types.GET_SHOPPING_LISTS,
    payload: {
      request: {
        method: 'get',
        url: 'shopping-list',
        headers: {
          authorization: `Bearer ${token}`,
          accept: SHOPPING_LIST_1_0,
        },
      },
    },
  };
}

export function getShoppingList({ token, id }) {
  return {
    type: types.GET_SHOPPING_LIST,
    meta: {
      listId: id,
    },
    payload: {
      request: {
        method: 'get',
        url: `shopping-list/${id}`,
        headers: {
          authorization: `Bearer ${token}`,
          accept: SHOPPING_LIST_1_0,
        },
      },
    },
  };
}

export function addShoppingList({ token, name }) {
  return {
    type: types.ADD_SHOPPING_LIST,
    payload: {
      request: {
        method: 'post',
        url: 'shopping-list',
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

export function updateShoppingList({ token, listId, name }) {
  return {
    type: types.UPDATE_SHOPPING_LIST,
    payload: {
      request: {
        method: 'put',
        url: `shopping-list/${listId}`,
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

export function deleteShoppingList({ token, listId }) {
  return {
    type: types.DELETE_SHOPPING_LIST,
    payload: {
      request: {
        method: 'delete',
        url: `shopping-list/${listId}`,
        headers: {
          authorization: `Bearer ${token}`,
          accept: SHOPPING_LIST_1_0,
        },
      },
    },
  };
}

export function deleteItemType({ token, listId, id }) {
  return {
    type: types.DELETE_ITEM_TYPE,
    meta: {
      itemTypeId: id,
    },
    payload: {
      request: {
        method: 'delete',
        url: `shopping-list/${listId}/item-type/${id}`,
        headers: {
          authorization: `Bearer ${token}`,
          accept: SHOPPING_LIST_1_0,
        },
      },
    },
  };
}

export function addShoppingListItem({ token, listId, itemTypeId, itemTypeName, quantity }) {
  return {
    type: types.ADD_SHOPPING_LIST_ITEM,
    meta: {
      requestId: uuid.v4(),
      listId,
    },
    payload: {
      request: {
        method: 'post',
        url: `shopping-list/${listId}/item`,
        headers: {
          authorization: `Bearer ${token}`,
          accept: SHOPPING_LIST_1_0,
        },
        data: {
          itemTypeId,
          itemTypeName,
          quantity,
        },
      },
    },
  };
}

export const updateShoppingListItemRequestSelectorKey = (listId, itemId) => `${listId}:${itemId}`;

export function updateShoppingListItem({ token, listId, itemId, quantity, inCart }) {
  return {
    type: types.UPDATE_SHOPPING_LIST_ITEM,
    meta: {
      requestId: updateShoppingListItemRequestSelectorKey(listId, itemId),
      listId,
      itemId,
    },
    payload: {
      request: {
        method: 'put',
        url: `shopping-list/${listId}/item/${itemId}`,
        headers: {
          authorization: `Bearer ${token}`,
          accept: SHOPPING_LIST_1_0,
        },
        data: {
          quantity,
          inCart,
        },
      },
    },
  };
}

export const deleteShoppingListItemRequestSelectorKey = (listId, itemId) => `${listId}:${itemId}`;

export function deleteShoppingListItem({ token, listId, itemId }) {
  return {
    type: types.DELETE_SHOPPING_LIST_ITEM,
    meta: {
      requestId: deleteShoppingListItemRequestSelectorKey(listId, itemId),
      listId,
      itemId,
    },
    payload: {
      request: {
        method: 'delete',
        url: `shopping-list/${listId}/item/${itemId}`,
        headers: {
          authorization: `Bearer ${token}`,
          accept: SHOPPING_LIST_1_0,
        },
      },
    },
  };
}

export function emptyCart({ token, listId }) {
  return {
    type: types.EMPTY_CART,
    payload: {
      request: {
        method: 'delete',
        url: `shopping-list/${listId}/cart`,
        headers: {
          authorization: `Bearer ${token}`,
          accept: SHOPPING_LIST_1_0,
        },
      },
    },
  };
}
