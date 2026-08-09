package com.serenitydojo.playwright.todomvc.pageobjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class TodoMvcAppPage {

    private final Page page;
    private final String baseUrl;

    private final Locator TO_DO_ITEM;
    private final Locator T0_DO_ITEM_LABEL;
    private final Locator TO_DO_INPUT;
    private final Locator COMPLETED_ITEMS;
    private final Locator TO_DO_COUNT_LABEL;
    private final Locator CLEAR_COMPLETED_BUTTON;
    private final Locator FOOT_NAVIGATION;

    public TodoMvcAppPage(Page page) {
        this.page = page;
        baseUrl = (StringUtils.isEmpty(System.getenv("APP_HOST_URL"))) ? "http://localhost:7002" : System.getenv("APP_HOST_URL");

        this.TO_DO_ITEM = page.getByTestId("todo-item");
        this.T0_DO_ITEM_LABEL = page.getByTestId("todo-item-label");
        this.TO_DO_INPUT = page.getByTestId("text-input");
        this.COMPLETED_ITEMS = page.locator(".completed");
        this.TO_DO_COUNT_LABEL = page.locator(".todo-count");
        this.CLEAR_COMPLETED_BUTTON = page.locator(".clear-completed");
        this.FOOT_NAVIGATION = page.getByTestId("footer-navigation");
    }

    public void open() {
        page.navigate(baseUrl);
    }

    public int countItems() {
        return TO_DO_ITEM.count();
    }

    public String getPlaceholderText(){
        return TO_DO_INPUT.getAttribute("placeholder");
    }

    public Locator getInputField(){
        return TO_DO_INPUT;
    }

    public void addItem(String itemName){
        TO_DO_INPUT.fill(itemName);
        TO_DO_INPUT.press("Enter");
    }

    public List<String> getItemNames(){
        return T0_DO_ITEM_LABEL.allInnerTexts();
    }

    public void addItems(List<String> itemNames){
        for (String itemName : itemNames) {
            TO_DO_INPUT.fill(itemName);
            TO_DO_INPUT.press("Enter");
        }
    }

    public void deleteItem(String itemName){
        Locator itemToDelete = TO_DO_ITEM
                .filter(new Locator.FilterOptions().setHas(page.getByText(itemName, new Page.GetByTextOptions().setExact(true))));
        itemToDelete.hover();
        itemToDelete.getByTestId("todo-item-button").click();
    }

    public void completeItem(String itemName) {
        Locator itemToComplete = TO_DO_ITEM
                .filter(new Locator.FilterOptions().setHas(page.getByText(itemName, new Page.GetByTextOptions().setExact(true))));
        itemToComplete.getByTestId("todo-item-toggle").click();
    }

    public List<String> getCompletedItems(){
        return COMPLETED_ITEMS.allInnerTexts();
    }

    public String getToDoCountLabel() {
        return TO_DO_COUNT_LABEL.innerText();
    }

    public void clickOnClearCompleted() {
        CLEAR_COMPLETED_BUTTON.click();
    }

    public String getSelectedFilter(){
        return FOOT_NAVIGATION.locator(".selected").innerText();
    }

    public void selectFilter(String filterLabel) {
        FOOT_NAVIGATION.getByText(filterLabel).click();
    }

    // TODO: Add page object methods here
}
