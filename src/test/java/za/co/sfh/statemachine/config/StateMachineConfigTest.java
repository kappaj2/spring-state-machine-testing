package za.co.sfh.statemachine.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import za.co.sfh.statemachine.domain.PaymentEvent;
import za.co.sfh.statemachine.domain.PaymentState;

import java.util.UUID;

@Slf4j
@SpringBootTest
class StateMachineConfigTest {

     @Autowired
     private StateMachineFactory<PaymentState, PaymentEvent> factory;

     @Test
     void testNewStateMachine() {
          StateMachine<PaymentState, PaymentEvent> sm = factory.getStateMachine(UUID.randomUUID());

          sm.start();

          System.out.println(sm.getState().toString());

          sm.sendEvent(PaymentEvent.PRE_AUTHORIZE);

          System.out.println(sm.getState().toString());

          sm.sendEvent(PaymentEvent.PRE_AUTH_APPROVED);

          System.out.println(sm.getState().toString());

          sm.sendEvent(PaymentEvent.PRE_AUTH_DECLINED);

          System.out.println(sm.getState().toString());

     }
}