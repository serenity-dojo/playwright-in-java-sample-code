package com.serenitydojo.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.serenitydojo.playwright.toolshop.pageObjects.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

public class PlaywrightPageObjectTest {

    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext browserContext;
    Page page;
    SearchComponent searchComponent;
    ProductList productList;
    ProductDetails productDetails;
    NavBar navBar;
    CheckoutCart checkoutCart;

    @BeforeAll
    static void setUpBrowser() {
        playwright = Playwright.create();
        playwright.selectors().setTestIdAttribute("data-test");
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(true)
                        .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
        );
    }

    @BeforeEach
    void setUp()
    {
        browserContext = browser.newContext();
        page = browserContext.newPage();
        searchComponent = new SearchComponent(page);
        productList = new ProductList(page);
        productDetails = new ProductDetails(page);
        navBar = new NavBar(page);
        checkoutCart = new CheckoutCart(page);
    }

    @AfterEach
    void closeContext() {
        browserContext.close();
    }

    @AfterAll
    static void tearDown() {
        browser.close();
        playwright.close();
    }

    @Test
    void withoutPageObjects()
    {
        page.navigate("https://practicesoftwaretesting.com");
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

        page.navigate("https://practicesoftwaretesting.com");
        searchComponent.searchBy("tape");
        var matchingProducts = productList.getProductName();
        Assertions.assertThat(matchingProducts).contains("Measuring Tape");
    }

    @Test
    void searchAndAddToCart() {
        navBar.openHomePage();
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
        navBar.openHomePage();
        productList.viewProductDetails("Bolt Cutters");
        productDetails.increaseQuantityBy(2);
        productDetails.addToCart();

        navBar.openHomePage();
        productList.viewProductDetails("Claw Hammer");
        productDetails.addToCart();

        navBar.openCart();
    }
}
