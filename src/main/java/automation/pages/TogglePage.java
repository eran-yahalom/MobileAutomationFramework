package automation.pages;

import automation.utils.Utils;
import com.google.inject.Inject;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

import java.util.List;

public class TogglePage extends BasePage {

    @AndroidFindBy(uiAutomator = "new UiSelector().description(\"test-Item\")")
    private List<WebElement> items;

    @AndroidFindBy(xpath = "//android.widget.ScrollView[@content-desc='test-PRODUCTS']//android.widget.TextView[@content-desc='test-Price']")
    private List<WebElement> prices;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"+\")")
    private List<WebElement> plusIcons;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"\uDB80\uDDDB\")")
    private List<WebElement> dragAndDropIcons;

    @AndroidFindBy(accessibility = "test-Item title")
    private List<WebElement> productsNames;

    @Inject
    public TogglePage(AppiumDriver driver) {
        super(driver);
    }

    //    public boolean clickOnSelectedProductItem(String productName) {
//        // Set<String> uniqueProducts = new HashSet<>();
//
//        for (int i = 0; i < 6; i++) {
//            //   List<WebElement> visibleLabels = driver.findElements(AppiumBy.accessibilityId("test-Item title"));
//
//            for (WebElement label : items) {
//                try {
//                    String name = label.findElement(AppiumBy.accessibilityId("test-Item title")).getText();
//                    if (name.equalsIgnoreCase(productName)) {
//                        label.findElement(AppiumBy.androidUIAutomator("UiSelector().text(\"\uDB80\uDDDB\")")).click();
//                        return true;
//                    }
//                } catch (Exception e) {
//                }
//            }
//
//            if (i == 6 - 1) {
//                break;
//            }
//
//            Utils.scrollUpALittle(driver);
//
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//            }
//        }
//
//        //   log.info("Final count of unique items: {}", uniqueProducts.size());
//        return false;
//    }
    public boolean clickOnAddProductToCart(String productName,int buttonPosition) {
        for (int i = 0; i < 6; i++) {
            try {
                // XPath דינמי: מוצא את ה-ViewGroup שמכיל את שם המוצר המבוקש,
                // וצולל בתוכו ישירות ל-ViewGroup של כפתור הפלוס/עגלה
                String xpathExpression = String.format(
                        "//android.view.ViewGroup[@content-desc='test-Item' and .//android.widget.TextView[@text='%s']]" +
                                "//android.view.ViewGroup[@content-desc='test-ADD TO CART' or @content-desc='test-Drag Handle']",
                        productName
                );

                List<WebElement> targetButtons = driver.findElements(AppiumBy.xpath(xpathExpression));

                // אם נמצא כפתור מתאים על המסך - נקליק עליו ונסיים ברווח
                if (!targetButtons.isEmpty()) {
                    targetButtons.get(buttonPosition).click();
                    return true;
                }
            } catch (Exception e) {
                // הגנה בזמן תנועה/גלילה
            }

            // אם המוצר לא נמצא במסך הנוכחי, נגלול קצת למטה וננסה שוב
            Utils.scrollUpALittle(driver); // (שים לב: אם המתודה גוללת למטה, השם שלה תקין. אם היא גוללת למעלה, וודא שאתה קורא ל-scrollDown)

            try {
                Thread.sleep(500); // הפוגה קלה להתייצבות ה-DOM לאחר גלילה
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return false;
    }

    public boolean clickOnRemoveProductFromCart(String productName) {
        for (int i = 0; i < 6; i++) {
            try {
                // XPath דינמי: מוצא את הכרטיס עם שם המוצר המבוקש,
                // וצולל פנימה רק ל-ViewGroup שמכיל טקסט של מינוס (-)
                String xpathExpression = String.format(
                        "//android.view.ViewGroup[@content-desc='test-Item' and .//android.widget.TextView[@text='%s']]" +
                                "//android.view.ViewGroup[.//android.widget.TextView[@text='-']]",
                        productName
                );

                List<WebElement> removeButtons = driver.findElements(AppiumBy.xpath(xpathExpression));

                // אם נמצא כפתור מינוס מתאים על המסך - נקליק עליו כדי להסיר
                if (!removeButtons.isEmpty()) {
                    removeButtons.getFirst().click();
                    return true;
                }
            } catch (Exception e) {
                // הגנה מפני שינויים דינמיים ב-DOM בזמן תנועה
            }

            // אם המוצר לא נמצא במסך הנוכחי, נגלול כדי לחפש אותו בשאר הקטלוג
            Utils.scrollUpALittle(driver);

            try {
                Thread.sleep(500); // התייצבות קלה של המסך
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return false;
    }

    public boolean isFirstPlusButtonDisplayed() {
        return isDisplayed(plusIcons.getFirst());
    }
}
