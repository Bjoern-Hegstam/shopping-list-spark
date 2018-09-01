import reducer from '../../js/reducers/ShoppingListReducer';
import * as types from '../../js/actions/types';

const initialState = {
    shoppingLists: {},

    fetchingShoppingLists: false,
    errorGetShoppingLists: null,

    addingShoppingList: false,
    errorAddShoppingList: null,

    updatingShoppingList: false,
    errorUpdateShoppingList: null,

    deletingShoppingList: false,
    errorDeleteShoppingList: null,

    addingShoppingListItem: false,
    errorAddShoppingListItem: null,

    updatingShoppingListItem: false,
    errorUpdateShoppingListItem: null,

    deletingShoppingListItem: false,
    errorDeleteShoppingListItem: null,

    emptyingCart: false,
    errorEmptyCart: null,
};

it('should return initial state for unknown action type', () => {
    // given
    const state = undefined;
    const action = { type: 'unknown' };

    // when
    const newState = reducer(state, action);

    // then
    expect(newState).toEqual(initialState);
});

describe('GET_SHOPPING_LISTS', () => {
    it('should set fetching flag and reset error on action GET_SHOPPING_LISTS', () => {
        const state = {
            ...initialState,
            errorGetShoppingLists: 'Error',
        };
        const action = { type: types.GET_SHOPPING_LISTS };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            fetchingShoppingLists: true,
            errorGetShoppingLists: null,
        });
    });

    it('GET_SHOPPING_LISTS_SUCCESS when fetching lists first time', () => {
        // given
        const state = {
            ...initialState,
            fetchingShoppingLists: true,
        };
        const action = {
            type: types.GET_SHOPPING_LISTS_SUCCESS,
            payload: {
                data: {
                    shoppingLists: [
                        {
                            id: 17,
                            name: 'Foo',
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
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            fetchingShoppingLists: false,
            shoppingLists: {
                17: {
                    id: 17,
                    name: 'Foo',
                    items: [],
                    fetching: false,
                    error: null,
                },
                18: {
                    id: 18,
                    name: 'Bar',
                    items: [],
                    fetching: false,
                    error: null,
                },
            },
        });
    });

    it('GET_SHOPPING_LISTS_SUCCESS updates previously fetched list', () => {
        // given
        const state = {
            ...initialState,
            fetchingShoppingLists: true,
            shoppingLists: {
                17: {
                    id: 17,
                    name: 'Foo',
                    items: [
                        'item',
                    ],
                },
            },
        };
        const action = {
            type: types.GET_SHOPPING_LISTS_SUCCESS,
            payload: {
                data: {
                    shoppingLists: [{
                        id: 17,
                        name: 'Bar',
                    }],
                },
            },
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            fetchingShoppingLists: false,
            errorGetShoppingLists: null,
            shoppingLists: {
                17: {
                    id: 17,
                    name: 'Bar',
                    items: [],
                    fetching: false,
                    error: null,
                },
            },
        });
    });

    it('should set error on GET_SHOPPING_LISTS_FAIL', () => {
        // given
        const state = {
            ...initialState,
            fetchingShoppingLists: true,
        };
        const action = {
            type: types.GET_SHOPPING_LISTS_FAIL,
            error: 'Error while fetching',
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            fetchingShoppingLists: false,
            errorGetShoppingLists: 'Error while fetching',
        });
    });
});

describe('GET_SHOPPING_LIST', () => {
    it('should create new list when GET_SHOPPING_LIST received and list not fetched before', () => {
        const listId = '90a7dcb5-4777-4ba2-95ce-b5694b3e9314';

        const state = {
            ...initialState,
        };
        const action = {
            type: types.GET_SHOPPING_LIST,
            queryInfo: {
                listId,
            },
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            shoppingLists: {
                [listId]: {
                    id: listId,
                    name: '',
                    items: [],
                    fetching: true,
                    error: null,
                },
            },
        });
    });

    it('should set fetching flag and reset error on GET_SHOPPING_LIST', () => {
        const listId = '90a7dcb5-4777-4ba2-95ce-b5694b3e9314';

        const state = {
            ...initialState,
            shoppingLists: {
                [listId]: {
                    id: listId,
                    name: 'Foo',
                    items: [{
                        id: '2045b9a4-46b1-49a5-af68-2a6544490416',
                        itemType: {
                            id: '72c7314f-df0e-4fc7-b4d6-af1373bfb821',
                            name: 'Apples',
                        },
                        quantity: 5,
                        inCart: false,
                    }],
                    fetching: false,
                    error: 'Error',
                },
            },
        };
        const action = {
            type: types.GET_SHOPPING_LIST,
            queryInfo: {
                listId,
            },
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            shoppingLists: {
                [listId]: {
                    id: listId,
                    name: 'Foo',
                    items: [{
                        id: '2045b9a4-46b1-49a5-af68-2a6544490416',
                        itemType: {
                            id: '72c7314f-df0e-4fc7-b4d6-af1373bfb821',
                            name: 'Apples',
                        },
                        quantity: 5,
                        inCart: false,
                    }],
                    fetching: true,
                    error: null,
                },
            },
        });
    });

    it('should handle GET_SHOPPING_LIST_SUCCESS', () => {
        const listId = '90a7dcb5-4777-4ba2-95ce-b5694b3e9314';

        const state = {
            ...initialState,
        };
        const action = {
            type: types.GET_SHOPPING_LIST_SUCCESS,
            payload: {
                data: {
                    id: listId,
                    name: 'Foo',
                    items: [
                        {
                            id: 'b756f961-d8ab-4cbe-a9c5-641d3fb5036d',
                            itemType: {
                                id: '3370eb06-bc2f-45cc-ab19-5d4428d2beb6',
                                name: 'Bananas',
                            },
                            quantity: 1,
                            inCart: true,
                        },
                        {
                            id: '2045b9a4-46b1-49a5-af68-2a6544490416',
                            itemType: {
                                id: '72c7314f-df0e-4fc7-b4d6-af1373bfb821',
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
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            shoppingLists: {
                [listId]: {
                    id: listId,
                    name: 'Foo',
                    items: [
                        {
                            id: '2045b9a4-46b1-49a5-af68-2a6544490416',
                            itemType: {
                                id: '72c7314f-df0e-4fc7-b4d6-af1373bfb821',
                                name: 'Apples',
                            },
                            quantity: 5,
                            inCart: false,
                        },
                        {
                            id: 'b756f961-d8ab-4cbe-a9c5-641d3fb5036d',
                            itemType: {
                                id: '3370eb06-bc2f-45cc-ab19-5d4428d2beb6',
                                name: 'Bananas',
                            },
                            quantity: 1,
                            inCart: true,
                        },
                    ],
                    fetching: false,
                    error: null,
                },
            },
        });
    });

    it('should handle GET_SHOPPING_LIST_FAIL', () => {
        const listId = '90a7dcb5-4777-4ba2-95ce-b5694b3e9314';

        const state = {
            ...initialState,
            shoppingLists: {
                [listId]: {
                    id: listId,
                    name: 'Foo',
                    items: [{
                        id: '2045b9a4-46b1-49a5-af68-2a6544490416',
                        itemType: {
                            id: '72c7314f-df0e-4fc7-b4d6-af1373bfb821',
                            name: 'Apples',
                        },
                        quantity: 5,
                        inCart: false,
                    }],
                    fetching: true,
                    error: null,
                },
            },
        };
        const action = {
            type: types.GET_SHOPPING_LIST_FAIL,
            error: 'Error',
            meta: {
                previousAction: {
                    queryInfo: {
                        listId,
                    },
                },
            },
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            shoppingLists: {
                [listId]: {
                    id: listId,
                    name: 'Foo',
                    items: [{
                        id: '2045b9a4-46b1-49a5-af68-2a6544490416',
                        itemType: {
                            id: '72c7314f-df0e-4fc7-b4d6-af1373bfb821',
                            name: 'Apples',
                        },
                        quantity: 5,
                        inCart: false,
                    }],
                    fetching: false,
                    error: 'Error',
                },
            },
        });
    });
});

describe('ADD_SHOPPING_LIST', () => {
    it('should set fetching flag and reset error on ADD_SHOPPING_LIST', () => {
        const state = {
            ...initialState,
            errorAddShoppingList: 'Error',
        };
        const action = {
            type: types.ADD_SHOPPING_LIST,
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            addingShoppingList: true,
            errorAddShoppingList: null,
        });
    });

    it('should handle ADD_SHOPPING_LIST_SUCCESS', () => {
        const state = {
            ...initialState,
        };
        const action = {
            type: types.ADD_SHOPPING_LIST_SUCCESS,
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            addingShoppingList: false,
        });
    });

    it('should handle ADD_SHOPPING_LIST_FAIL', () => {
        const state = {
            ...initialState,
        };
        const action = {
            type: types.ADD_SHOPPING_LIST_FAIL,
            error: 'Error',
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            addingShoppingList: false,
            errorAddShoppingList: 'Error',
        });
    });
});

describe('UPDATE_SHOPPING_LIST', () => {
    it('should set fetching flag and reset error on UPDATE_SHOPPING_LIST', () => {
        const state = {
            ...initialState,
            errorUpdateShoppingList: 'Error',
        };
        const action = {
            type: types.UPDATE_SHOPPING_LIST,
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            updatingShoppingList: true,
            errorUpdateShoppingList: null,
        });
    });

    it('should handle UPDATE_SHOPPING_LIST_SUCCESS', () => {
        const state = {
            ...initialState,
        };
        const action = {
            type: types.UPDATE_SHOPPING_LIST_SUCCESS,
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            updatingShoppingList: false,
        });
    });

    it('should handle UPDATE_SHOPPING_LIST_FAIL', () => {
        const state = {
            ...initialState,
        };
        const action = {
            type: types.UPDATE_SHOPPING_LIST_FAIL,
            error: 'Error',
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            updatingShoppingList: false,
            errorUpdateShoppingList: 'Error',
        });
    });
});

describe('DELETE_SHOPPING_LIST', () => {
    it('should set fetching flag and reset error on DELETE_SHOPPING_LIST', () => {
        const state = {
            ...initialState,
            errorDeleteShoppingList: 'Error',
        };
        const action = {
            type: types.DELETE_SHOPPING_LIST,
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            deletingShoppingList: true,
            errorDeleteShoppingList: null,
        });
    });

    it('should handle DELETE_SHOPPING_LIST_SUCCESS', () => {
        const state = {
            ...initialState,
        };
        const action = {
            type: types.DELETE_SHOPPING_LIST_SUCCESS,
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            deletingShoppingList: false,
        });
    });

    it('should handle DELETE_SHOPPING_LIST_FAIL', () => {
        const state = {
            ...initialState,
        };
        const action = {
            type: types.DELETE_SHOPPING_LIST_FAIL,
            error: 'Error',
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            deletingShoppingList: false,
            errorDeleteShoppingList: 'Error',
        });
    });
});

describe('ADD_SHOPPING_LIST_ITEM', () => {
    it('should set fetching flag and reset error on ADD_SHOPPING_LIST_ITEM', () => {
        const state = {
            ...initialState,
            errorAddShoppingListItem: 'Error',
        };
        const action = {
            type: types.ADD_SHOPPING_LIST_ITEM,
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            addingShoppingListItem: true,
            errorAddShoppingListItem: null,
        });
    });

    it('should handle ADD_SHOPPING_LIST_ITEM_SUCCESS', () => {
        const state = {
            ...initialState,
        };
        const action = {
            type: types.ADD_SHOPPING_LIST_ITEM_SUCCESS,
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            addingShoppingListItem: false,
        });
    });

    it('should handle ADD_SHOPPING_LIST_ITEM_FAIL', () => {
        const state = {
            ...initialState,
        };
        const action = {
            type: types.ADD_SHOPPING_LIST_ITEM_FAIL,
            error: 'Error',
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            addingShoppingListItem: false,
            errorAddShoppingListItem: 'Error',
        });
    });
});

describe('UPDATE_SHOPPING_LIST_ITEM', () => {
    it('should set fetching flag and reset error on UPDATE_SHOPPING_LIST_ITEM', () => {
        const state = {
            ...initialState,
            errorUpdateShoppingListItem: 'Error',
        };
        const action = {
            type: types.UPDATE_SHOPPING_LIST_ITEM,
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            updatingShoppingListItem: true,
            errorUpdateShoppingListItem: null,
        });
    });

    it('should handle UPDATE_SHOPPING_LIST_ITEM_SUCCESS', () => {
        const state = {
            ...initialState,
        };
        const action = {
            type: types.UPDATE_SHOPPING_LIST_ITEM_SUCCESS,
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            updatingShoppingListItem: false,
        });
    });

    it('should handle UPDATE_SHOPPING_LIST_ITEM_FAIL', () => {
        const state = {
            ...initialState,
        };
        const action = {
            type: types.UPDATE_SHOPPING_LIST_ITEM_FAIL,
            error: 'Error',
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            updatingShoppingListItem: false,
            errorUpdateShoppingListItem: 'Error',
        });
    });
});

describe('DELETE_SHOPPING_LIST_ITEM', () => {
    it('should set fetching flag and reset error on DELETE_SHOPPING_LIST_ITEM', () => {
        const state = {
            ...initialState,
            errorDeleteShoppingListItem: 'Error',
        };
        const action = {
            type: types.DELETE_SHOPPING_LIST_ITEM,
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            deletingShoppingListItem: true,
            errorDeleteShoppingListItem: null,
        });
    });

    it('should handle DELETE_SHOPPING_LIST_ITEM_SUCCESS', () => {
        const state = {
            ...initialState,
        };
        const action = {
            type: types.DELETE_SHOPPING_LIST_ITEM_SUCCESS,
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            deletingShoppingListItem: false,
        });
    });

    it('should handle DELETE_SHOPPING_LIST_ITEM_FAIL', () => {
        const state = {
            ...initialState,
        };
        const action = {
            type: types.DELETE_SHOPPING_LIST_ITEM_FAIL,
            error: 'Error',
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            deletingShoppingListItem: false,
            errorDeleteShoppingListItem: 'Error',
        });
    });
});

describe('EMPTY_CART', () => {
    it('should set fetching flag and reset error on EMPTY_CART', () => {
        const state = {
            ...initialState,
            errorEmptyCart: 'Error',
        };
        const action = {
            type: types.EMPTY_CART,
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            emptyingCart: true,
            errorEmptyCart: null,
        });
    });

    it('should handle EMPTY_CART_SUCCESS', () => {
        const state = {
            ...initialState,
        };
        const action = {
            type: types.EMPTY_CART_SUCCESS,
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            emptyingCart: false,
        });
    });

    it('should handle EMPTY_CART_FAIL', () => {
        const state = {
            ...initialState,
        };
        const action = {
            type: types.EMPTY_CART_FAIL,
            error: 'Error',
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            emptyingCart: false,
            errorEmptyCart: 'Error',
        });
    });
});
