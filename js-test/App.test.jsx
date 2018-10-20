import { shallow } from 'enzyme/build';
import React from 'react';
import { App } from '../js/App';

function setup(optProps) {
    const defaultProps = {
        location: {
            pathname: '/',
        },
        history: {
            push: jest.fn(),
        },

        loggingIn: false,
    };

    const props = {
        ...defaultProps,
        ...optProps,
    };

    const component = shallow(<App {...props} />);

    return { component, props };
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
