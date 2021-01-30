package jp.co.axa.apidemo.unit.controllers;

import jp.co.axa.apidemo.controllers.EmployeeController;
import jp.co.axa.apidemo.dto.EmployeeDTO;
import jp.co.axa.apidemo.dto.EmployeeReturnDTO;
import jp.co.axa.apidemo.dto.ResourceHeaderDTO;
import jp.co.axa.apidemo.dto.SuccessDTO;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.LockingException;
import jp.co.axa.apidemo.exceptions.ResourceNotFoundException;
import jp.co.axa.apidemo.services.EmployeeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = EmployeeController.class)
public class EmployeeControllerTest {


    @Autowired
    MockMvc mockMvc;

    @MockBean
    EmployeeService service;

    @Test
    public void testGetSingleEntry() throws Exception {

        when(service.getEmployee(1l)).thenReturn(new EmployeeReturnDTO());

        mockMvc.perform(get("/api/v1/employees/1").contentType("application/json")).andExpect(MockMvcResultMatchers.status().isOk());

        verify(service).getEmployee(1l);
    }

    @Test
    public void testGetListEntry() throws Exception {

        Page mockPage = mock(Page.class);
        when(mockPage.getTotalPages()).thenReturn(1);
        when(mockPage.getTotalElements()).thenReturn(10l);
        when(mockPage.getContent()).thenReturn(new ArrayList<Employee>());


        when(service.retrieveEmployees(1,1)).thenReturn(mockPage);

        mockMvc.perform(get("/api/v1/employees/?page=1&size=1").contentType("application/json")).andExpect(MockMvcResultMatchers.status().isOk());

        verify(service).retrieveEmployees(1,1);
    }

    @Test
    public void testCreateEntry() throws Exception {

        when(service.saveEmployee(any(EmployeeDTO.class))).thenReturn(new ResourceHeaderDTO());

        mockMvc.perform(post("/api/v1/employees/").contentType("application/json").content("{\"department\": \"dept\",\"name\": \"thename\",\"salary\": 1000}")).andExpect(MockMvcResultMatchers.status().isOk());

        ArgumentCaptor<EmployeeDTO> captor = ArgumentCaptor.forClass(EmployeeDTO.class);
        verify(service).saveEmployee(captor.capture());
        EmployeeDTO value = captor.getValue();
        assertEquals("name is correct","thename",value.getName());
        assertEquals("dept is correct","dept",value.getDepartment());
        assertEquals("salary is correct",new Integer(1000),value.getSalary());
    }


    @Test
    public void testUpdateEntry() throws Exception {

        when(service.updateEmployee(any(EmployeeDTO.class),eq(1l),eq(1))).thenReturn(new ResourceHeaderDTO());

        mockMvc.perform(put("/api/v1/employees/1/1").contentType("application/json").content("{\"department\": \"dept\",\"name\": \"thename\",\"salary\": 1000}")).andExpect(MockMvcResultMatchers.status().isOk());

        ArgumentCaptor<EmployeeDTO> captor = ArgumentCaptor.forClass(EmployeeDTO.class);
        verify(service).updateEmployee(captor.capture(),eq(1l),eq(1));
        EmployeeDTO value = captor.getValue();
        assertEquals("name is correct","thename",value.getName());
        assertEquals("dept is correct","dept",value.getDepartment());
        assertEquals("salary is correct",new Integer(1000),value.getSalary());
    }

    @Test
    public void testDeleteEntry() throws Exception {

        when(service.deleteEmployee(new Long(1),new Integer(1))).thenReturn(new SuccessDTO(true));

        mockMvc.perform(delete("/api/v1/employees/1/1").contentType("application/json")).andExpect(MockMvcResultMatchers.status().isOk());

        ArgumentCaptor<EmployeeDTO> captor = ArgumentCaptor.forClass(EmployeeDTO.class);
        verify(service).deleteEmployee(eq(1l),eq(1));

    }

    @Test
    public void testResourceNotFound404() throws Exception{
        when(service.deleteEmployee(new Long(1),new Integer(1))).thenThrow(new ResourceNotFoundException(1l,"EMP"));
        mockMvc.perform(delete("/api/v1/employees/1/1").contentType("application/json")).andExpect(MockMvcResultMatchers.status().is(404));


    }

    @Test
    public void testWrongVersion409() throws Exception{
        when(service.deleteEmployee(new Long(1),new Integer(1))).thenThrow(new LockingException(1l,"EMP"));
        mockMvc.perform(delete("/api/v1/employees/1/1").contentType("application/json")).andExpect(MockMvcResultMatchers.status().is(409));
    }




}
