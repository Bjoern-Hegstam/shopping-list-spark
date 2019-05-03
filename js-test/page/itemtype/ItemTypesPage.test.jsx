import { setupComponent } from '../../util';
import { ItemTypesPage } from '../../../js/page/itemtypes/ItemTypesPage';
import { itemTypes } from '../../fixtures/itemTypes';
import ItemType from '../../../js/page/itemtypes/ItemType';

function setup(optProps) {
  return setupComponent(
    ItemTypesPage,
    {
      token: 'token',
      itemTypes,

      getItemTypes: jest.fn(),
      deleteItemType: jest.fn(),
    },
    optProps,
  );
}

let component;
let props;

beforeEach(() => {
  ({ component, props } = setup());
});

it('fetches item types on mount', () => {
  expect(props.getItemTypes).toHaveBeenCalledWith(props.token);
});

it('renders list of item types', () => {
  const renderedItemTypes = component.find(ItemType);

  expect(renderedItemTypes).toHaveLength(itemTypes.length);
  renderedItemTypes.forEach((itemType, i) => {
    expect(itemType.key()).toBe(itemTypes[i].id);
    expect(itemType.prop('id')).toBe(itemTypes[i].id);
    expect(itemType.prop('name')).toBe(itemTypes[i].name);
  });
});


it('handles delete of item type', () => {
  // when
  component.find(ItemType).first().simulate('delete', itemTypes[0].id);

  // then
  expect(props.deleteItemType).toHaveBeenCalledWith({ token: props.token, id: itemTypes[0].id });
});
