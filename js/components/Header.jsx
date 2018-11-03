import React from 'react';
import PropTypes from 'prop-types';
import { NavLink } from 'react-router-dom';

import { connect } from 'react-redux';
import { UserType } from '../propTypes';
import { logout } from '../actions/UserActions';

export class Header extends React.Component {
    static propTypes = {
        user: UserType,
        logout: PropTypes.func.isRequired,
    };

    static defaultProps = {
        user: undefined,
    };

    logoutCurrentUser = () => {
        this.props.logout();
    };

    render() {
        return (
            <header>
                <span className="header__name">shopping-list-spark</span>
                {this.props.user
                    ? (
                        <>
                            <nav role="navigation" className="header__links">
                                <NavLink to="/lists">Lists</NavLink>
                                <NavLink to="/item-types">Items</NavLink>
                            </nav>
                            <nav role="navigation" className="header__logout">
                                <button type="button" onClick={this.logoutCurrentUser}>Logout</button>
                            </nav>
                        </>
                    )
                    : (
                        <nav role="navigation" className="header__links">
                            <NavLink to="/login">Login</NavLink>
                            <NavLink to="/register">Register</NavLink>
                        </nav>
                    )
                }
            </header>
        );
    }
}

export default connect(
    store => ({
        user: store.auth.currentUser,
    }),
    dispatch => ({
        logout: () => dispatch(logout()),
    }),
)(Header);
