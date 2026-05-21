@draw
Feature: Drawing feature to verify drawing actions

  Background:
    Given user is successfully logged in
    And User clicks on menu icon
    And The user navigates to the "Drawing" screen


  Scenario: Verify that the user can draw a shape on the canvas
    When user draws on the drawing pad
    And user clicks on drawing page save button
    Then user should close the save drawing pop up