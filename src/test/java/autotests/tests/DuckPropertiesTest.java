package autotests.tests;

import autotests.clients.DuckPropertiesClient;
import autotests.clients.DuckCreateClient;
import autotests.payloads.response.DuckCreateRequest;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckPropertiesTest extends DuckPropertiesClient {

    private DuckCreateClient createClient = new DuckCreateClient();

    @Test(description = "properties: существующая утка (валидация через Payload)")
    @CitrusTest
    public void testPropertiesWithPayload(@Optional @CitrusResource TestCaseRunner runner) {
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

        getDuckProperties(runner, "${duckId}");

        validatePropertiesResponse(runner);
    }
}