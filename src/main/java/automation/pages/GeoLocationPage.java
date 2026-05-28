package automation.pages;

import automation.utils.AndroidSystemHandler;
import automation.utils.ConfigurationsUtils;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebElement;

@Log4j2
public class GeoLocationPage extends BasePage {

    @AndroidFindBy(accessibility = "Stop Observing button")
    private WebElement stopObservingButton;

    @AndroidFindBy(accessibility = "Start Observing button")
    private WebElement startObservingButton;

    @AndroidFindBy(accessibility = "test-latitude")
    private WebElement latitudeData;

    @AndroidFindBy(accessibility = "test-longitude")
    private WebElement longitudeData;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text=\"404\"]")
    private WebElement error404Text;

    @AndroidFindBy(id="com.android.chrome:id/title")
    private WebElement welcomeToChromeText;

    @Inject
    public GeoLocationPage(AppiumDriver driver) {
        super(driver);
    }

    public boolean allowLocationAccess() {
        try {
            AndroidSystemHandler.allowAppLocationAccess(driver);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean clickOnStartObservingButton() {
        return click(startObservingButton);
    }

    public boolean clickOnStopObservingButton() {
        return click(stopObservingButton);
    }

    public boolean clickOnThisLink() {
        try {
            System.out.println("Executing global Android intent to open external browser...");

            // במקום להילחם בלחיצה הפיזית או ב-driver.get השבור,
            // אנחנו פותחים את הקישור הנדרש ישירות בדפדפן של המכשיר
            String targetUrl = "https://appium.io/docs/en/commands/session/geolocation/set-geolocation/";

            driver.executeScript("mobile: startActivity", ImmutableMap.of(
                    "action", "android.intent.action.VIEW",
                    "uri", targetUrl
            ));

            log.info("Successfully launched external browser with URL: {}", targetUrl);
            return true;

        } catch (Exception e) {
            log.error("Failed to launch external browser via intent: {}", e.getMessage());
            return false;
        }
    }

    public String getLatitude() {
        return latitudeData.getText();
    }

    public String getLongitude() {
        return longitudeData.getText();
    }

    public boolean is404ErrorDisplayed() {
        return getText(error404Text).contains("404");
    }

    public boolean isWelcomeToChromeTextVisible(){
        return isDisplayed(welcomeToChromeText) && getText(welcomeToChromeText).equalsIgnoreCase(ConfigurationsUtils.readProperty("welcomeToChrome"));
    }
}