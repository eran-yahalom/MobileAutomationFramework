package automation.di;

import automation.utils.DriverManager;
import com.google.inject.Provider;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.net.URL;
import java.time.Duration;

public class AndroidDriverProvider implements Provider<AndroidDriver> {

    private static final String APPIUM_URL = "http://127.0.0.1:4723/";
    private static final String APP_PATH = System.getProperty("user.dir") + "/src/test/resources/apps/Android-MyDemoAppRN.1.3.0.build-244.apk";

    @Override
    public AndroidDriver get() {
        System.out.println("DEBUG: Initializing AndroidDriver via Provider...");

        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setDeviceName("emulator-5554")
                .setAutomationName("UiAutomator2")
                .setApp(APP_PATH)
                .setAppPackage("com.saucelabs.mydemoapp.rn")
                .setAppActivity(".MainActivity")
                .setNoReset(false)
                .setAutoGrantPermissions(true);

        try {
            AndroidDriver driver = new AndroidDriver(new URL(APPIUM_URL), options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            // חשוב: עדכון ה-DriverManager כדי ששאר ה-framework יכיר את הדרייבר
            DriverManager.setDriver(driver);

            return driver;
        } catch (Exception e) {
            throw new RuntimeException("CRITICAL: Failed to initialize AndroidDriver", e);
        }
    }
}