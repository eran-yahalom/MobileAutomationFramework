package automation.launchers;

import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {
                "automation.step_definitions", // כאן נמצאים הצעדים שלך
                "automation.hooks"             // כאן נמצאים ה-Before/After
        },
        plugin = {
                "pretty",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
                "json:target/cucumber.json"
        },
        tags = "@checkout"
)
public class CheckOutLauncher extends BaseLauncher {
    @Override
    // שינוי ל-true מאפשר הרצה מקבילית של Scenarios
    // בזכות ה-ThreadLocal ב-DriverManager, כל טסט יקבל דרייבר משלו
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
