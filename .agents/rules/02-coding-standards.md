---
trigger: always_on
---

# Rule 02 — Chuẩn viết code

## Ngôn ngữ
- Định danh (class, method, biến, file): **tiếng Anh**.
- Comment, Javadoc, `@Step`, `@Description`, message của assert, log: **tiếng Việt**.

## Chờ phần tử (Synchronization)
- Implicit wait luôn = 0 (đã set trong `DriverFactory`).
- Chỉ dùng explicit wait qua `BasePage` (`find`, `click`, `type`, `isVisibleWithin`, `isAnyVisibleWithin`, `waitForUrlContains`) hoặc `WaitUtils`.
- **CẤM `Thread.sleep`.** Cần chờ điều kiện mới → thêm hàm vào `WaitUtils`/`BasePage`.

## Assert
- Chỉ dùng TestNG: `org.testng.Assert` (hard) và `org.testng.asserts.SoftAssert` (nhiều kiểm tra trong một test, nhớ gọi `assertAll()`).
- Mọi assert **phải có message tiếng Việt** mô tả kỳ vọng, có `caseId` nếu là test data-driven.
- Assert chỉ nằm trong class Test, ở phần `// Assert` cuối method.

## Page Object
- Locator: `private static final By TEN_PHAN_TU = ...;` đặt đầu class, nhóm theo comment `// locators`.
- Hàm hành động public có `@Step("mô tả tiếng Việt")`.
- Hàm trạng thái trả `String`/`boolean`/`int`, không ném lỗi khi phần tử không có (trả `""`/`false`).
- Mỗi Page override `isLoaded()`.

## Log
- Dùng `log` (Log4j2) có sẵn trong `BasePage`/`BaseTest`. **CẤM `System.out.println`, `printStackTrace()`.**
- Không log mật khẩu. `toString()` của model data không được in mật khẩu.

## Chung
- Không để import thừa, code bị comment, `TODO` không có link issue.
- Hằng số: `SCREAMING_SNAKE_CASE`, `private static final`.
- Class tiện ích: `final` + constructor `private`.
