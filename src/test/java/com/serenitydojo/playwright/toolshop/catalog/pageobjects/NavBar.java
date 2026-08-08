package com.serenitydojo.playwright.toolshop.catalog.pageobjects;

import com.microsoft.playwright.Page;
import net.serenitybdd.annotations.Step;


public class NavBar {
    private final Page page;

    public NavBar(Page page) {
        this.page = page;
    }

    @Step("Open cart")
    public void openCart() {
        page.getByTestId("nav-cart").click();
    }

    @Step("Open the home page")
    public void openHomePage() {
        page.navigate("https://practicesoftwaretesting.com");
    }

    @Step("Open the Contact page")
    public void toTheContactPage() {
        page.navigate("https://practicesoftwaretesting.com/contact");
    }
}