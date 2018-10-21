import { setupComponent } from '../../util';
import ItemType from '../../../js/page/itemtypes/ItemType';

function setup(optProps) {
    return setupComponent(
        ItemType,
        {
            id: '835396a8-65d4-4c39-b673-8cfc2cef7301',
            name: 'foo',

            onDelete: jest.fn(),
        },
        optProps,
    );
}

let component;
let props;

beforeEach(() => {
    ({ component, props } = setup());
});

it('onDelete', () => {
    // when
    component.find('.item-type__delete-button').simulate('click');

    // then
    expect(props.onDelete).toHaveBeenCalledWith(props.id);
});
