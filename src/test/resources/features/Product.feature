@product
Feature: product page tests

  Background:
    Given user is successfully logged in

  Scenario: Go back to products page
    When user selects product "Sauce Labs Bike Light" from the products list
    And user clicks on back to products button
    Then user is in the products page

  Scenario: user adds item to cart successfully
    When user selects product "Sauce Labs Bike Light" from the products list
    And user adds the product to the cart
    And user counts the number of items in the cart badge
    Then number of items in cart badge should be "1"

  Scenario: user removes item from cart successfully
    When user selects product "Sauce Labs Bike Light" from the products list
    And user "add" the product to the cart from product page
    And user "remove" the product to the cart from product page
    And user counts the number of items in the cart badge
    Then number of items in cart badge should be "0"

