package com.serenitydojo.playwright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.SelectOption;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class PWFormTest extends BaseTest {

    @DisplayName("Interacting with text fields")
    @Nested
    class WhenInteractingWithTextFields
    {
        @BeforeEach
        void openContactPage()

        {
            page.navigate("https://practicesoftwaretesting.com/contact");
        }

        @DisplayName("Complete the form fields")
        @Test
        void completeForm() throws URISyntaxException {

            contactForm.setFirstName("Sarah");
            contactForm.setLastName("Marge");
            contactForm.setEmail("sarah.marge@test.com");
            contactForm.setMessage("Test");
            contactForm.selectSubject(3);

            Path fileToUpload = Paths.get(ClassLoader.getSystemResource("data/sample-data.txt").toURI());
            contactForm.setAttachment(fileToUpload);

            contactForm.submitForm();

            Assertions.assertThat(contactForm.getAlertMessage())
                    .contains("Thanks for your message! We will contact you");

//            assertThat(firstNameField).hasValue("Sarah");
//            assertThat(lastNameField).hasValue("Marge");
//            assertThat(emailNameField).hasValue("sarah.marge@test.com");
//            assertThat(messageField).hasValue("Hello, world!");
//            assertThat(subjectDropDown).hasValue("return");
//
//            String uploadedFile = uploadField.inputValue();
//            Assertions.assertThat(uploadedFile).endsWith("sample-data.txt");
        }

        @DisplayName("Mandatory fields")
        @ParameterizedTest
        @ValueSource(strings = {"First name", "Last name", "Email", "Message"})
        void mandatoryFields(String fieldName)
        {
            var firstNameField = page.getByLabel("First name");
            var lastNameField = page.getByLabel("Last name");
            var emailNameField = page.getByLabel("Email address");
            var messageField = page.getByLabel("Message");
            var sendButton = page.getByText("Send");

            //fill in the fields
            firstNameField.fill("Sarah");
            lastNameField.fill("Marge");
            emailNameField.fill("sarah.marge@test.com");
            messageField.fill("Hello, world!");

            //clear one of the fields
            page.getByLabel(fieldName).clear();
            sendButton.click();

            // check the error message
            var errorMessage = page.getByRole(AriaRole.ALERT).getByText(fieldName + " is required");
            assertThat(errorMessage).isVisible();
        }
    }
}
