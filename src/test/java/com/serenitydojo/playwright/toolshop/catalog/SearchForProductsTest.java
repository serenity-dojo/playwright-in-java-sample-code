package com.serenitydojo.playwright.toolshop.catalog;

import com.serenitydojo.playwright.toolshop.catalog.pageobjects.ProductList;
import com.serenitydojo.playwright.toolshop.catalog.pageobjects.SearchComponent;
import com.serenitydojo.playwright.toolshop.fixtures.PlaywrightTestCase;
import com.serenitydojo.playwright.toolshop.fixtures.ScreenshotManager;
import net.serenitybdd.annotations.Feature;
import net.serenitybdd.annotations.Story;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.playwright.junit5.SerenityPlaywrightExtension;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


@ExtendWith(SerenityJUnit5Extension.class)
@ExtendWith(SerenityPlaywrightExtension.class)
@DisplayName("Searching for products")
@Feature("Searching for products")
public class SearchForProductsTest extends PlaywrightTestCase {

    @BeforeEach
    void openHomePage() {
        page.navigate("https://practicesoftwaretesting.com");
    }

    @Nested
    @DisplayName("Searching by keyword")
    @Story("Searching by keyword")
    class SearchingByKeyword {

        @Test
        @DisplayName("When there are matching results")
        void whenSearchingByKeyword() {
            SearchComponent searchComponent = new SearchComponent(page);
            ProductList productList = new ProductList(page);

//            ScreenshotManager.takeScreenshot(page, "ss1");
            searchComponent.searchBy("tape");
//            ScreenshotManager.takeScreenshot(page, "ss2");

            var matchingProducts = productList.getProductNames();

            Assertions.assertThat(matchingProducts).contains("Tape Measure 7.5m", "Measuring Tape", "Tape Measure 5m");
        }

        @Test
        @DisplayName("When there are no matching results")
        void whenThereIsNoMatchingProduct() {
            SearchComponent searchComponent = new SearchComponent(page);
            ProductList productList = new ProductList(page);
            searchComponent.searchBy("unknown");

            var matchingProducts = productList.getProductNames();

            Assertions.assertThat(matchingProducts).isEmpty();
            Assertions.assertThat(productList.getSearchCompletedMessage()).contains("There are no products found.");
        }
    }

    @Test
    @Story("Clearing the previous search results")
    @DisplayName("When the user clears a previous search results")
    void clearingTheSearchResults() {
        SearchComponent searchComponent = new SearchComponent(page);
        ProductList productList = new ProductList(page);
        searchComponent.searchBy("saw");

        var matchingFilteredProducts = productList.getProductNames();
        Assertions.assertThat(matchingFilteredProducts).hasSize(2);

        searchComponent.clearSearch();

        var matchingProducts = productList.getProductNames();
        Assertions.assertThat(matchingProducts).hasSize(9);
    }
}