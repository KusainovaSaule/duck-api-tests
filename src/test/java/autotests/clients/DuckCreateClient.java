package autotests.clients;

import com.consol.citrus.TestCaseRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Locale;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

public class DuckCreateClient extends DuckClient {

    public void createDuck(TestCaseRunner runner, String color, double height,
                           String material, String sound, String wingsState) {
        String heightFormatted = String.format(Locale.US, "%f", height);
        String body = String.format(
                "{\"color\":\"%s\",\"height\":%s,\"material\":\"%s\",\"sound\":\"%s\",\"wingsState\":\"%s\"}",
                color, heightFormatted, material, sound, wingsState
        );

        runner.$(http()
                .client(duckService)
                .send()
                .post("/api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body));
    }

    public void validateCreateResponse(TestCaseRunner runner, String color, double height,
                                       String material, String sound, String wingsState) {
        String expectedHeight;
        if (height == (long) height) {
            expectedHeight = String.format(Locale.US, "%.1f", height);
        } else {
            expectedHeight = String.format(Locale.US, "%f", height);
        }

        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MediaType.APPLICATION_JSON_VALUE)
                .validate(jsonPath()
                        .expression("$.id", "@isNumber()@")
                        .expression("$.material", material)
                        .expression("$.color", color)
                        .expression("$.height", expectedHeight)
                        .expression("$.sound", sound)
                        .expression("$.wingsState", wingsState)));
    }

    public void createDuckAndExtractId(TestCaseRunner runner, String color, double height,
                                       String material, String sound, String wingsState,
                                       String variableName) {
        createDuck(runner, color, height, material, sound, wingsState);

        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MediaType.APPLICATION_JSON_VALUE)
                .extract(fromBody().expression("$.id", variableName)));
    }
}