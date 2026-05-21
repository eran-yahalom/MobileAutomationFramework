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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Log4j2
public class CartPage extends BasePage {

    @AndroidFindBy(accessibility = "product row")
    private List<WebElement> productRows;

    @AndroidFindBy(accessibility = "product label")
    private List<WebElement> productLabels;

    @AndroidFindBy(accessibility = "total number")
    private WebElement totalNumberLabel;

    @AndroidFindBy(accessibility = "total price")
    private WebElement totalPriceLabel;

    @AndroidFindBy(accessibility = "Proceed To Checkout button")
    private WebElement proceedToCheckoutButton;

    @AndroidFindBy(accessibility = "Go Shopping button")
    private WebElement goShoppingButton;

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

    public boolean removeAllProductsWithScroll() {
        boolean canScrollMore = true;

        while (canScrollMore) {
            List<WebElement> removeButtons = driver.findElements(AppiumBy.accessibilityId("remove item"));

            if (!removeButtons.isEmpty()) {
                int itemsToRemove = removeButtons.size();
                for (int i = 0; i < itemsToRemove; i++) {
                    try {
                        driver.findElement(AppiumBy.accessibilityId("remove item")).click();
                        Thread.sleep(300);
                    } catch (Exception e) {
                        log.info("Moving passed a removed item or an off-screen element, will try next one...");
                    }
                }
            }

            try {
                driver.findElement(AppiumBy.androidUIAutomator(
                        "new UiScrollable(new UiSelector().scrollable(true)).scrollForward()"
                ));
            } catch (Exception e) {
                canScrollMore = false;
            }
        }

        boolean isCartEmpty = driver.findElements(AppiumBy.accessibilityId("remove item")).isEmpty();
        log.info("All items removed successfully: {}", isCartEmpty);

        return isCartEmpty;
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
            return click(proceedToCheckoutButton);
        } catch (Exception e) {
            log.info("Failed to click on Proceed To Checkout button: {}", e.getMessage());
            return false;
        }
    }

    public boolean isProductInCart(String productName, int numberOfItems) {
        return Utils.isProductInCart(driver, productName, numberOfItems);
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
}
