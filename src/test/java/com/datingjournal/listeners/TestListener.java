package com.datingjournal.listeners;

import com.datingjournal.config.AppConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Ghi log vòng đời suite/test. Đăng ký trong file suite XML.
 */
public class TestListener implements ITestListener, ISuiteListener {

    private static final Logger LOG = LogManager.getLogger(TestListener.class);

    @Override
    public void onStart(ISuite suite) {
        LOG.info("===== BẮT ĐẦU SUITE: {} | env={} | url={} | browser={} | viewport={} ({}x{}) | headless={} =====",
                suite.getName(), AppConfig.environment(), AppConfig.baseUrl(),
                AppConfig.browser(), AppConfig.viewport(), AppConfig.windowWidth(), AppConfig.windowHeight(),
                AppConfig.isHeadless());
    }

    @Override
    public void onFinish(ISuite suite) {
        LOG.info("===== KẾT THÚC SUITE: {} =====", suite.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        LOG.info("▶ BẮT ĐẦU: {}{}", name(result), params(result));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOG.info("✔ PASS: {}{} ({} ms)", name(result), params(result), duration(result));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LOG.error("✘ FAIL: {}{} ({} ms) - {}", name(result), params(result), duration(result),
                result.getThrowable() == null ? "" : result.getThrowable().getMessage());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOG.warn("⤼ SKIP: {}{}", name(result), params(result));
    }

    private static String name(ITestResult result) {
        return result.getTestClass().getRealClass().getSimpleName() + "." + result.getMethod().getMethodName();
    }

    private static String params(ITestResult result) {
        Object[] parameters = result.getParameters();
        if (parameters == null || parameters.length == 0) {
            return "";
        }
        return Arrays.stream(parameters).map(String::valueOf).collect(Collectors.joining(", ", " [", "]"));
    }

    private static long duration(ITestResult result) {
        return result.getEndMillis() - result.getStartMillis();
    }
}
