import PropTypes from 'prop-types';

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