package automation.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class BasePage {
    protected AppiumDriver driver;
    protected WebDriverWait wait;

    public BasePage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public boolean isAndroid() {
        if (driver instanceof AndroidDriver) {
            return true;
        }
        String platform = driver.getCapabilities().getPlatformName().toString();
        return platform.equalsIgnoreCase("android");
    }

    /**
     * מחזירה אמת אם הדרייבר הנוכחי של הטרד מריץ iOS
     */
    public boolean isIOS() {
        if (driver instanceof IOSDriver) {
            return true;
        }
        String platform = driver.getCapabilities().getPlatformName().toString();
        return platform.equalsIgnoreCase("ios");
    }

    protected boolean click(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
            return true;
        } catch (Exception e) {
            log.error("Element not visible: {}", e.getMessage());
            return false;
        }
    }

    protected String getText(WebElement element) {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(element)).getText();
        } catch (Exception e) {
            log.error("Element not visible:{} ", e.getMessage());
            return "";
        }
    }

    protected boolean enterDetails(WebElement element, String text) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.clear();
            element.sendKeys(text);
            return true;
        } catch (Exception e) {
            log.error("Element not visible: {}", e.getMessage());
            return false;
        }
    }

    /**
     * גלילה עד לטקסט מסוים - מותאם דינמית גם לאנדרואיד וגם ל-iOS
     */
    protected void scrollToText(String text) {
        String platform = System.getProperty("platform", "android");

        if (platform.equalsIgnoreCase("ios")) {
            try {
                Map<String, Object> args = new HashMap<>();
                args.put("predicateString", "label == '" + text + "' OR value == '" + text + "'");
                args.put("direction", "down");
                driver.executeScript("mobile: scroll", args);
                log.info("Scrolled down to iOS text: {}", text);
            } catch (Exception e) {
                log.error("Failed to scroll to iOS text: {}. Error: {}", text, e.getMessage());
            }
        } else {
            driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true))" +
                            ".scrollIntoView(new UiSelector().text(\"" + text + "\"))"));
        }
    }


    /**
     * גלילה למטה עד שהאלמנט המבוקש הופך לגלוי ובר-קליק על המסך.
     * מונע קריסות של תהליך ה-Instrumentation באנדרואיד.
     * * @param element האלמנט אליו נרצה להגיע
     * @param elementText הטקסט המופיע על האלמנט או ה-accessibility id שלו (עבור אנדרואיד)
     * @return true אם האלמנט נמצא וגלוי
     */
    protected boolean scrollToElement(WebElement element, String elementText) {
        int maxScrollAttempts = 6;

        if (isAndroid()) {
            try {
                // שימוש במנוע הנייטיבי של אנדרואיד - הכי מהיר והכי בטוח מפני קריסות
                driver.findElement(AppiumBy.androidUIAutomator(
                        "new UiScrollable(new UiSelector().scrollable(true))" +
                                ".scrollIntoView(new UiSelector().text(\"" + elementText + "\"))"));
                return element.isDisplayed();
            } catch (Exception e) {
                log.warn("Native Android scroll failed, falling back to safe step-by-step scroll");
            }
        }

        // מנגנון גלילה צעד-צעד בטוח (עבור iOS או כ-Fallback לאנדרואיד)
        for (int i = 0; i < maxScrollAttempts; i++) {
            try {
                if (element.isDisplayed()) {
                    log.info("Element found and displayed after {} scrolls.", i);
                    return true;
                }
            } catch (Exception e) {
                // האלמנט עדיין לא נולד ב-DOM, נמשיך לגלול בלי להתרסק
            }

            log.info("Element not visible yet, performing scroll attempt #{}", (i + 1));

            if (isIOS()) {
                try {
                    Map<String, Object> args = new HashMap<>();
                    args.put("direction", "down");
                    driver.executeScript("mobile: scroll", args);
                } catch (Exception e) {
                    executeW3CScroll();
                }
            } else {
                executeW3CScroll();
            }

            delayForMobileDomRefresh();
        }

        return false;
    }

    /**
     * גלילה חופשית למטה - מנגנון ה-W3C Actions שלך יעבוד מצוין בשתי הפלטפורמות,
     * אך ב-iOS יש חלופה מהירה ואמינה בהרבה דרך סקריפט המערכת.
     */
    protected void scrollDown() {
        String platform = System.getProperty("platform", "android");

        if (platform.equalsIgnoreCase("ios")) {
            try {
                // שימוש ב-put במקום add עבור ה-Map
                Map<String, Object> args = new HashMap<>();
                args.put("direction", "down");
                driver.executeScript("mobile: scroll", args);
            } catch (Exception e) {
                log.error("Failed executing iOS mobile:scroll, falling back to W3C actions");
                executeW3CScroll();
            }
        } else {
            executeW3CScroll();
        }
    }

    /**
     * מנגנון הגלילה המקורי מבוסס ה-W3C Actions שכתבת
     */
    private void executeW3CScroll() {
        int height = driver.manage().window().getSize().getHeight();
        int width = driver.manage().window().getSize().getWidth();
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence scroll = new Sequence(finger, 1);
        scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), width / 2, (int) (height * 0.8)));
        scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        scroll.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), width / 2, (int) (height * 0.2)));
        scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(scroll));
    }

    public void takeScreenshot(String fileName) {
        File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(file, new File("target/screenshots/" + fileName + ".png"));
        } catch (IOException e) {
            log.error("Failed to save screenshot: {}", e.getMessage());
        }
    }

    public boolean goBack() {
        try {
            driver.navigate().back();
            return true;
        } catch (Exception e) {
            log.error("Failed to navigate back: {}", e.getMessage());
            return false;
        }
    }

    public boolean isEnabled(WebElement element) {
        try {
            return wait.until(ExpectedConditions.visibilityOf(element)).isEnabled();
        } catch (Exception e) {
            log.error("Element not visible: {}", e.getMessage());
            return false;
        }
    }

    public boolean isDisplayed(WebElement element) {
        try {
            return wait.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
        } catch (Exception e) {
            log.error("Element not visible: {}", e.getMessage());
            return false;
        }
    }

    protected void delayForMobileDomRefresh() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.info("Thread sleep was interrupted: {}", e.getMessage());
        }
    }

    public void waitForSeconds(int seconds) {
        try {
            java.util.concurrent.TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Wait was interrupted: {}", e.getMessage());
        }
    }
}