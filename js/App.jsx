import React from 'react';
import { connect } from 'react-redux';
import { Redirect, Route, Switch, withRouter, } from 'react-router-dom';
import LoginPage from './page/login/LoginPage';
import RegistrationPage from './page/registration/RegistrationPage';
import PageNotFound from './components/PageNotFound';
import ShoppingListsPage from './page/shoppinglists/ShoppingListsPage';
import ShoppingListPage from './page/shoppinglist/ShoppingListPage';
import { UserType } from './propTypes';

function App(props) {
    if (props.user) {
        return (
            <Switch>
                <Redirect exact path="/" to="/lists" />
                <Redirect exact path="/login" to="/lists" />

                <Route exact path="/lists" component={ShoppingListsPage} />
                <Route path="/lists/:listId" component={ShoppingListPage} />
                <Route component={PageNotFound} />
            </Switch>
        );
    }
    return (
        <Switch>
            <Route exact path="/login" component={LoginPage} />
            <Route exact path="/register" component={RegistrationPage} />
            <Redirect path="/" to="/login" />
        </Switch>
    );
}

App.propTypes = {
    user: UserType,
};

App.defaultProps = {
    user: undefined,
};

export default withRouter(connect(store => ({
    user: store.auth.currentUser,
}))(App));
