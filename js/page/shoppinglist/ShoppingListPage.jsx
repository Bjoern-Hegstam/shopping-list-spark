import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import AppLayout from '../../components/AppLayout';
import { ItemTypeType, ShoppingListType } from '../../propTypes';
import {
    addShoppingListItem,
    deleteShoppingList,
    deleteShoppingListItem,
    emptyCart,
    getShoppingList,
    updateShoppingList,
    updateShoppingListItem,
} from '../../actions/ShoppingListActions';
import { addItemType, getItemTypes } from '../../actions/ItemTypeActions';

import ShoppingList from './ShoppingList';
import { createErrorSelector, createLoadingSelector, itemTypesSelector, shoppingListSelector, } from '../../selectors';
import * as types from '../../actions/types';

export class ShoppingListPage extends React.Component {
    static propTypes = {
        token: PropTypes.string.isRequired,
        match: PropTypes.shape({
            params: PropTypes.shape({
                listId: PropTypes.string.isRequired,
            }).isRequired,
        }).isRequired,
        history: PropTypes.shape({ push: PropTypes.func.isRequired }).isRequired,

        shoppingList: ShoppingListType,
        itemTypes: PropTypes.arrayOf(ItemTypeType),

        getShoppingList: PropTypes.func.isRequired,
        fetchingShoppingList: PropTypes.bool.isRequired,
        errorGetShoppingList: PropTypes.object,

        addItemType: PropTypes.func.isRequired,
        addingItemType: PropTypes.bool.isRequired,
        errorAddItemType: PropTypes.object,

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
        errorEmptyCart: PropTypes.object,
    };

    static defaultProps = {
        shoppingList: undefined,
        itemTypes: [],

        errorAddItemType: null,
        errorUpdateShoppingList: null,
        errorDeleteShoppingList: null,
        errorAddShoppingListItem: null,
        errorUpdateShoppingListItem: null,
        errorDeleteShoppingListItem: null,
    };

    state = {
        isEditing: false,
        initialFetchComplete: false,
    };

    componentDidMount() {
        this.props.getShoppingList({
            token: this.props.token,
            id: this.props.match.params.listId,
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
        if (shoppingListFetched) {
            const newState = {};
            if (prevProps.isEditing) {
                newState.isEditing = false;
            }

            if (!prevProps.initialFetchComplete) {
                newState.initialFetchComplete = true;
            }

            if (newState) {
                this.setState(newState);
            }
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
        this.setState({ isEditing: false });
    };

    handleAddItem = async ({ id, name }) => {
        const { token, shoppingList } = this.props;

        let itemTypeId;
        if (!id) {
            const response = await this.props.addItemType({ token, name });
            this.props.getItemTypes(token);
            itemTypeId = response.payload.data.id;
        } else {
            itemTypeId = id;
        }

        const itemInList = shoppingList.items.find(item => item.itemType.id === itemTypeId);

        if (!itemInList) {
            this.props.addShoppingListItem({
                token,
                listId: shoppingList.id,
                itemTypeId,
                quantity: 1,
            });
        } else {
            this.props.updateShoppingListItem({
                token,
                listId: shoppingList.id,
                itemId: itemInList.id,
                quantity: itemInList.quantity + 1,
                inCart: itemInList.inCart,
            });
        }
    };

    handleToggleItemInCart = (item, newInCart) => {
        const { token, shoppingList } = this.props;

        this.props.updateShoppingListItem({
            token,
            listId: shoppingList.id,
            itemId: item.id,
            quantity: item.quantity,
            inCart: newInCart,
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
                inCart: item.inCart,
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
        const { shoppingList, itemTypes } = this.props;
        const { initialFetchComplete, isEditing } = this.state;

        if (!initialFetchComplete) {
            return (
                <AppLayout>Loading...</AppLayout>
            );
        }

        return (
            <AppLayout>
                <ShoppingList
                    shoppingList={shoppingList}
                    itemTypes={itemTypes}
                    isEditing={isEditing}
                    onStartEdit={this.handleStartEdit}
                    onCancelEdit={this.handleCancelEdit}
                    onAddItem={this.handleAddItem}
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

const fetchingShoppingListSelector = createLoadingSelector([types.GET_SHOPPING_LIST]);
const errorGetShoppingListSelector = createErrorSelector([types.GET_SHOPPING_LIST]);

const addingItemTypeSelector = createLoadingSelector([types.ADD_ITEM_TYPE]);
const errorAddItemTypeSelector = createErrorSelector([types.ADD_ITEM_TYPE]);

const updatingShoppingListSelector = createLoadingSelector([types.UPDATE_SHOPPING_LIST]);
const errorUpdateShoppingListSelector = createErrorSelector([types.UPDATE_SHOPPING_LIST]);

const deletingShoppingListSelector = createLoadingSelector([types.DELETE_SHOPPING_LIST]);
const errorDeleteShoppingListSelector = createErrorSelector([types.DELETE_SHOPPING_LIST]);

const addingItemSelector = createLoadingSelector([types.ADD_SHOPPING_LIST_ITEM]);
const errorAddItemSelector = createErrorSelector([types.ADD_SHOPPING_LIST_ITEM]);

const updatingItemSelector = createLoadingSelector([types.UPDATE_SHOPPING_LIST_ITEM]);
const errorUpdateItemSelector = createErrorSelector([types.UPDATE_SHOPPING_LIST_ITEM]);

const deletingItemSelector = createLoadingSelector([types.DELETE_SHOPPING_LIST_ITEM]);
const errorDeleteItemSelector = createErrorSelector([types.DELETE_SHOPPING_LIST_ITEM]);

const emptyingCartSelector = createLoadingSelector([types.EMPTY_CART]);
const errorEmptyCartSelector = createErrorSelector([types.EMPTY_CART]);

const mapStateToProps = (store, ownProps) => {
    if (!ownProps.match) {
        return {};
    }

    const { listId } = ownProps.match.params;

    return {
        token: store.auth.token,

        shoppingList: shoppingListSelector(store, listId),
        itemTypes: itemTypesSelector(store),

        fetchingShoppingList: fetchingShoppingListSelector(store),
        errorGetShoppingList: errorGetShoppingListSelector(store),

        addingItemType: addingItemTypeSelector(store),
        errorAddItemType: errorAddItemTypeSelector(store),

        updatingShoppingList: updatingShoppingListSelector(store),
        errorUpdateShoppingList: errorUpdateShoppingListSelector(store),

        deletingShoppingList: deletingShoppingListSelector(store),
        errorDeleteShoppingList: errorDeleteShoppingListSelector(store),

        addingShoppingListItem: addingItemSelector(store),
        errorAddShoppingListItem: errorAddItemSelector(store),

        updatingShoppingListItem: updatingItemSelector(store),
        errorUpdateShoppingListItem: errorUpdateItemSelector(store),

        deletingShoppingListItem: deletingItemSelector(store),
        errorDeleteShoppingListItem: errorDeleteItemSelector(store),

        emptyingCart: emptyingCartSelector(store),
        errorEmptyCart: errorEmptyCartSelector(store),
    };
};

const mapDispatchToProps = dispatch => ({
    getShoppingList: args => dispatch(getShoppingList(args)),
    updateShoppingList: args => dispatch(updateShoppingList(args)),
    deleteShoppingList: args => dispatch(deleteShoppingList(args)),
    addItemType: args => dispatch(addItemType(args)),
    getItemTypes: args => dispatch(getItemTypes(args)),
    addShoppingListItem: args => dispatch(addShoppingListItem(args)),
    updateShoppingListItem: args => dispatch(updateShoppingListItem(args)),
    deleteShoppingListItem: args => dispatch(deleteShoppingListItem(args)),
    emptyCart: args => dispatch(emptyCart(args)),
});

export default connect(mapStateToProps, mapDispatchToProps)(ShoppingListPage);
