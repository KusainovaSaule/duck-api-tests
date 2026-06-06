package autotests.tests;

import autotests.clients.DuckDeleteClient;
import autotests.clients.DuckCreateClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckDeleteTest extends DuckDeleteClient {

    private DuckCreateClient createClient = new DuckCreateClient();

    @Test(description = "delete: удалить утку")
    @CitrusTest
    public void testDeleteDuck(@Optional @CitrusResource TestCaseRunner runner) {
        createClient.duckService = this.duckService;
        createClient.createDuckAndExtractId(runner, "yellow", 10, "rubber", "quack", "ACTIVE", "duckId");

        deleteDuck(runner, "${duckId}");
        validateDeleteResponse(runner);
    }
}