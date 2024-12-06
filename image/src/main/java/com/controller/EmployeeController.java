package com.controller;

import com.model.Employee;
import com.model.EmployeeTaxDetailsDTO;
import com.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<String> createEmployee(@RequestBody Employee employee) {
        employeeService.saveEmployee(employee);
        return ResponseEntity.ok("Employee created successfully!");
    }

    @GetMapping("/{employeeId}/tax-details")
    public ResponseEntity<EmployeeTaxDetailsDTO> getTaxDetails(@PathVariable String employeeId) {
        return ResponseEntity.ok(employeeService.calculateTax(employeeId));
    }
}
