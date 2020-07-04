import React from 'react';
import * as PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { v4 as uuidV4 } from 'uuid';
import AppLayout from '../../components/AppLayout';
import { ShoppingListType } from '../../propTypes';
import {
  addShoppingListItem,
  deleteShoppingList,
  deleteShoppingListItem,
  emptyCart,
  getShoppingList,
  updateShoppingList,
  updateShoppingListItem,
} from '../../actions/ShoppingListActions';

import ShoppingList from './ShoppingList';
import * as types from '../../actions/types';
import { shoppingListSelector } from '../../selectors/ShoppingListSelectors';
import { createLoadingSelector } from '../../selectors/RequestSelectors';

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

    getShoppingList: PropTypes.func.isRequired,
    fetchingShoppingList: PropTypes.bool.isRequired,

    updateShoppingList: PropTypes.func.isRequired,
    updatingShoppingList: PropTypes.bool.isRequired,

    deleteShoppingList: PropTypes.func.isRequired,
    deletingShoppingList: PropTypes.bool.isRequired,

    addShoppingListItem: PropTypes.func.isRequired,
    updateShoppingListItem: PropTypes.func.isRequired,
    deleteShoppingListItem: PropTypes.func.isRequired,

    emptyCart: PropTypes.func.isRequired,
    emptyingCart: PropTypes.bool.isRequired,
  };

  static defaultProps = {
    shoppingList: null,
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
  }

  componentDidUpdate(prevProps) {
    this.reFetchShoppingListIfHasChanged(prevProps);
    this.handleShoppingListFetched(prevProps);
    this.handleShoppingListDeleted(prevProps);
  }

  reFetchShoppingListIfHasChanged = prevProps => {
    const shoppingListUpdated = prevProps.updatingShoppingList && !this.props.updatingShoppingList;
    const cartEmptied = prevProps.emptyingCart && !this.props.emptyingCart;

    if (shoppingListUpdated || cartEmptied) {
      const { token, shoppingList } = this.props;
      this.props.getShoppingList({
        token,
        id: shoppingList.id,
      });
    }
  };

  handleShoppingListFetched = prevProps => {
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

  handleShoppingListDeleted = prevProps => {
    const shoppingListDeleted = prevProps.deletingShoppingList && !this.props.deletingShoppingList;
    if (shoppingListDeleted) {
      this.props.history.push('/lists');
    }
  };

  handleStartEdit = () => {
    this.setState({ isEditing: true });
  };

  handleChangeName = newName => {
    const { token, shoppingList } = this.props;
    this.props.updateShoppingList({ token, listId: shoppingList.id, name: newName });
    this.setState({ isEditing: false });
  };

  handleCancelEdit = () => {
    this.setState({ isEditing: false });
  };

  handleAddItem = ({ id, name }) => {
    const requestData = {
      requestId: uuidV4(),
      token: this.props.token,
      listId: this.props.shoppingList.id,
      quantity: 1,
    };

    if (id) {
      requestData.itemTypeId = id;
    } else {
      requestData.itemTypeName = name;
    }

    this.props.addShoppingListItem(requestData);
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
    const { shoppingList } = this.props;
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

    fetchingShoppingList: createLoadingSelector(types.GET_SHOPPING_LIST)(store),
    updatingShoppingList: createLoadingSelector(types.UPDATE_SHOPPING_LIST)(store),
    deletingShoppingList: createLoadingSelector(types.DELETE_SHOPPING_LIST)(store),

    emptyingCart: createLoadingSelector(types.EMPTY_CART)(store),
  };
};

const mapDispatchToProps = {
  getShoppingList,
  updateShoppingList,
  deleteShoppingList,
  addShoppingListItem,
  updateShoppingListItem,
  deleteShoppingListItem,
  emptyCart,
};

export default connect(mapStateToProps, mapDispatchToProps)(ShoppingListPage);
