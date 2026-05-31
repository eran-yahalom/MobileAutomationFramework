package automation.pages;

import automation.utils.Utils;
import com.google.inject.Inject;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static automation.utils.Utils.scrollDownALittle;

@Log4j2
public class CheckOutOverViewPage extends BasePage {

    @AndroidFindBy(accessibility = "test-CANCEL")
    private WebElement cancelButton;

    @AndroidFindBy(accessibility = "test-FINISH")
    private WebElement finishButton;

    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text, 'Item total')]")
    private WebElement itemTotalValue;

    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text, 'Tax')]")
    private WebElement totalTaxValue;

    @AndroidFindBy(xpath = "//android.widget.TextView[starts-with(@text, 'Total')]")
    private WebElement totalPriceValue;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Price\"]")
    private List<WebElement> prices;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Payment Information:']/following-sibling::android.widget.TextView")
    private List<WebElement> information;

    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@text, 'SauceCard')]")
    private WebElement creditCardNumber;

    @Inject
    public CheckOutOverViewPage(AppiumDriver driver) {
        super(driver);
    }

    public boolean clickOnCancelButton() {
        return click(cancelButton);
    }

    public boolean clickOnFinishButton() {
        scrollToText("FINISH");
        return click(finishButton);
    }

    public double getTotalCartPriceWithScroll() {
        // מפה למניעת כפילויות בגלילה: [מזהה שורה ייחודי -> (כמות * מחיר)]
        Map<String, Double> productSubtotalsMap = new HashMap<>();

        By itemRowLocator = AppiumBy.accessibilityId("test-Item");
        By footerLocator = AppiumBy.xpath("//android.widget.TextView[contains(@text, 'Sauce Labs. All Rights Reserved.')]");

        for (int i = 0; i < 10; i++) {
            // 1. מוצאים את כל שורות המוצרים שקיימות כרגע במסך
            List<WebElement> productRows = driver.findElements(itemRowLocator);

            for (WebElement row : productRows) {
                try {
                    String rowId = ((org.openqa.selenium.remote.RemoteWebElement) row).getId();

                    // 2. שימוש ב-XPath יחסי מתוך השורה (הנקודה . בתחילת ה-XPath קריטית!)
                    String qtyText = row.findElement(By.xpath(".//android.view.ViewGroup[@content-desc='test-Amount']/android.widget.TextView")).getText().trim();
                    int qty = qtyText.isEmpty() ? 1 : Integer.parseInt(qtyText);

                    String priceText = row.findElement(By.xpath(".//android.view.ViewGroup[@content-desc='test-Price']/android.widget.TextView")).getText().replaceAll("[^0-9.]", "");
                    double price = Double.parseDouble(priceText);

                    // 3. חישוב המכפלה למוצר הספציפי והכנסה למפה
                    productSubtotalsMap.put(rowId, qty * price);

                } catch (Exception ignored) {
                    // הגנה מאלמנטים שחתוכים במסך בזמן גלילה
                }
            }

            // תנאי עצירה: הגענו לסוף העמוד
            if (!driver.findElements(footerLocator).isEmpty() && driver.findElement(footerLocator).isDisplayed()) {
                break;
            }

            Utils.scrollDownALittle(driver);
            Utils.delayForMobileDomRefresh();
        }

        // 4. סכימה של כל ה-Subtotals מתוך המפה
        double totalSum = productSubtotalsMap.values().stream().mapToDouble(Double::doubleValue).sum();
        log.info("Calculated total cart price: ${}", totalSum);
        return java.math.BigDecimal.valueOf(totalSum)
                .setScale(2, java.math.RoundingMode.HALF_UP)
                .doubleValue();
    }

    public String getItemTotal() {
        return getText(itemTotalValue).replaceAll("[^0-9.]", "");
    }

    public String getTax() {
        return getText(totalTaxValue).replaceAll("[^0-9.]", "");
    }

    public String getTotalPrice() {
        return getText(totalPriceValue).replaceAll("[^0-9.]", "");
    }

    public Double getTotalPriceWithTax() {
        Double itemsTotal = Double.valueOf(getItemTotal());
        Double tax = Double.valueOf(getTax());
        return itemsTotal + tax;
    }
}
