package com.datingjournal.pages;

import com.datingjournal.components.AppNavigation;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

/**
 * Page Object cho Trang chủ sau đăng nhập ({@code /dashboard}).
 */
public class DashboardPage extends BasePage {

    public static final String PATH = "/dashboard";

    private static final By GREETING_HEADING = By.tagName("h1");

    private final AppNavigation navigation = new AppNavigation();

    @Step("Mở trực tiếp Trang chủ")
    public DashboardPage open() {
        openPath(PATH);
        return this;
    }

    public AppNavigation navigation() {
        return navigation;
    }

    @Override
    public boolean isLoaded() {
        return waitForUrlContains(PATH) && isVisibleWithin(GREETING_HEADING);
    }

    /** Lời chào, ví dụ "Chào Em 💗". */
    public String getGreetingText() {
        return getText(GREETING_HEADING);
    }
}
