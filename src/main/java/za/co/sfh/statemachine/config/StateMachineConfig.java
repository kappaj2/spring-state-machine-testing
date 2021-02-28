package za.co.sfh.statemachine.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import za.co.sfh.statemachine.domain.PaymentEvent;
import za.co.sfh.statemachine.domain.PaymentState;

import java.util.EnumSet;
import java.util.Random;

import static za.co.sfh.statemachine.service.impl.PaymentServiceImpl.PAYMENT_ID_HEADER;

@EnableStateMachineFactory
@Configuration
@RequiredArgsConstructor
@Slf4j
public class StateMachineConfig extends StateMachineConfigurerAdapter<PaymentState, PaymentEvent> {

     private final Action<PaymentState, PaymentEvent> preAuthAction;
     private final Action<PaymentState, PaymentEvent> authAction;
     private final Guard<PaymentState, PaymentEvent> paymentIdGuard;
     private final Action<PaymentState, PaymentEvent> preAuthApprovedAction;
     private final Action<PaymentState, PaymentEvent> preAuthDeclinedAction;
     private final Action<PaymentState, PaymentEvent> authApprovedAction;
     private final Action<PaymentState, PaymentEvent> authDeclinedAction;

     @Override
     public void configure(StateMachineConfigurationConfigurer<PaymentState, PaymentEvent> config) throws Exception {

          //   Multiple methods here that we can override, stateChanged, stateEntered, stateExited, transitionStarted etc
          StateMachineListenerAdapter<PaymentState, PaymentEvent> adapter = new StateMachineListenerAdapter<>() {
               @Override
               public void stateChanged(State<PaymentState, PaymentEvent> from, State<PaymentState, PaymentEvent> to) {
                    log.info(String.format("stateChanged(fromState : %s, toState: %s)", from, to));
               }
          };

          config.withConfiguration()
                  .listener(adapter);
     }

//     @Override
//     public void configure(StateMachineConfigurationConfigurer<PaymentState, PaymentEvent> config)
//             throws Exception {
//          config
//                  .withConfiguration()
//                  .autoStartup(true)
//                  .listener(listener());
//     }

     @Override
     public void configure(StateMachineStateConfigurer<PaymentState, PaymentEvent> states) throws Exception {
          states.withStates()
                  .initial(PaymentState.NEW)
                  .states(EnumSet.allOf(PaymentState.class))
                  .end(PaymentState.AUTH)
                  .end(PaymentState.PRE_AUTH_ERROR)
                  .end(PaymentState.AUTH_ERROR);
     }

     public void configure(StateMachineTransitionConfigurer<PaymentState, PaymentEvent> transitions) throws Exception {
          transitions.withExternal().source(PaymentState.NEW).target(PaymentState.NEW).event(PaymentEvent.PRE_AUTHORIZE)
                  .action(preAuthAction).guard(paymentIdGuard)
                  .and()
                  .withExternal().source(PaymentState.NEW).target(PaymentState.PRE_AUTH).event(PaymentEvent.PRE_AUTH_APPROVED)
                  .action(preAuthApprovedAction)
                  .and()
                  .withExternal().source(PaymentState.NEW).target(PaymentState.PRE_AUTH_ERROR).event(PaymentEvent.PRE_AUTH_DECLINED)
                  .action(preAuthDeclinedAction)
                  //preauth to auth
                  .and()
                  .withExternal().source(PaymentState.PRE_AUTH).target(PaymentState.PRE_AUTH).event(PaymentEvent.AUTHORIZE)
                  .action(authAction)
                  .and()
                  .withExternal().source(PaymentState.PRE_AUTH).target(PaymentState.AUTH).event(PaymentEvent.AUTH_APPROVED)
                  .action(authApprovedAction)
                  .and()
                  .withExternal().source(PaymentState.PRE_AUTH).target(PaymentState.AUTH_ERROR).event(PaymentEvent.AUTH_DECLINED)
                  .action(authDeclinedAction);
     }


     public Action<PaymentState, PaymentEvent> preAuthAction() {
          return stateContext -> {
               System.out.println("PreAuth was called");
               if (new Random().nextInt(10) < 8) {
                    stateContext.getStateMachine().sendEvent(MessageBuilder.withPayload(PaymentEvent.PRE_AUTH_APPROVED)
                            .setHeader(PAYMENT_ID_HEADER, stateContext.getMessageHeader(PAYMENT_ID_HEADER))
                            .build());
               } else {
                    System.out.println("Preauth was declined");
                    stateContext.getStateMachine().sendEvent(MessageBuilder.withPayload(PaymentEvent.PRE_AUTH_DECLINED)
                            .setHeader(PAYMENT_ID_HEADER, stateContext.getMessageHeader(PAYMENT_ID_HEADER))
                            .build());
               }
          };
     }

//     @Bean
//     public StateMachineListener<PaymentState, PaymentEvent> listener() {
//          return new StateMachineListenerAdapter<PaymentState, PaymentEvent>() {
//               @Override
//               public void stateChanged(State<PaymentState, PaymentEvent> from, State<PaymentState, PaymentEvent> to) {
//                    super.stateChanged(from, to);
//               }
//          };
//     }

}
