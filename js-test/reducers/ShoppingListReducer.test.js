import reducer from '../../js/reducers/ShoppingListReducer';
import * as types from '../../js/actions/types';
import { deepCopy } from '../util';

const initialState = {
  shoppingLists: [],
};

const requestId = 'test-request-id';

const shoppingList1Id = 'shopping-list-1-id';
const shoppingList2Id = 'shopping-list-2-id';

const partialShoppingList1 = {
  id: shoppingList1Id,
  name: 'partial-list-1-name',
  itemTypes: [],
  items: [],
};

const shoppingList1 = {
  ...partialShoppingList1,
  itemTypes: [],
  items: [],
};

const partialShoppingList2 = {
  id: shoppingList2Id,
  name: 'partial-list-2-name',
  itemTypes: [],
  items: [],
};

const itemType1 = {
  id: 'item-type-1-id',
  name: 'item-type-1-name',
};

const itemType2 = {
  id: 'item-type-2-id',
  name: 'item-type-2-name',
};

const item2 = {
  id: 'item-2-id',
  itemType: itemType2,
  quantity: 1,
  inCart: false,
};

it('initial state', () => {
  const state = reducer(undefined, { type: 'INIT' });

  expect(state).toEqual(initialState);
});

describe('GET_SHOPPING_LISTS', () => {
  it('success - overwrites existing state', () => {
    // given
    const state = {
      ...initialState,
      shoppingLists: [partialShoppingList1],
    };

    const action = {
      type: types.GET_SHOPPING_LISTS_SUCCESS,
      payload: {
        data: {
          shoppingLists: [{
            id: shoppingList2Id,
            name: 'partial-list-2-name',
          }],
        },
      },
    };

    // when
    const newState = reducer(state, action);

    // then
    expect(newState).toEqual({
      ...initialState,
      shoppingLists: [partialShoppingList2],
    });
  });
});

describe('GET_SHOPPING_LIST', () => {
  it('success', () => {
    // given
    const state = {
      ...initialState,
      shoppingLists: [
        partialShoppingList1,
        partialShoppingList2,
      ],
    };

    const action = {
      type: types.GET_SHOPPING_LIST_SUCCESS,
      payload: {
        data: shoppingList1,
      },
    };

    // when
    const newState = reducer(state, action);

    // then
    expect(newState).toEqual({
      ...initialState,
      shoppingLists: [
        partialShoppingList2,
        shoppingList1,
      ],
    });
  });
});

describe('ADD_SHOPPING_LIST_ITEM', () => {
  it('pending - add by itemTypeId', () => {
    // given
    const state = {
      ...initialState,
      shoppingLists: [{
        ...shoppingList1,
        itemTypes: [itemType1, itemType2],
        items: [item2],
      }],
    };

    const action = {
      type: types.ADD_SHOPPING_LIST_ITEM,
      meta: {
        requestId,
        listId: shoppingList1.id,
      },
      payload: {
        request: {
          data: {
            itemTypeId: itemType1.id,
            quantity: 2,
          },
        },
      },
    };

    // when
    const newState = reducer(state, action);

    // then
    expect(newState).toEqual({
      ...initialState,
      shoppingLists: [{
        ...shoppingList1,
        itemTypes: [itemType1, itemType2],
        items: [
          {
            requestId,
            itemType: itemType1,
            quantity: 2,
            inCart: false,
          },
          item2,
        ],
      }],
    });
  });

  it('pending - add by itemTypeName', () => {
    // given
    const state = {
      ...initialState,
      shoppingLists: [{
        ...shoppingList1,
        itemTypes: [itemType2],
        items: [item2],
      }],
    };

    const action = {
      type: types.ADD_SHOPPING_LIST_ITEM,
      meta: {
        requestId,
        listId: shoppingList1.id,
      },
      payload: {
        request: {
          data: {
            itemTypeName: 'Apples',
            quantity: 2,
          },
        },
      },
    };

    // when
    const newState = reducer(state, action);

    // then
    expect(newState).toEqual({
      ...initialState,
      shoppingLists: [{
        ...shoppingList1,
        itemTypes: [
          itemType2,
          {
            requestId,
            name: 'Apples',
          },
        ],
        items: [
          {
            requestId,
            itemType: {
              requestId,
              name: 'Apples',
            },
            quantity: 2,
            inCart: false,
          },
          item2,
        ],
      }],
    });
  });

  it('success - added by itemTypeId', () => {
    // given
    const state = {
      ...initialState,
      shoppingLists: [{
        ...shoppingList1,
        itemTypes: [itemType1, itemType2],
        items: [
          {
            requestId,
            itemType: itemType1,
            quantity: 2,
          },
          item2,
        ],
      }],
    };

    const persistedItemId = 'persisted-item-id';

    const action = {
      type: types.ADD_SHOPPING_LIST_ITEM_SUCCESS,
      meta: {
        previousAction: {
          meta: {
            requestId,
            listId: shoppingList1Id,
          },
        },
      },
      payload: {
        data: {
          id: persistedItemId,
          itemType: itemType1,
        },
      },
    };

    // when
    const newState = reducer(state, action);

    // then
    const expectedState = deepCopy(state);
    expectedState.shoppingLists[0].items[0].id = persistedItemId;
    expectedState.shoppingLists[0].items[0].requestId = undefined;

    expect(newState).toEqual(expectedState);
  });

  it('success - added by itemTypeName', () => {
    // given
    const itemType = {
      requestId,
      name: 'a-test-item-type-name',
    };
    const state = {
      ...initialState,
      shoppingLists: [{
        ...shoppingList1,
        itemTypes: [itemType, itemType2],
        items: [
          {
            requestId,
            itemType,
            quantity: 2,
          },
          item2,
        ],
      }],
    };

    const persistedItemId = 'persisted-item-id';
    const persistedItemTypeId = 'persisted-item-type-id';

    const action = {
      type: types.ADD_SHOPPING_LIST_ITEM_SUCCESS,
      meta: {
        previousAction: {
          meta: {
            requestId,
            listId: shoppingList1Id,
          },
        },
      },
      payload: {
        data: {
          id: persistedItemId,
          itemType: {
            id: persistedItemTypeId,
          },
        },
      },
    };

    // when
    const newState = reducer(state, action);

    // then
    const expectedState = deepCopy(state);
    expectedState.shoppingLists[0].itemTypes = [
      itemType2,
      {
        id: persistedItemTypeId,
        name: 'a-test-item-type-name',
        requestId: undefined,
      },
    ];
    expectedState.shoppingLists[0].items[0].id = persistedItemId;
    expectedState.shoppingLists[0].items[0].requestId = undefined;

    expect(newState).toEqual(expectedState);
  });

  it('fail - added by itemTypeId', () => {
    // given
    const state = {
      ...initialState,
      shoppingLists: [{
        ...shoppingList1,
        itemTypes: [itemType1],
        items: [{
          requestId,
          itemType: itemType1,
          quantity: 2,
        }],
      }],
    };

    const action = {
      type: types.ADD_SHOPPING_LIST_ITEM_FAIL,
      meta: {
        previousAction: {
          meta: {
            requestId,
            listId: shoppingList1Id,
          },
        },
      },
    };

    // when
    const newState = reducer(state, action);

    // then
    const expectedState = deepCopy(state);
    expectedState.shoppingLists[0].items = [];

    expect(newState).toEqual(expectedState);
  });

  it('fail - added by itemTypeName', () => {
    // given
    const itemType = {
      requestId,
      name: 'test-item-type-name',
    };
    const state = {
      ...initialState,
      shoppingLists: [{
        ...shoppingList1,
        itemTypes: [itemType],
        items: [{
          requestId,
          itemType,
          quantity: 2,
        }],
      }],
    };

    const action = {
      type: types.ADD_SHOPPING_LIST_ITEM_FAIL,
      meta: {
        previousAction: {
          meta: {
            requestId,
            listId: shoppingList1Id,
          },
        },
      },
    };

    // when
    const newState = reducer(state, action);

    // then
    const expectedState = deepCopy(state);
    expectedState.shoppingLists[0].itemTypes = [];
    expectedState.shoppingLists[0].items = [];

    expect(newState).toEqual(expectedState);
  });
});

describe('UPDATE_SHOPPING_LIST_ITEM', () => {
  it('pending - update item and save old version', () => {
    // given
    const item = {
      id: 'item-1-id',
      itemType: itemType1,
      quantity: 2,
    };
    const state = {
      ...initialState,
      shoppingLists: [{
        ...shoppingList1,
        itemTypes: [itemType1, itemType2],
        items: [item, item2],
      }],
    };

    const action = {
      type: types.UPDATE_SHOPPING_LIST_ITEM,
      meta: {
        listId: shoppingList1Id,
        itemId: item.id,
      },
      payload: {
        request: {
          data: {
            quantity: 5,
            inCart: true,
          },
        },
      },
    };

    // when
    const newState = reducer(state, action);

    // then
    const expectedState = deepCopy(state);
    expectedState.shoppingLists[0].items = [
      {
        ...item,
        quantity: 5,
        inCart: true,
        prevItem: item,
      },
      item2,
    ];

    expect(newState).toEqual(expectedState);
  });

  it('success', () => {
    // given
    const prevItem = {
      id: 'item-1-id',
      itemType: itemType1,
      quantity: 2,
    };
    const item = {
      ...prevItem,
      quantity: 5,
      inCart: true,
      prevItem,
    };
    const state = {
      ...initialState,
      shoppingLists: [{
        ...shoppingList1,
        itemTypes: [itemType1, itemType2],
        items: [item, item2],
      }],
    };

    const action = {
      type: types.UPDATE_SHOPPING_LIST_ITEM_SUCCESS,
      meta: {
        previousAction: {
          meta: {
            listId: shoppingList1Id,
            itemId: item.id,
          },
        },
      },
    };

    // when
    const newState = reducer(state, action);

    // then
    const expectedState = deepCopy(state);
    expectedState.shoppingLists[0].items = [
      {
        ...item,
        prevItem: undefined,
      },
      item2,
    ];

    expect(newState).toEqual(expectedState);
  });

  it('fail', () => {
    // given
    const prevItem = {
      id: 'item-1-id',
      itemType: itemType1,
      quantity: 2,
    };
    const item = {
      ...prevItem,
      quantity: 5,
      inCart: true,
      prevItem,
    };
    const state = {
      ...initialState,
      shoppingLists: [{
        ...shoppingList1,
        itemTypes: [itemType1, itemType2],
        items: [item, item2],
      }],
    };

    const action = {
      type: types.UPDATE_SHOPPING_LIST_ITEM_FAIL,
      meta: {
        previousAction: {
          meta: {
            listId: shoppingList1Id,
            itemId: item.id,
          },
        },
      },
    };

    // when
    const newState = reducer(state, action);

    // then
    const expectedState = deepCopy(state);
    expectedState.shoppingLists[0].items = [prevItem, item2];

    expect(newState).toEqual(expectedState);
  });
});

describe('DELETE_SHOPPING_LIST_ITEM', () => {
  it('success', () => {
    // given
    const item = {
      id: 'item-1-id',
      itemType: itemType1,
      quantity: 2,
    };
    const state = {
      ...initialState,
      shoppingLists: [{
        ...shoppingList1,
        itemTypes: [itemType1],
        items: [item],
      }],
    };

    const action = {
      type: types.DELETE_SHOPPING_LIST_ITEM_SUCCESS,
      meta: {
        previousAction: {
          meta: {
            listId: shoppingList1Id,
            itemId: item.id,
          },
        },
      },
    };

    // when
    const newState = reducer(state, action);

    // then
    const expectedState = deepCopy(state);
    expectedState.shoppingLists[0].items = [];

    expect(newState).toEqual(expectedState);
  });
});
