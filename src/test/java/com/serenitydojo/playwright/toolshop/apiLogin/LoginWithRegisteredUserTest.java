package com.serenitydojo.playwright.toolshop.apiLogin;

import com.serenitydojo.playwright.toolshop.domain.User;
import com.serenitydojo.playwright.toolshop.fixtures.PlaywrightTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginWithRegisteredUserTest extends PlaywrightTestCase {

    @Test
    @DisplayName("Should be able to login with a registered user")
    void should_login_with_registered_user(){

        User user = User.randomUser();
        UserAPIClient userAPIClient = new UserAPIClient(page);
        userAPIClient.registerUser(user);

        LoginPage loginPage = new LoginPage(page);
        loginPage.open();
        loginPage.loginAs(user);

        assertThat(loginPage.title()).isEqualTo("My account");

    }

    @Test
    void should_reject_user_with_invalid_password(){

        User user = User.randomUser();
        UserAPIClient userAPIClient = new UserAPIClient(page);
        userAPIClient.registerUser(user);

        LoginPage loginPage = new LoginPage(page);
        loginPage.open();
        loginPage.loginAs(user.withPassword("wrong-password"));

        assertThat(loginPage.loginErrorMessage()).isEqualTo("Invalid email or password");
    }


}
