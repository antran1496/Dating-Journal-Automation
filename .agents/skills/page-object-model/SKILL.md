---
name: page-object-model
description: Dùng khi tạo mới hoặc sửa Page Object / Component trong src/main/java/com/datingjournal/pages hoặc components. Mô tả template chuẩn, cách khai báo locator, hàm hành động fluent, hàm trạng thái và isLoaded().
---

# Skill: Page Object Model

Mẫu chuẩn: [LoginPage.java](../../../src/main/java/com/datingjournal/pages/LoginPage.java) · Component mẫu: [AppNavigation.java](../../../src/main/java/com/datingjournal/components/AppNavigation.java)

## 1. Template Page (sao chép và đổi tên)

```java
package com.datingjournal.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;

/**
 * Page Object cho màn hình <Tên tiếng Việt> ({@code /duong-dan}).
 */
public class XxxPage extends BasePage {

    public static final String PATH = "/duong-dan";

    // ------------------------------------------------------------ locators
    private static final By TITLE_INPUT = By.id("...");
    private static final By SAVE_BUTTON = By.cssSelector("form button[type='submit']");

    // ------------------------------------------------------------ hành động
    @Step("Mở trang <Tên>")
    public XxxPage open() {
        openPath(PATH);
        return this;
    }

    @Step("Nhập tiêu đề: {title}")
    public XxxPage enterTitle(String title) {
        type(TITLE_INPUT, title);
        return this;
    }

    @Step("Bấm Lưu")
    public YyyPage clickSave() {          // chuyển trang → trả về Page đích
        click(SAVE_BUTTON);
        return new YyyPage();
    }

    // ------------------------------------------------------------ trạng thái
    @Override
    public boolean isLoaded() {
        return waitForUrlContains(PATH) && isVisibleWithin(TITLE_INPUT);
    }

    public String getTitleError() {
        return isDisplayed(...) ? getText(...) : "";
    }
}
```

## 2. Quy tắc

| Mục | Quy tắc |
|---|---|
| Kế thừa | Page & Component luôn `extends BasePage` |
| Constructor | Không cần — `BasePage` đã lấy driver. Không truyền driver qua tham số |
| Locator | `private static final By`, tên `SCREAMING_SNAKE_CASE` + hậu tố loại (`_INPUT`, `_BUTTON`, `_LINK`, `_ERROR`, `_HEADING`, `_ITEM`) |
| Hành động | public, động từ (`enterX`, `clickX`, `selectX`, `open`), có `@Step` tiếng Việt, trả về `this` hoặc Page đích |
| Luồng gộp | Gộp nhiều bước thành 1 hàm nghiệp vụ (`loginAs`, `submitCredentials`) — vẫn gọi các hàm nhỏ bên trong |
| Trạng thái | `getX()` trả `String`, `isX()` trả `boolean`; không có thì trả `""`/`false`, KHÔNG ném lỗi |
| Assert | **CẤM** trong Page |
| Driver | **CẤM** `driver.findElement` — dùng `find/findAll/click/type/getText/isDisplayed/...` của BasePage |
| Chờ | Đã có sẵn trong hàm BasePage. Cần kiểu chờ mới → thêm vào `BasePage`/`WaitUtils`, không viết trong Page |

## 3. Component

- Dùng cho UI xuất hiện ở nhiều trang (header, bottom nav, modal xác nhận, toast).
- Đặt ở `components/`, Page nhúng bằng field `private final XxxComponent xxx = new XxxComponent();` + getter.
- Component không có `PATH`, không có `open()`.

## 4. Kiểm tra sau khi viết
1. `bash scripts/verify-structure.sh`
2. `mvn -q -B clean test-compile`
