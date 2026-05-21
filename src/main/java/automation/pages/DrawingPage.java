package automation.pages;

import automation.utils.AndroidSystemHandler;
import automation.utils.DrawingUtils;
import com.google.inject.Inject;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

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
        AndroidSystemHandler.closeAlert(driver);
        return true;
    }
}
