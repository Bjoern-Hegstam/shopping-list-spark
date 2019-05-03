import React from 'react';
import * as PropTypes from 'prop-types';
import { CSSTransition, TransitionGroup } from 'react-transition-group';
import { ItemTypeType, ShoppingListType } from '../../propTypes';
import ShoppingListItem from './ShoppingListItem';
import AddShoppingListItemInput from './AddShoppingListItemInput';

export default class ShoppingList extends React.Component {
    static propTypes = {
        shoppingList: ShoppingListType.isRequired,
        itemTypes: PropTypes.arrayOf(ItemTypeType).isRequired,
        isEditing: PropTypes.bool.isRequired,

        onStartEdit: PropTypes.func.isRequired,
        onChangeName: PropTypes.func.isRequired,
        onCancelEdit: PropTypes.func.isRequired,

        onAddItem: PropTypes.func.isRequired,
        onToggleItemInCart: PropTypes.func.isRequired,
        onUpdateItemQuantity: PropTypes.func.isRequired,
        onEmptyCart: PropTypes.func.isRequired,
        onDelete: PropTypes.func.isRequired,
    };

    constructor(props) {
        super(props);
        this.state = {
            name: '',
        };
        this.inputRef = React.createRef();
    }

    componentDidUpdate(prevProps) {
        if (!prevProps.isEditing && this.props.isEditing && this.inputRef.current) {
            this.inputRef.current.focus();
        }
    }

    handleStartEdit = () => {
        this.setState({ name: this.props.shoppingList.name }, () => this.props.onStartEdit());
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
        const {
            shoppingList,
            itemTypes,
            isEditing,
            onAddItem,
            onToggleItemInCart,
            onUpdateItemQuantity,
            onEmptyCart,
            onDelete,
        } = this.props;
        const { name } = this.state;

        const hasItems = shoppingList.items.length > 0;
        const hasItemsInCart = shoppingList.items.some(item => item.inCart);

        return (
            <div className="shopping-list">
                {!isEditing && (
                    <span
                        className="shopping-list__name"
                        onClick={this.handleStartEdit}
                    >
                        {shoppingList.name}
                    </span>
                )}
                {isEditing && (
                    <input
                        ref={this.inputRef}
                        className="shopping-list__name shopping-list__name--edit"
                        value={name}
                        onChange={this.handleNameChange}
                        onKeyDown={this.handleNameInputKeyDown}
                        onBlur={this.props.onCancelEdit}
                        size={1} // Prevents input from getting too wide
                    />
                )}
                {hasItems && (
                    <button
                        type="button"
                        className="shopping-list__button"
                        onClick={onEmptyCart}
                        disabled={!hasItemsInCart}
                    >
                        Empty Cart
                    </button>
                )}
                {!hasItems && (
                    <button
                        type="button"
                        className="shopping-list__button"
                        onClick={onDelete}
                        disabled={hasItems}
                    >
                        Delete
                    </button>
                )}
                <div className="shopping-list__items">
                    <TransitionGroup>
                        {shoppingList.items.map(item => (
                            <CSSTransition
                                key={item.id}
                                classNames="shopping-list-item"
                                timeout={{ enter: 500 }}
                                exit={false}
                            >
                                <ShoppingListItem
                                    key={item.id}
                                    item={item}
                                    onToggleInCart={onToggleItemInCart}
                                    onUpdateQuantity={onUpdateItemQuantity}
                                />
                            </CSSTransition>
                        ))}
                    </TransitionGroup>
                </div>
                <div className="shopping-list__add-item">
                    <AddShoppingListItemInput itemTypes={itemTypes} onAddItem={onAddItem} />
                </div>
            </div>
        );
    }
}
