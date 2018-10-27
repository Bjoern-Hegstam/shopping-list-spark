import ShoppingList from '../../../js/page/shoppinglist/ShoppingList';
import { emptyShoppingList, shoppingListWithEmptyCart, shoppingListWithItemsInCart } from '../../fixtures/shoppingList';
import { itemTypes } from '../../fixtures/itemTypes';
import ShoppingListItem from '../../../js/page/shoppinglist/ShoppingListItem';
import AddShoppingListItemInput from '../../../js/page/shoppinglist/AddShoppingListItemInput';
import { setupComponent } from '../../util';

function setup(optProps) {
    return setupComponent(
        ShoppingList,
        {
            shoppingList: emptyShoppingList,
            itemTypes,
            isEditing: false,

            onStartEdit: jest.fn(),
            onCancelEdit: jest.fn(),
            onAddItem: jest.fn(),
            onToggleItemInCart: jest.fn(),
            onUpdateItemQuantity: jest.fn(),
            onEmptyCart: jest.fn(),
            onChangeName: jest.fn(),
            onDelete: jest.fn(),
        },
        optProps,
    );
}

let component;
let props;

beforeEach(() => {
    ({ component, props } = setup());
});

it('initial state', () => {
    expect(component.state('name')).toBe('');
});

describe('initial render', () => {
    it('empty shopping list', () => {
        // given
        component.setProps({ shoppingList: emptyShoppingList });

        // then
        expect(component.find('.shopping-list__name').text()).toBe(emptyShoppingList.name);
        expect(component.find('.shopping-list__name--edit')).toHaveLength(0);

        expect(component.find('.shopping-list__button').text()).toBe('Delete');
        expect(component.find('.shopping-list__button').prop('disabled')).toBeFalsy();

        expect(component.find(ShoppingListItem)).toHaveLength(0);
    });

    it('shopping list with items, cart is empty', () => {
        // given
        component.setProps({ shoppingList: shoppingListWithEmptyCart });

        // then
        expect(component.find('.shopping-list__name').text()).toBe(shoppingListWithEmptyCart.name);
        expect(component.find('.shopping-list__name--edit')).toHaveLength(0);


        expect(component.find('.shopping-list__button').text()).toBe('Empty Cart');
        expect(component.find('.shopping-list__button').prop('disabled')).toBeTruthy();

        expect(component.find(ShoppingListItem)).toHaveLength(shoppingListWithEmptyCart.items.length);
    });

    it('shopping list with items and items in cart', () => {
        // given
        component.setProps({ shoppingList: shoppingListWithItemsInCart });

        // then
        expect(component.find('.shopping-list__name').text()).toBe(shoppingListWithItemsInCart.name);
        expect(component.find('.shopping-list__name--edit')).toHaveLength(0);

        expect(component.find('.shopping-list__button').text()).toBe('Empty Cart');
        expect(component.find('.shopping-list__button').prop('disabled')).toBeFalsy();

        expect(component.find(ShoppingListItem)).toHaveLength(shoppingListWithItemsInCart.items.length);
    });
});

it('starts editing when name clicked', () => {
    // when
    component.find('.shopping-list__name').simulate('click');

    // then
    expect(component.state('name')).toBe(emptyShoppingList.name);
    expect(props.onStartEdit).toHaveBeenCalledTimes(1);
});

describe('isEditing', () => {
    beforeEach(() => {
        component.setProps({ isEditing: true });
    });

    it('initial render', () => {
        expect(component.find('span.shopping-list__name')).toHaveLength(0);
        expect(component.find('input.shopping-list__name--edit')).toHaveLength(1);
    });

    it('updates state when name changed', () => {
        // when
        component
            .find('.shopping-list__name--edit')
            .simulate('change', { target: { value: 'Edited name' } });

        // then
        expect(component.state('name')).toBe('Edited name');
    });

    it('calls callback with new name on enter', () => {
        // given
        component.setState({ name: 'Edited name' });

        // when
        component
            .find('.shopping-list__name--edit')
            .simulate('keyDown', { key: 'Enter' });

        // then
        expect(props.onChangeName).toHaveBeenCalledWith('Edited name');
    });

    it('cancels edit onBlur', () => {
        // when
        component.find('.shopping-list__name--edit').simulate('blur');

        // then
        expect(props.onCancelEdit).toHaveBeenCalledTimes(1);
    });

    it('cancels edit on escape', () => {
        // given
        component.setState({ name: 'Edited name' });

        // when
        component
            .find('.shopping-list__name--edit')
            .simulate('keyDown', { key: 'Escape' });

        // then
        expect(props.onCancelEdit).toHaveBeenCalledTimes(1);
        expect(component.state('name')).toBe(emptyShoppingList.name);
    });
});

it('invokes onEmptyCart when empty cart button clicked', () => {
    // given
    component.setProps({ shoppingList: shoppingListWithItemsInCart });

    // when
    component
        .find('.shopping-list__button')
        .simulate('click');

    // then
    expect(props.onEmptyCart).toHaveBeenCalledTimes(1);
});

it('invokes onDelete when delete button clicked', () => {
    // given
    component.setProps({ shoppingList: emptyShoppingList });

    // when
    component
        .find('.shopping-list__button')
        .simulate('click');

    // then
    expect(props.onDelete).toHaveBeenCalledTimes(1);
});

describe('ShoppingListItem', () => {
    it('renders items', () => {
        // given
        component.setProps({ shoppingList: shoppingListWithEmptyCart });

        // then
        const items = component.find(ShoppingListItem);
        expect(items.at(0).key()).toBe(shoppingListWithEmptyCart.items[0].id);
        expect(items.at(0).prop('item')).toBe(shoppingListWithEmptyCart.items[0]);

        expect(items.at(1).key()).toBe(shoppingListWithEmptyCart.items[1].id);
        expect(items.at(1).prop('item')).toBe(shoppingListWithEmptyCart.items[1]);

        expect(items.at(2).key()).toBe(shoppingListWithEmptyCart.items[2].id);
        expect(items.at(2).prop('item')).toBe(shoppingListWithEmptyCart.items[2]);
    });

    it('calls onToggleItemInCart', () => {
        // given
        component.setProps({ shoppingList: shoppingListWithEmptyCart });

        // when
        const items = component
            .find(ShoppingListItem);
        items
            .first()
            .simulate('toggleInCart');

        // then
        expect(props.onToggleItemInCart).toHaveBeenCalledTimes(1);
    });

    it('calls onUpdateItemQuantity', () => {
        // given
        component.setProps({ shoppingList: shoppingListWithEmptyCart });

        // when
        component
            .find(ShoppingListItem)
            .first()
            .simulate('updateQuantity');

        // then
        expect(props.onUpdateItemQuantity).toHaveBeenCalledTimes(1);
    });
});

describe('AddShoppingListItemInput', () => {
    it('renders', () => {
        const addItemInput = component.find(AddShoppingListItemInput);
        expect(addItemInput.prop('itemTypes')).toBe(itemTypes);
    });

    it('onAddItem', () => {
        // when
        component.find(AddShoppingListItemInput).simulate('addItem');

        // then
        expect(props.onAddItem).toHaveBeenCalledTimes(1);
    });
});
