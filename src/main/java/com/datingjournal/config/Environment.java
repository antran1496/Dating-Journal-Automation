package com.datingjournal.config;

import java.util.Arrays;

/**
 * Các môi trường chạy test. Mỗi môi trường ứng với 1 file trong {@code src/test/resources/config/}.
 */
public enum Environment {
    DEV("dev.properties"),
    STAGING("staging.properties"),
    PROD("prod.properties");

    private final String fileName;

    Environment(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    /** Chuyển chuỗi (không phân biệt hoa thường) sang enum; mặc định là PROD nếu để trống. */
    public static Environment from(String value) {
        if (value == null || value.isBlank()) {
            return PROD;
        }
        return Arrays.stream(values())
                .filter(env -> env.name().equalsIgnoreCase(value.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Môi trường không hợp lệ: " + value));
    }
}
