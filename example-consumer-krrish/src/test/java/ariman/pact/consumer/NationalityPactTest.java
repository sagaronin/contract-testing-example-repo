package ariman.pact.consumer;

import static au.com.dius.pact.consumer.ConsumerPactRunnerKt.runConsumerTest;
import static io.pactfoundation.consumer.dsl.LambdaDsl.newJsonBody;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import au.com.dius.pact.consumer.ConsumerPactBuilder;
import au.com.dius.pact.consumer.PactVerificationResult;
import au.com.dius.pact.consumer.dsl.DslPart;
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
public class NationalityPactTest {

    @Autowired
    ProviderService providerService;

    private void checkResult(PactVerificationResult result) {
        if (result instanceof PactVerificationResult.Error) {
            throw new RuntimeException(((PactVerificationResult.Error) result).getError());
        }
        assertThat(result, is(instanceOf(PactVerificationResult.Ok.class)));
    }

    @Test
    public void testWithNationality() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json;");

        DslPart body = newJsonBody((root) -> {
            root.stringValue("name", "Krrish");
            root.stringValue("nationality", "India");
            root.stringValue("strength", "Flying");
            root.stringValue("weakness", "super-power");
            root.numberType("salary");
            root.object("contact", (contactObject) -> {
                contactObject.stringMatcher("Email", ".*@shimla.com", "krrish.mehra@shimla.com");
                contactObject.stringType("Phone Number", "9090940123");
            });
        }).build();

        RequestResponsePact pact = ConsumerPactBuilder
                .consumer("ConsumerKrrishWithNationality")
                .hasPactWith("ExampleProvider")
                .given("")
                .uponReceiving("Query name is Krrish")
                .path("/information")
                .query("name=Krrish")
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
            assertEquals(information.getName(), "Krrish");
            assertEquals(information.getNationality(), "India");
            return null;
        });

        checkResult(result);
    }

    @Test
    public void testNoNationality() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json;");

        DslPart body = newJsonBody((root) -> {
            root.stringValue("name", "Krrish");
            root.stringValue("nationality", null);
            root.stringValue("strength", "Flying");
            root.stringValue("weakness", "super-power");
            root.numberType("salary");
            root.object("contact", (contactObject) -> {
                contactObject.stringMatcher("Email", ".*@shimla.com", "krrish.mehra@shimla.com");
                contactObject.stringType("Phone Number", "9090940123");
            });
        }).build();

        RequestResponsePact pact = ConsumerPactBuilder
                .consumer("ConsumerKrrishNoNationality")
                .hasPactWith("ExampleProvider")
                .given("No nationality")
                .uponReceiving("Query name is Krrish")
                .path("/information")
                .query("name=Krrish")
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
            assertEquals(information.getName(), "Krrish");
            assertNull(information.getNationality());
            return null;
        });

        checkResult(result);
    }
}
