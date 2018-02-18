import React from "react";
import {connect} from 'react-redux';
import PropTypes from 'prop-types';

class App extends React.Component {
    static propTypes = {
        user: PropTypes.string
    };

    static defaultProps = {
        user: undefined
    };

    render() {
        return (
            <div>Hello World</div>
        );
    }
}

export default connect(store => ({

}))(App);