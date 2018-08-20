import React from 'react';
import PropTypes from 'prop-types';
import AppLayout from "../../components/AppLayout";
import { connect } from "react-redux";
import { ItemTypeType, ShoppingListType } from "../../propTypes";
import {
    addShoppingListItem,
    deleteShoppingList,
    deleteShoppingListItem,
    emptyCart,
    getShoppingList,
    updateShoppingList,
    updateShoppingListItem
} from "../../actions/ShoppingListActions";
import { getItemTypes } from "../../actions/ItemTypeActions";

import ShoppingList from "./ShoppingList";

export class ShoppingListPage extends React.Component {
    static propTypes = {
        token: PropTypes.string.isRequired,
        match: PropTypes.object.isRequired,
        history: PropTypes.shape({ push: PropTypes.func.isRequired }).isRequired,

        shoppingList: ShoppingListType,
        itemTypes: PropTypes.arrayOf(ItemTypeType),

        getShoppingList: PropTypes.func.isRequired,
        fetchingShoppingList: PropTypes.bool.isRequired,
        errorGetShoppingList: PropTypes.object,

        getItemTypes: PropTypes.func.isRequired,

        updateShoppingList: PropTypes.func.isRequired,
        updatingShoppingList: PropTypes.bool.isRequired,
        errorUpdateShoppingList: PropTypes.object,

        deleteShoppingList: PropTypes.func.isRequired,
        deletingShoppingList: PropTypes.bool.isRequired,
        errorDeleteShoppingList: PropTypes.object,

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

    state = {
        isEditing: false
    };

    componentDidMount() {
        this.props.getShoppingList({
            token: this.props.token,
            id: this.props.match.params.listId
        });

        this.props.getItemTypes(this.props.token);
    }

    componentDidUpdate(prevProps) {
        const shoppingListUpdated = prevProps.updatingShoppingList && !this.props.updatingShoppingList;
        const shoppingListItemAdded = prevProps.addingShoppingListItem && !this.props.addingShoppingListItem;
        const shoppingListItemUpdated = prevProps.updatingShoppingListItem && !this.props.updatingShoppingListItem;
        const shoppingListItemDeleted = prevProps.deletingShoppingListItem && !this.props.deletingShoppingListItem;
        const cartEmptied = prevProps.emptyingCart && !this.props.emptyingCart;

        const shoppingListHasChanged = shoppingListUpdated
            || shoppingListItemAdded
            || shoppingListItemUpdated
            || shoppingListItemDeleted
            || cartEmptied;

        if (shoppingListHasChanged) {
            const { token, shoppingList } = this.props;
            this.props.getShoppingList({ token, id: shoppingList.id });
        }

        const shoppingListFetched = prevProps.fetchingShoppingList && !this.props.fetchingShoppingList;
        if (shoppingListFetched && this.state.isEditing) {
            this.setState({ isEditing: false })
        }

        const shoppingListDeleted = prevProps.deletingShoppingList && !this.props.deletingShoppingList;
        if (shoppingListDeleted) {
            this.props.history.push('/lists');
        }
    }

    handleStartEdit = () => {
        this.setState({ isEditing: true });
    };

    handleCancelEdit = () => {
        this.setState({ isEditing: false });
    };

    handleChangeName = (newName) => {
        const { token, shoppingList } = this.props;
        this.props.updateShoppingList({ token, listId: shoppingList.id, name: newName });
    };

    handleToggleItemInCart = (item, newInCart) => {
        const { token, shoppingList } = this.props;

        this.props.updateShoppingListItem({
            token,
            listId: shoppingList.id,
            itemId: item.id,
            quantity: item.quantity,
            inCart: newInCart
        });
    };

    handleUpdateItemQuantity = (item, newQuantity) => {
        const { token, shoppingList } = this.props;

        if (newQuantity > 0) {
            this.props.updateShoppingListItem({
                token,
                listId: shoppingList.id,
                itemId: item.id,
                quantity: newQuantity,
                inCart: item.inCart
            });
        } else {
            this.props.deleteShoppingListItem({ token, listId: shoppingList.id, itemId: item.id });
        }
    };

    handleEmptyCart = () => {
        const { token, shoppingList } = this.props;

        this.props.emptyCart({ token, listId: shoppingList.id });
    };

    handleDelete = () => {
        const { token, shoppingList } = this.props;

        this.props.deleteShoppingList({ token, listId: shoppingList.id });
    };

    render() {
        const { shoppingList } = this.props;
        const { isEditing } = this.state;

        if (!shoppingList) {
            return (
                <AppLayout>Loading...</AppLayout>
            )
        }

        return (
            <AppLayout>
                <ShoppingList
                    shoppingList={shoppingList}
                    isEditing={isEditing}
                    onStartEdit={this.handleStartEdit}
                    onCancelEdit={this.handleCancelEdit}
                    onToggleItemInCart={this.handleToggleItemInCart}
                    onUpdateItemQuantity={this.handleUpdateItemQuantity}
                    onEmptyCart={this.handleEmptyCart}
                    onChangeName={this.handleChangeName}
                    onDelete={this.handleDelete}
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

        const { listId } = ownProps.match.params;
        const { shoppingLists } = store.shoppingList;
        const { itemTypes } = store.itemType;

        const shoppingList = listId ? shoppingLists[listId] : undefined;
        return {
            token: store.auth.token,
            ...store.shoppingList,
            shoppingList,
            fetchingShoppingList: shoppingList ? shoppingList.fetching : false,
            errorGetShoppingList: shoppingList ? shoppingList.error: null,
            itemTypes
        };
    },
    dispatch => ({
        getShoppingList: args => dispatch(getShoppingList(args)),
        updateShoppingList: args => dispatch(updateShoppingList(args)),
        deleteShoppingList: args => dispatch(deleteShoppingList(args)),
        getItemTypes: args => dispatch(getItemTypes(args)),
        addShoppingListItem: args => dispatch(addShoppingListItem(args)),
        updateShoppingListItem: args => dispatch(updateShoppingListItem(args)),
        deleteShoppingListItem: args => dispatch(deleteShoppingListItem(args)),
        emptyCart: args => dispatch(emptyCart(args)),
    })
)(ShoppingListPage);