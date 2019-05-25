import React, { useState } from 'react';
import * as PropTypes from 'prop-types';
import classNames from 'classnames';
import { MdAddShoppingCart, MdRemoveShoppingCart } from 'react-icons/md';

import { ShoppingListItemType } from '../../propTypes';
import ShoppingListItemSubPanel from './ShoppingListItemSubPanel';

export default function ShoppingListItem({ item, onToggleInCart, onUpdateQuantity }) {
  const [showSubPanel, setShowSubPanel] = useState(false);

  const itemClassNames = classNames('shopping-list-item', {
    ' shopping-list-item--in-cart': item.inCart,
  });

  return (
    <div className={itemClassNames}>
      <div className="shopping-list-item__main">
        <span
          className="shopping-list-item__main__quantity"
          onClick={() => setShowSubPanel(!showSubPanel)}
        >
          {item.quantity}
        </span>
        <span
          className="shopping-list-item__main__name"
          onClick={() => setShowSubPanel(!showSubPanel)}
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
      {showSubPanel && (
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
  onToggleInCart: PropTypes.func.isRequired,
  onUpdateQuantity: PropTypes.func.isRequired,
};
