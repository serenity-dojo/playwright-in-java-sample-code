package com.serenitydojo.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.serenitydojo.playwright.toolshop.pageObjects.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.util.List;

public class PlaywrightPageObjectTest extends BaseTest {
    @BeforeEach
    void openHomePage()
    {
        navBar.openHomePage();
    }

    @Test
    void withoutPageObjects()
    {
        page.waitForResponse("**/products/search?q=tape", () -> {
            page.getByPlaceholder("Search").fill("tape");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
        });
        List<String> matchingProducts = page.getByTestId("product-name").allInnerTexts();
        Assertions.assertThat(matchingProducts).contains("Measuring Tape");
    }

    @Test
    void withPageObjects()
    {
        SearchComponent searchComponent = new SearchComponent(page);
        ProductList productList = new ProductList(page);

        searchComponent.searchBy("tape");
        var matchingProducts = productList.getProductName();
        Assertions.assertThat(matchingProducts).contains("Measuring Tape");
    }

    @Test
    void searchAndAddToCart() {
        searchComponent.searchBy("pliers");
        productList.viewProductDetails("Combination Pliers");

        productDetails.increaseQuantityBy(2);
        productDetails.addToCart();

        navBar.openCart();

        List<CartLineItem> lineItems = checkoutCart.getLineItems();
        Assertions.assertThat(lineItems)
                .hasSize(1)
                .first().satisfies(item -> {
                    Assertions.assertThat(item.title()).contains("Combination Pliers");
                    Assertions.assertThat(item.quantity()).isEqualTo(3);
                    Assertions.assertThat(item.total()).isEqualTo(item.quantity() * item.price());
                });
    }

    @Test
    void checkOutMultipleItems()
    {
        productList.viewProductDetails("Bolt Cutters");
        productDetails.increaseQuantityBy(2);
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
                    Assertions.assertThat(item.price()).isGreaterThan(0.0);
                    Assertions.assertThat(item.quantity()).isGreaterThanOrEqualTo(1);
                    Assertions.assertThat(item.total()).isGreaterThan(0.0);
                    Assertions.assertThat(item.total()).isEqualTo(item.quantity() * item.price());
                });
    }
}
