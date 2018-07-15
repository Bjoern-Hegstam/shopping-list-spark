import React from 'react';
import PropTypes from 'prop-types';
import AppLayout from "./AppLayout";
import {connect} from "react-redux";
import {ShoppingListType} from "../propTypes";
import {getShoppingList} from "../actions/ShoppingListActions";

export class ShoppingListPage extends React.Component {
    static propTypes = {
        token: PropTypes.string.isRequired,
        shoppingList: ShoppingListType,

        getShoppingList: PropTypes.func.isRequired,

        match: PropTypes.object.isRequired
    };

    static defaultProps = {
        shoppingList: undefined
    };

    componentDidMount() {
        this.props.getShoppingList({
            token: this.props.token,
            id: this.props.match.params.listId
        });
    }

    render() {
        const {shoppingList} = this.props;

        if (!shoppingList) {
            return (
                <AppLayout>Loading...</AppLayout>
            )
        }

        return (
            <AppLayout>
                <div>{shoppingList.name}</div>
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

        return {
            token: store.auth.token,
            shoppingList: listId ? shoppingLists[listId] : undefined
        };
    },
    dispatch => ({
        getShoppingList: args => dispatch(getShoppingList(args))
    })
)(ShoppingListPage);