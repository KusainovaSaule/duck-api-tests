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

import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

public class DuckCreateTest extends TestNGCitrusSpringSupport {

    private void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
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
    }

    @Test(description = "create: создать утку с material = rubber")
    @CitrusTest
    public void testCreateDuckRubber(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 10.0, "rubber", "quack", "ACTIVE");

        runner.$(http()
                .client("http://localhost:2222")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .validate(jsonPath()
                        .expression("$.id", "@isNumber()@")
                        .expression("$.material", "rubber")
                        .expression("$.color", "yellow")
                        .expression("$.height", "10.0")
                        .expression("$.sound", "quack")
                        .expression("$.wingsState", "ACTIVE")));
    }

    @Test(description = "create: создать утку с material = wood")
    @CitrusTest
    public void testCreateDuckWood(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "brown", 15.0, "wood", "quack", "FIXED");

        runner.$(http()
                .client("http://localhost:2222")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .validate(jsonPath()
                        .expression("$.id", "@isNumber()@")
                        .expression("$.material", "wood")
                        .expression("$.color", "brown")
                        .expression("$.height", "15.0")
                        .expression("$.sound", "quack")
                        .expression("$.wingsState", "FIXED")));
    }
}