@products
Feature: Products page

  Background:
    Given user is successfully logged in

  Scenario: User added to cart product from products page
    When user selects product "Sauce Labs Backpack" from the products list
    And user adds the product to the cart
    And user clicks on cart icon
    And user counts number of items in the cart page
    Then number of items in cart matches the cart total count

  Scenario: Product is added successfully to the cart
    When user selects product "Sauce Labs Onesie" from the products list
    And user adds the product to the cart
    And user clicks on cart icon
    And user counts number of items in the cart page
    Then product "Sauce Labs Onesie" should be present in the cart

  Scenario: Number of items in cart matches the cart total count
    When user selects product "Sauce Labs Fleece Jacket" from the products list
    And user adds the product to the cart
    And user clicks on cart icon
    And user counts number of items in the cart page
    And user counts the number of items in the cart badge
    Then number of cart total count matches the cart badge count

  Scenario: user increases the quantity of a product in the cart
    When user selects product "Sauce Labs Bolt T-Shirt" from the products list
    And user adds the product to the cart
    And user clicks on cart icon
    And user increases the quantity of product "Sauce Labs Bolt T-Shirt" in the cart by 2
    And user counts number of items in the cart page
    And user counts the number of items in the cart badge
    Then number of cart total count matches the cart badge count

  Scenario: User deletes product from the cart
    Given user is successfully logged out
    And User clicks on menu icon
    And The user navigates to the "catalog" screen
    When user selects product "Sauce Labs Bike Light" from the products list
    And user adds the product to the cart
    And user clicks on cart icon
    And user removes all items from the cart
    Then user verifies that the get go shopping button is displayed

  Scenario Outline: sorted products by price
    When user clicks on sort button
    And user sort products by "<sortOption>"
    Then user gets all the products prices and verifies that they are sorted by "<sortOption>"

    Examples:
      | sortOption          |
      | Price (low to high) |
      | Price (high to low) |

  Scenario Outline: sorted products by name
    When user clicks on sort button
    And user sort products by "<sortOption>"
    Then user gets all the products names and verifies that they are sorted by "<sortOption>"

    Examples:
      | sortOption         |
      | name (low to high) |
      | name (high to low) |

  Scenario Outline: user increases the quantity of a product in the cart
    When user selects product "<firstItem>" from the products list
    And user adds the product to the cart
    And user clicks on go back button
    When user selects product "<secondItem>" from the products list
    And user adds the product to the cart
    And user clicks on cart icon
    And user increases the quantity of product "Sauce Labs Bike Light" in the cart by <incItemBy>
    And calculate the total price of all products in the cart
    Then cart total price matches the expected total price

    Examples:
      | firstItem               | secondItem              | incItemBy |
      | Sauce Labs Bolt T-Shirt | Sauce Labs Bike Light   | 2         |
      | Sauce Labs Bike Light   | Sauce Labs Bolt T-Shirt | 4         |

  Scenario: user can deletes product from the cart by decreasing the quantity to 0
    When user selects product "Sauce Labs Bolt T-Shirt" from the products list
    And user adds the product to the cart
    And user clicks on cart icon
    And user decreases the quantity of product "Sauce Labs Bolt T-Shirt" in the cart by 1
    And calculate the total price of all products in the cart
    And cart total price matches the expected total price
    Then user verifies that the get go shopping button is displayed

  Scenario: user can deletes product from the cart by decreasing the quantity to 0
    When user selects product "Sauce Labs Bolt T-Shirt" from the products list
    And user decrease number of items in product page by 1
    Then add to cart button should be disabled

  Scenario: increase the quantity of a product in product page will increase the total products in the cart
    When user selects product "Sauce Labs Bolt T-Shirt" from the products list
    And user increase number of items in product page by 2
    And user counts number of items in product page
    And user adds the product to the cart
    And user clicks on cart icon
    And user counts number of items in the cart page
    Then number of items in product page matches the cart page total count
    And user counts the number of items in the cart badge
    Then number of cart total count matches the cart badge count