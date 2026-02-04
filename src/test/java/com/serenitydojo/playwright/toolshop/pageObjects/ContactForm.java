package com.serenitydojo.playwright.toolshop.pageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.SelectOption;

import java.nio.file.Path;

public class ContactForm {

    Page page;
    private Locator firstNameField;
    private Locator lastNameField;
    private Locator emailNameField;
    private Locator messageField;
    private Locator subjectDropDown;
    private Locator sendButton;

    public ContactForm(Page page) {
        this.page = page;
        this.firstNameField = page.getByLabel("First name");
        this.lastNameField = page.getByLabel("Last name");
        this.emailNameField = page.getByLabel("Email address");
        this.messageField = page.getByLabel("Message");
        this.subjectDropDown = page.getByLabel("Subject");
        this.sendButton = page.getByText("Send");
    }

    public void setFirstName(String firstName) {
        firstNameField.fill(firstName);
    }

    public void setLastName(String lastName) {
        lastNameField.fill(lastName);
    }

    public void setEmail(String mail) {
        emailNameField.fill(mail);
    }

    public void setMessage(String message) {
        messageField.fill(message);
    }

    public void selectSubject(int index) {
        subjectDropDown.selectOption(new SelectOption().setIndex(index));
    }

    public void setAttachment(Path fileToUpload)
    {
        page.setInputFiles("#attachment", fileToUpload);
    }

    public void submitForm() {
        sendButton.click();
    }

    public String getAlertMessage() {
        return page.getByRole(AriaRole.ALERT).textContent();
    }
}
