import React, { Component } from 'react';
import * as PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { Redirect, Route, Switch, withRouter, } from 'react-router-dom';
import LoginPage from './page/login/LoginPage';
import RegistrationPage from './page/registration/RegistrationPage';
import PageNotFound from './components/PageNotFound';
import ShoppingListsPage from './page/shoppinglists/ShoppingListsPage';
import ShoppingListPage from './page/shoppinglist/ShoppingListPage';
import ItemTypesPage from './page/itemtypes/ItemTypesPage';
import { UserType } from './propTypes';
import { createErrorSelector, createLoadingSelector } from './selectors';
import * as actionTypes from './actions/types';

export class App extends Component {
    static propTypes = {
      location: PropTypes.shape({
        pathname: PropTypes.string.isRequired,
        state: PropTypes.shape({
          referer: PropTypes.string,
        }),
      }).isRequired,
      history: PropTypes.shape({
        push: PropTypes.func.isRequired,
      }).isRequired,

      user: UserType,

      loggingIn: PropTypes.bool.isRequired,
      errorLogin: PropTypes.object,
    };

    static defaultProps = {
      user: undefined,
      errorLogin: undefined,
    };

    componentDidUpdate(prevProps) {
      if (prevProps.loggingIn && !this.props.loggingIn && !this.props.errorLogin) {
        const { location, history } = this.props;
        if (location.state && location.state.referer && location.state.referer !== '/') {
          history.push(location.state.referer);
        }
      }
    }

    render() {
      if (this.props.user) {
        return (
          <Switch>
            <Redirect exact path="/" to="/lists" />
            <Redirect exact path="/login" to="/lists" />

            <Route exact path="/lists" component={ShoppingListsPage} />
            <Route path="/lists/:listId" component={ShoppingListPage} />
            <Route path="/item-types" component={ItemTypesPage} />
            <Route component={PageNotFound} />
          </Switch>
        );
      }
      return (
        <Switch>
          <Route exact path="/login" component={LoginPage} />
          <Route exact path="/register" component={RegistrationPage} />
          <Redirect path="/" to={{ pathname: '/login', state: { referer: this.props.location.pathname } }} />
        </Switch>
      );
    }
}

const mapStateToProps = state => ({
  user: state.auth.currentUser,

  loggingIn: createLoadingSelector(actionTypes.LOGIN)(state),
  errorLogin: createErrorSelector(actionTypes.LOGIN)(state),
});

export default withRouter(connect(mapStateToProps)(App));
