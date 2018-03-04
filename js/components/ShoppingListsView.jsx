import React from 'react';
import AppLayout from "./AppLayout";
import {connect} from "react-redux";

export class ShoppingListsView extends React.Component {
    render() {
        return (
            <AppLayout>
                <div>List of lists</div>
            </AppLayout>
        )
    }
}

export default connect(store => ({}))(ShoppingListsView);