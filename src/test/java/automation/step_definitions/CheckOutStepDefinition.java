package automation.step_definitions;

import automation.configurations.EnvManager;
import automation.pages.*;
import automation.utils.JsonDataLoaderUtils;
import automation.utils.ScenarioContext;
import com.google.inject.Inject;
import com.google.inject.Provider;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import models.testdata.UserDetailsData;
import net.datafaker.Faker;
import org.testng.Assert;

import java.util.List;

@ScenarioScoped
public class CheckOutStepDefinition {

    Faker faker = new Faker();

    private final Provider<CheckoutInformationPage> checkoutPageProvider;
    private final Provider<PaymentMethodPage> paymentMethodPageProvider;
    private final Provider<CheckOutCompletePage> checkOutCompletePageProvider;
    private final Provider<ReviewYourOrderPage> reviewYourOrderPageProvider;
    private final Provider<CheckOutOverViewPage> checkOutOverViewPageProvider;
    private final Provider<CartPage> cartPageProvider;

    @Inject
    public CheckOutStepDefinition(Provider<CheckoutInformationPage> checkoutPageProvider, Provider<PaymentMethodPage> paymentMethodPageProvider, Provider<CheckOutCompletePage> checkOutCompletePageProvider, Provider<ReviewYourOrderPage> reviewYourOrderPageProvider, Provider<CheckOutOverViewPage> checkOutOverViewPageProvider, Provider<CartPage> cartPageProvider) {
        this.checkoutPageProvider = checkoutPageProvider;
        this.paymentMethodPageProvider = paymentMethodPageProvider;
        this.checkOutCompletePageProvider = checkOutCompletePageProvider;
        this.reviewYourOrderPageProvider = reviewYourOrderPageProvider;
        this.checkOutOverViewPageProvider = checkOutOverViewPageProvider;
        this.cartPageProvider = cartPageProvider;
    }

    @And("user fills in the checkout:information page details")
    public void userFillsInTheCheckoutInformation() {
        Assert.assertTrue(checkoutPageProvider.get().enterFirstName(EnvManager.get().getCreditCardHolderName()),
                "Failed to enter first name");
        Assert.assertTrue(checkoutPageProvider.get().enterLastName(faker.address().streetAddress()),
                "Failed to enter address");
        Assert.assertTrue(checkoutPageProvider.get().enterZipCode(faker.address().zipCode()),
                "Failed to enter zip code");
    }

    @And("user selects payment method and confirms the order")
    public void userSelectsPaymentMethodAndConfirmsTheOrder() {
        Assert.assertTrue(paymentMethodPageProvider.get().enterPaymentMethodFullName(EnvManager.get().getCreditCardHolderName()),
                "Failed to enter card holder name");
        Assert.assertTrue(paymentMethodPageProvider.get().enterPaymentMethodCardNumber(EnvManager.get().getCreditCardNumber()),
                "Failed to enter card number");
        Assert.assertTrue(paymentMethodPageProvider.get().enterPaymentMethodExpirationDate(EnvManager.get().getCreditCardExpiry()),
                "Failed to enter card expiration date");
        Assert.assertTrue(paymentMethodPageProvider.get().enterPaymentMethodSecurityCode(EnvManager.get().getCreditCardCvv()),
                "Failed to enter card CVV");
        Assert.assertTrue(paymentMethodPageProvider.get().clickReviewOrderButton(),
                "Failed to click on confirm order button");
    }

    @And("user places the order and sees the confirmation message")
    public void userPlacesTheOrderAndSeesTheConfirmationMessage() {
        Assert.assertTrue(reviewYourOrderPageProvider.get().clickPlaceOrderButton(),
                "Failed to click on place order button");
    }

    @And("user clicks on the checkout:complete back home button")
    public void userClicksOnTheBackHomeButton() {
        Assert.assertTrue(checkOutCompletePageProvider.get().clickContinueShoppingButton(),
                "Failed to click on continue shopping button");
    }

    @When("user fills in checkout information using data from {string} at index {int}")
    public void userFillsInCheckOutInformation(String jsonFile, int index) {

        List<UserDetailsData> UserData = JsonDataLoaderUtils.getSearchData(jsonFile);
        UserDetailsData scenario = UserData.get(index);
        Assert.assertNotNull(UserData, "Test scenarios should not be null");

        String firstName = scenario.getFirstName();
        String lastName = scenario.getLastName();
        String zipCode = scenario.getZipCode();

//        String fullName = scenario.getFullName();
//        String address = scenario.getAddress1();
//        String city = scenario.getCity();
//        String country = scenario.getCountry();
//
//        String expiryDate = scenario.getCardExpiry();
//        String zipCode = scenario.getZipCode();
//        String cardNumber = scenario.getCardNumber();
//        String cvv = scenario.getCardCVV();

//        ScenarioContext.save("fullName", fullName);
//        ScenarioContext.save("address", address);
//        ScenarioContext.save("city", city);
//        ScenarioContext.save("country", country);
//        ScenarioContext.save("expiryDate", expiryDate);
//        ScenarioContext.save("zipCode", zipCode);
//        ScenarioContext.save("cardNumber", cardNumber);
//        ScenarioContext.save("cvv", cvv);


        Assert.assertTrue(checkoutPageProvider.get().enterFirstName(firstName),
                "Failed to enter first name");
        Assert.assertTrue(checkoutPageProvider.get().enterLastName(lastName),
                "Failed to enter address");
        Assert.assertTrue(checkoutPageProvider.get().enterZipCode(zipCode),
                "Failed to enter zip code");
    }

    @And("user fills payment method using data from {string} at index {int}")
    public void userSelectsPaymentMethodAndConfirmsTheOrderUsingDataFromAtIndex(String jsonFile, int index) {
        String fullName = ScenarioContext.get("fullName", String.class);
        String cardNumber = ScenarioContext.get("cardNumber", String.class);
        String expiryDate = ScenarioContext.get("expiryDate", String.class);
        String cvv = ScenarioContext.get("cvv", String.class);

        Assert.assertTrue(paymentMethodPageProvider.get().enterPaymentMethodFullName(fullName),
                "Failed to enter card holder name");
        Assert.assertTrue(paymentMethodPageProvider.get().enterPaymentMethodCardNumber(cardNumber),
                "Failed to enter card number");
        Assert.assertTrue(paymentMethodPageProvider.get().enterPaymentMethodExpirationDate(expiryDate),
                "Failed to enter card expiration date");
        Assert.assertTrue(paymentMethodPageProvider.get().enterPaymentMethodSecurityCode(cvv),
                "Failed to enter card CVV");
    }

    @And("user clicks on the shipping same as billing checkbox")
    public void userClicksOnTheShippingSameAsBillingCheckbox() {
        Assert.assertTrue(paymentMethodPageProvider.get().clickShippingAsBillingAddressCheckBox(),
                "Failed to click on shipping same as billing checkbox");
    }

    @And("user clicks on confirm order button")
    public void userClicksOnConfirmOrderButton() {
        Assert.assertTrue(paymentMethodPageProvider.get().clickReviewOrderButton(),
                "Failed to click on confirm order button");
    }

    @And("user fills the billing addresses")
    public void userFillsTheBillingAddresses() {
        String fullName = ScenarioContext.get("fullName", String.class);
        String address = ScenarioContext.get("address", String.class);
        String city = ScenarioContext.get("city", String.class);
        String country = ScenarioContext.get("country", String.class);
        String zipCode = ScenarioContext.get("zipCode", String.class);

        checkoutPageProvider.get().scrollToEnd();

        Assert.assertTrue(checkoutPageProvider.get().enterFirstName(fullName),
                "Failed to enter first name");
        Assert.assertTrue(checkoutPageProvider.get().enterAddressLine1(address),
                "Failed to enter address");
        Assert.assertTrue(checkoutPageProvider.get().enterCity(city),
                "Failed to enter city");
        Assert.assertTrue(checkoutPageProvider.get().enterZipCode(zipCode),
                "Failed to enter zip code");
        Assert.assertTrue(checkoutPageProvider.get().enterCountry(country),
                "Failed to enter country");
    }

    @And("user click on checkout:overview page finish button")
    public void clickOnCheckOutOverViewFinishButton() {
        Assert.assertTrue(checkOutOverViewPageProvider.get().clickOnFinishButton(), "Cant click on finishButton");
    }

    @And("user click on checkout:information continue button")
    public void userClicksOnCheckoutInformationPageContinueButton() {
        Assert.assertTrue(checkoutPageProvider.get().clickOnContinueButton(),
                "Failed to click on continue button");
    }

    @And("user clicks on cart page continue shopping button")
    public void userClicksOnTheContinueShoppingButton() {
        Assert.assertTrue(cartPageProvider.get().clickOnContinueShoppingButton(),
                "Failed to click on continue shopping button");
    }

}

