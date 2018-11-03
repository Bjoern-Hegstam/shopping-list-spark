import React from 'react';
import PropTypes from 'prop-types';
import Header from './Header';

export default function AppLayout(props) {
    return (
        <div className="app-layout">
            <Header />
            <main>
                {props.children}
            </main>
        </div>
    );
}

AppLayout.propTypes = {
    children: PropTypes.node,
};

AppLayout.defaultProps = {
    children: undefined,
};
