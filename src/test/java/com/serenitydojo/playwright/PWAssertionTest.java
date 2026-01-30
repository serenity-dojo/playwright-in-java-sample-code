package com.serenitydojo.playwright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.SelectOption;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@UsePlaywright(HeadlessChromeOptions.class)
@DisplayName("Making assertions")
@Nested
class PWAssertionTest
{
    @BeforeEach
    void openContactPage(Page page)
    {
        page.navigate("https://practicesoftwaretesting.com/contact");
    }

    @DisplayName("Checking the value of a field")
    @Test
    void fieldValues(Page page)
    {
        var firstNameField = page.getByLabel("First Name");
        assertThat(firstNameField).isEditable();
        firstNameField.fill("Cristi");
        assertThat(firstNameField).hasValue("Cristi");
    }

    @DisplayName("Making Assertions about data values")
    @Test
    void allProductsHavePrices(Page page)
    {
        page.navigate("https://practicesoftwaretesting.com");
        page.waitForCondition(() -> page.getByTestId("product-name").count() > 0);

        List<Double> prices = page.getByTestId("product-price")
                .allInnerTexts().stream()
                .map(price -> Double.parseDouble(price.replace("$", "")))
                .toList();
        Assertions.assertThat(prices).isNotEmpty().allMatch(price -> price > 0)
                .doesNotContain(0.0)
                .allMatch(price -> price <1000)
                .allSatisfy(price -> Assertions.assertThat(price).isGreaterThan(0.0).isLessThan(1000.0));
    }

    @Test
    void shouldSortInAlphabeticalOrder(Page page)
    {
        page.navigate("https://practicesoftwaretesting.com");
        page.waitForCondition(() -> page.getByTestId("product-name").count() > 0);

        page.locator("select[aria-label='sort']").selectOption(new SelectOption().setIndex(2));
        page.waitForLoadState(LoadState.NETWORKIDLE);

        List<String> productName = page.getByTestId("product-name").allTextContents();
        Assertions.assertThat(productName).isSortedAccordingTo(Comparator.reverseOrder());
    }
}
