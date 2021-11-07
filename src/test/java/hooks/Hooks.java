package hooks;

import lombok.extern.log4j.Log4j2;
import util.Context;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

@Log4j2
public class Hooks {
    @Before
    public void printScenario(Scenario scenario){
        log.info(scenario);
    }

    @After
    public void clearContext(){
        System.out.println("Scenario completed, clearing context...");
        Context.clearContext();
    }
}
