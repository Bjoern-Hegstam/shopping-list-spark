import React from 'react';
import { shallow } from 'enzyme';
import AddShoppingListItemInput from '../../../js/page/shoppinglist/AddShoppingListItemInput';

function setup(optProps) {
    const defaultProps = {
        itemTypes: [],
        onAddItem: jest.fn(),
    };

    const props = {
        ...defaultProps,
        ...optProps,
    };

    const component = shallow(<AddShoppingListItemInput {...props} />);

    return { component, props };
}

describe('AddShoppingListItemInput', () => {
    let component;
    let props;

    beforeEach(() => {
        ({ component, props } = setup());
    });

    it('renders empty list of item types', () => {
        component.setProps({ itemTypes: [] });

        const input = component.find('input');
        expect(input.prop('value')).toBe('');
        expect(input.prop('placeholder')).toBe('Add item');
    });
});
