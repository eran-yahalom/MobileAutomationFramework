package automation.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
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

@Slf4j
public abstract class BasePage {
    protected AppiumDriver driver;
    protected WebDriverWait wait;

    public BasePage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
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

    protected void scrollToText(String text) {
        driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().text(\"" + text + "\"))"));
    }

    protected void scrollDown() {
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