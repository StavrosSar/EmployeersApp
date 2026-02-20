package tech.getarrays.employeemanager.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.getarrays.employeemanager.exception.UserNotFoundException;
import tech.getarrays.employeemanager.model.Employee;
import tech.getarrays.employeemanager.repo.EmployeeRepo;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    // Fake repository used to isolate service logic from the real database.
    @Mock
    private EmployeeRepo employeeRepo;
    // Class under test.
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        // Inject the mock repository into a fresh service before each test.
        employeeService = new EmployeeService(employeeRepo);
    }

    @Test
    void testAddEmployee() {
        // Arrange: input employee and mocked save behavior.
        Employee employee = new Employee();
        employee.setName("John Doe");
        when(employeeRepo.save(any(Employee.class))).thenReturn(employee);

        // Act: call the service method.
        Employee result = employeeService.addEmployee(employee);

        // Assert: service generated employeeCode and saved through repository.
        assertNotNull(result.getEmployeeCode());
        assertFalse(result.getEmployeeCode().isBlank());
        assertEquals("John Doe", result.getName());
        verify(employeeRepo, times(1)).save(employee);
    }

    @Test
    void testAddEmployeeReplacesExistingCode() {
        // Arrange: incoming employee already has a code.
        Employee employee = new Employee();
        employee.setEmployeeCode("old-code");
        when(employeeRepo.save(any(Employee.class))).thenReturn(employee);

        // Act
        Employee result = employeeService.addEmployee(employee);

        // Assert: service should replace it with a generated code.
        assertNotEquals("old-code", result.getEmployeeCode());
        assertNotNull(result.getEmployeeCode());
        assertFalse(result.getEmployeeCode().isBlank());
        verify(employeeRepo, times(1)).save(employee);
    }

    @Test
    void testFindAllEmployees() {
        // Arrange: repository returns two employees.
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeRepo.findAll()).thenReturn(employees);

        // Act
        List<Employee> result = employeeService.findAllEmployees();

        // Assert
        assertEquals(2, result.size());
        verify(employeeRepo, times(1)).findAll();
    }

    @Test
    void testFindAllEmployeesEmpty() {
        // Arrange: repository has no employees.
        when(employeeRepo.findAll()).thenReturn(List.of());

        // Act
        List<Employee> result = employeeService.findAllEmployees();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(employeeRepo, times(1)).findAll();
    }

    @Test
    void testUpdateEmployee() {
        // Arrange: saving employee returns updated employee.
        Employee employee = new Employee();
        when(employeeRepo.save(employee)).thenReturn(employee);

        // Act
        Employee result = employeeService.updateEmployee(employee);

        // Assert
        assertNotNull(result);
        verify(employeeRepo, times(1)).save(employee);
    }

    @Test
    void testFindEmployeeById() {
        // Arrange: repository finds employee by id.
        Employee employee = new Employee();
        when(employeeRepo.findEmployeeById(1L)).thenReturn(Optional.of(employee));

        // Act
        Employee result = employeeService.findEmployeeById(1L);

        // Assert
        assertNotNull(result);
        verify(employeeRepo, times(1)).findEmployeeById(1L);
    }

    @Test
    void testFindEmployeeByIdNotFound() {
        // Arrange: repository does not find employee.
        when(employeeRepo.findEmployeeById(1L)).thenReturn(Optional.empty());

        // Assert: service should throw the domain exception.
        assertThrows(UserNotFoundException.class, () -> employeeService.findEmployeeById(1L));
    }

    @Test
    void testFindEmployeeByIdNotFoundMessage() {
        // Arrange
        Long id = 99L;
        when(employeeRepo.findEmployeeById(id)).thenReturn(Optional.empty());

        // Act
        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> employeeService.findEmployeeById(id));

        // Assert
        assertTrue(ex.getMessage().contains("User by id 99 was not found"));
    }

    @Test
    void testDeleteEmployee() {
        // Arrange: id exists.
        when(employeeRepo.existsById(1L)).thenReturn(true);

        // Act
        employeeService.deleteEmployee(1L);

        // Assert
        verify(employeeRepo, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteEmployeeCallOrder() {
        // Arrange
        when(employeeRepo.existsById(1L)).thenReturn(true);
        InOrder inOrder = inOrder(employeeRepo);

        // Act
        employeeService.deleteEmployee(1L);

        // Assert: existence check must happen before delete.
        inOrder.verify(employeeRepo).existsById(1L);
        inOrder.verify(employeeRepo).deleteById(1L);
    }

    @Test
    void testDeleteEmployeeNotFound() {
        // Arrange: id does not exist.
        when(employeeRepo.existsById(1L)).thenReturn(false);

        // Assert: deleting missing id throws exception.
        assertThrows(UserNotFoundException.class, () -> employeeService.deleteEmployee(1L));
        verify(employeeRepo, never()).deleteById(anyLong());
    }
}
