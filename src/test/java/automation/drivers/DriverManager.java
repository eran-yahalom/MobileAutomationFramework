package automation.drivers;

import io.appium.java_client.AppiumDriver;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class DriverManager {

    // שינוי ל-AppiumDriver גנרי: מונע התנגשויות ומבודד מופע דרייבר לכל Thread (אנדרואיד או iOS)
    private static final ThreadLocal<AppiumDriver> driverThreadLocal = new ThreadLocal<>();

    // משתני ThreadLocal לשמירת הגדרות ה-XML הספציפיות לכל טרד ריצה
    private static final ThreadLocal<String> udidThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<String> driverPortThreadLocal = new ThreadLocal<>(); // הפורט הפנימי (systemPort לאנדרואיד / wdaLocalPort ל-iOS)

    /**
     * מחזיר את הדרייבר הייחודי (AppiumDriver) ל-Thread הנוכחי.
     * מיועד לשימוש ה-Hooks, ה-Providers ושאר מחלקות ה-Steps/Pages בפרויקט.
     */
    public static AppiumDriver getDriver() {
        return driverThreadLocal.get();
    }

    /**
     * מגדיר את הדרייבר (Android או iOS) עבור ה-Thread הנוכחי.
     * נקרא אוטומטית על ידי ה-AppiumDriverProvider של Guice מיד לאחר האתחול.
     */
    public static void setDriver(AppiumDriver driver) {
        log.info("[DRIVER] Setting new AppiumDriver instance for Thread ID: {}", Thread.currentThread().getId());
        driverThreadLocal.set(driver);
    }

    /**
     * שומר את פרמטרי המכשיר שמגיעים מקובץ ה-XML של TestNG.
     * תואם גם ל-systemPort של אנדרואיד וגם ל-wdaLocalPort של iOS.
     */
    public static void setDeviceConfig(String udid, String driverPort) {
        log.info("[CONFIG] Caching device configuration for Thread ID: {} -> UDID: {}, Port: {}",
                Thread.currentThread().getId(), udid, driverPort);
        udidThreadLocal.set(udid);
        driverPortThreadLocal.set(driverPort);
    }

    /**
     * שולף את ה-UDID של המכשיר (אמולטור/סימולטור) עבור ה-Thread הנוכחי.
     */
    public static String getUdid() {
        if (udidThreadLocal.get() == null) {
            String platform = System.getProperty("platform", "android");
            if (platform.equalsIgnoreCase("ios")) {
                log.warn("[CONFIG WARN] UDID was not found for iOS Thread ID: {}. Using fallback: A61883F1-D2C5-46E0-99E6-462DF145AC38", Thread.currentThread().getId());
                return "A61883F1-D2C5-46E0-99E6-462DF145AC38"; // האייפון 17 הווירטואלי שלך כגיבוי
            }
            log.warn("[CONFIG WARN] UDID was not found for Android Thread ID: {}. Using fallback: emulator-5554", Thread.currentThread().getId());
            return "emulator-5554";
        }
        return udidThreadLocal.get();
    }

    /**
     * שולף את הפורט הייעודי (System Port לאנדרואיד או WDA Local Port ל-iOS) עבור ה-Thread הנוכחי.
     */
    public static String getSystemPort() {
        if (driverPortThreadLocal.get() == null) {
            String platform = System.getProperty("platform", "android");
            String defaultPort = platform.equalsIgnoreCase("ios") ? "8100" : "8200";
            log.warn("[CONFIG WARN] Driver Port was not found for Thread ID: {}. Using fallback: {}", Thread.currentThread().getId(), defaultPort);
            return defaultPort;
        }
        return driverPortThreadLocal.get();
    }

    /**
     * סוגר את סשן הדרייבר (מכל סוג) ומנקה לחלוטין את הזיכרון של ה-Thread הנוכחי.
     * נקרא תמיד בבלוק ה-finally בתוך ה-@After של מחלקת ה-Hooks.
     */
    public static void quitDriver() {
        long threadId = Thread.currentThread().getId();

        if (driverThreadLocal.get() != null) {
            try {
                log.info("[DRIVER] Quitting AppiumDriver session for Thread ID: {}", threadId);
                driverThreadLocal.get().quit();   // סגירה פיזית של האפליקציה והסשן בשרת
            } catch (Exception e) {
                log.error("[DRIVER ERROR] Failed to quit driver cleanly on Thread ID: {}. Message: {}", threadId, e.getMessage());
            } finally {
                driverThreadLocal.remove();       // ניקוי הדרייבר מה-Thread החי כדי למנוע Memory Leak
            }
        }

        // ניקוי מוחלט של קונפיגורציית הטרד
        udidThreadLocal.remove();
        driverPortThreadLocal.remove();
        log.info("[CONFIG] ThreadLocal memory cleared completely for Thread ID: {}", threadId);
    }
}