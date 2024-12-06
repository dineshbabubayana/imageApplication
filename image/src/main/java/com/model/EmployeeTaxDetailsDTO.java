package com.model;
import lombok.Data;

@Data
public class EmployeeTaxDetailsDTO {
    private String employeeId;
    private String firstName;
    private String lastName;
    private Double yearlySalary;
    private Double taxAmount;
    private Double cessAmount;
}
