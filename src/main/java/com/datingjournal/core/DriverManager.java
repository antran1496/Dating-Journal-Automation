package com.datingjournal.core;

import org.openqa.selenium.WebDriver;

/**
 * Giữ WebDriver theo từng thread (ThreadLocal) để chạy song song an toàn.
 * Đây là điểm truy cập driver DUY NHẤT trong toàn framework.
 */
public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
    }

    public static void initDriver() {
        if (DRIVER.get() == null) {
            DRIVER.set(DriverFactory.createDriver());
        }
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver chưa được khởi tạo cho thread hiện tại. Test đã kế thừa BaseTest chưa?");
        }
        return driver;
    }

    public static boolean hasDriver() {
        return DRIVER.get() != null;
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                DRIVER.remove();
            }
        }
    }
}
