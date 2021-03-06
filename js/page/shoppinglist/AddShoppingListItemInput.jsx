import React from 'react';
import * as PropTypes from 'prop-types';
import { ItemTypeType } from '../../propTypes';

export default class AddShoppingListItemInput extends React.Component {
    static propTypes = {
      itemTypes: PropTypes.arrayOf(ItemTypeType).isRequired,
      onAddItem: PropTypes.func.isRequired,
    };

    state = {
      nameInput: '',
    };

    handleAddItemInputChange = e => {
      const nameInput = e.target.value;
      this.setState({
        nameInput,
      });
    };

    getItemTypeSublist = name => this
      .props
      .itemTypes
      .filter(itemType => itemType.name.toLowerCase().includes(name.toLowerCase()))
      .slice(0, 5);

    handleSubmit = () => {
      const { itemTypes, onAddItem } = this.props;
      const { nameInput } = this.state;
      if (nameInput === '') {
        return;
      }

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
