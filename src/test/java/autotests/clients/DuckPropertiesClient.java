package autotests.clients;

import com.consol.citrus.TestCaseRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

public class DuckPropertiesClient extends DuckClient {

    public void getDuckProperties(TestCaseRunner runner, String duckId) {
        sendGetRequest(runner, "/api/duck/action/properties", "id", duckId);
    }

    public void validatePropertiesResponse(TestCaseRunner runner) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK));
    }

    public void validatePropertiesWithString(TestCaseRunner runner, String color, double height,
                                             String material, String sound, String wingsState) {
        validateResponseString(runner, color, height, material, sound, wingsState);
    }

    public void validatePropertiesMaterialWithString(TestCaseRunner runner, String expectedMaterial) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MediaType.APPLICATION_JSON_VALUE)
                .validate(jsonPath()
                        .expression("$.material", expectedMaterial)));
    }

    public void validatePropertiesWithResource(TestCaseRunner runner, String resourcePath) {
        validateResponseResource(runner, resourcePath);
    }

    public void validatePropertiesWithPayload(TestCaseRunner runner, Object expectedPayload) {
        validateResponsePayload(runner, expectedPayload);
    }
}