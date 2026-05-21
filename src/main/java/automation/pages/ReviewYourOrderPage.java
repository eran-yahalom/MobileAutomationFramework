package automation.pages;

import com.google.inject.Inject;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class ReviewYourOrderPage extends BasePage {

    @AndroidFindBy(accessibility = "Place Order button")
    private WebElement placeOrderButton;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc='checkout payment info']/android.widget.TextView[3]")
    private WebElement paymentMethodNumberText;

    @Inject
    public ReviewYourOrderPage(AppiumDriver driver) {
        super(driver);
    }

    public boolean clickPlaceOrderButton() {
        return click(placeOrderButton);
    }

    public boolean isPaymentMethodHasLastFourDigits(String lastFourDigits) {
        String paymentInfo = getText(paymentMethodNumberText);
        return paymentInfo != null && paymentInfo.endsWith(lastFourDigits);
    }

    public boolean isPaymentMethodHasMaskedCardNumber() {
        String paymentInfo = getText(paymentMethodNumberText);
        return paymentInfo != null && paymentInfo.matches(".*\\*{4}\\d{4}$");
    }
}
