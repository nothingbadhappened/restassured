package hooks;

import util.Context;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {
    @Before
    public void printScenario(Scenario scenario){
        System.out.println(scenario);
    }

    @After
    public void clearContext(){
        System.out.println("Scenario completed, clearing context...");
        Context.clearContext();
    }
}
