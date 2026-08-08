package com.serenitydojo.playwright.toolshop.apiLogin;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.RequestOptions;
import com.serenitydojo.playwright.toolshop.domain.User;

import java.util.Arrays;

public class UserAPIClient {

    private final Page page;
    private static final String REGISTER_USER = "https://api.practicesoftwaretesting.com/users/register";
    public UserAPIClient(Page page) {
        this.page = page;
    }

    public void registerUser(User user) {

        var response = page.request().post(
                REGISTER_USER,
                RequestOptions.create()
                        .setData(user)
                        .setHeader("Content-Type", "application/json")
                        .setHeader("accept","application/json")

        );
        System.out.println(response.text());
        if(response.status() != 201){
            throw new IllegalStateException("Unexpected result: " + response.status());
        }
    }
}
