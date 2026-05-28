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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

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

//    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"Username and password do not match any user in this service.\")")
//    @iOSXCUITFindBy(accessibility = "test-Error message")
//    private WebElement loginErrorMessage;

    @iOSXCUITFindBy(accessibility = "test-Error message")
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Error message\"]/android.widget.TextView")
    private WebElement genericErrorMessage;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"locked_out_user\")")
    @iOSXCUITFindBy(accessibility = "test-locked_out_user")
    private WebElement lockedOutUserLink;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"standard_user\")")
    private WebElement standardUserLink;

    @AndroidFindBy(uiAutomator = "new UiSelector().text(\"problem_user\")")
    private WebElement problemUserLink;


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
        return getText(genericErrorMessage);
    }

    public boolean isLoginErrorMessageDisplayed(String errorText) {
        try {
            return getLoginErrorMessage().equals(ConfigurationsUtils.readProperty(errorText));
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

    public boolean clickLockedOutUserLink() {
        try {
            click(lockedOutUserLink);
            return true;
        } catch (Exception e) {
            log.error("Failed to click on locked out user auto-fill: " + e.getMessage());
            return false;
        }
    }

    public boolean clickOnStandardUserLink() {
        return click(standardUserLink);
    }

    public boolean clickOnProblemUserLink() {
        scrollToText("problem_user");
        return click(problemUserLink);
    }

    public boolean checkLoginErrorScenario(String scenario) {
        Map<String, Supplier<Boolean>> errorScenariosMap = new HashMap<>();

        errorScenariosMap.put("locked", () -> isLoginErrorMessageDisplayed("lockedOutErrorMessageText"));
        errorScenariosMap.put("empty details", () -> isLoginErrorMessageDisplayed("emptyUserAndPasswordErrorMessage"));
        errorScenariosMap.put("not active user", () -> isLoginErrorMessageDisplayed("userNotInSystemErrorMessage"));
        errorScenariosMap.put("no user name", () -> isLoginErrorMessageDisplayed("emptyUsernameErrorMessage"));
        errorScenariosMap.put("no password", () -> isLoginErrorMessageDisplayed("emptyPasswordErrorMessage"));
        errorScenariosMap.put("problem user", () -> isLoginErrorMessageDisplayed("problemUserErrorMessage"));

        String cleanKey = scenario.trim().toLowerCase();
        Supplier<Boolean> scenarioAction = errorScenariosMap.get(cleanKey);

        if (scenarioAction == null) {
            log.error("Scenario '{}' is not defined in the error map!", scenario);
            return false;
        }

        log.info("Executing lambda action for login error scenario: '{}'", scenario);
        return scenarioAction.get();
    }
}