package provider;

import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import io.pact.impl.UserServiceImpl;
import io.pact.mockServer.MockServer;
import io.pact.models.User;
import io.pact.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@PactFolder("target/pacts")
//@PactBroker(
//        host = "localhost",
//        port = "9292"
//)
@PactTestFor(providerName = "PACT_PROVIDER")
@Provider("PACT_PROVIDER")

public class ProviderContractTest {

    private UserService userService;
    MockServer mockServer = new MockServer(userService);

    @BeforeEach
    public void setup() {
        userService = new UserServiceImpl();
        mockServer.startServer(8080);
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        mockServer.stopServer();
    }

    @State("a valid request received from client")
    public void validUserState() {
        User user = userService.getUserById(1);
        assertEquals(1,user.getId());
        assertEquals("John Doe", user.getName());
    }

    @State("an invalid request received from client with a non existent user")
    public void inValidUserStateNegativeUserId() {
        User user = userService.getUserById(-1);
        assertNull(user);
    }

    @State("an invalid request received from client with No User")
    public void inValidUserStateNoUserId() {
    }

    @State("a valid request received from client to create user")
    public void setAValidUser() {
        User user = userService.setUser(2, "Merry Christmas");
        userService.getUserById(2);
        assertEquals(2,user.getId());
        assertEquals("Merry Christmas", user.getName());
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void verifyPact(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", 8080, "/"));
        context.verifyInteraction();
    }
}
