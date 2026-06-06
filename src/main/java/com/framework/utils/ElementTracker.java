package com.framework.utils;

import org.openqa.selenium.WebElement;

public class ElementTracker {

    private static final ThreadLocal<WebElement> element = new ThreadLocal<>();

    public static void track(WebElement webElement) {
        element.set(webElement);
    }

    public static WebElement get() {
        return element.get();
    }

    public static void clear() {
        element.remove();
    }
}