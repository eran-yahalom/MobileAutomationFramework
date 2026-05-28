package automation.pages;

import automation.utils.ConfigurationsUtils;
import automation.utils.Utils;
import com.google.inject.Inject;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

import java.util.List;


public class ProductsPage extends BasePage {

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"PRODUCTS\")")
    private WebElement productsHeaderContainer;

    @AndroidFindBy(accessibility = "store item")
    private List<WebElement> productItems;

    @AndroidFindBy(accessibility = "store item text")
    private List<WebElement> productNames;

    @AndroidFindBy(accessibility = "store item price")
    private List<WebElement> productPrices;

    @AndroidFindBy(uiAutomator = "new UiSelector().description(\"test-ADD TO CART\").instance(0)")
    private WebElement addToCartButton;

    @AndroidFindBy(uiAutomator = "new UiSelector().description(\"test-REMOVE\")")
    private WebElement removeButton;

    @AndroidFindBy(xpath = "(//android.view.ViewGroup[@content-desc=\"test-Item\"])")
    private List<WebElement> products;

    @AndroidFindBy(xpath = "(//android.view.ViewGroup[@content-desc=\"test-ADD TO CART\"])")
    private List<WebElement> addToCrtButtons;

    @AndroidFindBy(accessibility = "test-REMOVE")
    private List<WebElement> removeButtons;


    @Inject
    public ProductsPage(AppiumDriver driver) {
        super(driver);
    }

    public boolean isPageHeaderCorrect() {
        return getText(productsHeaderContainer).equals(ConfigurationsUtils.readProperty("productsPageHeader"));
    }

    public boolean selectProductByName(String productName) {
        for (int i = 0; i < productNames.size(); i++) {
            String itemNameLowerCase = getText(productNames.get(i)).toLowerCase();
            if (itemNameLowerCase.equals(productName.toLowerCase())) {
                return click(productItems.get(i));
            }
        }
        return false;
    }

    public boolean getProductPriceByName(String productName) {
        for (WebElement item : productItems) {
            if (item.findElement(AppiumBy.accessibilityId("store item text")).getText().equals(productName)) {
                return getText(item).equals(ConfigurationsUtils.readProperty("productPrice"));
            }
        }
        return false;
    }

    public boolean clickOnSelectedProductsPageItem(String productName) {
        return Utils.clickOnSelectedProductFromProductsList(driver, productName, 6);
    }

    public List<Double> getAllProductPrices() {
        return Utils.getAllProductsPrices(driver);
    }

    public List<String> getAllProductNames() {
        return Utils.getAllProductsNames(driver);
    }

    public boolean doProductsAction(String action) {
        if (action.equalsIgnoreCase("add")) {
            return click(addToCrtButtons.getFirst());
        } else {
            return click(removeButtons.getFirst());
        }
    }
}
