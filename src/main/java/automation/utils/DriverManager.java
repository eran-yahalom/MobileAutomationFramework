package automation.utils;

import io.appium.java_client.android.AndroidDriver;

public class DriverManager {
    private static final ThreadLocal<AndroidDriver> dr = new ThreadLocal<>();

    public static AndroidDriver getDriver() {
        return dr.get();
    }

    public static void setDriver(AndroidDriver driver) {
        dr.set(driver);
    }

    public static void unload() {
        dr.remove();
    }
}