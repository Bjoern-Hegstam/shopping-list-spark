import React from 'react';
import PropTypes from 'prop-types';

export default class ItemType extends React.Component {
    static propTypes = {
        id: PropTypes.string.isRequired,
        name: PropTypes.string.isRequired,
    };

    render() {
        const { name } = this.props;

        return (
            <div className="item-type">
                <div className="item-type__name">{name}</div>
            </div>
        );
    }
}
