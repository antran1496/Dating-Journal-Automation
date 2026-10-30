package com.datingjournal.base;

import com.datingjournal.core.DriverManager;
import com.datingjournal.utils.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Lớp cha của MỌI class test.
 * <p>
 * Mỗi test method có một phiên trình duyệt RIÊNG (mở trước, đóng sau) → test độc lập, chạy song song an toàn.
 * Khi test fail sẽ tự chụp màn hình và đính kèm vào Allure.
 */
public abstract class BaseTest {

    protected final Logger log = LogManager.getLogger(getClass());

    @BeforeMethod(alwaysRun = true)
    public void setUpDriver() {
        DriverManager.initDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownDriver(ITestResult result) {
        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                ScreenshotUtils.capture(result.getMethod().getMethodName() + "_FAILED");
            }
        } finally {
            DriverManager.quitDriver();
        }
    }
}
