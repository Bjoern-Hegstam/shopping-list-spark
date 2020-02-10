import { getItemTypeByName } from './itemTypes';

const itemTypes = [
  {
    id: '29e5f3b8-735e-4eb3-a97d-917abaf14624',
    name: 'Apples',
  },
  {
    id: '076467ea-033b-423e-9dca-0faff6cd4b76',
    name: 'Bananas',
  },
  {
    id: '9cc0191b-2b12-4861-b552-9c3092bbbd6b',
    name: 'Granny smith',
  },
  {
    id: '5cbcd7a6-089a-423d-8b90-9fa025be594a',
    name: 'Lettuce',
  },
  {
    id: '464d2424-8e84-496a-9b2e-48aed96d6557',
    name: 'Pasta',
  },
  {
    id: '6cf57a3c-ae5c-463d-87f6-8cc8bae19ad8',
    name: 'Onion',
  },
  {
    id: '590f120d-a099-476f-b63e-f0e49fdeafa4',
    name: 'Tomatoes',
  },
];

export const emptyShoppingList = {
  id: '4e907f83-2353-4fc6-8e51-d0e55972ccbd',
  name: 'Empty shopping list',
  itemTypes: [],
  items: [],
};

export const shoppingListWithEmptyCart = {
  id: '655a6541-2262-4044-8d87-041a300d890f',
  name: 'Test shopping list with empty cart',
  itemTypes,
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
};

export const shoppingListWithItemsInCart = {
  id: '655a6541-2262-4044-8d87-041a300d890f',
  name: 'Test shopping list with items in cart',
  itemTypes,
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
};
