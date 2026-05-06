package com.serenitydojo.playwright.workflows;

import com.microsoft.playwright.BrowserContext;
import com.serenitydojo.playwright.domain.Customer;
import com.serenitydojo.playwright.pages.RegistrationPage;
import io.qameta.allure.Step;

public class RegistrationWorkflow {

    private final RegistrationPage registrationPage;

    public RegistrationWorkflow(BrowserContext context) {
        this.registrationPage = new RegistrationPage(context.newPage());
    }

    @Step("Register customer with valid details")
    public RegistrationWorkflow register(Customer customer) {
        registrationPage
                .open()
                .enterFirstName(customer.firstName())
                .enterLastName(customer.lastName())
                .enterDateOfBirth(customer.dob())
                .selectCountry(customer.country())
                .enterPostalCode(customer.postalCode())
                .enterHouseNumber(customer.houseNumber())
                .enterStreet(customer.street())
                .enterCity(customer.city())
                .enterState(customer.state())
                .enterPhone(customer.phone())
                .enterEmail(customer.email())
                .enterPassword(customer.password())
                .submit();
        registrationPage.waitForLoginRedirect();
        return this;
    }

    @Step("Submit empty registration form")
    public RegistrationWorkflow submitEmptyForm() {
        registrationPage.open().submit();
        return this;
    }

    @Step("Attempt registration with invalid email format")
    public RegistrationWorkflow registerWithInvalidEmail(String invalidEmail) {
        var customer = Customer.random();
        registrationPage
                .open()
                .enterFirstName(customer.firstName())
                .enterLastName(customer.lastName())
                .enterDateOfBirth(customer.dob())
                .selectCountry(customer.country())
                .enterPostalCode(customer.postalCode())
                .enterHouseNumber(customer.houseNumber())
                .enterStreet(customer.street())
                .enterCity(customer.city())
                .enterState(customer.state())
                .enterPhone(customer.phone())
                .enterEmail(invalidEmail)
                .enterPassword(customer.password())
                .submit();
        return this;
    }

    @Step("Attempt registration with specific password")
    public RegistrationWorkflow registerWithPassword(String password) {
        var customer = Customer.random();
        registrationPage
                .open()
                .enterFirstName(customer.firstName())
                .enterLastName(customer.lastName())
                .enterDateOfBirth(customer.dob())
                .selectCountry(customer.country())
                .enterPostalCode(customer.postalCode())
                .enterHouseNumber(customer.houseNumber())
                .enterStreet(customer.street())
                .enterCity(customer.city())
                .enterState(customer.state())
                .enterPhone(customer.phone())
                .enterEmail(customer.email())
                .enterPassword(password)
                .submit();
        return this;
    }

    @Step("Attempt registration with an already-registered email")
    public RegistrationWorkflow registerWithEmail(String email) {
        var customer = Customer.random();
        registrationPage
                .open()
                .enterFirstName(customer.firstName())
                .enterLastName(customer.lastName())
                .enterDateOfBirth(customer.dob())
                .selectCountry(customer.country())
                .enterPostalCode(customer.postalCode())
                .enterHouseNumber(customer.houseNumber())
                .enterStreet(customer.street())
                .enterCity(customer.city())
                .enterState(customer.state())
                .enterPhone(customer.phone())
                .enterEmail(email)
                .enterPassword(customer.password())
                .submit();
        return this;
    }

    @Step("Attempt registration with specific date of birth")
    public RegistrationWorkflow registerWithDob(String dob) {
        var customer = Customer.random();
        registrationPage
                .open()
                .enterFirstName(customer.firstName())
                .enterLastName(customer.lastName())
                .enterDateOfBirth(dob)
                .selectCountry(customer.country())
                .enterPostalCode(customer.postalCode())
                .enterHouseNumber(customer.houseNumber())
                .enterStreet(customer.street())
                .enterCity(customer.city())
                .enterState(customer.state())
                .enterPhone(customer.phone())
                .enterEmail(customer.email())
                .enterPassword(customer.password())
                .submit();
        return this;
    }

    public String fieldError(String fieldKey) {
        return registrationPage.fieldError(fieldKey);
    }

    public String registrationError() {
        return registrationPage.registrationError();
    }

    public String currentUrl() {
        return registrationPage.currentUrl();
    }
}
