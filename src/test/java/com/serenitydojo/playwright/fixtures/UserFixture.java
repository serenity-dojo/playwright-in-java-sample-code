package com.serenitydojo.playwright.fixtures;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.options.RequestOptions;
import com.serenitydojo.playwright.domain.Customer;
import io.qameta.allure.Step;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class UserFixture {

    @Step("Register customer via API")
    public static Customer registerViaApi(APIRequestContext api) {
        var customer = Customer.random();
        var response = api.post(
                "https://api.practicesoftwaretesting.com/users/register",
                RequestOptions.create().setData(Map.of(
                        "first_name", customer.firstName(),
                        "last_name", customer.lastName(),
                        "dob", customer.dob(),
                        "phone", customer.phone(),
                        "email", customer.email(),
                        "password", customer.password(),
                        "address", Map.of(
                                "street", customer.street(),
                                "city", customer.city(),
                                "state", customer.state(),
                                "country", "US",
                                "postal_code", customer.postalCode()
                        )
                ))
        );
        assertThat(response.status())
                .as("API registration failed: " + response.text())
                .isEqualTo(201);
        return customer;
    }
}
