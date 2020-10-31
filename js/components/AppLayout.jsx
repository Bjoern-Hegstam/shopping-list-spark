import React from 'react';
import * as PropTypes from 'prop-types';
import { connect } from 'react-redux';
import classnames from 'classnames';
import Header from './Header';
import { isUserAuthenticatedSelector } from '../selectors/UserSelectors';

export function AppLayout(props) {
  const layoutClassNames = classnames(
    'app-layout',
    { 'user--authenticated': props.userAuthenticated },
  );

  return (
    <div className={layoutClassNames}>
      <Header />
      <div className="main-container">
        <main>
          {props.children}
        </main>
      </div>
    </div>
  );
}

AppLayout.propTypes = {
  userAuthenticated: PropTypes.bool.isRequired,
  children: PropTypes.node,
};

AppLayout.defaultProps = {
  children: undefined,
};

const mapStateToProps = state => ({
  userAuthenticated: isUserAuthenticatedSelector(state),
});

export default connect(mapStateToProps, null)(AppLayout);
