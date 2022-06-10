package com.company.employeestate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.company.employeestate.model.Employee;
import com.company.employeestate.service.EmployeeService;

import io.swagger.annotations.ApiOperation;

@RestController
public class EmployeeController {

	private final EmployeeService employeeService;
	
	@Autowired
	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	@ApiOperation(value = "Add employee")
	@PostMapping(value = "/add")
	public ResponseEntity<Object> addEmployee(@RequestBody Employee employee) {
		
		try {
			employeeService.addEmployee(employee);
		} catch (Exception e) {
			throw new ResponseStatusException( 
					HttpStatus.BAD_REQUEST, e.toString() 
			); 
		}
		
		return ResponseEntity.ok(employee);
	}
	
	@ApiOperation(value = "Update employee state")
	@PutMapping(value = "/update")
	public ResponseEntity<Object> updateState(@RequestParam Integer id, @RequestParam String event) {
		
		Employee employee = null;
		try {
			employee = employeeService.updateState(id, event);
		} catch (Exception e) {
			throw new ResponseStatusException( 
					HttpStatus.BAD_REQUEST, e.toString() 
			); 
		}

		return ResponseEntity.ok(employee);
	}
	
	@ApiOperation(value = "Get employee data")
	@GetMapping(value = "/get")
	public ResponseEntity<Object> getEmployee(@RequestParam Integer id) {
		
		Employee employee = employeeService.getEmployee(id);
		
		if (employee == null) {			
			return new ResponseEntity<Object>(ResponseEntity.notFound().build(), HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(employee);
	}
}
