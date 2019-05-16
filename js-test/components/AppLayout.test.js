import { setupComponent } from '../util';
import { AppLayout } from '../../js/components/AppLayout';

function setup(optProps) {
  return setupComponent(
    AppLayout,
    {
      userAuthenticated: false,
    },
    optProps,
  );
}

let component;
let props;

beforeEach(() => {
  ({ component, props } = setup());
});

it('add css class when user authenticated', () => {
  component.setProps({ userAuthenticated: true });

  const appLayoutElement = component.find('.app-layout');
  expect(appLayoutElement.prop('className').includes('user--authenticated')).toBe(true);
});

it('it does not add css class when user not authenticated', () => {
  component.setProps({ userAuthenticated: false });

  const appLayoutElement = component.find('.app-layout');
  expect(appLayoutElement.prop('className').includes('user--authenticated')).toBe(false);
});
