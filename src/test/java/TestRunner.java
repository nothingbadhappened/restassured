import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {
                "src/test/resources/features"
        },

        glue = {
                "stepdefs", "hooks"
        },

        plugin = {
                "pretty",
                "html:results/html/cucumber.html"
//                ToDo: Connect Allure
//                "cucumberHooks.customReportListener",
//                "io.qameta.allure.cucumber7jvm.AllureCucumber6Jvm"
        },
        monochrome = true
)

public class TestRunner {

}
