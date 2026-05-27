package automation.di;

import automation.drivers.DriverManager;
import automation.utils.ConfigurationsUtils;
import com.google.inject.Provider;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;

import java.net.URL;
import java.time.Duration;

public class AppiumDriverProvider implements Provider<AppiumDriver> {

    private static final String APPIUM_URL = "http://127.0.0.1:4723/";

    // נתיבים מעודכנים ומדויקים לאפליקציית SwagLabs בשתי הפלטפורמות
    private static final String ANDROID_SWAGLABS_APK = System.getProperty("user.dir") + "/src/test/resources/apps/Android.SauceLabs.Mobile.Sample.app.2.7.1.apk";
    private static final String IOS_APP_PATH = System.getProperty("user.dir") + "/src/test/resources/apps/iOS.Simulator.SauceLabs.Mobile.Sample.app.2.7.1.app";

    @Override
    public AppiumDriver get() {
        // הגנה: אם הטרד הזה כבר מחזיק דרייבר פעיל, נשתמש בו ולא נפתח סשן חדש
        if (DriverManager.getDriver() != null) {
            return DriverManager.getDriver();
        }

        String platform = System.getProperty("platform");

        if (platform == null) {
            // קריאה מקובץ ה-global.properties שלך שבו הגדרת platform=ios
            platform = ConfigurationsUtils.readGlobalProperty("platform");
        }

        if (platform == null) {
            platform = "android";
        }

        platform = platform.toLowerCase().trim();

        try {
            String udid = DriverManager.getUdid();

            // מנגנון הגנה: תמיכה בהרצה ישירה מה-Feature Studio כשפרמטר ה-UDID לא מוזרק מה-XML
            if (udid == null) {
                udid = platform.equalsIgnoreCase("ios") ? "A61883F1-D2C5-46E0-99E6-462DF145AC38" : "emulator-5554";
            }

            AppiumDriver driver;

            if (platform.equalsIgnoreCase("ios")) {
                // קונפיגורציית ה-iOS הייעודית לאייפון 17 הווירטואלי שלך
                XCUITestOptions options = new XCUITestOptions()
                        .setPlatformName("iOS")
                        .setAutomationName("XCUITest")
                        .setDeviceName("iPhone 17")
                        .setUdid(udid)
                        .setPlatformVersion("26.5")
                        .setApp(IOS_APP_PATH)
                        .setWdaLocalPort(8100)
                        .setClearSystemFiles(true)
                        // תיקון ה-Reset: משאיר את האפליקציה במצבה הקיים לשמירת היוזר המחובר קבוע
                        .setNoReset(true)
                        .setFullReset(false);


                driver = new IOSDriver(new URL(APPIUM_URL), options);
            } else {
                // קונפיגורציית אנדרואיד מתוקנת
                String systemPort = DriverManager.getSystemPort();
                if (systemPort == null) {
                    systemPort = "8200"; // ברירת מחדל יציבה להרצה ישירה ללא תלות ב-XML
                }

                UiAutomator2Options options = new UiAutomator2Options()
                        .setPlatformName("Android")
                        .setAutomationName("UiAutomator2")
                        .setUdid(udid)
                        .setSystemPort(Integer.parseInt(systemPort))
                        .setApp(ANDROID_SWAGLABS_APK)
                        // הגדרת חובה המונעת משרת ה-Appium לנסות לנחש רכיבי פתיחה שגויים או ישנים
                        .setAppPackage("com.swaglabsmobileapp")
                        .setAppActivity(".MainActivity")
                        .setAutoGrantPermissions(true)
                        // התאמה מלאה ל-iOS: מונע מחיקה ואיפוס מחדש של האפליקציה בכל ריצה
                        .setNoReset(false)
                        .setFullReset(false);
//                        .setNoReset(false) --do both when app is installed and you need a new app on top of it , then comment and uncomment the above 2
//                        .setFullReset(true);

                driver = new AndroidDriver(new URL(APPIUM_URL), options);
            }

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            // רישום הדרייבר בגלובל של הטרד (ThreadLocal) בתוך ה-DriverManager
            DriverManager.setDriver(driver);
            return driver;

        } catch (Exception e) {
            throw new RuntimeException("CRITICAL: Failed to initialize AppiumDriver for platform: " + platform, e);
        }
    }
}