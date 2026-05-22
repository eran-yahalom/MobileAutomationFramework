package automation.launchers;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.FeatureWrapper;
import io.cucumber.testng.PickleWrapper;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import automation.utils.RetryAnalyzer;
import automation.drivers.DriverManager;

import java.io.File;

@Log4j2
public class BaseLauncher extends AbstractTestNGCucumberTests {

    // משתנים לשמירת הגיבוי של הקונפיגורציה עבור מנגנון ה-Retry
    private String cachedUdid;
    private String cachedSystemPort;

    @BeforeSuite
    public void beforeSuite() {
        log.info("--- Starting Mobile Automation Test Suite ---");
        File screenshotDir = new File("target/screenshots");
        if (!screenshotDir.exists()) {
            screenshotDir.mkdirs();
        }
    }

    @BeforeClass
    @Parameters({"xmlUdid", "xmlSystemPort"})
    public void cacheDeviceParameters(String xmlUdid, String xmlSystemPort) {
        log.info("Configuring Class with Device Pattern -> UDID: {}, Port: {}", xmlUdid, xmlSystemPort);
        this.cachedUdid = xmlUdid;
        this.cachedSystemPort = xmlSystemPort;
        DriverManager.setDeviceConfig(xmlUdid, xmlSystemPort);
    }

    @Override
    @Test(
            groups = "cucumber",
            description = "Runs Mobile Cucumber Scenarios",
            dataProvider = "scenarios",
            retryAnalyzer = RetryAnalyzer.class
    )
    public void runScenario(PickleWrapper pickle, FeatureWrapper cucumberFeature) {
        log.info(">>> Initializing Mobile Scenario: [{}]", pickle.getPickle().getName());

        // פותר את בעיית ה-Retry: אם ה-ThreadLocal התנקה בטסט הקודם, נטען אותו מחדש מהגיבוי של הלאנצ'ר הנוכחי
        if (DriverManager.getUdid().equals("emulator-5554") && !this.cachedUdid.equals("emulator-5554")) {
            log.info("[RETRY PROTECTION] Re-caching device parameters for Thread ID: {}", Thread.currentThread().getId());
            DriverManager.setDeviceConfig(this.cachedUdid, this.cachedSystemPort);
        }

        try {
            super.runScenario(pickle, cucumberFeature);
            log.info("<<< Scenario Completed: [{}] - PASSED", pickle.getPickle().getName());
        } catch (Throwable t) {
            log.error("!!! Mobile Scenario FAILED: [{}]", pickle.getPickle().getName());
            throw t;
        }
    }
}