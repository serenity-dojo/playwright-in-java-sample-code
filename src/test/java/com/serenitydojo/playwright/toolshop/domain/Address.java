package com.serenitydojo.playwright.toolshop.domain;

public record Address(
        String street,
        String house_number,
        String city,
        String state,
        String country,
        String postal_code
) {
}
