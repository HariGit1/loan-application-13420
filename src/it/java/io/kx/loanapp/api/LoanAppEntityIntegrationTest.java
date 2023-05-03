package io.kx.loanapp.api;

import com.google.protobuf.Empty;
import io.kx.loanapp.Main;
import io.kx.loanapp.domain.LoanAppDomain;
import kalix.javasdk.testkit.junit.KalixTestKitResource;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;

import java.util.UUID;

import static java.util.concurrent.TimeUnit.*;
import static org.junit.Assert.assertEquals;

// This class was initially generated based on the .proto definition by Kalix tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

// Example of an integration test calling our service via the Kalix proxy
// Run all test classes ending with "IntegrationTest" using `mvn verify -Pit`
public class LoanAppEntityIntegrationTest {

  /**
   * The test kit starts both the service container and the Kalix proxy.
   */
  @ClassRule
  public static final KalixTestKitResource testKit =
    new KalixTestKitResource(Main.createKalix());

  /**
   * Use the generated gRPC client to call the service through the Kalix proxy.
   */
  private final LoanAppService client;

  public LoanAppEntityIntegrationTest() {
    client = testKit.getGrpcClient(LoanAppService.class);
  }

  @Test
  public void submitNewLoanApplication() throws Exception {
    String loanAppId = UUID.randomUUID().toString();
    LoanAppApi.SubmitCommand submitCommand = LoanAppApi.SubmitCommand.newBuilder()
            .setLoanAppId(loanAppId)
            .setClientId(UUID.randomUUID().toString())
            .setClientMonthlyIncomeCents(20000000)
            .setLoanAmountCents(40000000)
            .setLoanDurationMonths(28)
            .build();
     client.submit(submitCommand)
             .toCompletableFuture().get(5, SECONDS);
    LoanAppApi.GetCommand getCommand = LoanAppApi.GetCommand.newBuilder().setLoanAppId(loanAppId).build();
    LoanAppApi.LoanAppState getState = client.get(getCommand).toCompletableFuture().get(5, SECONDS);
    assertEquals(LoanAppApi.LoanAppStatus.STATUS_IN_REVIEW, getState.getStatus());
  }

  @Test
  @Ignore("to be implemented")
  public void getOnNonExistingEntity() throws Exception {
    // TODO: set fields in command, and provide assertions to match replies
    // client.get(LoanAppApi.GetCommand.newBuilder().build())
    //         .toCompletableFuture().get(5, SECONDS);
  }

  @Test
  @Ignore("to be implemented")
  public void approveOnNonExistingEntity() throws Exception {
    // TODO: set fields in command, and provide assertions to match replies
    // client.approve(LoanAppApi.ApproveCommand.newBuilder().build())
    //         .toCompletableFuture().get(5, SECONDS);
  }

  @Test
  @Ignore("to be implemented")
  public void declineOnNonExistingEntity() throws Exception {
    // TODO: set fields in command, and provide assertions to match replies
    // client.decline(LoanAppApi.DeclineCommand.newBuilder().build())
    //         .toCompletableFuture().get(5, SECONDS);
  }
}
