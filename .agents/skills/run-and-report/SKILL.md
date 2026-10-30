---
name: run-and-report
description: Dùng khi cần chạy test (theo suite, môi trường, trình duyệt, headless), xem log, mở báo cáo Allure hoặc điều tra nguyên nhân test bị fail.
---

# Skill: Chạy test & Báo cáo

## 1. Lệnh chạy

| Mục đích | Lệnh |
|---|---|
| Biên dịch (không chạy) | `mvn -q -B clean test-compile` |
| Smoke | `mvn -B clean test -Dsuite=smoke` |
| Regression (mặc định) | `mvn -B clean test` hoặc `-Dsuite=regression` |
| Chạy ẩn | thêm `-Dheadless=true` |
| Chạy giao diện Mobile (`390x844`) | thêm `-Dviewport=mobile` (mặc định `desktop` `1440x900`) |
| Đổi trình duyệt | thêm `-Dbrowser=firefox` |
| Đổi môi trường | thêm `-Denv=staging` hoặc `-Dbase.url=https://...` |
| Một class | `mvn -B test -Dsuite=regression -Dtest=LoginTest` |
| Một method | `mvn -B test -Dsuite=regression -Dtest=LoginTest#loginWithInvalidDataShouldShowError` |

> Agent: lệnh Maven cần mạng để tải dependency lần đầu. Nếu sandbox chặn mạng (lỗi `Could not transfer artifact`), chạy lại với `BypassSandbox: true`.

## 2. Kết quả & báo cáo

| Thứ | Vị trí |
|---|---|
| Log chi tiết (DEBUG) | `target/logs/automation.log` |
| Screenshot khi fail | `target/screenshots/<method>_FAILED_<thời gian>.png` (cũng đính kèm trong Allure) |
| Kết quả Allure thô | `target/allure-results/` |
| Báo cáo TestNG | `target/surefire-reports/` |

Mở báo cáo Allure:
```bash
mvn allure:serve          # dựng và mở trình duyệt
mvn allure:report         # chỉ dựng ra target/site/allure-maven-plugin
```

## 3. Quy trình điều tra test fail
1. Đọc dòng `✘ FAIL` trong console/log → message assert tiếng Việt cho biết kỳ vọng nào sai.
2. Xem screenshot tương ứng trong `target/screenshots/`.
3. Mở lại trang bằng Playwright MCP, tái hiện bước, so locator/text thông báo với UI hiện tại.
4. Phân loại: **lỗi ứng dụng** (báo người dùng, không sửa test cho khớp bug) · **UI đổi** (cập nhật locator/test data) · **flaky do chờ** (bổ sung điều kiện chờ, KHÔNG thêm `Thread.sleep`, không tăng `retry.count` để che lỗi).
