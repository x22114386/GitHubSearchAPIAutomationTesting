Feature: GitHub search repository API

  #01. Fetch repositories for a specific user:
  Scenario: Retrieve GitHub repositories for a specific user
    Given the GitHub API is accessible
    When I make a GET request to "/search/repositories" with the query parameter "user:x22114386"
    Then the response status code should be 200

  #02. Fetching the total number of GitHub repositories for a given programming language:
  Scenario: Retrieve total number of GitHub repositories for a programming language
    Given the GitHub API is accessible
    When I make a GET request to "/search/repositories" with the query parameter "language:java"
    Then the response status code should be 200
    And the response should contain the total_count field representing the number of repositories

  #03. Fetching the total number of GitHub repositories for a given programming language "JAVA" and the user is "x22114386" where the count is "0":
  Scenario: Retrieve total number of GitHub repositories for a programming language
    Given the GitHub API is accessible
    When I make a GET request to "/search/repositories" with the query parameter "language:java user:x22114386"
    Then the response status code should be 200
    And the response should contain the total_count field representing the number of repositories 0

  #04. Filter by a given creation date and name:
  Scenario: Retrieve GitHub repositories created on a specific date with name contains cucumber
    Given the GitHub API is accessible
    When I make a GET request to "/search/repositories" with the query parameter "cucumber in:name created:2024-01-01"
    Then the response status code should be 200
    And the response should contain repositories created on the specified date "2024-01-01" and name contains "cucumber"

  #05. Fetch the most starred GitHub repositories and order the results decreasingly for a particular user
  Scenario: Retrieve most starred GitHub repositories in descending order for a user
    Given the GitHub API is accessible
    When I make a GET request to "/search/repositories" with the query parameters "q=stars:>0 user:x22114386,sort=stars,order=desc"
    Then the response status code should be 200
    And the response should contain repositories with the highest number of stars listed first

  # Boundary Scenarios:
  #06. Maximize Results with Large Page Size:
  Scenario: Retrieve GitHub repositories with a large page size
    Given the GitHub API is accessible
    When I make a GET request to "/search/repositories" with the query parameters "q=language:java,per_page=100"
    Then the response status code should be 200
    And the response should contain repositories with a maximum of 100 results

  #07. Filter by the Latest Possible Creation Date:
  Scenario: Retrieve GitHub repositories created on the latest possible date
    Given the GitHub API is accessible
    When I make a GET request to "/search/repositories" with the query parameter "created:2971-01-01"
    Then the response status code should be 422
    And the response should contain an error message indicating outside the accepted range 1970 to 2970.

  #08. Extensive search
  Scenario Outline: Retrieve GitHub repositories based on search criteria
    Given the GitHub API is accessible
    When I make a GET request to "/search/repositories" with the following query parameters:
      | searchQuery | <searchQuery> |
      | language    | <language>    |
      | created     | <createdDate> |
      | user        | <user>        |
      | sort        | <sortBy>      |
      | order       | <sortOrder>   |
      | per_page    | <perPage>     |
    Then the response status code should be <expectedStatusCode>
    And the response should <expectedResult>

    Examples:
      | searchQuery | language | createdDate | user         | sortBy | sortOrder | perPage | expectedStatusCode | expectedResult                            |
      | spring      | java     | 2021-06-05  | x22114386    | stars  | desc      | 10      | 200                | contain repositories                      |
      | invalid     | python   | 2022-01-01  | invalid_user | forks  | asc       | 5       | 422                | contain "Validation Failed" error message |

  #09. Extensive search
  Scenario Outline: Retrieve GitHub repositories based on search criteria
    Given the GitHub API is accessible
    When I make a GET request to "/search/repositories" with the following query parameters:
      | searchQuery | <searchQuery> |
      | description | <description> |
      | topics      | <topics>      |
      | readme      | <readme>      |
      | sort        | <sortBy>      |
      | order       | <sortOrder>   |
      | per_page    | <perPage>     |
    Then the response status code should be <expectedStatusCode>
    And the response should <expectedResult>

    Examples:
      | searchQuery | description | topics     | readme       | sortBy | sortOrder | perPage | expectedStatusCode | expectedResult                            |
      | spring      | java        | java       | x22114386    | stars  | desc      | 10      | 200                | contain repositories                      |
      | invalid     | python      | 2022-01-01 | invalid_user | forks  | asc       | 5       | 422                | contain "Validation Failed" error message |


  #10. Invalid Date Format:
  Scenario: Retrieve GitHub repositories with an invalid date format
    Given the GitHub API is accessible
    When I make a GET request to "/search/repositories" with the query parameter "created:2022/01/18"
    Then the response status code should be 422
    And the response should contain an error message indicating the date is "not a recognized date/time format"

  #11. Empty Query Parameter:
  Scenario: Retrieve GitHub repositories with an empty query parameter
    Given the GitHub API is accessible
    When I make a GET request to "/search/repositories" with the query parameter ""
    Then the response status code should be 422
    And the response should contain an error message with field "q" and code "missing"


  #12. Invalid API Endpoint:
  Scenario: Access an invalid GitHub API endpoint
    Given the GitHub API is accessible
    When I make a GET request to "/search/invalid-endpoint" with the query parameter "created:2022/01/18"
    Then the response status code should be 404
    And the response should contain an error message indicating the endpoint is "Not Found"



