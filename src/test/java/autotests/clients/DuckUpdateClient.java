package autotests.clients;

import com.consol.citrus.TestCaseRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

public class DuckUpdateClient extends DuckClient {

    public void updateDuck(TestCaseRunner runner, String duckId, String color, String height,
                           String material, String sound, String wingsState) {
        sendPutRequest(runner, "/api/duck/update",
                "id", duckId,
                "color", color,
                "height", height,
                "material", material,
                "sound", sound,
                "wingsState", wingsState);
    }

    public void validateUpdateWithString(TestCaseRunner runner, String expectedMessage) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MediaType.APPLICATION_JSON_VALUE)
                .validate(jsonPath()
                        .expression("$.message", expectedMessage)));
    }

    public void validateUpdateWithResource(TestCaseRunner runner, String resourcePath) {
        validateResponseResource(runner, resourcePath);
    }

    public void validateUpdateWithPayload(TestCaseRunner runner, Object expectedPayload) {
        try {
            String message = (String) expectedPayload.getClass().getMethod("message").invoke(expectedPayload);
            runner.$(http()
                    .client(duckService)
                    .receive()
                    .response(HttpStatus.OK)
                    .message()
                    .type(MediaType.APPLICATION_JSON_VALUE)
                    .validate(jsonPath()
                            .expression("$.message", message)));
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract payload values", e);
        }
    }
}