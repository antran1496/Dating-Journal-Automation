---
name: new-feature-test
description: Quy trình tổng khi người dùng yêu cầu tự động hóa test cho một màn hình hoặc tính năng mới của Dating Journal App (ví dụ đăng ký, tạo buổi hẹn, wishlist). Điều phối các skill locator-strategy, page-object-model, data-driven-testing, test-case-writing.
---

# Skill: Tự động hóa tính năng mới (quy trình tổng)

Làm **tuần tự**, không bỏ bước.

## Bước 0 — Kiểm tra & tạo Branch (BẮT BUỘC trước khi code)
- Chạy `git branch --show-current`.
- **CẤM dev trực tiếp trên nhánh `main`**. Nếu đang ở `main`, tạo branch mới: `git checkout -b feat/<tên-tính-năng>` (hoặc `feat/DJA_<số>-<tên>`).

## Bước 1 — Khảo sát (theo `locator-strategy`)
- Mở màn hình bằng Playwright MCP, đăng nhập bằng tài khoản trong `.env` nếu cần.
- Ghi lại: URL/path, các phần tử cần thao tác + locator, thông báo lỗi/thành công **nguyên văn**, điều hướng sau hành động.
- Liệt kê kịch bản: happy path, validation, quyền truy cập. Trình bày danh sách TC (tiếng Việt) cho người dùng nếu phạm vi lớn.

## Bước 2 — Page Object (theo `page-object-model`)
- Tạo `pages/<Tên>Page.java` (và Component nếu UI dùng chung). Sao chép bố cục `LoginPage`.

## Bước 3 — Test data (theo `data-driven-testing`)
- `testdata/<tính-năng>.json` → `data/models/<Tên>Data.java` → `dataproviders/<Tên>DataProvider.java`.

## Bước 4 — Test (theo `test-case-writing`)
- `tests/<tính năng>/<Tên>Test.java`, thêm group mới vào `TestGroups` nếu cần.

## Bước 5 — Xác minh
```bash
bash scripts/verify-structure.sh
mvn -q -B clean test-compile
mvn -B test -Dsuite=regression -Dtest=<Tên>Test -Dheadless=true
```

## Bước 6 — Báo cáo (tiếng Việt)
- Danh sách TC đã tự động hóa (mã + mô tả), file đã tạo/sửa, kết quả chạy, các điểm UI thiếu id/data-testid nên đề xuất dev.
- **Không commit** cho đến khi người dùng yêu cầu.

## Checklist file cho một tính năng `xxx`
```text
src/main/java/com/datingjournal/pages/XxxPage.java
src/main/java/com/datingjournal/data/models/XxxData.java
src/test/java/com/datingjournal/dataproviders/XxxDataProvider.java
src/test/java/com/datingjournal/tests/<tinhnang>/XxxTest.java
src/test/resources/testdata/xxx.json
```
