package com.serenitydojo.playwright.pages;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class RegistrationPage {

    private final Page page;

    public RegistrationPage(Page page) {
        this.page = page;
    }

    @Step("Open registration page")
    public RegistrationPage open() {
        page.navigate("/auth/register");
        return this;
    }

    @Step("Enter first name")
    public RegistrationPage enterFirstName(String value) {
        page.getByLabel("First name").fill(value);
        return this;
    }

    @Step("Enter last name")
    public RegistrationPage enterLastName(String value) {
        page.getByLabel("Last name").fill(value);
        return this;
    }

    @Step("Enter date of birth")
    public RegistrationPage enterDateOfBirth(String value) {
        page.getByTestId("dob").fill(value);
        return this;
    }

    @Step("Select country")
    public RegistrationPage selectCountry(String value) {
        page.getByLabel("Country").selectOption(value);
        return this;
    }

    @Step("Enter postal code")
    public RegistrationPage enterPostalCode(String value) {
        page.getByLabel("Postal code").fill(value);
        return this;
    }

    @Step("Enter house number")
    public RegistrationPage enterHouseNumber(String value) {
        page.getByLabel("House number").fill(value);
        return this;
    }

    @Step("Enter street")
    public RegistrationPage enterStreet(String value) {
        page.getByLabel("Street").fill(value);
        return this;
    }

    @Step("Enter city")
    public RegistrationPage enterCity(String value) {
        page.getByLabel("City").fill(value);
        return this;
    }

    @Step("Enter state")
    public RegistrationPage enterState(String value) {
        page.getByLabel("State").fill(value);
        return this;
    }

    @Step("Enter phone")
    public RegistrationPage enterPhone(String value) {
        page.getByLabel("Phone").fill(value);
        return this;
    }

    @Step("Enter email")
    public RegistrationPage enterEmail(String value) {
        page.getByLabel("Email address").fill(value);
        return this;
    }

    @Step("Enter password")
    public RegistrationPage enterPassword(String value) {
        page.getByTestId("password").fill(value);
        return this;
    }

    @Step("Submit registration form")
    public void submit() {
        page.getByTestId("register-submit").click();
    }

    @Step("Wait for login redirect")
    public void waitForLoginRedirect() {
        page.waitForURL("**/auth/login");
    }

    public String fieldError(String fieldKey) {
        return page.getByTestId(fieldKey + "-error").textContent().trim();
    }

    public String registrationError() {
        return page.getByTestId("register-error").textContent().trim();
    }

    public boolean hasFieldError(String fieldKey) {
        return page.getByTestId(fieldKey + "-error").isVisible();
    }

    public boolean hasRegistrationError() {
        return page.getByTestId("register-error").isVisible();
    }

    public String currentUrl() {
        return page.url();
    }
}
