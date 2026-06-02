package autotests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import static com.consol.citrus.DefaultTestActionBuilder.action;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

public class DuckQuackTest extends TestNGCitrusSpringSupport {

    private void createDuckInDb(TestCaseRunner runner, long id, String color, double height, String material, String sound, String wingsState) {
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

    private void sendQuackRequest(TestCaseRunner runner, long id, int repetitionCount, int soundCount) {
        runner.$(http()
                .client("http://localhost:2222")
                .send()
                .get("/api/duck/action/quack")
                .queryParam("id", String.valueOf(id))
                .queryParam("repetitionCount", String.valueOf(repetitionCount))
                .queryParam("soundCount", String.valueOf(soundCount)));
    }

    @Test(description = "quack: корректный нечетный id (1), корректный звук. repeatCount=2, soundCount=2. ожидается quack-quack, quack-quack")
    @CitrusTest
    public void testQuackOddId(@Optional @CitrusResource TestCaseRunner runner) {
        long id = 1L;
        clearDuckTable(runner);
        createDuckInDb(runner, id, "yellow", 10, "rubber", "quack", "ACTIVE");

        sendQuackRequest(runner, id, 2, 2);

        runner.$(http()
                .client("http://localhost:2222")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .validate(jsonPath()
                        .expression("$.sound", "quack-quack, quack-quack")));
    }

    @Test(description = "quack: корректный четный id (2), корректный звук. repeatCount=3, soundCount=1. ожидается quack, quack, quack")
    @CitrusTest
    public void testQuackEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        long id = 2L;
        clearDuckTable(runner);
        createDuckInDb(runner, id, "green", 15, "wood", "quack", "ACTIVE");

        sendQuackRequest(runner, id, 3, 1);

        runner.$(http()
                .client("http://localhost:2222")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .validate(jsonPath()
                        .expression("$.sound", "quack, quack, quack")));
    }
}