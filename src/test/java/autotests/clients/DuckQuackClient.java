package autotests.clients;

import com.consol.citrus.TestCaseRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

public class DuckQuackClient extends DuckClient {

    public void quackDuck(TestCaseRunner runner, String duckId, int repetitionCount, int soundCount) {
        runner.$(http()
                .client(duckService)
                .send()
                .get("/api/duck/action/quack?id=" + duckId + "&repetitionCount=" + repetitionCount + "&soundCount=" + soundCount)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    public void validateQuackResponse(TestCaseRunner runner, String expectedSound) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MediaType.APPLICATION_JSON_VALUE)
                .validate(jsonPath()
                        .expression("$.sound", expectedSound)));
    }
}