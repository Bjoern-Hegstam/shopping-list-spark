import React from 'react';
import {NavLink} from "react-router-dom";

import './Header.scss';
import {connect} from "react-redux";
import {UserType} from "../propTypes";

export class Header extends React.Component {
    static propTypes = {
        user: UserType
    };

    static defaultProps = {
        user: undefined
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

export default connect(store => ({
    user: store.user.user
}))(Header);