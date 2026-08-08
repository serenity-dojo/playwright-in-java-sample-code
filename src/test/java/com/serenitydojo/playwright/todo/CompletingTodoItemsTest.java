package com.serenitydojo.playwright.todo;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import io.qameta.allure.Feature;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@DisplayName("Completing todo items to the list")
@UsePlaywright(ChromeHeadlessOptions.class)
@Feature("Completing todo items to the list")
class CompletingTodoItemsTest {

    TodoMvcAppPage todoMvcApp;

    @BeforeEach
    void openApp(Page page) {
        todoMvcApp = new TodoMvcAppPage(page);

        todoMvcApp.open();
    }

    @DisplayName("Completed items should be marked as completed")
    @Test
    void completedItemsShouldBeMarkedAsCompleted() {
        todoMvcApp.addTodoItems("Feed the cat","Walk the dog","Buy some milk");

        todoMvcApp.completeItem("Feed the cat");

        assertThat(todoMvcApp.itemRow("Feed the cat")).hasClass("completed");
    }

    @DisplayName("Completing an item should update the number of items left count")
    @Test
    void shouldUpdateNumberOfItemsLeftCount() {
        todoMvcApp.addTodoItems("Feed the cat","Walk the dog","Buy some milk");

        todoMvcApp.completeItem("Feed the cat");

        Assertions.assertThat(todoMvcApp.todoCount()).isEqualTo("2 items left!");
    }

    @DisplayName("Should be able to clear completed items")
    @Test
    void shouldBeAbleToClearCompletedItems() {
        todoMvcApp.addTodoItems("Feed the cat", "Walk the dog", "Buy some milk");

        todoMvcApp.completeItem("Walk the dog");
        todoMvcApp.clearCompleted();

        Assertions.assertThat(todoMvcApp.todoItemsDisplayed()).containsExactly("Feed the cat", "Buy some milk");
    }
}
