package com.datingjournal.base;

/**
 * Tên group TestNG dùng chung. KHÔNG viết chuỗi group trực tiếp trong {@code @Test}.
 */
public final class TestGroups {

    /** Bộ test tối thiểu, chạy nhanh sau mỗi lần deploy. */
    public static final String SMOKE = "smoke";
    /** Bộ test đầy đủ. */
    public static final String REGRESSION = "regression";

    // Group theo tính năng
    public static final String AUTH = "auth";

    private TestGroups() {
    }
}
