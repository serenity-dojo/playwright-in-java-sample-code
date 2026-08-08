package com.serenitydojo.playwright.todo;


import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import io.qameta.allure.Feature;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@DisplayName("Filtering todo items")
@UsePlaywright(ChromeHeadlessOptions.class)
@Feature("Filtering todo items")
class FilteringTodoItemsTest {

    TodoMvcAppPage todoMvcApp;

    @BeforeEach
    void openApp(Page page) {
        todoMvcApp = new TodoMvcAppPage(page);
        todoMvcApp.open();
    }

    @DisplayName("All items should be displayed by default")
    @Test
    void allItemsShouldBeDisplayedByDefault() {
        todoMvcApp.addTodoItems("Feed the cat", "Walk the dog", "Buy some milk");

        Assertions.assertThat(todoMvcApp.activeFilter()).isEqualTo("All");
    }


    @DisplayName("Should be able to filter active items")
    @Test
    void shouldBeAbleToFilterByActiveItems() {
        todoMvcApp.addTodoItems("Feed the cat", "Walk the dog", "Buy some milk");

        todoMvcApp.completeItem("Walk the dog");
        todoMvcApp.filterItemsBy("Active");

        Assertions.assertThat(todoMvcApp.todoItemsDisplayed()).containsExactly("Feed the cat", "Buy some milk");
    }

    @DisplayName("Should be able to filter completed items")
    @Test
    void shouldBeAbleToFilterByCompletedItems() {
        todoMvcApp.addTodoItems("Feed the cat", "Walk the dog", "Buy some milk");

        todoMvcApp.completeItem("Walk the dog");
        todoMvcApp.filterItemsBy("Completed");

        Assertions.assertThat(todoMvcApp.todoItemsDisplayed()).containsExactly("Walk the dog");
    }

    @DisplayName("Should be able to revert to showing all items")
    @Test
    void shouldBeAbleToRevertToShowingAllItems() {
        todoMvcApp.addTodoItems("Feed the cat", "Walk the dog", "Buy some milk");

        todoMvcApp.completeItem("Walk the dog");
        todoMvcApp.filterItemsBy("Completed");
        todoMvcApp.filterItemsBy("All");

        Assertions.assertThat(todoMvcApp.todoItemsDisplayed()).containsExactly("Feed the cat", "Walk the dog", "Buy some milk");
    }

}
