package com.serenitydojo.playwright.toolshop.apiLogin;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.RequestOptions;
import com.serenitydojo.playwright.toolshop.domain.Address;
import com.serenitydojo.playwright.toolshop.domain.User;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@UsePlaywright
public class RegisterAPITest {

    private APIRequestContext request;
    private Gson gson = new Gson();
    @BeforeEach
    void setup(Playwright playwright){
        request = playwright.request().newContext(
                new APIRequest.NewContextOptions()
                        .setBaseURL("https://api.practicesoftwaretesting.com")
        );
    }

    @AfterEach
    void tearDown(){
        if (request != null){
            request.dispose();
        }
    }


    @Test
    void should_register_user(){
        User validUser = User.randomUser();

        var response = request.post("/users/register",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(validUser)
        );

        String responseBody = response.text();

        System.out.println("Request user:");
        System.out.println(new Gson().toJson(validUser));

        System.out.println("Response status: " + response.status());
        System.out.println("Response body:");
        System.out.println(responseBody);


        User createdUser = gson.fromJson(responseBody, User.class);

        JsonObject responseObject = gson.fromJson(responseBody, JsonObject.class);


        assertSoftly(softly -> {
                softly.assertThat(response.status())
                        .as("Registration should return 201 created status code")
                        .isEqualTo(201);

                    softly.assertThat(createdUser)
                            .isEqualTo(validUser.withPassword(null));

                    //response object doesn't have password field
                    assertThat(responseObject.has("password"))
                            .isFalse();

                    softly.assertThat(responseObject.get("id").getAsString())
                            .as("Register user Id")
                            .isNotEmpty();


    }
        );


    }

    @Test
    void firstNameIsMandatory(){

        Address address = new Address("street", "44","mly","ttt","nl","77kk");

        User userWithNoName = new User(
                null,
                "Smith",
                address,
                "+316878996789",
                "1990-01-01",
                "78678kjhkAs#$",
                "tyfteyt@gmail.com"
        );

        var response = request.post("/users/register",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(userWithNoName)
        );

        assertSoftly(softly -> {
            softly.assertThat(response.status()).isEqualTo(422);

            JsonObject responseObject = gson.fromJson(response.text(), JsonObject.class);
            softly.assertThat(responseObject.has("first_name")).isTrue();

            String errorMessage = responseObject.get("first_name").getAsString();
            softly.assertThat(errorMessage).isEqualTo("The first name field is required.");

        });



    }



}
