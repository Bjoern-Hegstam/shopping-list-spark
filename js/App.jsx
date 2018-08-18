import React from 'react';
import {connect} from 'react-redux';
import {Redirect, Route, Switch, withRouter} from 'react-router-dom';
import LoginPage from './page/login/LoginPage';
import RegistrationPage from './page/registration/RegistrationPage';
import PageNotFound from './components/PageNotFound';
import ShoppingListsPage from './page/shoppinglists/ShoppingListsPage';
import ShoppingListPage from './page/shoppinglist/ShoppingListPage';
import AdminArea from './components/AdminArea';
import {UserType} from "./propTypes";

class App extends React.Component {
    static propTypes = {
        user: UserType
    };

    static defaultProps = {
        user: undefined
    };

    render() {
        if (this.props.user) {
            return (
                <Switch>
                    <Redirect exact path='/' to='/lists'/>
                    <Redirect exact path='/login' to='/lists'/>

                    <Route exact path='/lists' component={ShoppingListsPage}/>
                    <Route path='/lists/:listId' component={ShoppingListPage}/>
                    ( this.props.user.roles.contains('admin') &&
                    <Route path='/admin' component={AdminArea}/>
                    )
                    <Route component={PageNotFound}/>
                </Switch>
            )
        } else {
            return (
                <Switch>
                    <Redirect exact path='/' to='/login'/>
                    <Redirect exact path='/lists' to='/login'/>

                    <Route exact path='/login' component={LoginPage}/>
                    <Route exact path='/register' component={RegistrationPage}/>
                    <Route component={PageNotFound}/>
                </Switch>
            )
        }
    }
}

export default withRouter(connect(store => ({
    user: store.auth.currentUser
}))(App));