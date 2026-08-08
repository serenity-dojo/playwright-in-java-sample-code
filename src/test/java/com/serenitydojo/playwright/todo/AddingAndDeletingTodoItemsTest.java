package com.serenitydojo.playwright.todo;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

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
            Assertions.assertThat(todoMvcApp.todoItemsDisplayed()).isEmpty();
        }

        @DisplayName("The user should be prompted to enter a todo item")
        @Test
        void the_user_should_be_prompted_to_enter_a_value() {
            assertThat(todoMvcApp.todoField()).isVisible();
            assertThat(todoMvcApp.todoField()).hasAttribute("placeholder", "What needs to be done?");
        }

    }

    @Story("When we want to add item to the list")
    @DisplayName("When we want to add item to the list")
    @Nested
    class WhenAddingItems {

        @DisplayName("We can add a single item")
        @Test
        void addingASingleItem() {
            todoMvcApp.addTodoItem("Feed the cat");

            Assertions.assertThat(todoMvcApp.todoItemsDisplayed()).containsExactly("Feed the cat");

        }

        @DisplayName("We can add multiple items")
        @Test
        void addingSeveralItem() {
            todoMvcApp.addTodoItem("Feed the cat");
            todoMvcApp.addTodoItem("Walk the dog");

            Assertions.assertThat(todoMvcApp.todoItemsDisplayed()).containsExactly("Feed the cat", "Walk the dog");
        }

        @DisplayName("We can't add add an empty item")
        @Test
        void addingAnEmptyItem() {
            todoMvcApp.addTodoItem("Feed the cat");
            todoMvcApp.addTodoItem("");

            Assertions.assertThat(todoMvcApp.todoItemsDisplayed()).containsExactly("Feed the cat");

        }

        @DisplayName("We can add duplicate items")
        @Test
        void addingDuplicateItem() {
            todoMvcApp.addTodoItem("Feed the cat");
            todoMvcApp.addTodoItem("Walk the dog");
            todoMvcApp.addTodoItem("Feed the cat");

            Assertions.assertThat(todoMvcApp.todoItemsDisplayed()).containsExactly("Feed the cat", "Walk the dog","Feed the cat");
        }

        @DisplayName("We can add items with non-English characters")
        @ParameterizedTest
        @CsvSource({
                "Feed the cat",            // English
                "Alimentar al gato",       // Spanish
                "להאכיל את החתול",         // Hebrew
                "ให้อาหารแมว",              // Thai
                "喂猫",                     // Chinese
                "إطعام القط",              // Arabic
                "кормить кошку",           // Russian
                "猫に餌をやる",              // Japanese
                "고양이 먹이기"                // Korean
        })
        void addingNonEnglishItems(String item) {
            todoMvcApp.addTodoItem(item);
            Assertions.assertThat(todoMvcApp.todoItemsDisplayed()).containsExactly(item);
        }
    }

    @Story("When we want to delete item in the list")
    @DisplayName("When we want to delete item in the list")
    @Nested
    class WhenDeletingItems {

        @DisplayName("We can delete an item in the middle of the list")
        @Test
        void deletingAnItemInTheMiddleOfTheList() {
            todoMvcApp.addTodoItems("Feed the cat", "Walk the dog", "Buy some milk");

            todoMvcApp.deleteItem("Walk the dog");

            Assertions.assertThat(todoMvcApp.todoItemsDisplayed()).containsExactly("Feed the cat", "Buy some milk");

        }


        @DisplayName("We can delete an item at the end of the list")
        @Test
        void deletingAnItemAtTheEndOfTheList() {
            todoMvcApp.addTodoItems("Feed the cat", "Walk the dog", "Buy some milk");

            todoMvcApp.deleteItem("Buy some milk");

            Assertions.assertThat(todoMvcApp.todoItemsDisplayed()).containsExactly("Feed the cat", "Walk the dog");

        }



        @DisplayName("We can delete an item at the start of the list")
        @Test
        void deletingAnItemAtTheStartOfTheList() {
            todoMvcApp.addTodoItems("Feed the cat", "Walk the dog", "Buy some milk");

            todoMvcApp.deleteItem("Feed the cat");

            Assertions.assertThat(todoMvcApp.todoItemsDisplayed()).containsExactly("Walk the dog", "Buy some milk");

        }

    }

}
