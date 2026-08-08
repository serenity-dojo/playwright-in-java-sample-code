package com.serenitydojo.playwright.toolshop.cucumber;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectPackages("features")
@ConfigurationParameter(
        key = "cucumber.plugin",
        value =
//                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm," +
                "net.serenitybdd.cucumber.core.plugin.SerenityReporterParallel," +
                "pretty," +
                "html:target/cucumber-reports/cucumber.html"
)
@ConfigurationParameter(key = "cucumber.glue", value = "com.serenitydojo.playwright.toolshop")
public class CucumberTests {
}
