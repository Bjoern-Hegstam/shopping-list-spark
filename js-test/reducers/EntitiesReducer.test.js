import * as types from '../../js/actions/types';
import reducer from '../../js/reducers/EntitiesReducer';
import { deepCopy } from '../util';

const shoppingList1Id = 'shopping-list-1-id';
const shoppingList2Id = 'shopping-list-2-id';

const itemType1Id = 'item-type-1-id';
const itemType2Id = 'item-type-2-id';

const list1Item1Id = 'item-1-id';

const initialState = {
  shoppingLists: {
    [shoppingList1Id]: {
      id: shoppingList1Id,
      name: 'Foo',
      items: [list1Item1Id],
    },
    [shoppingList2Id]: {
      id: shoppingList2Id,
      name: 'Baz',
    },
  },
  items: {
    [list1Item1Id]: {
      id: list1Item1Id,
      itemType: itemType1Id,
      quantity: 1,
      inCart: false,
    },
  },
  itemTypes: {
    [itemType1Id]: {
      id: itemType1Id,
      name: 'itemType1',
    },
    [itemType2Id]: {
      id: itemType2Id,
      name: 'itemType2',
    },
  },
};

it('handles GET_SHOPPING_LISTS_SUCCESS where lists have been added, edited and deleted', () => {
  // given
  const newShoppingListId = 'new-shopping-list-id';
  const action = {
    type: types.GET_SHOPPING_LISTS_SUCCESS,
    payload: {
      data: {
        shoppingLists: [
          {
            id: shoppingList1Id,
            name: 'FooNew',
          },
          {
            id: newShoppingListId,
            name: 'Bar',
          },
        ],
      },
    },
  };

  // when
  const newState = reducer(initialState, action);

  // then
  expect(newState).toEqual({
    ...initialState,
    shoppingLists: {
      [shoppingList1Id]: {
        id: shoppingList1Id,
        name: 'FooNew',
      },
      [newShoppingListId]: {
        id: newShoppingListId,
        name: 'Bar',
      },
    },
    itemTypes: {
      [itemType1Id]: {
        id: itemType1Id,
        name: 'itemType1',
      },
      [itemType2Id]: {
        id: itemType2Id,
        name: 'itemType2',
      },
    },
  });
});

it('normalizes shopping list on GET_SHOPPING_LIST_SUCCESS', () => {
  // given
  const item1Id = 'item-1-id';
  const item2Id = 'item-2-id';

  const itemTypeBananas = 'item-type-bananas-id';
  const itemTypeApples = 'item-type-apples-id';

  const action = {
    type: types.GET_SHOPPING_LIST_SUCCESS,
    payload: {
      data: {
        id: shoppingList1Id,
        name: 'Foo',
        items: [
          {
            id: item1Id,
            itemType: {
              id: itemTypeBananas,
              name: 'Bananas',
            },
            quantity: 1,
            inCart: true,
          },
          {
            id: item2Id,
            itemType: {
              id: itemTypeApples,
              name: 'Apples',
            },
            quantity: 5,
            inCart: false,
          },
        ],
      },
    },
  };

  // when
  const newState = reducer(initialState, action);

  // then
  expect(newState).toEqual({
    shoppingLists: {
      [shoppingList1Id]: {
        id: shoppingList1Id,
        name: 'Foo',
        items: [item1Id, item2Id],
      },
      [shoppingList2Id]: {
        id: shoppingList2Id,
        name: 'Baz',
      },
    },
    items: {
      ...initialState.items,
      [item1Id]: {
        id: item1Id,
        itemType: itemTypeBananas,
        quantity: 1,
        inCart: true,
      },
      [item2Id]: {
        id: item2Id,
        itemType: itemTypeApples,
        quantity: 5,
        inCart: false,
      },
    },
    itemTypes: {
      [itemTypeBananas]: {
        id: itemTypeBananas,
        name: 'Bananas',
      },
      [itemTypeApples]: {
        id: itemTypeApples,
        name: 'Apples',
      },
      [itemType1Id]: {
        id: itemType1Id,
        name: 'itemType1',
      },
      [itemType2Id]: {
        id: itemType2Id,
        name: 'itemType2',
      },
    },
  });
});

describe('ADD_SHOPPING_LIST_ITEM', () => {
  it('adds item to shopping list, sorted by item type name', () => {
    // given
    const action = {
      type: types.ADD_SHOPPING_LIST_ITEM,
      meta: { listId: shoppingList1Id },
      payload: {
        request: {
          data: {
            itemTypeId: itemType1Id,
            quantity: 3,
          },
        },
      },
    };

    const existingItemId = 'existing-item-id';

    const state = deepCopy(initialState);
    state.items[existingItemId] = {
      id: existingItemId,
      itemType: itemType2Id,
      quantity: 1,
      inCart: false,
    };
    state.shoppingLists[shoppingList1Id].items = [existingItemId];

    // when
    const newState = reducer(state, action);

    // then
    const listItems = newState.shoppingLists[shoppingList1Id].items;
    expect(listItems).toHaveLength(2);
    expect(listItems[1]).toEqual(existingItemId);
    const newItemTempId = listItems[0];
    expect(newState.items[newItemTempId]).toEqual({
      id: newItemTempId,
      itemType: itemType1Id,
      quantity: 3,
      inCart: false,
    });
  });

  it('success - replace temp item id', () => {
    // given
    const tempItemId = 'temp-item-id';
    const persistedItemId = 'persisted-item-id';
    const item = {
      id: tempItemId,
      itemType: itemType2Id,
      quantity: 3,
      inCart: false,
    };

    const state = deepCopy(initialState);
    state.shoppingLists[shoppingList1Id].items = [list1Item1Id, tempItemId];
    state.items[tempItemId] = item;

    const action = {
      type: types.ADD_SHOPPING_LIST_ITEM_SUCCESS,
      meta: {
        previousAction: {
          meta: { listId: shoppingList1Id },
          payload: { request: { data: { itemTypeId: item.itemType } } },
        },
      },
      payload: {
        data: {
          id: persistedItemId,
        },
      },
    };

    // when
    const newState = reducer(state, action);

    // then
    expect(newState.shoppingLists[shoppingList1Id].items).toEqual([persistedItemId, list1Item1Id]);
    expect(newState.items[persistedItemId]).toEqual({
      ...item,
      id: persistedItemId,
    });
  });

  it('fail', () => {
    // given
    const tempItemId = 'temp-item-id';
    const item = {
      id: tempItemId,
      itemType: itemType2Id,
      quantity: 3,
      inCart: false,
    };


    const state = deepCopy(initialState);
    state.shoppingLists[shoppingList1Id].items = [list1Item1Id, tempItemId];
    state.items[tempItemId] = item;

    const action = {
      type: types.ADD_SHOPPING_LIST_ITEM_FAIL,
      meta: {
        previousAction: {
          meta: { listId: shoppingList1Id },
          payload: { request: { data: { itemTypeId: item.itemType } } },
        },
      },
    };

    // when
    const newState = reducer(state, action);

    // then
    expect(newState.shoppingLists[shoppingList1Id].items).toEqual([list1Item1Id]);
  });
});

describe('UPDATE_SHOPPING_LIST_ITEM', () => {
  it('invoked', () => {
    // given
    const item = initialState.items[list1Item1Id];
    const action = {
      type: types.UPDATE_SHOPPING_LIST_ITEM,
      meta: { itemId: item.id },
      payload: {
        request: {
          data: {
            quantity: item.quantity + 3,
            inCart: !item.inCart,
          },
        },
      },
    };

    // when
    const newState = reducer(initialState, action);

    // then
    expect(newState).toEqual({
      ...initialState,
      items: {
        [item.id]: {
          id: item.id,
          itemType: item.itemType,
          quantity: item.quantity + 3,
          inCart: !item.inCart,
          prevItem: item,
        },
      },
    });
  });

  it('success - clears previous item', () => {
    // given
    const item = initialState.items[list1Item1Id];
    const prevItem = {
      ...item,
      inCart: !item.inCart,
    };

    const state = deepCopy(initialState);
    state.items[item.id].prevItem = prevItem;

    const action = {
      type: types.UPDATE_SHOPPING_LIST_ITEM_SUCCESS,
      meta: { previousAction: { meta: { itemId: item.id } } },
    };

    // when
    const newState = reducer(state, action);

    // then
    expect(newState).toEqual(initialState);
  });

  it('fail', () => {
    // given
    const item = initialState.items[list1Item1Id];
    const prevItem = {
      ...item,
      inCart: !item.inCart,
    };

    const state = deepCopy(initialState);
    state.items[item.id].prevItem = prevItem;

    const action = {
      type: types.UPDATE_SHOPPING_LIST_ITEM_FAIL,
      meta: { previousAction: { meta: { itemId: item.id } } },
    };

    // when
    const newState = reducer(state, action);

    // then
    expect(newState).toEqual({
      ...initialState,
      items: {
        [item.id]: {
          ...prevItem,
        },
      },
    });
  });
});

describe('DELETE_SHOPPING_LIST_ITEM', () => {
  it('success', () => {
    // given
    const action = {
      type: types.DELETE_SHOPPING_LIST_ITEM_SUCCESS,
      meta: {
        previousAction: {
          meta: {
            listId: shoppingList1Id,
            itemId: list1Item1Id,
          },
        },
      },
    };

    // when
    const newState = reducer(initialState, action);

    // then
    expect(newState.shoppingLists[shoppingList1Id].items).toEqual([]);
  });
});

it('handles ADD_ITEM_TYPE_SUCCESS', () => {
  // given
  const newItemType = {
    id: 'new-item-type-id',
    name: 'Apples',
  };
  const action = {
    type: types.ADD_ITEM_TYPE_SUCCESS,
    payload: {
      data: newItemType,
    },
  };

  // when
  const newState = reducer(initialState, action);

  // then
  expect(newState).toEqual({
    ...initialState,
    itemTypes: {
      ...initialState.itemTypes,
      [newItemType.id]: newItemType,
    },
  });
});

it('handles GET_ITEM_TYPES_SUCCESS', () => {
  // given
  const newItemTypeId = 'new-item-type-id';
  const action = {
    type: types.GET_ITEM_TYPES_SUCCESS,
    payload: {
      data: {
        itemTypes: [
          {
            id: itemType1Id,
            name: 'Bananas',
          },
          {
            id: newItemTypeId,
            name: 'Apples',
          },
        ],
      },
    },
  };

  // when
  const newState = reducer(initialState, action);

  // then
  expect(newState).toEqual({
    ...initialState,
    itemTypes: {
      [itemType1Id]: {
        id: itemType1Id,
        name: 'Bananas',
      },
      [newItemTypeId]: {
        id: newItemTypeId,
        name: 'Apples',
      },
    },
  });
});

it('handles DELETE_ITEM_TYPE_SUCCESS', () => {
  // given
  const action = {
    type: types.DELETE_ITEM_TYPE_SUCCESS,
    meta: {
      previousAction: {
        meta: {
          itemTypeId: itemType2Id,
        },
      },
    },
  };

  // when
  const newState = reducer(initialState, action);

  // then
  expect(newState).toEqual({
    ...initialState,
    itemTypes: {
      [itemType1Id]: {
        id: itemType1Id,
        name: 'itemType1',
      },
    },
  });
});
