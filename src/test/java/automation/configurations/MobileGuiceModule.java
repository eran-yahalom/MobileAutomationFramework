package automation.configurations;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.cucumber.guice.ScenarioScoped;
import io.appium.java_client.AppiumDriver;
import automation.di.AppiumDriverProvider; // שים לב לשינוי בשם ה-Provider

public class MobileGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        // קישור ליבה יחיד והפעם בצורה גנרית עבור AppiumDriver דרך ה-Provider החדש
        bind(AppiumDriver.class)
                .toProvider(AppiumDriverProvider.class)
                .in(ScenarioScoped.class);
    }

    /*
     מתודת ה-@Provides הישנה נמחקה!
     מכיוון שקשרנו את AppiumDriver ישירות ל-Provider החדש ב-configure(),
     כל קלאס בפרויקט שיבקש AppiumDriver יקבל אוטומטית את המופע הנכון (Android או iOS)
     בטווח של ה-ScenarioScoped, בלי צורך בשכבת תיווך נוספת.
    */
}