import React from 'react';
import PropTypes from 'prop-types';
import AppLayout from "../../components/AppLayout";
import {connect} from "react-redux";
import {getShoppingLists} from "../../actions/ShoppingListActions";
import {ShoppingListType} from "../../propTypes";
import {ShoppingListLink} from "./ShoppingListLink";
import {withRouter} from "react-router-dom";

export class ShoppingListsPage extends React.Component {
    static propTypes = {
        token: PropTypes.string.isRequired,
        shoppingLists: PropTypes.objectOf(ShoppingListType).isRequired,

        getShoppingLists: PropTypes.func.isRequired,
        fetching: PropTypes.bool,
        error: PropTypes.object,

        history: PropTypes.shape({
            push: PropTypes.func.isRequired
        }).isRequired
    };

    static defaultProps = {
        shoppingLists: {},
        fetching: false,
        error: null
    };

    componentDidMount() {
        this.props.getShoppingLists(this.props.token);
    }

    handleLinkClicked = (listId) => {
        this.props.history.push(`/lists/${listId}/`);
    };

    renderLists = () => {
        return Object
            .values(this.props.shoppingLists)
            .map(shoppingList => (
                <ShoppingListLink
                    key={shoppingList.id}
                    id={shoppingList.id}
                    name={shoppingList.name}
                    onClick={this.handleLinkClicked}
                />
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

export default withRouter(connect(
    store => ({
        ...store.shoppingList,
        token: store.auth.token
    }),
    dispatch => ({
        getShoppingLists: arg => dispatch(getShoppingLists(arg))
    })
)(ShoppingListsPage));