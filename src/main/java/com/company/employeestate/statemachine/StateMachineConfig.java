package com.company.employeestate.statemachine;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import com.company.employeestate.model.EmployeeEvent;
import com.company.employeestate.model.EmployeeState;

@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<EmployeeState, EmployeeEvent> {

	@Override
	public void configure(StateMachineStateConfigurer<EmployeeState, EmployeeEvent> states) throws Exception {
		states
		.withStates()
			.initial(EmployeeState.ADDED)
			.fork(EmployeeState.IN_CHECK)
			.join(EmployeeState.ALL_CHECKS_FINISHED)
			.state(EmployeeState.APPROVED)
			.end(EmployeeState.ACTIVE)
			.and()
		.withStates()
			.parent(EmployeeState.IN_CHECK)
			.initial(EmployeeState.SECURITY_CHECK_STARTED)
			.end(EmployeeState.SECURITY_CHECK_FINISHED)
			.and()
		.withStates()
			.parent(EmployeeState.IN_CHECK)
			.initial(EmployeeState.WORK_PERMIT_CHECK_STARTED)
			.state(EmployeeState.WORK_PERMIT_CHECK_PENDING_VERIFICATION)
			.end(EmployeeState.WORK_PERMIT_CHECK_FINISHED);
	}
	
	@Override
	public void configure(StateMachineTransitionConfigurer<EmployeeState, EmployeeEvent> transitions) throws Exception {
		transitions
			.withExternal()
				.source(EmployeeState.ADDED)
				.target(EmployeeState.IN_CHECK)
				.event(EmployeeEvent.BEGIN_CHECK)
				.and()
			.withExternal()
				.source(EmployeeState.SECURITY_CHECK_STARTED)
				.target(EmployeeState.SECURITY_CHECK_FINISHED)
				.event(EmployeeEvent.FINISH_SECURITY_CHECK)
				.and()
			.withExternal()
				.source(EmployeeState.WORK_PERMIT_CHECK_STARTED)
				.target(EmployeeState.WORK_PERMIT_CHECK_PENDING_VERIFICATION)
				.event(EmployeeEvent.COMPLETE_INITIAL_WORK_PERMIT_CHECK)
				.and()
			.withExternal()
				.source(EmployeeState.WORK_PERMIT_CHECK_PENDING_VERIFICATION)
				.target(EmployeeState.WORK_PERMIT_CHECK_FINISHED)
				.event(EmployeeEvent.FINISH_WORK_PERMIT_CHECK)
				.and()
			.withExternal()
				.source(EmployeeState.ALL_CHECKS_FINISHED)
				.target(EmployeeState.APPROVED)
				.and()
			.withExternal()
				.source(EmployeeState.APPROVED)
				.target(EmployeeState.ACTIVE)
				.event(EmployeeEvent.ACTIVATE)
				.and()
			.withFork()
				.source(EmployeeState.IN_CHECK)
				.target(EmployeeState.SECURITY_CHECK_STARTED)
				.target(EmployeeState.WORK_PERMIT_CHECK_STARTED)
				.and()
			.withJoin()
				.source(EmployeeState.SECURITY_CHECK_FINISHED)
				.source(EmployeeState.WORK_PERMIT_CHECK_FINISHED)
				.target(EmployeeState.ALL_CHECKS_FINISHED);
	}
}
