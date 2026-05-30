package autotests.actions.quack;

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

public class DuckQuackTest extends TestNGCitrusSpringSupport {

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

    @Test(description = "quack: корректный нечетный id, корректный звук. repeatCount=2, soundCount=2. ожидается quack-quack, quack-quack")
    @CitrusTest
    public void testQuackOddId(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 10.0, "rubber", "quack", "ACTIVE", "duckId");

        runner.$(http()
                .client("http://localhost:2222")
                .send()
                .get("/api/duck/action/quack")
                .queryParam("id", "${duckId}")
                .queryParam("repetitionCount", "2")
                .queryParam("soundCount", "2"));

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

    @Test(description = "quack: корректный четный id, корректный звук. repeatCount=3, soundCount=1. ожидается quack, quack, quack")
    @CitrusTest
    public void testQuackEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "green", 15.0, "wood", "quack", "ACTIVE", "duckId");

        runner.$(http()
                .client("http://localhost:2222")
                .send()
                .get("/api/duck/action/quack")
                .queryParam("id", "${duckId}")
                .queryParam("repetitionCount", "3")
                .queryParam("soundCount", "1"));

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