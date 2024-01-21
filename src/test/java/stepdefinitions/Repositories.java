package stepdefinitions;


import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Repositories {

    RequestSpecification requestSpecification;

    Response response;

    @Given("the GitHub API is accessible")
    public void the_git_hub_api_is_accessible() {
        RestAssured.baseURI = "https://api.github.com";
    }

    @When("I make a GET request to {string} with the query parameter {string}")
    public void i_make_a_get_request_to_with_the_query_parameter(String endpoint, String queryParameter) {
        requestSpecification = RestAssured.given().param("q", queryParameter);
        response = requestSpecification.get(endpoint);
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(Integer expectedStatusCode) {
        assertEquals(expectedStatusCode.longValue(), response.getStatusCode());
    }

    @And("the response should contain the total_count field representing the number of repositories")
    public void the_response_should_contain_the_total_count_field_representing_the_number_of_repositories() {
        // Parse the JSON response using JsonPath
        JsonPath jsonPath = response.jsonPath();

        // Retrieve the value of the "total_count" field
        int totalCount = jsonPath.getInt("total_count");

        // Assert that the "total_count" field is present and has a non-negative value
        assertTrue("total_count should be greater than or equal to 0", totalCount >= 0);
    }

    @And("the response should contain the total_count field representing the number of repositories {int}")
    public void the_response_should_contain_the_total_count_field_representing_the_number_of_repositories(int expectedTotalCount) {
        // Parse the JSON response using JsonPath
        JsonPath jsonPath = response.jsonPath();

        // Retrieve the value of the "total_count" field
        int actualTotalCount = jsonPath.getInt("total_count");

        // Assert that the "total_count" field is present and has the expected value
        assertEquals("Total count should match the expected value", expectedTotalCount, actualTotalCount);
    }

    @And("the response should contain repositories created on the specified date {string} and name contains {string}")
    public void the_response_should_contain_repositories_created_on_the_specified_date_and_name_contains(String createdDate, String name) {
        // Parse the JSON response using JsonPath
        JsonPath jsonPath = response.jsonPath();

        // Retrieve the list of items from the response
        List<Map<String, Object>> items = jsonPath.getList("items");

        // all item should match the createdDate and contains the name
        boolean repositoriesOnSpecifiedDateAndNameExist = items.stream()
                .allMatch(item -> item.get("created_at").toString().startsWith(createdDate)
                        && StringUtils.containsIgnoreCase(item.get("name").toString(), name));

        // Assert that all the repositories with the specified created date
        assertTrue("All the repository should be created on the specified date and contains the specific name", repositoriesOnSpecifiedDateAndNameExist);
    }

    @When("I make a GET request to {string} with the query parameters {string}")
    public void i_make_a_get_request_to_with_the_query_parameters(String endpoint, String queryParameters) {
        Map<String, String> paramMap = Arrays.stream(queryParameters.split(","))
                .collect(Collectors.toMap(param -> param.split("=")[0], param -> param.split("=")[1]));

        requestSpecification = RestAssured.given().params(paramMap);
        response = requestSpecification.get(endpoint);
    }

    @And("the response should contain repositories with the highest number of stars listed first")
    public void the_response_should_contain_repositories_with_the_highest_number_of_stars_listed_first() {
        // Parse the JSON response using JsonPath
        JsonPath jsonPath = response.jsonPath();

        // Retrieve the list of items from the response
        List<Map<String, Object>> items = jsonPath.getList("items");
        List<Map<String, Object>> sortedItems = new ArrayList<>(items);
        sortedItems.sort( Comparator.comparingInt(item -> (int) ((Map)item).get("stargazers_count")).reversed());
        boolean responseListAndSortedListEqual = items.equals(sortedItems);

        assertTrue("All the repository should be sorted based stars and highest number of stars listed first", responseListAndSortedListEqual);
    }

    @And("the response should contain repositories with a maximum of {int} results")
    public void the_response_should_contain_repositories_with_a_maximum_of_results(Integer numberOfResults) {
        JsonPath jsonPath = response.jsonPath();

        // Retrieve the list of items from the response
        boolean validResponseSize = numberOfResults >= jsonPath.getList("items").size();
        assertTrue("The response contains number of repositories which is equal or less than per_page number", validResponseSize);
    }

    @And("the response should contain an error message indicating outside the accepted range {int} to {int}.")
    public void the_response_should_contain_an_error_message_indicating_outside_the_accepted_range_to(Integer startDate, Integer endDate) {
        JsonPath jsonPath = response.jsonPath();
        List<Map<String, Object>> errors = jsonPath.getList("errors");
        String error = "is outside the accepted range " + startDate + " to " + endDate;
        boolean validError = errors.get(0).get("message").toString().contains(error);
        assertTrue("The response contains an error message indicating outside the accepted range", validError);
    }

    @When("I make a GET request to {string} with the following query parameters:")
    public void i_make_a_get_request_to_with_the_following_query_parameters(String endpoint, Map<String,String> dataMap) {
        String searchQuery = dataMap.get("searchQuery");
        String language = dataMap.get("language");
        String created = dataMap.get("created");
        String user = dataMap.get("user");
        String sort = dataMap.get("sort");
        String order = dataMap.get("order");
        String per_page = dataMap.get("per_page");

        String q = "";
        if(StringUtils.isNotBlank(searchQuery)){
            q = q.concat(searchQuery);
        }
        if(StringUtils.isNotBlank(language)){
            if(StringUtils.isNotBlank(q)){
                q = q.concat(" ");
            }
            q = q.concat("language:").concat(language);
        }
        if(StringUtils.isNotBlank(created)){
            if(StringUtils.isNotBlank(q)){
                q = q.concat(" ");
            }
            q = q.concat("created:").concat(created);
        }
        if(StringUtils.isNotBlank(user)){
            if(StringUtils.isNotBlank(q)){
                q = q.concat(" ");
            }
            q = q.concat("user:").concat(user);
        }

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("q", q);
        paramMap.put("sort", sort);
        paramMap.put("order", order);
        paramMap.put("per_page", per_page);

        requestSpecification = RestAssured.given().params(paramMap);
        response = requestSpecification.get(endpoint);
    }

    @And("the response should contain repositories")
    public void the_response_should_contain_repositories() {
        JsonPath jsonPath = response.jsonPath();

        boolean validResponseSize = (int) jsonPath.get("total_count") > 0;
        assertTrue("the response contains repositories", validResponseSize);
    }

    @And("the response should contain {string} error message")
    public void the_response_should_contain_error_message(String errorMessage) {
        JsonPath jsonPath = response.jsonPath();
        String message = jsonPath.get("message").toString();

        boolean validMessage = message.equals(errorMessage);
        assertTrue("The response contains valid message indicating the request validation failure", validMessage);
    }

    @And("the response should contain an error message indicating the date is {string}")
    public void the_response_should_contain_an_error_message_indicating_the_date_is(String errorMessage) {
        JsonPath jsonPath = response.jsonPath();
        List<Map<String, Object>> errors = jsonPath.getList("errors");
        boolean validError = errors.get(0).get("message").toString().contains(errorMessage);
        assertTrue("The response contains an error message indicating the date is not a recognized date/time format", validError);
    }

    @And("the response should contain an error message with field {string} and code {string}")
    public void the_response_should_contain_an_error_message_with_field_and_code(String field, String code) {
        JsonPath jsonPath = response.jsonPath();
        List<Map<String, Object>> errors = jsonPath.getList("errors");
        boolean validDetails = errors.get(0).get("field").toString().equals(field) && errors.get(0).get("code").toString().equals(code);
        assertTrue("The response contains an error message with correct field and code", validDetails);
    }

    @And("the response should contain an error message indicating the endpoint is {string}")
    public void the_response_should_contain_an_error_message_indicating_the_endpoint_is(String message) {
        JsonPath jsonPath = response.jsonPath();
        String responseMessage = jsonPath.get("message");
        boolean validMessage = responseMessage.equals(message);
        assertTrue("The response contains an error message indicating the endpoint is not found", validMessage);
    }


}
