package runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = "src/test/java/features"
		,glue={"stepdefinitions"}
		,dryRun = true,
//		plugin = { "me.jvt.cucumber.report.PrettyReports:target/cucumber" }
		plugin = {"pretty", "html:target/cucumber-reports.html"}
		)

public class TestRunner {

}