Feature: Product Catalog


  Rule: Customers
    Example: the one
      Given Sally is on the home page
      Then she searches for "Adjustable Wrench"
      Then the "Adjustable Wrench" product should be displayed


    Scenario: The one where Sally searches for a more general term
      Given Sally is on the home page
      When she searches for "saw"
      Then the following products should be displayed
        | Product      | Price  |
        | Wood Saw     | $12.18 |
        | Circular Saw | $80.19 |



    Scenario: The one where Sally searches for a product that doesn't exist
      Given Sally is on the home page
      When she searches for "Product-Does-Not-Exist"
      Then no products should be displayed
      And the message "There are no products found." should be displayed

  Rule: Customers should be able to narrow downs their search by category
    Example: The one where Sally only wants to see Hand Saws
      Given Sally is on the home page
      When she searches for "saw"
      And she filters by "Hand Saw"
      Then the following products should be displayed
        | Product  | Price  |
        | Wood Saw | $12.18 |


  Rule: Customers should be able to sort products by various criteria
    Scenario Outline: Sally sorts by different criteria
      Given Sally is on the home page
      When she sorts by "<Sort>"
      Then the first product displayed should be "<First Product>"

      Examples:
        | Sort         | First Product     |
        | Name (A - Z) | Adjustable Wrench |
        | Name (Z - A) | Wood Saw          |