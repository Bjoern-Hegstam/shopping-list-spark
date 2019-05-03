import React from 'react';
import * as PropTypes from 'prop-types';
import { NavLink } from 'react-router-dom';
import { connect } from 'react-redux';

import { UserType } from '../propTypes';
import { logout } from '../actions/UserActions';

export function Header(props) {
    const logoutCurrentUser = () => {
        props.logout();
    };

    return (
        <header>
            <span className="header__name">shopping-list-spark</span>
            {props.user
                ? (
                    <>
                        <nav role="navigation" className="header__links">
                            <NavLink to="/lists">Lists</NavLink>
                            <NavLink to="/item-types">Items</NavLink>
                        </nav>
                        <nav role="navigation" className="header__logout">
                            <button type="button" onClick={logoutCurrentUser}>Logout</button>
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

Header.propTypes = {
    user: UserType,
    logout: PropTypes.func.isRequired,
};

Header.defaultProps = {
    user: undefined,
};

export default connect(
    state => ({
        user: state.auth.currentUser,
    }),
    { logout },
)(Header);
