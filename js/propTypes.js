import PropTypes from 'prop-types';

export const UserType = PropTypes.shape({
    id: PropTypes.number.isRequired,
    username: PropTypes.string.isRequired,
    role: PropTypes.oneOf(['USER', 'ADMIN'])
});