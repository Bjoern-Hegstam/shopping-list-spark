import reducer from "../../js/reducers/ShoppingListReducer";
import * as types from "../../js/actions/types";

describe('ShoppingListReducer', () => {
    const initialState = {
        shoppingLists: {},
        fetchingLists: false,
        error: null
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
            error: 'Error'
        };
        const action = {type: types.GET_SHOPPING_LISTS};

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            fetchingLists: true,
            error: null
        })
    });

    it('GET_SHOPPING_LISTS_SUCCESS when fetching lists first time', () => {
        // given
        const state = {
                ...initialState,
            fetchingLists: true
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
            fetchingLists: false,
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
            fetchingLists: true,
            shoppingLists: {
                17: {
                    id: 17,
                    name: 'Foo',
                    item: [
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
            fetchingLists: false,
            error: null,
            shoppingLists: {
                17: {
                    id: 17,
                    name: 'Bar',
                    item: [
                        'item'
                    ]
                }
            }
        })
    });

    it('should set error on GET_SHOPPING_LISTS_FAIL', () => {
        // given
        const state = {
            ...initialState,
            fetchingLists: true
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
            fetchingLists: false,
            error: 'Error while fetching'
        })
    });
});