import React from 'react';
import PropTypes from 'prop-types';
import AppLayout from "./AppLayout";
import {connect} from "react-redux";
import {ItemTypeType, ShoppingListType} from "../propTypes";
import {getShoppingList} from "../actions/ShoppingListActions";
import {getItemTypes} from "../actions/ItemTypeActions";

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

    render() {
        const {shoppingList} = this.props;

        if (!shoppingList) {
            return (
                <AppLayout>Loading...</AppLayout>
            )
        }

        return (
            <AppLayout>
                <div className='shopping-list'>
                    <div className='shopping-list__header'>
                        <span className='shopping-list__header__name'>{shoppingList.name}</span>
                        <div className='shopping-list__header__empty-cart-button'>Empty Cart</div>
                    </div>
                    <div className='shopping-list__body'>
                        {this.renderItems()}
                    </div>
                </div>
                {/* TODO: Create custom component for adding new item (button, interactive narrowing of options based on input, on enter auto-select first option)  */}
            </AppLayout>
        );
    }

    renderItems = () => {
        return (
            <React.Fragment>
                {this.props.shoppingList.items.map(item => {
                    let itemClassName = 'shopping-list-item';
                    if (item.inCart) {
                        itemClassName += ' shopping-list-item--in-cart';
                    }

                    return (
                        <div key={item.id} className={itemClassName}>
                            <span className='shopping-list-item__name'>{item.itemType.name}</span>
                            <span className='shopping-list-item__quantity'>{item.quantity}</span>
                            <div className='shopping-list-item__inc-button'>+</div>
                            <div className='shopping-list-item__dec-button'>-</div>
                        </div>
                    )
                })}
            </React.Fragment>
        )
    };
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