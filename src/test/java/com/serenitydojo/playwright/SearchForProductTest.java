package com.serenitydojo.playwright;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class SearchForProductTest extends BaseTest
{
    @Test
    void whenSearchingByKeyboard()
    {
        navBar.openHomePage();
        searchComponent.searchBy("tape");
        var matchingProducts = productList.getProductName();

        Assertions.assertThat(matchingProducts).contains("Tape Measure 7.5m");
    }

    @Test
    void whenThereIsNoMatchingProduct()
    {
        navBar.openHomePage();
        searchComponent.searchBy("potato");
        var matchingProducts = productList.getProductName();

        Assertions.assertThat(matchingProducts).isEmpty();
        Assertions.assertThat(productList.getSearchCompletedMessage()).contains("There are no products found.");
    }

    @Test
    void clearingTheSearchResults()
    {
        navBar.openHomePage();
        searchComponent.searchBy("saw");

        var matchingFilteredProducts = productList.getProductName();
        Assertions.assertThat(matchingFilteredProducts).hasSize(2);

        searchComponent.clearSearch();
        var matchingProducts = productList.getProductName();
        Assertions.assertThat(matchingProducts).hasSize(9);
    }

}
