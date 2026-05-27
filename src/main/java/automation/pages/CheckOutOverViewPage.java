package automation.pages;

import com.google.inject.Inject;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class CheckOutOverViewPage extends BasePage {

    @AndroidFindBy(accessibility = "test-CANCEL")
    private WebElement cancelButton;

    @AndroidFindBy(accessibility = "test-FINISH")
    private WebElement finishButton;

    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text, 'Item total')]")
    private WebElement itemTotalValue;

    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text, 'Tax')]")
    private WebElement totalTaxValue;

    @AndroidFindBy(xpath = "//android.widget.TextView[starts-with(@text, 'Total')]")
    private WebElement totalPriceValue;

    @Inject
    public CheckOutOverViewPage(AppiumDriver driver) {
        super(driver);
    }

    public boolean clickOnCancelButton() {
        return click(cancelButton);
    }

    public boolean clickOnFinishButton() {
        scrollToText("FINISH");
        return click(finishButton);
    }
}
