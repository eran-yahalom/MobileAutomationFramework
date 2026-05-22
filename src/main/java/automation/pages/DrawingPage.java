package automation.pages;

import automation.utils.AndroidSystemHandler;
import automation.utils.DrawingUtils;
import com.google.inject.Inject;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DrawingPage extends BasePage {

    By pad = AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"drawing screen\"]/android.view.ViewGroup[2]");

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"drawing screen\"]/android.view.ViewGroup[2]")
    private WebElement drawingPad;

    @AndroidFindBy(accessibility = "Save button")
    private WebElement saveButton;

    @AndroidFindBy(accessibility = "Clear button")
    private WebElement clearButton;

    @Inject
    public DrawingPage(AppiumDriver driver) {
        super(driver);
    }

    public boolean clickOnSaveButton() {
        return click(saveButton);
    }

    public boolean clickOnClearButton() {
        return click(clearButton);
    }

    public boolean drow() {
        DrawingUtils.drawOnDrawingPad(driver, drawingPad);
        return true;
    }

    public boolean drowXSignature() {
        DrawingUtils.drawXSignature(driver, pad);
        return true;
    }

    public boolean saveDrawingPopUp() {
        try {
            // המתנה של 3 שניות בלבד לפופ-אפ הדינמי
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));

            // לוקייטורים נפוצים לפופ-אפ של אנדרואיד (אישור שמירת קובץ/מדיה או כפתור אישור כללי)
            By androidOkButton = By.id("android:id/button1"); // כפתור אישור/OK סטנדרטי

            shortWait.until(ExpectedConditions.elementToBeClickable(androidOkButton)).click();
            return true;
        } catch (Exception e) {
            // Fallback למנגנון המקורי שלך אם הלוקייטור הישיר לא עבד
            try {
                AndroidSystemHandler.closeAlert(driver);
                return true;
            } catch (Exception ex) {
                System.out.println("[WARN] Could not bypass drawing pop-up using standard handlers. Proceeding anyway.");
                return false;
            }
        }
    }
}
