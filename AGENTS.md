# Dating Journal Automation — Luật bắt buộc cho MỌI Agent

> [!IMPORTANT]
> File này được nạp tự động cho mọi agent, mọi đoạn chat. **Vi phạm bất kỳ mục "BẮT BUỘC" hay "CẤM" nào đều được coi là chưa hoàn thành.**
> Ứng dụng được test: https://dating-journal-app.vercel.app · Repo: https://github.com/antran1496/Dating-Journal-Automation

## 0. Ngôn ngữ (BẮT BUỘC)

- Agent **luôn trả lời người dùng bằng tiếng Việt**.
- Tên class, method, biến, hằng số, file, package: **tiếng Anh**.
- Comment, Javadoc, `@Description`, `@Step`, thông báo assert, log, tài liệu `.md`, rules, skills: **tiếng Việt**.
- Commit message: tiền tố Conventional Commits tiếng Anh (`feat`, `fix`...), phần mô tả **tiếng Việt** (xem skill `auto-commit`).

## 1. Tech stack cố định (KHÔNG tự ý thêm thư viện)

| Thành phần | Thư viện |
|---|---|
| Ngôn ngữ / Build | Java 17 · Maven |
| Điều khiển trình duyệt | Selenium 4 (Selenium Manager tự tải driver) |
| Test runner + Assert | TestNG (`Assert`, `SoftAssert`, `@DataProvider`) |
| Đọc test data | Jackson (`jackson-databind`) |
| Log | Log4j2 |
| Báo cáo | Allure (`allure-testng` + `aspectjweaver` agent theo hướng dẫn chính thức) |

- **CẤM** thêm dependency mới vào `pom.xml` khi chưa được người dùng đồng ý rõ ràng. Ưu tiên Java chuẩn (JDK) trước.
- Cấu hình `.env` được đọc bằng `java.util.Properties` trong `ConfigManager` — không dùng thư viện dotenv.

## 2. Kiến trúc & cấu trúc thư mục (ĐÃ KHÓA)

```text
dating/
├── AGENTS.md                      ← file này
├── README.md · pom.xml · .gitignore · .env.example   (.env: KHÔNG commit)
├── .agents/
│   ├── rules/                     ← luật chi tiết (always_on)
│   └── skills/                    ← quy trình từng việc
├── scripts/verify-structure.sh    ← kiểm tra tuân thủ, BẮT BUỘC chạy trước khi báo xong
└── src/
    ├── main/java/com/datingjournal/
    │   ├── config/        AppConfig, ConfigManager, ConfigKey, Environment
    │   ├── core/          DriverFactory, DriverManager (ThreadLocal), BrowserType
    │   ├── pages/         BasePage + <Tên>Page        (1 màn hình = 1 Page)
    │   ├── components/    thành phần UI dùng chung (header, modal...) — extends BasePage
    │   ├── data/          JsonDataReader + models/<Tên>Data (record)
    │   └── utils/         WaitUtils, ScreenshotUtils
    ├── main/resources/    log4j2.xml
    └── test/
        ├── java/com/datingjournal/
        │   ├── base/          BaseTest, TestGroups
        │   ├── listeners/     TestListener, RetryAnalyzer, RetryTransformer
        │   ├── dataproviders/ <Tính năng>DataProvider
        │   └── tests/<tính năng>/  <Tên>Test
        └── resources/
            ├── config/    prod.properties · staging.properties · dev.properties
            ├── testdata/  <tính năng>.json
            ├── suites/    smoke.xml · regression.xml
            └── allure.properties
```

- Hướng phụ thuộc **một chiều**: `tests → pages/components → core/utils/config` và `tests → dataproviders → data`.
- **CẤM** tạo thư mục/package cấp cao mới, đổi tên hay di chuyển thư mục trên khi chưa hỏi người dùng.
- **Mẫu chuẩn phải sao chép theo:** [LoginPage.java](src/main/java/com/datingjournal/pages/LoginPage.java) cho Page, [LoginTest.java](src/test/java/com/datingjournal/tests/auth/LoginTest.java) cho Test, [login.json](src/test/resources/testdata/login.json) + [LoginDataProvider.java](src/test/java/com/datingjournal/dataproviders/LoginDataProvider.java) cho dữ liệu.

## 3. Danh sách CẤM (tự động kiểm tra bởi `scripts/verify-structure.sh`)

1. `Thread.sleep` ở bất kỳ đâu → dùng `WaitUtils` / hàm chờ trong `BasePage`.
2. `driver.findElement`, `By.`, `WebDriver` trong `src/test/**` → chỉ gọi Page Object.
3. `Assert` / `SoftAssert` trong `pages/` hoặc `components/` → Page chỉ trả trạng thái.
4. Hardcode URL, email, mật khẩu trong code/JSON → dùng `AppConfig` và placeholder `${KEY}`.
5. Implicit wait khác 0 · XPath tuyệt đối (`/html/body/...`) · locator dựa vào class Tailwind động.
6. Test phụ thuộc thứ tự (`dependsOnMethods`, `priority` để nối luồng) · biến static chia sẻ trạng thái giữa test.
7. `@Test(enabled = false)` mà không có comment lý do + link issue.
8. Commit file `.env`, `target/`, báo cáo, hoặc tự ý commit/push khi người dùng chưa yêu cầu.

## 4. Quy trình bắt buộc cho mọi yêu cầu viết/sửa test

1. **Đọc skill liên quan TRƯỚC khi code** (bảng mục 5).
2. Khảo sát UI thật (Playwright MCP / trình duyệt) để lấy locator — không đoán.
3. Viết theo đúng mẫu chuẩn ở mục 2.
4. Chạy `bash scripts/verify-structure.sh` → phải PASS.
5. Chạy `mvn -q -B clean test-compile` → phải PASS; chạy test liên quan → phải PASS.
6. Báo cáo bằng tiếng Việt: file đã thêm/sửa, kết quả chạy. **Không tự commit** — chờ lệnh.

## 5. Skills (đọc trước khi làm việc tương ứng)

| Skill | Khi nào dùng |
|---|---|
| [new-feature-test](.agents/skills/new-feature-test/SKILL.md) | Tự động hóa một màn hình/tính năng mới (quy trình tổng) |
| [page-object-model](.agents/skills/page-object-model/SKILL.md) | Tạo/sửa Page, Component |
| [locator-strategy](.agents/skills/locator-strategy/SKILL.md) | Chọn locator |
| [data-driven-testing](.agents/skills/data-driven-testing/SKILL.md) | Thêm test data JSON, model, DataProvider |
| [test-case-writing](.agents/skills/test-case-writing/SKILL.md) | Viết class/method test, group, Allure |
| [naming-conventions](.agents/skills/naming-conventions/SKILL.md) | Đặt tên mọi thứ |
| [env-config](.agents/skills/env-config/SKILL.md) | Biến môi trường, thêm key cấu hình, thêm môi trường |
| [run-and-report](.agents/skills/run-and-report/SKILL.md) | Chạy test, đọc log, mở báo cáo Allure, debug fail |
| [auto-commit](.agents/skills/auto-commit/SKILL.md) | Khi người dùng yêu cầu commit / push code |
