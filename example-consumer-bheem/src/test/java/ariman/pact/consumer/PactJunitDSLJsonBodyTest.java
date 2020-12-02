package ariman.pact.consumer;

import static au.com.dius.pact.consumer.ConsumerPactRunnerKt.runConsumerTest;
import static io.pactfoundation.consumer.dsl.LambdaDsl.newJsonBody;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import au.com.dius.pact.consumer.ConsumerPactBuilder;
import au.com.dius.pact.consumer.PactVerificationResult;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.model.MockProviderConfig;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.RequestResponsePact;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PactJunitDSLJsonBodyTest {

    @Autowired
    ProviderService providerService;

    private void checkResult(PactVerificationResult result) {
        if (result instanceof PactVerificationResult.Error) {
            throw new RuntimeException(((PactVerificationResult.Error) result).getError());
        }
        assertThat(result, is(instanceOf(PactVerificationResult.Ok.class)));
    }

    @Test
    public void testWithPactDSLJsonBody() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");

        DslPart body = new PactDslJsonBody()
                .numberType("salary", 45000)
                .stringType("name", "Chota Bheem")
                .stringType("strength", "Punches")
                .stringType("weakness", "Laddoos")
                .stringType("nationality", "India")
                .object("contact")
                .stringValue("Email", "chota.bheem@dholakpur.com")
                .stringValue("Phone Number", "9090950980")
                .closeObject();

        RequestResponsePact pact = ConsumerPactBuilder
                .consumer("JunitDSLJsonBodyConsumer")
                .hasPactWith("ExampleProvider")
                .given("")
                .uponReceiving("Query name is Bheem")
                .path("/information")
                .query("name=Bheem")
                .method("GET")
                .willRespondWith()
                .headers(headers)
                .status(200)
                .body(body)
                .toPact();

        MockProviderConfig config = MockProviderConfig.createDefault(PactSpecVersion.V3);
        PactVerificationResult result = runConsumerTest(pact, config, (mockServer, context) -> {
            providerService.setBackendURL(mockServer.getUrl());
            Information information = providerService.getInformation();
            assertEquals(information.getName(), "Chota Bheem");
            return null;
        });

        checkResult(result);
    }

    @Test
    public void testWithLambdaDSLJsonBody() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");

        DslPart body = newJsonBody((root) -> {
            root.numberValue("salary", 45000);
            root.stringValue("name", "Chota Bheem");
            root.stringValue("strength", "Punches");
            root.stringValue("weakness", "Laddoos");
            root.stringValue("nationality", "India");
            root.object("contact", (contactObject) -> {
                contactObject.stringMatcher("Email", ".*@dholakpur.com", "chota.bheem@dholakpur.com");
                contactObject.stringType("Phone Number", "9090950980");
            });
        }).build();

        RequestResponsePact pact = ConsumerPactBuilder
                .consumer("JunitDSLLambdaJsonBodyConsumer")
                .hasPactWith("ExampleProvider")
                .given("")
                .uponReceiving("Query name is Bheem")
                .path("/information")
                .query("name=Bheem")
                .method("GET")
                .willRespondWith()
                .headers(headers)
                .status(200)
                .body(body)
                .toPact();

        MockProviderConfig config = MockProviderConfig.createDefault(PactSpecVersion.V3);
        PactVerificationResult result = runConsumerTest(pact, config, (mockServer, context) -> {
            providerService.setBackendURL(mockServer.getUrl());
            Information information = providerService.getInformation();
            assertEquals(information.getName(), "Chota Bheem");
            return null;
        });

        checkResult(result);
    }

}
