import reducer from "../../js/reducers/ItemTypeReducer";
import * as types from "../../js/actions/types";

describe('ItemTypeReducer', () => {
    const initialState = {
        itemTypes: [],
        fetchingItemTypes: false,
        errorGetItemTypes: null
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

    it('should set fetching flag and reset error on action GET_ITEM_TYPES', () => {
        const state = {
            ...initialState,
            errorGetItemTypes: 'Error'
        };
        const action = {type: types.GET_ITEM_TYPES};

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            fetchingItemTypes: true,
            errorGetItemTypes: null
        })
    });

    it('GET_ITEM_TYPES_SUCCESS', () => {
        // given
        const state = {
            ...initialState,
            fetchingItemTypes: true
        };
        const action = {
            type: types.GET_ITEM_TYPES_SUCCESS,
            payload: {
                data: {
                    itemTypes: [
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
            fetchingItemTypes: false,
            itemTypes: [
                {
                    id: 17,
                    name: 'Foo',
                },
                {
                    id: 18,
                    name: 'Bar',
                }
            ]
        })
    });

    it('should set error on GET_ITEM_TYPES_FAIL', () => {
        // given
        const state = {
            ...initialState,
            fetchingItemTypes: true
        };
        const action = {
            type: types.GET_ITEM_TYPES_FAIL,
            error: 'Error while fetching'
        };

        // when
        const newState = reducer(state, action);

        // then
        expect(newState).toEqual({
            ...initialState,
            fetchingItemTypes: false,
            errorGetItemTypes: 'Error while fetching'
        })
    });
});