package com.serenitydojo.playwright;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@UsePlaywright(HeadlessChromeOptions.class)
public class PlaywrightFormsTest {

    @DisplayName("Interacting with text fields")
    @Nested
    class WhenInteractingWithTextFields {

        @BeforeEach
        void openContactPage(Page page) {
            page.navigate("https://practicesoftwaretesting.com/contact");
        }

        @DisplayName("Complete the form")
        @Test
        void completeForm(Page page) throws URISyntaxException {
            var firstNameField = page.getByLabel("First name");
            var lastNameField = page.getByLabel("Last name");
            var emailNameField = page.getByLabel("Email");
            var messageField = page.getByLabel("Message");
            var subjectField = page.getByLabel("Subject");
            var uploadField = page.getByLabel("Attachment");

            firstNameField.fill("Erhan");
            lastNameField.fill("Batu");
            emailNameField.fill("erbatu@gmail.com");
            messageField.fill("Hello World!");
            subjectField.selectOption("Warranty");

            Path fileToUpload = Path.of(
                    ClassLoader.getSystemResource("data/sample-data.txt").toURI()
            );

            page.setInputFiles("#attachment", fileToUpload);


            assertThat(firstNameField).hasValue("Erhan");
            assertThat(lastNameField).hasValue("Batu");
            assertThat(subjectField).hasValue("warranty");

            String uploadFile = uploadField.inputValue();
            org.assertj.core.api.Assertions.assertThat(uploadFile).endsWith("sample-data.txt");



        }

        @DisplayName("Mandatory Fields")
        @ParameterizedTest
        @ValueSource(strings = {"First name", "Last name"})
        void mandatoryFields(String fieldName, Page page){
            var firstNameField = page.getByLabel("First name");
            var lastNameField = page.getByLabel("Last name");
            var emailNameField = page.getByLabel("Email");
            var messageField = page.getByLabel("Message");
            var subjectField = page.getByLabel("Subject");
            var sendButton = page.getByText("Send");

            firstNameField.fill("Erhan");
            lastNameField.fill("Batu");
            emailNameField.fill("erbatu@gmail.com");
            messageField.fill("Hello World!");
            subjectField.selectOption("Warranty");

            page.getByLabel(fieldName).clear();

            sendButton.click();

            var errorMessage = page.getByRole(AriaRole.ALERT).getByText(fieldName + " is required");

            assertThat(errorMessage).isVisible();


        }

        @DisplayName("Categories Tests")
        @Test
        void categoriesTests(Page page){

            page.getByRole(AriaRole.BUTTON).getByText("Categories").click();
            page.getByLabel("nav-categories").getByText("Power Tools").click();

            assertThat(page).hasTitle(Pattern.compile(".*Power Tools.*"));

            String circularSawPrice = page.locator(".card").filter(
                    new Locator.FilterOptions()
                            .setHasText("circular saw"))
                    .locator("[data-test='product-price']").innerText();
            System.out.println(circularSawPrice);

            Assertions.assertEquals(circularSawPrice, "$80.19");


        }

        @DisplayName("CheckBox Test")
        @Test
        void checkBoxTest(Page page){

            page.getByRole(AriaRole.BUTTON).getByText("Categories").click();
            page.getByLabel("nav-categories").getByText("Power Tools").click();

            page.getByRole(AriaRole.LIST).locator(".checkbox")
                            .filter(
                                    new Locator.FilterOptions().setHasText("Grinder")
                            ).locator("input[type='checkbox']").check();


            assertThat(page.getByText("There are no products found")).isVisible();

            page.getByRole(AriaRole.LIST).locator(".checkbox").filter(
                    new Locator.FilterOptions().setHasText("Sander")
            ).locator("[name='category_id']").check();

            Locator productCards = page.locator(".card-img-top");

            assertThat(productCards).hasCount(3);

            System.out.println(productCards.count());

        }

        @DisplayName("Dropdowns Test")
        @Test
        void dropDownTesting(Page page){

            page.getByRole(AriaRole.BUTTON).getByText("Categories").click();
            page.getByLabel("nav-categories").getByText("Power Tools").click();

            page.getByRole(AriaRole.COMBOBOX).selectOption("Price (High - Low)");

        }





    }







}
