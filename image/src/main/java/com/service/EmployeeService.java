package com.service;

import com.model.Employee;
import com.model.EmployeeTaxDetailsDTO;
import com.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public void saveEmployee(Employee employee) {
        validateEmployee(employee);
        employeeRepository.save(employee);
    }

    public EmployeeTaxDetailsDTO calculateTax(String employeeId) {
        Employee employee = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found!"));

        LocalDate doj = employee.getDateOfJoining();
        LocalDate currentDate = LocalDate.now();
        int monthsWorked = (int) ChronoUnit.MONTHS.between(doj.withDayOfMonth(1), currentDate.withDayOfMonth(1));

        if (doj.getDayOfMonth() > 1) {
            monthsWorked -= 1; // Exclude incomplete joining month
        }

        double yearlySalary = employee.getMonthlySalary() * monthsWorked;
        double taxAmount = calculateTaxAmount(yearlySalary);
        double cessAmount = yearlySalary > 2500000 ? (yearlySalary - 2500000) * 0.02 : 0.0;

        EmployeeTaxDetailsDTO taxDetails = new EmployeeTaxDetailsDTO();
        taxDetails.setEmployeeId(employee.getEmployeeId());
        taxDetails.setFirstName(employee.getFirstName());
        taxDetails.setLastName(employee.getLastName());
        taxDetails.setYearlySalary(yearlySalary);
        taxDetails.setTaxAmount(taxAmount);
        taxDetails.setCessAmount(cessAmount);

        return taxDetails;
    }

    private double calculateTaxAmount(double yearlySalary) {
        double tax = 0.0;

        if (yearlySalary > 250000 && yearlySalary <= 500000) {
            tax = (yearlySalary - 250000) * 0.05;
        } else if (yearlySalary > 500000 && yearlySalary <= 1000000) {
            tax = 12500 + (yearlySalary - 500000) * 0.10;
        } else if (yearlySalary > 1000000) {
            tax = 62500 + (yearlySalary - 1000000) * 0.20;
        }

        return tax;
    }

    private void validateEmployee(Employee employee) {
        if (employee.getMonthlySalary() <= 0) {
            throw new IllegalArgumentException("Salary must be positive!");
        }
        if (employee.getPhoneNumbers() == null || employee.getPhoneNumbers().isEmpty()) {
            throw new IllegalArgumentException("At least one phone number is required!");
        }
    }
}

