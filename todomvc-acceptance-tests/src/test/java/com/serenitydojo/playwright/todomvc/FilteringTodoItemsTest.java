package com.serenitydojo.playwright.todomvc;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.serenitydojo.playwright.fixtures.ChromeHeadlessOptions;
import com.serenitydojo.playwright.todomvc.pageobjects.TodoMvcAppPage;
import io.qameta.allure.Feature;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

@DisplayName("Filtering todo items")
@UsePlaywright(ChromeHeadlessOptions.class)
@Feature("Filtering todo items")
class FilteringTodoItemsTest {
    List<String> items = List.of("Feed the cat", "Walk the dog", "Buy some milk");
    TodoMvcAppPage todoMvcApp;

    @BeforeEach
    void openApp(Page page) {
        todoMvcApp = new TodoMvcAppPage(page);
        todoMvcApp.open();
    }

    @DisplayName("All items should be displayed by default")
    @Test
    void allItemsShouldBeDisplayedByDefault() {
        // 1) Add "Feed the cat", "Walk the dog", "Buy some milk"
        todoMvcApp.addItems(items);
        // 2) Verify that the active filter is set to "All"
        Assertions.assertThat(todoMvcApp.getSelectedFilter()).isEqualTo("All");
    }

    @DisplayName("Should be able to filter active items")
    @Test
    void shouldBeAbleToFilterByActiveItems() {
        List<String> expectedItems = List.of("Feed the cat", "Buy some milk");
        // 1) Add "Feed the cat", "Walk the dog", "Buy some milk"
        todoMvcApp.addItems(items);
        // 2) Complete "Walk the dog"
        todoMvcApp.completeItem("Walk the dog");
        // 3) Apply the "Active" filter
        todoMvcApp.selectFilter("Active");
        // 4) Verify that only "Feed the cat" and "Buy some milk" are displayed
        Assertions.assertThat(todoMvcApp.getItemNames()).isEqualTo(expectedItems);
    }

    @DisplayName("Should be able to filter completed items")
    @Test
    void shouldBeAbleToFilterByCompletedItems() {
        // 1) Add "Feed the cat", "Walk the dog", "Buy some milk"
        todoMvcApp.addItems(items);
        // 2) Complete "Walk the dog"
        todoMvcApp.completeItem("Walk the dog");
        // 3) Apply the "Completed" filter
        todoMvcApp.selectFilter("Completed");
        // 4) Verify that only "Walk the dog" is displayed
        Assertions.assertThat(todoMvcApp.getItemNames()).containsExactly("Walk the dog");
    }

    @DisplayName("Should be able to revert to showing all items")
    @Test
    void shouldBeAbleToRevertToShowingAllItems() {
        // TODO: Implement me
        // 1) Add "Feed the cat", "Walk the dog", "Buy some milk"
        todoMvcApp.addItems(items);
        // 2) Complete "Walk the dog"
        todoMvcApp.completeItem("Walk the dog");
        // 3) Apply the "Completed" filter
        todoMvcApp.selectFilter("Completed");
        // 4) Apply the "All" filter
        todoMvcApp.selectFilter("All");
        // 5) Verify that all three items ("Feed the cat", "Walk the dog", "Buy some milk") are displayed
        Assertions.assertThat(todoMvcApp.getItemNames()).isEqualTo(items);
    }
}
