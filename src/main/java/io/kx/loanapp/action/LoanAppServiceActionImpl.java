package io.kx.loanapp.action;

import akka.Done;
import com.google.protobuf.Empty;
import io.kx.loanapp.api.LoanAppApi;
import kalix.javasdk.action.ActionCreationContext;

import java.time.Duration;
import java.util.concurrent.CompletionStage;
import java.util.logging.Logger;

// This class was initially generated based on the .proto definition by Kalix tooling.
// This is the implementation for the Action Service described in your io/kx/loanapp/action/loan_app_action.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class LoanAppServiceActionImpl extends AbstractLoanAppServiceAction {

  private static Logger logger = Logger.getLogger("LoanAppServiceActionImpl");
  public LoanAppServiceActionImpl(ActionCreationContext creationContext) {}

  @Override
  public Effect<Empty> submitLoanAppAction(LoanAppApi.SubmitCommand submitCommand) {
    CompletionStage<Done> timerRegistration = timers().startSingleTimer(
            submitCommand.getLoanAppId(), Duration.ofSeconds(20),
            components().loanAppServiceActionImpl().expireAction(
                    LoanAppApi.DeclineCommand.newBuilder()
                            .setLoanAppId(submitCommand.getLoanAppId())
                            .setReason("")
                            .build()));

    return effects().asyncReply(
            timerRegistration.thenCompose(done -> components().loanAppEntity().submit(submitCommand).execute())
                    .thenApply(empty -> Empty.getDefaultInstance())
    );
  }
  @Override
  public Effect<Empty> expireAction(LoanAppApi.DeclineCommand declineCommand) {
    logger.info("############");
    logger.info("Expiring Loan app with id " + declineCommand.getLoanAppId());
    logger.info("############");
    return effects().forward(components().loanAppEntity().decline(declineCommand));
  }
}
