import React from 'react';
import PropTypes from 'prop-types';
import AppLayout from "./AppLayout";
import {connect} from "react-redux";
import {getShoppingLists} from "../actions/ShoppingListActions";
import {ShoppingListType} from "../propTypes";
import {Link} from "react-router-dom";

export class ShoppingListsPage extends React.Component {
    static propTypes = {
        getShoppingLists: PropTypes.func.isRequired,
        shoppingLists: PropTypes.objectOf(ShoppingListType).isRequired,
        fetching: PropTypes.bool,
        error: PropTypes.object
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
            .map(shoppingList => (
                    <div className="col-sm-4" key={shoppingList.id}>
                        <Link className="btn btn-link btn-block" to={`/lists/${shoppingList.id}/`}>
                            <h2>{shoppingList.name}</h2>
                        </Link>
                    </div>
                )
            );
    };

    render() {
        return (
            <AppLayout>
                <div className="container">{this.renderLists()}</div>
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
)(ShoppingListsPage);