import React from 'react';
import PropTypes from 'prop-types';
import {ShoppingListType} from "../../propTypes";
import ShoppingListItem from "./ShoppingListItem";

export default class ShoppingList extends React.Component {
    static propTypes = {
        shoppingList: ShoppingListType.isRequired,

        onToggleItemInCart: PropTypes.func,
        onUpdateItemQuantity: PropTypes.func,
        onEmptyCart: PropTypes.func
    };

    render() {
        const {shoppingList, onEmptyCart, onToggleItemInCart, onUpdateItemQuantity} = this.props;

        return (
            <div className='shopping-list'>
                <div className='shopping-list__header'>
                    <span className='shopping-list__header__name'>{shoppingList.name}</span>
                    <div className='shopping-list__header__empty-cart-button' onClick={onEmptyCart}>Empty Cart</div>
                </div>
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