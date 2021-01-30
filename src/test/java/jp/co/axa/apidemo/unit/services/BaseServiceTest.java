package jp.co.axa.apidemo.unit.services;

import jp.co.axa.apidemo.config.ApplicationConfiguration;
import jp.co.axa.apidemo.dto.ResourceHeaderDTO;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.BaseService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class BaseServiceTest {

    @InjectMocks
    BaseService service;

    @Before
    public void setup(){
        service.setMapper(new ApplicationConfiguration().modelMapper());
    }


    @Test
    public void testVersionedEntityMapping(){
        Integer version = 4;
        Long id = 12l;


        Employee employee = new Employee();
        employee.setVersion(version);
        employee.setId(id);

        ResourceHeaderDTO headerDTO = service.getHeaderDTOFromEntity(employee);

        assertEquals("id transferred",id, headerDTO.getId());
        assertEquals("version transferred",version, headerDTO.getVersion());
    }
}
