package autotests.clients;

import com.consol.citrus.TestCaseRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

public class DuckSwimClient extends DuckClient {

    public void swimDuck(TestCaseRunner runner, String duckId) {
        sendGetRequest(runner, "/api/duck/action/swim", "id", duckId);
    }

    public void validateNotFound(TestCaseRunner runner) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.NOT_FOUND));
    }

    public void validateSwimWithString(TestCaseRunner runner, String expectedMessage) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.NOT_FOUND)
                .message()
                .type(MediaType.APPLICATION_JSON_VALUE)
                .validate(jsonPath()
                        .expression("$.message", expectedMessage)));
    }

    public void validateSwimWithResource(TestCaseRunner runner, String resourcePath) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.NOT_FOUND)
                .message()
                .type(MediaType.APPLICATION_JSON_VALUE)
                .body(new org.springframework.core.io.ClassPathResource(resourcePath)));
    }

    public void validateSwimWithPayload(TestCaseRunner runner, Object expectedPayload) {
        try {
            String message = (String) expectedPayload.getClass().getMethod("message").invoke(expectedPayload);
            runner.$(http()
                    .client(duckService)
                    .receive()
                    .response(HttpStatus.NOT_FOUND)
                    .message()
                    .type(MediaType.APPLICATION_JSON_VALUE)
                    .validate(jsonPath()
                            .expression("$.message", message)));
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract payload values", e);
        }
    }
}