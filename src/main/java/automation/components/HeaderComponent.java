package automation.components;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.cucumber.guice.ScenarioScoped;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebElement;

import java.util.Map;

@Log4j2
@ScenarioScoped
public class HeaderComponent extends BaseComponent {

    @AndroidFindBy(accessibility = "test-Menu")
    private WebElement menuButton;

    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.ImageView\").instance(3)")
    private WebElement cartButton;

    @AndroidFindBy(accessibility = "test-Modal Selector Button")
    private WebElement sortButton;

    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.ImageView\").instance(0)")
    private WebElement topHeader;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Cart\"]/android.view.ViewGroup/android.widget.TextView")
    private WebElement cartBadgeCount;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Name (A to Z)\")")
    private WebElement nameAscSortOption;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Name (Z to A)\")")
    private WebElement nameDescSortOption;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Price (low to high)\")")
    private WebElement priceAscSortOption;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Price (high to low)\")")
    private WebElement priceDescSortOption;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Cancel\")")
    private WebElement cancelButton;

    @Inject
    public HeaderComponent(AppiumDriver driver) {
        super(driver);
    }

    public void checkUsernameVisibility() {
        waitForVisibility(menuButton);
    }

    public void checkCartButtonVisibility() {
        waitForVisibility(topHeader);
    }

    public boolean clickOnMenuButton() {
        return click(menuButton);
    }

    public boolean clickOnCartIcon() {
        return click(cartButton);
    }

    public String getCartBadgeNumber() {
        return getText(cartBadgeCount);
    }

    public boolean clickOnSortButton() {
        return click(sortButton);
    }

    public boolean clickOnNameAscSortOption() {
        return click(nameAscSortOption);
    }

    public boolean clickOnNameDescSortOption() {
        return click(nameDescSortOption);
    }

    public boolean clickOnPriceAscSortOption() {
        return click(priceAscSortOption);
    }

    public boolean clickOnPriceDescSortOption() {
        return click(priceDescSortOption);
    }

    public boolean selectSortOption(String sortType) {
        Map<String, java.util.function.Supplier<Boolean>> sortActions = Map.of(
                "name (low to high)", () -> clickOnNameAscSortOption(),
                "name (high to low)", () -> clickOnNameDescSortOption(),
                "Price (low to high)", () -> clickOnPriceAscSortOption(),
                "Price (high to low)", () -> clickOnPriceDescSortOption()
        );

        java.util.function.Supplier<Boolean> action = sortActions.get(sortType);

        if (action != null) {
            try {
                return action.get();
            } catch (Exception e) {
                log.error("Failed to execute sort action for: {}. Error: {}", sortType, e.getMessage());
                return false;
            }
        } else {
            log.info("Unknown sort option type requested: {}", sortType);
            return false;
        }
    }

    public boolean clickOnCancelButton() {
        return click(cancelButton);
    }
}