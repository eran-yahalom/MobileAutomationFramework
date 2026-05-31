package automation.step_definitions;

import automation.components.HeaderComponent;
import automation.components.MenuComponent;
import automation.pages.*;
import automation.utils.ScenarioContext;
import automation.utils.Utils;
import com.google.inject.Inject;
import com.google.inject.Provider;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.List;

@ScenarioScoped
public class ProductsStepDefinition {

    private final Provider<LoginPage> loginPageProvider;
    private final Provider<HeaderComponent> headerComponentProvider;
    private final Provider<MenuComponent> menuComponentProvider;
    private final Provider<ProductsPage> productsPageProvider;
    private final Provider<ProductPage> productPageProvider;
    private final Provider<CartPage> cartPageProvider;
    private final Provider<TogglePage> togglePageProvider;
    private final Provider<CheckOutOverViewPage> checkOutOverViewPageProvider;
    private final Provider<CheckoutInformationPage> checkoutInformationPageProvider;


    @Inject
    public ProductsStepDefinition(Provider<LoginPage> loginPageProvider, Provider<HeaderComponent> headerComponentProvider, Provider<MenuComponent> menuComponentProvider, Provider<ProductsPage> productsPageProvider, Provider<ProductPage> productPageProvider, Provider<CartPage> cartPageProvider, Provider<TogglePage> togglePageProvider, Provider<CheckOutOverViewPage> checkOutOverViewPageProvider, Provider<CheckoutInformationPage> checkoutInformationPageProvider) {
        this.loginPageProvider = loginPageProvider;
        this.headerComponentProvider = headerComponentProvider;
        this.menuComponentProvider = menuComponentProvider;
        this.productsPageProvider = productsPageProvider;
        this.productPageProvider = productPageProvider;
        this.cartPageProvider = cartPageProvider;
        this.togglePageProvider = togglePageProvider;
        this.checkOutOverViewPageProvider = checkOutOverViewPageProvider;

        this.checkoutInformationPageProvider = checkoutInformationPageProvider;
    }

    @And("user selects product {string} from the products list")
    public void userSelectsProductFromTheProductsList(String productName) {
        Assert.assertTrue(productsPageProvider.get().clickOnSelectedProductsPageItem(productName),
                "Failed to select product: " + productName);
    }

    @Then("The user should see the correct price for the product {string}")
    public void theUserShouldSeeTheCorrectPriceForTheProduct(String productName) {
        Assert.assertTrue(productsPageProvider.get().getProductPriceByName(productName),
                "The price for product " + productName + " is incorrect or not visible");
    }

    @Then("user adds the product to the cart")
    public void userAddsTheProductToTheCart() {
        Assert.assertTrue(productPageProvider.get().addToCart(),
                "Failed to add product to cart");
    }

    @And("user counts the number of items in the cart badge")
    public void userCountsTheNumberOfItemsInTheCartBadge() {
        String cartBadgeCount = headerComponentProvider.get().getCartBadgeNumber();
        Assert.assertNotNull(cartBadgeCount, "Cart badge count is null");
        ScenarioContext.save("cartBadgeCount", cartBadgeCount); // שמירת הערך ב-ScenarioContext
    }

    @And("user counts number of items in the cart page")
    public void userCountsNumberOfItemsInTheCartPage() {
        int totalCartItemCount = cartPageProvider.get().countAllCartProducts();
        ScenarioContext.save("expectedCartItemCount", totalCartItemCount);
    }


    @And("user counts number of items in product page")
    public void userCountsNumberOfItemsInProductPage() {
        int quantity = productPageProvider.get().getQuantity();
        ScenarioContext.save("expectedProductAmount", quantity);
    }

    @Then("number of items in product page matches the cart page total count")
    public void numberOfItemsInProductPageMatchesTheCartBadgeCount() {
        int expectedProductAmount = ScenarioContext.get("expectedProductAmount", Integer.class);
        int actualCount = cartPageProvider.get().getTotalNumberOfItems();
        Assert.assertEquals(expectedProductAmount, actualCount, "Number of items in product page does not match the cart count");
    }

    @Then("number of cart total count matches the cart badge count")
    public void numberOfCartTotalCountMatchesTheCartBadgeCount() {
        String cartBadgeCount = ScenarioContext.get("cartBadgeCount", String.class);
        int expectedCount = Integer.parseInt(cartBadgeCount);
        int actualCount = cartPageProvider.get().getTotalNumberOfItems();
        Assert.assertEquals(actualCount, expectedCount, "Cart total count does not match the cart badge count");
    }

    @And("number of items in cart matches the cart total count")
    public void numberOfItemsInCartMatchesTheCartTotalCount() {
        int cartItemsCount = ScenarioContext.get("expectedCartItemCount", Integer.class);
        int BadgeCount = Integer.parseInt(ScenarioContext.get("cartBadgeCount", String.class));
        Assert.assertEquals(cartItemsCount, BadgeCount, "Number of items in cart page does not match the cart badge count");
    }

    @And("number of items in ca")

    @And("product {string} should be present in the cart")
    public void productShouldBePresentInTheCart(String productName) {
        Assert.assertTrue(cartPageProvider.get().isProductInCart(productName),
                "Product " + productName + " is not present in the cart");
    }

    @And("user clicks on cart icon")
    public void userClicksOnCartIcon() {
        Assert.assertTrue(headerComponentProvider.get().clickOnCartIcon(),
                "Failed to click on cart icon");
    }

    @And("user clicks on go back button")
    public void userClicksOnGoBackButton() {
        Assert.assertTrue(cartPageProvider.get().goBack(),
                "Failed to click on go back button");
    }

    @And("user increases the quantity of product {string} in the cart by {int}")
    public void userIncreasesTheQuantityOfProductInTheCartTo(String productName, int quantity) {
        int currentNumberOfItemsInCart = cartPageProvider.get().getTotalNumberOfItems();
        ScenarioContext.save("expectedCartItemCount", currentNumberOfItemsInCart);
        Assert.assertTrue(cartPageProvider.get().increaseNumberOfItemsInCart(productName, quantity, currentNumberOfItemsInCart),
                "Failed to update quantity of product " + productName + " to " + quantity);
    }

    @And("user decreases the quantity of product {string} in the cart by {int}")
    public void userDecreasesTheQuantityOfProductInTheCartTo(String productName, int quantity) {
        int currentNumberOfItemsInCart = cartPageProvider.get().getTotalNumberOfItems();
        ScenarioContext.save("expectedCartItemCount", currentNumberOfItemsInCart);
        Assert.assertTrue(cartPageProvider.get().decreaseNumberOfItemsInCart(productName, quantity, currentNumberOfItemsInCart),
                "Failed to update quantity of product " + productName + " to " + quantity);
    }

    @And("user clicks on proceed to checkout button")
    public void userClicksOnProceedToCheckoutButton() {
        Assert.assertTrue(cartPageProvider.get().clickProceedToCheckout(),
                "Failed to click on proceed to checkout button");
    }

    @And("user removes all items from the cart")
    public void userRemovesAllItemsFromTheCart() {
//        int currentCount = cartPageProvider.get().getTotalNumberOfItems();
        Assert.assertTrue(cartPageProvider.get().removeAllProductsWithScroll(),
                "Failed to remove all items from the cart");
    }

    @And("user verifies that the get go shopping button is displayed")
    public void userVerifiesThatTheGetGoShoppingButtonIsDisplayed() {
        Assert.assertTrue(cartPageProvider.get().isGoShoppingButtonDisplayed(),
                "Go shopping button is not displayed after removing all items from the cart");
    }

    @And("user is in the products page")
    public void userIsInTheProductsPage() {
        Assert.assertTrue(productsPageProvider.get().isPageHeaderCorrect(),
                "User is not on the products page");
    }

    @And("user gets all the products prices and verifies that they are sorted by {string}")
    public void userGetsAllTheProductsPricesAndVerifiesThatTheyAreSortedInAscendingOrder(String sortOption) {
        List<Double> prices = productsPageProvider.get().getAllProductPrices();
        Assert.assertTrue((Utils.isListSorted(sortOption, prices)),
                "Product prices are not sorted in ascending order: " + prices);
    }

    @And("user gets all the products names and verifies that they are sorted by {string}")
    public void userGetsAllTheProductsNamesAndVerifiesThatTheyAreSortedInAscendingOrder(String sortOption) {
        List<String> names = productsPageProvider.get().getAllProductNames();
        Assert.assertTrue((Utils.isListSorted(sortOption, names)),
                "Product prices are not sorted in ascending order: " + names);
    }

    @And("user sort products by {string}")
    public void userSortProductsByPriceFromLowToHigh(String sortOption) {
        Assert.assertTrue(headerComponentProvider.get().selectSortOption(sortOption),
                "Failed to sort products by price from low to high");
    }

    @When("user clicks on sort button")
    public void userClicksOnSortButton() {
        Assert.assertTrue(headerComponentProvider.get().clickOnSortButton(),
                "Failed to click on sort button");
    }

    @And("calculate the total price of all products in the cart")
    public void calculateTheTotalPriceOfAllProductsInTheCartAndVerifyItDoesNotExceed() {
        //  int currentCount = ScenarioContext.get("expectedCartItemCount", Integer.class);
        Double totalPrice = checkOutOverViewPageProvider.get().getTotalCartPriceWithScroll();
        ScenarioContext.save("expectedTotalPrice", totalPrice);
    }

    @And("checkout:information total matches cart total")
    public void checkoutOverViewTotalMatchesCartTotal() {
        Double cartTotal = ScenarioContext.get("expectedTotalPrice", Double.class);
        String pageTotal = checkOutOverViewPageProvider.get().getItemTotal();
        Assert.assertEquals(cartTotal.toString(), pageTotal);
    }

    @And("checkout:information total price contains tax")
    public void checkoutOverViewTotalContainsTax() {
        String totalPrice = checkOutOverViewPageProvider.get().getTotalPrice();
        //  Double itemTotal = ScenarioContext.get("expectedTotalPrice", Double.class);
        Double total = checkOutOverViewPageProvider.get().getTotalPriceWithTax();
        Assert.assertEquals(total.toString(), totalPrice, "Total price got tax");
    }

    @Then("cart total price matches the expected total price")
    public void cartTotalPriceMatchesTheExpectedTotalPrice() {
        double expectedTotalPrice = ScenarioContext.get("expectedTotalPrice", Double.class);
        double actualTotalPrice = checkOutOverViewPageProvider.get().getTotalCartPriceWithScroll();
        Assert.assertEquals(actualTotalPrice, expectedTotalPrice, "Total price got taz in it");
    }

    @And("user decrease number of items in product page by {int}")
    public void userDecreaseNumberOfItemsInProductPageBy(int quantity) {
        Assert.assertTrue(productPageProvider.get().decreaseQuantityOfProduct(quantity),
                "Failed to decrease quantity of product by " + quantity);
    }

    @Then("add to cart button should be disabled")
    public void addToCartButtonShouldBeDisabled() {
        Assert.assertFalse(productPageProvider.get().isAddToCartButtonEnabled(),
                "Add to cart button is not disabled when quantity is decreased to zero");
    }

    @And("user increase number of items in product page by {int}")
    public void userIncreaseNumberOfItemsInProductPageBy(int quantity) {
        Assert.assertTrue(productPageProvider.get().increaseQuantityOfProduct(quantity),
                "Failed to increase quantity of product by " + quantity);
    }

    @Then("user verifies that cart is empty")
    public void cartIsEmpty() {
        int numberOfCartItems = cartPageProvider.get().countNumberOfProductElementsInCart();
        ScenarioContext.save("cartItemsCount", numberOfCartItems);

        Assert.assertEquals(numberOfCartItems, 0,
                "Cart items are not 0, cart is still full");
    }

    @And("user clicks on back to products button")
    public void clickOnBackToProductsButton() {
        Assert.assertTrue(productPageProvider.get().clickOnBackToProductsButton(),
                "Click on back to product button was not successful");
    }

    @Then("number of items in cart badge should be {string}")
    public void numberOfCartBadgeItemsShouldBe(String count) {
        String numberOfItemsInCartBadge = ScenarioContext.get("cartBadgeCount", String.class);

        Assert.assertEquals(numberOfItemsInCartBadge, count, "Cart badge don't match the actual item count ");
    }

    @Then("user performs {string} action for product on product page")
    public void userAddOrRemovesTheProductToTheCart(String action) {
        Assert.assertTrue(productPageProvider.get().doCartAction(action),
                "Failed to add product to cart");
    }

    @Then("user performs {string} action for product on products page")
    public void userAddOrRemoveFromProductsPage(String action) {
        Assert.assertTrue(productsPageProvider.get().doProductsAction(action),
                "Failed to add/remove product from product page to cart");
    }

    @And("user clicks on the toggle icon")
    public void clickOnToggle() {
        Assert.assertTrue(headerComponentProvider.get().clickOnToggle(), "Click on toggle was not successful");
    }

    @Then("user adds product {string} from toggle page to cart")
    public void addProductFromToggleToCart(String productName) {
        Assert.assertTrue(togglePageProvider.get().clickOnAddProductToCart(productName, 0), "Cant add product from toggle to cart");
    }

    @Then("user adds product {string} from products page to cart")
    public void addProductFromProductsPageToCart(String productName) {
        Assert.assertTrue(togglePageProvider.get().clickOnAddProductToCart(productName, 1), "Cant add product from toggle to cart");
    }

    @Then("user removes product {string} from toggle page to cart")
    public void removeProductFromToggleToCart(String productName) {
        Assert.assertTrue(togglePageProvider.get().clickOnRemoveProductFromCart(productName), "Cant add product from toggle to cart");
    }

    @And("user sees the toggle page")
    public void userSeesTogglePage() {
        Assert.assertTrue(togglePageProvider.get().isFirstPlusButtonDisplayed(), "Cant see toggle page");
    }

    @Then("toggle button is disabled")
    public void toggleIsDisabled() {
        Assert.assertFalse(headerComponentProvider.get().isToggleButtonEnabled(), "Toggle button is enabled");
    }

    @And("cart button is {string}")
    public void getCardButtonStatus(String status) {
        Assert.assertTrue(headerComponentProvider.get().isCartButtonEnabled(status), "Cart button status is not correct");
    }

    @And("menu button is {string}")
    public void getMenuButtonStatus(String status) {
        Assert.assertTrue(headerComponentProvider.get().isMenuButtonEnabled(status), "Menu button status is not correct");
    }

    @And("toggle button is {string}")
    public void getToggleButtonStatus(String status) {
        Assert.assertTrue(headerComponentProvider.get().isToggleButtonEnabled(status), "Toggle button status is not correct");
    }

    @Then("number of items in cart should be {int}")
    public void numberOfItemsInCartShouldBe(int count) {
        int countElements = ScenarioContext.get("expectedCartItemCount", Integer.class).intValue();
        Assert.assertEquals(count, countElements, "Number of elements in cart do not match number of added elements");
    }

    @And("click on checkout:information cancel button")
    public void clickOnCancelButton() {
        Assert.assertTrue(checkoutInformationPageProvider.get().clickOnCancelButton());
    }
}
