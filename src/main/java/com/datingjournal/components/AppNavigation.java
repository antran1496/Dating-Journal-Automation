package com.datingjournal.components;

import com.datingjournal.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

/**
 * Component thanh điều hướng chính xuất hiện trên mọi trang sau khi đăng nhập.
 * <p>
 * Hỗ trợ cả 2 giao diện Responsive:
 * <ul>
 *     <li><b>Desktop ({@code >= 768px})</b>: thanh {@code <header><nav>} ở phía trên màn hình.</li>
 *     <li><b>Mobile ({@code < 768px})</b>: thanh {@code <nav>} cố định ở đáy màn hình (Bottom Bar),
 *     kèm nút tạo nhanh buổi hẹn ({@code /dates/new}).</li>
 * </ul>
 * Nhờ {@link BasePage} tự động chọn phần tử đang hiển thị ({@code isDisplayed() == true}),
 * bộ locator {@code nav a[href='...']} hoạt động thống nhất trên cả Desktop lẫn Mobile.
 */
public class AppNavigation extends BasePage {

    private static final By HOME_LINK = By.cssSelector("nav a[href='/dashboard']");
    private static final By DATES_LINK = By.cssSelector("nav a[href='/dates']");
    private static final By CUPID_LINK = By.cssSelector("nav a[href='/chat']");
    private static final By MORE_LINK = By.cssSelector("nav a[href='/more']");
    private static final By QUICK_NEW_DATE_LINK = By.cssSelector("nav a[href='/dates/new']");
    private static final By DESKTOP_PROFILE_LINK = By.cssSelector("header a[href='/settings']");

    @Step("Bấm menu Trang chủ")
    public void goToHome() {
        click(HOME_LINK);
    }

    @Step("Bấm menu Hẹn hò")
    public void goToDates() {
        click(DATES_LINK);
    }

    @Step("Bấm menu Cupid")
    public void goToCupid() {
        click(CUPID_LINK);
    }

    @Step("Bấm menu Thêm")
    public void goToMore() {
        click(MORE_LINK);
    }

    @Step("Bấm nút Ghi nhanh buổi hẹn mới (Mobile Bottom Bar)")
    public void clickQuickNewDate() {
        click(QUICK_NEW_DATE_LINK);
    }

    @Override
    public boolean isLoaded() {
        return isVisibleWithin(HOME_LINK);
    }

    /** Nút avatar dẫn tới Cài đặt trên header (chỉ hiển thị ở giao diện Desktop). */
    public boolean isDesktopProfileLinkDisplayed() {
        return isDisplayed(DESKTOP_PROFILE_LINK);
    }

    /** Nút dấu cộng Ghi nhanh buổi hẹn ở thanh điều hướng đáy (chỉ hiển thị ở giao diện Mobile). */
    public boolean isMobileQuickAddDisplayed() {
        return isDisplayed(QUICK_NEW_DATE_LINK);
    }
}
