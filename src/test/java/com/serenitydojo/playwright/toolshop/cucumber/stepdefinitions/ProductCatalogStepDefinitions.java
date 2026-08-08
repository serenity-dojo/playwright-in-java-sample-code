package com.serenitydojo.playwright.toolshop.cucumber.stepdefinitions;

import com.serenitydojo.playwright.toolshop.catalog.pageobjects.NavBar;
import com.serenitydojo.playwright.toolshop.catalog.pageobjects.ProductList;
import com.serenitydojo.playwright.toolshop.catalog.pageobjects.SearchComponent;
import com.serenitydojo.playwright.toolshop.fixtures.ProductSummary;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.*;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductCatalogStepDefinitions {

    NavBar navBar;
    SearchComponent searchComponent;
    ProductList productList;

    @Before
    public void setupPageObject(){
        navBar = new NavBar(PlaywrightCucumberFixtures.getPage());
        searchComponent = new SearchComponent(PlaywrightCucumberFixtures.getPage());
        productList = new ProductList(PlaywrightCucumberFixtures.getPage());
    }

    @Given("Sally is on the home page")
    public void sally_is_on_the_home_page() {
        navBar.openHomePage();
    }
    @Then("she searches for {string}")
    public void she_searches_for(String searchTerm) {
        searchComponent.searchBy(searchTerm);
    }
    @Then("the {string} product should be displayed")
    public void the_product_should_be_displayed(String productName) {
        var matchingProduct = productList.getProductNames();
        assertThat(matchingProduct).contains(productName);
    }

    @DataTableType
    public ProductSummary productSummaryRow(Map<String, String> productData){
        return new ProductSummary(productData.get("Product"), productData.get("Price"));
    }
    @Then("the following products should be displayed")
    public void theFollowingProductsShouldBeDisplayed(List<ProductSummary> expectedProductSummaries) {
        var matchingProducts = productList.getProductSummaries();
//        List<Map<String, String>> expectedProductData = expectedProductSummaries.asMaps();
//        List<ProductSummary> expectedProductSummaries =
//                expectedProductData.stream()
//                                .map(productData -> new ProductSummary(
//                                        productData.get("Product"),
//                                        productData.get("Price")
//                                )).toList();


        assertThat(matchingProducts).containsExactlyInAnyOrderElementsOf(expectedProductSummaries);
    }

    @Then("no products should be displayed")
    public void noProductsShouldBeDisplayed() {

        List<ProductSummary> matchingProducts = productList.getProductSummaries();
        assertThat(matchingProducts).isEmpty();

    }

    @And("the message {string} should be displayed")
    public void theMessageShouldBeDisplayed(String messageText) {
        String completionMessage = productList.getSearchCompletedMessage();
        assertThat(completionMessage).isEqualTo(messageText);
    }

    @And("she filters by {string}")
    public void sheFiltersBy(String filterName) {
        searchComponent.filterBy(filterName);
    }

    @When("she sorts by {string}")
    public void sheSortsBy(String sortFilter) {
        searchComponent.sortBy(sortFilter);
    }

    @Then("the first product displayed should be {string}")
    public void theFirstProductDisplayedShouldBe(String firstProductName) {
        List<String> productNames = productList.getProductNames();
        assertThat(productNames).startsWith(firstProductName);
    }
}
