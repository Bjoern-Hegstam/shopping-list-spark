import React from 'react';
import * as PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { NavLink } from 'react-router-dom';
import { logout } from '../actions/UserActions';
import { UserType } from '../propTypes';

export function Header(props) {
  const logoutCurrentUser = () => {
    props.logout();
  };

  return (
    <header>
      {props.user ? (
        <>
          <NavLink to="/lists" className="header__name">shopping-list-spark</NavLink>
          <button className="header__logout-button" type="button" onClick={logoutCurrentUser}>Logout</button>
        </>
      ) : (
        <span className="header__name">shopping-list-spark</span>
      )}
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
