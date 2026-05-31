@Login
Feature: Login Functionality

  Scenario: Success login with valid credentials
    When user clicks on standard user link
    And user clicks on the login button
    Then The user should see the products page header

  Scenario: failed login with locked user credentials
    When user clicks on the locked account user link
    And user clicks on the login button
    Then user see the correct error message for "locked" scenario

  Scenario: failed login with empty user credentials
    When The user enters email "" and password ""
    And user clicks on the login button
    Then user see the correct error message for "empty details" scenario

  Scenario: failed login with empty user credentials
    When The user enters email "abc@yopmail.com" and password "123456"
    And user clicks on the login button
    Then user see the correct error message for "not active user" scenario

  Scenario: failed login with empty user name
    When The user enters email "" and password "secret_sauce"
    And user clicks on the login button
    Then user see the correct error message for "no user name" scenario

  Scenario: failed login with empty password
    When The user enters email "standard_user" and password ""
    And user clicks on the login button
    Then user see the correct error message for "no password" scenario

  Scenario: failed login with problem user
    When user clicks on problem user link
    And user clicks on the login button
    Then user see the correct error message for "no password" scenario

  Scenario: successful log out
    Given  user is successfully logged in
    And user clicks on menu icon
    And The user navigates to the "LOGOUT" screen
    Then user should see the login screen