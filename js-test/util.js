import { shallow } from 'enzyme/build';
import React from 'react';

export function setupComponent(Component, defaultProps, optProps) {
    const props = {
        ...defaultProps,
        ...optProps,
    };

    const component = shallow(<Component {...props} />);

    return { component, props };
}
