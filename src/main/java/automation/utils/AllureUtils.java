package automation.utils;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import java.io.ByteArrayInputStream;

@Log4j2
public class AllureUtils {

    // ==============================
    // Screenshot
    // ==============================
    public static void attachScreenshot(WebDriver driver, String name) {

        if (driver == null) return;

        try {
            byte[] screenshot =
                    ((TakesScreenshot) driver)
                            .getScreenshotAs(OutputType.BYTES);

            Allure.addAttachment(
                    name,
                    "image/png",
                    new ByteArrayInputStream(screenshot),
                    ".png"
            );
        } catch (Exception e) {
            log.error("Failed to capture screenshot: " + e.getMessage(), e);
        }
    }

    // ==============================
    // Browser Console Logs
    // ==============================
    public static void attachBrowserLogs(WebDriver driver) {

        if (driver == null) return;

        try {
            LogEntries logs = driver.manage().logs().get(LogType.BROWSER);

            StringBuilder logText = new StringBuilder();

            for (LogEntry entry : logs) {
                logText.append(entry.getLevel())
                        .append(" - ")
                        .append(entry.getMessage())
                        .append("\n");
            }

            Allure.addAttachment("Browser Console Logs", logText.toString());

        } catch (Exception e) {
            log.error("Failed to capture browser logs: {}", e.getMessage(), e);
        }
    }

    // ==============================
    // Page Source
    // ==============================
    public static void attachPageSource(WebDriver driver) {

        if (driver == null) return;

        try {
            Allure.addAttachment("Page Source", driver.getPageSource());
        } catch (Exception e) {
            log.error("Failed to capture page source: {}", e.getMessage(), e);
        }
    }

    // ==============================
    // Current URL
    // ==============================
    public static void attachCurrentUrl(WebDriver driver) {

        if (driver == null) return;

        try {
            Allure.addAttachment("Current URL", driver.getCurrentUrl());
        } catch (Exception e) {
            log.error("Failed to capture current URL: {}", e.getMessage(), e);
        }
    }

    // ==============================
    // Custom Text Attachment
    // ==============================
    public static void attachText(String name, String content) {
        Allure.addAttachment(name, content);
    }

    // ==============================
    // Environment Info
    // ==============================
    public static void attachEnvironmentInfo() {

        String info =
                "OS: " + System.getProperty("os.name") + "\n" +
                        "OS Version: " + System.getProperty("os.version") + "\n" +
                        "Java Version: " + System.getProperty("java.version");

        Allure.addAttachment("Environment Info", info);
    }

    // ==============================
    //  Step Helper (Optional)
    // ==============================
    @Step("{stepName}")
    public static void step(String stepName) {
    }
}