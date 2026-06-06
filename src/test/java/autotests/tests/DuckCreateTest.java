package autotests.tests;

import autotests.clients.DuckCreateClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckCreateTest extends DuckCreateClient {

    @Test(description = "create: создать утку с material = rubber")
    @CitrusTest
    public void testCreateDuckRubber(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 10.0, "rubber", "quack", "ACTIVE");
        validateCreateResponse(runner, "yellow", 10.0, "rubber", "quack", "ACTIVE");
    }

    @Test(description = "create: создать утку с material = wood")
    @CitrusTest
    public void testCreateDuckWood(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "brown", 15.0, "wood", "quack", "FIXED");
        validateCreateResponse(runner, "brown", 15.0, "wood", "quack", "FIXED");
    }
}