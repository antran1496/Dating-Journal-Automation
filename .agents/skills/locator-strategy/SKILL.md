---
name: locator-strategy
description: Dùng khi cần chọn hoặc sửa locator (By) cho phần tử trên Dating Journal App. Quy định thứ tự ưu tiên locator, các kiểu bị cấm và cách khảo sát DOM thật trước khi viết.
---

# Skill: Chiến lược Locator

## 1. Thứ tự ưu tiên (chọn cái đầu tiên dùng được)

| # | Loại | Ví dụ | Ghi chú |
|---|---|---|---|
| 1 | `id` | `By.id("login-email")` | Ổn định nhất |
| 2 | `name` | `By.name("password")` | Với input form |
| 3 | `data-testid` | `By.cssSelector("[data-testid='date-card']")` | Nếu dev có thêm |
| 4 | Thuộc tính ngữ nghĩa | `a[href='/signup']`, `button[type='submit']`, `[aria-label='Ghi nhanh buổi hẹn mới']`, `label[for='x']` | |
| 5 | CSS quan hệ | `#login-email ~ p`, `form button[type='submit']` | Neo vào phần tử có id gần nhất |
| 6 | XPath tương đối theo text | `//button[normalize-space()='Đăng nhập']` | Chỉ khi không còn cách khác; text tiếng Việt dễ đổi |

## 2. CẤM

- XPath tuyệt đối: `/html/body/div[2]/...`
- Class Tailwind/utility: `.w-full.h-11`, `.bg-red-50`, `.text-sm` — đổi giao diện là gãy.
- Index mơ hồ: `(//div)[5]`, `:nth-child(7)` khi không có neo.
- Locator khai báo trong class Test.

## 3. Cách khảo sát (bắt buộc trước khi viết)

1. Mở trang bằng Playwright MCP (`browser_navigate`) → `browser_snapshot` để xem cấu trúc.
2. Dùng `browser_evaluate` liệt kê thuộc tính `id/name/type/aria-label/data-testid/href` của input, button, link.
3. Kích hoạt trạng thái lỗi/thành công thật để lấy **chính xác** text thông báo (copy nguyên văn vào test data).
4. Ghi locator vào Page dưới dạng `private static final By`.

## 4. Locator đã xác minh (tham khảo)

| Màn hình | Phần tử | Locator |
|---|---|---|
| `/login` | Email / Mật khẩu | `#login-email` / `#login-password` |
| `/login` | Nút Đăng nhập | `form button[type='submit']` |
| `/login` | Lỗi chung | `svg.lucide-circle-alert + span` |
| `/login` | Lỗi theo ô | `#login-email ~ p`, `#login-password ~ p` |
| `/dashboard` | Lời chào | `h1` ("Chào Em 💗") |
| Điều hướng (Desktop & Mobile) | Menu chính | `nav a[href='/dashboard' \| '/dates' \| '/chat' \| '/more']` |
| Điều hướng (chỉ Mobile `< 768px`) | Nút dấu cộng tạo nhanh | `nav a[href='/dates/new']` (`aria-label='Ghi nhanh buổi hẹn mới'`) |
| Điều hướng (chỉ Desktop `>= 768px`) | Avatar cài đặt trên header | `header a[href='/settings']` |

## 5. Lưu ý quan trọng: Giao diện Desktop vs Mobile (Responsive DOM)

Ứng dụng Dating Journal render **cả phần tử Desktop lẫn Mobile trong cùng DOM** và ẩn/hiện bằng CSS (`hidden md:flex` / `md:hidden`):
- **Desktop (`1440x900`, `>= 768px`)**: Thanh điều hướng nằm trong `<header><nav>` ở đầu trang; thanh `<nav>` cố định ở đáy màn hình bị ẩn (`display: none`).
- **Mobile (`390x844`, `< 768px`)**: Các link trong `<header>` bị ẩn (`display: none`); thanh điều hướng đáy (Bottom Bar `<nav>`) ở cuối màn hình hiển thị kèm nút tạo nhanh `/dates/new`.
- **Quy tắc chọn locator**:
  - `WaitUtils.visible()` / `clickable()` và `BasePage.isDisplayed()` đã được thiết kế để **tự động lấy phần tử ĐANG HIỂN THỊ đầu tiên** trong danh sách khớp với locator.
  - Vì vậy, với phần tử có ở cả 2 bản (như menu điều hướng), viết locator chung `nav a[href='/dates']` thay vì khóa cứng vào `header a[href='/dates']`, để test chạy đúng trên cả `-Dviewport=desktop` lẫn `-Dviewport=mobile`.
  - Khi khảo sát bằng Playwright MCP, luôn kiểm tra cả 2 khung nhìn (`1440x900` và `390x844`) nếu tính năng có bố cục responsive khác nhau.

> Khi phát hiện ứng dụng thiếu id/data-testid ở phần tử quan trọng, ghi chú lại trong báo cáo để đề xuất dev bổ sung.
