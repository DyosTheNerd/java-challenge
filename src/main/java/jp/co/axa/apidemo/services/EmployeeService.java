package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.dto.EmployeeDTO;
import jp.co.axa.apidemo.dto.EmployeeReturnDTO;
import jp.co.axa.apidemo.dto.ResourceHeaderDTO;
import jp.co.axa.apidemo.dto.SuccessDTO;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.LockingException;
import jp.co.axa.apidemo.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {


     /**
      * Retrieves a Page of employees, ordered by id.
      * @param page the page number
      * @param size the page size
      * @return The Pageable List of Employees
      */
     Page<Employee> retrieveEmployees(int page, int size);

     /**
      * Get an Employee of ID.
      * @param employeeId the ID of the Employee
      * @return A DTO representing the Employee.
      */
     EmployeeReturnDTO getEmployee(Long employeeId);

     /**
      * save a new employee object. no duplication check is performed.
      * @param employee the information to be stored.
      * @return The header information of the new object.
      */
     ResourceHeaderDTO saveEmployee(EmployeeDTO employee);

     /**
      * Delete the employee, if version is correct
      *
      * evicts the cache
      *
      * @param employeeId the id of the deletable object.
      * @param version the known version of the entity
      * @return A success wrapper
      * @throws LockingException if version is wrong
      * @throws ResourceNotFoundException if resource does not exist
      */
     SuccessDTO deleteEmployee(Long employeeId, Integer version);

     /**
      * update the employee in the database, if the version number is correct.
      *
      * evicts the cache (instead of put, due to return values being different from the get method).
      *
      * @param employee The new employee information
      * @param employeeId the id of the employee to be updated
      * @param version the version of the employee to be updated
      * @return
      * @throws LockingException if the version number is not correct
      * @throws ResourceNotFoundException if the resource does not exist
      */
     ResourceHeaderDTO updateEmployee(EmployeeDTO employee,Long employeeID, Integer version);
}