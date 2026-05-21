@Login
Feature: Login Functionality

  Background:
    Given User clicks on menu icon

  Scenario: Success login with valid credentials
    Given The user navigates to the "log in" screen
    When The user enters email "bob@example.com" and password "10203040"
    Then The user should see the products page header

  Scenario Outline: Unsuccessful login with invalid credentials
    Given The user navigates to the "log in" screen
    When The user enters email "<email>" and password "<password>"
    Then User see the login error message

    Examples:
      | email             | password |
      | abc@yopmail.com   | 123456   |
      | alice@example.com | ddddddd  |

  Scenario: Test that in order to purchase a product, the user must be logged in
    Given The user navigates to the "catalog" screen
    When user selects product "Test.allTheThings() T-Shirt" from the products list
    And user adds the product to the cart
    And user clicks on cart icon
    And user clicks on proceed to checkout button
    Then user should see the login screen

  Scenario: Locked out user should not be able to log in
    Given The user navigates to the "log in" screen
    When user clicks on the locked account user link
    And user clicks on the login button
    Then locked out error message is displayed