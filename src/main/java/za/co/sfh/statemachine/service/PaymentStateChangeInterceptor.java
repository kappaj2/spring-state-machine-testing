package za.co.sfh.statemachine.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;
import za.co.sfh.statemachine.domain.Payment;
import za.co.sfh.statemachine.domain.PaymentEvent;
import za.co.sfh.statemachine.domain.PaymentState;
import za.co.sfh.statemachine.repository.PaymentRepository;
import za.co.sfh.statemachine.service.impl.PaymentServiceImpl;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentStateChangeInterceptor extends StateMachineInterceptorAdapter<PaymentState, PaymentEvent> {

     private final PaymentRepository paymentRepository;

     @Override
     public void preStateChange(State<PaymentState, PaymentEvent> state,
                                Message<PaymentEvent> message,
                                Transition<PaymentState, PaymentEvent> transition,
                                StateMachine<PaymentState, PaymentEvent> stateMachine,
                                StateMachine<PaymentState, PaymentEvent> rootStateMachine) {

          Optional.ofNullable(message).ifPresent(msg->{
               Optional.ofNullable((Long.class.cast(msg.getHeaders().getOrDefault(PaymentServiceImpl.PAYMENT_ID_HEADER, -1))))
                       .ifPresent(paymentId ->{
                            Payment payment = paymentRepository.getOne(paymentId);
                            payment.setPaymentState(state.getId());
                            paymentRepository.save(payment);
                       });
          });

     }
}
