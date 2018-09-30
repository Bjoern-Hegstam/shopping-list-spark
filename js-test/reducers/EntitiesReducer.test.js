import * as types from '../../js/actions/types';
import reducer from '../../js/reducers/EntitiesReducer';

const initialState = {
    shoppingLists: {
        17: {
            id: 17,
            name: 'Foo',
        },
        42: {
            id: 42,
            name: 'Baz',
        },
    },
    itemTypes: {
        1: {
            id: 1,
            name: 'itemType1',
        },
        2: {
            id: 2,
            name: 'itemType2',
        },
    },
};

it('handles GET_SHOPPING_LISTS_SUCCESS where lists have been added, edited and deleted', () => {
    // given
    const action = {
        type: types.GET_SHOPPING_LISTS_SUCCESS,
        payload: {
            data: {
                shoppingLists: [
                    {
                        id: 17,
                        name: 'FooNew',
                    },
                    {
                        id: 18,
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
            17: {
                id: 17,
                name: 'FooNew',
            },
            18: {
                id: 18,
                name: 'Bar',
            },
        },
        itemTypes: {
            1: {
                id: 1,
                name: 'itemType1',
            },
            2: {
                id: 2,
                name: 'itemType2',
            },
        },
    });
});

it('normalizes shopping list on GET_SHOPPING_LIST_SUCCESS', () => {
    // given
    const listId = '90a7dcb5-4777-4ba2-95ce-b5694b3e9314';

    const item1Id = 'b756f961-d8ab-4cbe-a9c5-641d3fb5036d';
    const item2Id = '2045b9a4-46b1-49a5-af68-2a6544490416';

    const itemType1Id = '3370eb06-bc2f-45cc-ab19-5d4428d2beb6';
    const itemType2Id = '72c7314f-df0e-4fc7-b4d6-af1373bfb821';

    const action = {
        type: types.GET_SHOPPING_LIST_SUCCESS,
        payload: {
            data: {
                id: listId,
                name: 'Foo',
                items: [
                    {
                        id: item1Id,
                        itemType: {
                            id: itemType1Id,
                            name: 'Bananas',
                        },
                        quantity: 1,
                        inCart: true,
                    },
                    {
                        id: item2Id,
                        itemType: {
                            id: itemType2Id,
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
            [listId]: {
                id: listId,
                name: 'Foo',
                items: [item1Id, item2Id],
            },
            17: {
                id: 17,
                name: 'Foo',
            },
            42: {
                id: 42,
                name: 'Baz',
            },
        },
        items: {
            [item1Id]: {
                id: item1Id,
                itemType: itemType1Id,
                quantity: 1,
                inCart: true,
            },
            [item2Id]: {
                id: item2Id,
                itemType: itemType2Id,
                quantity: 5,
                inCart: false,
            },
        },
        itemTypes: {
            [itemType1Id]: {
                id: itemType1Id,
                name: 'Bananas',
            },
            [itemType2Id]: {
                id: itemType2Id,
                name: 'Apples',
            },
            1: {
                id: 1,
                name: 'itemType1',
            },
            2: {
                id: 2,
                name: 'itemType2',
            },
        },
    });
});
