@checkout
Feature: Checkout process

  Background:
    Given user is successfully logged in
    And user selects product "Sauce Labs Backpack" from the products list
    And user adds the product to the cart
    And user clicks on cart icon
    And user clicks on proceed to checkout button

  Scenario: successfully checkout with valid information
    When user fills in the checkout information
    And user selects payment method and confirms the order
    Then user places the order and sees the confirmation message
    And user clicks on the continue shopping button
    And user is in the products page

  Scenario Outline: successfully checkout with valid information from json file
    When user fills in checkout information using data from "user_details.json" at index <index>
    And user fills payment method using data from "user_details.json" at index <index>
    And user clicks on the shipping same as billing checkbox
    And user fills the billing addresses
    And user clicks on confirm order button
    Then user places the order and sees the confirmation message
    And user clicks on the continue shopping button
    And user is in the products page

    Examples:
      | index |
      | 0     |