# PlaywrightJava Test Project

## Overview
- **AUT**: https://practicesoftwaretesting.com
- **Stack**: Playwright 1.57, JUnit Jupiter 6, AssertJ, Allure, Maven · Java 21
- **Tests**: integration tests (`*IT` suffix) via `maven-failsafe-plugin`

## Commands
```bash
mvn verify          # compile + run IT tests + collect Allure results
mvn allure:serve    # generate and open Allure report in browser
```

## Package Layoutrest
```
com.serenitydojo.playwright
  ├── tests/         # JUnit test classes only
  ├── workflows/     # business-level orchestration
  ├── pages/         # full-page objects
  ├── components/    # reusable UI component objects (NavBar, Modal, ProductCard…)
  ├── fixtures/      # API-based test data setup
  └── domain/        # Java 21 records for domain data
```

## Test Lifecycle — Composition Over Inheritance
- Every test class is annotated `@PlaywrightTest` (= `@UsePlaywright` + `@ExtendWith(CrossCuttingExtension.class)`) — **no BaseTest, no inheritance**
- `Page`, `BrowserContext`, `Browser`, `Playwright` are injected as **method parameters**
- Cross-cutting behaviour via marker interfaces; test classes opt-in by implementing them:
  - `TakeFinalScreenshot` — attaches a PNG to Allure on test failure
  - `WithTracing` — records a Playwright trace per test; attaches the zip on failure

→ [docs/examples/test-lifecycle.md](docs/examples/test-lifecycle.md)

## Three-Layer Architecture
```
Test  →  Workflow  →  Page / Component
```
| Layer | Responsibility | Returns |
|---|---|---|
| Test | Calls workflow methods only; zero `Page` or locator usage | — |
| Workflow | Orchestrates pages/components; one method = one business action | domain record or `this` |
| Page / Component | Wraps Playwright locators; fluent API | `this` or domain record |

**Component objects** are page objects scoped to one reusable UI element — `NavBar`, `ProductCard`, `Modal`, `CartWidget`. Never model an entire page as a component.

## Test Data

**Randomized values**: use `net.datafaker:datafaker`; generate values inside domain record factory methods, not inline in tests.

```java
record Customer(String email, String password) {
    static Customer random() {
        var f = new Faker();
        return new Customer(f.internet().emailAddress(), "Pw-" + f.number().digits(8) + "!");
    }
}
```

**API seeding**: create preconditions through the app's REST API — never through the UI.
- Fixture classes live in `fixtures/`; call them from `@BeforeEach` or directly in the test before the workflow
- `APIRequestContext` is obtained from `page.context().request()`
- Every fixture method carries `@Step` so it appears in the Allure report

```java
class UserFixture {
    @Step("Register user via API")
    static RegisteredUser register(APIRequestContext api) {
        var email    = new Faker().internet().emailAddress();
        var password = "Pw-" + new Faker().number().digits(8) + "!";
        var r = api.post("/users/register",
            RequestOptions.create().setData(Map.of("email", email, "password", password)));
        assertThat(r.status()).isEqualTo(201);
        return new RegisteredUser(email, password);
    }
}
```

- Never share a fixed test account between tests — seed fresh data per test run

## Allure
- `@Step` on **all public methods** in workflow, page, and component classes
- `@Description`, `@Severity`, `@Story` on test methods where meaningful
- Screenshots and traces attached **only on failure**, via marker interfaces

## Locator Priority
`getByRole` › `getByLabel` › `getByText` › `getByTestId` › CSS selector › XPath (last resort only)

## Coding Rules
- Java 21: `record` for all domain models; `var` for all local variables
- **No `Thread.sleep()`** — use `page.waitForResponse()`, `locator.waitFor()`, or Playwright's built-in auto-waiting
- **AssertJ only** for assertions — never `org.junit.jupiter.api.Assertions`
- Page/component methods return `this` for fluent chaining; terminal methods return a domain record or `void`
- `@Nested` + `@DisplayName` for readable test hierarchy; every test method has a `@DisplayName`
- Keep test data in domain records; avoid hard-coding raw strings in test assertions

## Naming
| Artefact | Pattern | Example |
|---|---|---|
| Test class | `<Feature>IT` | `CheckoutIT` |
| Workflow | `<Feature>Workflow` | `CheckoutWorkflow` |
| Page object | `<Page>Page` | `ProductDetailPage` |
| Component | bare noun | `NavBar`, `Modal` |
| Domain record | singular noun | `Product`, `CartItem`, `Address` |

## Never
- Inherit from a base class
- Call `Page` or a locator directly from a test method
- Use `Thread.sleep()` for any reason
- Use XPath when a role, label, or text selector exists
- Write assertions inside page or workflow classes
- Use `org.junit.jupiter.api.Assertions`
