import React from 'react';
import AppLayout from "../../components/AppLayout";
import RegistrationForm from "./RegistrationForm";

export default class RegistrationPage extends React.Component {
    render() {
        return (
            <AppLayout>
                <RegistrationForm />
            </AppLayout>
        );
    }
}
