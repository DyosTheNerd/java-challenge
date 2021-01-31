package jp.co.axa.apidemo.unit.services;


import jp.co.axa.apidemo.config.ApplicationConfiguration;
import jp.co.axa.apidemo.dto.EmployeeDTO;
import jp.co.axa.apidemo.dto.EmployeeReturnDTO;
import jp.co.axa.apidemo.dto.ResourceHeaderDTO;
import jp.co.axa.apidemo.dto.SuccessDTO;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.LockingException;
import jp.co.axa.apidemo.exceptions.ResourceNotFoundException;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import jp.co.axa.apidemo.services.EmployeeServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration()
public class EmployeeServiceTest {

    @Before
    public void setup(){
        testObject.setMapper(new ApplicationConfiguration().modelMapper());
    }

    @InjectMocks
    EmployeeServiceImpl testObject;


    @Mock
    EmployeeRepository repository;

    @Test(expected = ResourceNotFoundException.class)
    public void testDeleteNullPointerFromExisting() throws ResourceNotFoundException{
        when(repository.findById(1l)).thenReturn(null);

        testObject.deleteEmployee(1l,1);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testDeletePointerFromExisting() throws ResourceNotFoundException{
        when(repository.findById(1l)).thenReturn(Optional.empty());

        testObject.deleteEmployee(1l,1);
    }


    @Test(expected = LockingException.class)
    public void testDeleteWrongVersion() throws LockingException {
        testObject.setMapper(new ApplicationConfiguration().modelMapper());
        Employee persisted = new Employee();
        persisted.setId(1l);
        persisted.setVersion(2);
        persisted.setDepartment("");
        persisted.setName("");
        persisted.setSalary(100);
        when(repository.findById(1l)).thenReturn(Optional.of(persisted));

        testObject.deleteEmployee(1l,1);
    }



    @Test
    public void testDeleteOK(){

        Employee persisted = new Employee();
        persisted.setId(1l);
        persisted.setVersion(1);
        persisted.setDepartment("");
        persisted.setName("");
        persisted.setSalary(100);
        when(repository.findById(1l)).thenReturn(Optional.of(persisted));

        SuccessDTO result = testObject.deleteEmployee(1l, 1);
        assertTrue(result.isSuccessful());
    }

    @Test(expected = LockingException.class)
    public void testUpdateFailVersion() throws LockingException{
        when(repository.save(any())).thenThrow(ObjectOptimisticLockingFailureException.class);
        when(repository.findById(any())).thenReturn(Optional.of(new Employee()));
        testObject.updateEmployee(new EmployeeDTO(),1l,0);
    }


    @Test(expected = ResourceNotFoundException.class)
    public void testUpdateFailNotFound() throws LockingException{
        when(repository.findById(any())).thenReturn(Optional.empty());
        testObject.updateEmployee(new EmployeeDTO(),1l,0);
    }

    @Test
    public void testSaveEmployee() {
        EmployeeDTO employeeDto = new EmployeeDTO();
        Employee persistedEmployee = new Employee();
        persistedEmployee.setId(new Long(11));
        persistedEmployee.setVersion(new Integer(10));
        when(repository.save(any())).thenReturn(persistedEmployee);
        ResourceHeaderDTO result = testObject.saveEmployee(employeeDto);
        assertEquals("pass persistence version ",new Integer(10), result.getVersion());
        assertEquals("pass persistence Id ",new Long(11), result.getId());
    }

    @Test
    public void testGetEmployee(){
        Employee employee = getTestEmployee();
        when(repository.findById(new Long(2))).thenReturn(Optional.of(employee));

        EmployeeReturnDTO result = testObject.getEmployee(new Long(2));

        assertEquals("pass persistence version ",employee.getVersion(), result.getVersion());
        assertEquals("pass persistence Id ",employee.getDepartment(), result.getDepartment());
    }


    private Employee getTestEmployee(){
        Employee persisted = new Employee();
        persisted.setId(1l);
        persisted.setVersion(1);
        persisted.setDepartment("");
        persisted.setName("");
        persisted.setSalary(100);
        return persisted;
    }



}
