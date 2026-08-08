package com.serenitydojo.playwright.toolshop.restAPITest;


import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import com.serenitydojo.playwright.HeadlessChromeOptions;
import com.serenitydojo.playwright.toolshop.catalog.pageobjects.CartLineItem;
import com.serenitydojo.playwright.toolshop.catalog.pageobjects.CheckoutCart;
import com.serenitydojo.playwright.toolshop.catalog.pageobjects.NavBar;
import com.serenitydojo.playwright.toolshop.catalog.pageobjects.ProductDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@UsePlaywright(HeadlessChromeOptions.class)
public class PlaywrightPageObjectTest {

    SearchComponent searchComponent;
    ProductList productList;
    ProductDetails productDetails;
    NavBar navBar;
    CheckoutCart checkoutCart;

    @BeforeEach
    void setup(Page page){
        searchComponent = new SearchComponent(page);
        productList = new ProductList(page);
        productDetails = new ProductDetails(page);
        navBar = new NavBar(page);
        checkoutCart = new CheckoutCart(page);

    }

    @Test
    void withoutPageObject(Page page){

        page.navigate("https://practicesoftwaretesting.com");

        page.waitForResponse(response ->
                response.url().endsWith("/products/search"),

                ()->{
            page.getByPlaceholder("Search").fill("tape");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
        });

        List<String> matchingProducts = page.getByTestId("product-name").allInnerTexts();
        assertThat(matchingProducts)
                .contains("Tape Measure 7.5m", "Measuring Tape");

    }

    @Test
    void withPageObjects(Page page){

        SearchComponent searchComponent = new SearchComponent(page);
        ProductList productList = new ProductList(page);

        searchComponent.seacrhBy("tape");

        var matchingProducts = productList.getProductNames();

        assertThat(matchingProducts)
                .contains("Tape Measure 7.5m", "Measuring Tape");


    }

    @Test
    void withPageObjectAddCart(Page page){

        searchComponent.seacrhBy("pliers");
        productList.viewProductDetails("Combination Pliers");
        productDetails.increaseQuanityBy(2);
        productDetails.addToCart();

        navBar.openCart();

        List<CartLineItem> lineItems = checkoutCart.getLineItems();
        assertThat(lineItems)
                .hasSize(1)
                .first()
                .satisfies(item -> {
                    assertThat(item.title()).contains("Combination Pliers");
                    assertThat(item.quantity()).isEqualTo(3);
                    assertThat(item.total()).isEqualTo(item.quantity()* item.price());
                });
    }

    @Test
    void whenCheckingOutMultipleItems(){
        navBar.openHomePage();
        productList.viewProductDetails("Bolt Cutters");
        productDetails.increaseQuanityBy(2);
        productDetails.addToCart();

        navBar.openHomePage();
        productList.viewProductDetails("Slip Joint Pliers");
        productDetails.addToCart();

        navBar.openCart();

        List<CartLineItem> lineItems = checkoutCart.getLineItems();
        assertThat(lineItems)
                .hasSize(2);

        List<String> productNames = lineItems.stream().map(CartLineItem::title).toList();
        assertThat(productNames).contains("Bolt Cutters", "Slip Joint Pliers");

        assertThat(lineItems)
                .allSatisfy(item -> {
                    assertThat(item.total()).isEqualTo(item.quantity()*item.price());
                });

    }

















    class SearchComponent{
        private final Page page;

        SearchComponent(Page page) {
            this.page = page;
        }


        public void seacrhBy(String keyword) {

            page.navigate("https://practicesoftwaretesting.com");

            page.waitForResponse(response ->
                            response.url().endsWith("/products/search"),

                    ()->{
                        page.getByPlaceholder("Search").fill(keyword);
                        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
                    });

        }
    }

    class ProductList{

        private final Page page;

        ProductList(Page page) {
            this.page = page;
        }

        public List<String> getProductNames() {

            return page.getByTestId("product-name").allInnerTexts();
        }

        public void viewProductDetails(String productName) {

            page.locator(".card").getByText(productName).click();
        }
    }


}
