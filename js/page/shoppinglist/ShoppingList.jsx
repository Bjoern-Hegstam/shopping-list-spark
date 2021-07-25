import React from 'react';
import * as PropTypes from 'prop-types';
import { CSSTransition, TransitionGroup } from 'react-transition-group';
import { ShoppingListType } from '../../propTypes';
import ShoppingListItem from './ShoppingListItem';
import AddShoppingListItemInput from './AddShoppingListItemInput';

export default class ShoppingList extends React.Component {
  static propTypes = {
    shoppingList: ShoppingListType.isRequired,
    isEditing: PropTypes.bool.isRequired,

    onStartEdit: PropTypes.func.isRequired,
    onChangeName: PropTypes.func.isRequired,
    onCancelEdit: PropTypes.func.isRequired,

    onAddItem: PropTypes.func.isRequired,
    onToggleItemInCart: PropTypes.func.isRequired,
    onUpdateItemQuantity: PropTypes.func.isRequired,
    onEmptyCart: PropTypes.func.isRequired,
  };

  constructor(props) {
    super(props);
    this.state = {
      name: '',
      expandedItemId: null,
    };
    this.inputRef = React.createRef();
  }

  componentDidUpdate(prevProps) {
    if (!prevProps.isEditing && this.props.isEditing && this.inputRef.current) {
      this.inputRef.current.focus();
    }
  }

  handleStartEditName = () => {
    this.setState({ name: this.props.shoppingList.name }, () => this.props.onStartEdit());
  };

  handleNameChange = e => {
    this.setState({ name: e.target.value });
  };

  handleNameInputKeyDown = e => {
    if (e.key === 'Enter') {
      this.props.onChangeName(this.state.name);
    } else if (e.key === 'Escape') {
      this.setState({ name: this.props.shoppingList.name });
      this.props.onCancelEdit();
    }
  };

  handleItemClick = itemId => {
    this.setState(prevState => ({
      ...prevState,
      expandedItemId: prevState.expandedItemId !== itemId ? itemId : null,
    }));
  };

  render() {
    const {
      shoppingList,
      isEditing,
      onAddItem,
      onToggleItemInCart,
      onUpdateItemQuantity,
      onEmptyCart,
    } = this.props;
    const { name, expandedItemId } = this.state;

    const hasItemsInCart = shoppingList.items.some(item => item.inCart);

    return (
      <div className="shopping-list">
        {isEditing ? (
          <input
            ref={this.inputRef}
            className="shopping-list__name shopping-list__name--edit"
            value={name}
            onChange={this.handleNameChange}
            onKeyDown={this.handleNameInputKeyDown}
            onBlur={this.props.onCancelEdit}
            size={1} // Prevents input from getting too wide
          />
        ) : (
          <span
            className="shopping-list__name"
            onClick={this.handleStartEditName}
          >
            {shoppingList.name}
          </span>
        )}

        <p className="shopping-list__to-buy-header">To buy</p>

        <div className="shopping-list__add-item">
          <AddShoppingListItemInput itemTypes={shoppingList.itemTypes} onAddItem={onAddItem} />
        </div>

        <div className="shopping-list__items">
          <TransitionGroup>
            {shoppingList
              .items
              .filter(item => !item.inCart)
              .map(item => (
                <CSSTransition
                  key={item.id || item.requestId}
                  classNames="shopping-list-item"
                  timeout={{ enter: 500 }}
                  exit={false}
                >
                  <ShoppingListItem
                    key={item.id}
                    item={item}
                    showExpandedView={item.id === expandedItemId}
                    onClick={this.handleItemClick}
                    onToggleInCart={onToggleItemInCart}
                    onUpdateQuantity={onUpdateItemQuantity}
                  />
                </CSSTransition>
              ))}
          </TransitionGroup>
        </div>

        {hasItemsInCart && (
          <>
            <p className="shopping-list__cart-header">Cart</p>
            <div className="shopping-list__items-in-cart">
              {shoppingList
                .items
                .filter(item => item.inCart)
                .map(item => (
                  <ShoppingListItem
                    key={item.id || item.requestId}
                    item={item}
                    showExpandedView={item.id === expandedItemId}
                    onClick={this.handleItemClick}
                    onToggleInCart={onToggleItemInCart}
                    onUpdateQuantity={onUpdateItemQuantity}
                  />
                ))}
            </div>

            <button
              type="button"
              className="shopping-list__empty-cart-button"
              onClick={onEmptyCart}
              disabled={!hasItemsInCart}
            >
              Empty Cart
            </button>
          </>
        )}
      </div>
    );
  }
}
