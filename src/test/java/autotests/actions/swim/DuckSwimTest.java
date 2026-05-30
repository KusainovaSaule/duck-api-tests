package autotests.actions.swim;

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

public class DuckSwimTest extends TestNGCitrusSpringSupport {

    private void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState, String variableName) {
        String heightFormatted = String.format(Locale.US, "%f", height);
        String body = String.format(
                "{\"color\":\"%s\",\"height\":%s,\"material\":\"%s\",\"sound\":\"%s\",\"wingsState\":\"%s\"}",
                color, heightFormatted, material, sound, wingsState
        );

        runner.$(http()
                .client("http://localhost:2222")
                .send()
                .post("/api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body));

        runner.$(http()
                .client("http://localhost:2222")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .extract(fromBody().expression("$.id", variableName)));
    }

    @Test(description = "swim: существующий id. ожидается, что утка найдена и фраза, что она умеет плавать")
    @CitrusTest
    public void testSwimExistingId(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 10.0, "rubber", "quack", "ACTIVE", "duckId");

        runner.$(http()
                .client("http://localhost:2222")
                .send()
                .get("/api/duck/action/swim")
                .queryParam("id", "${duckId}"));

        runner.$(http()
                .client("http://localhost:2222")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .validate(jsonPath()
                        .expression("$.message", "I'm swimming")));
    }

    @Test(description = "swim: несуществующий id. ожидается, что такой утки нет")
    @CitrusTest
    public void testSwimNonExistentId(@Optional @CitrusResource TestCaseRunner runner) {
        runner.$(http()
                .client("http://localhost:2222")
                .send()
                .get("/api/duck/action/swim")
                .queryParam("id", "999999"));

        runner.$(http()
                .client("http://localhost:2222")
                .receive()
                .response(HttpStatus.NOT_FOUND));
    }
}
