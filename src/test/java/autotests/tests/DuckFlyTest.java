package autotests.tests;

import autotests.clients.DuckFlyClient;
import autotests.clients.DuckCreateClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckFlyTest extends DuckFlyClient {

    private DuckCreateClient createClient = new DuckCreateClient();

    @Test(description = "fly: существующий id с активными крыльями")
    @CitrusTest
    public void testFlyActiveWings(@Optional @CitrusResource TestCaseRunner runner) {
        createClient.duckService = this.duckService;
        createClient.createDuckAndExtractId(runner, "yellow", 10, "rubber", "quack", "ACTIVE", "duckId");

        flyDuck(runner, "${duckId}");
        validateFlyResponse(runner, "I am flying :)");
    }

    @Test(description = "fly: существующий id со связанными крыльями")
    @CitrusTest
    public void testFlyFixedWings(@Optional @CitrusResource TestCaseRunner runner) {
        createClient.duckService = this.duckService;
        createClient.createDuckAndExtractId(runner, "red", 12, "wood", "quack", "FIXED", "duckId");

        flyDuck(runner, "${duckId}");
        validateFlyResponse(runner, "I can not fly :C");
    }

    @Test(description = "fly: существующий id с крыльями в неопределенном состоянии")
    @CitrusTest
    public void testFlyUndefinedWings(@Optional @CitrusResource TestCaseRunner runner) {
        createClient.duckService = this.duckService;
        createClient.createDuckAndExtractId(runner, "blue", 8, "plastic", "quack", "UNDEFINED", "duckId");

        flyDuck(runner, "${duckId}");
        validateFlyResponse(runner, "Wings are not detected :(");
    }
}