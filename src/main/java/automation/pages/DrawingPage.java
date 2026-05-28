package automation.pages;

import automation.utils.AndroidSystemHandler;
import automation.utils.DrawingUtils;
import com.google.inject.Inject;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Log4j2
public class DrawingPage extends BasePage {

    // כפתור השמירה הנייטיבי בתחתית המסך
    @AndroidFindBy(accessibility = "test-SAVE")
    private WebElement saveButton;

    // כפתור הניקוי הנייטיבי בתחתית המסך
    @AndroidFindBy(accessibility = "test-CLEAR")
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

    /**
     * מצייר קו ישר במרכז פד הציור על בסיס אחוזי מסך דינמיים
     */
    public boolean drow() {
        log.info("Starting to draw on the pad using screen percentages...");
        delayForMobileDomRefresh(); // מבטיח שהמסך יציב לחלוטין לפני הנגיעה הראשונה

        DrawingUtils.drawOnDrawingPadByScreenSize(driver);
        return true;
    }

    /**
     * מצייר חתימת X מושלמת בתוך גבולות הגזרה של הלוח הפיזי
     */
    public boolean drowXSignature() {
        log.info("Drawing X signature using screen percentages...");
        delayForMobileDomRefresh();

        DrawingUtils.drawXSignatureByScreenSize(driver);
        return true;
    }

    /**
     * מטפל בפופ-אפ אישור השמירה של אנדרואיד
     */
    public boolean saveDrawingPopUp() {
        try {
            // המתנה קצרה לפופ-אפ הדינמי של מערכת ההפעלה
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));

            // מזהה ה-ID הסטנדרטי של כפתור אישור (OK) באנדרואיד
            By androidOkButton = By.id("android:id/button1");

            shortWait.until(ExpectedConditions.elementToBeClickable(androidOkButton)).click();
            log.info("Successfully clicked OK on drawing saved pop-up.");
            return true;
        } catch (Exception e) {
            // במידה והלוקייטור הישיר נכשל, נשתמש במנגנון ה-Fallback המקורי שלך
            try {
                AndroidSystemHandler.closeAlert(driver);
                return true;
            } catch (Exception ex) {
                log.warn("Could not bypass drawing pop-up using standard handlers. Proceeding anyway.");
                return false;
            }
        }
    }
}