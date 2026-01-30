package com.serenitydojo.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;
import com.microsoft.playwright.junit.UsePlaywright;
import org.junit.jupiter.api.*;

import java.util.Arrays;

//@UsePlaywright(ASimplePlaywrightTest.MyOptions.class)
public class ASimplePlaywrightTest {

    public static class MyOptions implements OptionsFactory {

        @Override
        public Options getOptions() {
            return new Options()
                    .setHeadless(false)
                    .setLaunchOptions(
                            new BrowserType.LaunchOptions()
                                    .setArgs(Arrays.asList("--no-sandbox", "--disable-gpu"))
                    );
        }
    }

    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext browserContext;
    Page page;

    @BeforeAll
    public static void setUpBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
        );
        browserContext = browser.newContext();
    }

    @BeforeEach
    public void setUp() {
        page = browserContext.newPage();
    }

    @AfterAll
    public static void teardown() {
        browser.close();
        playwright.close();
    }

    @Test
    void shouldShowThePageTitle() {
        page.navigate("https://practicesoftwaretesting.com/");
        String title = page.title();
        Assertions.assertTrue(title.contains("Practice Software Testing"));
    }

    @Test
    void shouldSearchByKeyword() {
        page.navigate("https://practicesoftwaretesting.com/");
        page.locator("[placeholder=Search]").fill("pliers");
        page.locator("button:has-text('Search')").click();

        int matchingSearchResults = page.locator(".card").count();

        Assertions.assertTrue(matchingSearchResults > 0);
    }
}
