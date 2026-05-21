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

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Products\")")
    private WebElement productsHeaderContainer;

    @AndroidFindBy(accessibility = "store item")
    private List<WebElement> productItems;

    @AndroidFindBy(accessibility = "store item text")
    private List<WebElement> productNames;

    @AndroidFindBy(accessibility = "store item price")
    private List<WebElement> productPrices;


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
}
