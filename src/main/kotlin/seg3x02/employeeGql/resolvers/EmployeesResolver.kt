package seg3x02.employeeGql.resolvers

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import seg3x02.employeeGql.entity.Employee
import seg3x02.employeeGql.repository.EmployeesRepository
import java.util.*

@Controller
class EmployeeController(private var employeesRepository: EmployeesRepository) {

    @QueryMapping
    fun employees(): List<Employee> = employeesRepository.findAll()

    @QueryMapping
    fun employeeById(@Argument id: String): Employee? = employeesRepository.findById(id).orElse(null)

    @MutationMapping
    fun newEmployee(@Argument createEmployeeInput: CreateEmployeeInput): Employee {
        var employee = Employee(
            name = createEmployeeInput.name,
            dateOfBirth = createEmployeeInput.dateOfBirth,
            city = createEmployeeInput.city,
            salary = createEmployeeInput.salary,
            gender = createEmployeeInput.gender,
            email = createEmployeeInput.email
        )
        employee.id = UUID.randomUUID().toString()
        return employeesRepository.save(employee)
    }

    @MutationMapping
    fun deleteEmployee(@Argument id: String): Boolean {
        return try {
            employeesRepository.deleteById(id)
            true
        } catch (e: Exception) {
            false
        }
    }

    @MutationMapping
    fun updateEmployee(@Argument id: String, @Argument createEmployeeInput: CreateEmployeeInput): Employee? {
        var existingEmployee = employeesRepository.findById(id).orElse(null) ?: return null
        existingEmployee.apply {
            name = createEmployeeInput.name
            dateOfBirth = createEmployeeInput.dateOfBirth
            city = createEmployeeInput.city
            salary = createEmployeeInput.salary
            gender = createEmployeeInput.gender
            email = createEmployeeInput.email
        }
        return employeesRepository.save(existingEmployee)
    }
}

data class CreateEmployeeInput(
    var name: String,
    var dateOfBirth: String,
    var city: String,
    var salary: Float,
    var gender: String?,
    var email: String?
)
