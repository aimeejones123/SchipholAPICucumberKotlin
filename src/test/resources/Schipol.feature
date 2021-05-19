@api
Feature:  Test the Schipol Public API
  As a User
  I want to use the Schipol Public API
  So that I can learn about flight information

  Background:

  Scenario: Get Flight Info and verify IATA codes exist
    Given I have access to the Schipol API
    When all flights leaving Schipol today are retrieved
    Then verify each flight contains IATA codes
