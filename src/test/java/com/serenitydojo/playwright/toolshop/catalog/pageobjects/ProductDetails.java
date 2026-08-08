package com.serenitydojo.playwright.toolshop.catalog.pageobjects;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import net.serenitybdd.annotations.Step;


public class ProductDetails {
    private final Page page;

    public ProductDetails(Page page) {
        this.page = page;
    }

    @Step("Increase product quantity")
    public void increaseQuanityBy(int increment) {
        for (int i = 1; i <= increment; i++) {
            page.getByTestId("increase-quantity").click();
        }
    }

    @Step("Add to cart")
    public void addToCart() {
        page.waitForResponse(
                response -> response.url().contains("/carts") && response.request().method().equals("POST"),
                () -> {
                    page.getByText("Add to cart").click();
                    page.getByRole(AriaRole.ALERT).click();
                }
        );

    }
}