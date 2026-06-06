package autotests.tests;

import autotests.clients.DuckDeleteClient;
import autotests.clients.DuckCreateClient;
import autotests.payloads.response.DuckCreateRequest;
import autotests.payloads.response.DuckMessageResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckDeleteTest extends DuckDeleteClient {

    private DuckCreateClient createClient = new DuckCreateClient();

    @Test(description = "delete: удалить утку (валидация через Resource)")
    @CitrusTest
    public void testDeleteDuckWithResource(@Optional @CitrusResource TestCaseRunner runner) {
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

        deleteDuck(runner, "${duckId}");

        validateDeleteWithResource(runner, "deleteResponse.json");
    }

    @Test(description = "delete: удалить утку (валидация через Payload)")
    @CitrusTest
    public void testDeleteDuckWithPayload(@Optional @CitrusResource TestCaseRunner runner) {
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

        deleteDuck(runner, "${duckId}");

        DuckMessageResponse expectedResponse = new DuckMessageResponse()
                .message("Duck is deleted");

        validateDeleteWithPayload(runner, expectedResponse);
    }
}