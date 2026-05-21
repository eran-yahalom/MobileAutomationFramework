package automation.pages;

import automation.utils.Utils;
import com.google.inject.Inject;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebElement;

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

    @AndroidFindBy(accessibility = "Add To Cart button")
    private WebElement addToCartButton;

    @AndroidFindBy(accessibility = "counter plus button")
    private WebElement increaseQuantityButton;

    @AndroidFindBy(accessibility = "counter minus button")
    private WebElement decreaseQuantityButton;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"counter amount\"]/android.widget.TextView")
    private WebElement quantityCounter;

    @AndroidFindBy(accessibility = "product description")
    private WebElement productDescription;

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
        return click(addToCartButton);
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
}
