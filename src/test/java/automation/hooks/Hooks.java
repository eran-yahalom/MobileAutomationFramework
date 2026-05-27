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
            if (scenario.isFailed() && driver != null) {
                try {
                    String screenshotName = "Failure Screenshot - " + scenario.getName();
                    // אילור מקבל את ה-AppiumDriver ומצלם מסך בצורה אחידה לשתי הפלטפורמות
                    AllureUtils.attachScreenshot(driver, screenshotName);
                    AllureUtils.attachPageSource(driver);
                    log.info("Successfully attached mobile artifacts to Allure.");
                } catch (Exception e) {
                    log.error("Failed to attach artifacts: {}", e.getMessage());
                }
            }
        } finally {
            log.info("Tearing down driver and cleaning ThreadLocal variables...");
            // סגירת הסשן בשרת האפיום (מנקה גם XCUITest וגם UiAutomator2) ואיפוס ה-ThreadLocal
            DriverManager.quitDriver();
        }
    }
}