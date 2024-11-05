package com.serenitydojo.playwright;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.RequestOptions;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.Arrays;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Execution(ExecutionMode.SAME_THREAD)
public class PlaywrightRestAPITest {

    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext browserContext;

    Page page;

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
    void setUp() {
        browserContext = browser.newContext();
        page = browserContext.newPage();
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

    @BeforeEach
    void openHomePage() {
        page.route("**/products/search?q=pliers",
                route -> route.fulfill(new Route.FulfillOptions()
                        .setBody("{\"message\": \"Internal Server Error\"}")
                        .setStatus(404))
        );
        page.navigate("https://practicesoftwaretesting.com");
    }

    @DisplayName("Playwright allows us to mock out API responses")
    @Nested
    class MockingAPIResponses {

        @Test
        @DisplayName("When a search returns a single product")
        void whenASingleItemIsFound() {
            page.route("**/products/search?q=pliers",
                    route -> route.fulfill(new Route.FulfillOptions()
                            .setBody(MockSearchResponses.RESPONSE_WITH_A_SINGLE_ENTRY)
                            .setStatus(200))
            );

            page.navigate("https://practicesoftwaretesting.com");
            page.getByPlaceholder("Search").fill("pliers");
            page.getByPlaceholder("Search").press("Enter");

            assertThat(page.getByTestId("product-name")).hasCount(1);
            assertThat(page.getByTestId("product-name")
                    .filter(new Locator.FilterOptions().setHasText("Super Pliers")))
                    .isVisible();
        }

        @Test
        @DisplayName("When a search returns no products")
        void whenNoItemsAreFound() {
            page.route("**/products/search?q=pliers",
                    route -> route.fulfill(new Route.FulfillOptions()
                            .setBody(MockSearchResponses.RESPONSE_WITH_NO_ENTRIES)
                            .setStatus(200))
            );

            page.navigate("https://practicesoftwaretesting.com");
            page.getByPlaceholder("Search").fill("pliers");
            page.getByPlaceholder("Search").press("Enter");

            assertThat(page.getByTestId("product-name")).isHidden();
            assertThat(page.getByTestId("search_completed")).hasText("There are no products found.");
        }
    }
}