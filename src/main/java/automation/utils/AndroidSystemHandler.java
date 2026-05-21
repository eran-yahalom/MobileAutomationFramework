package automation.utils;

import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

@Slf4j
public class AndroidSystemHandler {

    // ==============================
    // pop up handling (system/regular)
    // ==============================
    public static void closeAlert(AppiumDriver driver) {
        try {
            WebElement button = driver.findElement(By.id("android:id/button2"));
            button.click();
        } catch (Exception e) {
            log.info("pop up not found");
        }
    }

    public static void approveAlert(AppiumDriver driver) {
        try {
            WebElement button = driver.findElement(By.id("android:id/button1"));
            button.click();
        } catch (Exception e) {
            log.info("pop up not found");
        }
    }

    public static void allowAppLocationAccess(AppiumDriver driver) {
        try {
            WebElement allowButton = driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button"));
            allowButton.click();
        } catch (Exception e) {
            log.info("pop up not found");
        }
    }
}