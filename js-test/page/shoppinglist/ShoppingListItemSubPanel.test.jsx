import { setupComponent } from '../../util';
import ShoppingListItemSubPanel from '../../../js/page/shoppinglist/ShoppingListItemSubPanel';

function setup(optProps) {
  return setupComponent(
    ShoppingListItemSubPanel,
    {
      onIncrementClick: jest.fn(),
      onDecrementClick: jest.fn(),
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
  expect(component.debug()).toMatchSnapshot();
});

it('calls onIncrementClick', () => {
  component.find('.shopping-list-item-sub-panel__button__incr').simulate('click');

  expect(props.onIncrementClick).toHaveBeenCalledTimes(1);
  expect(props.onDecrementClick).toHaveBeenCalledTimes(0);
});

it('calls onDecrementClick', () => {
  component.find('.shopping-list-item-sub-panel__button__decr').simulate('click');

  expect(props.onIncrementClick).toHaveBeenCalledTimes(0);
  expect(props.onDecrementClick).toHaveBeenCalledTimes(1);
});
