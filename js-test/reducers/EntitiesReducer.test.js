import * as types from '../../js/actions/types';
import reducer from '../../js/reducers/EntitiesReducer';

const shoppingList1Id = 'd84fcaa9-d3b4-49f5-bb6f-4f3889406056';
const shoppingList2Id = 'f4783aa3-7171-413a-a8a3-2c3669ded567';

const itemType1Id = '83b66aa7-3189-4e11-b4fa-fc73ded86ca5';
const itemType2Id = '69d58d9c-f763-4481-a5a6-0defa1993329';

const initialState = {
  shoppingLists: {
    [shoppingList1Id]: {
      id: shoppingList1Id,
      name: 'Foo',
    },
    [shoppingList2Id]: {
      id: shoppingList2Id,
      name: 'Baz',
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
  const newShoppingListId = '05c3cb46-35e6-462c-a40f-f9469f0e6d30';
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
  const item1Id = 'b756f961-d8ab-4cbe-a9c5-641d3fb5036d';
  const item2Id = '2045b9a4-46b1-49a5-af68-2a6544490416';

  const itemTypeBananas = '3370eb06-bc2f-45cc-ab19-5d4428d2beb6';
  const itemTypeApples = '72c7314f-df0e-4fc7-b4d6-af1373bfb821';

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

it('handles ADD_ITEM_TYPE_SUCCESS', () => {
  // given
  const newItemType = {
    id: '56cbbed1-ffce-403d-84ef-b98a4be839d9',
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
  const newItemTypeId = '56cbbed1-ffce-403d-84ef-b98a4be839d9';
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
    shoppingLists: {
      [shoppingList1Id]: {
        id: shoppingList1Id,
        name: 'Foo',
      },
      [shoppingList2Id]: {
        id: shoppingList2Id,
        name: 'Baz',
      },
    },
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
        queryInfo: {
          itemTypeId: itemType1Id,
        },
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
      },
      [shoppingList2Id]: {
        id: shoppingList2Id,
        name: 'Baz',
      },
    },
    itemTypes: {
      [itemType1Id]: {
        id: itemType1Id,
        name: 'itemType1',
        deleted: true,
      },
      [itemType2Id]: {
        id: itemType2Id,
        name: 'itemType2',
      },
    },
  });
});
