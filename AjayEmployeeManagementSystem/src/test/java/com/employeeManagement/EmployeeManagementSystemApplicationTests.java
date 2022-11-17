package com.employeeManagement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import com.employeeManagement.entity.Employee;
import com.employeeManagement.repository.EmployeeRepository;
import com.employeeManagement.service.EmployeeService;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class EmployeeManagementSystemApplicationTests {
@Autowired
private EmployeeRepository employeeRepository;
@Autowired
private EmployeeService employeeservice;

	      @Test
	      @Order(1)
	      @Rollback(value = false)
	      public void saveEmployeeTest() {
	    	  Employee employee=new Employee();
	  		employee.setEmpId(83L);
	  		employee.setFirstName("mahesh");
	  		employee.setLastName("babu");
	  		employee.setEmail("mahesh@gamail.com");
	  		employee.setDateOfBirth("1999-10-06");
	  		employee.setPhone(9077655414L);
	  		employeeservice.addEmployee(employee); 
	    	  assertNotNull(employeeRepository.findById(83L).get());
	    	             
	    	  
	      }
	    @Test
	    @Order(2)
	    public void getemplopyee() {
	    	 Employee employee= employeeservice. getEmployeeById(83L);
	    	 Assertions.assertThat(employee.getEmpId()).isEqualTo(83L);
	    
	    }
	    @Test
	    @Order(3)
	    public void getemplopyeebyList() {
	    	List< Employee> employee= employeeservice.getAllEmployees();
	   	 Assertions.assertThat(employee.size()).isGreaterThan(0);}
	    

  	    @Test
    @Order(4)
    public void getemplopyeebyFirstname() {
    	String actual1="mahesh";
    	List< Employee> employee=employeeservice.getEmployeeByFirstName(actual1);
    	 equals(actual1.equals(employee));
    	 
    }
    
    @Test
    @Order(5)
    public void getemplopyeebylastname() {
    	String actual2="babu";
    	List< Employee> employee=employeeservice.getEmployeeByLastName("babu");
    	 equals(actual2.equals(employee));}
	    @Test
	    @Order(6)
	    @Rollback(value = false)
	    public void updateEmployeeTest() {
	    	Employee employee=new Employee();
	  		employee.setEmpId(83L);
	  		employee.setFirstName("krishna");
	  		employee.setLastName("superstar");
	  		employee.setEmail("krishna@gmail.com");
	  		employee.setDateOfBirth("1998-09-06");
	  		employee.setPhone(7037887480L);
	  	   	employeeservice.updateEmployee(83L, employee);
	 		assertNotNull(employeeRepository.findById(83L).get());
	 		
	 		
	    }
	    @Test
	    @Order(7)
	    @Rollback(value = false)
	    public void deleteEmplopyeeTest() throws Exception{
	    	Employee employee= employeeRepository.findById(83L).get();
	    	employeeservice.deleteEmployeeById(83L);
	        Assertions.assertThat(employee.getEmpId()).isGreaterThan(0) ;       
	    }
	  	    
	   
//		@Test
//	    @Order(7)
//	    public void getemplopyeebyemail() {
//	    	String actual="rajanna1998@gmail.com";
//	    	//Employee employee= employeeRepository.findByEmail("rajanna1998@gmail.com");
//	    	Employee employee =employeeservice.getEmployeeByEmail(actual);
//	    	equals(actual.equals(employee.getEmail()));
//	    }
//    @Test
//	    @Order(3)
//	    public void getemplopyeebyphone() {
//	    	Employee employee= employeeRepository.findByPhone(8217328665L);
//Assertions.assertThat( employee.getPhone()).isEqualTo(8217328665L);

	    
    //}
	
	    }
