package com.serenitydojo.playwright.toolshop.login;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.RequestOptions;
import com.serenitydojo.playwright.toolshop.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@UsePlaywright
public class RegisterUserAPITest {

    private APIRequestContext request;
    private Gson gson = new Gson();

    @BeforeEach
    void setup(Playwright playwright) {
        request = playwright.request().newContext(
                new APIRequest.NewContextOptions().setBaseURL("https://api.practicesoftwaretesting.com")
        );
    }

    @Test
    void first_name_is_mandatory() {
        User userWithNoName = User.randomUser().withFirstName(null);

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


    @AfterEach
    void tearDown() {
        if (request != null) {
            request.dispose();
        }
    }

    @Test
    void should_register_user() {
        User validUser = User.randomUser();

        var response = request.post("/users/register",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(validUser)
        );

        String responseBody = response.text();
        User createdUser = gson.fromJson(responseBody, User.class);

        JsonObject responseObject = gson.fromJson(responseBody, JsonObject.class);

        assertSoftly(softly -> {
            softly.assertThat(response.status())
                    .as("Registration should return 201 created status code")
                    .isEqualTo(201);

            softly.assertThat(createdUser)
                    .as("Created user should match the specified user without the password")
                    .isEqualTo(validUser.withPassword(null));

            assertThat(responseObject.has("password"))
                    .as("No password should be returned")
                    .isFalse();

            softly.assertThat(responseObject.get("id").getAsString())
                    .as("Registered user should have an id")
                    .isNotEmpty();

            softly.assertThat(response.headers().get("content-type")).contains("application/json");
        });
    }


    @Test
    void should_not_register_user() {
        User invalidUser = User.randomUserWithNullField("firstName");

        var response = request.post("/users/register",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(invalidUser)
        );

        String responseBody = response.text();
//        User createdUser = gson.fromJson(responseBody, User.class);
        assertSoftly(softly -> {
            softly.assertThat(responseBody).isEqualTo("{\"first_name\":[\"The first name field is required.\"]}")
                            .as("First name field is required");
            softly.assertThat(response.status()).isEqualTo(422);
        });


        JsonObject responseObject = gson.fromJson(responseBody, JsonObject.class);
        System.out.println(responseObject);
    }

}
