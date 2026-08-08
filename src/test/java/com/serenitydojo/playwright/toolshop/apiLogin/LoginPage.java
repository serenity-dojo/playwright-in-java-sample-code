package com.serenitydojo.playwright.toolshop.apiLogin;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.serenitydojo.playwright.toolshop.domain.User;

public class LoginPage {

    private final Page page;

    public LoginPage(Page page) {
        this.page = page;
    }

    public void open(){
        page.navigate("https://practicesoftwaretesting.com/auth/login");
    }

    public void loginAs(User user) {

        page.getByPlaceholder("Your email").fill(user.email());
        page.getByPlaceholder("Your password").fill(user.password());
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();


    }



    public String title() {
        return page.getByTestId("page-title").textContent();
    }

    public String loginErrorMessage() {
        return page.getByTestId("login-error").textContent();
    }
}
