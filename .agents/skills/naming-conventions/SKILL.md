---
name: naming-conventions
description: Dùng khi đặt tên class, method, biến, hằng số, locator, file test data, caseId, group, branch trong dự án Dating Journal Automation.
---

# Skill: Quy tắc đặt tên

> Định danh luôn bằng **tiếng Anh**. Comment/mô tả bằng **tiếng Việt**.

## 1. File & Class

| Loại | Quy tắc | Đúng | Sai |
|---|---|---|---|
| Page | `<Màn hình>Page` | `LoginPage`, `DateDetailPage` | `Login`, `PageLogin`, `LoginScreen` |
| Component | `<Tên>` mô tả vai trò | `AppNavigation`, `ConfirmDialog` | `Header1`, `Comp` |
| Test | `<Tính năng>Test` | `LoginTest`, `CreateDateTest` | `TestLogin`, `LoginTests` |
| DataProvider | `<Tính năng>DataProvider` | `LoginDataProvider` | `LoginData_DP` |
| Model data | `<Tính năng>Data` (record) | `LoginData`, `DateEntryData` | `LoginModel`, `LoginDTO` |
| Test data | `kebab-case.json` theo tính năng | `login.json`, `create-date.json` | `LoginData.json`, `data1.json` |
| Package test | chữ thường, số ít/tên tính năng | `tests.auth`, `tests.dates` | `tests.Auth`, `tests.login_tests` |
| Suite | `kebab-case.xml` | `smoke.xml`, `regression.xml` | `Smoke Suite.xml` |

## 2. Code

| Loại | Quy tắc | Ví dụ |
|---|---|---|
| Locator | `SCREAMING_SNAKE_CASE` + hậu tố loại | `EMAIL_INPUT`, `SUBMIT_BUTTON`, `FORM_ERROR`, `SIGNUP_LINK` |
| Hằng số | `SCREAMING_SNAKE_CASE` | `PATH`, `TEST_DATA_DIR` |
| Hành động Page | động từ camelCase | `open()`, `enterEmail()`, `clickSubmit()`, `loginAs()` |
| Trạng thái Page | `getX()` / `isX()` / `hasX()` | `getFormErrorMessage()`, `isLoaded()` |
| Test method | `<hànhĐộng><ĐiềuKiện>Should<KếtQuả>` | `loginWithValidCredentialsShouldOpenDashboard` |
| Biến boolean | `is/has/can/should` | `isHeadless`, `hasError` |
| Key cấu hình | dot-case (properties) ↔ SCREAMING_SNAKE (env) | `base.url` ↔ `BASE_URL` |
| Section JSON / tên DataProvider | camelCase, giống nhau | `validLogin`, `invalidLogin` |
| Group TestNG | chữ thường | `smoke`, `regression`, `auth` |

## 3. Mã test case
- `TC_<MODULE>_<NN>` cho method, `TC_<MODULE>_<NN>_<MM>` cho từng dòng data. Ví dụ `TC_LOGIN_02_03`.

## 4. Git
- Branch: `<loại>/<mô-tả-kebab>` hoặc `<loại>/DJA_<số>-<mô-tả>` — loại: `feat`, `fix`, `refactor`, `test`, `chore`, `docs`.
- Ví dụ: `feat/DJA_12-signup-test`, `fix/login-locator`.
