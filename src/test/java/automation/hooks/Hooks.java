package automation.hooks;

import automation.utils.AllureUtils;
import automation.utils.AndroidSystemHandler;
import automation.utils.DriverManager;
import com.google.inject.Inject;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Hooks {
    private final AndroidDriver driver;

    @Inject
    public Hooks(AndroidDriver driver) {
        this.driver = driver;
        // מעדכנים את ה-DriverManager כדי ששאר הפרויקט יוכל לגשת לדרייבר
        DriverManager.setDriver(driver);
    }

    @Before
    public void setUp() {
        AndroidSystemHandler.closeAlert(driver);
        log.info("Driver initialized and injected into Hooks.");
    }

    @After
    public void tearDown(Scenario scenario) { //  הוספת אובייקט ה-Scenario של Cucumber
        try {
            // אם הטסט נכשל והדרייבר חי - נאסוף מרקחים (Artifacts) לדו"ח של Allure
            if (scenario.isFailed() && driver != null) {
                try {
                    String screenshotName = "Failure Screenshot - " + scenario.getName();
                    AllureUtils.attachScreenshot(driver, screenshotName);
                    AllureUtils.attachPageSource(driver); //   קריטי במובייל כדי לראות את מבנה ה-XML בזמן הנפילה
                } catch (Exception e) {
                    log.error("Failed to attach mobile artifacts to Allure: {}", e.getMessage());
                }
            }
        } finally {
            // ניקוי הדרייבר מתבצע תמיד, גם אם איסוף המרקחים נכשל
            if (driver != null) {
                driver.quit();
            }
            DriverManager.unload();
            log.info("Driver quit and unloaded.");
        }
    }
}