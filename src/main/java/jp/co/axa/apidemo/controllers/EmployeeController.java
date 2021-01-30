package jp.co.axa.apidemo.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import jp.co.axa.apidemo.dto.EmployeeDTO;
import jp.co.axa.apidemo.dto.EmployeeReturnDTO;
import jp.co.axa.apidemo.dto.ResourceHeaderDTO;
import jp.co.axa.apidemo.dto.SuccessDTO;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.ResourceNotFoundException;
import jp.co.axa.apidemo.services.EmployeeService;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    @Setter
    private EmployeeService employeeService;


    Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    ApplicationEventPublisher eventPublisher;


    @ApiOperation(value = "Get a page of employees.")
    @GetMapping("/employees")
    public List<Employee> getEmployees(@RequestParam("page") int page,
                                       @RequestParam("size") int size) {

        Page<Employee> resultPage = employeeService.retrieveEmployees(page, size);
        if (page > resultPage.getTotalPages()) {
            throw new ResourceNotFoundException(page,"Page");
        }
        logger.debug("Employees Pagination Retrieved");

        return resultPage.getContent();




    }

    @ApiOperation(value = "Get a single employee with id, if exists.")
    @GetMapping("/employees/{employeeId}")
    public EmployeeReturnDTO getEmployee(@PathVariable(name="employeeId")Long employeeId) {
        logger.debug("Getting Single Employee");
        return employeeService.getEmployee(employeeId);
    }

    @ApiOperation(value = "Create a new employee.")
    @PostMapping("/employees")
    public ResourceHeaderDTO saveEmployee( @RequestBody @Valid EmployeeDTO employee){
        ResourceHeaderDTO header = employeeService.saveEmployee(employee);
        logger.debug("Employee Saved Successfully");
        return  header;
    }

    @ApiOperation(value = "Delete an employee with id, if exists and version correct.")
    @DeleteMapping("/employees/{employeeId}/{version}")
    public SuccessDTO deleteEmployee(@PathVariable(name="employeeId")Long employeeId, @NotNull @PathVariable(name="version") Integer version){
        SuccessDTO dto = employeeService.deleteEmployee(employeeId, version);
        logger.debug("Employee Deleted Successfully");
        return dto;
    }

    @ApiOperation(value = "Update an employee with id, if exists and version correct.")
    @PutMapping("/employees/{employeeId}/{version}")
    public ResourceHeaderDTO updateEmployee(@RequestBody EmployeeDTO employee,
                               @PathVariable(name="employeeId")Long employeeId, @NotNull  @PathVariable(name="version") Integer version){
        ResourceHeaderDTO header =  employeeService.updateEmployee(employee,employeeId, version);
        logger.debug("Employee Update Successfully");
        return  header;
    }

}
