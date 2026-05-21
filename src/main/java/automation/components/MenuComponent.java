package automation.components;

import com.google.inject.Inject;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.cucumber.guice.ScenarioScoped;
import org.openqa.selenium.WebElement;

import java.util.List;

@ScenarioScoped
public class MenuComponent extends BaseComponent {

    @AndroidFindBy(uiAutomator = "new UiSelector().descriptionContains(\"menu item\")")
    private List<WebElement> allMenuItems;

    @Inject
    public MenuComponent(AppiumDriver driver) {
        super(driver);
    }

    public boolean clickOnMenuItem(String itemName) {
        for (WebElement item : allMenuItems) {
            if (item.getAttribute("content-desc").toLowerCase().contains(itemName.toLowerCase())) {
                item.click();
                return true;
            }
        }
        return false;
    }
}
