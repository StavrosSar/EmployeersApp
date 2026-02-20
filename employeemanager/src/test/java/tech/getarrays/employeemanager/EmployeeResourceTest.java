package tech.getarrays.employeemanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tech.getarrays.employeemanager.exception.UserNotFoundException;
import tech.getarrays.employeemanager.model.Employee;
import tech.getarrays.employeemanager.service.EmployeeService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeResourceTest {
    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeResource employeeResource;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setEmail("john@example.com");
    }

    @Test
    void testGetAllEmployees() {
        List<Employee> employees = Arrays.asList(employee);
        when(employeeService.findAllEmployees()).thenReturn(employees);

        ResponseEntity<List<Employee>> response = employeeResource.getAllEmployees();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(employeeService, times(1)).findAllEmployees();
    }

    @Test
    void testGetEmployeeById() {
        when(employeeService.findEmployeeById(1L)).thenReturn(employee);

        ResponseEntity<Employee> response = employeeResource.getEmployeeById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("John Doe", response.getBody().getName());
        verify(employeeService, times(1)).findEmployeeById(1L);
    }

    @Test
    void testAddEmployee() {
        when(employeeService.addEmployee(employee)).thenReturn(employee);

        ResponseEntity<Employee> response = employeeResource.addEmployee(employee);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("John Doe", response.getBody().getName());
        verify(employeeService, times(1)).addEmployee(employee);
    }

    @Test
    void testUpdateEmployee() {
        when(employeeService.updateEmployee(employee)).thenReturn(employee);

        ResponseEntity<Employee> response = employeeResource.updateEmployee(employee);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("John Doe", response.getBody().getName());
        verify(employeeService, times(1)).updateEmployee(employee);
    }

    @Test
    void testDeleteEmployee() {
        doNothing().when(employeeService).deleteEmployee(1L);

        ResponseEntity<Void> response = employeeResource.deleteEmployee(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(employeeService, times(1)).deleteEmployee(1L);
    }

    @Test
    void testGetEmployeeByIdNotFound() {
        when(employeeService.findEmployeeById(999L))
                .thenThrow(new UserNotFoundException("User by id 999 was not found"));

        assertThrows(UserNotFoundException.class, () -> employeeResource.getEmployeeById(999L));
        verify(employeeService, times(1)).findEmployeeById(999L);
    }

    @Test
    void testDeleteEmployeeNotFound() {
        doThrow(new UserNotFoundException("User by id 999 was not found"))
                .when(employeeService).deleteEmployee(999L);

        assertThrows(UserNotFoundException.class, () -> employeeResource.deleteEmployee(999L));
        verify(employeeService, times(1)).deleteEmployee(999L);
    }
}
