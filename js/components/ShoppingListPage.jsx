import React from 'react';
import PropTypes from 'prop-types';
import AppLayout from "./AppLayout";
import {connect} from "react-redux";
import {ShoppingListType} from "../util/proptypes";
import {getShoppingList} from "../actions/ShoppingListActions";

export class ShoppingListPage extends React.Component {
    static propTypes = {
        shoppingList: ShoppingListType,

        getShoppingList: PropTypes.func.isRequired,

        match: PropTypes.object.isRequired
    };

    defaultProps = {
        shoppingList: undefined
    };

    constructor(props) {
        super(props);
        this.state = {
            listId: match.params.listId
        }
    }

    componentDidMount() {
        this.props.getShoppingList(this.state.listId);
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
                <div>{shoppingList}</div>
            </AppLayout>
        );
    }
}

export default connect(
    store => {
        const {listId} = this.state;
        const {shoppingLists} = store.shoppingList;

        return ({
            shoppingList: listId ? shoppingLists[listId] : undefined
        });
    },
    dispatch => ({
        getShoppingList: id => dispatch(getShoppingList(id))
    })
)(ShoppingListPage);