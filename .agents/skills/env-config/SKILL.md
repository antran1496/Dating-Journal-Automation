---
name: env-config
description: Dùng khi thiết lập biến môi trường, file .env, thêm key cấu hình mới, thêm môi trường chạy mới (dev/staging/prod) hoặc ghi đè cấu hình khi chạy Maven/CI.
---

# Skill: Cấu hình & Biến môi trường

Mã nguồn: [ConfigManager.java](../../../src/main/java/com/datingjournal/config/ConfigManager.java) · [AppConfig.java](../../../src/main/java/com/datingjournal/config/AppConfig.java) · [ConfigKey.java](../../../src/main/java/com/datingjournal/config/ConfigKey.java)

## 1. Thứ tự ưu tiên (cao → thấp)

1. System property: `mvn test -Dbase.url=https://...`
2. Biến môi trường OS / CI: `BASE_URL=https://...`
3. File `.env` ở gốc dự án (đọc bằng `java.util.Properties`, cú pháp `KEY=VALUE`)
4. `src/test/resources/config/<env>.properties` — `<env>` lấy từ `ENV` (mặc định `prod`)

## 2. Thiết lập lần đầu
```bash
cp .env.example .env     # rồi điền TEST_EMAIL, TEST_PASSWORD
```
- `.env` nằm trong `.gitignore` — **CẤM commit**. `.env.example` được commit, chỉ chứa key + giá trị rỗng/mặc định.

## 3. Danh sách key

| Properties | Env | Ý nghĩa | Mặc định |
|---|---|---|---|
| `env` | `ENV` | Chọn file properties: `prod` / `staging` / `dev` | `prod` |
| `base.url` | `BASE_URL` | URL ứng dụng | theo properties |
| `browser` | `BROWSER` | `chrome` / `firefox` / `edge` / `safari` | `chrome` |
| `headless` | `HEADLESS` | Chạy ẩn trình duyệt | `false` |
| `viewport` | `VIEWPORT` | Khung nhìn: `desktop` (`1440x900`) hoặc `mobile` (`390x844`) | `desktop` |
| `timeout.seconds` | `TIMEOUT_SECONDS` | Explicit wait | `15` |
| `page.load.timeout.seconds` | `PAGE_LOAD_TIMEOUT_SECONDS` | Timeout tải trang | `60` |
| `window.width` / `window.height` | `WINDOW_WIDTH` / `WINDOW_HEIGHT` | Kích thước cửa sổ | `1440x900` |
| `retry.count` | `RETRY_COUNT` | Số lần chạy lại test fail | `0` |
| `test.email` / `test.password` | `TEST_EMAIL` / `TEST_PASSWORD` | Tài khoản test (bắt buộc, chỉ để trong `.env`/CI secret) | — |

## 4. Thêm key mới (đủ 4 bước)
1. Thêm hằng vào `ConfigKey` (dot-case).
2. Thêm getter typed vào `AppConfig` (có Javadoc tiếng Việt).
3. Thêm giá trị không nhạy cảm vào cả 3 file `config/*.properties`; giá trị nhạy cảm chỉ vào `.env`.
4. Thêm dòng vào `.env.example` + bảng trên.

## 5. Thêm môi trường mới
1. Thêm hằng vào enum `Environment` (ví dụ `UAT("uat.properties")`).
2. Tạo `src/test/resources/config/uat.properties` đủ các key như `prod.properties`.
3. Chạy: `mvn test -Denv=uat`.

## 6. Kiểm tra
- Thiếu key bắt buộc → `ConfigManager` ném lỗi nêu rõ tên key và tên biến môi trường.
