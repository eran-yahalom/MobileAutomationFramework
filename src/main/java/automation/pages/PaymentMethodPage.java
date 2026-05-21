package automation.pages;

import com.google.inject.Inject;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class PaymentMethodPage extends BasePage {

    @AndroidFindBy(accessibility = "Full Name* input field")
    private WebElement paymentMethodFullNameField;

    @AndroidFindBy(accessibility = "Card Number* input field")
    private WebElement paymentMethodCardNumberField;

    @AndroidFindBy(accessibility = "Expiration Date* input field")
    private WebElement paymentMethodExpirationDateField;

    @AndroidFindBy(accessibility = "Security Code* input field")
    private WebElement paymentMethodSecurityCodeField;

    @AndroidFindBy(accessibility = "Review Order button")
    private WebElement reviewOrderButton;

    @Inject
    public PaymentMethodPage(AppiumDriver driver) {
        super(driver);
    }

    public boolean enterPaymentMethodFullName(String fullName) {
        return enterDetails(paymentMethodFullNameField, fullName);
    }

    public boolean enterPaymentMethodCardNumber(String cardNumber) {
        return enterDetails(paymentMethodCardNumberField, cardNumber);
    }

    public boolean enterPaymentMethodExpirationDate(String expirationDate) {
        return enterDetails(paymentMethodExpirationDateField, expirationDate);
    }

    public boolean enterPaymentMethodSecurityCode(String securityCode) {
        return enterDetails(paymentMethodSecurityCodeField, securityCode);
    }

    public boolean clickReviewOrderButton() {
        return click(reviewOrderButton);
    }
}
