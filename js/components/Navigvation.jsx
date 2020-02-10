import React from 'react';
import { NavLink } from 'react-router-dom';
import * as PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { logout } from '../actions/UserActions';
import { UserType } from '../propTypes';

export function Navigation(props) {
  const logoutCurrentUser = () => {
    props.logout();
  };

  return (
    <div className="navigation">
      {props.user
        ? (
          <>
            <nav role="navigation" className="navigation__links">
              <ul>
                <li><NavLink to="/lists" activeClassName="navigation__link--active">Lists</NavLink></li>
              </ul>
            </nav>
            <nav role="navigation" className="navigation__logout">
              <button type="button" onClick={logoutCurrentUser}>Logout</button>
            </nav>
          </>
        )
        : (
          <nav role="navigation" className="navigation__links">
            <ul>
              <li><NavLink to="/login" activeClassName="navigation__link--active">Login</NavLink></li>
              {/* <li><NavLink to="/register" activeClassName="navigation__link--active">Register</NavLink></li> */}
            </ul>
          </nav>
        )}
    </div>
  );
}

Navigation.propTypes = {
  user: UserType,
  logout: PropTypes.func.isRequired,
};

Navigation.defaultProps = {
  user: undefined,
};

export default connect(
  state => ({
    user: state.auth.currentUser,
  }),
  { logout },
)(Navigation);
