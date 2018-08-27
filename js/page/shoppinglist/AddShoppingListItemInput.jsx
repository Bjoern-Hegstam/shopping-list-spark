import React from "react";
import PropTypes from 'prop-types';
import { ItemTypeType } from "../../propTypes";

import './AddShoppingListItemInput.scss';

export default class AddShoppingListItemInput extends React.Component {
    static propTypes = {
        itemTypes: PropTypes.arrayOf(ItemTypeType).isRequired,
        onAddItem: PropTypes.func.isRequired,
    };

    state = {
        nameInput: '',
        listedItemTypes: [],
    };

    handleAddItemInputChange = (e) => {
        const nameInput = e.target.value;
        this.setState({
            nameInput,
            listItemTypes: this.getItemTypeSublist(nameInput)
        });
    };

    getItemTypeSublist = (name) => {
        return this
            .props
            .itemTypes
            .filter(itemType => itemType.name.toLowerCase().includes(name.toLowerCase()))
            .slice(0, 5);
    };

    handleSubmit = () => {
        const { itemTypes, onAddItem } = this.props;
        const { nameInput } = this.state;
        const selectedItemType = itemTypes.find(type => type.name === nameInput);
        if (selectedItemType) {
            onAddItem({ ...selectedItemType });
        } else {
            onAddItem({ name: nameInput });
        }
        this.setState({ nameInput: '' });
    };

    render() {
        const { nameInput } = this.state;

        return (
            <form onSubmit={this.handleSubmit}>
                <input
                    className="shopping-list__add-item__input"
                    list="shopping-list__add-item__datalist"
                    value={nameInput}
                    placeholder="Add item"
                    onChange={this.handleAddItemInputChange}
                />
                <datalist id="shopping-list__add-item__datalist">
                    {this.getItemTypeSublist(nameInput).map(itemType => (
                        <option key={itemType.id}>{itemType.name}</option>
                    ))}
                </datalist>
            </form>
        );
    }
}