package automation.hooks;

import automation.utils.AllureUtils;
import automation.drivers.DriverManager;
import com.google.inject.Inject;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

@Log4j2
public class Hooks {
    private final AndroidDriver driver;

    @Inject
    public Hooks(AndroidDriver driver) {
        // Guice יזריק כאן את המופע הייחודי לטרד ולסנריו הנוכחי
        this.driver = driver;
    }

    @Before
    public void setUp() {
        log.info("Scenario Setup: Handling initial application pop-ups.");

        // הגנה אגרסיבית מבוססת טקסט - תופס "אישור", "אפשר", "OK", "Allow"
        String[] commonAlertButtons = {
                "//*[@text='OK' or @text='ok' or @text='אישור']",
                "//*[@text='ALLOW' or @text='Allow' or @text='אפשר בזמן שימוש באפליקציה']",
                "//android.widget.Button[@id='android:id/button2']"
        };

        for (String xpath : commonAlertButtons) {
            try {
                WebDriverWait quickWait = new WebDriverWait(driver, Duration.ofSeconds(2));
                quickWait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath))).click();
                log.info("Bypassed initial pop-up using locator: {}", xpath);
            } catch (Exception e) {
                // לא נמצא פופ-אפ ספציפי זה, ממשיך לבא בתור בלי להיתקע
            }
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        try {
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
            log.info("Tearing down driver and cleaning ThreadLocal variables...");
            // מנקה את הסשן של אפיום ומאפס את ה-ThreadLocal לטסט הבא
            DriverManager.quitDriver();
        }
    }
}