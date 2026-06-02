package autotests.tests;

import autotests.clients.DuckSwimClient;
import autotests.clients.DuckCreateClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import static com.consol.citrus.DefaultTestActionBuilder.action;

public class DuckSwimTest extends DuckSwimClient {

    private DuckCreateClient createClient = new DuckCreateClient();

    @Test(description = "swim: существующий id")
    @CitrusTest
    public void testSwimExistingId(@Optional @CitrusResource TestCaseRunner runner) {
        createClient.duckService = this.duckService;
        createClient.createDuckAndExtractId(runner, "yellow", 10, "rubber", "quack", "ACTIVE", "duckId");

        swimDuck(runner, "${duckId}");
        validateSwimResponseNotFound(runner);
    }

    @Test(description = "swim: несуществующий id")
    @CitrusTest
    public void testSwimNonExistentId(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(action(context -> {
            try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "dev");
                 java.sql.Statement stmt = conn.createStatement()) {
                stmt.execute("DELETE FROM DUCK WHERE ID = 999999");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        swimDuck(runner, "999999");
        validateNotFound(runner);
    }
}