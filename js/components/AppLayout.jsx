import React from 'react';
import PropTypes from 'prop-types';
import Header from './Header';

export default function AppLayout(props) {
    return (
        <>
            <Header />
            <main>
                <div className="main-content">
                    {props.children}
                </div>
            </main>
        </>
    );
}

AppLayout.propTypes = {
    children: PropTypes.node,
};

AppLayout.defaultProps = {
    children: undefined,
};
