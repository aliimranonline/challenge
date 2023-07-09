package com.mainteny.challenge.jobConfigs;

import com.mainteny.challenge.daos.Employee;
import org.springframework.batch.item.ItemProcessor;

public class EmployeeProcessor implements ItemProcessor<Employee, Employee> {
    @Override
    public Employee process(Employee item) throws Exception {
        return item; //ATM we are not doing any processing just returning
    }
}
