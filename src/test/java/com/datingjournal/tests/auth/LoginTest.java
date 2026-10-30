package com.datingjournal.tests.auth;

import com.datingjournal.base.BaseTest;
import com.datingjournal.base.TestGroups;
import com.datingjournal.data.models.LoginData;
import com.datingjournal.dataproviders.LoginDataProvider;
import com.datingjournal.pages.DashboardPage;
import com.datingjournal.pages.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * Bộ test màn hình Đăng nhập.
 * <p>
 * ĐÂY LÀ CLASS TEST MẪU CHUẨN — mọi class test mới phải theo đúng bố cục:
 * kế thừa BaseTest → gọi Page Object (không đụng driver) → dữ liệu từ DataProvider/AppConfig → assert ở cuối (AAA).
 */
@Epic("Xác thực")
@Feature("Đăng nhập")
public class LoginTest extends BaseTest {

    @Test(groups = {TestGroups.SMOKE, TestGroups.REGRESSION, TestGroups.AUTH},
            dataProvider = LoginDataProvider.VALID_LOGIN, dataProviderClass = LoginDataProvider.class)
    @Severity(SeverityLevel.BLOCKER)
    @Description("TC_LOGIN_01: Đăng nhập bằng tài khoản hợp lệ → chuyển tới Trang chủ và hiển thị lời chào")
    public void loginWithValidCredentialsShouldOpenDashboard(LoginData data) {
        // Arrange
        LoginPage loginPage = new LoginPage().open();

        // Act
        DashboardPage dashboardPage = loginPage.loginAs(data.email(), data.password());

        // Assert
        Assert.assertTrue(dashboardPage.isLoaded(),
                "Trang chủ phải được tải sau khi đăng nhập thành công");
        Assert.assertTrue(dashboardPage.getCurrentUrl().endsWith(DashboardPage.PATH),
                "URL phải kết thúc bằng " + DashboardPage.PATH + " nhưng là: " + dashboardPage.getCurrentUrl());
        Assert.assertTrue(dashboardPage.getGreetingText().startsWith("Chào"),
                "Lời chào không đúng: " + dashboardPage.getGreetingText());
        Assert.assertTrue(dashboardPage.navigation().isLoaded(),
                "Thanh điều hướng (Desktop Header hoặc Mobile Bottom Bar) phải hiển thị");
    }

    @Test(groups = {TestGroups.REGRESSION, TestGroups.AUTH},
            dataProvider = LoginDataProvider.INVALID_LOGIN, dataProviderClass = LoginDataProvider.class)
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC_LOGIN_02: Đăng nhập với dữ liệu không hợp lệ → hiển thị đúng thông báo lỗi và ở lại trang Đăng nhập")
    public void loginWithInvalidDataShouldShowError(LoginData data) {
        // Arrange
        LoginPage loginPage = new LoginPage().open();

        // Act
        loginPage.submitCredentials(data.email(), data.password());

        // Assert
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(loginPage.getFormErrorMessage(), data.expectedFormError(),
                "[" + data.caseId() + "] Lỗi chung");
        softAssert.assertEquals(loginPage.getEmailErrorMessage(), data.expectedEmailError(),
                "[" + data.caseId() + "] Lỗi ô email");
        softAssert.assertEquals(loginPage.getPasswordErrorMessage(), data.expectedPasswordError(),
                "[" + data.caseId() + "] Lỗi ô mật khẩu");
        softAssert.assertTrue(loginPage.getCurrentUrl().contains(LoginPage.PATH),
                "[" + data.caseId() + "] Phải ở lại trang Đăng nhập");
        softAssert.assertAll();
    }

    @Test(groups = {TestGroups.REGRESSION, TestGroups.AUTH})
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC_LOGIN_03: Truy cập Trang chủ khi chưa đăng nhập → bị chuyển về trang Đăng nhập")
    public void accessDashboardWithoutLoginShouldRedirectToLogin() {
        // Arrange + Act
        new DashboardPage().open();
        LoginPage loginPage = new LoginPage();

        // Assert
        Assert.assertTrue(loginPage.isLoaded(),
                "Người dùng chưa đăng nhập phải bị chuyển về " + LoginPage.PATH);
    }
}
