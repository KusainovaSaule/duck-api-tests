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
import java.util.Locale;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

public class DuckFlyTest extends TestNGCitrusSpringSupport {

    private void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        String body = String.format(
                "{\"color\":\"%s\",\"height\":%s,\"material\":\"%s\",\"sound\":\"%s\",\"wingsState\":\"%s\"}",
                color, height, material, sound, wingsState
        );

        runner.$(http()
                .client("http://localhost:2222")
                .send()
                .post("/api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body));
    }

    private void extractDuckId(TestCaseRunner runner, String variableName) {
        runner.$(http()
                .client("http://localhost:2222")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .extract(fromBody().expression("$.id", variableName)));
    }

    private void sendFlyRequest(TestCaseRunner runner, String duckId) {
        runner.$(http()
                .client("http://localhost:2222")
                .send()
                .get("/api/duck/action/fly")
                .queryParam("id", duckId));
    }

    @Test(description = "fly: существующий id с активными крыльями (wingsState=ACTIVE). ожидается I'm flying, если смотреть документацию")
    @CitrusTest
    public void testFlyActiveWings(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 10.0, "rubber", "quack", "ACTIVE");
        extractDuckId(runner, "duckId");

        sendFlyRequest(runner, "${duckId}");

        runner.$(http()
                .client("http://localhost:2222")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .validate(jsonPath()
                        .expression("$.message", "I'm flying")));
    }

    @Test(description = "fly: существующий id со связанными крыльями (wingsState=FIXED). ожидается I can't fly, если смотреть документацию")
    @CitrusTest
    public void testFlyFixedWings(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "red", 12.0, "wood", "quack", "FIXED");
        extractDuckId(runner, "duckId");

        sendFlyRequest(runner, "${duckId}");

        runner.$(http()
                .client("http://localhost:2222")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .validate(jsonPath()
                        .expression("$.message", "I can't fly")));
    }

    @Test(description = "fly: существующий id с крыльями в неопределенном состоянии (wingsState=UNDEFINED). ожидается по сваггеру Wings are not detected :(")
    @CitrusTest
    public void testFlyUndefinedWings(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "blue", 8.0, "plastic", "quack", "UNDEFINED");
        extractDuckId(runner, "duckId");

        sendFlyRequest(runner, "${duckId}");

        runner.$(http()
                .client("http://localhost:2222")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .validate(jsonPath()
                        .expression("$.message", "Wings are not detected :(")));
    }
}