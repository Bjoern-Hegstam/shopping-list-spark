import React from 'react';
import AppLayout from "../../components/AppLayout";
import PropTypes from 'prop-types';

import './LoginPage.scss';
import { connect } from "react-redux";
import { login } from "../../actions/UserActions";

export class LoginPage extends React.Component {
    static propTypes = {
        login: PropTypes.func.isRequired
    };

    state = {
        username: '',
        password: ''
    };

    onUsernameChange = (e) => {
        e.preventDefault();
        this.setState({username: e.target.value});
    };

    onPasswordChange = (e) => {
        e.preventDefault();
        this.setState({password: e.target.value});
    };

    onLoginSubmit = (e) => {
        e.preventDefault();
        const { username, password } = this.state;
        this.props.login({ username, password });
    };

    render() {
        return (
            <AppLayout>
                <form className="login-form" onSubmit={this.onLoginSubmit}>
                    <input
                        type="text"
                        placeholder="Username"
                        value={this.state.username}
                        onChange={this.onUsernameChange}
                    />
                    <input
                        type="password"
                        placeholder="Password"
                        value={this.state.password}
                        onChange={this.onPasswordChange}
                    />
                    <div>
                        <button type="submit">Login</button>
                    </div>
                </form>
            </AppLayout>
        )
    }
}

export default connect(
    undefined,
    (dispatch) => ({
        login: args => dispatch(login(args))
    })
)(LoginPage);