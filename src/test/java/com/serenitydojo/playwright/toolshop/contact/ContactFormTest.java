package com.serenitydojo.playwright.toolshop.contact;

import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.options.AriaRole;
import com.serenitydojo.playwright.toolshop.catalog.pageobjects.NavBar;
import com.serenitydojo.playwright.toolshop.fixtures.PlaywrightTestCase;
import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.playwright.junit5.SerenityPlaywrightExtension;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@ExtendWith(SerenityJUnit5Extension.class)
@ExtendWith(SerenityPlaywrightExtension.class)
@DisplayName("Contact form")
@Feature("Contact form")
@Execution(ExecutionMode.SAME_THREAD)
public class ContactFormTest extends PlaywrightTestCase {

    ContactForm contactForm;
    NavBar navigate;

    @BeforeEach
    void openContactPage() {
        contactForm = new ContactForm(page);
        navigate = new NavBar(page);
        navigate.toTheContactPage();
    }

    @Story("Submitting a request")
    @DisplayName("Customers can use the contact form to contact us")
    @Test
    void completeForm() throws URISyntaxException {
        contactForm.setFirstName("Sarah-Jane");
        contactForm.setLastName("Smith");
        contactForm.setEmail("sarah@example.com");
        contactForm.setMessage("A very long message to the warranty service about a warranty on a product!");
        contactForm.selectSubject("Warranty");

        Path fileToUpload = Paths.get(ClassLoader.getSystemResource("data/sample-data.txt").toURI());
        contactForm.setAttachment(fileToUpload);

        contactForm.submitForm();

        Assertions.assertThat(contactForm.getAlertMessage())
                .contains("Thanks for your message! We will contact you shortly.");
    }

    @Story("Submitting a request")
    @DisplayName("First name, last name, email and message are mandatory")
    @ParameterizedTest(name = "{arguments} is a mandatory field")
    @ValueSource(strings = {"First name", "Last name", "Email", "Message"})
    void mandatoryFields(String fieldName) {
        // Fill in the field values
        contactForm.setFirstName("Sarah-Jane");
        contactForm.setLastName("Smith");
        contactForm.setEmail("sarah@example.com");
        contactForm.setMessage("A very long message to the warranty service about a warranty on a product!");
        contactForm.selectSubject("Warranty");

        // Clear one of the fields
        contactForm.clearField(fieldName);
        page.waitForTimeout(250);
        contactForm.submitForm();

        // Check the error message for that field
        var errorMessage = page.getByRole(AriaRole.ALERT).getByText(fieldName + " is required");

        assertThat(errorMessage).isVisible();
    }

    @Story("Submitting a request")
    @DisplayName("The message must be at least 50 characters long")
    @Test
    void messageTooShort() {

        contactForm.setFirstName("Sarah-Jane");
        contactForm.setLastName("Smith");
        contactForm.setEmail("sarah@example.com");
        contactForm.setMessage("A short long message.");
        contactForm.selectSubject("Warranty");

        contactForm.submitForm();

        assertThat(page.getByRole(AriaRole.ALERT)).hasText("Message must be minimal 50 characters");
    }

    @Story("Submitting a request")
    @DisplayName("The email address must be correctly formatted")
    @ParameterizedTest(name = "'{arguments}' should be rejected")
    @ValueSource(strings = {"not-an-email", "not-an.email.com", "notanemail"})
    void invalidEmailField(String invalidEmail) {
        contactForm.setFirstName("Sarah-Jane");
        contactForm.setLastName("Smith");
        contactForm.setEmail(invalidEmail);
        contactForm.setMessage("A very long message to the warranty service about a warranty on a product!");
        contactForm.selectSubject("Warranty");

        contactForm.submitForm();

        assertThat(page.getByRole(AriaRole.ALERT)).hasText("Email format is invalid");
    }
}