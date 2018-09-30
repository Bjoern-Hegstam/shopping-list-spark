import { schema } from 'normalizr';

export const itemTypeSchema = new schema.Entity('itemTypes');

export const itemSchema = new schema.Entity('items', {
    itemType: itemTypeSchema,
});

export const shoppingListSchema = new schema.Entity('shoppingLists', {
    items: [itemSchema],
});

export const shoppingListsSchema = [shoppingListSchema];
