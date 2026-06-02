package autotests.tests;

import autotests.clients.DuckPropertiesClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import static com.consol.citrus.DefaultTestActionBuilder.action;

public class DuckPropertiesTest extends DuckPropertiesClient {

    private void createDuckInDb(TestCaseRunner runner, long id, String color, double height,
                                String material, String sound, String wingsState) {
        runner.$(action(context -> {
            try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "dev");
                 java.sql.Statement stmt = conn.createStatement()) {
                stmt.execute(String.format(
                        "INSERT INTO DUCK (ID, COLOR, HEIGHT, MATERIAL, SOUND, WINGS_STATE) VALUES (%d, '%s', %f, '%s', '%s', '%s')",
                        id, color, height, material, sound, wingsState
                ));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }

    private void clearDuckTable(TestCaseRunner runner) {
        runner.$(action(context -> {
            try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "dev");
                 java.sql.Statement stmt = conn.createStatement()) {
                stmt.execute("DELETE FROM DUCK");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }

    @Test(description = "properties: четный ID (2), material=wood")
    @CitrusTest
    public void testPropertiesEvenIdWood(@Optional @CitrusResource TestCaseRunner runner) {
        long id = 2L;
        clearDuckTable(runner);
        createDuckInDb(runner, id, "green", 15.0, "wood", "quack", "ACTIVE");

        getDuckProperties(runner, String.valueOf(id));
        validatePropertiesResponse(runner);
    }

    @Test(description = "properties: нечетный ID (1), material=rubber")
    @CitrusTest
    public void testPropertiesOddIdRubber(@Optional @CitrusResource TestCaseRunner runner) {
        long id = 1L;
        clearDuckTable(runner);
        createDuckInDb(runner, id, "yellow", 10.0, "rubber", "quack", "FIXED");

        getDuckProperties(runner, String.valueOf(id));
        validatePropertiesMaterial(runner, "rubber");
    }
}