# GitHub API testing #
This repository contains API Testing framework for GIT SEARCH
https://api.github.com/search/repositories?
The documentation for this endpoint can be found at: https://developer.github.com/v3/search/#search-repositories

The endpoint supports querying based on a series of keywords, as well as ordering and sorting of the returned results.

Note:
The automation uses Cucumber- BDD framework (Gerkins), with JAVA as the programing language

## Pre-Requisites to run the code ##
1. JAVA 8
2. IntelliJ IDEA 2023.3.2
3. Apache Maven 3.8.6

## Setup project and executing tests ##
1. Clone repository "https://github.com/x22114386/GitHubSearchAPIAutomationTesting.git"
2. Open project in IntelliJ Idea
3. Execute below command to run test
`mvn clean test`
4. Build the Maven Project
5. go to /GitHubSearchAPIAutomationTesting/src/java/runner/TestRunner
6. run the TestRunner file.

Note:
/GitHubSearchAPIAutomationTesting/src/java/features/SearchRepositories.feature 
- This file consist of all the testcase scenarios to execute.

/GitHubSearchAPIAutomationTesting/src/java/stepdefinitions/Repositories
- this file consist the implementation using JAVA and cucumber framework

/GitHubSearchAPIAutomationTesting/src/java/runner/TestRunner
-this file consist of the start of execution.

The search API doesnot fetch data related to PRIVATE or FORKED repository.

## To view the Report ##
Once the test execution is complete, the reports are stored in
/GitHubSearchAPIAutomationTesting/src/target/cucumber-reports.html
