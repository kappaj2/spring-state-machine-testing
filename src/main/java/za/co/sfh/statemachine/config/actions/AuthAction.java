package za.co.sfh.statemachine.config.actions;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import za.co.sfh.statemachine.domain.PaymentEvent;
import za.co.sfh.statemachine.domain.PaymentState;

import java.util.Random;

import static za.co.sfh.statemachine.service.impl.PaymentServiceImpl.PAYMENT_ID_HEADER;

@Component
public class AuthAction implements Action<PaymentState, PaymentEvent> {

     @Override
     public void execute(StateContext<PaymentState, PaymentEvent> context) {
          System.out.println("Auth was called!!!");

          if (new Random().nextInt(10) < 8) {
               System.out.println("Auth Approved");
               context.getStateMachine().sendEvent(MessageBuilder.withPayload(PaymentEvent.AUTH_APPROVED)
                       .setHeader(PAYMENT_ID_HEADER, context.getMessageHeader(PAYMENT_ID_HEADER))
                       .build());

          } else {
               System.out.println("Auth Declined! No Credit!!!!!!");
               context.getStateMachine().sendEvent(MessageBuilder.withPayload(PaymentEvent.AUTH_DECLINED)
                       .setHeader(PAYMENT_ID_HEADER, context.getMessageHeader(PAYMENT_ID_HEADER))
                       .build());
          }
     }
}
