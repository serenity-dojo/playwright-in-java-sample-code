package com.serenitydojo.playwright.fixtures;

import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;

public class PlaywrightOptions implements OptionsFactory {

    @Override
    public Options getOptions() {
        return new Options()
                .setHeadless(false)
                .setBaseUrl("https://practicesoftwaretesting.com")
                .setTestIdAttribute("data-test");
    }
}
