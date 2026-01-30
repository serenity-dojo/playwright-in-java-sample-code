package com.serenitydojo.playwright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@UsePlaywright(HeadlessChromeOptions.class)
public class PlaywrightWaitsTest {

    @Nested
    class WaitingForState{
        @BeforeEach
        void openHomePage(Page page)
        {
            page.navigate("https://practicesoftwaretesting.com/");
            page.waitForSelector(".card-img-top");
        }

        @Test
        void shouldShowAllProducts(Page page)
        {
            List<String> productNames = page.getByTestId("product-name").allInnerTexts();
            Assertions.assertThat(productNames).contains("Pliers", "Bolt Cutters", "Hammer");
        }

        @Test
        void shouldShowAllProductsImages(Page page)
        {
            List<String> productImageTitles = page.locator(".card-img-top").all()
                    .stream().map(img -> img.getAttribute("alt"))
                    .toList();

            Assertions.assertThat(productImageTitles).contains("Pliers", "Bolt Cutters", "Hammer");
        }
    }

    @Nested
    class AutomaticWaits
    {
        @BeforeEach
        void openHomePage(Page page)
        {
            page.navigate("https://practicesoftwaretesting.com/");
        }

        @Test
        void waitForFilterCheckboxes(Page page)
        {
            var screwdriverFilter = page.getByLabel("Screwdriver");
            screwdriverFilter.click();

            assertThat(screwdriverFilter).isChecked();
        }

        @Test
        void filterByCategory(Page page)
        {
            page.getByRole(AriaRole.MENUBAR).getByText("Categories").click();
            page.getByRole(AriaRole.MENUBAR).getByText("Power Tools").click();
            //page.waitForSelector(".card");
            page.waitForSelector(".card", new Page.WaitForSelectorOptions()
                    .setState(WaitForSelectorState.VISIBLE).setTimeout(2000));

            var filteredProducts = page.getByTestId("product-name").allInnerTexts();
            Assertions.assertThat(filteredProducts).contains("Belt Sander", "Circular Saw");
        }
    }

    @Nested
    class WaitForElementsToAppearAndDisappear
    {
        @BeforeEach
        void openHomePage(Page page)
        {
            page.navigate("https://practicesoftwaretesting.com/");
        }

        @Test
        void shouldDisplayToastMessage(Page page)
        {
            page.getByText("Bolt Cutters").click();
            page.getByText("Add to cart").click();

            assertThat(page.getByRole(AriaRole.ALERT)).isVisible();
            assertThat(page.getByRole(AriaRole.ALERT)).hasText("Product added to shopping cart.");

            page.waitForCondition(() -> page.getByRole(AriaRole.ALERT).isHidden());
        }

        @Test
        void shouldUpdateTheCart(Page page)
        {
            page.getByText("Bolt Cutters").click();
            page.getByText("Add to cart").click();

            page.waitForCondition(() -> page.getByTestId("cart-quantity").textContent().equals("1"));

            //page.waitForSelector("[data-test=cart-quantity]:has-text('1')");
        }
    }
    @Nested
    class WaitingForAPICalls
    {
        @Test
        void sortByDescendingPrice(Page page)
        {
            page.navigate("https://practicesoftwaretesting.com/");

            page.waitForResponse("**/products?page=0&sort=price**",
                    () -> {
                        page.locator("select[aria-label='sort']").selectOption(new SelectOption().setIndex(3));
                    });

            var productPrices = page.getByTestId("product-price")
                    .allTextContents()
                    .stream()
                    .map(WaitingForAPICalls::extractPrice)
                    .toList();

            System.out.println("Product prices: " + productPrices);
            Assertions.assertThat(productPrices)
                    .isNotEmpty()
                    .isSortedAccordingTo(Comparator.reverseOrder());
        }

        private static double extractPrice(String price)
        {
            return Double.parseDouble(price.replace("$", ""));
        }
    }
}
