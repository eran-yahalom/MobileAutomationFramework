@navigation
Feature: navigate the app

  Background:
    Given user is successfully logged in
    And user adds product "Sauce Labs Backpack" from products page to cart
    And user clicks on cart icon

  Scenario: click on cancel from checkout:information page will go to products page
    And user clicks on proceed to checkout button
    And user fills in the checkout:information page details
    And click on checkout:information cancel button
    Then user is in the products page

  Scenario: click on continue shopping from cart page will go to products page
    And user clicks on cart page continue shopping button
    Then user is in the products page

  Scenario: click on continue shopping from checkout:information page
    And user clicks on proceed to checkout button
    And user fills in the checkout:information page details
    And user click on checkout:information continue button
    And click on checkout:information cancel button
    Then user is in the products page