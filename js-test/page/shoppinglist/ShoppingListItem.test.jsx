import ShoppingListItem from '../../../js/page/shoppinglist/ShoppingListItem';
import { getItemTypeByName } from '../../fixtures/itemTypes';
import { setupComponent } from '../../util';

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
  expect(component.find('.shopping-list-item__quantity').text()).toBe(`${item.quantity}`);
  expect(component.find('.shopping-list-item__name').text()).toBe(item.itemType.name);

  expect(component.find('.shopping-list-item__button--incr').find('span').text()).toBe('+');
  expect(component.find('.shopping-list-item__button--decr').find('span').text()).toBe('-');
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

it('invokes onUpdateQuantity when increment button clicked', () => {
  // when
  component.find('.shopping-list-item__button--incr').simulate('click', { stopPropagation: jest.fn() });

  // then
  expect(props.onUpdateQuantity).toHaveBeenCalledWith(props.item, props.item.quantity + 1);
});

it('invokes onUpdateQuantity when decrement button clicked', () => {
  // when
  component.find('.shopping-list-item__button--decr').simulate('click', { stopPropagation: jest.fn() });

  // then
  expect(props.onUpdateQuantity).toHaveBeenCalledWith(props.item, props.item.quantity - 1);
});

it('invokes onToggleInCart when item clicked', () => {
  // when
  component.find('.shopping-list-item').simulate('click');

  // then
  expect(props.onToggleInCart).toHaveBeenCalledWith(item, !item.inCart);
});
