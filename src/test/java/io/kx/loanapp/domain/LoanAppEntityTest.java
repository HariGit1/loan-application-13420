package io.kx.loanapp.domain;

import com.google.protobuf.Empty;
import io.kx.loanapp.api.LoanAppApi;
import kalix.javasdk.eventsourcedentity.EventSourcedEntity;
import kalix.javasdk.eventsourcedentity.EventSourcedEntityContext;
import kalix.javasdk.testkit.EventSourcedResult;
import org.junit.Ignore;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

// This class was initially generated based on the .proto definition by Kalix tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class LoanAppEntityTest {

    @Test
    @Ignore("to be implemented")
    public void exampleTest() {
        LoanAppEntityTestKit service = LoanAppEntityTestKit.of(LoanAppEntity::new);
        // // use the testkit to execute a command
        // // of events emitted, or a final updated state:
        // SomeCommand command = SomeCommand.newBuilder()...build();
        // EventSourcedResult<SomeResponse> result = service.someOperation(command);
        // // verify the emitted events
        // ExpectedEvent actualEvent = result.getNextEventOfType(ExpectedEvent.class);
        // assertEquals(expectedEvent, actualEvent);
        // // verify the final state after applying the events
        // assertEquals(expectedState, service.getState());
        // // verify the reply
        // SomeReply reply = result.getReply();
        // assertEquals(expectedReply, reply);
    }

    @Test
    public void submitTest() {
        LoanAppEntityTestKit service = LoanAppEntityTestKit.of(LoanAppEntity::new);
        String loanAppId = UUID.randomUUID().toString();
        LoanAppApi.SubmitCommand submitCommand = LoanAppApi.SubmitCommand.newBuilder()
                .setLoanAppId(loanAppId)
                .setClientId(UUID.randomUUID().toString())
                .setClientMonthlyIncomeCents(20000000)
                .setLoanAmountCents(40000000)
                .setLoanDurationMonths(28)
                .build();
        EventSourcedResult<Empty> result = service.submit(submitCommand);
        assertTrue(result.didEmitEvents());
        LoanAppApi.GetCommand getCmd = LoanAppApi.GetCommand.newBuilder()
                .setLoanAppId(loanAppId).build();
        EventSourcedResult<LoanAppApi.LoanAppState> getResult = service.get(getCmd);
        assertEquals(LoanAppApi.LoanAppStatus.STATUS_IN_REVIEW, getResult.getReply().getStatus());
    }


    @Test
    public void getTest() {
        LoanAppEntityTestKit service = LoanAppEntityTestKit.of(LoanAppEntity::new);
        String loanAppId = UUID.randomUUID().toString();
        LoanAppApi.SubmitCommand submitCommand = LoanAppApi.SubmitCommand.newBuilder()
                .setLoanAppId(loanAppId)
                .setClientId(UUID.randomUUID().toString())
                .setClientMonthlyIncomeCents(20000000)
                .setLoanAmountCents(40000000)
                .setLoanDurationMonths(28)
                .build();
        EventSourcedResult<Empty> result = service.submit(submitCommand);
        assertTrue(result.didEmitEvents());
        LoanAppApi.GetCommand command = LoanAppApi.GetCommand.newBuilder().setLoanAppId(loanAppId).build();
        EventSourcedResult<LoanAppApi.LoanAppState> getResult = service.get(command);
        assertEquals(LoanAppApi.LoanAppStatus.STATUS_IN_REVIEW, getResult.getReply().getStatus());
    }


  @Test
  public void approveTest() {
    LoanAppEntityTestKit service = LoanAppEntityTestKit.of(LoanAppEntity::new);
    String loanAppId = UUID.randomUUID().toString();
    LoanAppApi.SubmitCommand submitCommand = LoanAppApi.SubmitCommand.newBuilder()
            .setLoanAppId(loanAppId)
            .setClientId(UUID.randomUUID().toString())
            .setClientMonthlyIncomeCents(20000000)
            .setLoanAmountCents(40000000)
            .setLoanDurationMonths(28)
            .build();
    EventSourcedResult<Empty> result = service.submit(submitCommand);
    assertTrue(result.didEmitEvents());
    LoanAppApi.ApproveCommand approveCommand = LoanAppApi.ApproveCommand.newBuilder().setLoanAppId(loanAppId).build();
    EventSourcedResult<Empty> approveResult = service.approve(approveCommand);
    assertEquals(true, approveResult.didEmitEvents());
    LoanAppApi.GetCommand command = LoanAppApi.GetCommand.newBuilder().setLoanAppId(loanAppId).build();
    EventSourcedResult<LoanAppApi.LoanAppState> getResult = service.get(command);
    assertEquals(LoanAppApi.LoanAppStatus.STATUS_APPROVED, getResult.getReply().getStatus());
  }


    @Test
    public void declineTest() {
        LoanAppEntityTestKit service = LoanAppEntityTestKit.of(LoanAppEntity::new);
        String loanAppId = UUID.randomUUID().toString();
        LoanAppApi.SubmitCommand submitCommand = LoanAppApi.SubmitCommand.newBuilder()
                .setLoanAppId(loanAppId)
                .setClientId(UUID.randomUUID().toString())
                .setClientMonthlyIncomeCents(20000000)
                .setLoanAmountCents(40000000)
                .setLoanDurationMonths(28)
                .build();
        EventSourcedResult<Empty> result = service.submit(submitCommand);
        assertTrue(result.didEmitEvents());
        LoanAppApi.DeclineCommand command = LoanAppApi.DeclineCommand.newBuilder()
                .setLoanAppId(loanAppId)
                .setReason("Your application does not meet criteria").build();
        EventSourcedResult<Empty> declineResult = service.decline(command);
        assertEquals(true, declineResult.didEmitEvents());
        LoanAppApi.GetCommand getCommand = LoanAppApi.GetCommand.newBuilder().setLoanAppId(loanAppId).build();
        EventSourcedResult<LoanAppApi.LoanAppState> getResult = service.get(getCommand);
        assertEquals(LoanAppApi.LoanAppStatus.STATUS_DECLINED, getResult.getReply().getStatus());
    }

}
