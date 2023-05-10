package io.kx.loanapp.action;

import akka.stream.javadsl.Source;
import com.google.protobuf.Empty;
import io.kx.loanapp.action.LoanAppActionHandler;
import io.kx.loanapp.action.LoanAppServiceActionImpl;
import io.kx.loanapp.action.LoanAppServiceActionImplTestKit;
import io.kx.loanapp.api.LoanAppApi;
import kalix.javasdk.testkit.ActionResult;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

// This class was initially generated based on the .proto definition by Kalix tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class LoanAppServiceActionImplTest {

  @Test
  @Ignore("to be implemented")
  public void exampleTest() {
    LoanAppServiceActionImplTestKit service = LoanAppServiceActionImplTestKit.of(LoanAppServiceActionImpl::new);
    // // use the testkit to execute a command
    // SomeCommand command = SomeCommand.newBuilder()...build();
    // ActionResult<SomeResponse> result = service.someOperation(command);
    // // verify the reply
    // SomeReply reply = result.getReply();
    // assertEquals(expectedReply, reply);
  }

  @Test
  @Ignore("to be implemented")
  public void submitLoanAppActionTest() {
    LoanAppServiceActionImplTestKit testKit = LoanAppServiceActionImplTestKit.of(LoanAppServiceActionImpl::new);
    // ActionResult<Empty> result = testKit.submitLoanAppAction(LoanAppApi.SubmitCommand.newBuilder()...build());
  }

  @Test
  @Ignore("to be implemented")
  public void expireActionTest() {
    LoanAppServiceActionImplTestKit testKit = LoanAppServiceActionImplTestKit.of(LoanAppServiceActionImpl::new);
    // ActionResult<Empty> result = testKit.expireAction(LoanAppActionHandler.ExpireMessage.newBuilder()...build());
  }

}
