import React from 'react';
import PropTypes from 'prop-types';
import AppLayout from "../../components/AppLayout";
import {connect} from "react-redux";
import {ItemTypeType, ShoppingListType} from "../../propTypes";
import {
    addShoppingListItem,
    deleteShoppingListItem,
    emptyCart,
    getShoppingList,
    updateShoppingListItem
} from "../../actions/ShoppingListActions";
import {getItemTypes} from "../../actions/ItemTypeActions";

import './ShoppingListPage.scss'
import ShoppingList from "./ShoppingList";

export class ShoppingListPage extends React.Component {
    static propTypes = {
        token: PropTypes.string.isRequired,
        match: PropTypes.object.isRequired,

        shoppingList: ShoppingListType,
        itemTypes: PropTypes.arrayOf(ItemTypeType),

        getShoppingList: PropTypes.func.isRequired,
        getItemTypes: PropTypes.func.isRequired,

        addShoppingListItem: PropTypes.func.isRequired,
        addingShoppingListItem: PropTypes.bool.isRequired,
        errorAddShoppingListItem: PropTypes.object,

        updateShoppingListItem: PropTypes.func.isRequired,
        updatingShoppingListItem: PropTypes.bool.isRequired,
        errorUpdateShoppingListItem: PropTypes.object,

        deleteShoppingListItem: PropTypes.func.isRequired,
        deletingShoppingListItem: PropTypes.bool.isRequired,
        errorDeleteShoppingListItem: PropTypes.object,

        emptyCart: PropTypes.func.isRequired,
        emptyingCart: PropTypes.bool.isRequired,
        errorEmptyCart: PropTypes.object
    };

    static defaultProps = {
        shoppingList: undefined,
        itemTypes: []
    };

    componentDidMount() {
        this.props.getShoppingList({
            token: this.props.token,
            id: this.props.match.params.listId
        });

        this.props.getItemTypes(this.props.token);
    }

    componentDidUpdate(prevProps) {
        const shoppingListItemAdded = prevProps.addingShoppingListItem && !this.props.addingShoppingListItem;
        const shoppingListItemUpdated = prevProps.updatingShoppingListItem && !this.props.updatingShoppingListItem;
        const shoppingListItemDeleted = prevProps.deletingShoppingListItem && !this.props.deletingShoppingListItem;
        const cartEmptied = prevProps.emptyingCart && !this.props.emptyingCart;

        const cartUpdated = shoppingListItemAdded || shoppingListItemUpdated || shoppingListItemDeleted || cartEmptied;
        if (cartUpdated) {
            const {token, shoppingList} = this.props;
            this.props.getShoppingList({token, id: shoppingList.id});
        }
    }

    handleToggleItemInCart = (item, newInCart) => {
        const {token, shoppingList} = this.props;

        this.props.updateShoppingListItem({
            token,
            listId: shoppingList.id,
            itemId: item.id,
            quantity: item.quantity,
            inCart: newInCart
        });
    };

    handleUpdateItemQuantity = (item, newQuantity) => {
        const {token, shoppingList} = this.props;

        if (newQuantity > 0) {
            this.props.updateShoppingListItem({
                token,
                listId: shoppingList.id,
                itemId: item.id,
                quantity: newQuantity,
                inCart: item.inCart
            });
        } else {
            this.props.deleteShoppingListItem({token, listId: shoppingList.id, itemId: item.id});
        }
    };

    handleEmptyCart = () => {
        const {token, shoppingList} = this.props;

        this.props.emptyCart({token, listId: shoppingList.id});
    };

    render() {
        const {shoppingList} = this.props;

        if (!shoppingList) {
            return (
                <AppLayout>Loading...</AppLayout>
            )
        }

        return (
            <AppLayout>
                <ShoppingList
                    shoppingList={shoppingList}
                    onToggleItemInCart={this.handleToggleItemInCart}
                    onUpdateItemQuantity={this.handleUpdateItemQuantity}
                    onEmptyCart={this.handleEmptyCart}
                />
            </AppLayout>
        );
    }
}

export default connect(
    (store, ownProps) => {
        if (!ownProps.match) {
            return {};
        }

        const {listId} = ownProps.match.params;
        const {shoppingLists} = store.shoppingList;
        const {itemTypes} = store.itemType;

        return {
            token: store.auth.token,
            ...store.shoppingList,
            shoppingList: listId ? shoppingLists[listId] : undefined,
            itemTypes
        };
    },
    dispatch => ({
        getShoppingList: args => dispatch(getShoppingList(args)),
        getItemTypes: args => dispatch(getItemTypes(args)),
        addShoppingListItem: args => dispatch(addShoppingListItem(args)),
        updateShoppingListItem: args => dispatch(updateShoppingListItem(args)),
        deleteShoppingListItem: args => dispatch(deleteShoppingListItem(args)),
        emptyCart: args => dispatch(emptyCart(args)),
    })
)(ShoppingListPage);