import React from 'react';
import * as PropTypes from 'prop-types';
import { connect } from 'react-redux';
import AppLayout from '../../components/AppLayout';
import { ItemTypeType, RequestErrorType, ShoppingListType } from '../../propTypes';
import {
  addShoppingListItem,
  addShoppingListItemRequestSelectorKey,
  deleteShoppingList,
  deleteShoppingListItem,
  emptyCart,
  getShoppingList,
  updateShoppingList,
  updateShoppingListItem,
} from '../../actions/ShoppingListActions';
import { addItemType, getItemTypes } from '../../actions/ItemTypeActions';

import ShoppingList from './ShoppingList';
import * as types from '../../actions/types';
import { itemTypesSelector, shoppingListSelector } from '../../selectors/ShoppingListSelectors';
import {
  createErrorSelector,
  createLoadingSelector,
  createSubRequestLoadingSelector,
} from '../../selectors/RequestSelectors';

export const AddItemFlowStep = {
  CREATE_ITEM_TYPE: 'CREATE_ITEM_TYPE',
  ADD_ITEM: 'ADD_ITEM',
};

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

    addItemType: PropTypes.func.isRequired,
    addingItemType: PropTypes.bool.isRequired,
    errorAddItemType: RequestErrorType,

    getItemTypes: PropTypes.func.isRequired,

    updateShoppingList: PropTypes.func.isRequired,
    updatingShoppingList: PropTypes.bool.isRequired,

    deleteShoppingList: PropTypes.func.isRequired,
    deletingShoppingList: PropTypes.bool.isRequired,

    addShoppingListItem: PropTypes.func.isRequired,
    addingShoppingListItem: PropTypes.objectOf(PropTypes.bool).isRequired,
    errorAddShoppingListItem: PropTypes.objectOf(RequestErrorType),

    updateShoppingListItem: PropTypes.func.isRequired,
    deleteShoppingListItem: PropTypes.func.isRequired,

    emptyCart: PropTypes.func.isRequired,
    emptyingCart: PropTypes.bool.isRequired,
  };

  static defaultProps = {
    shoppingList: undefined,
    itemTypes: [],

    errorAddItemType: null,
    errorAddShoppingListItem: {},
  };

  state = {
    isEditing: false,
    initialFetchComplete: false,
    addItemFlow: null,
  };

  componentDidMount() {
    this.props.getShoppingList({
      token: this.props.token,
      id: this.props.match.params.listId,
    });

    this.props.getItemTypes(this.props.token);
  }

  componentDidUpdate(prevProps) {
    this.handleAddItemFlow(prevProps);
    this.reFetchShoppingListIfHasChanged(prevProps);
    this.handleShoppingListFetched(prevProps);
    this.handleShoppingListDeleted(prevProps);
  }

  handleAddItemFlow = (prevProps) => {
    if (!this.state.addItemFlow) {
      return;
    }

    const { step, itemType } = this.state.addItemFlow;

    if (step === AddItemFlowStep.CREATE_ITEM_TYPE) {
      if (prevProps.addingItemType && !this.props.addingItemType) {
        if (!this.props.errorAddItemType) {
          // Successfully created item type, fetch it from state and go to next step in flow
          this.setState({
            addItemFlow: {
              step: AddItemFlowStep.ADD_ITEM,
              itemType: this.props.itemTypes.find(it => it.name === itemType.name),
            },
          });
        } else {
          // Something went wrong when creating item type, abort flow and refresh data
          this.setState({ addItemFlow: null }, () => {
            this.props.getItemTypes(this.props.token);
            this.props.getShoppingList({
              token: this.props.token,
              id: this.props.shoppingList.id,
            });
          });
        }
      } else if (!this.props.addingItemType) {
        // Initiate creation of item type
        this.props.addItemType({
          token: this.props.token,
          name: itemType.name,
        });
      }
    } else if (step === AddItemFlowStep.ADD_ITEM) {
      const selectorKey = addShoppingListItemRequestSelectorKey(this.props.shoppingList.id, itemType.id);
      if (prevProps.addingShoppingListItem[selectorKey] && !this.props.addingShoppingListItem[selectorKey]) {
        if (!this.props.errorAddShoppingListItem[selectorKey]) {
          // Item successfully added to list, clear flow.
          // Can leave it to generic change handler to make call to refresh list data
          this.setState({ addItemFlow: null });
        } else {
          // Something went wrong when adding item to list, abort flow.
          // Generic change handler will refresh list data, but we need to manually re-fetch item types
          this.setState({ addItemFlow: null }, () => this.props.getItemTypes(this.props.token));
        }
      } else if (!this.props.addingShoppingListItem[selectorKey]) {
        // Initiate adding of item to list
        this.props.addShoppingListItem({
          token: this.props.token,
          listId: this.props.shoppingList.id,
          itemTypeId: itemType.id,
          quantity: 1,
        });
      }
    }
  };

  reFetchShoppingListIfHasChanged = (prevProps) => {
    const shoppingListUpdated = prevProps.updatingShoppingList && !this.props.updatingShoppingList;
    const cartEmptied = prevProps.emptyingCart && !this.props.emptyingCart;

    if (shoppingListUpdated || cartEmptied) {
      const { token, shoppingList } = this.props;
      this.props.getShoppingList({ token, id: shoppingList.id });
    }
  };

  handleShoppingListFetched = (prevProps) => {
    const shoppingListFetched = prevProps.fetchingShoppingList && !this.props.fetchingShoppingList;
    if (shoppingListFetched) {
      const newState = {};
      if (this.state.isEditing) {
        newState.isEditing = false;
      }

      if (!this.state.initialFetchComplete) {
        newState.initialFetchComplete = true;
      }

      if (newState) {
        this.setState(newState);
      }
    }
  };

  handleShoppingListDeleted = (prevProps) => {
    const shoppingListDeleted = prevProps.deletingShoppingList && !this.props.deletingShoppingList;
    if (shoppingListDeleted) {
      this.props.history.push('/lists');
    }
  };

  handleStartEdit = () => {
    this.setState({ isEditing: true });
  };

  handleChangeName = (newName) => {
    const { token, shoppingList } = this.props;
    this.props.updateShoppingList({ token, listId: shoppingList.id, name: newName });
    this.setState({ isEditing: false });
  };

  handleCancelEdit = () => {
    this.setState({ isEditing: false });
  };

  handleAddItem = async ({ id, name }) => {
    this.setState({
      addItemFlow: {
        step: id ? 'ADD_ITEM' : 'CREATE_ITEM_TYPE',
        itemType: { id, name },
      },
    });
  };

  handleToggleItemInCart = ({ item, newInCart }) => {
    const { token, shoppingList } = this.props;

    this.props.updateShoppingListItem({
      token,
      listId: shoppingList.id,
      itemId: item.id,
      quantity: item.quantity,
      inCart: newInCart,
    });
  };

  handleUpdateItemQuantity = ({ item, newQuantity }) => {
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
        <AppLayout />
      );
    }

    return (
      <AppLayout>
        <ShoppingList
          shoppingList={shoppingList}
          itemTypes={itemTypes}
          isEditing={isEditing}
          onStartEdit={this.handleStartEdit}
          onChangeName={this.handleChangeName}
          onCancelEdit={this.handleCancelEdit}
          onAddItem={this.handleAddItem}
          onToggleItemInCart={this.handleToggleItemInCart}
          onUpdateItemQuantity={this.handleUpdateItemQuantity}
          onEmptyCart={this.handleEmptyCart}
          onDelete={this.handleDelete}
        />
      </AppLayout>
    );
  }
}

const mapStateToProps = (store, ownProps) => {
  if (!ownProps.match) {
    return {};
  }

  const { listId } = ownProps.match.params;

  return {
    token: store.auth.token,

    shoppingList: shoppingListSelector(listId)(store),
    itemTypes: itemTypesSelector(store),

    fetchingShoppingList: createLoadingSelector(types.GET_SHOPPING_LIST)(store),

    addingItemType: createLoadingSelector(types.ADD_ITEM_TYPE)(store),
    errorAddItemType: createErrorSelector(types.ADD_ITEM_TYPE)(store),

    updatingShoppingList: createLoadingSelector(types.UPDATE_SHOPPING_LIST)(store),
    deletingShoppingList: createLoadingSelector(types.DELETE_SHOPPING_LIST)(store),

    addingShoppingListItem: createSubRequestLoadingSelector(types.ADD_SHOPPING_LIST_ITEM)(store),
    errorAddShoppingListItem: createErrorSelector(types.ADD_SHOPPING_LIST_ITEM)(store),

    emptyingCart: createLoadingSelector(types.EMPTY_CART)(store),
  };
};

const mapDispatchToProps = {
  getShoppingList,
  updateShoppingList,
  deleteShoppingList,
  addItemType,
  getItemTypes,
  addShoppingListItem,
  updateShoppingListItem,
  deleteShoppingListItem,
  emptyCart,
};

export default connect(mapStateToProps, mapDispatchToProps)(ShoppingListPage);
