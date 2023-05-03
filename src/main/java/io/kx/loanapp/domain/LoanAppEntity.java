package io.kx.loanapp.domain;

import com.google.protobuf.Empty;
import com.google.protobuf.util.Timestamps;
import io.kx.loanapp.api.LoanAppApi;
import kalix.javasdk.eventsourcedentity.EventSourcedEntity;
import kalix.javasdk.eventsourcedentity.EventSourcedEntity.Effect;
import kalix.javasdk.eventsourcedentity.EventSourcedEntityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Event Sourced Entity Service described in your io/kx/loanapp/api/loan_app_api.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class LoanAppEntity extends AbstractLoanAppEntity {

  @SuppressWarnings("unused")
  Logger logger = LoggerFactory.getLogger("LoanAppEntity");
  private final String entityId;

  public LoanAppEntity(EventSourcedEntityContext context) {
    this.entityId = context.entityId();
  }

  @Override
  public LoanAppDomain.LoanAppDomainState emptyState() {
    return LoanAppDomain.LoanAppDomainState.getDefaultInstance();
  }

  @Override
  public Effect<Empty> submit(LoanAppDomain.LoanAppDomainState currentState, LoanAppApi.SubmitCommand submitCommand) {
    if(currentState.equals(LoanAppDomain.LoanAppDomainState.getDefaultInstance())) {
      //create a submittted event
      LoanAppDomain.Submitted submittedEvent = LoanAppDomain.Submitted.newBuilder()
              .setLoanAppId(submitCommand.getLoanAppId())
              .setClientId(submitCommand.getClientId())
              .setClientMonthlyIncomeCents(1000000)
              .setLoanAmountCents(4000000)
              .setLoanDurationMonths(28)
              .setEventTimestamp(Timestamps.fromMillis(System.currentTimeMillis()))
              .build();

      return effects().emitEvent(submittedEvent).thenReply(any -> Empty.getDefaultInstance());
    } else if (currentState.getStatus() == LoanAppDomain.LoanAppDomainStatus.STATUS_IN_REVIEW) {
      return effects().reply(Empty.getDefaultInstance());
    } else {
      return effects().error("STATUS IS WRONG OR STH");
    }
  }

  @Override
  public Effect<LoanAppApi.LoanAppState> get(LoanAppDomain.LoanAppDomainState currentState, LoanAppApi.GetCommand getCommand) {
    LoanAppApi.LoanAppState state = LoanAppApi.LoanAppState.newBuilder()
            .setClientId(currentState.getClientId())
            .setClientMonthlyIncomeCents(currentState.getClientMonthlyIncomeCents())
            .setLoanAmountCents(currentState.getLoanAmountCents())
            .setLoanDurationMonths(currentState.getLoanDurationMonths())
            .setStatus(LoanAppApi.LoanAppStatus.forNumber(currentState.getStatus().getNumber()))
            .build();
    return effects().reply(state);
  }

  @Override
  public Effect<Empty> approve(LoanAppDomain.LoanAppDomainState currentState, LoanAppApi.ApproveCommand approveCommand) {
    if(currentState.equals(LoanAppDomain.LoanAppDomainState.getDefaultInstance())) {
      return effects().error("NOT FOUND");
    } else if (currentState.getStatus() == LoanAppDomain.LoanAppDomainStatus.STATUS_IN_REVIEW) {
      LoanAppDomain.Approved approvedEvent = LoanAppDomain.Approved.newBuilder()
              .setLoanAppId(approveCommand.getLoanAppId())
              .setEventTimestamp(Timestamps.fromMillis(System.currentTimeMillis()))
              .build();
      return effects().emitEvent(approvedEvent).thenReply(any -> Empty.getDefaultInstance());
    } else if(currentState.getStatus() == LoanAppDomain.LoanAppDomainStatus.STATUS_APPROVED) {
      return effects().reply(Empty.getDefaultInstance());
    } else {
      return effects().error("WRONG STATUS");
    }
  }

  @Override
  public Effect<Empty> decline(LoanAppDomain.LoanAppDomainState currentState, LoanAppApi.DeclineCommand declineCommand) {
    if(currentState.equals(LoanAppDomain.LoanAppDomainState.getDefaultInstance())) {
      return effects().error("NOT FOUND");
    } else if (currentState.getStatus() == LoanAppDomain.LoanAppDomainStatus.STATUS_IN_REVIEW) {
      LoanAppDomain.Declined declinedEvent = LoanAppDomain.Declined.newBuilder()
              .setReason(declineCommand.getReason())
              .setLoanAppId(declineCommand.getLoanAppId())
              .setEventTimestamp(Timestamps.fromMillis(System.currentTimeMillis()))
              .build();
      return effects().emitEvent(declinedEvent).thenReply(any -> Empty.getDefaultInstance());
    } else {
      return effects().error("STATUS ERROR");
    }
  }

  @Override
  public LoanAppDomain.LoanAppDomainState submitted(LoanAppDomain.LoanAppDomainState currentState, LoanAppDomain.Submitted submitted) {
    return LoanAppDomain.LoanAppDomainState.newBuilder()
            .setStatus(LoanAppDomain.LoanAppDomainStatus.STATUS_IN_REVIEW)
            .setClientId(submitted.getClientId())
            .setLoanDurationMonths(submitted.getLoanDurationMonths())
            .setLastUpdateTimestamp(submitted.getEventTimestamp())
            .setClientMonthlyIncomeCents(submitted.getClientMonthlyIncomeCents())
            .setLoanAmountCents(submitted.getLoanAmountCents())
            .build();
  }
  @Override
  public LoanAppDomain.LoanAppDomainState approved(LoanAppDomain.LoanAppDomainState currentState, LoanAppDomain.Approved approved) {
    return currentState.toBuilder()
            .setStatus(LoanAppDomain.LoanAppDomainStatus.STATUS_APPROVED)
            .setLastUpdateTimestamp(approved.getEventTimestamp())
            .build();
  }
  @Override
  public LoanAppDomain.LoanAppDomainState declined(LoanAppDomain.LoanAppDomainState currentState, LoanAppDomain.Declined declined) {
    return currentState.toBuilder()
            .setStatus(LoanAppDomain.LoanAppDomainStatus.STATUS_DECLINED)
            .setLastUpdateTimestamp(declined.getEventTimestamp())
            .build();
  }

}
