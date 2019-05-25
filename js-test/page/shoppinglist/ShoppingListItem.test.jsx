import ShoppingListItem from '../../../js/page/shoppinglist/ShoppingListItem';
import { getItemTypeByName } from '../../fixtures/itemTypes';
import { setupComponent } from '../../util';
import ShoppingListItemSubPanel from '../../../js/page/shoppinglist/ShoppingListItemSubPanel';

const item = {
  id: 'fc30a4d5-6bc8-4448-970d-dd01597e22cb',
  itemType: getItemTypeByName('Apples'),
  quantity: 3,
  inCart: false,
};

function setup(optProps) {
  return setupComponent(
    ShoppingListItem,
    {
      item,

      onToggleInCart: jest.fn(),
      onUpdateQuantity: jest.fn(),
    },
    optProps,
  );
}

let component;
let props;

beforeEach(() => {
  ({ component, props } = setup());
});

it('initial render', () => {
  expect(component.find('.shopping-list-item__main__quantity').text()).toBe(`${item.quantity}`);
  expect(component.find('.shopping-list-item__main__name').text()).toBe(item.itemType.name);

  expect(component.find(ShoppingListItemSubPanel)).toHaveLength(0);
});

describe('sub panel', () => {
  it('shows sub panel when quantity clicked', () => {
    component.find('.shopping-list-item__main__quantity').simulate('click');

    expect(component.find(ShoppingListItemSubPanel)).toHaveLength(1);
  });

  it('shows sub panel when name clicked', () => {
    component.find('.shopping-list-item__main__name').simulate('click');

    expect(component.find(ShoppingListItemSubPanel)).toHaveLength(1);
  });

  it('onIncrementClick', () => {
    component.find('.shopping-list-item__main__name').simulate('click');

    component.find(ShoppingListItemSubPanel).simulate('incrementClick');

    expect(props.onUpdateQuantity).toHaveBeenCalledWith({ item, newQuantity: item.quantity + 1 });
  });

  it('onDecrementClick', () => {
    component.find('.shopping-list-item__main__name').simulate('click');

    component.find(ShoppingListItemSubPanel).simulate('decrementClick');

    expect(props.onUpdateQuantity).toHaveBeenCalledWith({ item, newQuantity: item.quantity - 1 });
  });
});

describe('cart management', () => {
  it('renders when item not in cart', () => {
    expect(component.find('.shopping-list-item--in-cart')).toHaveLength(0);
  });

  it('renders when item is in cart', () => {
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

  it('click cart button when item not in cart', () => {
    // given
    const notInCartItem = {
      ...item,
      inCart: false,
    };
    component.setProps({
      item: notInCartItem,
    });

    // when
    component.find('.shopping-list-item__main__cart-button').simulate('click');

    // then
    expect(props.onToggleInCart).toHaveBeenCalledWith({ item: notInCartItem, newInCart: true });
  });

  it('click cart button when item in cart', () => {
    // given
    const inCartItem = {
      ...item,
      inCart: true,
    };
    component.setProps({
      item: inCartItem,
    });

    // when
    component.find('.shopping-list-item__main__cart-button').simulate('click');

    // then
    expect(props.onToggleInCart).toHaveBeenCalledWith({ item: inCartItem, newInCart: false });
  });
});
