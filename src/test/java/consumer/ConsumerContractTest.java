package consumer;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerContractTest {

    @BeforeAll
    public static void setup() {
        System.setProperty("pact.mockServer.interface", "localhost");
    }

    @Pact(provider = "PACT_PROVIDER", consumer = "PACT_CONSUMER")
    public V4Pact createPactValidResponse(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");

        PactDslJsonBody validResponseBody = new PactDslJsonBody()
                .numberValue("id", 1)
                .stringValue("name", "John Doe");

        return builder.given("a valid request received from client")
                .uponReceiving("valid user request")
                .path("/user/1")
                .headers(headers)
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(validResponseBody)
                .toPact(V4Pact.class);
    }

    @Pact(provider = "PACT_PROVIDER", consumer = "PACT_CONSUMER")
    public V4Pact createPactInvalidResponse(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");

        PactDslJsonBody inValidResponseBody = new PactDslJsonBody()
                .stringValue("error", "User not found");

        return builder.given("an invalid request received from client with a non existent user")
                .uponReceiving("invalid user request - non existent user")
                .path("/user/-1")
                .headers(headers)
                .method("GET")
                .willRespondWith()
                .status(404)
                .body(inValidResponseBody)
                .toPact(V4Pact.class);
    }

    @Pact(provider = "PACT_PROVIDER", consumer = "PACT_CONSUMER")
    public V4Pact createPactInvalidResponseNoValue(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");

        PactDslJsonBody inValidResponseBody = new PactDslJsonBody()
                .stringValue("error", "No User entered");

        return builder.given("an invalid request received from client with No User")
                .uponReceiving("invalid user request - No User entered")
                .path("/user/")
                .headers(headers)
                .method("GET")
                .willRespondWith()
                .status(404)
                .body(inValidResponseBody)
                .toPact(V4Pact.class );
    }

    @Pact(provider = "PACT_PROVIDER", consumer = "PACT_CONSUMER")
    public V4Pact createPactSetNewUser(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");

        PactDslJsonBody requestBody = new PactDslJsonBody()
                .numberValue("id", 2)
                .stringValue("name", "Merry Christmas");

        PactDslJsonBody validResponseBody = new PactDslJsonBody()
                .stringValue("success", "new user created");

        return builder.given("a valid request received from client to create user")
                .uponReceiving("a valid request to create user")
                .path("/user/add")
                .headers(headers)
                .body(requestBody)
                .method("POST")
                .willRespondWith()
                .status(201)
                .body(validResponseBody)
                .toPact(V4Pact.class );
    }

    @Test
    @PactTestFor(pactMethod = "createPactValidResponse", providerName = "PACT_PROVIDER", pactVersion = PactSpecVersion.V4)
    public void testGetRequestMethodWithPositiveValues(MockServer server) {
        given().baseUri(server.getUrl())
                .header("Accept", "application/json")
                .header("Content-type","application/json")
                .when()
                .get("/user/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("name",equalTo("John Doe"));
    }

    @Test
    @PactTestFor(pactMethod = "createPactInvalidResponse", providerName = "PACT_PROVIDER", pactVersion = PactSpecVersion.V4)
    public void testGetRequestMethodWithNegativeValues(MockServer server) {
        given().baseUri(server.getUrl())
                .header("Accept", "application/json")
                .header("Content-type","application/json")
                .when()
                .get("/user/-1")
                .then()
                .statusCode(404)
                .body("error", equalTo("User not found"));
    }

    @Test
    @PactTestFor(pactMethod = "createPactInvalidResponseNoValue", providerName = "PACT_PROVIDER", pactVersion = PactSpecVersion.V4)
    public void testGetRequestMethodWithNoValues(MockServer server) {
        given().baseUri(server.getUrl())
                .header("Accept", "application/json")
                .header("Content-type","application/json")
                .when()
                .get("/user/")
                .then()
                .statusCode(404)
                .body("error", equalTo("No User entered"));
    }

    @Test
    @PactTestFor(pactMethod = "createPactSetNewUser", providerName = "PACT_PROVIDER", pactVersion = PactSpecVersion.V4)
    public void testSetNewUserRequestMethod(MockServer server) {
        given().baseUri(server.getUrl())
                .header("Accept", "application/json")
                .header("Content-type","application/json")
                .when()
                .request().body("{ \"id\": 2, \"name\": \"Merry Christmas\" }")
                .post("/user/add")
                .then()
                .statusCode(201)
                .body("success", equalTo("new user created"));
    }
}
