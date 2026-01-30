package com.serenitydojo.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@UsePlaywright(HeadlessChromeOptions.class)
public class AddItemsToTheCartTest {

    @DisplayName("Search for pliers")
    @Test
    void searchForPliers(Page page)
    {
        page.navigate("https://practicesoftwaretesting.com");
        page.getByPlaceholder("Search").fill("Pliers");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();

        assertThat(page.locator(".card")).hasCount(4);

        List<String> productNames = page.getByTestId("product-name").allTextContents();
        Assertions.assertThat(productNames).allMatch(name -> name.contains("Pliers"));

        List<Locator> outOfStockItem = page.locator(".card")
                .filter(new Locator.FilterOptions().setHasText("Out of stock"))
                .getByTestId("product-name").all();
        Assertions.assertThat(outOfStockItem.size()).isEqualTo(1);
        assertThat(outOfStockItem.get(0)).hasText("Long Nose Pliers");
    }
}
