package autotests.tests;

import autotests.clients.DuckQuackClient;
import autotests.clients.DuckCreateClient;
import autotests.payloads.response.DuckCreateRequest;
import autotests.payloads.response.DuckQuackResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckQuackTest extends DuckQuackClient {

    private DuckCreateClient createClient = new DuckCreateClient();

    @Test(description = "quack: нечетный id (валидация через Payload)")
    @CitrusTest
    public void testQuackOddIdWithPayload(@Optional @CitrusResource TestCaseRunner runner) {
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

        quackDuck(runner, "${duckId}", 2, 2);

        DuckQuackResponse expectedResponse = new DuckQuackResponse()
                .sound("quack-quack, quack-quack");

        validateQuackWithPayload(runner, expectedResponse);
    }

    @Test(description = "quack: четный id (валидация через String)")
    @CitrusTest
    public void testQuackEvenIdWithString(@Optional @CitrusResource TestCaseRunner runner) {
        createClient.duckService = this.duckService;

        DuckCreateRequest request = new DuckCreateRequest()
                .color("green")
                .height(15.0)
                .material("wood")
                .sound("quack")
                .wingsState("ACTIVE");

        createClient.createDuckAndExtractId(runner, request.color(), request.height(),
                request.material(), request.sound(),
                request.wingsState(), "duckId");

        quackDuck(runner, "${duckId}", 3, 1);

        validateQuackWithString(runner, "moo-moo-moo");
    }
}