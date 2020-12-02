package ariman.pact.consumer;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactProviderRule;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PactJunitRuleTest {

    @Autowired
    ProviderService providerService;

    @Rule //will execute before each test
    public PactProviderRule mockProvider = new PactProviderRule("ExampleProvider", this);
    //this will create a mock server at given host and port

    @Pact(consumer = "MikuConsumer")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json;charset=UTF-8");

        return builder
                .given("")
                    .uponReceiving("Pact JVM example Pact interaction")
                    .path("/information")
                    .query("name=Bheem")
                    .method("GET")
                .willRespondWith()
                    .headers(headers)
                    .status(200)
                    .body("{\n" +
                            "    \"salary\": 45000,\n" +
                            "    \"name\": \"Chota Bheem\",\n" +
                            "    \"strength\": \"Punches\",\n" +
                            "    \"nationality\": \"India\",\n" +
                            "    \"contact\": {\n" +
                            "        \"Email\": \"chota.bheem@dholakpur.com\",\n" +
                            "        \"Phone Number\": \"9090950980\"\n" +
                            "    }\n" +
                            "}")
                .toPact();
    }

    @Test
    @PactVerification
    public void runTest() {
        providerService.setBackendURL(mockProvider.getUrl());
        Information information = providerService.getInformation();
        assertEquals(information.getName(), "Chota Bheem");
    }
}
