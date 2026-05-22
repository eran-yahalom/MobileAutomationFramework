package automation.configurations;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.cucumber.guice.ScenarioScoped;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import automation.di.AndroidDriverProvider;

public class MobileGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        // קישור ליבה יחיד עבור הדרייבר האנדרואידי
        bind(AndroidDriver.class)
                .toProvider(AndroidDriverProvider.class)
                .in(ScenarioScoped.class);
    }

    @Provides
    @ScenarioScoped
    public AppiumDriver provideAppiumDriver(AndroidDriver androidDriver) {
        // מבטיח שכל קלאס שמבקש AppiumDriver יקבל בדיוק את אותו המופע של הטרד
        return androidDriver;
    }
}