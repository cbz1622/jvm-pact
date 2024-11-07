package io.pact.mockServer;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import io.pact.service.UserService;

public class MockServer {
    private final UserService  userService;
    private WireMockServer wireMockServer;

    public MockServer(UserService service) {
        this.userService = service;
    }

    public void startServer(int portNumber) {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(portNumber));
        wireMockServer.start();

        wireMockServer.stubFor(get(urlEqualTo("/user/1"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                        .withStatus(200).withBody("{ \"id\": 1, \"name\": \"John Doe\" }")));

        wireMockServer.stubFor(get(urlEqualTo("/user/-1"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                                .withStatus(404).withBody("{\"error\": \"User not found\" }")));

        wireMockServer.stubFor(get(urlEqualTo("/user/"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                        .withStatus(404).withBody("{\"error\": \"No User entered\" }")));

        wireMockServer.stubFor(post(urlEqualTo("/user/add"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                        .withStatus(201).withBody("{\"success\": \"new user created\" }")));
    }

    public void stopServer() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }
}
