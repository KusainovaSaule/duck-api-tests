package autotests.clients;

import com.consol.citrus.TestCaseRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

public class DuckPropertiesClient extends DuckClient {

    public void getDuckProperties(TestCaseRunner runner, String duckId) {
        runner.$(http()
                .client(duckService)
                .send()
                .get("/api/duck/action/properties?id=" + duckId)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    public void validatePropertiesResponse(TestCaseRunner runner) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK));
    }

    public void validatePropertiesMaterial(TestCaseRunner runner, String expectedMaterial) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MediaType.APPLICATION_JSON_VALUE)
                .validate(jsonPath()
                        .expression("$.material", expectedMaterial)));
    }
}