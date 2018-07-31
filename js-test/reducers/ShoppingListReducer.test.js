import reducer from "../../js/reducers/ShoppingListReducer";
import * as types from "../../js/actions/types";

describe('ShoppingListReducer', () => {
    const initialState = {
        shoppingLists: {},
        fetchingShoppingLists: false,
        errorGetShoppingLists: null
    };

    it('should return initial state for unknown action type', () => {
        // given
        const state = undefined;
        const action = {type: 'unknown'};

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual(initialState)
    });

    it('should set fetching flag and reset error on action GET_SHOPPING_LISTS', () => {
        const state = {
            ...initialState,
            errorGetShoppingLists: 'Error'
        };
        const action = {type: types.GET_SHOPPING_LISTS};

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            fetchingShoppingLists: true,
            errorGetShoppingLists: null
        })
    });

    it('GET_SHOPPING_LISTS_SUCCESS when fetching lists first time', () => {
        // given
        const state = {
                ...initialState,
            fetchingShoppingLists: true
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
                        }
                    ]
                }
            }
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
                    error: null
                },
                18: {
                    id: 18,
                    name: 'Bar',
                    items: [],
                    fetching: false,
                    error: null
                }
            }
        })
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
                        'item'
                    ]
                }
            }
        };
        const action = {
            type: types.GET_SHOPPING_LISTS_SUCCESS,
            payload: {
                data: {
                    shoppingLists: [{
                        id: 17,
                        name: 'Bar',
                    }]
                }
            }
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
                    error: null
                }
            }
        })
    });

    it('should set error on GET_SHOPPING_LISTS_FAIL', () => {
        // given
        const state = {
            ...initialState,
            fetchingShoppingLists: true
        };
        const action = {
            type: types.GET_SHOPPING_LISTS_FAIL,
            error: 'Error while fetching'
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            fetchingShoppingLists: false,
            errorGetShoppingLists: 'Error while fetching'
        })
    });

    it('should create new list when GET_SHOPPING_LIST received and list not fetched before', () => {
        const listId = "90a7dcb5-4777-4ba2-95ce-b5694b3e9314";

        const state = {
            ...initialState
        };
        const action = {
            type: types.GET_SHOPPING_LIST,
            queryInfo: {
                listId
            }
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
                    error: null
                }
            }
        })
    });

    it('should set fetching flag and reset error on GET_SHOPPING_LIST', () => {
        const listId = "90a7dcb5-4777-4ba2-95ce-b5694b3e9314";

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
                            name: 'Apples'
                        },
                        quantity: 5,
                        inCart: false
                    }],
                    fetching: false,
                    error: "Error"
                }
            }
        };
        const action = {
            type: types.GET_SHOPPING_LIST,
            queryInfo: {
                listId
            }
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
                            name: 'Apples'
                        },
                        quantity: 5,
                        inCart: false
                    }],
                    fetching: true,
                    error: null
                }
            }
        })
    });

    it('should handle GET_SHOPPING_LIST_SUCCESS', () => {
        const listId = "90a7dcb5-4777-4ba2-95ce-b5694b3e9314";

        const state = {
            ...initialState
        };
        const action = {
            type: types.GET_SHOPPING_LIST_SUCCESS,
            payload: {
                data: {
                    id: listId,
                    name: 'Foo',
                    items: [{
                        id: '2045b9a4-46b1-49a5-af68-2a6544490416',
                        itemType: {
                            id: '72c7314f-df0e-4fc7-b4d6-af1373bfb821',
                            name: 'Apples'
                        },
                        quantity: 5,
                        inCart: false
                    }]
                }
            }
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
                            name: 'Apples'
                        },
                        quantity: 5,
                        inCart: false
                    }],
                    fetching: false,
                    error: null
                }
            }
        })
    });

    it('should handle GET_SHOPPING_LIST_FAIL', () => {
        const listId = "90a7dcb5-4777-4ba2-95ce-b5694b3e9314";

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
                            name: 'Apples'
                        },
                        quantity: 5,
                        inCart: false
                    }],
                    fetching: true,
                    error: null
                }
            }
        };
        const action = {
            type: types.GET_SHOPPING_LIST_FAIL,
            error: "Error",
            meta: {
                previousAction: {
                    queryInfo: {
                        listId
                    }
                }
            }
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
                            name: 'Apples'
                        },
                        quantity: 5,
                        inCart: false
                    }],
                    fetching: false,
                    error: "Error"
                }
            }
        })
    });
});