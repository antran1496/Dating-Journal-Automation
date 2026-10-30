package com.datingjournal.pages;

import org.openqa.selenium.By;

import io.qameta.allure.Step;

/**
 * Page Object cho màn hình Đăng nhập ({@code /login}).
 * <p>
 * ĐÂY LÀ PAGE MẪU CHUẨN — mọi Page mới phải theo đúng bố cục:
 * PATH → Locators (private static final By) → Hành động (@Step, trả về Page) →
 * Trạng thái (getter/boolean).
 */
public class LoginPage extends BasePage {

    public static final String PATH = "/login";

    // ------------------------------------------------------------ locators
    private static final By EMAIL_INPUT = By.id("login-email");
    private static final By PASSWORD_INPUT = By.id("login-password");
    private static final By SUBMIT_BUTTON = By.cssSelector("form button[type='submit']");
    private static final By EMAIL_ERROR = By.cssSelector("#login-email ~ p");
    private static final By PASSWORD_ERROR = By.cssSelector("#login-password ~ p");
    private static final By FORM_ERROR = By.cssSelector("svg.lucide-circle-alert + span");
    private static final By HEADING = By.tagName("h1");
    private static final By SIGNUP_LINK = By.cssSelector("a[href='/signup']");

    // ------------------------------------------------------------ hành động

    @Step("Mở trang Đăng nhập")
    public LoginPage open() {
        openPath(PATH);
        find(EMAIL_INPUT);
        return this;
    }

    @Step("Nhập email: {email}")
    public LoginPage enterEmail(String email) {
        type(EMAIL_INPUT, email);
        return this;
    }

    @Step("Nhập mật khẩu")
    public LoginPage enterPassword(String password) {
        type(PASSWORD_INPUT, password);
        return this;
    }

    @Step("Bấm nút Đăng nhập")
    public LoginPage clickSubmit() {
        click(SUBMIT_BUTTON);
        return this;
    }

    /** Luồng đăng nhập thành công: trả về Dashboard. */
    @Step("Đăng nhập với tài khoản: {email}")
    public DashboardPage loginAs(String email, String password) {
        submitCredentials(email, password);
        return new DashboardPage();
    }

    /** Luồng đăng nhập (có thể thất bại): vẫn ở lại trang Login. */
    @Step("Gửi form đăng nhập với email: {email}")
    public LoginPage submitCredentials(String email, String password) {
        return enterEmail(email).enterPassword(password).clickSubmit();
    }

    // ------------------------------------------------------------ trạng thái

    @Override
    public boolean isLoaded() {
        return waitForUrlContains(PATH) && isVisibleWithin(EMAIL_INPUT);
    }

    public String getHeadingText() {
        return getText(HEADING);
    }

    /**
     * Thông báo lỗi chung phía trên form (ví dụ sai mật khẩu). Rỗng nếu không có.
     */
    public String getFormErrorMessage() {
        waitForValidationFeedback();
        return isDisplayed(FORM_ERROR) ? getText(FORM_ERROR) : "";
    }

    /** Thông báo lỗi dưới ô email. Rỗng nếu không có. */
    public String getEmailErrorMessage() {
        waitForValidationFeedback();
        return isDisplayed(EMAIL_ERROR) ? getText(EMAIL_ERROR) : "";
    }

    /** Thông báo lỗi dưới ô mật khẩu. Rỗng nếu không có. */
    public String getPasswordErrorMessage() {
        waitForValidationFeedback();
        return isDisplayed(PASSWORD_ERROR) ? getText(PASSWORD_ERROR) : "";
    }

    /**
     * Chờ đến khi xuất hiện BẤT KỲ thông báo lỗi nào (lỗi chung hoặc lỗi theo ô).
     */
    private void waitForValidationFeedback() {
        isAnyVisibleWithin(FORM_ERROR, EMAIL_ERROR, PASSWORD_ERROR);
    }

    public boolean isSignupLinkDisplayed() {
        return isDisplayed(SIGNUP_LINK);
    }
}
