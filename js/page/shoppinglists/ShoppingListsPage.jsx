import React from 'react';
import * as PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';
import AppLayout from '../../components/AppLayout';
import { addShoppingList, getShoppingLists } from '../../actions/ShoppingListActions';
import { ShoppingListType } from '../../propTypes';
import ShoppingListLink from './ShoppingListLink';
import { createErrorSelector, createLoadingSelector, shoppingListsSelector } from '../../selectors';
import * as types from '../../actions/types';

export class ShoppingListsPage extends React.Component {
    static propTypes = {
        token: PropTypes.string.isRequired,
        shoppingLists: PropTypes.arrayOf(ShoppingListType),

        getShoppingLists: PropTypes.func.isRequired,
        fetchingShoppingLists: PropTypes.bool.isRequired,
        errorGetShoppingLists: PropTypes.object,

        addShoppingList: PropTypes.func.isRequired,
        addingShoppingList: PropTypes.bool.isRequired,
        errorAddShoppingList: PropTypes.object,

        history: PropTypes.shape({ push: PropTypes.func.isRequired }).isRequired,
    };

    static defaultProps = {
        shoppingLists: {},
        errorGetShoppingLists: null,
        errorAddShoppingList: null,
    };

    state = {
        newListName: '',
    };

    componentDidMount() {
        this.props.getShoppingLists(this.props.token);
    }

    componentDidUpdate(prevProps) {
        if (prevProps.addingShoppingList && !this.props.addingShoppingList) {
            this.props.getShoppingLists(this.props.token);
        }
    }

    handleLinkClicked = (listId) => {
        this.props.history.push(`/lists/${listId}/`);
    };

    onNewListNameChange = (e) => {
        this.setState({ newListName: e.target.value });
    };

    addNewList = () => {
        const { token } = this.props;
        const { newListName } = this.state;

        this.setState({ newListName: '' });
        this.props.addShoppingList({ token, name: newListName });
    };

    renderLists = () => Object
        .values(this.props.shoppingLists)
        .map(shoppingList => (
            <ShoppingListLink
                key={shoppingList.id}
                id={shoppingList.id}
                name={shoppingList.name}
                onClick={this.handleLinkClicked}
            />
        ));

    render() {
        return (
            <AppLayout>
                <div className="shopping-list-links">
                    {this.renderLists()}
                </div>
                <div className="add-new-shopping-list">
                    <form
                        className="add-new-shopping-list__form"
                        onSubmit={this.addNewList}
                    >
                        <input
                            id="add-new-shopping-list__name__input"
                            className="add-new-shopping-list__name__input"
                            placeholder="New list name..."
                            value={this.state.newListName}
                            onChange={this.onNewListNameChange}
                        />
                    </form>
                </div>
            </AppLayout>
        );
    }
}

const mapStateToProps = state => ({
    token: state.auth.token,

    shoppingLists: shoppingListsSelector(state),
    fetchingShoppingLists: createLoadingSelector(types.GET_SHOPPING_LISTS)(state),
    errorGetShoppingLists: createErrorSelector(types.GET_SHOPPING_LISTS)(state),

    addingShoppingList: createLoadingSelector(types.ADD_SHOPPING_LIST)(state),
    errorAddShoppingList: createErrorSelector(types.ADD_SHOPPING_LIST)(state),
});

const mapDispatchToProps = {
    getShoppingLists,
    addShoppingList,
};

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(ShoppingListsPage));
