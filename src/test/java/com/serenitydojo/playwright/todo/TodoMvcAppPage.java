package com.serenitydojo.playwright.todo;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class TodoMvcAppPage {

    private final Page page;

    private final Locator todoItems;
    private final Locator todoField;

    private final String baseUrl;

    public TodoMvcAppPage(Page page) {
        this.page = page;
        baseUrl = (StringUtils.isEmpty(System.getenv("APP_HOST_URL"))) ? "http://localhost:7002" : System.getenv("APP_HOST_URL");
        this.todoItems = page.getByTestId("todo-item");
        this.todoField = page.getByTestId("text-input");
    }

    public void open() {
        page.navigate(baseUrl);
    }

    public List<String> todoItemsDisplayed() {
        return todoItems.allTextContents();
    }

    public Locator todoField() {
        return todoField;
    }

    public void addTodoItem(String todoItem) {
        todoField.fill(todoItem);
        todoField.press("Enter");
    }

    public void addTodoItems(String... items) {
        for (String item : items) {
            addTodoItem(item);
        }
    }

    public void deleteItem(String itemName) {
        Locator itemRow = itemRow(itemName);
        Locator deleteButton = itemRow.getByTestId("todo-item-button");

        itemRow.hover();
        deleteButton.click();
    }

    public Locator itemRow(String itemName) {
        return page.getByTestId("todo-item").filter(new Locator.FilterOptions().setHasText(itemName));
    }

    public void completeItem(String itemName) {
        Locator itemRow = itemRow(itemName);
        Locator completeButton = itemRow.getByTestId("todo-item-toggle");
        completeButton.click();

    }

    public String todoCount() {
        return page.locator(".todo-count").textContent();
    }

    public String activeFilter() {
        return page.locator(".filters .selected").textContent();
    }

    public void filterItemsBy(String filter) {
        page.locator(".filters").getByText(filter).click();
    }

    public void clearCompleted() {
        page.getByTestId("footer").getByText("Clear completed").click();
    }
}
