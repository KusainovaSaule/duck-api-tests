package autotests.clients;

import com.consol.citrus.TestCaseRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

public class DuckFlyClient extends DuckClient {

    public void flyDuck(TestCaseRunner runner, String duckId) {
        sendGetRequest(runner, "/api/duck/action/fly", "id", duckId);
    }

    public void validateFlyWithString(TestCaseRunner runner, String expectedMessage) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MediaType.APPLICATION_JSON_VALUE)
                .validate(jsonPath()
                        .expression("$.message", expectedMessage)));
    }

    public void validateFlyWithResource(TestCaseRunner runner, String resourcePath) {
        validateResponseResource(runner, resourcePath);
    }

    public void validateFlyWithPayload(TestCaseRunner runner, Object expectedPayload) {
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