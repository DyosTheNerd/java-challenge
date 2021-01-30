package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.dto.EmployeeDTO;
import jp.co.axa.apidemo.dto.EmployeeReturnDTO;
import jp.co.axa.apidemo.dto.ResourceHeaderDTO;
import jp.co.axa.apidemo.dto.SuccessDTO;
import jp.co.axa.apidemo.entities.Employee;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {

     Page<Employee> retrieveEmployees(int page, int size);

     EmployeeReturnDTO getEmployee(Long employeeId);

     ResourceHeaderDTO saveEmployee(EmployeeDTO employee);

     SuccessDTO deleteEmployee(Long employeeId, Integer version);

     ResourceHeaderDTO updateEmployee(EmployeeDTO employee,Long employeeID, Integer version);
}