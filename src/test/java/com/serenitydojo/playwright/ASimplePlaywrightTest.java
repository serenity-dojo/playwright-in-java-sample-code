package com.serenitydojo.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.junit.UsePlaywright;
import org.junit.jupiter.api.*;

import java.util.Arrays;

//This annotation allows you to do test without creating playwright environment and browser
//@UsePlaywright
public class ASimplePlaywrightTest {

    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext browserContext;
    Page page;

    @BeforeAll
    public static void setUpBrowser(){
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions","--disable-gpu"))
        );
        //this allows paralel execution
        browserContext = browser.newContext();

    }

    @BeforeEach
    public void setUp(){
        page = browser.newPage();
    }

    @AfterAll
    public static void tearDown(){
        browser.close();
        playwright.close();
    }

    @Test
    void shouldShowThePageTitle() {
        // TODO: Write your first playwright test here

        page.navigate("https://practicesoftwaretesting.com/");
        String title = page.title();
        Assertions.assertTrue(title.contains("Practice Software Testing"));

    }

    @Test
    void shouldSearchByKeyword(){

        page.navigate("https://practicesoftwaretesting.com/");
        page.locator("[placeholder=Search]").fill("Pliers");
        //this is basically sort of xpath
        page.locator("button:has-text('Search')").click();

        //this will look for the element with card
        int matchingSearchResults = page.locator(".card").count();
        System.out.println(matchingSearchResults);

        Assertions.assertTrue(matchingSearchResults>0);

    }
}
