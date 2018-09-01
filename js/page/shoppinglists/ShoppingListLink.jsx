import React from 'react';
import PropTypes from 'prop-types';

export default class ShoppingListLink extends React.PureComponent {
    static propTypes = {
        id: PropTypes.string.isRequired,
        name: PropTypes.string.isRequired,
        onClick: PropTypes.func.isRequired,
    };

    handleClick = () => {
        this.props.onClick(this.props.id);
    };

    render() {
        const { id, name } = this.props;

        return (
            <div className="shopping-list-link" key={id} onClick={this.handleClick}>
                <span className="shopping-list-link--name">{name}</span>
            </div>
        );
    }
}
