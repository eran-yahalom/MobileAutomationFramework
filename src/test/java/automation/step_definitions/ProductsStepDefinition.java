package automation.step_definitions;

import automation.components.HeaderComponent;
import automation.components.MenuComponent;
import automation.pages.CartPage;
import automation.pages.LoginPage;
import automation.pages.ProductPage;
import automation.pages.ProductsPage;
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

    @Inject
    public ProductsStepDefinition(Provider<LoginPage> loginPageProvider, Provider<HeaderComponent> headerComponentProvider, Provider<MenuComponent> menuComponentProvider, Provider<ProductsPage> productsPageProvider, Provider<ProductPage> productPageProvider, Provider<CartPage> cartPageProvider) {
        this.loginPageProvider = loginPageProvider;
        this.headerComponentProvider = headerComponentProvider;
        this.menuComponentProvider = menuComponentProvider;
        this.productsPageProvider = productsPageProvider;
        this.productPageProvider = productPageProvider;
        this.cartPageProvider = cartPageProvider;
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
        int totalCartItemCount = cartPageProvider.get().getTotalNumberOfItems();
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

    @And("product {string} should be present in the cart")
    public void productShouldBePresentInTheCart(String productName) {
        int expectedCount = ScenarioContext.get("expectedCartItemCount", Integer.class);
        Assert.assertTrue(cartPageProvider.get().isProductInCart(productName, expectedCount),
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
        int currentCount = cartPageProvider.get().getTotalNumberOfItems();
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
        int currentCount = ScenarioContext.get("expectedCartItemCount", Integer.class);
        double totalPrice = cartPageProvider.get().calculateTotalCartPrice(currentCount);
        ScenarioContext.save("expectedTotalPrice", totalPrice);
    }

    @Then("cart total price matches the expected total price")
    public void cartTotalPriceMatchesTheExpectedTotalPrice() {
        double expectedTotalPrice = ScenarioContext.get("expectedTotalPrice", Double.class);
        double actualTotalPrice = cartPageProvider.get().getTotalPrice();
        Assert.assertEquals(actualTotalPrice, expectedTotalPrice, "Cart total price does not match the expected total price");
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
}
