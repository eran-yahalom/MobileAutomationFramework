@geoLocation
Feature: GeoLocation

  Background:
    Given user is successfully logged in
    And User clicks on menu icon
    And The user navigates to the "GEO LOCATION" screen
    And user closes the geo location pop up

#  Scenario: Navigate to this link and verify that we see the chrome page
#    When user clicks on this link
#    Then user should see the chrome web page

  Scenario: See that longitude ant latitude changes as time goes by
    And user waits for 5 seconds
    When user saves the longitude and latitude data as "start"
    And user waits for 5 seconds
    Then user should verify that the longitude and latitude data is not updated after stopping the observation
#
#  Scenario: Start observing button should Start the location updates
#    And user waits for 3 seconds
#    When user clicks on stop observing button
#    When user saves the longitude and latitude data as "start"
#    And user clicks on start observing button
#    And user waits for 7 seconds
#    Then user should verify that the longitude and latitude data is updated after starting the observation
#
#
