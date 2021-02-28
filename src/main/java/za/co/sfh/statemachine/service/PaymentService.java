package za.co.sfh.statemachine.service;

import org.springframework.statemachine.StateMachine;
import za.co.sfh.statemachine.domain.Payment;
import za.co.sfh.statemachine.domain.PaymentEvent;
import za.co.sfh.statemachine.domain.PaymentState;

public interface PaymentService {

     Payment newPayment(Payment payment);

     StateMachine<PaymentState, PaymentEvent> preAuth(Long paymentId);
     StateMachine<PaymentState, PaymentEvent> authorizePayment(Long paymentId);
     StateMachine<PaymentState, PaymentEvent> declineAuth(Long paymentId);
}
