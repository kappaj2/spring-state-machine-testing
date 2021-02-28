package za.co.sfh.statemachine.config.actions;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import za.co.sfh.statemachine.domain.PaymentEvent;
import za.co.sfh.statemachine.domain.PaymentState;

@Component
public class AuthDeclinedAction implements Action<PaymentState, PaymentEvent> {
     @Override
     public void execute(StateContext<PaymentState, PaymentEvent> context) {
          System.out.println("Sending Notification of Auth DECLINED");
     }
}
