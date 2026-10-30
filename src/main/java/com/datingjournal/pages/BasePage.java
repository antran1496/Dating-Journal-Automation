package com.datingjournal.pages;

import com.datingjournal.config.AppConfig;
import com.datingjournal.core.DriverManager;
import com.datingjournal.utils.WaitUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Arrays;
import java.util.List;

/**
 * Lớp cha của MỌI Page Object và Component.
 * <p>
 * Quy tắc:
 * <ul>
 *     <li>Page con chỉ gọi các hàm protected ở đây, KHÔNG gọi {@code driver.findElement} trực tiếp.</li>
 *     <li>Mọi thao tác đều có explicit wait sẵn — KHÔNG dùng {@code Thread.sleep}.</li>
 *     <li>Page KHÔNG chứa assert; chỉ trả về trạng thái (String/boolean) để Test tự assert.</li>
 * </ul>
 */
public abstract class BasePage {

    /** Phím chọn tất cả theo hệ điều hành: Cmd trên macOS, Ctrl trên Windows/Linux. */
    private static final Keys SELECT_ALL_MODIFIER =
            System.getProperty("os.name", "").toLowerCase().contains("mac") ? Keys.COMMAND : Keys.CONTROL;

    protected final Logger log = LogManager.getLogger(getClass());
    protected final WebDriver driver;

    protected BasePage() {
        this.driver = DriverManager.getDriver();
    }

    // ------------------------------------------------------------ điều hướng

    /** Mở một đường dẫn tương đối so với base URL, ví dụ {@code "/login"}. */
    protected void openPath(String path) {
        String url = AppConfig.baseUrl() + path;
        log.info("Mở trang: {}", url);
        driver.get(url);
        WaitUtils.pageLoaded(driver);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getTitle() {
        return driver.getTitle();
    }

    /** Chờ URL chứa đoạn path; trả về false nếu hết thời gian chờ (không ném lỗi). */
    protected boolean waitForUrlContains(String fragment) {
        try {
            return WaitUtils.urlContains(driver, fragment);
        } catch (TimeoutException e) {
            return false;
        }
    }

    // ------------------------------------------------------------ thao tác

    protected WebElement find(By locator) {
        return WaitUtils.visible(driver, locator);
    }

    protected List<WebElement> findAll(By locator) {
        return driver.findElements(locator);
    }

    protected void click(By locator) {
        log.debug("Click: {}", locator);
        WaitUtils.clickable(driver, locator).click();
    }

    /** Xóa sạch nội dung (kể cả với input React) rồi nhập giá trị mới. Chuỗi rỗng = chỉ xóa. */
    protected void type(By locator, String value) {
        WebElement element = WaitUtils.visible(driver, locator);
        element.click();
        element.sendKeys(Keys.chord(SELECT_ALL_MODIFIER, "a"), Keys.BACK_SPACE);
        if (value != null && !value.isEmpty()) {
            element.sendKeys(value);
        }
    }

    protected String getText(By locator) {
        return find(locator).getText().trim();
    }

    protected String getAttribute(By locator, String attribute) {
        return find(locator).getDomAttribute(attribute);
    }

    // ------------------------------------------------------------ trạng thái

    /** Kiểm tra hiển thị NGAY (không chờ) — trả về true nếu có ít nhất 1 phần tử khớp locator đang hiển thị. */
    protected boolean isDisplayed(By locator) {
        for (WebElement element : driver.findElements(locator)) {
            if (element.isDisplayed()) {
                return true;
            }
        }
        return false;
    }

    /** Chờ phần tử hiển thị trong timeout mặc định; trả về false nếu không xuất hiện. */
    protected boolean isVisibleWithin(By locator) {
        try {
            WaitUtils.visible(driver, locator);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Trả về text nếu phần tử xuất hiện trong timeout, ngược lại trả về chuỗi rỗng. */
    protected String getTextIfVisible(By locator) {
        return isVisibleWithin(locator) ? getText(locator) : "";
    }

    /** Chờ đến khi ÍT NHẤT MỘT trong các phần tử hiển thị; trả về false nếu hết thời gian chờ. */
    protected boolean isAnyVisibleWithin(By... locators) {
        ExpectedCondition<?>[] conditions = Arrays.stream(locators)
                .map(WaitUtils::firstVisibleCondition)
                .toArray(ExpectedCondition[]::new);
        try {
            return WaitUtils.waitFor(driver).until(ExpectedConditions.or(conditions));
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Mỗi Page phải tự định nghĩa thế nào là "đã tải xong". */
    public abstract boolean isLoaded();
}
