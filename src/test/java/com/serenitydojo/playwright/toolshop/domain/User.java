package com.serenitydojo.playwright.toolshop.domain;

import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record User(
        String first_name,
        String last_name,
        Address address,
        String phone,
        String dob,
        String password,
        String email) {
    public static User randomUser() {
        Faker fake = new Faker();

        int year = fake.number().numberBetween(1970, 2000);
        int month = fake.number().numberBetween(1, 12);
        int day = fake.number().numberBetween(1, 28);
        LocalDate date = LocalDate.of(year, month, day);
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return new User(
                fake.name().firstName(),
                fake.name().lastName(),
                new Address(
                        fake.address().streetName(),
                        fake.address().city(),
                        fake.address().state(),
                        fake.address().country(),
                        fake.address().postcode()
                ),
                fake.phoneNumber().phoneNumber(),
                formattedDate,
                "Az123!&xyz",
                fake.internet().emailAddress()
        );
    }

    public User withPassword(String password) {
        return new User(
                first_name,
                last_name,
                address,
                phone,
                dob,
                password,
                email);
    }

    public User withFirstName(String first_name) {
        return new User(first_name,last_name,address,phone,dob,password,email);
    }

    public static User randomUserWithNullField(String fieldName) {

        User user = randomUser();

        switch (fieldName.toLowerCase()) {

            case "firstname":
                return new User(
                        null,
                        user.last_name(),
                        user.address(),
                        user.phone(),
                        user.dob(),
                        user.password(),
                        user.email()
                );

            case "lastname":
                return new User(
                        user.first_name(),
                        null,
                        user.address(),
                        user.phone(),
                        user.dob(),
                        user.password(),
                        user.email()
                );

            case "phone":
                return new User(
                        user.first_name(),
                        user.last_name(),
                        user.address(),
                        null,
                        user.dob(),
                        user.password(),
                        user.email()
                );

            case "email":
                return new User(
                        user.first_name(),
                        user.last_name(),
                        user.address(),
                        user.phone(),
                        user.dob(),
                        user.password(),
                        null
                );

            case "password":
                return new User(
                        user.first_name(),
                        user.last_name(),
                        user.address(),
                        user.phone(),
                        user.dob(),
                        null,
                        user.email()
                );

            default:
                throw new IllegalArgumentException(
                        "Unknown field: " + fieldName
                );
        }
    }

}
