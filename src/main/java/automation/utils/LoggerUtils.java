package automation.utils;

import org.slf4j.LoggerFactory;

public class LoggerUtils {

    public static void logInfo(String message) {
        String className = getCallingClassName();
        org.slf4j.Logger logger = LoggerFactory.getLogger(className);
        logger.info(message);
    }

    public static void logWarning(String message) {
        org.slf4j.Logger logger = LoggerFactory.getLogger(getCallingClassName());
        logger.warn(message);
    }

    public static void logError(String message, Exception e) {
        org.slf4j.Logger logger = LoggerFactory.getLogger(getCallingClassName());
        logger.error(message, e);
    }

    public static String getCallingClassName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (!element.getClassName().equals(LoggerUtils.class.getName())
                    && !element.getClassName().contains("java.lang.Thread")) {
                return element.getClassName();
            }
        }
        return LoggerUtils.class.getName();
    }
}

