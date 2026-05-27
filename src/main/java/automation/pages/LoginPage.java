package automation.pages;

import automation.utils.AndroidSystemHandler;
import automation.utils.ConfigurationsUtils;
import automation.utils.Utils;
import com.google.inject.Inject;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebElement;

@Log4j2
public class LoginPage extends BasePage {

    @AndroidFindBy(accessibility = "test-Username")
    @iOSXCUITFindBy(accessibility = "test-Username")
    private WebElement usernameField;

    @AndroidFindBy(accessibility = "test-Password")
    @iOSXCUITFindBy(accessibility = "test-Password")
    private WebElement passwordField;

    @AndroidFindBy(accessibility = "test-LOGIN")
    @iOSXCUITFindBy(accessibility = "test-LOGIN")
    private WebElement loginButton;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Sorry, this user has been locked out.\")")
    private WebElement lockedOutErrorMessage;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Username and password do not match any user in this service.\")")
    @iOSXCUITFindBy(accessibility = "test-Error message")
    private WebElement loginErrorMessage;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"locked_out_user\")")
    @iOSXCUITFindBy(accessibility = "test-locked_out_user")
    private WebElement lockedOutUserAutoFill;

    @Inject
    public LoginPage(AppiumDriver driver) {
        super(driver);
    }

    public void login(String user, String pass) {
        enterDetails(usernameField, user);
        enterDetails(passwordField, pass);
        click(loginButton);
    }

    public boolean enterUserName(String user) {
        return enterDetails(usernameField, user);
    }

    public boolean enterPassWord(String user) {
        return enterDetails(passwordField, user);
    }

    public boolean clickLoginButton() {
        try {
            click(loginButton);
            return true;
        } catch (Exception e) {
            log.error("Failed to click login button: " + e.getMessage());
            return false;
        }
    }

    public String getLoginErrorMessage() {
        return getText(loginErrorMessage);
    }

    public boolean isLoginErrorMessageDisplayed() {
        try {
            return getLoginErrorMessage().equals(ConfigurationsUtils.readProperty("loginErrorMessageText"));
        } catch (Exception e) {
            log.error("Login error message not found: " + e.getMessage());
            return false;
        }
    }

    public boolean isLoginButtonEnabled() {
        return isEnabled(loginButton) && isDisplayed(loginButton);
    }

    public boolean approveLogOutPopUp() {
        try {
            AndroidSystemHandler.approveAlert(driver);
            return true;

        } catch (Exception e) {
            log.info("No logout confirmation popup found - proceeding without approval.");
            return false;
        }
    }

    public boolean approveSuccessfullyLoggedOutPopUp() {
        try {
            AndroidSystemHandler.approveAlert(driver);
            return true;

        } catch (Exception e) {
            log.info("No logout confirmation popup found - proceeding without approval.");
            return false;
        }
    }

    public boolean isLockedOutErrorMessageDisplayed() {
        return Utils.isErrorMessageCorrect(lockedOutErrorMessage, "lockedOutErrorMessageText");
    }

    public boolean clickLockedOutUserAutoFill() {
        try {
            click(lockedOutUserAutoFill);
            return true;
        } catch (Exception e) {
            log.error("Failed to click on locked out user auto-fill: " + e.getMessage());
            return false;
        }
    }
}