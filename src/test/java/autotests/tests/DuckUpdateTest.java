package autotests.tests;

import autotests.clients.DuckUpdateClient;
import autotests.clients.DuckCreateClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckUpdateTest extends DuckUpdateClient {

    private DuckCreateClient createClient = new DuckCreateClient();

    @Test(description = "update: изменить цвет и высоту уточки")
    @CitrusTest
    public void testUpdateColorAndHeight(@Optional @CitrusResource TestCaseRunner runner) {
        createClient.duckService = this.duckService;
        createClient.createDuckAndExtractId(runner, "yellow", 10, "rubber", "quack", "ACTIVE", "duckId");

        updateDuck(runner, "${duckId}", "red", "25", "rubber", "quack", "ACTIVE");
        validateUpdateResponse(runner, "${duckId}");
    }

    @Test(description = "update: изменить цвет и звук уточки")
    @CitrusTest
    public void testUpdateColorAndSound(@Optional @CitrusResource TestCaseRunner runner) {
        createClient.duckService = this.duckService;
        createClient.createDuckAndExtractId(runner, "yellow", 10, "rubber", "quack", "ACTIVE", "duckId");

        updateDuck(runner, "${duckId}", "blue", "10", "rubber", "meow", "ACTIVE");
        validateUpdateResponse(runner, "${duckId}");
    }
}