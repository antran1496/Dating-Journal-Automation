---
trigger: always_on
---

# Rule 03 — Quy trình làm việc & Definition of Done

## Trước khi code (BẮT BUỘC)
1. Trả lời người dùng bằng **tiếng Việt**.
2. **Kiểm tra branch hiện tại (`git branch --show-current`) TRƯỚC khi tạo/sửa bất kỳ file nào**:
   - **CẤM tuyệt đối dev trực tiếp trên nhánh `main`**.
   - Nếu đang ở `main`, phải tạo branch mới từ `main`: `git checkout -b <loại>/<mô-tả-ngắn>` hoặc `<loại>/DJA_<số>-<mô-tả>` (ví dụ `feat/DJA_12-signup-test`, `fix/login-locator`, `docs/update-rules`).
   - Nếu đang ở một feature branch khác không liên quan đến yêu cầu mới, hỏi người dùng hoặc quay về `main` (`git checkout main && git pull`) để tách branch mới từ `main`.
3. Đọc `AGENTS.md` + skill tương ứng trong `.agents/skills/`.
4. Với màn hình mới: khảo sát UI thật để lấy locator và thông báo lỗi chính xác (theo skill `new-feature-test`).

## Definition of Done (đủ TẤT CẢ mới được báo "xong")
- [ ] Đang đứng ở branch làm việc riêng (KHÔNG phải `main`).
- [ ] File đặt đúng thư mục, đặt tên đúng skill `naming-conventions`.
- [ ] `bash scripts/verify-structure.sh` → PASS.
- [ ] `mvn -q -B clean test-compile` → PASS.
- [ ] Test liên quan chạy PASS (ví dụ `mvn -B test -Dsuite=regression -Dheadless=true`).
- [ ] Test data mới nằm trong `testdata/*.json`, dữ liệu nhạy cảm dùng placeholder `${KEY}`.
- [ ] Key cấu hình mới đã có trong `.env.example`.
- [ ] Cập nhật `README.md` nếu thay đổi cách chạy/cấu hình.
- [ ] Báo cáo tiếng Việt: tên branch hiện tại + danh sách file thay đổi + kết quả chạy.

## Git & Quản lý nhánh
- **KHÔNG BAO GIỜ tự commit/push** khi người dùng chưa yêu cầu rõ ("commit", "đẩy code", "push", `/auto-commit`). Khi được yêu cầu → làm đúng skill `auto-commit`.
- **KHÔNG BAO GIỜ dev hoặc commit trực tiếp vào `main`**. Mọi thay đổi đều thực hiện trên branch riêng tạo từ `main`.
- **KHÔNG BAO GIỜ tự ý merge vào `main`**. Chỉ thực hiện merge vào `main` khi người dùng yêu cầu rõ ràng (ví dụ: "merge vào main", "gộp nhánh vào main").

## Thay đổi kiến trúc
- Thêm thư viện, thêm/đổi thư mục cấp cao, đổi `BasePage`/`BaseTest`/`ConfigManager`: **phải hỏi người dùng trước**, sau đó cập nhật `AGENTS.md` + rule liên quan trong cùng thay đổi.
