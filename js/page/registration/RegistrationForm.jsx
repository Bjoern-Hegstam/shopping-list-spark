import React from "react";
import PropTypes from 'prop-types';

import './RegistrationForm.scss';

export default class RegistrationForm extends React.Component {
    static propTypes = {
        onSubmit: PropTypes.func.isRequired,
    };

    state = {
        username: '',
        password: '',
        email: ''
    };

    handleUsernameChanged = (e) => {
        this.setState({ username: e.target.value });
    };

    handlePasswordChanged = (e) => {
        this.setState({ password: e.target.value });
    };

    handleEmailChanged = (e) => {
        this.setState({ email: e.target.value });
    };

    handleSubmit = () => {
        this.props.onSubmit({ ...this.state });
    };

    render() {
        const { username, password, email } = this.state;

        return (
            <form className="registration-form" onSubmit={this.handleSubmit}>
                <input
                    type="text"
                    placeholder="Username"
                    value={username}
                    onChange={this.handleUsernameChanged}
                />

                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={this.handlePasswordChanged}
                />

                <input
                    type="text"
                    placeholder="Email"
                    value={email}
                    onChange={this.handleEmailChanged}
                />

                <div>
                    <button type="submit">Register</button>
                </div>
            </form>
        );
    }
}