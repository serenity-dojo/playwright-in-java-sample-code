package com.serenitydojo.playwright.fixtures;

import com.microsoft.playwright.junit.UsePlaywright;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@UsePlaywright(PlaywrightOptions.class)
@ExtendWith(CrossCuttingExtension.class)
public @interface PlaywrightTest {
}
