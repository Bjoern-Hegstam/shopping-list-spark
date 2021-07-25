import React from 'react';
import * as PropTypes from 'prop-types';
import classNames from 'classnames';
import { MdAddShoppingCart, MdRemoveShoppingCart } from 'react-icons/md';

import { ShoppingListItemType } from '../../propTypes';
import ShoppingListItemSubPanel from './ShoppingListItemSubPanel';

export default function ShoppingListItem({ item, showExpandedView, onClick, onToggleInCart, onUpdateQuantity }) {
  const itemClassNames = classNames('shopping-list-item', {
    ' shopping-list-item--in-cart': item.inCart,
  });

  return (
    <div className={itemClassNames}>
      <div className="shopping-list-item__main">
        <span
          className="shopping-list-item__main__quantity"
          onClick={() => onClick(item.id)}
        >
          {item.quantity}
        </span>
        <span
          className="shopping-list-item__main__name"
          onClick={() => onClick(item.id)}
        >
          {item.itemType.name}
        </span>
        <span
          className="shopping-list-item__main__cart-button"
          onClick={() => onToggleInCart({ item, newInCart: !item.inCart })}
        >
          {item.inCart ? <MdRemoveShoppingCart /> : <MdAddShoppingCart />}
        </span>
      </div>
      {showExpandedView && (
        <ShoppingListItemSubPanel
          onIncrementClick={() => onUpdateQuantity({ item, newQuantity: item.quantity + 1 })}
          onDecrementClick={() => onUpdateQuantity({ item, newQuantity: item.quantity - 1 })}
        />
      )}
    </div>
  );
}

ShoppingListItem.propTypes = {
  item: ShoppingListItemType.isRequired,
  showExpandedView: PropTypes.bool.isRequired,
  onClick: PropTypes.func.isRequired,
  onToggleInCart: PropTypes.func.isRequired,
  onUpdateQuantity: PropTypes.func.isRequired,
};
