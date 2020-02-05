import React from 'react';
import * as PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { registerUser } from '../../actions/UserActions';
import * as types from '../../actions/types';
import { createErrorSelector, createLoadingSelector } from '../../selectors/RequestSelectors';

export class RegistrationForm extends React.Component {
    static propTypes = {
      registerUser: PropTypes.func.isRequired,
      registeringUser: PropTypes.bool.isRequired,
      errorRegisterUser: PropTypes.object,
    };

    static defaultProps = {
      errorRegisterUser: undefined,
    };

    initialState = {
      username: '',
      password: '',
      email: '',
    };

    state = { ...this.initialState };

    componentDidUpdate(prevProps) {
      if (prevProps.registeringUser && !this.props.registeringUser && !this.props.errorRegisterUser) {
        this.setState({ ...this.initialState });
      }
    }

    handleUsernameChanged = e => {
      this.setState({ username: e.target.value });
    };

    handlePasswordChanged = e => {
      this.setState({ password: e.target.value });
    };

    handleEmailChanged = e => {
      this.setState({ email: e.target.value });
    };

    onSubmit = () => {
      const { username, password, email } = this.state;
      if (this.isFormValid()) {
        this.props.registerUser({ username, password, email });
      }
    };

    isFormValid() {
      const { username, password, email } = this.state;

      return username.trim() !== ''
            && password.trim() !== ''
            && email.trim() !== '';
    }

    render() {
      const { username, password, email } = this.state;

      return (
        <form className="registration-form" onSubmit={this.onSubmit}>
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
            <button type="submit" disabled={!this.isFormValid()}>Register</button>
          </div>
        </form>
      );
    }
}

const mapStateToProps = state => ({
  registeringUser: createLoadingSelector(types.REGISTER_USER)(state),
  errorRegisterUser: createErrorSelector(types.REGISTER_USER)(state),
});

export default connect(mapStateToProps, { registerUser })(RegistrationForm);
