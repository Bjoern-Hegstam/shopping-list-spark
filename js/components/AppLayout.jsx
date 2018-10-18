import React from 'react';
import PropTypes from 'prop-types';
import Header from './Header';

export default function AppLayout(props) {
    return (
        <div>
            <Header />
            <main>
                <div className="main-content">
                    {props.children}
                </div>
            </main>
        </div>
    );
}

AppLayout.propTypes = {
    children: PropTypes.node.isRequired,
};
