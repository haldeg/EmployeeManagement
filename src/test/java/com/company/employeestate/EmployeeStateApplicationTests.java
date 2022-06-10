package com.company.employeestate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import com.company.employeestate.controller.EmployeeController;
import com.company.employeestate.model.EmployeeEvent;
import com.company.employeestate.model.EmployeeState;
import com.company.employeestate.service.EmployeeService;

import reactor.core.publisher.Mono;

@SpringBootTest
public class EmployeeStateApplicationTests {

	@Autowired
	private StateMachineFactory<EmployeeState, EmployeeEvent> stateMachineFactory;
	
	@Autowired
	EmployeeController employeeController;
	
	@Autowired
	EmployeeService employeeService;
	
	private StateMachine<EmployeeState, EmployeeEvent> stateMachine;
	
	@BeforeEach
	public void init() {
		stateMachine = stateMachineFactory.getStateMachine();
		stateMachine.startReactively().subscribe();
	}
	
	@Test
	public void contextLoads() {
		Assertions.assertThat(employeeController).isNotNull();
		Assertions.assertThat(employeeService).isNotNull();
	}
	
	@Test
	public void testStateMachineConfig() {
		
		sendEvent(EmployeeEvent.BEGIN_CHECK);
		
		assertEquals(stateMachine.getState().getId(), EmployeeState.IN_CHECK);
		
		sendEvent(EmployeeEvent.FINISH_SECURITY_CHECK);
		sendEvent(EmployeeEvent.COMPLETE_INITIAL_WORK_PERMIT_CHECK);
		sendEvent(EmployeeEvent.FINISH_WORK_PERMIT_CHECK);
		
		assertEquals(stateMachine.getState().getId(), EmployeeState.APPROVED);
		
		sendEvent(EmployeeEvent.ACTIVATE);
		
		assertEquals(stateMachine.getState().getId(), EmployeeState.ACTIVE);
	}

	private void sendEvent(EmployeeEvent employeeEvent) {
		Message<EmployeeEvent> message = MessageBuilder.withPayload(employeeEvent).build();
		Mono<Message<EmployeeEvent>> mono = Mono.just(message);
		stateMachine.sendEvent(mono).subscribe();
	}
	
}
