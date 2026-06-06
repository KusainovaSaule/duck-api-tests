package autotests.clients;

import com.consol.citrus.TestCaseRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

public class DuckDeleteClient extends DuckClient {

    public void deleteDuck(TestCaseRunner runner, String duckId) {
        sendDeleteRequest(runner, "/api/duck/delete", "id", duckId);
    }

    public void validateDeleteWithString(TestCaseRunner runner, String expectedMessage) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MediaType.APPLICATION_JSON_VALUE)
                .validate(jsonPath()
                        .expression("$.message", expectedMessage)));
    }

    public void validateDeleteWithResource(TestCaseRunner runner, String resourcePath) {
        validateResponseResource(runner, resourcePath);
    }

    public void validateDeleteWithPayload(TestCaseRunner runner, Object expectedPayload) {
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