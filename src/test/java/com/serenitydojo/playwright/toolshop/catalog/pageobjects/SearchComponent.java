package com.serenitydojo.playwright.toolshop.catalog.pageobjects;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import net.serenitybdd.annotations.Step;

public class SearchComponent {
    private final Page page;

    public SearchComponent(Page page) {
        this.page = page;
    }

    @Step("Search for {0}")
    public void searchBy(String keyword) {
        page.waitForResponse(response ->
                response.url().endsWith("/products/search")
                        && "QUERY".equalsIgnoreCase(
                        response.request().method()
                )
                        && response.status() == 200,
                () -> {
            page.getByPlaceholder("Search").fill(keyword);
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
        });
    }

    @Step("Clear search")
    public void clearSearch() {
        page.waitForResponse("**/products**", () -> {
            page.getByTestId("search-reset").click();
        });
    }

    @Step("Filter by {0}")
    public void filterBy(String filterName) {

        page.waitForResponse(
                response ->
                        response.url().endsWith("/products")
                                && "QUERY".equalsIgnoreCase(
                                response.request().method()
                        )
                                && response.status() == 200,
                () -> page.getByLabel(filterName).check()
        );
    }

    @Step("Sort by {0}")
    public void sortBy(String sortFilter) {
        page.waitForResponse(response ->
                response.url().endsWith("/products")
                        && "QUERY".equalsIgnoreCase(
                        response.request().method()
                )
                        && response.status() == 200, () -> {
            page.getByTestId("sort").selectOption(sortFilter);
        });
    }
}