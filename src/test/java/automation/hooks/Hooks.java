package automation.hooks;

import automation.utils.AllureUtils;
import automation.drivers.DriverManager;
import com.google.inject.Inject;
import io.appium.java_client.AppiumDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Hooks {
    private final AppiumDriver driver;

    @Inject
    public Hooks(AppiumDriver driver) {
        // Guice מזריק כאן אוטומטית את מופע הדרייבר המתאים (Android או iOS) הייחודי לטרד הנוכחי
        this.driver = driver;
    }

    @Before
    public void setUp() {
        log.info("Starting Scenario: Appium Driver is initialized and ready.");
    }

    @After
    public void tearDown(Scenario scenario) {
        try {
            // 1. צילום מסך והפקת לוגים רק במידה והטסט נכשל והדרייבר פעיל
            if (scenario.isFailed() && driver != null) {
                try {
                    String screenshotName = "Failure Screenshot - " + scenario.getName();
                    AllureUtils.attachScreenshot(driver, screenshotName);
                    AllureUtils.attachPageSource(driver);
                    log.info("Successfully attached mobile artifacts to Allure.");
                } catch (Exception e) {
                    log.error("Failed to attach artifacts: {}", e.getMessage());
                }
            }
        } finally {
            // 2. שחרור הפורט (8200) וסגירת הסשן בצורה אקטיבית והרמטית
            if (driver != null) {
                try {
                    log.info("[DRIVER] Actively quitting current session to free system ports...");
                    driver.quit();
                } catch (Exception e) {
                    log.error("Failed to perform direct driver.quit(): {}", e.getMessage());
                }
            }

            // 3. איפוס סופי של משתני ה-ThreadLocal וה-Memory בפרוורק שלך
            log.info("Tearing down driver and cleaning ThreadLocal variables...");
            DriverManager.quitDriver();
        }
    }
}