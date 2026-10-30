package com.datingjournal.core;

import java.util.Arrays;

/**
 * Các trình duyệt được hỗ trợ.
 */
public enum BrowserType {
    CHROME,
    FIREFOX,
    EDGE,
    SAFARI;

    public static BrowserType from(String value) {
        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(value.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Trình duyệt không hỗ trợ: " + value));
    }
}
