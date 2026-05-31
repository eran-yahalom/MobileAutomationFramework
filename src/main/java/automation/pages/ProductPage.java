package automation.pages;

import automation.utils.Utils;
import com.google.inject.Inject;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

@Log4j2
public class ProductPage extends BasePage {

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc='container header']/android.widget.TextView")
    private WebElement productTitle;

    @AndroidFindBy(accessibility = "product price")
    private WebElement productPrice;

    @AndroidFindBy(uiAutomator = "new UiSelector().descriptionContains(\"circle\")")
    private List<WebElement> colorCircles;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[contains(@content-desc, 'circle')]")
    private List<WebElement> colorCircles1;

    @AndroidFindBy(accessibility = "test-ADD TO CART")
    private WebElement addToCartButton;

    @AndroidFindBy(accessibility = "test-REMOVE")
    private WebElement removeButton;

    @AndroidFindBy(accessibility = "counter plus button")
    private WebElement increaseQuantityButton;

    @AndroidFindBy(accessibility = "counter minus button")
    private WebElement decreaseQuantityButton;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"counter amount\"]/android.widget.TextView")
    private WebElement quantityCounter;

    @AndroidFindBy(accessibility = "product description")
    private WebElement productDescription;

    @AndroidFindBy(accessibility = "test-BACK TO PRODUCTS")
    private WebElement backToProductsButton;

    @Inject
    public ProductPage(AppiumDriver driver) {
        super(driver);
    }

    public String getProductTitle() {
        return getText(productTitle);
    }

    public String getProductPrice() {
        return getText(productPrice);
    }

    public boolean selectColor(String colorName) {
        for (WebElement circle : colorCircles) {
            if (getText(circle).equalsIgnoreCase(colorName)) {
                return click(circle);
            }
        }
        return false;
    }

    public boolean addToCart() {
        int maxAttempts = 6;
        By targetButtonLocator = AppiumBy.accessibilityId("test-ADD TO CART");
        By footerLocator = AppiumBy.xpath("//android.widget.TextView[contains(@text, 'Sauce Labs. All Rights Reserved.')]");

        for (int i = 0; i < maxAttempts; i++) {
            // 1. בדיקה האם הכפתור קיים וגלוי כרגע על המסך
            List<WebElement> buttons = driver.findElements(targetButtonLocator);
            if (!buttons.isEmpty() && buttons.get(0).isDisplayed()) {
                log.info("Add To Cart button found on screen after {} scrolls.", i);
                return click(buttons.get(0)); // לחיצה על האלמנט שמצאנו באופן דינמי
            }

            // 2. הגנה: אם הגענו לסוף העמוד (Footer) והכפתור לא שם, אין טעם להמשיך
            if (!driver.findElements(footerLocator).isEmpty() && driver.findElement(footerLocator).isDisplayed()) {
                log.error("Reached the bottom of the page but Add To Cart button was not found.");
                break;
            }

            // 3. הכפתור לא גלוי? גוללים מעט למטה כדי לרענן את ה-DOM ולהביא אותו
            log.info("Button not visible yet, scrolling down... (Attempt {}/{})", i + 1, maxAttempts);
            Utils.scrollDownALittle(driver);
            Utils.delayForMobileDomRefresh();
        }

        log.error("Failed to find or click the Add To Cart button.");
        return false;
    }

    public boolean doCartAction(String action) {
        if (action.equalsIgnoreCase("add")) {
            scrollToText("ADD TO CART");
            delayForMobileDomRefresh();
            return click(addToCartButton);
        } else {
            return click(removeButton);
        }
    }

    public boolean increaseQuantity() {
        return click(increaseQuantityButton);
    }

    public boolean decreaseQuantity() {
        return click(decreaseQuantityButton);
    }

    public int getQuantity() {
        try {
            return Integer.parseInt(getText(quantityCounter));
        } catch (NumberFormatException e) {
            log.error("Failed to parse quantity text to integer: {} | Error: {}", getText(quantityCounter), e.getMessage());
            return 0;
        }

    }

    public Boolean decreaseQuantityOfProduct(int numberOfItemsToDecrease) {
        return Utils.decNumberOfCartItems(driver, numberOfItemsToDecrease);
    }

    public boolean increaseQuantityOfProduct(int numberOfItemsToIncrease) {
        return Utils.incNumberOfCartItems(driver, numberOfItemsToIncrease);
    }

    public boolean isAddToCartButtonEnabled() {
        return isEnabled(addToCartButton);
    }

    public boolean clickOnBackToProductsButton() {
        return click(backToProductsButton);
    }
}
