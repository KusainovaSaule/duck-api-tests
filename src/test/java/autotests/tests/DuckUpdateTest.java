package autotests.tests;

import autotests.clients.DuckUpdateClient;
import autotests.clients.DuckCreateClient;
import autotests.payloads.response.DuckCreateRequest;
import autotests.payloads.response.DuckMessageResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckUpdateTest extends DuckUpdateClient {

    private DuckCreateClient createClient = new DuckCreateClient();

    @Test(description = "update: изменить цвет и высоту (валидация через Payload)")
    @CitrusTest
    public void testUpdateColorAndHeightWithPayload(@Optional @CitrusResource TestCaseRunner runner) {
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

        updateDuck(runner, "${duckId}", "red", "25", "rubber", "quack", "ACTIVE");

        DuckMessageResponse expectedResponse = new DuckMessageResponse()
                .message("Duck with id = ${duckId} is updated");

        validateUpdateWithPayload(runner, expectedResponse);
    }

    @Test(description = "update: изменить цвет и звук (валидация через String)")
    @CitrusTest
    public void testUpdateColorAndSoundWithString(@Optional @CitrusResource TestCaseRunner runner) {
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

        updateDuck(runner, "${duckId}", "blue", "10", "rubber", "meow", "ACTIVE");

        String expectedMessage = "Duck with id = ${duckId} is updated";
        validateUpdateWithString(runner, expectedMessage);
    }
}