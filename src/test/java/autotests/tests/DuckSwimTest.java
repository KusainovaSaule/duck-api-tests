package autotests.tests;

import autotests.clients.DuckSwimClient;
import autotests.clients.DuckCreateClient;
import autotests.payloads.response.DuckCreateRequest;
import autotests.payloads.response.DuckMessageResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckSwimTest extends DuckSwimClient {

    private DuckCreateClient createClient = new DuckCreateClient();

    @Test(description = "swim: существующий id (валидация через Payload)")
    @CitrusTest
    public void testSwimExistingIdWithPayload(@Optional @CitrusResource TestCaseRunner runner) {
        createClient.duckService = this.duckService;

        DuckCreateRequest request = new DuckCreateRequest()
                .color("yellow")
                .height(10.0)
                .material("rubber")
                .sound("quack")
                .wingsState("ACTIVE");

        createClient.createDuckAndExtractId(runner, request.color(), request.height(),
                request.material(), request.sound(),
                request.wingsState(), "duckId");

        swimDuck(runner, "${duckId}");

        DuckMessageResponse expectedResponse = new DuckMessageResponse()
                .message("Paws are not found ((((");

        validateSwimWithPayload(runner, expectedResponse);
    }

    @Test(description = "swim: несуществующий id (валидация через NOT FOUND)")
    @CitrusTest
    public void testSwimNonExistentId(@Optional @CitrusResource TestCaseRunner runner) {
        swimDuck(runner, "999999");
        validateNotFound(runner);
    }
}