package com.serenitydojo.playwright.toolshop.catalog.pageobjects;

import com.microsoft.playwright.Page;

import java.util.List;

public class CheckoutCart {
    private final Page page;

    public CheckoutCart(Page page) {
        this.page = page;
    }

    public List<CartLineItem> getLineItems() {
        page.locator("app-cart tbody tr").first().waitFor();
        return page.locator("app-cart tbody tr")
                .all()
                .stream()
                .map(
                        row -> {
                            String title = trimmed(row.getByTestId("product-title").innerText());
                            int quantity = Integer.parseInt(row.getByTestId("product-quantity").inputValue());
                            double price = Double.parseDouble(price(row.getByTestId("product-price").innerText()));
                            double linePrice = Double.parseDouble(price(row.getByTestId("line-price").innerText()));
                            return new CartLineItem(title, quantity, price, linePrice);
                        }
                ).toList();
    }

    private String trimmed(String value) {
        return value.strip().replaceAll("\u00A0", "");
    }

    private String price(String value) {
        return value.replace("$", "");
    }

}