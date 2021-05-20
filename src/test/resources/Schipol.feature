@api
Feature:  Test the Schipol Public API
  As a User
  I want to use the Schipol Public API
  So that I can learn about flight information

  Scenario: Try to access API with Invalid key
    Given I don't have access to the Schipol API
    When I try to get flight information
    Then a response code of 403 is returned

  Scenario: Get flight Info and verify IATA codes exist
    Given I have access to the Schipol API
    When all flights leaving Schipol today are retrieved
    Then verify each flight contains IATA codes

  Scenario: Get destination info and verify
    Given I have access to the Schipol API
    When all destinations are retrieved in ascending order by country
    Then verify a destination exists for Sydney, Australia
    And verify all IATA and destination cities exist for Australia