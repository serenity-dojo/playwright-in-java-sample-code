package com.serenitydojo.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

@UsePlaywright(HeadlessChromeOptions.class)
public class AddingItemsToTheCartTest {

    @DisplayName("Search for pliers")
    @Test
    void searchForPliers(Page page){
        page.navigate("https://practicesoftwaretesting.com/");
//        page.locator("[placeholder='Search']").fill("pliers");
        page.getByPlaceholder("Search").fill("pliers");
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Search")).click();

        Locator products = page.locator(".card").filter(
                new Locator.FilterOptions()
                        .setHas(page.getByAltText("Pliers"))
                        .setHasNot(page.getByText("Out of stock"))
        );

        List<String> productNames = products.locator("h5").allTextContents();
        System.out.println(productNames);

    }














}
