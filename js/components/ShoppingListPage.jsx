import React from 'react';
import PropTypes from 'prop-types';
import AppLayout from "./AppLayout";
import {connect} from "react-redux";
import {ItemTypeType, ShoppingListType} from "../propTypes";
import {getShoppingList} from "../actions/ShoppingListActions";
import {getItemTypes} from "../actions/ItemTypeActions";

import './ShoppingListPage.scss'
import ShoppingList from "./ShoppingList";

export class ShoppingListPage extends React.Component {
    static propTypes = {
        token: PropTypes.string.isRequired,
        shoppingList: ShoppingListType,
        itemTypes: PropTypes.arrayOf(ItemTypeType),

        getShoppingList: PropTypes.func.isRequired,
        getItemTypes: PropTypes.func.isRequired,

        match: PropTypes.object.isRequired
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

    handleToggleItemInCart = (itemId, newInCart) => {
        console.log(`Change item ${itemId} to inCart: <${newInCart}>`);
    };

    handleUpdateItemQuantity = (itemId, newQuantity) => {
        console.log(`Change item ${itemId} to quantity: <${newQuantity}>`);
    };

    handleEmptyCart = () => {
        console.log(`Empty cart of shopping list ${this.props.shoppingList.id}`);
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
            shoppingList: listId ? shoppingLists[listId] : undefined,
            itemTypes
        };
    },
    dispatch => ({
        getShoppingList: args => dispatch(getShoppingList(args)),
        getItemTypes: args => dispatch(getItemTypes(args))
    })
)(ShoppingListPage);