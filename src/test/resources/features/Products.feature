@products
Feature: Products page

  Background:
    Given user is successfully logged in

#  Scenario: User deletes product from the cart
#    When user selects product "Sauce Labs Bike Light" from the products list
#    And user adds the product to the cart
#    And user clicks on cart icon
#    And user removes all items from the cart
#    Then user verifies that cart is empty
#
#  Scenario: add product to cart from products page
#    When user performs "add" action for product on products page
#    And user counts the number of items in the cart badge
#    Then number of items in cart badge should be "1"
#
#  Scenario: remove product to cart from products page
#    When user performs "add" action for product on products page
#    When user performs "remove" action for product on products page
#    And user counts the number of items in the cart badge
#    Then number of items in cart badge should be "0"
#
#  Scenario: Add product to cart via toggle
#    When user clicks on the toggle icon
#    And user adds product "Test.allTheThings() T-Shirt (Red)" from toggle page to cart
#    And user counts the number of items in the cart badge
#    Then number of items in cart badge should be "1"
#
#  Scenario: Remove product to cart via toggle
#    When user clicks on the toggle icon
#   And user adds product "Test.allTheThings() T-Shirt (Red)" from toggle page to cart
#   And user removes product "Test.allTheThings() T-Shirt (Red)" from toggle page to cart
#    And user counts the number of items in the cart badge
#    Then  number of items in cart badge should be "0"
#
#  Scenario Outline: sorted products by price
#    When user clicks on sort button
#    And user sort products by "<sortOption>"
#     Then user gets all the products prices and verifies that they are sorted by "<sortOption>"
#
#    Examples:
#      | sortOption          |
#      | Price (low to high) |
#      | Price (high to low) |
#
#  Scenario Outline: sorted products by name
#    When user clicks on sort button
#    And user sort products by "<sortOption>"
#    Then user gets all the products names and verifies that they are sorted by "<sortOption>"
#
#    Examples:
#      | sortOption         |
#      | name (low to high) |
#      | name (high to low) |

#  Scenario Outline: sorted products by name
#    When user clicks on sort button
#    And user sort products by "<sortOption>"
#    And user clicks on the toggle icon
#    Then user gets all the products names and verifies that they are sorted by "<sortOption>"
#
#    Examples:
#      | sortOption         |
#      | name (low to high) |
#      | name (high to low) |

 # Scenario Outline: sorted products by name
#    When user clicks on sort button
#    And user sort products by "<sortOption>"
#    And user clicks on the toggle icon
#    Then user gets all the products names and verifies that they are sorted by "<sortOption>"
#
 #   Examples:
#      | sortOption          |
#      | Price (low to high) |
#      | Price (high to low) |

#  Scenario: Toggle icon works as expected
#    When user clicks on the toggle icon
#    And user sees the toggle page
#    And user clicks on the toggle icon
#    Then user is in the products page

#  Scenario: cart,menu and toggle are enabled when entering app
#    Given cart button is "enabled"
#    And menu button is "enabled"
#    And toggle button is "enabled"
#
#  Scenario: cart,menu and toggle are disabled when opening the sort button
#    When user clicks on sort button
#    Then cart button is "disabled"
#    And menu button is "disabled"
#    And toggle button is "disabled"

#  Scenario: Product is added successfully to the cart
#    When user selects product "Sauce Labs Onesie" from the products list
#    And user adds the product to the cart
#    And user clicks on cart icon
#    Then product "Sauce Labs Onesie" should be present in the cart

#  Scenario: User added to cart product from products page
#    When user selects product "Sauce Labs Backpack" from the products list
#    And user adds the product to the cart
#    And user clicks on cart icon
#    And user counts number of items in the cart page
#    Then number of items in cart should be 1

#  Scenario: add product from products page and count them in cart
#    And user adds product "Test.allTheThings() T-Shirt (Red)" from products page to cart
#    And user clicks on cart icon
#    And user counts number of items in the cart page
#    Then number of items in cart should be 1

#  Scenario: add products from products page and count them in cart
#    When user adds product "Sauce Labs Backpack" from products page to cart
#    And user adds product "Test.allTheThings() T-Shirt (Red)" from products page to cart
#    And user clicks on cart icon
#    And user counts number of items in the cart page
#    Then number of items in cart should be 2


#  Scenario: cart items total price matches checkout overview page total price
#    When user adds product "Sauce Labs Backpack" from products page to cart
#    And user adds product "Test.allTheThings() T-Shirt (Red)" from products page to cart
#    And user clicks on cart icon
#    And user clicks on proceed to checkout button
#    And user fills in the checkout:information page details
#    And user click on checkout:information continue button
#    And calculate the total price of all products in the cart
#    Then checkout:information total matches cart total

#  Scenario: checkout:information total price contains tax
#    When user adds product "Sauce Labs Backpack" from products page to cart
#    And user adds product "Test.allTheThings() T-Shirt (Red)" from products page to cart
#    And user clicks on cart icon
#    And user clicks on proceed to checkout button
#    And user fills in the checkout:information page details
#    And user click on checkout:information continue button
#    And calculate the total price of all products in the cart
#    Then checkout:information total price contains tax











  


#  Scenario: Product is added successfully to the cart
#    When user selects product "Sauce Labs Onesie" from the products list
#    And user adds the product to the cart
#    And user clicks on cart icon
#    And user counts number of items in the cart page
#    Then product "Sauce Labs Onesie" should be present in the cart
#
#  Scenario: Number of items in cart matches the cart total count
#    When user selects product "Sauce Labs Fleece Jacket" from the products list
#    And user adds the product to the cart
#    And user clicks on cart icon
#    And user counts number of items in the cart page
#    And user counts the number of items in the cart badge
#    Then number of cart total count matches the cart badge count
#
#  Scenario: user increases the quantity of a product in the cart
#    When user selects product "Sauce Labs Bolt T-Shirt" from the products list
#    And user adds the product to the cart
#    And user clicks on cart icon
#    And user increases the quantity of product "Sauce Labs Bolt T-Shirt" in the cart by 2
#    And user counts number of items in the cart page
#    And user counts the number of items in the cart badge
#    Then number of cart total count matches the cart badge count
#

#
#  Scenario Outline: sorted products by price
#    When user clicks on sort button
#    And user sort products by "<sortOption>"
#    Then user gets all the products prices and verifies that they are sorted by "<sortOption>"
#
#    Examples:
#      | sortOption          |
#      | Price (low to high) |
#      | Price (high to low) |
#
#  Scenario Outline: sorted products by name
#    When user clicks on sort button
#    And user sort products by "<sortOption>"
#    Then user gets all the products names and verifies that they are sorted by "<sortOption>"
#
#    Examples:
#      | sortOption         |
#      | name (low to high) |
#      | name (high to low) |
#
#  Scenario Outline: user increases the quantity of a product in the cart
#    When user selects product "<firstItem>" from the products list
#    And user adds the product to the cart
#    And user clicks on go back button
#    When user selects product "<secondItem>" from the products list
#    And user adds the product to the cart
#    And user clicks on cart icon
#    And user increases the quantity of product "Sauce Labs Bike Light" in the cart by <incItemBy>
#    And calculate the total price of all products in the cart
#    Then cart total price matches the expected total price
#
#    Examples:
#      | firstItem               | secondItem              | incItemBy |
#      | Sauce Labs Bolt T-Shirt | Sauce Labs Bike Light   | 2         |
#      | Sauce Labs Bike Light   | Sauce Labs Bolt T-Shirt | 4         |
#
#  Scenario: user can deletes product from the cart by decreasing the quantity to 0
#    When user selects product "Sauce Labs Bolt T-Shirt" from the products list
#    And user adds the product to the cart
#    And user clicks on cart icon
#    And user decreases the quantity of product "Sauce Labs Bolt T-Shirt" in the cart by 1
#    And calculate the total price of all products in the cart
#    And cart total price matches the expected total price
#    Then user verifies that the get go shopping button is displayed
#
#  Scenario: user can deletes product from the cart by decreasing the quantity to 0
#    When user selects product "Sauce Labs Bolt T-Shirt" from the products list
#    And user decrease number of items in product page by 1
#    Then add to cart button should be disabled
#
#  Scenario: increase the quantity of a product in product page will increase the total products in the cart
#    When user selects product "Sauce Labs Bolt T-Shirt" from the products list
#    And user increase number of items in product page by 2
#    And user counts number of items in product page
#    And user adds the product to the cart
#    And user clicks on cart icon
#    And user counts number of items in the cart page
#    Then number of items in product page matches the cart page total count
#    And user counts the number of items in the cart badge
#    Then number of cart total count matches the cart badge count