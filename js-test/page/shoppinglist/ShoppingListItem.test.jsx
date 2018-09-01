import React from 'react';
import { shallow } from 'enzyme';
import ShoppingListItem from '../../../js/page/shoppinglist/ShoppingListItem';
import { getItemTypeByName } from '../../fixtures/itemTypes';

const item = {
    id: 'fc30a4d5-6bc8-4448-970d-dd01597e22cb',
    itemType: getItemTypeByName('Apples'),
    quantity: 3,
    inCart: false,
};

function setup(optProps) {
    const defaultProps = {
        item,

        onToggleInCart: jest.fn(),
        onUpdateQuantity: jest.fn(),
    };

    const props = {
        ...defaultProps,
        ...optProps,
    };

    const component = shallow(<ShoppingListItem {...props} />);

    return { component, props };
}

let component;
let props;

beforeEach(() => {
    ({ component, props } = setup());
});

it('initial render', () => {
    expect(component.find('.shopping-list-item__quantity').text()).toBe(`${item.quantity}`);
    expect(component.find('.shopping-list-item__name').text()).toBe(item.itemType.name);

    expect(component.find('.shopping-list-item__inc-button').find('span').text()).toBe('+');
    expect(component.find('.shopping-list-item__dec-button').find('span').text()).toBe('-');
});

it('item not in cart', () => {
    expect(component.find('.shopping-list-item--in-cart')).toHaveLength(0);
});

it('item is in cart', () => {
    // given
    component.setProps({
        item: {
            ...item,
            inCart: true,
        },
    });

    // then
    expect(component.find('.shopping-list-item--in-cart')).toHaveLength(1);
});

it('invokes callback when increment button clicked', () => {
    // when
    component.find('.shopping-list-item__inc-button').simulate('click', { stopPropagation: jest.fn() });

    // then
    expect(props.onUpdateQuantity).toHaveBeenCalledWith(props.item, props.item.quantity + 1);
});

it('invokes callback when decrement button clicked', () => {
    // when
    component.find('.shopping-list-item__dec-button').simulate('click', { stopPropagation: jest.fn() });

    // then
    expect(props.onUpdateQuantity).toHaveBeenCalledWith(props.item, props.item.quantity - 1);
});

it('invokes onToggleInCart when item clicked', () => {
    // when
    component.find('.shopping-list-item').simulate('click');

    // then
    expect(props.onToggleInCart).toHaveBeenCalledWith(item, !item.inCart);
});
