import React from 'react';
import PropTypes from 'prop-types';
import AppLayout from "./AppLayout";
import {connect} from "react-redux";
import {getShoppingLists} from "../actions/ShoppingListActions";

export class ShoppingListsView extends React.Component {
    static propTypes = {
        getShoppingLists: PropTypes.func.isRequired,
        shoppingLists: PropTypes.object,
        fetching: PropTypes.bool,
        error: PropTypes.string
    };

    static defaultProps = {
        shoppingLists: {},
        fetching: false,
        error: null
    };

    componentDidMount() {
        this.props.getShoppingLists();
    }

    renderLists = () => {
        return Object
            .values(this.props.shoppingLists)
            .map(shoppingList => (<div key={shoppingList.id}>{shoppingList.name}</div>));
    };

    render() {
        return (
            <AppLayout>
                <div>{this.renderLists()}</div>
            </AppLayout>
        )
    }
}

export default connect(
    store => ({
        ...store.shoppingList
    }),
    dispatch => ({
        getShoppingLists: () => dispatch(getShoppingLists())
    })
)(ShoppingListsView);