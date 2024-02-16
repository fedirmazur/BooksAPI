Feature: CRUD operations with Books API

  Background: Setup before each scenario
    Given setup

  @positive @get
  Scenario: Get all books and validate field values
    When user makes GET request for Books API
    Then list of books is returned

  @positive @get
  Scenario:  Get all books response and validate JSON schema
    When user makes GET request for Books API to get response
    Then response matches to JSON schema for all books

  @positive @get
  Scenario: Get a book by id
    When user makes GET request with id 2 to Books API
    Then user gets a book with certain details
      | id | name                                               | author        | publication                 | category    | pages | price |
      | 2  | Refactoring: Improving the Design of Existing Code | Martin Fowler | Addison-Wesley Professional | Programming | 448   | 35.50 |

  @positive @post
  Scenario: Create a new book
    When user makes POST request to create new book with certain details
      | name                                               | author        | publication                 | category    | pages | price |
      | Refactoring: Improving the Design of Existing Code | Martin Fowler | Addison-Wesley Professional | Programming | 448   | 35.50 |
    Then book is created and has all details according to request

  @positive @put
  Scenario: Update a book
    Given user makes POST request to create new book with certain details
      | name                                               | author        | publication                 | category    | pages | price |
      | Refactoring: Improving the Design of Existing Code | Martin Fowler | Addison-Wesley Professional | Programming | 448   | 35.50 |
    When user makes PUT request to update book
      | id | name                                               | author        | publication                 | category    | pages | price |
      | 2  | Refactoring: Improving the Design of Existing Code | Martin Fowler | Addison-Wesley Professional | Programming | 448   | 29.26 |
    When user checks that book is updated

  @positive @delete
  Scenario: Delete a book
    Given user makes POST request to create new book with certain details
      | name                                               | author        | publication                 | category    | pages | price |
      | Refactoring: Improving the Design of Existing Code | Martin Fowler | Addison-Wesley Professional | Programming | 448   | 35.50 |
    When user deletes previously created book
    Then book is deleted

  @negative @get
  Scenario: Get a book with wrong id
    When user makes GET request with invalid id 0 to Books API
    Then user checks that response has 404 status code

  @negative @post
  Scenario: Create a book with wrong data
    When user makes POST request to create new book with invalid request body
    Then user checks that response has 400 status code

  @negative @get
  Scenario: Get all books with invalid credentials
    When user makes GET request for Books API with invalid credentials
    Then user checks that response has 401 status code


