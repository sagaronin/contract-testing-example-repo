package ariman.pact.consumer;

import static au.com.dius.pact.consumer.ConsumerPactRunnerKt.runConsumerTest;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import au.com.dius.pact.consumer.ConsumerPactBuilder;
import au.com.dius.pact.consumer.PactVerificationResult;
import au.com.dius.pact.consumer.model.MockProviderConfig;
import au.com.dius.pact.core.model.RequestResponsePact;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PactJunitDSLTest {

    @Autowired
    ProviderService providerService;

    private void checkResult(PactVerificationResult result) {
        if (result instanceof PactVerificationResult.Error) {
            throw new RuntimeException(((PactVerificationResult.Error) result).getError());
        }
        assertThat(result, is(instanceOf(PactVerificationResult.Ok.class)));
    }

    @Test
    public void testPact1() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");

        RequestResponsePact pact = ConsumerPactBuilder
                .consumer("JunitDSLConsumer1")
                .hasPactWith("ExampleProvider")
                .given("")
                .uponReceiving("Query name is Bheem")
                .path("/information")
                .query("name=Bheem")
                .method("GET")
                .willRespondWith()
                .headers(headers)
                .status(200)
                .body("{" +
                        "\"salary\":45000," +
                        "\"name\":\"Chota Bheem\"," +
                        "\"nationality\":\"India\"," +
                        "\"strength\":\"Punches\"," +
                        "\"weakness\":\"Laddoos\"," +
                        "\"contact\":" +
                            "{\"Email\":\"chota.bheem@dholakpur.com\"," +
                            "\"Phone Number\":\"9090950980\"}" +
                    "}")
                .toPact();

        MockProviderConfig config = MockProviderConfig.createDefault();
        PactVerificationResult result = runConsumerTest(pact, config, (mockServer, context) -> {
            providerService.setBackendURL(mockServer.getUrl(), "Bheem");
            Information information = providerService.getInformation();
            assertEquals(information.getName(), "Chota Bheem");
            return null;
        });

        checkResult(result);
    }

    @Test
    public void testPact2() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");

        RequestResponsePact pact = ConsumerPactBuilder
                .consumer("JunitDSLConsumer2")
                .hasPactWith("ExampleProvider")
                .given("")
                .uponReceiving("Query name is Krrish")
                .path("/information")
                .query("name=Krrish")
                .method("GET")
                .willRespondWith()
                .headers(headers)
                .status(200)
                .body("{" +
                    "\"salary\":80000," +
                    "\"name\":\"Krrish\"," +
                    "\"nationality\":\"India\"," +
                    "\"strength\":\"Flying\"," +
                    "\"weakness\":\"super-power\"," +
                    "\"contact\":" +
                        "{\"Email\":\"krrish.mehra@shimla.com\",\"Phone Number\":\"9090940123\"}" +
                    "}")
                .toPact();

        MockProviderConfig config = MockProviderConfig.createDefault();
        PactVerificationResult result = runConsumerTest(pact, config, (mockServer, context) -> {
            providerService.setBackendURL(mockServer.getUrl(), "Krrish");
            Information information = providerService.getInformation();
            assertEquals(information.getName(), "Krrish");
            return null;
        });

        checkResult(result);
    }
}
