import * as PropTypes from 'prop-types';
import React from 'react';

export default function ShoppingListItemSubPanel({ onIncrementClick, onDecrementClick }) {
  return (
    <div className="shopping-list-item-sub-panel">
      <div
        className="shopping-list-item-sub-panel__button shopping-list-item-sub-panel__button__incr"
        onClick={onIncrementClick}
      >
        <span>+</span>
      </div>
      <div
        className="shopping-list-item-sub-panel__button shopping-list-item-sub-panel__button__decr"
        onClick={onDecrementClick}
      >
        <span>-</span>
      </div>
    </div>
  );
}

ShoppingListItemSubPanel.propTypes = {
  onIncrementClick: PropTypes.func.isRequired,
  onDecrementClick: PropTypes.func.isRequired,
};
