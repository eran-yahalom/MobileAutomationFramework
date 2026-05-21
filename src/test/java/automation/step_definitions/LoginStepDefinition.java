package automation.step_definitions;

import automation.components.HeaderComponent;
import automation.components.MenuComponent;
import automation.configurations.EnvManager;
import automation.pages.LoginPage;
import automation.pages.ProductsPage;
import com.google.inject.Inject;
import com.google.inject.Provider;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

@ScenarioScoped
public class LoginStepDefinition {

    private final Provider<LoginPage> loginPageProvider;
    private final Provider<HeaderComponent> headerComponentProvider;
    private final Provider<MenuComponent> menuComponentProvider;
    private final Provider<ProductsPage> productsPageProvider;

    @Inject
    public LoginStepDefinition(Provider<LoginPage> loginPageProvider,
                               Provider<HeaderComponent> headerComponentProvider, Provider<MenuComponent> menuComponentProvider, Provider<ProductsPage> productsPageProvider) {
        this.loginPageProvider = loginPageProvider;
        this.headerComponentProvider = headerComponentProvider;
        this.menuComponentProvider = menuComponentProvider;
        this.productsPageProvider = productsPageProvider;
    }

    @Given("The user navigates to the {string} screen")
    public void navigateToLogin(String menuItem) {
        Assert.assertTrue(menuComponentProvider.get().clickOnMenuItem(menuItem),
                "Failed to navigate to " + menuItem + " screen");
    }

    @When("The user enters email {string} and password {string}")
    public void login(String user, String pass) {
        Assert.assertTrue(loginPageProvider.get().enterUserName(user));
        Assert.assertTrue(loginPageProvider.get().enterPassWord(pass));
        Assert.assertTrue(loginPageProvider.get().clickLoginButton());
    }

    @Then("The user should see the products page header")
    public void verifyLogin() {
        Assert.assertTrue(productsPageProvider.get().isPageHeaderCorrect(),
                "Products page header is incorrect or not visible");
    }

    @And("User clicks on menu icon")
    public void clickMenuIcon() {
        Assert.assertTrue(headerComponentProvider.get().clickOnMenuButton(),
                "Failed to click on menu icon");
    }

    @And("user is successfully logged in")
    public void userIsSuccessfullyLoggedIn() {
        Assert.assertTrue(headerComponentProvider.get().clickOnMenuButton());
        Assert.assertTrue(menuComponentProvider.get().clickOnMenuItem("log in"));
        Assert.assertTrue(loginPageProvider.get().enterUserName(EnvManager.get().getUsername()));
        Assert.assertTrue(loginPageProvider.get().enterPassWord(EnvManager.get().getPassword()));
        Assert.assertTrue(loginPageProvider.get().clickLoginButton());
    }

    @And("user is successfully logged out")
    public void userIsSuccessfullyLoggedOut() {
        Assert.assertTrue(headerComponentProvider.get().clickOnMenuButton());
        Assert.assertTrue(menuComponentProvider.get().clickOnMenuItem("log out"));
        Assert.assertTrue(loginPageProvider.get().approveLogOutPopUp());
        Assert.assertTrue(loginPageProvider.get().approveSuccessfullyLoggedOutPopUp());
        Assert.assertTrue(loginPageProvider.get().isLoginButtonEnabled());
    }

    @Then("User see the login error message")
    public void userSeeTheLoginErrorMessage() {
        Assert.assertTrue(loginPageProvider.get().isLoginErrorMessageDisplayed(),
                "Login error message is not displayed or does not match expected text");
    }

    @Then("user should see the login screen")
    public void userSeeTheLoginPage() {
        Assert.assertTrue(loginPageProvider.get().isLoginButtonEnabled(),
                "Login button is not enabled or not visible, user might not be on the login page");
    }

    @And("user clicks on the locked account user link")
    public void userClicksOnTheLockedAccountLoginAttempt() {
        Assert.assertTrue(loginPageProvider.get().clickLockedOutUserAutoFill());
    }

    @And("locked out error message is displayed")
    public void lockedOutErrorMessageIsDisplayed() {
        Assert.assertTrue(loginPageProvider.get().isLockedOutErrorMessageDisplayed(),
                "Locked out error message is not displayed or does not match expected text");
    }

    @And("user clicks on the login button")
    public void userClicksOnTheLoginButton() {
        Assert.assertTrue(loginPageProvider.get().clickLoginButton(),
                "Failed to click on login button");
    }
}