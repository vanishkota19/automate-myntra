@amazon
Feature: Amazon Search and Add to Cart
  As a user
  I want to search for products on Amazon
  And add them to my cart

  Background:
    Given I am on the Amazon home page

  @search
  Scenario: Search for a product
    When I search for "tshirt"
    Then I should see search results
    And the search results should contain "tshirt"

  @addToCart
  Scenario: Add a product to cart
    When I search for "tshirt"
    And I click on the first product
    Then I should be on the product page
    When I add the product to cart
    And I navigate to the cart
    Then the product should be in the cart

  @datadriven
  Scenario Outline: Search for different products
    When I search for "<product>"
    Then I should see search results
    And the search results should contain "<product>"

    Examples:
      | product |
      | tshirt  |
      | shoes   |
      | watch   |

  @dynamicLocator
  Scenario: Add a product to cart using dynamic locators
    When I search for "tshirt" using dynamic locator
    And I click on a product containing "tshirt" in the title
    Then I should be on the product page
    When I add the product to cart using dynamic locator
    And I navigate to the cart using dynamic locator
    Then the product should be in the cart 