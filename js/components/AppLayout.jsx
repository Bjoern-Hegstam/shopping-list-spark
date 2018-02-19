import React from 'react';
import Header from "./Header";

import './AppLayout.scss';

export default class AppLayout extends React.Component {
    render() {
        return (
            <div>
                <Header/>
                <main>
                    <div className="main-content">
                        {this.props.children}
                    </div>
                </main>
            </div>
        );
    }
}