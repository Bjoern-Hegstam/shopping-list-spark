import { App } from '../js/App';
import { setupComponent } from './util';

function setup(optProps) {
  return setupComponent(
    App,
    {
      location: {
        pathname: '/',
      },
      history: {
        push: jest.fn(),
      },

      loggingIn: false,
    },
    optProps,
  );
}

let component;
let props;

beforeEach(() => {
  ({ component, props } = setup());
});


it('redirects to referer on login', () => {
  // given
  component.setProps({
    loggingIn: true,
    location: {
      pathname: '/',
      state: {
        referer: 'referer-path',
      },
    },
  });

  // when
  component.setProps({ loggingIn: false });

  // then
  expect(props.history.push).toHaveBeenCalledWith('referer-path');
});

it('does not redirect on login when referer is /', () => {
  // given
  component.setProps({
    loggingIn: true,
    location: {
      pathname: '/',
      state: {
        referer: '/',
      },
    },
  });

  // when
  component.setProps({ loggingIn: false });

  // then
  expect(props.history.push).toHaveBeenCalledTimes(0);
});

it('does not redirect on login when no referer set', () => {
  // given
  component.setProps({
    loggingIn: true,
    location: {
      pathname: '/',
    },
  });

  // when
  component.setProps({ loggingIn: false });

  // then
  expect(props.history.push).toHaveBeenCalledTimes(0);
});
