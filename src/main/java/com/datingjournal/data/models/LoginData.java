package com.datingjournal.data.models;

/**
 * Một bộ dữ liệu test cho màn hình Đăng nhập (ánh xạ 1-1 với phần tử trong {@code testdata/login.json}).
 *
 * @param caseId                mã test case, ví dụ {@code TC_LOGIN_02_01}
 * @param description           mô tả ngắn bằng tiếng Việt, hiển thị trên báo cáo
 * @param email                 email nhập vào (có thể là placeholder {@code ${ADMIN_EMAIL}})
 * @param password              mật khẩu nhập vào
 * @param expectedFormError     thông báo lỗi chung mong đợi ("" nếu không có)
 * @param expectedEmailError    thông báo lỗi dưới ô email mong đợi ("" nếu không có)
 * @param expectedPasswordError thông báo lỗi dưới ô mật khẩu mong đợi ("" nếu không có)
 */
public record LoginData(
        String caseId,
        String description,
        String email,
        String password,
        String expectedFormError,
        String expectedEmailError,
        String expectedPasswordError) {

    public LoginData {
        email = nullToEmpty(email);
        password = nullToEmpty(password);
        expectedFormError = nullToEmpty(expectedFormError);
        expectedEmailError = nullToEmpty(expectedEmailError);
        expectedPasswordError = nullToEmpty(expectedPasswordError);
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    /** Hiển thị gọn trên báo cáo TestNG/Allure (không lộ mật khẩu). */
    @Override
    public String toString() {
        return caseId + " - " + description;
    }
}
