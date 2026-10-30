# Dating Journal Automation

Bộ test UI automation cho [Dating Journal App](https://dating-journal-app.vercel.app), được xây dựng theo **Page Object Model + Data-Driven**.

## Tech stack
Java 17 · Maven · Selenium 4 · TestNG · Jackson · Log4j2 · Allure

## Bắt đầu nhanh

```bash
# 1. Tạo file biến môi trường và điền tài khoản test
cp .env.example .env

# 2. Kiểm tra cấu trúc + biên dịch
bash scripts/verify-structure.sh
mvn -q -B clean test-compile

# 3. Chạy test
mvn -B clean test -Dsuite=smoke                     # smoke (Desktop 1440x900)
mvn -B clean test                                   # regression (mặc định)
mvn -B clean test -Dviewport=mobile                 # chạy giao diện Mobile (390x844)
mvn -B clean test -Dheadless=true -Dbrowser=firefox # tùy chọn

# 4. Xem báo cáo
mvn allure:serve
```

## Cấu hình
Thứ tự ưu tiên: `-Dkey=...` > biến môi trường OS > `.env` > `src/test/resources/config/<env>.properties`.
Chi tiết các key: [.agents/skills/env-config/SKILL.md](.agents/skills/env-config/SKILL.md).

## Cấu trúc & quy tắc
Toàn bộ cấu trúc thư mục, luật và quy trình cho người lẫn AI agent nằm trong [AGENTS.md](AGENTS.md) và `.agents/`.

| Mẫu chuẩn | File |
|---|---|
| Page Object | [LoginPage.java](src/main/java/com/datingjournal/pages/LoginPage.java) |
| Test class | [LoginTest.java](src/test/java/com/datingjournal/tests/auth/LoginTest.java) |
| Test data | [login.json](src/test/resources/testdata/login.json) |

## Test case hiện có

| Mã | Mô tả | Group |
|---|---|---|
| TC_LOGIN_01 | Đăng nhập hợp lệ → vào Trang chủ, hiện lời chào | smoke, regression |
| TC_LOGIN_02_01..05 | Sai mật khẩu, email không tồn tại, bỏ trống email/mật khẩu/cả hai → đúng thông báo lỗi | regression |
| TC_LOGIN_03 | Vào `/dashboard` khi chưa đăng nhập → bị chuyển về `/login` | regression |
