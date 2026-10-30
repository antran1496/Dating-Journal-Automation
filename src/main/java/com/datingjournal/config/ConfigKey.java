package com.datingjournal.config;

/**
 * Tập trung toàn bộ tên key cấu hình. KHÔNG dùng chuỗi key rời rạc ở nơi khác.
 */
public final class ConfigKey {

    public static final String ENV = "env";
    public static final String BASE_URL = "base.url";
    public static final String BROWSER = "browser";
    public static final String HEADLESS = "headless";
    public static final String VIEWPORT = "viewport";
    public static final String TIMEOUT_SECONDS = "timeout.seconds";
    public static final String PAGE_LOAD_TIMEOUT_SECONDS = "page.load.timeout.seconds";
    public static final String WINDOW_WIDTH = "window.width";
    public static final String WINDOW_HEIGHT = "window.height";
    public static final String RETRY_COUNT = "retry.count";
    public static final String ADMIN_EMAIL = "admin.email";
    public static final String ADMIN_PASSWORD = "admin.password";

    private ConfigKey() {
    }
}
