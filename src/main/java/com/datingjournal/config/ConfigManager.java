package com.datingjournal.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Nguồn cấu hình DUY NHẤT của framework (Singleton). Chỉ dùng thư viện chuẩn của Java.
 * <p>
 * Thứ tự ưu tiên khi đọc một key (cao → thấp):
 * <ol>
 *     <li>System property: {@code -Dbase.url=...}</li>
 *     <li>Biến môi trường OS: {@code BASE_URL=...}</li>
 *     <li>File {@code .env} ở thư mục gốc dự án</li>
 *     <li>File {@code config/{env}.properties} trong test resources</li>
 * </ol>
 * Key luôn được viết dạng dot-case ({@code base.url}); khi tra biến môi trường/.env
 * sẽ tự chuyển sang SCREAMING_SNAKE_CASE ({@code BASE_URL}).
 */
public final class ConfigManager {

    private static final Path DOT_ENV_FILE = Paths.get(".env");
    private static final ConfigManager INSTANCE = new ConfigManager();

    private final Properties dotEnv = new Properties();
    private final Properties properties = new Properties();
    private final Environment environment;

    private ConfigManager() {
        loadDotEnv();
        this.environment = Environment.from(readRaw(ConfigKey.ENV));
        loadProperties(environment);
    }

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    public Environment getEnvironment() {
        return environment;
    }

    /** Lấy giá trị bắt buộc; ném lỗi rõ ràng nếu thiếu cấu hình. */
    public String get(String key) {
        String value = readRaw(key);
        if (!isPresent(value)) {
            throw new IllegalStateException(
                    "Thiếu cấu hình '" + key + "' (env: " + toEnvName(key) + "). "
                            + "Hãy khai báo trong .env hoặc config/" + environment.getFileName());
        }
        return value.trim();
    }

    /** Lấy giá trị tùy chọn, trả về giá trị mặc định nếu không có. */
    public String get(String key, String defaultValue) {
        String value = readRaw(key);
        return isPresent(value) ? value.trim() : defaultValue;
    }

    public int getInt(String key, int defaultValue) {
        return Integer.parseInt(get(key, String.valueOf(defaultValue)));
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return Boolean.parseBoolean(get(key, String.valueOf(defaultValue)));
    }

    // ---------------------------------------------------------------- nội bộ

    private String readRaw(String key) {
        String value = System.getProperty(key);
        if (isPresent(value)) {
            return value;
        }
        String envName = toEnvName(key);
        value = System.getenv(envName);
        if (isPresent(value)) {
            return value;
        }
        value = dotEnv.getProperty(envName);
        if (isPresent(value)) {
            return value;
        }
        return properties.getProperty(key);
    }

    /** File .env có cú pháp KEY=VALUE nên đọc được bằng java.util.Properties. Không có file thì bỏ qua. */
    private void loadDotEnv() {
        if (!Files.exists(DOT_ENV_FILE)) {
            return;
        }
        try (Reader reader = Files.newBufferedReader(DOT_ENV_FILE, StandardCharsets.UTF_8)) {
            dotEnv.load(reader);
        } catch (IOException e) {
            throw new IllegalStateException("Không đọc được file .env", e);
        }
    }

    private void loadProperties(Environment env) {
        String path = "config/" + env.getFileName();
        try (InputStream in = ConfigManager.class.getClassLoader().getResourceAsStream(path)) {
            if (in == null) {
                throw new IllegalStateException("Không tìm thấy file cấu hình: " + path);
            }
            properties.load(new InputStreamReader(in, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IllegalStateException("Không đọc được file cấu hình: " + path, e);
        }
    }

    private static boolean isPresent(String value) {
        return value != null && !value.isBlank();
    }

    private static String toEnvName(String key) {
        return key.toUpperCase().replace('.', '_').replace('-', '_');
    }
}
