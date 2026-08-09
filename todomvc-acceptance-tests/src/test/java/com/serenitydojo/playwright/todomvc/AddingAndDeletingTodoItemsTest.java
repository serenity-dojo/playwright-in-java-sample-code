package com.serenitydojo.playwright.todomvc;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.serenitydojo.playwright.fixtures.ChromeHeadlessOptions;
import com.serenitydojo.playwright.todomvc.pageobjects.TodoMvcAppPage;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Adding and deleting todo items to the list")
@Feature("Adding and deleting todo items to the list")
@UsePlaywright(ChromeHeadlessOptions.class)
class AddingAndDeletingTodoItemsTest {

    TodoMvcAppPage todoMvcApp;

    @BeforeEach
    void openApp(Page page) {
        todoMvcApp = new TodoMvcAppPage(page);
        todoMvcApp.open();
    }

    @Story("When the application starts")
    @DisplayName("When the application starts")
    @Nested
    class WhenTheApplicationStarts {
        @DisplayName("The list should be empty")
        @Test
        void the_list_should_initially_be_empty() {
            // 1) Verify that no items are displayed in the todo list
            assertEquals(0 ,todoMvcApp.countItems());
        }

        @DisplayName("The user should be prompted to enter a todo item")
        @Test
        void the_user_should_be_prompted_to_enter_a_value() {
            // 1) Verify that the input field is visible
            assertTrue(todoMvcApp.getInputField().isVisible());
            // 2) Verify that the placeholder text is "What needs to be done?"
            assertEquals("What needs to be done?", todoMvcApp.getPlaceholderText());
        }
    }

    @Story("When we want to add item to the list")
    @DisplayName("When we want to add item to the list")
    @Nested
    class WhenAddingItems {

        @DisplayName("We can add a single item")
        @Test
        void addingASingleItem() {
            // 1) Add a single todo item "Feed the cat"
            todoMvcApp.addItem("Feed the cat");
            // 2) Verify that the list contains exactly "Feed the cat"
            assertEquals(1, todoMvcApp.countItems());
            assertEquals(List.of("Feed the cat"), todoMvcApp.getItemNames());
        }

        @DisplayName("We can add multiple items")
        @Test
        void addingSeveralItem() {
            List<String> expectedItems = List.of("Feed the cat", "Walk the dog");
            // 1) Add multiple items "Feed the cat" and "Walk the dog"
            todoMvcApp.addItems(expectedItems);
            // 2) Verify that the list contains exactly "Feed the cat" and "Walk the dog"
            assertEquals(2, todoMvcApp.countItems());
            assertEquals(expectedItems, todoMvcApp.getItemNames());
        }

        @DisplayName("We can't add an empty item")
        @Test
        void addingAnEmptyItem() {
            // 1) Add a valid item "Feed the cat"
            todoMvcApp.addItem("Feed the cat");
            // 2) Attempt to add an empty item
            todoMvcApp.addItem("");
            // 3) Verify that the list contains only "Feed the cat"
            assertEquals(1, todoMvcApp.countItems());
            assertEquals(List.of("Feed the cat"), todoMvcApp.getItemNames());
        }

        @DisplayName("We can add duplicate items")
        @Test
        void addingDuplicateItem() {
            List<String> expectedItems = List.of("Feed the cat", "Walk the dog", "Feed the cat");
            // 1) Add items "Feed the cat", "Walk the dog", and "Feed the cat" again
            todoMvcApp.addItems(expectedItems);
            // 2) Verify that the list contains duplicates in the order they were added
            assertEquals(expectedItems, todoMvcApp.getItemNames());
        }

        @DisplayName("We can add items with non-English characters")
        @ParameterizedTest
        @CsvSource({
                "Buy groceries",
                "إطعام القط",
                "Einkaufen gehen",
                "Faire le ménage",
                "Зателефонувати мамі",
                "Aprender Java",
                "Scrivere i test Playwright",
                "喂猫",
                "Leer un libro",
                "Plan a trip"
        })
        void addingNonEnglishItems(String itemName) {
            // 1) Add items in various languages (e.g., "Feed the cat", "喂猫", "إطعام القط")
            todoMvcApp.addItem(itemName);
            // 2) Verify that each item appears in the list as added
            Assertions.assertThat(todoMvcApp.getItemNames()).containsExactly(itemName);
        }
    }

    @Story("When we want to delete item in the list")
    @DisplayName("When we want to delete item in the list")
    @Nested
    class WhenDeletingItems {
        List<String> items = List.of("Feed the cat", "Walk the dog", "Buy some milk");

        @DisplayName("We can delete an item in the middle of the list")
        @Test
        void deletingAnItemInTheMiddleOfTheList() {

            List<String> expectedItems = List.of("Feed the cat", "Buy some milk");
            // 1) Add items "Feed the cat", "Walk the dog", "Buy some milk"
            todoMvcApp.addItems(items);
            // 2) Delete "Walk the dog"
            todoMvcApp.deleteItem("Walk the dog");
            // 3) Verify that the list contains "Feed the cat" and "Buy some milk"
            assertEquals(expectedItems, todoMvcApp.getItemNames());
        }

        @DisplayName("We can delete an item at the end of the list")
        @Test
        void deletingAnItemAtTheEndOfTheList() {
            List<String> expectedItems = List.of("Feed the cat", "Walk the dog");
            // 1) Add items "Feed the cat", "Walk the dog", "Buy some milk"
            todoMvcApp.addItems(items);
            // 2) Delete "Buy some milk"
            todoMvcApp.deleteItem("Buy some milk");
            // 3) Verify that the list contains "Feed the cat" and "Walk the dog"
            assertEquals(expectedItems, todoMvcApp.getItemNames());
        }

        @DisplayName("We can delete an item at the start of the list")
        @Test
        void deletingAnItemAtTheStartOfTheList() {
            List<String> expectedItems = List.of("Walk the dog", "Buy some milk");
            // 1) Add items "Feed the cat", "Walk the dog", "Buy some milk"
            todoMvcApp.addItems(items);
            // 2) Delete "Feed the cat"
            todoMvcApp.deleteItem("Feed the cat");
            // 3) Verify that the list contains "Walk the dog" and "Buy some milk"
            assertEquals(expectedItems, todoMvcApp.getItemNames());
        }
    }
}
