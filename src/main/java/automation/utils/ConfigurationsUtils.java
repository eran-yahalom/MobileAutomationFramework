package automation.utils;

import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Log4j2
public class ConfigurationsUtils {

    public static String readProperty(String key) {
        Properties prop = new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/test/resources/configuration.properties");
            prop.load(fis);
        } catch (IOException e) {
            log.error("Error: Unable to find the Properties file at the specified path {}", e.getMessage());
            e.printStackTrace();
        }
        return prop.getProperty(key);
    }

    /**
     * קורא ערכים ספציפית מתוך קובץ ההגדרות הגלובלי (global.properties)
     */
    public static String readGlobalProperty(String key) {
        Properties prop = new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/test/resources/global.properties");
            prop.load(fis);
        } catch (IOException e) {
            log.error("Error: Unable to find the Global Properties file at the specified path {}", e.getMessage());
            e.printStackTrace();
        }
        return prop.getProperty(key);
    }

    public static boolean isPageHeaderCorrect(WebElement element, String pageHeader) {
        return element.getText().equalsIgnoreCase(ConfigurationsUtils.readProperty(pageHeader));
    }

    public static String formatDateAsDDMMYYYY(LocalDate departureDate) {
        if (departureDate == null)
            return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        return departureDate.format(formatter);
    }

    public static String extractNumberFromText(WebDriver driver, WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String fullText = wait.until(ExpectedConditions.visibilityOf(element)).getText();

        return fullText.replaceAll("\\D+", "");
    }

    public static boolean addProductToCart(WebDriver driver, List<WebElement> titles, List<WebElement> buttons, String productName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(titles.get(titles.size() - 1)));
        for (int i = 0; i < titles.size(); i++) {
            if (titles.get(i).getText().equalsIgnoreCase(productName)) {
                buttons.get(i).click();
                return true;
            }
        }
        return false;
    }

    public static boolean isMessageDisplayedCorrectly(WebDriver driver, WebElement element, String propertyKey) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(element));
            return element.getText().equalsIgnoreCase(readProperty(propertyKey));
        } catch (TimeoutException e) {
            log.info("Element not found within the specified timeout: {}", e.getMessage());
            return false;
        }
    }

    public static List<Double> getPricesFromUI(List<WebElement> priceElements) {

        List<Double> prices = new ArrayList<>();

        for (WebElement element : priceElements) {
            String priceText = element.getText()
                    .replaceAll("[^0-9.]", "");
            prices.add(Double.parseDouble(priceText));
        }
        return prices;
    }

    public static List<String> getTitleNamesFromUI(List<WebElement> nameElements) {

        List<String> names = new ArrayList<>();

        for (WebElement element : nameElements) {
            String priceText = element.getText().trim();

            names.add(priceText);
        }
        return names;
    }

    public static List<Double> verifyPricesSortedHighToLow(List<Double> actualPrices) {

        List<Double> expectedPrices = new ArrayList<>(actualPrices);
        expectedPrices.sort(Comparator.reverseOrder());
        return expectedPrices;
    }

    public static List<Double> verifyPricesSortedLowToHigh(List<Double> actualPrices) {

        List<Double> expectedPrices = new ArrayList<>(actualPrices);
        expectedPrices.sort(Comparator.naturalOrder());
        return expectedPrices;
    }

    public static List<String> getProducesNamesFromUI(List<WebElement> productNameElements) {

        List<String> productNames = new ArrayList<>();
        for (WebElement element : productNameElements) {
            String productName = element.getText();
            productNames.add(productName);
        }
        return productNames;
    }

    public static boolean verifyNameSortedZToA(List<WebElement> actualNames) {
        List<String> actualTitles = getTitleNamesFromUI(actualNames);
        List<String> sortedTitles = new ArrayList<>(actualTitles);
        sortedTitles.sort(Collections.reverseOrder());

        return actualTitles.equals(sortedTitles);
    }

    public static boolean verifyNameSortedAToZ(List<WebElement> actualNames) {
        List<String> actualTitles = getTitleNamesFromUI(actualNames);
        List<String> sortedTitles = new ArrayList<>(actualTitles);
        Collections.sort(sortedTitles);

        return actualTitles.equals(sortedTitles);
    }

    public static boolean areItemsDisplayedAInCorrectOrder(List<WebElement> elements) {
        int size = elements.size();
        return size > 0;
    }

    public int getPriceMaxValue(List<WebElement> priceElements) {
        List<Double> prices = getPricesFromUI(priceElements);
        return (int) Collections.max(prices).doubleValue();
    }

    public int getPriceMinValue(List<WebElement> priceElements) {
        List<Double> prices = getPricesFromUI(priceElements);
        return (int) Collections.min(prices).doubleValue();
    }

    public static boolean isSearchedItemInCategoryItems(WebDriver driver, List<WebElement> titles, String productName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfAllElements(titles));
        for (int i = 0; i < titles.size(); i++) {
            String pageTitlesFromUI = titles.get(i).getText().toLowerCase();
            if (!pageTitlesFromUI.contains(productName.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    public static int getNumberOfProductsTitles(List<WebElement> productNameElementsTitles) {
        return productNameElementsTitles.size();
    }

    public static List<String> getProductsTitles(List<WebElement> elements) {
        List<String> titles = new ArrayList<>();
        for (WebElement titleElement : elements) {
            titles.add(titleElement.getText().trim());
        }
        return titles;
    }
}


