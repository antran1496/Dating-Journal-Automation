---
trigger: always_on
---

# Rule 01 — Kiến trúc Framework

## Các lớp và trách nhiệm

| Lớp | Package | Được làm | KHÔNG được làm |
|---|---|---|---|
| Config | `config` | Đọc cấu hình theo thứ tự `-D` > OS env > `.env` > `config/{env}.properties` | Chứa logic test |
| Core | `core` | Tạo/giữ/đóng WebDriver (ThreadLocal) | Biết về Page cụ thể |
| Utils | `utils` | Hàm tiện ích chung (wait, screenshot) | Chứa locator của trang |
| Page / Component | `pages`, `components` | Locator + thao tác + trả trạng thái | Assert, đọc test data, tạo driver |
| Data | `data`, `data/models` | Đọc JSON, model `record` bất biến | Gọi Selenium |
| DataProvider | `dataproviders` (test) | Nối JSON ↔ TestNG `@DataProvider` | Chứa logic UI |
| Test | `tests/<tính năng>` (test) | Arrange–Act–Assert bằng Page Object | Gọi `driver`/`By`, hardcode data |
| Base / Listener | `base`, `listeners` (test) | Vòng đời driver, chụp màn hình, log, retry | Logic nghiệp vụ |

## Nguyên tắc

- **Một màn hình = một Page class**; phần UI lặp lại trên nhiều màn hình = Component, được Page nhúng qua composition (ví dụ `DashboardPage.navigation()`).
- Hàm hành động trong Page trả về **Page đích** (fluent): ở lại trang → `return this`; chuyển trang → `return new XxxPage()`.
- Mỗi test method có **phiên trình duyệt riêng** (BaseTest `@BeforeMethod` / `@AfterMethod`) → test độc lập, chạy song song được.
- Driver chỉ lấy qua `DriverManager.getDriver()`; trong Page đã có sẵn field `driver` từ `BasePage`.
- Mọi giá trị cấu hình lấy qua `AppConfig` (typed). Thêm key mới phải thêm vào `ConfigKey` + `AppConfig` + `.env.example`.
