package com.serenitydojo.playwright.fixtures;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Tracing;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CrossCuttingExtension implements BeforeEachCallback, AfterEachCallback {

    @Override
    public void beforeEach(ExtensionContext ctx) {
        if (!(ctx.getRequiredTestInstance() instanceof WithTracing)) return;
        var context = getBrowserContext(ctx);
        if (context == null) return;
        context.tracing().start(
                new Tracing.StartOptions().setScreenshots(true).setSnapshots(true));
    }

    @Override
    public void afterEach(ExtensionContext ctx) {
        var failed = ctx.getExecutionException().isPresent();
        var instance = ctx.getRequiredTestInstance();
        var context = getBrowserContext(ctx);
        if (context == null) return;

        if (instance instanceof WithTracing) {
            try {
                var dest = Paths.get("target/traces", ctx.getDisplayName() + ".zip");
                Files.createDirectories(dest.getParent());
                context.tracing().stop(new Tracing.StopOptions().setPath(dest));
                if (failed) {
                    Allure.addAttachment("Trace", new FileInputStream(dest.toFile()));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (instance instanceof TakeFinalScreenshot && failed) {
            var pages = context.pages();
            if (!pages.isEmpty()) {
                Allure.addAttachment("Screenshot", "image/png",
                        new ByteArrayInputStream(pages.get(0).screenshot()), "png");
            }
        }
    }

    private static BrowserContext getBrowserContext(ExtensionContext ctx) {
        try {
            Class<?> cls = Class.forName("com.microsoft.playwright.impl.junit.BrowserContextExtension");
            Method method = cls.getMethod("getOrCreateBrowserContext", ExtensionContext.class);
            return (BrowserContext) method.invoke(null, ctx);
        } catch (Exception e) {
            return null;
        }
    }
}
