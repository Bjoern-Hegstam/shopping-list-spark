import React from 'react';
import PropTypes from 'prop-types';
import { ShoppingListItemType } from '../../propTypes';

export default class ShoppingListItem extends React.Component {
    static propTypes = {
        item: ShoppingListItemType.isRequired,
        onToggleInCart: PropTypes.func.isRequired,
        onUpdateQuantity: PropTypes.func.isRequired,
    };

    handleClick = () => {
        const { onToggleInCart, item } = this.props;
        onToggleInCart(item, !item.inCart);
    };

    handleIncrementClick = (e) => {
        e.stopPropagation();

        const { onUpdateQuantity, item } = this.props;
        onUpdateQuantity(item, item.quantity + 1);
    };

    handleDecrementClick = (e) => {
        e.stopPropagation();

        const { onUpdateQuantity, item } = this.props;
        onUpdateQuantity(item, item.quantity - 1);
    };

    render() {
        const { item } = this.props;

        let itemClassName = 'shopping-list-item';
        if (item.inCart) {
            itemClassName += ' shopping-list-item--in-cart';
        }

        return (
            <div className={itemClassName} onClick={this.handleClick}>
                <div className="shopping-list-item__info">
                    <span className="shopping-list-item__quantity">{item.quantity}</span>
                    <span className="shopping-list-item__name">{item.itemType.name}</span>
                </div>
                <div className="shopping-list-item__buttons">
                    <div className="shopping-list-item__inc-button" onClick={this.handleIncrementClick}>
                        <span>+</span>
                    </div>
                    <div className="shopping-list-item__dec-button" onClick={this.handleDecrementClick}>
                        <span>-</span>
                    </div>
                </div>
            </div>
        );
    }
}
