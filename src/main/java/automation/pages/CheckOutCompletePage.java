package automation.pages;

import com.google.inject.Inject;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class CheckOutCompletePage extends BasePage {

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Checkout Complete\")")
    private WebElement checkoutCompleteTitle;

    @AndroidFindBy(accessibility = "Continue Shopping button")
    private WebElement continueShoppingButton;

    @Inject
    public CheckOutCompletePage(AppiumDriver driver) {
        super(driver);
    }

    public boolean isCheckoutCompleteTitleDisplayed() {
        return isDisplayed(checkoutCompleteTitle);
    }

    public boolean clickContinueShoppingButton() {
        return click(continueShoppingButton);
    }
}
