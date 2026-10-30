package com.datingjournal.config;

/**
 * Lớp truy cập cấu hình có kiểu (type-safe). Page/Test chỉ gọi lớp này,
 * KHÔNG gọi trực tiếp {@link ConfigManager#get(String)} với key chuỗi.
 */
public final class AppConfig {

    private static final ConfigManager CONFIG = ConfigManager.getInstance();

    private AppConfig() {
    }

    public static Environment environment() {
        return CONFIG.getEnvironment();
    }

    /** URL gốc, luôn KHÔNG có dấu "/" ở cuối. */
    public static String baseUrl() {
        String url = CONFIG.get(ConfigKey.BASE_URL);
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }

    public static String browser() {
        return CONFIG.get(ConfigKey.BROWSER, "chrome");
    }

    public static boolean isHeadless() {
        return CONFIG.getBoolean(ConfigKey.HEADLESS, false);
    }

    public static int timeoutSeconds() {
        return CONFIG.getInt(ConfigKey.TIMEOUT_SECONDS, 15);
    }

    public static int pageLoadTimeoutSeconds() {
        return CONFIG.getInt(ConfigKey.PAGE_LOAD_TIMEOUT_SECONDS, 60);
    }

    /** Chế độ khung nhìn: "desktop" (mặc định, 1440x900) hoặc "mobile" (390x844). */
    public static String viewport() {
        return CONFIG.get(ConfigKey.VIEWPORT, "desktop").toLowerCase();
    }

    public static boolean isMobileViewport() {
        return "mobile".equalsIgnoreCase(viewport());
    }

    public static int windowWidth() {
        if (isMobileViewport() && System.getProperty(ConfigKey.WINDOW_WIDTH) == null) {
            return 390;
        }
        return CONFIG.getInt(ConfigKey.WINDOW_WIDTH, 1440);
    }

    public static int windowHeight() {
        if (isMobileViewport() && System.getProperty(ConfigKey.WINDOW_HEIGHT) == null) {
            return 844;
        }
        return CONFIG.getInt(ConfigKey.WINDOW_HEIGHT, 900);
    }

    public static int retryCount() {
        return CONFIG.getInt(ConfigKey.RETRY_COUNT, 0);
    }

    public static String adminEmail() {
        return CONFIG.get(ConfigKey.ADMIN_EMAIL);
    }

    public static String adminPassword() {
        return CONFIG.get(ConfigKey.ADMIN_PASSWORD);
    }

    /** Tra cứu giá trị theo key bất kỳ (dùng cho placeholder ${KEY} trong test data). */
    public static String resolve(String key) {
        return CONFIG.get(key);
    }
}
