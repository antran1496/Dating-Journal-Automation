---
name: auto-commit
description: Kích hoạt khi người dùng yêu cầu "lưu code", "commit", "đẩy code", "push git", "auto commit" hoặc gọi /auto-commit. Tự kiểm tra tuân thủ framework, AI review diff, tạo commit message chuẩn emoji + Conventional Commits và push lên GitHub (antran1496/Dating-Journal-Automation).
---

# Skill: Auto-Commit & Push

> [!IMPORTANT]
> **QUY TẮC TỐI THƯỢNG:** KHÔNG BAO GIỜ tự commit hay push khi người dùng CHƯA YÊU CẦU RÕ RÀNG. Nhiệm vụ mặc định của agent là viết code, báo cáo, rồi **CHỜ LỆNH**.

Remote: `https://github.com/antran1496/Dating-Journal-Automation.git`

## Trình tự (làm tuần tự)

### 1. Kiểm tra repo & branch
- `git rev-parse --is-inside-work-tree` — nếu chưa phải repo: `git init -b main` rồi `git remote add origin https://github.com/antran1496/Dating-Journal-Automation.git`.
- `git branch --show-current` → lấy tên branch. Nếu dạng `feat/DJA_12-...` → ticket `DJA_12`; `main` → không có ticket.

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
- Định dạng: `<emoji> [<TICKET>] <type>(<scope>): <mô tả tiếng Việt>` — bỏ `[<TICKET>]` nếu không có.
- `scope` = tính năng/lớp: `auth`, `dates`, `pages`, `core`, `config`, `data`, `agents`, `deps`.

| Emoji | type | Khi nào |
|---|---|---|
| ✨ | `feat` | Thêm test case / Page / tính năng framework mới |
| ✅ | `test` | Bổ sung/sửa test data, kịch bản trong test có sẵn |
| 🐛 | `fix` | Sửa test fail, sửa locator, sửa lỗi framework |
| ♻️ | `refactor` | Tái cấu trúc không đổi hành vi |
| 📝 | `docs` | README, rules, skills |
| 🔧 | `chore` | pom.xml, cấu hình, suite, .gitignore |

Ví dụ: `✨ feat(auth): thêm bộ test đăng nhập data-driven` · `🐛 [DJA_12] fix(pages): cập nhật locator nút lưu buổi hẹn`

### 6. Commit
- `git commit -m "<message>"` (dùng nháy kép).

### 7. Push (chỉ khi người dùng nói "đẩy/push")
- `git push -u origin <branch>` — chạy với **`BypassSandbox: true`** (cần mạng).
- Push lỗi xác thực → hướng dẫn người dùng cấu hình credential/SSH, không tự sửa cấu hình git global.

### 8. Tổng kết (tiếng Việt)
- Danh sách file đã commit, commit message, hash (`git log -1 --oneline`), trạng thái push.
