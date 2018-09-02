import PropTypes from 'prop-types';

export const UserType = PropTypes.shape({
    id: PropTypes.string.isRequired,
    username: PropTypes.string.isRequired,
    role: PropTypes.oneOf(['USER', 'ADMIN']),
});

export const ItemTypeType = PropTypes.shape({
    id: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired,
});

export const ShoppingListItemType = PropTypes.shape({
    id: PropTypes.string.isRequired,
    itemType: ItemTypeType.isRequired,
    quantity: PropTypes.number.isRequired,
    inCart: PropTypes.bool.isRequired,
});

export const ShoppingListType = PropTypes.shape({
    id: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired,
    items: PropTypes.arrayOf(ShoppingListItemType).isRequired,
    meta: PropTypes.shape({
        loaded: PropTypes.bool.isRequired,
        fetching: PropTypes.bool.isRequired,
        error: PropTypes.array,
    }).isRequired,
});
