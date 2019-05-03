import { getItemTypeByName } from './itemTypes';

export const emptyShoppingList = {
  id: '4e907f83-2353-4fc6-8e51-d0e55972ccbd',
  name: 'Empty shopping list',
  items: [],
  meta: {
    loaded: true,
    error: null,
  },
};

export const shoppingListWithEmptyCart = {
  id: '655a6541-2262-4044-8d87-041a300d890f',
  name: 'Test shopping list with empty cart',
  items: [
    {
      id: 'fc30a4d5-6bc8-4448-970d-dd01597e22cb',
      itemType: getItemTypeByName('Apples'),
      quantity: 3,
      inCart: false,
    },
    {
      id: '899ad6f8-ddd8-4255-a5a0-c3562026daf0',
      itemType: getItemTypeByName('Lettuce'),
      quantity: 1,
      inCart: false,
    },
    {
      id: 'ad076e45-351c-4034-b400-3f3381b1d88d',
      itemType: getItemTypeByName('Pasta'),
      quantity: 5,
      inCart: false,
    },
  ],
  meta: {
    loaded: true,
    error: null,
  },
};

export const shoppingListWithItemsInCart = {
  id: '655a6541-2262-4044-8d87-041a300d890f',
  name: 'Test shopping list with items in cart',
  items: [
    {
      id: 'fc30a4d5-6bc8-4448-970d-dd01597e22cb',
      itemType: getItemTypeByName('Apples'),
      quantity: 3,
      inCart: true,
    },
    {
      id: '899ad6f8-ddd8-4255-a5a0-c3562026daf0',
      itemType: getItemTypeByName('Lettuce'),
      quantity: 1,
      inCart: false,
    },
    {
      id: 'ad076e45-351c-4034-b400-3f3381b1d88d',
      itemType: getItemTypeByName('Pasta'),
      quantity: 5,
      inCart: true,
    },
  ],
  meta: {
    loaded: true,
    error: null,
  },
};
