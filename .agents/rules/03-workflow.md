---
trigger: always_on
---

# Rule 03 — Quy trình làm việc & Definition of Done

## Trước khi code
1. Trả lời người dùng bằng **tiếng Việt**.
2. Đọc `AGENTS.md` + skill tương ứng trong `.agents/skills/`.
3. Với màn hình mới: khảo sát UI thật để lấy locator và thông báo lỗi chính xác (theo skill `new-feature-test`).

## Definition of Done (đủ TẤT CẢ mới được báo "xong")
- [ ] File đặt đúng thư mục, đặt tên đúng skill `naming-conventions`.
- [ ] `bash scripts/verify-structure.sh` → PASS.
- [ ] `mvn -q -B clean test-compile` → PASS.
- [ ] Test liên quan chạy PASS (ví dụ `mvn -B test -Dsuite=regression -Dheadless=true`).
- [ ] Test data mới nằm trong `testdata/*.json`, dữ liệu nhạy cảm dùng placeholder `${KEY}`.
- [ ] Key cấu hình mới đã có trong `.env.example`.
- [ ] Cập nhật `README.md` nếu thay đổi cách chạy/cấu hình.
- [ ] Báo cáo tiếng Việt: danh sách file thay đổi + kết quả chạy.

## Git
- **KHÔNG BAO GIỜ tự commit/push** khi người dùng chưa yêu cầu rõ ("commit", "đẩy code", "push", `/auto-commit`). Khi được yêu cầu → làm đúng skill `auto-commit`.
- Branch mới luôn tạo từ `main`: `<loại>/<mô-tả-ngắn>` hoặc `<loại>/DJA_<số>` (ví dụ `feat/DJA_12-signup-test`).

## Thay đổi kiến trúc
- Thêm thư viện, thêm/đổi thư mục cấp cao, đổi `BasePage`/`BaseTest`/`ConfigManager`: **phải hỏi người dùng trước**, sau đó cập nhật `AGENTS.md` + rule liên quan trong cùng thay đổi.
