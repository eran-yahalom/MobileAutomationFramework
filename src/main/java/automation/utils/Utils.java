package automation.utils;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.function.Supplier;

@Log4j2
public class Utils {

    public static void takeScreenshot(WebDriver driver, String screenshotName) {
        TakesScreenshot ts = (TakesScreenshot) driver;

        File source = ts.getScreenshotAs(OutputType.FILE);

        String destinationPath = "target/screenshots/" + screenshotName + ".png";

        try {
            FileUtils.copyFile(source, new File(destinationPath));
            log.info("Screenshot saved to: {}", destinationPath);
        } catch (IOException e) {
            log.info("Failed to capture screenshot: {}", e.getMessage());
        }
    }

    public static boolean isProductInCart(AppiumDriver driver, String productName, int numberOfItems) {
        Set<String> uniqueProducts = new HashSet<>();
        for (int i = 0; i < numberOfItems; i++) {
            List<WebElement> visibleLabels = driver.findElements(AppiumBy.accessibilityId("store item text"));

            for (WebElement label : visibleLabels) {
                try {
                    uniqueProducts.add(label.getText());
                } catch (Exception e) {
                }
            }

            if (i == numberOfItems - 1) {
                break;
            }

            scrollUpALittle(driver);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }

        log.info("Final count of unique items: {}", uniqueProducts.size());
        return uniqueProducts.contains(productName);
    }

    public static boolean removeAllProductsFromCart(AppiumDriver driver, int numberOfItems) {
        try {
            for (int i = 0; i < numberOfItems; i++) {
                WebElement visibleRows = driver.findElement(AppiumBy.accessibilityId("remove item"));
                visibleRows.click();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean incOrDecNumberOfCartItems(AppiumDriver driver, String productName, int numberOfProductItems, String action, int numberOfItemsToIncOrDec) {

        for (int i = 0; i < numberOfProductItems; i++) {
            List<WebElement> visibleRows = driver.findElements(AppiumBy.accessibilityId("product row"));

            for (WebElement row : visibleRows) {
                try {
                    String currentLabelText = row.findElement(AppiumBy.accessibilityId("product label")).getText();

                    if (currentLabelText.equalsIgnoreCase(productName)) {
                        boolean actionResult = false;

                        if (action.equalsIgnoreCase("inc")) {
                            actionResult = incNumberOfCartItems(driver, numberOfItemsToIncOrDec);
                        } else if (action.equalsIgnoreCase("dec")) {
                            actionResult = decNumberOfCartItems(driver, numberOfItemsToIncOrDec);
                        }

                        System.out.println("Action '" + action + "' executed for product: " + productName + " with result: " + actionResult);
                        return actionResult;
                    }
                } catch (Exception e) {
                }
            }

            if (i == numberOfProductItems - 1) {
                break;
            }

            scrollUpALittle(driver);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        log.info("Product: '{}", productName + "' was not found in the cart.");
        return false;
    }

    public static boolean clickOnSelectedProductFromProductsList(AppiumDriver driver, String productName, int numberOfItems) {
        Set<String> uniqueProducts = new HashSet<>();

        for (int i = 0; i < numberOfItems; i++) {
            List<WebElement> visibleLabels = driver.findElements(AppiumBy.accessibilityId("store item text"));

            for (WebElement label : visibleLabels) {
                try {
                    uniqueProducts.add(label.getText());
                    if (label.getText().equalsIgnoreCase(productName)) {
                        label.click();
                        return true;
                    }
                } catch (Exception e) {
                }
            }

            if (i == numberOfItems - 1) {
                break;
            }

            scrollUpALittle(driver);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }

        log.info("Final count of unique items: {}", uniqueProducts.size());
        return false;
    }

    public static int countCartItemsWithExactScroll(AppiumDriver driver, int numberOfItems) {
        Set<String> uniqueProducts = new HashSet<>();

        for (int i = 0; i < numberOfItems; i++) {
            List<WebElement> visibleLabels = driver.findElements(AppiumBy.accessibilityId("product label"));

            for (WebElement label : visibleLabels) {
                try {
                    uniqueProducts.add(label.getText());
                } catch (Exception e) {
                }
            }

            if (i == numberOfItems - 1) {
                break;
            }

            scrollUpALittle(driver);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }

        log.info("Final count of unique items: {}", uniqueProducts.size());
        return uniqueProducts.size();
    }


    // מתודת עזר שמבצעת גלילה קטנה ומבוקרת של כ-30% מהמסך
    public static void scrollUpALittle(AppiumDriver driver) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence scroll = new Sequence(finger, 1);

        scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), 500, 1200));
        scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

        scroll.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), 500, 700));
        scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(scroll));
    }

    public static boolean incNumberOfCartItems(AppiumDriver driver, int numberOfItems) {
        try {
            for (int i = 0; i < numberOfItems; i++) {
                driver.findElement(AppiumBy.accessibilityId("counter plus button")).click();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean decNumberOfCartItems(AppiumDriver driver, int numberOfItems) {
        try {
            for (int i = 0; i < numberOfItems; i++) {
                driver.findElement(AppiumBy.accessibilityId("counter minus button")).click();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isErrorMessageCorrect(WebElement element, String propertyKey) {
        try {
            return element.getText().equals(ConfigurationsUtils.readProperty(propertyKey));
        } catch (Exception e) {
            log.error("Locked out error message not found: {}", e.getMessage());
            return false;
        }
    }

    public static List<Double> getAllProductsPrices1(AppiumDriver driver, int numberOfScrolls) {
        List<Double> capturedPrices = new ArrayList<>();

        for (int i = 0; i < numberOfScrolls; i++) {
            scrollUpALittle(driver);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
            }
            List<WebElement> visiblePrices = driver.findElements(AppiumBy.accessibilityId("store item price"));

            for (WebElement priceElement : visiblePrices) {
                try {
                    String priceText = priceElement.getText(); // למשל "$9.99"

                    String cleanedPrice = priceText.replaceAll("[^0-9.]", "");
                    Double priceVal = Double.parseDouble(cleanedPrice);
                    capturedPrices.add(priceVal);

                } catch (Exception e) {
                }
            }

            if (i == numberOfScrolls - 1) {
                break;
            }

            scrollUpALittle(driver);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
            }
        }

        log.info("Captured prices from UI: {}", capturedPrices);

        List<Double> sortedPrices = new ArrayList<>(capturedPrices);
        return sortedPrices.subList(0, Math.min(sortedPrices.size(), 5));
    }

    public static List<Double> getAllProductsPrices(AppiumDriver driver) {
        List<Double> capturedPrices = new ArrayList<>();
        scrollUpALittle(driver);
        delayForMobileDomRefresh();
        List<WebElement> visiblePrices = driver.findElements(AppiumBy.accessibilityId("store item price"));

        for (WebElement priceElement : visiblePrices) {
            try {
                String priceText = priceElement.getText(); // למשל "$9.99"

                String cleanedPrice = priceText.replaceAll("[^0-9.]", "");
                Double priceVal = Double.parseDouble(cleanedPrice);
                capturedPrices.add(priceVal);
            } catch (Exception e) {
                log.error("Error parsing price: {}", e.getMessage());
            }
        }

        return capturedPrices;
    }

    public static List<String> getAllProductsNames(AppiumDriver driver) {
        List<String> capturedNames = new ArrayList<>();
        scrollUpALittle(driver);
        delayForMobileDomRefresh();
        List<WebElement> visiblePrices = driver.findElements(AppiumBy.accessibilityId("store item text"));

        for (WebElement priceElement : visiblePrices) {
            try {
                String priceText = priceElement.getText();
                capturedNames.add(priceText);
            } catch (Exception e) {
                log.error("Error parsing price: {}", e.getMessage());
            }
        }

        return capturedNames;
    }

    public static <T extends Comparable<? super T>> boolean isListSortedLowToHigh(List<T> list) {
        if (list == null || list.isEmpty()) {
            return true;
        }
        List<T> lowToHighCopy = new ArrayList<>(list);

        Collections.sort(lowToHighCopy);

        return list.equals(lowToHighCopy);
    }

    public static <T extends Comparable<? super T>> boolean isListSortedHighToLow(List<T> list) {
        if (list == null || list.isEmpty()) {
            return true;
        }
        List<T> highToLowCopy = new ArrayList<>(list);

        Collections.sort(highToLowCopy, Comparator.reverseOrder());

        return list.equals(highToLowCopy);
    }

    public static <T extends Comparable<? super T>> boolean isListSorted(String sortType, List<T> actualList) {
        if (actualList == null || actualList.isEmpty()) {
            return true;
        }

        Map<String, Supplier<Boolean>> sortActions = new HashMap<>();

        String normalizedSortType = sortType.toLowerCase().trim();

        sortActions.put("name (low to high)", () -> isListSortedLowToHigh(actualList));
        sortActions.put("name (high to low)", () -> isListSortedHighToLow(actualList));
        sortActions.put("price (low to high)", () -> isListSortedLowToHigh(actualList));
        sortActions.put("price (high to low)", () -> isListSortedHighToLow(actualList));

        Supplier<Boolean> action = sortActions.get(normalizedSortType);

        if (action != null) {
            try {
                return action.get();
            } catch (Exception e) {
                log.error("Failed to verify sort for type: {}. Error: {}", sortType, e.getMessage());
                return false;
            }
        } else {
            log.error("Unknown sort option type requested: {}", sortType);
            return false;
        }
    }

    public static void delayForMobileDomRefresh() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Thread sleep was interrupted: {}", e.getMessage());
        }
    }

    public static void scrollDown(AppiumDriver driver) {
        if (driver == null) {
            log.info("Driver is not initialized in Utils! Call Utils.setDriver(driver) first.");
            return;
        }

        // גילוי מימדי המסך של המכשיר
        Dimension size = driver.manage().window().getSize();
        int startX = size.width / 2;
        int startY = (int) (size.height * 0.7); // מתחילים ב-70% מגובה המסך
        int endY = (int) (size.height * 0.3);   // גוררים למעלה עד 30% מגובה המסך

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence scroll = new Sequence(finger, 1);

        scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        scroll.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), startX, endY));
        scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(List.of(scroll));
    }
}
