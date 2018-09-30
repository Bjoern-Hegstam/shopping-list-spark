export const createLoadingSelector = actionTypes => state => actionTypes
    .some(type => state.request.loading[type]);

export const createErrorMessageSelector = actionTypes => state => actionTypes
    .find(type => state.request.error[type]);

export function shoppingListsSelector(state) {
    return state.shoppingList.shoppingLists;
}

export function shoppingListSelector(state, listId) {
    const { shoppingLists } = state.shoppingList;
    return listId in shoppingLists ? shoppingLists[listId] : undefined;
}

export function itemTypesSelector(state) {
    return state.itemType.itemTypes;
}
