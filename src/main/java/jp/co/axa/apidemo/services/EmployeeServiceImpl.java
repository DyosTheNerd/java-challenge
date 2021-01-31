package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.dto.EmployeeDTO;
import jp.co.axa.apidemo.dto.EmployeeReturnDTO;
import jp.co.axa.apidemo.dto.ResourceHeaderDTO;
import jp.co.axa.apidemo.dto.SuccessDTO;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exceptions.LockingException;
import jp.co.axa.apidemo.exceptions.ResourceNotFoundException;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeServiceImpl  extends BaseService implements EmployeeService {

    @Autowired
    @Setter
    private EmployeeRepository employeeRepository;


    /**
     * Retrieves a Page of employees, ordered by id.
     * @param page the page number
     * @param size the page size
     * @return The Pageable List of Employees
     */
    public Page<Employee> retrieveEmployees(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        return employeeRepository.findAll(pageable);
    }

    /**
     * Get an Employee of ID.
     * @param employeeId the ID of the Employee
     * @return A DTO representing the Employee.
     */
    @Cacheable("employee")
    public EmployeeReturnDTO getEmployee(Long employeeId) {
        return getDTOFromEntity(getEmployeeEntity(employeeId));
    }

    /**
     * save a new employee object. no duplication check is performed.
     * @param employee the information to be stored.
     * @return The header information of the new object.
     */
    public ResourceHeaderDTO saveEmployee(EmployeeDTO employee){
        Employee newObject = getEntityFromDTO(employee);
        newObject.setVersion(0);
        Employee persitedObject = employeeRepository.save(newObject);
        return getHeaderDTOFromEntity(persitedObject);
    }


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
    @CacheEvict(value = "employee", key = "#employeeId")
    public SuccessDTO deleteEmployee(Long employeeId, Integer version){

        EmployeeReturnDTO dto = getEmployee(employeeId);
        if (dto!= null && dto.getVersion() == version){
            employeeRepository.deleteById(employeeId);
            return new SuccessDTO(true);
        }

        LockingException exception = new LockingException(employeeId,Employee.class.getSimpleName());
        throw exception;
    }

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
    @CacheEvict(value = "employee", key = "#employeeId")
    public ResourceHeaderDTO updateEmployee(EmployeeDTO employee, Long employeeId, Integer version) {
        try
        {
            getEmployeeEntity(employeeId);
            Employee requested = getEntityFromDTO(employee,employeeId,version);
            Employee persitedObject = employeeRepository.save(requested);
            return getHeaderDTOFromEntity(persitedObject);
        }
        catch (ObjectOptimisticLockingFailureException lockingException){
            throw new LockingException(employeeId,Employee.class.getSimpleName());
        }
    }

    /**
     * Mapper Method wrap, to encapsulate mapping code.
     * @param employeeDTO the basic dto
     * @return the mapped entity object
     */
    private Employee getEntityFromDTO(EmployeeDTO employeeDTO) {
        return mapper.map(employeeDTO,Employee.class);
    }

    /**
     * Mapper Method wrap, to encapsulate mapping code.
     * @param employee the basic entity
     * @return the dto representing the employee
     */
    private EmployeeReturnDTO getDTOFromEntity(Employee employee) {
        return mapper.map(employee,EmployeeReturnDTO.class);
    }

    /**
     * Mapper Method wrap, to encapsulate mapping code.
     * @param employeeDTO the basic dto
     * @return the mapped entity object
     */
    private Employee getEntityFromDTO(EmployeeDTO employeeDTO, Long employeeID, Integer version) {
        Employee emp =getEntityFromDTO(employeeDTO);
        emp.setId(employeeID);
        emp.setVersion(version);
        return emp;
    }


    /**
     * Private wrapper to get the entity and unwrap it from the Optional. Exception is thrown when not found.
     * @param employeeID
     * @return The unwrapped Employee Entity
     * @throws ResourceNotFoundException when the item is not there.
     */
    private Employee getEmployeeEntity(Long employeeID) throws ResourceNotFoundException{
        Optional<Employee> optEmp = employeeRepository.findById(employeeID);
        if (optEmp != null && optEmp.isPresent()){
            return optEmp.get();
        }
        throw new ResourceNotFoundException(employeeID,Employee.class.getSimpleName());
    }


}