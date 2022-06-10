package com.company.employeestate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.company.employeestate.model.Employee;
import com.company.employeestate.model.EmployeeEvent;
import com.company.employeestate.model.EmployeeState;
import com.company.employeestate.service.EmployeeService;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private MockMvc mockMvc;
	
	private Employee employee;
	
	@BeforeEach
	public void init() {
		List<EmployeeState> list = new ArrayList<>();
		list.add(EmployeeState.ADDED);
		
		employee = new Employee();
		employee.setName(UUID.randomUUID().toString());
		employee.setState(list);
	}
	
	@Test
	public void getEmployee() throws Exception {
		employeeService.addEmployee(employee);
		mockMvc.perform(MockMvcRequestBuilders.get("/get?id=" + employee.getId())).andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void updateState() throws Exception {
		MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
		params.add("id", String.valueOf(employee.getId()));
		params.add("event", EmployeeEvent.BEGIN_CHECK.toString());
		mockMvc.perform(MockMvcRequestBuilders.put("/update").params(params)).andExpect(MockMvcResultMatchers.status().isOk());
	}
}
