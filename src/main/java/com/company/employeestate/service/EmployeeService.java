package com.company.employeestate.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

import com.company.employeestate.model.Employee;
import com.company.employeestate.model.EmployeeEvent;
import com.company.employeestate.model.EmployeeState;
import com.company.employeestate.repository.EmployeeRepository;

import reactor.core.publisher.Mono;

@Service
@Transactional
public class EmployeeService {
	
	@Autowired
	private StateMachineFactory<EmployeeState, EmployeeEvent> stateMachineFactory;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	public Employee addEmployee(Employee employee) {
		return employeeRepository.save(employee);
	}
	
	public Employee getEmployee(Integer id) {	
		return employeeRepository.findById(id).orElse(null);
	}
	
	public Employee updateState(Integer id, String event) {
		
		Employee employee = getEmployee(id);
		
		if (employee == null)
			return null;
		
		//build a new state machine instance
		StateMachine<EmployeeState, EmployeeEvent> stateMachine = stateMachineFactory.getStateMachine(String.valueOf(employee.getId()));
		
		//reset state(s) of the machine and start it
		stateMachine.getStateMachineAccessor().doWithAllRegions(access -> {
			List<EmployeeState> states = new ArrayList<EmployeeState>(employee.getState());
			int numStates = states.size();
			DefaultStateMachineContext<EmployeeState,EmployeeEvent> context;
			
			if (numStates > 1) {
				List<StateMachineContext<EmployeeState, EmployeeEvent>> contextList = new ArrayList<>();
				states.subList(1, employee.getState().size()).forEach(state -> contextList.add(new DefaultStateMachineContext<EmployeeState, EmployeeEvent>(state, null, null, null, null)));
				context = new DefaultStateMachineContext<EmployeeState, EmployeeEvent>(contextList, states.get(0), null, null, null, null);
			} else {	
				context = new DefaultStateMachineContext<EmployeeState, EmployeeEvent>(states.get(0), null, null, null, null);
			}
			access.resetStateMachineReactively(context).subscribe();
		});
		stateMachine.startReactively().subscribe();
		
		//send event
		Message<EmployeeEvent> message = MessageBuilder.withPayload(Enum.valueOf(EmployeeEvent.class, event))
				.setHeader("employeeId", employee.getId())
				.build();
		Mono<Message<EmployeeEvent>> mono = Mono.just(message);
		stateMachine.sendEvent(mono).subscribe();
		
		//update with the new state(s)
		employee.setState(new ArrayList<EmployeeState>(stateMachine.getState().getIds()));
		return employeeRepository.save(employee);
	}
}

