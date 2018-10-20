import React from 'react';
import PropTypes from 'prop-types';
import { FaTrash } from 'react-icons/fa';

export default class ItemType extends React.Component {
    static propTypes = {
        id: PropTypes.string.isRequired,
        name: PropTypes.string.isRequired,

        onDelete: PropTypes.func,
    };

    static defaultProps = {
        onDelete: undefined,
    };

    handleOnDeleteClicked = () => {
        const { onDelete, id } = this.props;

        if (onDelete) {
            onDelete(id);
        }
    };

    render() {
        const { name } = this.props;

        return (
            <div className="item-type">
                <button
                    type="button"
                    className="item-type__delete-button"
                    onClick={this.handleOnDeleteClicked}
                >
                    <FaTrash />
                </button>
                <div className="item-type__name">{name}</div>
            </div>
        );
    }
}
