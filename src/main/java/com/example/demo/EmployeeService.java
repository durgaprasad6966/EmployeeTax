package com.example.demo;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EmployeeService {
	@Autowired
    private EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee) {
        double totalSalary = employee.getMonthlySalary() * (employee.getDateOfJoining().getMonthValue() - LocalDate.now().getMonthValue() + 1);
        double lossOfPayPerDay = employee.getMonthlySalary() / 30;

        employee.setTotalSalary(totalSalary);
        employee.setLossOfPayPerDay(lossOfPayPerDay);

        return employeeRepository.save(employee);
}
    
    public TaxInfo calculateTax(Employee employee) {
        double yearlySalary = employee.getTotalSalary();
        double taxAmount = 0;
        double cessAmount = 0;
        double slab1 = 250000;
        double slab2 = 500000;
        double slab3 = 1000000;

        if (yearlySalary <= slab1) {
           taxAmount = 0;
        } else if (yearlySalary > slab1 && yearlySalary <= slab2) {
           taxAmount = (yearlySalary - slab1) * 0.005;
        } else if (yearlySalary > slab2 && yearlySalary <= slab3) {
           taxAmount = (slab1 * 0.005) + ((yearlySalary - slab2) * 0.1);
        } else {
           taxAmount = (slab1 * 0.005) + (slab2 * 0.1) + ((yearlySalary - slab3) * 0.2);
        }

        if (yearlySalary > 2500000) {
            cessAmount = (yearlySalary - 2500000) * 0.02;
        }

        TaxInfo taxInfo = new TaxInfo();
        taxInfo.setEmployeeCode(employee.getEmployeeId());
        taxInfo.setFirstName(employee.getFirstName());
        taxInfo.setLastName(employee.getLastName());
        taxInfo.setYearlySalary(yearlySalary);
        taxInfo.setTaxAmount(taxAmount);
        taxInfo.setCessAmount(cessAmount);

        return taxInfo;
    }

    public Employee getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + employeeId));
    }
}
