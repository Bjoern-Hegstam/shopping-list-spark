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
  const { component, props } = setupComponent(
    ShoppingListPage,
    {
      token: 'token',
      match: { params: { listId } },
      history: { push: jest.fn() },

      shoppingList: {
        id: listId,
        name: 'Foo',
        itemTypes: [itemType],
        items: [item],
      },

      getShoppingList: jest.fn(),
      fetchingShoppingList: false,

      updateShoppingList: jest.fn(),
      updatingShoppingList: false,

      deleteShoppingList: jest.fn(),
      deletingShoppingList: false,

      addShoppingListItem: jest.fn(),
      updateShoppingListItem: jest.fn(),
      deleteShoppingListItem: jest.fn(),

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

it('fetches shopping list on mount', () => {
  expect(props.getShoppingList).toHaveBeenCalledWith({
    token: props.token,
    id: props.shoppingList.id,
  });
});

describe('re-fetch shopping list', () => {
  it('when list updated', () => {
    // given
    component.setProps({ updatingShoppingList: true });
    props.getShoppingList.mockReset();

    // when
    component.setProps({ updatingShoppingList: false });

    // then
    expect(props.getShoppingList).toHaveBeenCalledWith({
      token: props.token,
      id: props.shoppingList.id,
    });
  });

  it('when cart emptied', () => {
    // given
    component.setProps({ emptyingCart: true });
    props.getShoppingList.mockReset();

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
  it('add item - by itemTypeId', () => {
    // when
    const itemTypeName = 'Apples';
    component.find(ShoppingList).simulate('addItem', { name: itemTypeName });

    // then
    expect(props.addShoppingListItem).toHaveBeenCalledWith({
      requestId: expect.anything(),
      token: props.token,
      listId: props.shoppingList.id,
      quantity: 1,
      itemTypeName,
    });
  });

  it('add item - by itemTypeName', () => {
    // when
    const itemTypeId = 'test-item-type-id';
    component.find(ShoppingList).simulate('addItem', { id: itemTypeId });

    // then
    expect(props.addShoppingListItem).toHaveBeenCalledWith({
      requestId: expect.anything(),
      token: props.token,
      listId: props.shoppingList.id,
      quantity: 1,
      itemTypeId,
    });
  });

  describe('cart', () => {
    it('add item to cart', () => {
      // when
      component.find(ShoppingList).simulate('toggleItemInCart', { item, newInCart: true });

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
      component.find(ShoppingList).simulate('toggleItemInCart', { item, newInCart: false });

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
      component.find(ShoppingList).simulate('updateItemQuantity', { item, newQuantity: 5 });

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
      component.find(ShoppingList).simulate('updateItemQuantity', { item, newQuantity: 0 });

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
