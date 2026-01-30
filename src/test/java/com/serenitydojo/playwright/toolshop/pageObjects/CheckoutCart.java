package com.serenitydojo.playwright.toolshop.pageObjects;

import com.microsoft.playwright.Page;

import java.util.List;

public class CheckoutCart {

    private final Page page;

    public CheckoutCart(Page page) {
        this.page = page;
    }

    public List<CartLineItem> getLineItems()
    {
        page.locator("app-cart tbody tr").waitFor();
        return page.locator("app-cart tbody tr").all()
                .stream().map(row -> {
                    String title = row.getByTestId("product-title").innerText();
                    int quantity = Integer.parseInt(row.getByTestId("product-quantity").inputValue());
                    double price = Double.parseDouble(price(row.getByTestId("product-price").innerText()));
                    double lineTotal = Double.parseDouble(price(row.getByTestId("line-price").innerText()));
                    return new CartLineItem(title, quantity, price, lineTotal);
                }).toList();
    }

    private String price(String value)
    {
        return value.replace("$", "");
    }
}
