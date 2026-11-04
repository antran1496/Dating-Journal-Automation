---
name: test-case-writing
description: Dùng khi viết hoặc sửa class/method test TestNG trong src/test/java/com/datingjournal/tests. Quy định cấu trúc AAA, tên test, group smoke/regression, annotation Allure, cách assert và tính độc lập của test.
---

# Skill: Viết Test Case

Mẫu chuẩn: [LoginTest.java](../../../src/test/java/com/datingjournal/tests/auth/LoginTest.java)

## 1. Template

```java
package com.datingjournal.tests.<tinhnang>;

@Epic("<Nhóm nghiệp vụ>")          // ví dụ "Xác thực", "Hẹn hò"
@Feature("<Tính năng>")             // ví dụ "Đăng nhập"
public class XxxTest extends BaseTest {

    @Test(groups = {TestGroups.REGRESSION, TestGroups.XXX},
          dataProvider = XxxDataProvider.VALID_XXX, dataProviderClass = XxxDataProvider.class)
    @Severity(SeverityLevel.CRITICAL)
    @Description("TC_XXX_01: <Hành động> → <Kết quả mong đợi>")
    public void actionWithConditionShouldResult(XxxData data) {
        // Arrange
        XxxPage page = new XxxPage().open();

        // Act
        page.doSomething(data.value());

        // Assert
        Assert.assertEquals(page.getMessage(), data.expectedMessage(), "[" + data.caseId() + "] Thông báo");
    }
}
```

## 2. Quy tắc

| Mục | Quy tắc |
|---|---|
| Kế thừa | Luôn `extends BaseTest` (tự mở/đóng trình duyệt, chụp màn hình khi fail) |
| Vị trí | `tests/<tính năng>/` — ví dụ `tests/auth`, `tests/dates`, `tests/settings` |
| Cấu trúc | 3 khối comment `// Arrange` · `// Act` · `// Assert` |
| Group | Lấy từ `TestGroups`; mọi test có `REGRESSION` + group tính năng; luồng sống còn thêm `SMOKE`. Group mới → thêm hằng vào `TestGroups` |
| Allure | `@Epic`, `@Feature` ở class; `@Severity`, `@Description("TC_ID: ...")` ở method |
| Assert | TestNG `Assert` / `SoftAssert` (gọi `assertAll()`), message tiếng Việt, có `caseId` |
| Dữ liệu | Từ DataProvider hoặc `AppConfig` — không hardcode |
| Độc lập | Không `dependsOnMethods`, không chia sẻ state static; test cần đăng nhập thì tự đăng nhập trong Arrange (`new LoginPage().open().loginAs(AppConfig.testEmail(), AppConfig.testPassword())`) |
| Đăng ký | Test tự được suite nhặt qua package `com.datingjournal.tests.*` + group — không cần sửa XML |

## 3. Mã Test Case
- Định dạng `TC_<MODULE>_<NN>` (method) và `TC_<MODULE>_<NN>_<MM>` (từng dòng data). Module: `LOGIN`, `SIGNUP`, `DATE`, `DASHBOARD`...

## 4. Kiểm tra
```bash
bash scripts/verify-structure.sh
mvn -B clean test -Dsuite=regression -Dheadless=true
```
