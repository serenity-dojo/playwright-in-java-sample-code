package com.serenitydojo.playwright.tests;

import com.microsoft.playwright.BrowserContext;
import com.serenitydojo.playwright.domain.Customer;
import com.serenitydojo.playwright.fixtures.PlaywrightTest;
import com.serenitydojo.playwright.fixtures.TakeFinalScreenshot;
import com.serenitydojo.playwright.fixtures.UserFixture;
import com.serenitydojo.playwright.workflows.RegistrationWorkflow;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@PlaywrightTest
@DisplayName("Customer Registration")
class CustomerRegistrationIT implements TakeFinalScreenshot {

    @Nested
    @DisplayName("Happy Path")
    class HappyPath {

        @Test
        @Story("Customer Registration")
        @Severity(SeverityLevel.BLOCKER)
        @Description("AC-1: A user can register with valid details and is redirected to the login page")
        @DisplayName("AC-1: Valid registration with all required details redirects to login page")
        void validRegistrationRedirectsToLogin(BrowserContext context) {
            var workflow = new RegistrationWorkflow(context);
            workflow.register(Customer.random());
            assertThat(workflow.currentUrl()).contains("/auth/login");
        }
    }

    @Nested
    @DisplayName("Required Field Validation")
    class RequiredFieldValidation {

        @Test
        @Story("Customer Registration")
        @Severity(SeverityLevel.CRITICAL)
        @Description("AC-2: Submitting an empty form shows a validation error for every required field")
        @DisplayName("AC-2: Empty form submission shows all required-field errors")
        void emptyFormShowsAllRequiredFieldErrors(BrowserContext context) {
            var workflow = new RegistrationWorkflow(context);
            workflow.submitEmptyForm();

            SoftAssertions.assertSoftly(soft -> {
                soft.assertThat(workflow.fieldError("first-name")).contains("First name is required");
                soft.assertThat(workflow.fieldError("last-name")).contains("Last name is required");
                soft.assertThat(workflow.fieldError("dob")).contains("Date of Birth is required");
                soft.assertThat(workflow.fieldError("country")).contains("Country is required");
                soft.assertThat(workflow.fieldError("postal_code")).contains("Postcode is required");
                soft.assertThat(workflow.fieldError("house_number")).contains("House number is required");
                soft.assertThat(workflow.fieldError("street")).contains("Street is required");
                soft.assertThat(workflow.fieldError("city")).contains("City is required");
                soft.assertThat(workflow.fieldError("state")).contains("State is required");
                soft.assertThat(workflow.fieldError("phone")).contains("Phone is required");
                soft.assertThat(workflow.fieldError("email")).contains("Email is required");
                soft.assertThat(workflow.fieldError("password")).contains("Password is required");
            });
        }

        @Test
        @Story("Customer Registration")
        @Severity(SeverityLevel.NORMAL)
        @Description("AC-2: A date of birth in the wrong format is rejected with a format hint")
        @DisplayName("AC-2: Date of birth not in YYYY-MM-DD format shows format error")
        void invalidDobFormatShowsError(BrowserContext context) {
            var workflow = new RegistrationWorkflow(context);
            workflow.registerWithDob("24-04-1990");
            assertThat(workflow.fieldError("dob"))
                    .contains("Please enter a valid date in YYYY-MM-DD format");
        }
    }

    @Nested
    @DisplayName("Email Validation")
    class EmailValidation {

        @Test
        @Story("Customer Registration")
        @Severity(SeverityLevel.CRITICAL)
        @Description("AC-3: A value with no @ symbol is rejected with an email format error")
        @DisplayName("AC-3: Plain text with no @ shows email format error")
        void plainTextEmailShowsFormatError(BrowserContext context) {
            var workflow = new RegistrationWorkflow(context);
            workflow.registerWithInvalidEmail(Customer.invalidEmailAddress());
            assertThat(workflow.fieldError("email")).contains("Email format is invalid");
        }

        @Test
        @Story("Customer Registration")
        @Severity(SeverityLevel.NORMAL)
        @Description("AC-3: An email with no domain after @ is rejected with a format error")
        @DisplayName("AC-3: Email missing domain part shows format error")
        void emailMissingDomainShowsFormatError(BrowserContext context) {
            var workflow = new RegistrationWorkflow(context);
            workflow.registerWithInvalidEmail("user@");
            assertThat(workflow.fieldError("email")).contains("Email format is invalid");
        }
    }

    @Nested
    @DisplayName("Password Validation")
    class PasswordValidation {

        @Test
        @Story("Customer Registration")
        @Severity(SeverityLevel.CRITICAL)
        @Description("AC-4: A password shorter than the minimum length is rejected")
        @DisplayName("AC-4: Password shorter than minimum length shows length error")
        void shortPasswordShowsLengthError(BrowserContext context) {
            var workflow = new RegistrationWorkflow(context);
            workflow.registerWithPassword(Customer.shortPassword());
            assertThat(workflow.fieldError("password"))
                    .contains("Password must be minimal 6 characters long");
        }
    }

    @Nested
    @DisplayName("Duplicate Email")
    class DuplicateEmailCheck {

        @Test
        @Story("Customer Registration")
        @Severity(SeverityLevel.CRITICAL)
        @Description("AC-5: Attempting to register with an already-used email is rejected")
        @DisplayName("AC-5: Registering with an already-registered email shows duplicate error")
        void duplicateEmailShowsDuplicateError(BrowserContext context) {
            var existingCustomer = UserFixture.registerViaApi(context.request());
            var workflow = new RegistrationWorkflow(context);
            workflow.registerWithEmail(existingCustomer.email());
            assertThat(workflow.registrationError())
                    .contains("A customer with this email address already exists");
        }
    }
}
