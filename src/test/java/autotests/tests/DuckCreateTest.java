package autotests.tests;

import autotests.clients.DuckCreateClient;
import autotests.payloads.response.DuckCreateRequest;
import autotests.payloads.response.DuckResponse;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckCreateTest extends DuckCreateClient {

    @Test(description = "create: создать утку с material = rubber (валидация через Payload)")
    @CitrusTest
    public void testCreateDuckRubberWithPayload(@Optional @CitrusResource TestCaseRunner runner) {
        DuckCreateRequest request = new DuckCreateRequest()
                .color("yellow")
                .height(10.0)
                .material("rubber")
                .sound("quack")
                .wingsState("ACTIVE");

        createDuck(runner, request.color(), request.height(), request.material(),
                request.sound(), request.wingsState());

        DuckResponse expectedResponse = new DuckResponse()
                .color("yellow")
                .height(10.0)
                .material("rubber")
                .sound("quack")
                .wingsState("ACTIVE");

        validateCreateWithPayload(runner, expectedResponse);
    }

    @Test(description = "create: создать утку с material = wood (валидация через String)")
    @CitrusTest
    public void testCreateDuckWoodWithString(@Optional @CitrusResource TestCaseRunner runner) {
        DuckCreateRequest request = new DuckCreateRequest()
                .color("brown")
                .height(15.0)
                .material("wood")
                .sound("quack")
                .wingsState("FIXED");

        createDuck(runner, request.color(), request.height(), request.material(),
                request.sound(), request.wingsState());

        validateCreateWithString(runner, request.color(), request.height(),
                request.material(), request.sound(), request.wingsState());
    }
}