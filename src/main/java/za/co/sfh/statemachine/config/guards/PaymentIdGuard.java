package za.co.sfh.statemachine.config.guards;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;
import za.co.sfh.statemachine.domain.PaymentEvent;
import za.co.sfh.statemachine.domain.PaymentState;

import static za.co.sfh.statemachine.service.impl.PaymentServiceImpl.PAYMENT_ID_HEADER;

@Component
public class PaymentIdGuard implements Guard<PaymentState, PaymentEvent> {

     @Override
     public boolean evaluate(StateContext<PaymentState, PaymentEvent> context) {
          return context.getMessageHeader(PAYMENT_ID_HEADER) != null;
     }
}