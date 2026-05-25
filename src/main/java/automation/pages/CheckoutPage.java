package automation.pages;

import com.google.inject.Inject;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebElement;

@Log4j2
public class CheckoutPage extends BasePage {

    @AndroidFindBy(accessibility = "Full Name* input field")
    private WebElement fullNameField;

    @AndroidFindBy(accessibility = "Address Line 1* input field")
    private WebElement addressLine1Field;

    @AndroidFindBy(accessibility = "Address Line 2 input field")
    private WebElement addressLine2Field;

    @AndroidFindBy(accessibility = "City* input field")
    private WebElement cityField;

    @AndroidFindBy(accessibility = "State/Region input field")
    private WebElement stateRegionField;

    @AndroidFindBy(accessibility = "Zip Code* input field")
    private WebElement zipCodeField;

    @AndroidFindBy(accessibility = "Country* input field")
    private WebElement countryField;

    @AndroidFindBy(accessibility = "To Payment button")
    private WebElement toPaymentButton;

    @Inject
    public CheckoutPage(AppiumDriver driver) {
        super(driver);
    }

    public boolean enterFullName(String fullName) {
        return enterDetails(fullNameField, fullName);
    }

    public boolean enterAddressLine1(String addressLine1) {
        return enterDetails(addressLine1Field, addressLine1);
    }

    public boolean enterAddressLine2(String addressLine2) {
        return enterDetails(addressLine2Field, addressLine2);
    }

    public boolean enterCity(String city) {
        return enterDetails(cityField, city);
    }

    public boolean enterStateRegion(String stateRegion) {
        return enterDetails(stateRegionField, stateRegion);
    }

    public boolean enterZipCode(String zipCode) {
        return enterDetails(zipCodeField, zipCode);
    }

    public boolean enterCountry(String country) {
        return enterDetails(countryField, country);
    }

    public boolean clickToPaymentButton() {
        try {
            click(toPaymentButton);
            return true;
        } catch (Exception e) {
            log.error("Failed to click To Payment button: {}", e.getMessage());
            return false;
        }
    }

    public boolean scrollToCityField() {
        try {
            scrollToText("Zip Code*");
            return true;
        } catch (Exception e) {
            log.error("Failed to scroll to City field: {}", e.getMessage());
            return false;
        }
    }

    public boolean scrollToEnd(){
        try {
            scrollDown();
            return true;
        } catch (Exception e) {
            log.error("Failed to scroll down: {}", e.getMessage());
            return false;
        }
    }
}
