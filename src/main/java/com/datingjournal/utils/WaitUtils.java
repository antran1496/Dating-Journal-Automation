package com.datingjournal.utils;

import com.datingjournal.config.AppConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Tiện ích chờ tường minh (explicit wait). Thay thế hoàn toàn cho {@code Thread.sleep}.
 * <p>
 * Hỗ trợ Responsive (Desktop & Mobile): khi một locator khớp nhiều phần tử trong DOM
 * (ví dụ 1 bản desktop bị ẩn và 1 bản mobile đang hiển thị), {@link #visible} và {@link #clickable}
 * sẽ tự động chọn phần tử ĐANG HIỂN THỊ thay vì chỉ kiểm tra phần tử đầu tiên.
 */
public final class WaitUtils {

    private WaitUtils() {
    }

    public static WebDriverWait waitFor(WebDriver driver) {
        return waitFor(driver, AppConfig.timeoutSeconds());
    }

    public static WebDriverWait waitFor(WebDriver driver, int timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    /** Chờ và trả về phần tử ĐANG HIỂN THỊ đầu tiên khớp với locator (hỗ trợ cả Desktop & Mobile DOM). */
    public static WebElement visible(WebDriver driver, By locator) {
        return waitFor(driver).until(firstVisibleCondition(locator));
    }

    /** Chờ và trả về phần tử ĐANG HIỂN THỊ và có thể click đầu tiên khớp với locator. */
    public static WebElement clickable(WebDriver driver, By locator) {
        return waitFor(driver).until(d -> {
            for (WebElement element : d.findElements(locator)) {
                if (element.isDisplayed() && element.isEnabled()) {
                    return element;
                }
            }
            return null;
        });
    }

    /** Điều kiện chờ có ít nhất một phần tử hiển thị khớp với locator. */
    public static ExpectedCondition<WebElement> firstVisibleCondition(By locator) {
        return d -> {
            for (WebElement element : d.findElements(locator)) {
                if (element.isDisplayed()) {
                    return element;
                }
            }
            return null;
        };
    }

    public static List<WebElement> allVisible(WebDriver driver, By locator) {
        return waitFor(driver).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public static boolean urlContains(WebDriver driver, String fragment) {
        return waitFor(driver).until(ExpectedConditions.urlContains(fragment));
    }

    public static boolean invisible(WebDriver driver, By locator) {
        return waitFor(driver).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /** Chờ đến khi document.readyState = complete. */
    public static void pageLoaded(WebDriver driver) {
        waitFor(driver).until(d -> "complete".equals(
                ((JavascriptExecutor) d).executeScript("return document.readyState")));
    }
}
