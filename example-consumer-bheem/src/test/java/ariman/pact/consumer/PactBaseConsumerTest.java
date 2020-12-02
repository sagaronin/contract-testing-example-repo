package ariman.pact.consumer;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.PactTestExecutionContext;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.ConsumerPactTest;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PactBaseConsumerTest extends ConsumerPactTest {

    @Autowired
    ProviderService providerService;

    @Override
    @Pact(provider="ExampleProvider", consumer="BaseConsumer")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");

        return builder
                .given("")
                .uponReceiving("Pact JVM example Pact interaction")
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
    }

    @Override
    protected String providerName() {
        return "ExampleProvider";
    }

    @Override
    protected String consumerName() {
        return "BaseConsumer";
    }

    @Override
    protected void runTest(MockServer mockServer, PactTestExecutionContext context) {
        providerService.setBackendURL(mockServer.getUrl());
        Information information = providerService.getInformation();
        assertEquals(information.getName(), "Chota Bheem");
    }
}
