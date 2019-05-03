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
    queryInfo: {
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

export function addShoppingListItem({
  token, listId, itemTypeId, quantity,
}) {
  return {
    type: types.ADD_SHOPPING_LIST_ITEM,
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
          quantity,
        },
      },
    },
  };
}

export function updateShoppingListItem({
  token, listId, itemId, quantity, inCart,
}) {
  return {
    type: types.UPDATE_SHOPPING_LIST_ITEM,
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

export function deleteShoppingListItem({ token, listId, itemId }) {
  return {
    type: types.DELETE_SHOPPING_LIST_ITEM,
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
    type: types.DELETE_SHOPPING_LIST_ITEM,
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
