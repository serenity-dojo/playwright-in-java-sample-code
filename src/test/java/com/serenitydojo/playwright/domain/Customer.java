package com.serenitydojo.playwright.domain;

import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public record Customer(
        String firstName,
        String lastName,
        String dob,
        String country,
        String postalCode,
        String houseNumber,
        String street,
        String city,
        String state,
        String phone,
        String email,
        String password
) {
    public static String invalidEmailAddress() {
        return new Faker(Locale.US).internet().username();
    }

    public static String shortPassword() {
        return new Faker(Locale.US).internet().password(2, 5, true, true);
    }

    public static Customer random() {
        var faker = new Faker(Locale.US);
        var dob = LocalDate.of(
                faker.number().numberBetween(1950, 2000),
                faker.number().numberBetween(1, 12),
                faker.number().numberBetween(1, 28)
        ).format(DateTimeFormatter.ISO_LOCAL_DATE);
        return new Customer(
                faker.name().firstName(),
                faker.name().lastName(),
                dob,
                "United States of America (the)",
                faker.number().digits(5),
                String.valueOf(faker.number().numberBetween(1, 999)),
                faker.address().streetName(),
                faker.address().city(),
                faker.address().stateAbbr(),
                faker.phoneNumber().subscriberNumber(10),
                faker.internet().emailAddress(),
                faker.internet().password(8, 12, true, true)
        );
    }
}
