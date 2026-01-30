package com.serenitydojo.playwright.apiResources;

import net.datafaker.Faker;

public record User(String first_name, String last_name, Address address,
                   String phone, String dob, String password, String email)
{
    record Address(
            String street,
            String city,
            String state,
            String country,
            String postal_code
    ) {}

    public static User randomUser()
    {
        Faker fake = new Faker();
        Address address = new Address(
                fake.address().streetAddress(),
                fake.address().city(),
                fake.address().state(),
                fake.address().country(),
                fake.address().zipCode()
        );

        return new User(
                fake.name().firstName(),
                fake.name().lastName(),
                address,
                fake.phoneNumber().phoneNumber(),
                "1990-01-01",
                "Frewxcs22!",
                fake.internet().emailAddress()
        );
    }

    public User withPassword(String password)
    {
        return new User(first_name, last_name, address, phone, dob, password, email);
    }

    public User withFirstName(String first_name) {
        return new User(first_name,last_name,address,phone,dob,password,email);
    }
}
