import React from 'react';
import PropTypes from 'prop-types';
import {NavLink} from "react-router-dom";

import './Header.scss';
import {connect} from "react-redux";
import {UserType} from "../propTypes";
import {logout} from "../actions/UserActions";

export class Header extends React.Component {
    static propTypes = {
        user: UserType,
        logout: PropTypes.func.isRequired
    };

    static defaultProps = {
        user: undefined
    };

    logoutCurrentUser = () => {
        this.props.logout();
    };

    render() {
        return (
            <header>
                <div className="header-content">
                    <span>shopping-list-spark</span>
                    {this.props.user ?
                        <nav>
                            <ul>
                                <li><NavLink to="/lists">Lists</NavLink></li>
                                <li><a href="#" onClick={this.logoutCurrentUser}>Logout</a></li>
                            </ul>
                        </nav>
                        :
                        <nav>
                            <ul>
                                <li><NavLink to="/login">Login</NavLink></li>
                                <li><NavLink to="/register">Register</NavLink></li>
                            </ul>
                        </nav>
                    }
                </div>
            </header>
        );
    }
}

export default connect(
    store => ({
        user: store.user.currentUser
    }),
    dispatch => ({
        logout: () => dispatch(logout())
    })
)(Header);