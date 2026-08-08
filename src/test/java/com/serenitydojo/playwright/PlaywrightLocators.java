package com.serenitydojo.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.Nested;

import java.util.Arrays;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Execution(ExecutionMode.SAME_THREAD)
public class PlaywrightLocators {


    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext browserContext;

    Page page;

    @BeforeAll
    static void setUpBrowser() {
        playwright = Playwright.create();
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

    @DisplayName("Locating elements using CSS")
    @Nested
    class LocatingElementsUsingCSS {

        @BeforeEach
        void openContactPage() {
            page.navigate("https://practicesoftwaretesting.com/contact");
        }

        @DisplayName("By id")
        @Test
        void locateTheFirstNameFieldByID() {
            page.locator("#first_name").fill("Sarah-Jane");
            assertThat(page.locator("#first_name")).hasValue("Sarah-Jane");
        }

        @DisplayName("By CSS class")
        @Test
        void locateTheSendButtonByCssClass() {
            page.locator("#first_name").fill("Sarah-Jane");
            page.locator(".btnSubmit").click();
            List<String> alertMessages = page.locator(".alert").allTextContents();
            Assertions.assertTrue(!alertMessages.isEmpty());
        }

        @DisplayName("By attribute")
        @Test
        void locateTheSendButtonByAttribute() {
            page.locator("[placeholder='Your last name *']").fill("Smith");
            PlaywrightAssertions.assertThat(page.locator("#last_name")).hasValue("Smith");
        }











    }


















    @DisplayName("Locating elements by text")
    @Nested
    class LocatingElementsByText {

        @BeforeEach
        void openTheCatalogPage() {
            openPage();
        }

        @DisplayName("Locating an element by text contents")
        @Test
        void byText() {
            page.getByText("Bolt Cutters").click();

            assertThat(page.getByText("MightyCraft Hardware")).isVisible();
        }

        @DisplayName("Using alt text")
        @Test
        void byAltText() {
            page.getByAltText("Combination Pliers").click();

            assertThat(page.getByText("ForgeFlex Tools")).isVisible();
        }

        @DisplayName("Using title")
        @Test
        void byTitle() {
            page.getByAltText("Combination Pliers").click();

            page.getByTitle("Practice Software Testing - Toolshop").click();
        }
    }

    @DisplayName("Locating elements by test Id")
    @Nested
    class LocatingElementsByTestID {

        @BeforeAll
        static void setTestId() {
            playwright.selectors().setTestIdAttribute("data-test");
        }

        @DisplayName("Using a custom data-test field")
        @Test
        void byTestId() {
            openPage();

            playwright.selectors().setTestIdAttribute("data-test");

            page.getByTestId("search-query").fill("Pliers");
            page.getByTestId("search-submit").click();
        }

    }



    private void openPage() {
        page.navigate("https://practicesoftwaretesting.com");
    }


}
