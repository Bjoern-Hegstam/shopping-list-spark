import React from 'react';
import PropTypes from 'prop-types';
import AppLayout from "./AppLayout";
import {connect} from "react-redux";
import {getShoppingLists} from "../actions/ShoppingListActions";
import {ShoppingListType} from "../propTypes";
import {Link} from "react-router-dom";

export class ShoppingListsPage extends React.Component {
    static propTypes = {
        token: PropTypes.string.isRequired,
        shoppingLists: PropTypes.objectOf(ShoppingListType).isRequired,

        getShoppingLists: PropTypes.func.isRequired,
        fetching: PropTypes.bool,
        error: PropTypes.object
    };

    static defaultProps = {
        shoppingLists: {},
        fetching: false,
        error: null
    };

    componentDidMount() {
        this.props.getShoppingLists(this.props.token);
    }

    renderLists = () => {
        return Object
            .values(this.props.shoppingLists)
            .map(shoppingList => (
                    <div className="shopping-lists--shopping-list" key={shoppingList.id}>
                        <Link to={`/lists/${shoppingList.id}/`}>{shoppingList.name}</Link>
                    </div>
                )
            );
    };

    render() {
        return (
            <AppLayout>
                <div className="shopping-lists">{this.renderLists()}</div>
                {/* TODO: Add button to create new list */}
            </AppLayout>
        )
    }
}

export default connect(
    store => ({
        ...store.shoppingList,
        token: store.auth.token
    }),
    dispatch => ({
        getShoppingLists: arg => dispatch(getShoppingLists(arg))
    })
)(ShoppingListsPage);