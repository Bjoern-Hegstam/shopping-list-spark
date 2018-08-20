import React from 'react';
import PropTypes from 'prop-types';
import { ShoppingListType } from "../../propTypes";
import ShoppingListItem from "./ShoppingListItem";
import './ShoppingList.scss';

export default class ShoppingList extends React.Component {
    static propTypes = {
        shoppingList: ShoppingListType.isRequired,
        isEditing: PropTypes.bool.isRequired,

        onStartEdit: PropTypes.func.isRequired,
        onCancelEdit: PropTypes.func.isRequired,
        onToggleItemInCart: PropTypes.func.isRequired,
        onUpdateItemQuantity: PropTypes.func.isRequired,
        onEmptyCart: PropTypes.func.isRequired,
        onChangeName: PropTypes.func.isRequired,
        onDelete: PropTypes.func.isRequired
    };

    constructor(props) {
        super(props);
        this.state = {
            name: ""
        };
        this.inputRef = React.createRef();
    }

    componentDidUpdate(prevProps) {
        if (!prevProps.isEditing && this.props.isEditing) {
            this.inputRef.current.focus();
        }
    }

    handleStartEdit = () => {
        this.setState({ name: this.props.shoppingList.name });
        this.props.onStartEdit();
    };

    handleNameChange = (e) => {
        this.setState({ name: e.target.value });
    };

    handleNameInputKeyDown = (e) => {
        if (e.key === 'Enter') {
            this.props.onChangeName(this.state.name);
        } else if (e.key === 'Escape') {
            this.setState({ name: this.props.shoppingList.name });
            this.props.onCancelEdit();
        }
    };

    render() {
        const { shoppingList, isEditing, onToggleItemInCart, onUpdateItemQuantity, onEmptyCart, onDelete } = this.props;
        const { name } = this.state;

        return (
            <div className='shopping-list'>
                {!isEditing && (
                    <div className='shopping-list__header'>
                        <span className='shopping-list__header__name' onClick={this.handleStartEdit}>{shoppingList.name}</span>
                        <div className='shopping-list__header__empty-cart-button' onClick={onEmptyCart}>Empty Cart</div>
                    </div>
                )}
                {isEditing && (
                    <div className='shopping-list__header--edit'>
                        <input
                            ref={this.inputRef}
                            className='shopping-list__header__name-input'
                            value={name}
                            onChange={this.handleNameChange}
                            onKeyDown={this.handleNameInputKeyDown}
                        />
                        <div className="shopping-list__header__delete-button" onClick={onDelete}>Delete</div>
                    </div>
                )}
                <div className='shopping-list__body'>
                    {shoppingList.items.map(item => <ShoppingListItem
                        key={item.id}
                        item={item}
                        onToggleInCart={onToggleItemInCart}
                        onUpdateQuantity={onUpdateItemQuantity}
                    />)}
                </div>
            </div>
        );
    }
}