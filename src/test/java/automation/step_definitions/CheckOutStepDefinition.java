package automation.step_definitions;

import automation.configurations.EnvManager;
import automation.pages.CheckOutCompletePage;
import automation.pages.CheckoutPage;
import automation.pages.PaymentMethodPage;
import automation.pages.ReviewYourOrderPage;
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

    private final Provider<CheckoutPage> checkoutPageProvider;
    private final Provider<PaymentMethodPage> paymentMethodPageProvider;
    private final Provider<CheckOutCompletePage> checkOutCompletePageProvider;
    private final Provider<ReviewYourOrderPage> reviewYourOrderPageProvider;

    @Inject
    public CheckOutStepDefinition(Provider<CheckoutPage> checkoutPageProvider, Provider<PaymentMethodPage> paymentMethodPageProvider, Provider<CheckOutCompletePage> checkOutCompletePageProvider, Provider<ReviewYourOrderPage> reviewYourOrderPageProvider) {
        this.checkoutPageProvider = checkoutPageProvider;
        this.paymentMethodPageProvider = paymentMethodPageProvider;
        this.checkOutCompletePageProvider = checkOutCompletePageProvider;
        this.reviewYourOrderPageProvider = reviewYourOrderPageProvider;
    }

    @And("user fills in the checkout information")
    public void userFillsInTheCheckoutInformation() {
        Assert.assertTrue(checkoutPageProvider.get().enterFullName(EnvManager.get().getCreditCardHolderName()),
                "Failed to enter first name");
        Assert.assertTrue(checkoutPageProvider.get().enterAddressLine1(faker.address().streetAddress()),
                "Failed to enter address");
        Assert.assertTrue(checkoutPageProvider.get().enterCity(faker.address().city()),
                "Failed to enter city");
        Assert.assertTrue(checkoutPageProvider.get().enterZipCode(faker.address().zipCode()),
                "Failed to enter zip code");
        Assert.assertTrue(checkoutPageProvider.get().enterCountry(faker.address().country()),
                "Failed to enter country");
        Assert.assertTrue(checkoutPageProvider.get().clickToPaymentButton(),
                "Failed to click on payment button");
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

    @And("user clicks on the continue shopping button")
    public void userClicksOnTheContinueShoppingButton() {
        Assert.assertTrue(checkOutCompletePageProvider.get().clickContinueShoppingButton(),
                "Failed to click on continue shopping button");
    }
}