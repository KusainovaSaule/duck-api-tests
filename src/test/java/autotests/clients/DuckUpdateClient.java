package autotests.clients;

import com.consol.citrus.TestCaseRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

public class DuckUpdateClient extends DuckClient {

    public void updateDuck(TestCaseRunner runner, String duckId, String color, String height,
                           String material, String sound, String wingsState) {
        runner.$(http()
                .client(duckService)
                .send()
                .put("/api/duck/update?id=" + duckId + "&color=" + color + "&height=" + height +
                        "&material=" + material + "&sound=" + sound + "&wingsState=" + wingsState)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    public void validateUpdateResponse(TestCaseRunner runner, String duckId) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MediaType.APPLICATION_JSON_VALUE)
                .validate(jsonPath()
                        .expression("$.message", "Duck with id = " + duckId + " is updated")));
    }
}