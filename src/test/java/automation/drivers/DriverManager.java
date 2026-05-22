package automation.drivers;

import io.appium.java_client.android.AndroidDriver;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class DriverManager {

    // מניעת התנגשויות בריצה מקבילית: כל Thread מקבל מופע דרייבר מבודד משלו
    private static final ThreadLocal<AndroidDriver> driverThreadLocal = new ThreadLocal<>();

    // משתני ThreadLocal לשמירת הגדרות ה-XML הספציפיות לכל טרד ריצה
    private static final ThreadLocal<String> udidThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<String> systemPortThreadLocal = new ThreadLocal<>();

    /**
     * מחזיר את הדרייבר הייחודי ל-Thread הנוכחי.
     * מיועד לשימוש ה-Hooks ושאר מחלקות ה-Steps/Pages בפרויקט.
     */
    public static AndroidDriver getDriver() {
        return driverThreadLocal.get();
    }

    /**
     * מגדיר את הדרייבר עבור ה-Thread הנוכחי.
     * נקרא אוטומטית על ידי ה-AndroidDriverProvider של Guice מיד לאחר האתחול.
     */
    public static void setDriver(AndroidDriver driver) {
        log.info("[DRIVER] Setting new AndroidDriver instance for Thread ID: {}", Thread.currentThread().getId());
        driverThreadLocal.set(driver);
    }

    /**
     * שומר את פרמטרי המכשיר שמגיעים מקובץ ה-XML של TestNG.
     * נקרא ב-@BeforeClass בתוך ה-BaseLauncher שלך.
     */
    public static void setDeviceConfig(String udid, String systemPort) {
        log.info("[CONFIG] Caching device configuration for Thread ID: {} -> UDID: {}, Port: {}",
                Thread.currentThread().getId(), udid, systemPort);
        udidThreadLocal.set(udid);
        systemPortThreadLocal.set(systemPort);
    }

    /**
     * שולף את ה-UDID של האמונייטור עבור ה-Thread הנוכחי.
     */
    public static String getUdid() {
        if (udidThreadLocal.get() == null) {
            log.warn("[CONFIG WARN] UDID was not found for Thread ID: {}. Using fallback: emulator-5554", Thread.currentThread().getId());
            return "emulator-5554";
        }
        return udidThreadLocal.get();
    }

    /**
     * שולף את ה-System Port הייעודי ל-UiAutomator2 עבור ה-Thread הנוכחי.
     */
    public static String getSystemPort() {
        if (systemPortThreadLocal.get() == null) {
            log.warn("[CONFIG WARN] System Port was not found for Thread ID: {}. Using fallback: 8200", Thread.currentThread().getId());
            return "8200";
        }
        return systemPortThreadLocal.get();
    }

    /**
     * סוגר את סשן הדרייבר באמונייטור ומנקה לחלוטין את הזיכרון של ה-Thread הנוכחי.
     * נקרא תמיד בבלוק ה-finally בתוך ה-@After של מחלקת ה-Hooks.
     */
    public static void quitDriver() {
        long threadId = Thread.currentThread().getId();

        if (driverThreadLocal.get() != null) {
            try {
                log.info("[DRIVER] Quitting AndroidDriver session for Thread ID: {}", threadId);
                driverThreadLocal.get().quit();   // סגירה פיזית של האפליקציה והסשן בשרת
            } catch (Exception e) {
                log.error("[DRIVER ERROR] Failed to quit driver cleanly on Thread ID: {}. Message: {}", threadId, e.getMessage());
            } finally {
                driverThreadLocal.remove();       // ניקוי הדרייבר מה-Thread החי כדי למנוע Memory Leak
            }
        }

        // ניקוי מוחלט של קונפיגורציית הטרד
        udidThreadLocal.remove();
        systemPortThreadLocal.remove();
        log.info("[CONFIG] ThreadLocal memory cleared completely for Thread ID: {}", threadId);
    }
}