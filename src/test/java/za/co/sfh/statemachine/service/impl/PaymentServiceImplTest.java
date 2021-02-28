package za.co.sfh.statemachine.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.transaction.annotation.Transactional;
import za.co.sfh.statemachine.domain.Payment;
import za.co.sfh.statemachine.domain.PaymentEvent;
import za.co.sfh.statemachine.domain.PaymentState;
import za.co.sfh.statemachine.repository.PaymentRepository;
import za.co.sfh.statemachine.service.PaymentService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class PaymentServiceImplTest {

     @Autowired
     private PaymentService paymentService;

     @Autowired
     private PaymentRepository paymentRepository;

     Payment payment;

     @BeforeEach
     void setup(){
          payment = Payment.builder()
                  .amount(new BigDecimal("12.99"))
                  .build();
     }

     @Test
     @Transactional
     void preAuth() {

          Payment savedPayment = paymentService.newPayment(payment);

          System.out.println("State should be NEW");
          System.out.println(savedPayment.getPaymentState());

          StateMachine<PaymentState, PaymentEvent> sm = paymentService.preAuth(savedPayment.getId());

          Payment preAuthedPayment = paymentRepository.getOne(savedPayment.getId());

          System.out.println("Should be PRE_AUTH or PRE_AUTH_ERROR");//Random declined in action
          System.out.println("StateMachine state : "+sm.getState().getId());
          System.out.println(preAuthedPayment);
     }

     @Transactional
     @RepeatedTest(10)
     void testAuth() {
          Payment savedPayment = paymentService.newPayment(payment);

          StateMachine<PaymentState, PaymentEvent> preAuthSM = paymentService.preAuth(savedPayment.getId());

          if (preAuthSM.getState().getId() == PaymentState.PRE_AUTH) {
               System.out.println("Payment is Pre Authorized");

               StateMachine<PaymentState, PaymentEvent> authSM = paymentService.authorizePayment(savedPayment.getId());

               System.out.println("Result of Auth: " + authSM.getState().getId());
          } else {
               System.out.println("Payment failed pre-auth...");
          }
     }
}