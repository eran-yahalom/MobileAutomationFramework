package automation.pages;

import automation.utils.Utils;
import com.google.inject.Inject;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.*;

@Log4j2
public class CartPage extends BasePage {

    @AndroidFindBy(accessibility = "test-Item")
    private List<WebElement> products;

    @AndroidFindBy(accessibility = "product label")
    private List<WebElement> productLabels;

    @AndroidFindBy(accessibility = "total number")
    private WebElement totalNumberLabel;

    @AndroidFindBy(accessibility = "total price")
    private WebElement totalPriceLabel;

    @AndroidFindBy(accessibility = "test-CHECKOUT")
    private WebElement checkOutButton;

    @AndroidFindBy(accessibility = "Go Shopping button")
    private WebElement goShoppingButton;

    @AndroidFindBy(accessibility = "test-Amount")
    private List<WebElement> itemsNumber;

    @AndroidFindBy(accessibility = "test-CONTINUE SHOPPING")
    private WebElement continueShoppingButton;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[1]")
    private List<WebElement> productsNames;

    @Inject
    public CartPage(AppiumDriver driver) {
        super(driver);
    }

    public boolean isProductInCart1(String targetName, int numberOfItems) {

        for (int i = 0; i < numberOfItems; i++) {
            List<WebElement> visibleRows = driver.findElements(AppiumBy.accessibilityId("product row"));
            if (visibleRows.isEmpty()) {
                return false;
            }

            WebElement currentRow = visibleRows.get(0);

            String currentHeader = currentRow.findElement(AppiumBy.accessibilityId("product label")).getText();

            if (currentHeader.equalsIgnoreCase(targetName)) {
                log.error("Found product: {}", currentHeader);
                return true;
            }

            // 4. אם זה לא המוצר, ולא הגענו לפריט האחרון - גוללים קצת למטה כדי להביא את הבא בתור
            if (i < numberOfItems - 1) {
                driver.findElement(AppiumBy.androidUIAutomator(
                        "new UiScrollable(new UiSelector().scrollable(true)).scrollForward(25)"
                ));

                // המתנה קצרה לרענון ה-DOM
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        log.info("Product {}'", targetName + "' was not found in cart.");
        return false;
    }

    private void scrollUP30PercentOfScreen() {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence scroll = new Sequence(finger, 1);

        scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), 500, 1200));
        scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

        scroll.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), 500, 700));
        scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(scroll));
    }

    private void addVisibleItemsToSet(Set<String> set) {
        List<WebElement> rows = driver.findElements(AppiumBy.accessibilityId("product row"));
        for (WebElement row : rows) {
            try {
                String name = row.findElement(AppiumBy.accessibilityId("product label")).getText();
                set.add(name);
            } catch (Exception e) {
                log.info("Skipping an off-screen element");
            }
        }
    }

//    public boolean removeAllProductsWithScroll() {
//        boolean canScrollMore = true;
//
//        while (canScrollMore) {
//            List<WebElement> removeButtons = driver.findElements(AppiumBy.accessibilityId("test-REMOVE"));
//
//            if (!removeButtons.isEmpty()) {
//                int itemsToRemove = removeButtons.size();
//                for (int i = 0; i < itemsToRemove; i++) {
//                    try {
//                        driver.findElement(AppiumBy.accessibilityId("test-REMOVE")).click();
//                        Thread.sleep(300);
//                    } catch (Exception e) {
//                        log.info("Moving passed a removed item or an off-screen element, will try next one...");
//                    }
//                }
//            }
//
//            try {
//                driver.findElement(AppiumBy.androidUIAutomator(
//                        "new UiScrollable(new UiSelector().scrollable(true)).scrollForward()"
//                ));
//            } catch (Exception e) {
//                canScrollMore = false;
//            }
//        }
//
//        boolean isCartEmpty = driver.findElements(AppiumBy.accessibilityId("test-REMOVE")).isEmpty();
//        log.info("All items removed successfully: {}", isCartEmpty);
//
//        return isCartEmpty;
//    }


    public boolean removeAllProductsWithScroll() {
        log.info("Starting to remove all products from the cart...");
        boolean keepChecking = true;

        while (keepChecking) {
            // 1. שליפת כפתורי ה-REMOVE שגלויים כרגע על המסך
            List<WebElement> visibleRemoveButtons = driver.findElements(AppiumBy.accessibilityId("test-REMOVE"));

            // 2. כל עוד יש כפתורים על המסך, נמחק תמיד את הכפתור הראשון (הוא תמיד קיים באינדקס 0)
            while (!visibleRemoveButtons.isEmpty()) {
                try {
                    log.info("Removing an item from the cart...");
                    visibleRemoveButtons.get(0).click();

                    // המתנה קצרצרה לריענון ה-DOM הדינמי לאחר מחיקת פריט
                    delayForMobileDomRefresh();
                } catch (Exception e) {
                    log.warn("Failed to click remove button, refreshing list. Message: {}", e.getMessage());
                }
                // עדכון רשימת הכפתורים שנותרו על המסך
                visibleRemoveButtons = driver.findElements(AppiumBy.accessibilityId("test-REMOVE"));
            }

            // 3. אחרי שניקינו את כל מה שגלוי, ננסה לגלול קצת למטה כדי לראות אם יש עוד פריטים
            log.info("No more visible remove buttons, attempting to scroll down for hidden items...");
            boolean didScroll = scrollDownUIAutomator();

            // אם הגלילה נכשלה (הגענו לסוף המסך) ואין יותר כפתורי REMOVE, אפשר לסיים את הלולאה
            if (!didScroll && driver.findElements(AppiumBy.accessibilityId("test-REMOVE")).isEmpty()) {
                log.info("Reached the bottom of the cart screen.");
                keepChecking = false;
            }
        }

        // 4. וולידציה סופית: מוודאים שהעגלה אכן ריקה לחלוטין
        boolean isCartEmpty = driver.findElements(AppiumBy.accessibilityId("test-REMOVE")).isEmpty();
        log.info("All items removed successfully: {}", isCartEmpty);

        return isCartEmpty;
    }

    /**
     * מתודת עזר לביצוע גלילה חכמה באנדרואיד שמחזירה האם הגלילה פיזית הצליחה או הגיעה לסוף
     */
    private boolean scrollDownUIAutomator() {
        try {
            // שימוש ב-UiScrollable בצורה נכונה שמחזירה ערך בוליאני למערכת
            String scrollCommand = "new UiScrollable(new UiSelector().scrollable(true)).scrollForward()";
            Object result = driver.executeScript("mobile: androiduiAutomator", Map.of("expression", scrollCommand));
            return result instanceof Boolean && (Boolean) result;
        } catch (Exception e) {
            return false;
        }
    }

    public int getTotalNumberOfItems() {
        try {
            String totalText = totalNumberLabel.getText();
            String numberOnly = totalText.replaceAll("[^0-9]", "");
            return Integer.parseInt(numberOnly);
        } catch (Exception e) {
            log.info("Failed to parse total number of items: {}", e.getMessage());
            return 0;
        }
    }

    public double getTotalPrice() {
        try {
            String priceText = totalPriceLabel.getText();
            String numberOnly = priceText.replaceAll("[^0-9.]", "");
            return Double.parseDouble(numberOnly);
        } catch (Exception e) {
            log.info("Failed to parse total price: {}", e.getMessage());
            return 0.0;
        }
    }

    public boolean clickProceedToCheckout() {
        try {
            scrollToText("CHECKOUT");
            return click(checkOutButton);
        } catch (Exception e) {
            log.info("Failed to click on Proceed To Checkout button: {}", e.getMessage());
            return false;
        }
    }

    public boolean isProductInCart(String productName) {
        return Utils.isRequestedProductInCart(driver, productName, AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[1]"));
    }

    public int countCartItemsWithExactScroll(int numberOfItems) {
        return Utils.countCartItemsWithExactScroll(driver, numberOfItems - 1);
    }

    public boolean increaseNumberOfItemsInCart(String productName, int numberOfItems, int currentItemsInCartCount) {
        return Utils.incOrDecNumberOfCartItems(driver, productName, currentItemsInCartCount, "inc", numberOfItems);
    }

    public boolean decreaseNumberOfItemsInCart(String productName, int numberOfItems, int currentItemsInCartCount) {
        return Utils.incOrDecNumberOfCartItems(driver, productName, currentItemsInCartCount, "dec", numberOfItems);
    }

    public boolean isGoShoppingButtonDisplayed() {
        return isDisplayed(goShoppingButton) && isEnabled(goShoppingButton);
    }

    public boolean clickGoShoppingButton() {
        try {
            return click(goShoppingButton);
        } catch (Exception e) {
            log.info("Failed to click on Go Shopping button: {}", e.getMessage());
            return false;
        }
    }

    public double calculateTotalCartPrice(int expectedTotalItems) {
        double totalCalculatedCartPrice = 0.0;

        Set<String> processedProducts = new HashSet<>();

        int maxScrolls = 7;

        for (int i = 0; i < expectedTotalItems; i++) {
            List<WebElement> currentVisibleRows = driver.findElements(
                    AppiumBy.xpath("//android.view.ViewGroup[@content-desc='product row']")
            );

            for (WebElement row : currentVisibleRows) {
                try {
                    String productName = row.findElement(AppiumBy.xpath(".//android.widget.TextView[@content-desc='product label']")).getText();

                    if (processedProducts.contains(productName)) {
                        continue;
                    }

                    String priceText = row.findElement(AppiumBy.xpath("//android.widget.TextView[@content-desc=\"product price\"]")).getText();
                    String quantityText = row.findElement(AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"counter amount\"]/android.widget.TextView")).getText();

                    double price = Double.parseDouble(priceText.replaceAll("[^0-9.]", ""));
                    int quantity = Integer.parseInt(quantityText.trim());

                    totalCalculatedCartPrice += (price * quantity);

                    processedProducts.add(productName);

                    log.info(String.format("Calculated: %s | Price: %.2f | Qty: %d", productName, price, quantity));

                } catch (Exception e) {
                    log.info("Skipping a partially visible row, will retry next scroll...");
                }
            }

            if (processedProducts.size() >= expectedTotalItems) {
                log.info("Successfully processed all " + expectedTotalItems + " items.");
                break;
            }

            log.info("Processed {}", processedProducts.size() + " items so far. Scrolling down...");
            Utils.scrollDown(driver);
            Utils.delayForMobileDomRefresh();
        }

        return totalCalculatedCartPrice;
    }

    public int countNumberOfProductElementsInCart() {
        try {
            return products.size();
        } catch (Exception e) {
            log.error("Cant count number of products in cart");
            return 0;
        }
    }

    public int countAllCartProducts() {
        return Utils.sumAllProductQuantitiesWithScroll(driver);
    }

    public boolean clickOnContinueShoppingButton() {
        return click(continueShoppingButton);
    }
}
