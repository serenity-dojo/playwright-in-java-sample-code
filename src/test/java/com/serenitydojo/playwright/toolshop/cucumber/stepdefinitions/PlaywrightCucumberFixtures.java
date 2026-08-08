package com.serenitydojo.playwright.toolshop.cucumber.stepdefinitions;

import com.microsoft.playwright.*;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.qameta.allure.Allure;


import java.io.ByteArrayInputStream;
import java.util.Arrays;

public class PlaywrightCucumberFixtures {

    //this is for parallel execution
    private static final ThreadLocal<Playwright> playwright
            = ThreadLocal.withInitial(() -> {
                Playwright playwright = Playwright.create();
                playwright.selectors().setTestIdAttribute("data-test");
                return playwright;
            }
    );

    private static final ThreadLocal<Browser> browser = ThreadLocal.withInitial(() ->
            playwright.get().chromium().launch(
                    new BrowserType.LaunchOptions()
                            .setHeadless(false)
                            .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
            )
    );

    private static final  ThreadLocal<BrowserContext> browserContext = new ThreadLocal<>();

    private static final  ThreadLocal<Page> page = new ThreadLocal<>();

    @Before(order = 100)
    public void setUpBrowserContext() {

        browserContext.set(browser.get().newContext());
        page.set(browserContext.get().newPage());
    }

    @After
    public void closeContext() {
        takeScreenshot("End of test");
        browserContext.get().close();
    }

    protected void takeScreenshot(String name){
        var screenshot = page.get().screenshot(
                new Page.ScreenshotOptions().setFullPage(true)
        );
        Allure.addAttachment(name, new ByteArrayInputStream(screenshot));
    }

    @AfterAll
    public static void tearDown() {
        browser.get().close();
        browser.remove();

        playwright.get().close();
        playwright.remove();
    }

    public static Page getPage(){
        return page.get();
    }


}
