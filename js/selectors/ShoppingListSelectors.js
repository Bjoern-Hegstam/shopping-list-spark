import { createLoadingSelector } from './RequestSelectors';
import { ADD_SHOPPING_LIST_ITEM, DELETE_SHOPPING_LIST_ITEM, UPDATE_SHOPPING_LIST_ITEM } from '../actions/types';

export const shoppingListsSelector = (state) => {
  const { shoppingLists } = state.entities;

  if (!shoppingLists) {
    return [];
  }

  return Object
    .values(shoppingLists)
    .map(shoppingList => ({
      id: shoppingList.id,
      name: shoppingList.name,
    }));
};

export const shoppingListSelector = listId => (state) => {
  const { shoppingLists, items, itemTypes } = state.entities;

  if (!shoppingLists) {
    return undefined;
  }

  const shoppingList = shoppingLists[listId];

  // TODO: Test use of selectors
  const itemAddingSelector = createLoadingSelector(ADD_SHOPPING_LIST_ITEM)(state);
  const itemUpdatingSelector = createLoadingSelector(UPDATE_SHOPPING_LIST_ITEM, DELETE_SHOPPING_LIST_ITEM)(state);

  return {
    ...shoppingList,
    items: (shoppingList.items || [])
      .map(itemId => items[itemId])
      .map((item) => {
        const itemType = itemTypes[item.itemType];
        return ({
          ...item,
          itemType,
          loading: itemAddingSelector[`${listId}:${itemType.id}`] || itemUpdatingSelector[`${listId}:${item.id}`],
        });
      }),
  };
};

export const itemTypesSelector = (state) => {
  const { itemTypes } = state.entities;
  return itemTypes ? Object.values(itemTypes).filter(itemType => !itemType.deleted) : [];
};
