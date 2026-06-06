package autotests.tests;

import autotests.clients.DuckFlyClient;
import autotests.clients.DuckCreateClient;
import autotests.payloads.response.DuckCreateRequest;
import autotests.payloads.response.DuckMessageResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckFlyTest extends DuckFlyClient {

    private DuckCreateClient createClient = new DuckCreateClient();

    @Test(description = "fly: существующий id с активными крыльями (валидация через Payload)")
    @CitrusTest
    public void testFlyActiveWingsWithPayload(@Optional @CitrusResource TestCaseRunner runner) {
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

        flyDuck(runner, "${duckId}");

        DuckMessageResponse expectedResponse = new DuckMessageResponse()
                .message("I am flying :)");

        validateFlyWithPayload(runner, expectedResponse);
    }

    @Test(description = "fly: существующий id со связанными крыльями (валидация через String)")
    @CitrusTest
    public void testFlyFixedWingsWithString(@Optional @CitrusResource TestCaseRunner runner) {
        createClient.duckService = this.duckService;

        DuckCreateRequest request = new DuckCreateRequest()
                .color("red")
                .height(12.0)
                .material("wood")
                .sound("quack")
                .wingsState("FIXED");

        createClient.createDuckAndExtractId(runner, request.color(), request.height(),
                request.material(), request.sound(),
                request.wingsState(), "duckId");

        flyDuck(runner, "${duckId}");

        validateFlyWithString(runner, "I can not fly :C");
    }

    @Test(description = "fly: существующий id с крыльями в неопределенном состоянии (валидация через String)")
    @CitrusTest
    public void testFlyUndefinedWingsWithString(@Optional @CitrusResource TestCaseRunner runner) {
        createClient.duckService = this.duckService;

        DuckCreateRequest request = new DuckCreateRequest()
                .color("blue")
                .height(8.0)
                .material("plastic")
                .sound("quack")
                .wingsState("UNDEFINED");

        createClient.createDuckAndExtractId(runner, request.color(), request.height(),
                request.material(), request.sound(),
                request.wingsState(), "duckId");

        flyDuck(runner, "${duckId}");

        validateFlyWithString(runner, "Wings are not detected :(");
    }
}