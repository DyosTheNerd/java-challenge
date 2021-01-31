package jp.co.axa.apidemo.unit.services;


import jp.co.axa.apidemo.ApiDemoApplication;
import jp.co.axa.apidemo.dto.EmployeeDTO;
import jp.co.axa.apidemo.dto.EmployeeReturnDTO;
import jp.co.axa.apidemo.dto.ResourceHeaderDTO;
import jp.co.axa.apidemo.dto.SuccessDTO;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.ResourceNotFoundException;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import jp.co.axa.apidemo.services.EmployeeServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * This tests the usage of the cache for the employee service.
 */
@SpringBootTest(classes = ApiDemoApplication.class)
@RunWith(SpringRunner.class)
public class EmployeeServiceCacheTest {

    @Autowired
    EmployeeServiceImpl service;



    @Autowired
    CacheManager cacheManager;


    @MockBean
    EmployeeRepository repository;

    /**
     * this tests, that the cache is actually in use.
     */
    @Test
    public void testDoubleReadUsesCache(){
        Employee employee = new Employee();
        employee.setVersion(1);
        employee.setId(1l);
        employee.setSalary(100);
        employee.setName("name");
        employee.setDepartment("dept");
        Optional<Employee> employeeOpt = Optional.of(employee);
        when(repository.findById(1l)).thenReturn(employeeOpt);

        // call the service twice
        EmployeeReturnDTO dto = service.getEmployee(1l);
        EmployeeReturnDTO dto2 = service.getEmployee(1l);

        // verify that both return items are the same.
        assertEquals(dto,dto2);


        // verify that the repository was only hit once.
        verify(repository, VerificationModeFactory.atMost(1)).findById(1l);
    }


    /**
     * This tests is that the cache is emptied for an updated item, so that its new state is returned the next time
     */
    @Test
    public void testUpdateEvicts(){
        Employee employee = new Employee();
        employee.setVersion(1);
        employee.setId(1l);
        employee.setSalary(100);
        employee.setName("name");
        employee.setDepartment("dept");
        Optional<Employee> employeeOpt = Optional.of(employee);
        when(repository.findById(1l)).thenReturn(employeeOpt);

        // first call of get to put item into the cache
        EmployeeReturnDTO dto = service.getEmployee(1l);
        EmployeeDTO incomingdto = new EmployeeDTO();
        incomingdto.setName("newName");
        incomingdto.setDepartment("newDpt");
        incomingdto.setSalary(2000);

        // update the item.
        ResourceHeaderDTO dtoheader = service.updateEmployee(incomingdto, 1l, 1);
        // second call of get, should get into the actual implementation again.

        EmployeeReturnDTO dto2 = service.getEmployee(1l);

        // thus a different item should be returned now.
        assertNotEquals(dto,dto2);


        // there should be at least two get  requests
        verify(repository, VerificationModeFactory.atLeast(2)).findById(1l);
    }


    /**
     * This tests is that the cache is emptied for at least a deleted item, so that it is not available on get anymore.
     */
    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteEvicts(){
        Employee employee = new Employee();
        employee.setVersion(1);
        employee.setId(1l);
        employee.setSalary(100);
        employee.setName("name");
        employee.setDepartment("dept");
        Optional<Employee> employeeOpt = Optional.of(employee);
        when(repository.findById(1l)).thenReturn(employeeOpt).thenReturn(employeeOpt).thenReturn(Optional.empty());
        EmployeeReturnDTO dto = service.getEmployee(1l);
        SuccessDTO dtoSuccess = service.deleteEmployee(1l, 1);
        EmployeeReturnDTO dto2 = service.getEmployee(1l);
    }

}
