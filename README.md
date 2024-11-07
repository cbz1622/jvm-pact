# JVM-PACT
Code repo that contains contract testing using PACT-JVM

## Overview
- This project is a Java-Maven based automation framework for contract testing
- Uses Pact packages to generate consumer contracts which can be validated against the provider
- For the purpose of this example, a mock server has been created to mimic the server behaviours 
- Optional - Uses docker containers to create a Pact broker where the contracts can be centrally stored

## Table of Contents
1. [Prerequisites]()
2. [Dependencies]()
3. [Compile & Running Tests]()

## Prerequisites
- Java JDk 17 or higher
- Maven 3.9.4 or higher
- An IDE (e.g., IntelliJ IDEA, Eclipse) with Maven support
- Chrome Browser (latest)
- Docker (optional)

## Dependencies
The project dependencies are managed through Maven.

Key dependencies include:
- JUnit 5 for Assertion library (V 5.11)
- Wiremock (3.9.1)
- Pact (Consumer & Provider) (V 4.6.14)
- Pact Broker (V 4.6.15)

## Compile & Running Tests

### Compile
`mvn clean compile`

### Run locally on a machine
Uncomment `@PactFolder("target/pacts")` in [ProviderContractTest](src/test/java/provider/ProviderContractTest.java)
Comment `@PactBroker(
        host = "localhost",
        port = "9292"
)` in [ProviderContractTest](src/test/java/provider/ProviderContractTest.java)
1. Contract Generation - `mvn test -Dtest=ConsumerContractTest`
2. Once the above command is executed, contracts will be created in `target/pacts` folder
3. Contract Verification - `mvn test -Dtest=ProviderContractTest`

### Run by pushing Contracts to a Pact Broker
Comment `@PactFolder("target/pacts")` in [ProviderContractTest](src/test/java/provider/ProviderContractTest.java)
UnComment `@PactBroker(
        host = "localhost",
        port = "9292"
)` in [ProviderContractTest](src/test/java/provider/ProviderContractTest.java)
1. Make sure Docker is installed
2. Run the `docker-compose.yml` file - this will pull the pact broker and postgres images and create the container 
3. Make sure the container is running through command `docker-compose ps`
4. Make sure `http://localhost:9292` - this will show if pact broker is running in localhost on port 9292
5. Contract Generation - `mvn test -Dtest=ConsumerContractTest`
6. Once the above command is executed, contracts will be created in `target/pacts` folder
7. Publish the contract `mvn pact:publish`
8. Contract Verification - `mvn test -Dtest=ProviderContractTest`
