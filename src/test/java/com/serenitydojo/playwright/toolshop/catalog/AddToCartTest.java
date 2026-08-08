package com.serenitydojo.playwright.toolshop.catalog;

import com.serenitydojo.playwright.toolshop.catalog.pageobjects.*;
import com.serenitydojo.playwright.toolshop.fixtures.PlaywrightTestCase;

import net.serenitybdd.annotations.Feature;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.annotations.Story;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.playwright.PlaywrightSerenity;
import net.serenitybdd.playwright.junit5.SerenityPlaywrightExtension;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

@DisplayName("Shopping Cart")
@Feature("Shopping Cart")
@ExtendWith(SerenityJUnit5Extension.class)
@ExtendWith(SerenityPlaywrightExtension.class)
public class AddToCartTest extends PlaywrightTestCase {
    @Steps SearchComponent searchComponent;
    @Steps ProductList productList;
    @Steps ProductDetails productDetails;
    @Steps NavBar navBar;
    @Steps CheckoutCart checkoutCart;

    @BeforeEach
    void openHomePage() {
        page.navigate("https://practicesoftwaretesting.com");
    }

    @BeforeEach
    void setUp() {

        // we use @Steps from serenity so we don't have to use initiate them here, serenity does automatically
//        searchComponent = new SearchComponent(page);
//        productList = new ProductList(page);
//        productDetails = new ProductDetails(page);
//        navBar = new NavBar(page);
//        checkoutCart = new CheckoutCart(page);

        PlaywrightSerenity.registerPage(page);
    }


    @Test
    @Story("Check out")
    @DisplayName("Checking out a single item")
    void whenCheckingOutASingleItem() {
        searchComponent.searchBy("pliers");
        productList.viewProductDetails("Combination Pliers");

        productDetails.increaseQuanityBy(2);
        productDetails.addToCart();

        navBar.openCart();

        List<CartLineItem> lineItems = checkoutCart.getLineItems();

        Assertions.assertThat(lineItems)
                .hasSize(1)
                .first()
                .satisfies(item -> {
                    Assertions.assertThat(item.title()).contains("Combination Pliers");
                    Assertions.assertThat(item.quantity()).isEqualTo(3);
                    Assertions.assertThat(item.total()).isEqualTo(item.quantity() * item.price());
                });
    }

    @Test
    @Story("Check out")
    @DisplayName("Checking out multiple items")
    void whenCheckingOutMultipleItems() {
        navBar.openHomePage();

        productList.viewProductDetails("Bolt Cutters");
        productDetails.increaseQuanityBy(2);
        productDetails.addToCart();

        navBar.openHomePage();
        productList.viewProductDetails("Slip Joint Pliers");
        productDetails.addToCart();

        navBar.openCart();

        List<CartLineItem> lineItems = checkoutCart.getLineItems();

        Assertions.assertThat(lineItems).hasSize(2);
        List<String> productNames = lineItems.stream().map(CartLineItem::title).toList();
        Assertions.assertThat(productNames).contains("Bolt Cutters", "Slip Joint Pliers");

        Assertions.assertThat(lineItems)
                .allSatisfy(item -> {
                    Assertions.assertThat(item.quantity()).isGreaterThanOrEqualTo(1);
                    Assertions.assertThat(item.price()).isGreaterThan(0.0);
                    Assertions.assertThat(item.total()).isGreaterThan(0.0);
                    Assertions.assertThat(item.total()).isEqualTo(item.quantity() * item.price());
                });

    }
}