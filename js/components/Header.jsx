import React from 'react';
import {NavLink} from "react-router-dom";

import './Header.scss';

export default class Header extends React.Component {
    render() {
        return (
            <header>
                <div className="header-content">
                    <span>shopping-list-spark</span>
                    <nav>
                        <ul>
                            <li><NavLink to="/login">Login</NavLink></li>
                            <li><NavLink to="/register">Register</NavLink></li>
                        </ul>
                    </nav>
                </div>
            </header>
        );
    }
}