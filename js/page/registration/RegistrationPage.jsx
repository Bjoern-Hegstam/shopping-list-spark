import React from 'react';
import AppLayout from "../../components/AppLayout";
import RegistrationForm from "./RegistrationForm";

export default class RegistrationPage extends React.Component {
    onSubmit = (data) => {
    };

    render() {
        return (
            <AppLayout>
                <RegistrationForm onSubmit={this.onSubmit} />
            </AppLayout>
        );
    }
}
