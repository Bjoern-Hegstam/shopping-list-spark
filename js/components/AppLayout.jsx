import React from 'react';
import * as PropTypes from 'prop-types';
import Header from './Header';
import Navigation from './Navigvation';

export default function AppLayout(props) {
  return (
    <div className="app-layout">
      <Header />
      <div className="nav-container">
        <Navigation />
      </div>
      <div className="main-container">
        <main>
          {props.children}
        </main>
      </div>
    </div>
  );
}

AppLayout.propTypes = {
  children: PropTypes.node,
};

AppLayout.defaultProps = {
  children: undefined,
};
