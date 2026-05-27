@checkout
Feature: Checkout process

  Background:
    Given user is successfully logged in
    And user selects product "Sauce Labs Backpack" from the products list
    And user adds the product to the cart
    And user clicks on cart icon
    And user clicks on proceed to checkout button

  Scenario: successfully checkout with valid information
    When user fills in the checkout:information page details
    And user click on checkout:information continue button
    And user click on checkout:overview page finish button
    And user clicks on the checkout:complete back home button
    And user is in the products page

  Scenario Outline: successfully checkout with valid information from json file
    When user fills in checkout information using data from "user_details.json" at index <index>
    And user click on checkout:information continue button
    And user click on checkout:overview page finish button
    And user clicks on the checkout:complete back home button
    And user is in the products page

    Examples:
      | index |
      | 0     |