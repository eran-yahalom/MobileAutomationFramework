package automation.di;

import automation.drivers.DriverManager;
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
        // הגנה: אם הטרד הזה כבר מחזיק דרייבר פעיל, נשתמש בו ולא נפתח סשן חדש
        if (DriverManager.getDriver() != null) {
            return DriverManager.getDriver();
        }

        try {
            String udid = DriverManager.getUdid();
            String systemPort = DriverManager.getSystemPort();

            // חסימת מצב שבו הפרמטרים לא הגיעו מה-XML בזמן
            if (udid == null || systemPort == null) {
                throw new RuntimeException("TestNG XML parameters are missing for this thread!");
            }

            UiAutomator2Options options = new UiAutomator2Options()
                    .setPlatformName("Android")
                    .setAutomationName("UiAutomator2")
                    .setUdid(udid)
                    .setSystemPort(Integer.parseInt(systemPort))
                    .setApp(APP_PATH)
                    .setAppPackage("com.saucelabs.mydemoapp.rn")
                    .setAppActivity(".MainActivity")
                    .setAutoGrantPermissions(true)
                    .setNoReset(false)
                    .setFullReset(true); // מבטיח דף חלק לחלוטין בכל טסט מחדש

            AndroidDriver driver = new AndroidDriver(new URL(APPIUM_URL), options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            // רישום הדרייבר בגלובל של הטרד
            DriverManager.setDriver(driver);
            return driver;

        } catch (Exception e) {
            throw new RuntimeException("CRITICAL: Failed to initialize AndroidDriver", e);
        }
    }
}