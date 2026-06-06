package autotests.clients;

import com.consol.citrus.TestCaseRunner;
import java.util.Locale;

public class DuckCreateClient extends DuckClient {

    public void createDuck(TestCaseRunner runner, String color, double height,
                           String material, String sound, String wingsState) {
        String heightFormatted = String.format(Locale.US, "%f", height);
        String body = String.format(
                "{\"color\":\"%s\",\"height\":%s,\"material\":\"%s\",\"sound\":\"%s\",\"wingsState\":\"%s\"}",
                color, heightFormatted, material, sound, wingsState
        );
        sendPostRequest(runner, "/api/duck/create", body);
    }

    public void createDuckAndExtractId(TestCaseRunner runner, String color, double height,
                                       String material, String sound, String wingsState,
                                       String variableName) {
        createDuck(runner, color, height, material, sound, wingsState);
        extractId(runner, variableName);
    }

    public void validateCreateWithString(TestCaseRunner runner, String color, double height,
                                         String material, String sound, String wingsState) {
        validateResponseString(runner, color, height, material, sound, wingsState);
    }

    public void validateCreateWithResource(TestCaseRunner runner, String resourcePath) {
        validateResponseResource(runner, resourcePath);
    }

    public void validateCreateWithPayload(TestCaseRunner runner, Object expectedPayload) {
        validateResponsePayload(runner, expectedPayload);
    }
}