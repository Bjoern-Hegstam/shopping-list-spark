import { createLoadingSelector } from './RequestSelectors';
import { DELETE_SHOPPING_LIST_ITEM, UPDATE_SHOPPING_LIST_ITEM } from '../actions/types';
import { hasId } from '../util/util';

export const shoppingListsSelector = state => state.shoppingList.shoppingLists;

export const shoppingListSelector = listId => state => {
  const shoppingList = state.shoppingList.shoppingLists.find(hasId(listId));

  if (!shoppingList) {
    return null;
  }

  const itemUpdatingSelector = createLoadingSelector(UPDATE_SHOPPING_LIST_ITEM, DELETE_SHOPPING_LIST_ITEM)(state);

  return {
    ...shoppingList,
    items: (shoppingList.items || [])
      .map(item => ({
        ...item,
        loading: !item.id || itemUpdatingSelector[`${listId}:${item.id}`],
      })),
  };
};
