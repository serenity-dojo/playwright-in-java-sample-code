package com.serenitydojo.playwright;


import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@UsePlaywright(HeadlessChromeOptions.class)
public class PlaywrightWaitsTest {


    @DisplayName("Explicit Wait")
    @Nested
    class ExplicitWait {
        @BeforeEach
        void openContactPage(Page page) {
            page.navigate("https://practicesoftwaretesting.com");
            page.waitForSelector("[data-test=product-name]");
            page.waitForSelector(".card-img-top");
        }

        @Test
        void shouldShowAllProductName(Page page){
            List<String> productNames = page.getByTestId("product-name").allInnerTexts();
            Assertions.assertThat(productNames).contains("Pliers", "Bolt Cutters");

        }

        @Test
        void shouldShowAllProductImages(Page page){
            List<String> productImageTitles = page.locator(".card-img-top").all()
                    .stream()
                    .map(img -> img.getAttribute("alt"))
                    .toList();

            Assertions.assertThat(productImageTitles).contains("Bolt Cutters");
        }

    }

    @DisplayName("Implicit Wait")
    @Nested
    class ImplicitWait{

        @BeforeEach
        void openContactPage(Page page) {
            page.navigate("https://practicesoftwaretesting.com");

        }

        @Test
        void shouldWaitForTheFilterCheckboxes(Page page){

            var screwDriverFilter = page.getByLabel("Screwdriver");

            screwDriverFilter.click();

            assertThat(screwDriverFilter).isChecked();
        }

        @Test
        void shouldFilterProductsByCategory(Page page){

            page.getByRole(AriaRole.MENUBAR).getByText("Categories").click();
            page.getByRole(AriaRole.MENUBAR).getByText("Power Tools").click();

            page.waitForSelector(".card",
                    new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(2000)
            );
            var filteredProducts = page.getByTestId("product-name").allInnerTexts();

            Assertions.assertThat(filteredProducts).contains("Sheet Sander");

        }



    }


    @Nested
    class WaitingForElementsToAppearAndDisappear{

        @BeforeEach
        void openContactPage(Page page) {
            page.navigate("https://practicesoftwaretesting.com");

        }

        @Test
        void shouldDisplayToasterMessage(Page page){

            page.getByText("Bolt Cutters").click();
            page.getByText("Add to cart").click();

            assertThat(page.getByRole(AriaRole.ALERT)).isVisible();
            assertThat(page.getByRole(AriaRole.ALERT)).hasText("Product added to shopping cart.");

            page.waitForCondition(() -> page.getByRole(AriaRole.ALERT).isHidden());

        }

        @Test
        void shoudlUpdateTheCartItemCount(Page page){

            page.getByText("Bolt Cutters").click();
            page.getByText("Add to cart").click();

            page.waitForCondition(() -> page.getByTestId("cart-quantity").textContent().equals("1"));
            page.waitForSelector("[data-test=cart-quantity]:has-text('1')");

        }




    }


    @Nested
    class WaitingForAPICalls {

        @Test
        void sortByDescendingPrice(Page page) {
            page.navigate("https://practicesoftwaretesting.com");

            // Sort by descending price
            // (Note: The API endpoint has evolved and is slightly different to the one in the video;
            // it now sends the sort/page criteria in the request body rather than as URL query params)
            page.waitForResponse(
                    response -> response.url().contains("/products")
                            && response.request().postData() != null
                            && response.request().postData().contains("\"sort\":\"price,desc\""),
                    () -> {
                        page.getByTestId("sort").selectOption("Price (High - Low)");
                    });

            // Find all the prices on the page
            var productPrices = page.getByTestId("product-price")
                    .allInnerTexts()
                    .stream()
                    .map(WaitingForAPICalls::extractPrice)
                    .toList();

            // Are the prices in the correct order
            Assertions.assertThat(productPrices)
                    .isNotEmpty()
                    .isSortedAccordingTo(Comparator.reverseOrder());
        }

        private static double extractPrice(String price) {
            return Double.parseDouble(price.replace("$", ""));
        }
    }






}
