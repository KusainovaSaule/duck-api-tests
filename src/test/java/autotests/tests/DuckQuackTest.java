package autotests.tests;

import autotests.clients.DuckQuackClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import static com.consol.citrus.DefaultTestActionBuilder.action;

public class DuckQuackTest extends DuckQuackClient {

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

    @Test(description = "quack: корректный нечетный id (1), repeatCount=2, soundCount=2")
    @CitrusTest
    public void testQuackOddId(@Optional @CitrusResource TestCaseRunner runner) {
        long id = 1L;
        clearDuckTable(runner);
        createDuckInDb(runner, id, "yellow", 10, "rubber", "quack", "ACTIVE");

        quackDuck(runner, String.valueOf(id), 2, 2);
        validateQuackResponse(runner, "quack-quack, quack-quack");
    }

    @Test(description = "quack: корректный четный id (2), repeatCount=3, soundCount=1")
    @CitrusTest
    public void testQuackEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        long id = 2L;
        clearDuckTable(runner);
        createDuckInDb(runner, id, "green", 15, "wood", "quack", "ACTIVE");

        quackDuck(runner, String.valueOf(id), 3, 1);
        validateQuackResponse(runner, "moo-moo-moo");
    }
}