export const createLoadingSelector = (...actionTypes) => state => actionTypes
    .some(type => state.request.loading[type]);

export const createErrorSelector = (...actionTypes) => state => actionTypes
    .find(type => state.request.error[type]) || null;

export function shoppingListsSelector(state) {
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
}

export function shoppingListSelector(state, listId) {
    const { shoppingLists, items, itemTypes } = state.entities;

    if (!shoppingLists) {
        return undefined;
    }

    const shoppingList = shoppingLists[listId];

    return {
        ...shoppingList,
        items: (shoppingList.items || [])
            .map(itemId => items[itemId])
            .map(item => ({
                ...item,
                itemType: itemTypes[item.itemType],
            })),
    };
}

export function itemTypesSelector(state) {
    const { itemTypes } = state.entities;
    return itemTypes ? Object.values(itemTypes).filter(itemType => !itemType.deleted) : [];
}
