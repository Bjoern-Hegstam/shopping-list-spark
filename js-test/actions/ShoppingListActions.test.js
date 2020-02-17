import * as types from '../../js/actions/types';
import {
  addShoppingList,
  addShoppingListItem,
  deleteItemType,
  deleteShoppingList,
  deleteShoppingListItem,
  deleteShoppingListItemRequestSelectorKey,
  emptyCart,
  getShoppingList,
  getShoppingLists,
  updateShoppingList,
  updateShoppingListItem,
  updateShoppingListItemRequestSelectorKey,
} from '../../js/actions/ShoppingListActions';
import { SHOPPING_LIST_1_0 } from '../../js/actions/MimeTypes';

const token = 'test-token';
const listId = 'test-shopping-list-id';
const itemTypeId = 'test-item-type-itemTypeId';
const itemId = 'test-shopping-list-item-id';

const expectedAction = ({ type, method, url, meta = undefined, data = undefined }) => ({
  type,
  meta,
  payload: {
    request: {
      method,
      url,
      headers: {
        authorization: `Bearer ${token}`,
        accept: SHOPPING_LIST_1_0,
      },
      data,
    },
  },
});

it('getShoppingList', () => {
  // when
  const action = getShoppingLists(token);

  // then
  expect(action).toEqual(expectedAction({
    type: types.GET_SHOPPING_LISTS,
    method: 'get',
    url: 'shopping-list',
  }));
});

it('getShoppingLists', () => {
  // when
  const action = getShoppingList({ token, id: listId });

  // then
  expect(action).toEqual(expectedAction({
    type: types.GET_SHOPPING_LIST,
    method: 'get',
    url: `shopping-list/${listId}`,
    meta: { listId },
  }));
});

it('addShoppingList', () => {
  // when
  const action = addShoppingList({ token, name: 'test-name' });

  // then
  expect(action).toEqual(expectedAction({
    type: types.ADD_SHOPPING_LIST,
    method: 'post',
    url: 'shopping-list',
    data: { name: 'test-name' },
  }));
});

it('updateShoppingList', () => {
  // when
  const action = updateShoppingList({ token, listId, name: 'test-name' });

  // then
  expect(action).toEqual(expectedAction({
    type: types.UPDATE_SHOPPING_LIST,
    method: 'put',
    url: `shopping-list/${listId}`,
    data: { name: 'test-name' },
  }));
});

it('deleteShoppingList', () => {
  // when
  const action = deleteShoppingList({ token, listId });

  // then
  expect(action).toEqual(expectedAction({
    type: types.DELETE_SHOPPING_LIST,
    method: 'delete',
    url: `shopping-list/${listId}`,
  }));
});

it('deleteItemType', () => {
  // given
  const action = deleteItemType({ token, listId, id: itemTypeId });

  // then
  expect(action).toEqual(expectedAction({
    type: types.DELETE_ITEM_TYPE,
    method: 'delete',
    url: `shopping-list/${listId}/item-type/${itemTypeId}`,
    meta: { itemTypeId },
  }));
});

it('addShoppingListItem - by itemTypeId', () => {
  // when
  const action = addShoppingListItem({ token, listId, itemTypeId, quantity: 2 });

  // then
  expect(action).toEqual(expectedAction({
    type: types.ADD_SHOPPING_LIST_ITEM,
    method: 'post',
    url: `shopping-list/${listId}/item`,
    meta: {
      requestId: expect.anything(),
      listId,
    },
    data: {
      itemTypeId,
      quantity: 2,
    },
  }));
});

it('addShoppingListItem - by itemTypeName', () => {
  // when
  const action = addShoppingListItem({ token, listId, itemTypeName: 'test-item-type-name', quantity: 2 });

  // then
  expect(action).toEqual(expectedAction({
    type: types.ADD_SHOPPING_LIST_ITEM,
    method: 'post',
    url: `shopping-list/${listId}/item`,
    meta: {
      requestId: expect.anything(),
      listId,
    },
    data: {
      itemTypeName: 'test-item-type-name',
      quantity: 2,
    },
  }));
});

it('updateShoppingListItem', () => {
  // when
  const action = updateShoppingListItem({ token, listId, itemId, quantity: 2, inCart: true });

  // then
  expect(action).toEqual(expectedAction({
    type: types.UPDATE_SHOPPING_LIST_ITEM,
    method: 'put',
    url: `shopping-list/${listId}/item/${itemId}`,
    meta: {
      requestId: updateShoppingListItemRequestSelectorKey(listId, itemId),
      listId,
      itemId,
    },
    data: {
      quantity: 2,
      inCart: true,
    },
  }));
});

it('deleteShoppingListItem', () => {
  // when
  const action = deleteShoppingListItem({ token, listId, itemId });

  // then
  expect(action).toEqual(expectedAction({
    type: types.DELETE_SHOPPING_LIST_ITEM,
    method: 'delete',
    url: `shopping-list/${listId}/item/${itemId}`,
    meta: {
      requestId: deleteShoppingListItemRequestSelectorKey(listId, itemId),
      listId,
      itemId,
    },
  }));
});

it('emptyCart', () => {
  // when
  const action = emptyCart({ token, listId });

  // then
  expect(action).toEqual(expectedAction({
    type: types.EMPTY_CART,
    method: 'delete',
    url: `shopping-list/${listId}/cart`,
  }));
});
