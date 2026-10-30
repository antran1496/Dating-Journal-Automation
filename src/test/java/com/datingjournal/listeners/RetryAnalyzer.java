package com.datingjournal.listeners;

import com.datingjournal.config.AppConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Chạy lại test fail tối đa {@code retry.count} lần (mặc định 0 = không retry).
 * Chỉ dùng để giảm nhiễu do mạng/môi trường, KHÔNG dùng để che giấu test flaky.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger LOG = LogManager.getLogger(RetryAnalyzer.class);
    private final int maxRetry = AppConfig.retryCount();
    private int attempt;

    @Override
    public boolean retry(ITestResult result) {
        if (attempt < maxRetry) {
            attempt++;
            LOG.warn("Retry lần {}/{} cho test: {}", attempt, maxRetry, result.getMethod().getMethodName());
            return true;
        }
        return false;
    }
}
