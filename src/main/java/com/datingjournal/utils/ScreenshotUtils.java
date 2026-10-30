package com.datingjournal.utils;

import com.datingjournal.core.DriverManager;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Chụp màn hình: lưu file vào {@code target/screenshots/} và đính kèm vào báo cáo Allure.
 */
public final class ScreenshotUtils {

    private static final Logger LOG = LogManager.getLogger(ScreenshotUtils.class);
    private static final Path SCREENSHOT_DIR = Paths.get("target", "screenshots");
    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtils() {
    }

    public static void capture(String name) {
        if (!DriverManager.hasDriver()) {
            return;
        }
        try {
            byte[] image = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(name, "image/png", new ByteArrayInputStream(image), ".png");

            Files.createDirectories(SCREENSHOT_DIR);
            String safeName = name.replaceAll("[^a-zA-Z0-9_-]", "_");
            Path file = SCREENSHOT_DIR.resolve(safeName + "_" + LocalDateTime.now().format(TIMESTAMP) + ".png");
            Files.write(file, image);
            LOG.info("Đã lưu screenshot: {}", file.toAbsolutePath());
        } catch (IOException | RuntimeException e) {
            LOG.warn("Không chụp được screenshot '{}': {}", name, e.getMessage());
        }
    }
}
