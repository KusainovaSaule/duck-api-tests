package autotests.clients;

import autotests.EndpointConfig;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.consol.citrus.message.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import static com.consol.citrus.validation.json.JsonPathMessageValidationContext.Builder.jsonPath;

@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckClient extends TestNGCitrusSpringSupport {

    @Autowired
    public HttpClient duckService;

    protected ObjectMapper objectMapper = new ObjectMapper();

    protected void sendPostRequest(TestCaseRunner runner, String path, String body) {
        runner.$(http()
                .client(duckService)
                .send()
                .post(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body));
    }

    protected void sendGetRequest(TestCaseRunner runner, String path, String queryParam, String queryValue) {
        runner.$(http()
                .client(duckService)
                .send()
                .get(path + "?" + queryParam + "=" + queryValue)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    protected void sendPutRequest(TestCaseRunner runner, String path, String... params) {
        StringBuilder url = new StringBuilder(path);
        for (int i = 0; i < params.length; i += 2) {
            url.append(i == 0 ? "?" : "&").append(params[i]).append("=").append(params[i + 1]);
        }
        runner.$(http()
                .client(duckService)
                .send()
                .put(url.toString())
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    protected void sendDeleteRequest(TestCaseRunner runner, String path, String queryParam, String queryValue) {
        runner.$(http()
                .client(duckService)
                .send()
                .delete(path + "?" + queryParam + "=" + queryValue));
    }

    protected void extractId(TestCaseRunner runner, String variableName) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MediaType.APPLICATION_JSON_VALUE)
                .extract(fromBody().expression("$.id", variableName)));
    }

    protected void validateResponseString(TestCaseRunner runner, String color, double height,
                                          String material, String sound, String wingsState) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .validate(jsonPath()
                        .expression("$.color", color)
                        .expression("$.height", String.valueOf(height))
                        .expression("$.material", material)
                        .expression("$.sound", sound)
                        .expression("$.wingsState", wingsState)));
    }

    protected void validateResponseStringMessage(TestCaseRunner runner, String expectedMessage) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .validate(jsonPath()
                        .expression("$.message", expectedMessage)));
    }

    protected void validateResponseStringSound(TestCaseRunner runner, String expectedSound) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .validate(jsonPath()
                        .expression("$.sound", expectedSound)));
    }

    protected void validateResponseResource(TestCaseRunner runner, String resourcePath) {
        runner.$(http()
                .client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .body(new ClassPathResource(resourcePath)));
    }

    protected void validateResponsePayload(TestCaseRunner runner, Object expectedPayload) {
        try {
            String color = (String) expectedPayload.getClass().getMethod("color").invoke(expectedPayload);
            double height = (double) expectedPayload.getClass().getMethod("height").invoke(expectedPayload);
            String material = (String) expectedPayload.getClass().getMethod("material").invoke(expectedPayload);
            String sound = (String) expectedPayload.getClass().getMethod("sound").invoke(expectedPayload);
            String wingsState = (String) expectedPayload.getClass().getMethod("wingsState").invoke(expectedPayload);

            runner.$(http()
                    .client(duckService)
                    .receive()
                    .response(HttpStatus.OK)
                    .message()
                    .type(MessageType.JSON)
                    .validate(jsonPath()
                            .expression("$.color", color)
                            .expression("$.height", String.valueOf(height))
                            .expression("$.material", material)
                            .expression("$.sound", sound)
                            .expression("$.wingsState", wingsState)));
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract payload values", e);
        }
    }

    protected void validateResponsePayloadMessage(TestCaseRunner runner, Object expectedPayload) {
        try {
            String message = (String) expectedPayload.getClass().getMethod("message").invoke(expectedPayload);
            runner.$(http()
                    .client(duckService)
                    .receive()
                    .response(HttpStatus.OK)
                    .message()
                    .type(MessageType.JSON)
                    .validate(jsonPath()
                            .expression("$.message", message)));
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract payload values", e);
        }
    }

    protected void validateResponsePayloadSound(TestCaseRunner runner, Object expectedPayload) {
        try {
            String sound = (String) expectedPayload.getClass().getMethod("sound").invoke(expectedPayload);
            runner.$(http()
                    .client(duckService)
                    .receive()
                    .response(HttpStatus.OK)
                    .message()
                    .type(MessageType.JSON)
                    .validate(jsonPath()
                            .expression("$.sound", sound)));
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract payload values", e);
        }
    }
}