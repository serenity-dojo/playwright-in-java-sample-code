package com.serenitydojo.playwright.toolshop.restAPITest;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Route;
import com.microsoft.playwright.junit.UsePlaywright;
import com.serenitydojo.playwright.HeadlessChromeOptions;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


@UsePlaywright(HeadlessChromeOptions.class)
public class PlaywrightRestAPITest {

    @Test
    void whenASingleItemIsFound(Page page){

        //this will mock out the product and make it up
        page.route("**/products/search", route -> {
            route.fulfill(
                    new Route.FulfillOptions()
                            .setBody(MockSearchResponses.RESPONSE_WITH_A_SINGLE_ENTRY)
                            .setStatus(200)
            );
        });

        page.navigate("https://practicesoftwaretesting.com");
        page.getByPlaceholder("Search").fill("Pliers");
        page.getByPlaceholder("Search").press("Enter");

        page.waitForTimeout(5000);

        assertThat(page.getByTestId("product-name")).hasCount(1);
        assertThat(page.getByTestId("product-name")).hasText("Super Pliers");

    }
}
