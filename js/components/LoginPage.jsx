import React from 'react';
import AppLayout from "./AppLayout";

import './LoginPage.scss';

export default class LoginPage extends React.Component {
    render() {
        return (
            <AppLayout>
                <form action="#" id="login-form">
                    <input type="text" placeholder="Username"/>
                    <input type="password" placeholder="Password"/>
                    <div>
                        <button>Login</button>
                    </div>
                </form>
            </AppLayout>
        )
    }
}