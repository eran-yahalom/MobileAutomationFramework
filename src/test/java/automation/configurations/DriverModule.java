//package automation.configurations;
//
//import automation.di.AndroidDriverProvider;
//import com.google.inject.AbstractModule;
//import io.appium.java_client.AppiumDriver;
//import io.appium.java_client.android.AndroidDriver;
//import io.cucumber.guice.ScenarioScoped;
//
//public class DriverModule extends AbstractModule {
//    @Override
//    protected void configure() {
//        // קישור ה-Provider ל-AndroidDriver
//        bind(AndroidDriver.class)
//                .toProvider(AndroidDriverProvider.class)
//                .in(ScenarioScoped.class);
//
//        // קישור AppiumDriver ל-AndroidDriver כדי שניתן יהיה להזריק את שניהם
//        bind(AppiumDriver.class)
//                .to(AndroidDriver.class)
//                .in(ScenarioScoped.class);
//    }
//}