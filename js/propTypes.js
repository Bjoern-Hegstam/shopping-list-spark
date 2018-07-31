import PropTypes from 'prop-types';

export const UserType = PropTypes.shape({
    id: PropTypes.number.isRequired,
    username: PropTypes.string.isRequired,
    role: PropTypes.oneOf(['USER', 'ADMIN'])
});

export const ShoppingListType = PropTypes.shape({
    id: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired,
    items: PropTypes.arrayOf(PropTypes.shape({
        id: PropTypes.string.isRequired,
        itemType: PropTypes.shape({
            id: PropTypes.string.isRequired,
            name: PropTypes.string.isRequired
        }).isRequired,
        quantity: PropTypes.number.isRequired,
        inCart: PropTypes.bool.isRequired
    })).isRequired
});

export const ItemTypeType = PropTypes.shape({
    id: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired
});
