package automation.components;

import com.google.inject.Inject;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.cucumber.guice.ScenarioScoped;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebElement;

import java.util.List;

import static automation.utils.Utils.delayForMobileDomRefresh;

@Log4j2
@ScenarioScoped
public class MenuComponent extends BaseComponent {

//    @AndroidFindBy(uiAutomator = "new UiSelector().descriptionContains(\"menu item\")")
//    private List<WebElement> allMenuItems;


    @AndroidFindBy(xpath = "//android.widget.TextView[@text='ALL ITEMS' or @text='WEBVIEW' or @text='QR CODE SCANNER' or @text='GEO LOCATION' or @text='DRAWING' or @text='ABOUT' or @text='LOGOUT' or @text='RESET APP STATE']")
    private List<WebElement> allMenuItems;

    @Inject
    public MenuComponent(AppiumDriver driver) {
        super(driver);
    }

//    public boolean clickOnMenuItem(String itemName) {
//        for (WebElement item : allMenuItems) {
//            if (item.getAttribute("content-desc").toLowerCase().contains(itemName.toLowerCase())) {
//                item.click();
//                return true;
//            }
//        }
//        return false;
//    }

    public boolean clickOnMenuItem(String itemName) {
        log.info("Searching for menu item containing: '{}'", itemName);

        // נותנים חצי שנייה לאנימציית פתיחת התפריט להסתיים כדי שהרשימה תיטען במלואה
        delayForMobileDomRefresh();

        for (WebElement item : allMenuItems) {
            // שולפים את הטקסט הפיזי של הפריט (למשל "ABOUT" או "LOGOUT")
            String itemText = item.getText();

            if (itemText != null && itemText.toLowerCase().contains(itemName.toLowerCase())) {
                log.info("Found matching menu item: '{}'. Clicking it...", itemText);
                return click(item); // שימוש במתודת הלחיצה המוגנת שלך
            }
        }

        log.error("Menu item '{}' was not found in the menu list!", itemName);
        return false;
    }
}
