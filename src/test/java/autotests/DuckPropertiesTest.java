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

public class DuckPropertiesTest extends TestNGCitrusSpringSupport {

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

    private void sendPropertiesRequest(TestCaseRunner runner, String duckId) {
        runner.$(http()
                .client("http://localhost:2222")
                .send()
                .get("/api/duck/action/properties")
                .queryParam("id", duckId));
    }

    @Test(description = "properties: четный ID (2), material=wood. INSERT INTO DUCK (ID, COLOR, HEIGHT, MATERIAL, SOUND, WINGS_STATE) \n" +
            "VALUES (2, 'green', 15.0, 'wood', 'quack', 'ACTIVE');")
    @CitrusTest
    public void testPropertiesEvenIdWood(@Optional @CitrusResource TestCaseRunner runner) {
        long id = 2L;
        clearDuckTable(runner);
        createDuckInDb(runner, id, "green", 15.0, "wood", "quack", "ACTIVE");

        sendPropertiesRequest(runner, String.valueOf(id));

        runner.$(http()
                .client("http://localhost:2222")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test(description = "properties: нечетный ID (1), material=rubber. INSERT INTO DUCK (ID, COLOR, HEIGHT, MATERIAL, SOUND, WINGS_STATE) \n" +
            "VALUES (1, 'yellow', 10.0, 'rubber', 'quack', 'FIXED');")
    @CitrusTest
    public void testPropertiesOddIdRubber(@Optional @CitrusResource TestCaseRunner runner) {
        long id = 1L;
        clearDuckTable(runner);
        createDuckInDb(runner, id, "yellow", 10.0, "rubber", "quack", "FIXED");

        sendPropertiesRequest(runner, String.valueOf(id));

        runner.$(http()
                .client("http://localhost:2222")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .validate(jsonPath()
                        .expression("$.material", "rubber")));
    }
}