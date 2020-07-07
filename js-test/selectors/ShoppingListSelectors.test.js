import { shoppingListsSelector } from '../../js/selectors/ShoppingListSelectors';

describe('ShoppingListsSelector', () => {
  it('returns empty array when there are no shopping lists', () => {
    // given
    const state = {
      shoppingList: {
        shoppingLists: [],
      },
    };

    // when
    const shoppingLists = shoppingListsSelector(state);

    // then
    expect(shoppingLists).toHaveLength(0);
  });

  it('returns shopping lists sorted by name', () => {
    // given
    const state = {
      shoppingList: {
        shoppingLists: [
          { name: 'foo' },
          { name: 'bar' },
        ],
      },
    };

    // when
    const shoppingLists = shoppingListsSelector(state);

    // then
    expect(shoppingLists).toEqual([
      { name: 'bar' },
      { name: 'foo' },
    ]);
  });
});
