package automation.launchers;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.FeatureWrapper;
import io.cucumber.testng.PickleWrapper;
import lombok.extern.log4j.Log4j2;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import automation.utils.RetryAnalyzer; // עדכון ה-Import לפי המבנה החדש

import java.io.File;

@Log4j2
public class BaseLauncher extends AbstractTestNGCucumberTests {

    @BeforeSuite
    public void beforeSuite() {
        log.info("--- Starting Mobile Automation Test Suite ---");

        // יצירת תיקיית צילומי מסך (חשוב במיוחד לכישלונות במובייל)
        File screenshotDir = new File("target/screenshots");
        if (!screenshotDir.exists()) {
            if (screenshotDir.mkdirs()) {
                log.info("Created directory: target/screenshots");
            }
        }

        // לוג להרצה מקבילית - קריטי כשמריצים על מספר אמולטורים במקביל
        String threadCount = System.getProperty("dataproviderthreadcount", "1");
        log.info("Parallel Execution: DataProvider Thread Count set to: {}", threadCount);
    }

    @Override
    @Test(
            groups = "cucumber",
            description = "Runs Mobile Cucumber Scenarios",
            dataProvider = "scenarios",
            retryAnalyzer = RetryAnalyzer.class // משתמש במנגנון ה-Retry שראינו ב-image_2c9b38.jpg
    )
    public void runScenario(PickleWrapper pickle, FeatureWrapper cucumberFeature) {

        log.info(">>> Initializing Mobile Scenario: [{}]", pickle.getPickle().getName());

        try {
            super.runScenario(pickle, cucumberFeature);
            log.info("<<< Scenario Completed: [{}] - PASSED", pickle.getPickle().getName());
        } catch (Throwable t) {
            log.error("!!! Mobile Scenario FAILED: [{}]", pickle.getPickle().getName());
            log.error("Failure Details: {}", t.getMessage());
            throw t;
        }
    }

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        // parallel=true מבטיח שכל Scenario יקבל Thread משלו מה-ThreadLocal ב-DriverManager
        return super.scenarios();
    }
}