package com.datingjournal.core;

import com.datingjournal.config.AppConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;

import java.time.Duration;
import java.util.Map;

/**
 * Factory khởi tạo WebDriver theo cấu hình. Driver binary được Selenium Manager tự tải.
 * <p>
 * KHÔNG dùng implicit wait (để 0) — mọi thao tác chờ phải qua explicit wait trong BasePage/WaitUtils.
 */
public final class DriverFactory {

    private static final Logger LOG = LogManager.getLogger(DriverFactory.class);

    private DriverFactory() {
    }

    public static WebDriver createDriver() {
        BrowserType browser = BrowserType.from(AppConfig.browser());
        boolean isHeadless = AppConfig.isHeadless();
        LOG.info("Khởi tạo trình duyệt: {} (headless={})", browser, isHeadless);

        WebDriver driver = switch (browser) {
            case CHROME -> new ChromeDriver(chromeOptions(isHeadless));
            case FIREFOX -> new FirefoxDriver(firefoxOptions(isHeadless));
            case EDGE -> new EdgeDriver(edgeOptions(isHeadless));
            case SAFARI -> new SafariDriver();
        };

        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(AppConfig.pageLoadTimeoutSeconds()));
        driver.manage().window().setSize(new Dimension(AppConfig.windowWidth(), AppConfig.windowHeight()));
        return driver;
    }

    private static ChromeOptions chromeOptions(boolean isHeadless) {
        ChromeOptions options = new ChromeOptions();
        if (isHeadless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--disable-notifications", "--no-sandbox", "--disable-dev-shm-usage", "--lang=vi-VN");
        options.setExperimentalOption("prefs", Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "profile.password_manager_leak_detection", false));
        return options;
    }

    private static FirefoxOptions firefoxOptions(boolean isHeadless) {
        FirefoxOptions options = new FirefoxOptions();
        if (isHeadless) {
            options.addArguments("-headless");
        }
        return options;
    }

    private static EdgeOptions edgeOptions(boolean isHeadless) {
        EdgeOptions options = new EdgeOptions();
        if (isHeadless) {
            options.addArguments("--headless=new");
        }
        return options;
    }
}
