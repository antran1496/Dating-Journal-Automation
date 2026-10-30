---
name: auto-commit
description: Kích hoạt khi người dùng yêu cầu "lưu code", "commit", "đẩy code", "push git", "auto commit" hoặc gọi /auto-commit. Tự kiểm tra tuân thủ framework, AI review diff, tạo commit message tiếng Anh chuẩn Conventional Commits (chỉ thêm icon 🐛 cho fix/bug) và push lên GitHub (antran1496/Dating-Journal-Automation).
---

# Skill: Auto-Commit & Push

> [!IMPORTANT]
> **QUY TẮC TỐI THƯỢNG:** KHÔNG BAO GIỜ tự commit hay push khi người dùng CHƯA YÊU CẦU RÕ RÀNG. Nhiệm vụ mặc định của agent là viết code, báo cáo, rồi **CHỜ LỆNH**.

Remote: `https://github.com/antran1496/Dating-Journal-Automation.git`

## Trình tự (làm tuần tự)

### 1. Kiểm tra repo & branch (KHÔNG commit trực tiếp vào `main`)
- Chạy `git branch --show-current` để lấy tên branch hiện tại.
- **QUY TẮC NHÁNH BẮT BUỘC**:
  - **CẤM dev và CẤM commit trực tiếp trên nhánh `main`**.
  - Nếu phát hiện đang ở `main` mà có thay đổi chưa commit: phải tự động tạo và chuyển sang branch mới phù hợp (`git checkout -b <loại>/<mô-tả-ngắn>`, ví dụ `feat/signup-test`, `fix/login-locator`, `docs/update-rules`) TRƯỚC khi stage/commit.
  - Nếu branch có dạng `feat/DJA_12-...` → lấy ticket ID `DJA_12`; nếu không có mã ticket thì bỏ qua `[<TICKET>]`.
- **QUY TẮC MERGE**:
  - **KHÔNG BAO GIỜ tự ý merge vào `main`**.
  - Chỉ khi người dùng yêu cầu rõ ràng ("merge vào main", "gộp nhánh vào main") thì mới thực hiện: `git checkout main && git pull origin main && git merge --no-ff <branch>` (và push `main` nếu được yêu cầu).

### 2. Kiểm tra tuân thủ (BẮT BUỘC, fail → DỪNG)
```bash
bash scripts/verify-structure.sh
mvn -q -B clean test-compile
```

### 3. Stage
- `git add -A` rồi `git status --short`.
- Nếu thấy `.env`, `target/`, `allure-results/`, `*.log`, file IDE trong staging → `git reset <file>` và kiểm tra lại `.gitignore`.
- Không có gì để commit → báo người dùng và DỪNG.

### 4. AI review diff — `git diff --cached`
| Mức | Dấu hiệu | Xử lý |
|---|---|---|
| 🚨 Nghiêm trọng | Lộ mật khẩu/token/email thật trong code hoặc JSON, `.env` bị stage, code không biên dịch | **DỪNG**, `git reset`, báo lỗi |
| 🚨 Vi phạm kiến trúc | `Thread.sleep`, `findElement`/`By` trong test, assert trong Page, thư viện mới chưa được duyệt | **DỪNG**, báo vi phạm kèm vị trí |
| ⚠️ Cảnh báo | `System.out`, code comment bỏ dở, `TODO` không link issue, `@Test(enabled=false)` không lý do, assert thiếu message | Cảnh báo, hỏi người dùng có tiếp tục không |
| ✅ Sạch | — | Sang bước 5 |

### 5. Commit message
- **Ngôn ngữ**: BẮT BUỘC viết hoàn toàn bằng **tiếng Anh** theo chuẩn Conventional Commits.
- **Quy tắc Icon (Emoji)**: **CHỈ thêm icon `🐛` đối với bug (`fix`)**. Tất cả các loại khác (`feat`, `test`, `refactor`, `docs`, `chore`) **KHÔNG dùng icon/emoji**.
- **Định dạng**:
  - Đối với `fix` (bug): `🐛 [<TICKET>] fix(<scope>): <english description>` (bỏ `[<TICKET>]` nếu không có).
  - Đối với các loại khác: `[<TICKET>] <type>(<scope>): <english description>` (bỏ `[<TICKET>]` nếu không có).
- `scope` = tính năng/lớp: `auth`, `dates`, `pages`, `core`, `config`, `data`, `agents`, `deps`.

| Icon | type | Khi nào | Ví dụ |
|---|---|---|---|
| *(không dùng)* | `feat` | Thêm test case / Page / tính năng framework mới | `feat(auth): add data-driven login test suite` |
| *(không dùng)* | `test` | Bổ sung/sửa test data, kịch bản trong test có sẵn | `test(auth): add empty credentials test cases` |
| 🐛 | `fix` | Sửa lỗi (bug), sửa test fail, sửa locator, sửa lỗi framework | `🐛 fix(config): resolve static init order in ConfigManager` |
| *(không dùng)* | `refactor` | Tái cấu trúc không đổi hành vi | `refactor(pages): extract responsive wait helper` |
| *(không dùng)* | `docs` | README, rules, skills | `docs(agents): enforce english commit message rule` |
| *(không dùng)* | `chore` | pom.xml, cấu hình, suite, .gitignore | `chore(deps): configure surefire aspectj agent` |

Ví dụ có Ticket ID:
- `feat(dates): [DJA_12] add create date page object` hoặc `[DJA_12] feat(dates): add create date page object`
- `🐛 [DJA_12] fix(pages): update save date button locator`

### 6. Commit
- `git commit -m "<message>"` (dùng nháy kép).

### 7. Push (chỉ khi người dùng nói "đẩy/push")
- `git push -u origin <branch>` — chạy với **`BypassSandbox: true`** (cần mạng).
- Push lỗi xác thực → hướng dẫn người dùng cấu hình credential/SSH, không tự sửa cấu hình git global.

### 8. Tổng kết (tiếng Việt)
- Danh sách file đã commit, commit message, hash (`git log -1 --oneline`), trạng thái push.
