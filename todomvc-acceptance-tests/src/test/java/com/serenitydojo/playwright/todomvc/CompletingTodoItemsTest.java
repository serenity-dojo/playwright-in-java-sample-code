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

@DisplayName("Completing todo items to the list")
@UsePlaywright(ChromeHeadlessOptions.class)
@Feature("Completing todo items to the list")
class CompletingTodoItemsTest {

    TodoMvcAppPage todoMvcApp;
    List<String> items = List.of("Feed the cat", "Walk the dog", "Buy some milk");

    @BeforeEach
    void openApp(Page page) {
        todoMvcApp = new TodoMvcAppPage(page);

        todoMvcApp.open();
    }

    @DisplayName("Completed items should be marked as completed")
    @Test
    void completedItemsShouldBeMarkedAsCompleted() {
        // 1) Add "Feed the cat", "Walk the dog", "Buy some milk"
        todoMvcApp.addItems(items);
        // 2) Complete "Feed the cat"
        todoMvcApp.completeItem("Feed the cat");
        // 3) Check that "Feed the cat" is shown as completed
        Assertions.assertThat(todoMvcApp.getCompletedItems()).containsExactly("Feed the cat");
    }

    @DisplayName("Completing an item should update the number of items left count")
    @Test
    void shouldUpdateNumberOfItemsLeftCount() {
        // 1) Add "Feed the cat", "Walk the dog", "Buy some milk"
        todoMvcApp.addItems(items);
        // 2) Complete "Feed the cat"
        todoMvcApp.completeItem("Feed the cat");
        // 3) Verify the todo count shows "2 items left!"
        Assertions.assertThat(todoMvcApp.getToDoCountLabel()).isEqualTo("2 items left!");
    }

    @DisplayName("Should be able to clear completed items")
    @Test
    void shouldBeAbleToClearCompletedItems() {
        List<String> expectedItems = List.of("Feed the cat", "Buy some milk");
        // 1) Add "Feed the cat", "Walk the dog", "Buy some milk"
        todoMvcApp.addItems(items);
        // 2) Complete "Walk the dog"
        todoMvcApp.completeItem("Walk the dog");
        // 3) Clear the completed items
        todoMvcApp.clickOnClearCompleted();
        // 4) Verify that the remaining items are "Feed the cat" and "Buy some milk"
        Assertions.assertThat(todoMvcApp.getItemNames()).isEqualTo(expectedItems);
    }
}
