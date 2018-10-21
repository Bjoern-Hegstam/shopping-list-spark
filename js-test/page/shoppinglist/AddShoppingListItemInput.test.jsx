import AddShoppingListItemInput from '../../../js/page/shoppinglist/AddShoppingListItemInput';
import { itemTypes } from '../../fixtures/itemTypes';
import { setupComponent } from '../../util';

function setup(optProps) {
    return setupComponent(
        AddShoppingListItemInput,
        {
            itemTypes,
            onAddItem: jest.fn(),
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
    const input = component.find('input');
    expect(input.prop('list')).toBe('shopping-list__add-item__datalist');
    expect(input.prop('value')).toBe('');
    expect(input.prop('placeholder')).toBe('Add item');
});

describe('datalist', () => {
    it('is limited to max five items, when more than five items applicable', () => {
        expect(props.itemTypes.length).toBeGreaterThan(5);

        const options = component.find('datalist#shopping-list__add-item__datalist option');
        expect(options).toHaveLength(5);
    });

    it('is limited based on entered name', () => {
        component.setState({ nameInput: 'e' });
        let options = component.find('datalist#shopping-list__add-item__datalist option');
        expect(options).toHaveLength(3);
        expect(options.at(0).text()).toBe('Apples');
        expect(options.at(1).text()).toBe('Lettuce');
        expect(options.at(2).text()).toBe('Tomatoes');

        component.setState({ nameInput: 'le' });
        options = component.find('datalist#shopping-list__add-item__datalist option');
        expect(options).toHaveLength(2);
        expect(options.at(0).text()).toBe('Apples');
        expect(options.at(1).text()).toBe('Lettuce');

        component.setState({ nameInput: 'app' });
        options = component.find('datalist#shopping-list__add-item__datalist option');
        expect(options).toHaveLength(1);
        expect(options.at(0).text()).toBe('Apples');
    });

    it('is empty when no item types matches entered name', () => {
        component.setState({ nameInput: 'x' });

        const options = component.find('datalist#shopping-list__add-item__datalist option');
        expect(options).toHaveLength(0);
    });
});

it('ignores submit when name is empty', () => {
    // given
    component.setState({ nameInput: '' });

    // when
    component.find('form').simulate('submit');

    // then
    expect(props.onAddItem).toHaveBeenCalledTimes(0);
});

it('calls onAddItem on submit, name does not match existing item type', () => {
    // given
    component.setState({ nameInput: 'xyz' });

    // when
    component.find('form').simulate('submit');

    // then
    expect(props.onAddItem).toHaveBeenCalledWith({ name: 'xyz' });
    expect(component.state('nameInput')).toBe('');
});

it('calls onAddItem on submit, name matches existing item type', () => {
    // given
    component.setState({ nameInput: 'Apples' });

    // when
    component.find('form').simulate('submit');

    // then
    expect(props.onAddItem).toHaveBeenCalledWith(
        {
            id: '29e5f3b8-735e-4eb3-a97d-917abaf14624',
            name: 'Apples',
        },
    );
    expect(component.state('nameInput')).toBe('');
});
