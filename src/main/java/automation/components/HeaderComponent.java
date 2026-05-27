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

    @AndroidFindBy(accessibility = "open menu")
    private WebElement menuButton;

    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.widget.ImageView\").instance(3)")
    private WebElement cartButton;

    @AndroidFindBy(accessibility = "sort button")
    private WebElement sortButton;

    @AndroidFindBy(uiAutomator = "new UiSelector().className(\"android.view.ViewGroup\").childSelector(new UiSelector().description(\"open menu\"))")
    private WebElement topHeader;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"cart badge\"]/android.widget.TextView")
    private WebElement cartBadgeCount;

    @AndroidFindBy(accessibility = "nameAsc")
    private WebElement nameAscSortOption;

    @AndroidFindBy(accessibility = "nameDesc")
    private WebElement nameDescSortOption;

    @AndroidFindBy(accessibility = "priceAsc")
    private WebElement priceAscSortOption;

    @AndroidFindBy(accessibility = "priceDesc")
    private WebElement priceDescSortOption;

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
}