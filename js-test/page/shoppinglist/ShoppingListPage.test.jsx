import { ShoppingListPage } from '../../../js/page/shoppinglist/ShoppingListPage';
import ShoppingList from '../../../js/page/shoppinglist/ShoppingList';
import { setupComponent } from '../../util';

const itemType = {
    id: '721eb282-a107-4263-99d9-d29e4785f0b8',
    name: 'Apples',
};
const listId = 'ad227e79-2b27-471f-ae69-88fd5bdc170e';
const item = {
    id: '9ffe1cc3-4505-4cf2-8d35-0319c6c5199a',
    itemType,
    quantity: 1,
    inCart: false,
};

function setup(optProps) {
    const {component, props} = setupComponent(
        ShoppingListPage,
        {
            token: 'token',
            match: { params: { listId } },
            history: { push: jest.fn() },

            shoppingList: {
                id: listId,
                name: 'Foo',
                items: [item],
            },
            itemTypes: [itemType],

            getShoppingList: jest.fn(),
            fetchingShoppingList: false,

            addItemType: jest.fn(),
            addingItemType: false,

            getItemTypes: jest.fn(),

            updateShoppingList: jest.fn(),
            updatingShoppingList: false,

            deleteShoppingList: jest.fn(),
            deletingShoppingList: false,

            addShoppingListItem: jest.fn(),
            addingShoppingListItem: false,

            updateShoppingListItem: jest.fn(),
            updatingShoppingListItem: false,

            deleteShoppingListItem: jest.fn(),
            deletingShoppingListItem: false,

            emptyCart: jest.fn(),
            emptyingCart: false,
        },
        optProps,
    );

    component.setState({ initialFetchComplete: true });

    return {
        component,
        props,
    };
}

let component;
let props;

beforeEach(() => {
    ({ component, props } = setup());
});

it('renders loading text when initial fetch not complete', () => {
    // given
    component.setState({ initialFetchComplete: false });

    // then
    expect(component.find(ShoppingList)).toHaveLength(0);
});


it('renders shopping list when initial fetch complete', () => {
    // given
    component.setState({ initialFetchComplete: true });

    // then
    expect(component.find(ShoppingList)).toHaveLength(1);
});

it('fetches shopping list and item types on mount', () => {
    expect(props.getShoppingList).toHaveBeenCalledWith({
        token: props.token,
        id: props.shoppingList.id,
    });
    expect(props.getItemTypes).toHaveBeenCalledWith(props.token);
});

describe('re-fetch shopping list', () => {
    it('when list updated', () => {
        // given
        component.setProps({ updatingShoppingList: true });

        // when
        component.setProps({ updatingShoppingList: false });

        // then
        expect(props.getShoppingList).toHaveBeenCalledWith({
            token: props.token,
            id: props.shoppingList.id,
        });
    });

    it('when item added', () => {
        // given
        component.setProps({ addingShoppingListItem: true });

        // when
        component.setProps({ addingShoppingListItem: false });

        // then
        expect(props.getShoppingList).toHaveBeenCalledWith({
            token: props.token,
            id: props.shoppingList.id,
        });
    });

    it('when item updated', () => {
        // given
        component.setProps({ updatingShoppingListItem: true });

        // when
        component.setProps({ updatingShoppingListItem: false });

        // then
        expect(props.getShoppingList).toHaveBeenCalledWith({
            token: props.token,
            id: props.shoppingList.id,
        });
    });

    it('when item deleted', () => {
        // given
        component.setProps({ deletingShoppingListItem: true });

        // when
        component.setProps({ deletingShoppingListItem: false });

        // then
        expect(props.getShoppingList).toHaveBeenCalledWith({
            token: props.token,
            id: props.shoppingList.id,
        });
    });

    it('when cart emptied', () => {
        // given
        component.setProps({ emptyingCart: true });

        // when
        component.setProps({ emptyingCart: false });

        // then
        expect(props.getShoppingList).toHaveBeenCalledWith({
            token: props.token,
            id: props.shoppingList.id,
        });
    });
});

describe('handle fetch of shopping list', () => {
    it('exits edit mode', () => {
        // give
        component.setState({ isEditing: true });
        component.setProps({ fetchingShoppingList: true });

        // when
        component.setProps({ fetchingShoppingList: false });

        // then
        expect(component.state('isEditing')).toBeFalsy();
    });

    it('sets initialFetchComplete on first load', () => {
        // give
        component.setState({ initialFetchComplete: false });
        component.setProps({ fetchingShoppingList: true });

        // when
        component.setProps({ fetchingShoppingList: false });

        // then
        expect(component.state('initialFetchComplete')).toBeTruthy();
    });
});

it('redirects when shopping list deleted', () => {
    // given
    component.setProps({ deletingShoppingList: true });

    // when
    component.setProps({ deletingShoppingList: false });

    // then
    expect(props.history.push).toHaveBeenCalledWith('/lists');
});

describe('edit', () => {
    it('start editing', () => {
        // given
        component.setState({ isEditing: false });

        // when
        component.find(ShoppingList).simulate('startEdit');

        // then
        component.setState({ isEditing: true });
    });

    it('change name', () => {
        // given
        component.setState({ isEditing: true });

        // when
        component.find(ShoppingList).simulate('changeName', 'Bar');

        // then
        expect(props.updateShoppingList).toHaveBeenCalledWith({
            token: props.token,
            listId: props.shoppingList.id,
            name: 'Bar',
        });
        component.setState({ isEditing: false });
    });

    it('cancel edit', () => {
        // given
        component.setState({ isEditing: true });

        // when
        component.find(ShoppingList).simulate('cancelEdit');

        // then
        component.setState({ isEditing: false });
    });
});

describe('item actions', () => {
    describe('add item', () => {
        it('new item type', async () => {
            // given
            const newItemTypeId = 'f6ac3770-c837-4e70-b580-fecc1216a195';
            props.addItemType.mockReturnValueOnce({ payload: { data: { id: newItemTypeId } } });

            // when
            component.find(ShoppingList).simulate('addItem', {
                name: 'Bananas',
            });

            await props.addItemType;

            // then
            expect(props.addItemType).toHaveBeenCalledWith({
                token: props.token,
                name: 'Bananas',
            });
            expect(props.getItemTypes).toHaveBeenCalledWith(props.token);
            expect(props.addShoppingListItem).toHaveBeenCalledWith({
                token: props.token,
                listId: props.shoppingList.id,
                itemTypeId: newItemTypeId,
                quantity: 1,
            });
        });

        describe('existing item type', () => {
            it('adds item with quantity 1 if item of type not already in list', () => {
                // given
                component.setProps({
                    shoppingList: {
                        ...props.shoppingList,
                        items: [],
                    },
                });

                // when
                component.find(ShoppingList).simulate('addItem', itemType);

                // then
                expect(props.addShoppingListItem).toHaveBeenCalledWith({
                    token: props.token,
                    listId: props.shoppingList.id,
                    itemTypeId: itemType.id,
                    quantity: 1,
                });
            });

            it('increases quantity by 1 if item of type already in list', () => {
                // given
                component.setProps({
                    shoppingList: {
                        ...props.shoppingList,
                        items: [item],
                    },
                });

                // when
                component.find(ShoppingList).simulate('addItem', itemType);

                // then
                expect(props.updateShoppingListItem).toHaveBeenCalledWith({
                    token: props.token,
                    listId: props.shoppingList.id,
                    itemId: item.id,
                    quantity: 2,
                    inCart: false,
                });
            });
        });
    });

    describe('cart', () => {
        it('add item to cart', () => {
            // when
            component.find(ShoppingList).simulate('toggleItemInCart', item, true);

            // then
            expect(props.updateShoppingListItem).toHaveBeenCalledWith({
                token: props.token,
                listId: props.shoppingList.id,
                itemId: item.id,
                quantity: item.quantity,
                inCart: true,
            });
        });

        it('remove item from cart', () => {
            // when
            component.find(ShoppingList).simulate('toggleItemInCart', item, false);

            // then
            expect(props.updateShoppingListItem).toHaveBeenCalledWith({
                token: props.token,
                listId: props.shoppingList.id,
                itemId: item.id,
                quantity: item.quantity,
                inCart: false,
            });
        });
    });

    describe('update item quantity', () => {
        it('sets new quantity when greater than zero', () => {
            // when
            component.find(ShoppingList).simulate('updateItemQuantity', item, 5);

            // then
            expect(props.updateShoppingListItem).toHaveBeenCalledWith({
                token: props.token,
                listId: props.shoppingList.id,
                itemId: item.id,
                quantity: 5,
                inCart: item.inCart,
            });
        });

        it('deletes item when new quantity less than or equal to zero', () => {
            // when
            component.find(ShoppingList).simulate('updateItemQuantity', item, 0);

            // then
            expect(props.deleteShoppingListItem).toHaveBeenCalledWith({
                token: props.token,
                listId: props.shoppingList.id,
                itemId: item.id,
            });
        });
    });
});

it('empty cart callback', () => {
    // when
    component.find(ShoppingList).simulate('emptyCart');

    // then
    expect(props.emptyCart).toHaveBeenCalledWith({
        token: props.token,
        listId: props.shoppingList.id,
    });
});

it('delete shopping list callback', () => {
    // when
    component.find(ShoppingList).simulate('delete');

    // then
    expect(props.deleteShoppingList).toHaveBeenCalledWith({
        token: props.token,
        listId: props.shoppingList.id,
    });
});
