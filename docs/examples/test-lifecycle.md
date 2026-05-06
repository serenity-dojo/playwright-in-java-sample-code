# Test Lifecycle Pattern

The cross-cutting extension pattern is the only genuinely non-obvious design decision.
Everything else (three-layer architecture, locator priority) is self-evident from CLAUDE.md.

## `@PlaywrightTest` — composed annotation

Keeps test classes clean; one annotation covers lifecycle + cross-cutting.

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@UsePlaywright
@ExtendWith(CrossCuttingExtension.class)
public @interface PlaywrightTest {}
```

## Marker interfaces

```java
public interface TakeFinalScreenshot {}
public interface WithTracing {}
```

## CrossCuttingExtension

`PlaywrightExtension` (the extension behind `@UsePlaywright`) stores `Page` and friends
in the JUnit extension store under its own namespace. Retrieve them with the helper below.

```java
public class CrossCuttingExtension implements BeforeEachCallback, AfterEachCallback {

    private static final ExtensionContext.Namespace PW_NS =
        ExtensionContext.Namespace.create(PlaywrightExtension.class);

    @Override
    public void beforeEach(ExtensionContext ctx) throws Exception {
        if (ctx.getRequiredTestInstance() instanceof WithTracing) {
            page(ctx).context().tracing().start(
                new Tracing.StartOptions().setScreenshots(true).setSnapshots(true));
        }
    }

    @Override
    public void afterEach(ExtensionContext ctx) throws Exception {
        var failed = ctx.getExecutionException().isPresent();
        var page   = page(ctx);

        if (ctx.getRequiredTestInstance() instanceof WithTracing) {
            var dest = Paths.get("target/traces", ctx.getDisplayName() + ".zip");
            Files.createDirectories(dest.getParent());
            page.context().tracing().stop(new Tracing.StopOptions().setPath(dest));
            if (failed)
                Allure.addAttachment("Trace", new FileInputStream(dest.toFile()));
        }

        if (ctx.getRequiredTestInstance() instanceof TakeFinalScreenshot && failed) {
            Allure.addAttachment("Screenshot", "image/png",
                new ByteArrayInputStream(page.screenshot()), "png");
        }
    }

    private Page page(ExtensionContext ctx) {
        return ctx.getStore(PW_NS).get(Page.class, Page.class);
    }
}
```

> **Upgrade note**: `PlaywrightExtension.class` is the public class behind `@UsePlaywright`.
> Verify the namespace key resolves correctly after any Playwright major version bump.

## Typical test class

```java
@PlaywrightTest
@DisplayName("Checkout")
class CheckoutIT implements TakeFinalScreenshot, WithTracing {

    @Nested
    @DisplayName("As a guest")
    class AsAGuest {

        @Test
        @DisplayName("I can add a product and complete checkout")
        void completeCheckout(Page page) {
            var order = new CheckoutWorkflow(page)
                .addToCart("Bolt Cutters")
                .checkout(new Address("John", "Doe", "123 Main St", "London", "SW1A 1AA"));

            assertThat(order.confirmationNumber()).isNotBlank();
        }
    }
}
```
