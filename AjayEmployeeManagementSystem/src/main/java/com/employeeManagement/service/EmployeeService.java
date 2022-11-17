package com.employeeManagement.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import com.employeeManagement.custom.exception.BusinessException;
import com.employeeManagement.entity.Employee;
import com.employeeManagement.repository.EmployeeRepository;

@Service
//@Profile({"employeedatabase","estuate"})
public class EmployeeService implements EmployeeServiceInterface {

	Logger logger = LoggerFactory.getLogger(EmployeeService .class);

	@Autowired
	private EmployeeRepository employeeRepository;



	//---------------------ADD EMPLOYEE-----------------------------------------------------	

	@Override

	public Employee addEmployee(Employee employee) {
		Employee  savedEmployee= new Employee();
		savedEmployee = employee;

		if (!(employeeRepository.findByPhone(savedEmployee.getPhone())==null) ) {

			throw new BusinessException("AddEmployee Duplicate Phone","Entered Phone Number already exsists in DataBase");
		}
		if (!(employeeRepository.findByEmail(savedEmployee.getEmail())==null) ) {
			throw new BusinessException("AddEmployee Duplicate Email","Entered Email Id already exsists in DataBase");
		}

		if ((employeeRepository.findByPhone(savedEmployee.getPhone())==null && 
				employeeRepository.findByEmail(savedEmployee.getEmail())==null)) {
			try {
				savedEmployee = employeeRepository.save(employee);		
				savedEmployee.setESTUATE_ID("EST-"+savedEmployee.getEmpId());
				savedEmployee = employeeRepository.save(employee);
				return savedEmployee;
			} catch (IllegalArgumentException e) {
				throw new BusinessException("EmployeeService-addEmployee-2",
						"Not Valid Name, Please Enter Valid Name " + e.getMessage());
			} catch (Exception e) {
				throw new BusinessException("EmployeeService-addEmployee-3",
						"Something went wrong in service layer " + e.getMessage());
			}
		}else {
			throw new BusinessException("EmployeeService-addEmployee-1;",
					"Email Id or Mobile Already Exsists in DataBase ")	;
		}

	}

	//	@Override
	//	public Employee addEmployee( String eSTUATE_ID, @NotNull String firstName, String lastName,
	//			String dateOfBirth, @Email String email, Long phone, String fileName, String type, MultipartFile data) {
	//		
	//		Employee newEmployee = new Employee( eSTUATE_ID, firstName, lastName, dateOfBirth, phone, fileName, type, null);
	//		newEmployee.setFirstName(firstName);
	//		newEmployee.setLastName(lastName);
	//		newEmployee.setDateOfBirth(dateOfBirth);
	//		newEmployee.setEmail(email);
	//		newEmployee.setPhone(phone);
	//	
	//		try {
	//			newEmployee.setData(Base64.getEncoder().encodeToString(data.getBytes()));
	//		} catch (IOException e) {
	//			// TODO Auto-generated catch block
	//			new BusinessException("Adding image", "Failed to Add Image");
	//		}
	//		employeeRepository.save(newEmployee);
	//		newEmployee.setESTUATE_ID("EST-"+newEmployee.getEmpId());
	//		newEmployee= employeeRepository.save(newEmployee);
	//		return newEmployee ;
	//	}
	//	@Override
	//	public Employee addEmployee( Object employee) {
	//		MultipartFile file=null;
	//		Employee  savedEmployee = (Employee) employee;
	//
	//		if (!(employeeRepository.findByPhone(savedEmployee.getPhone())==null) ) {
	//			logger.error(" EmployeeService : add Employee : Phone Number already exsist " + savedEmployee.getPhone());
	//			throw new BusinessException("AddEmployee Duplicate Phone","Entered Phone Number already exsists in DataBase");
	//		}
	//		if (!(employeeRepository.findByEmail(savedEmployee.getEmail())==null) ) {
	//			logger.error(" EmployeeService : add Employee : Email Id already exsist " + savedEmployee.getEmail());
	//			throw new BusinessException("AddEmployee Duplicate Email","Entered Email Id already exsists in DataBase");
	//		}
	//
	//		if ((employeeRepository.findByPhone(savedEmployee.getPhone())==null && 
	//				employeeRepository.findByEmail(savedEmployee.getEmail())==null)) {
	//			try {
	//				String fileName = "";
	//				
	//				Employee savingEmployee = new Employee(fileName, file.getContentType(), file.getBytes());
	//				
	//				savingEmployee.setFirstName(((Employee) employee).getFirstName());
	//				savingEmployee.setLastName(((Employee) employee).getLastName());
	//				savingEmployee.setEmail(((Employee) employee).getEmail());
	//				savingEmployee.setDateOfBirth(((Employee) employee).getDateOfBirth());
	//				savingEmployee.setPhone(((Employee) employee).getPhone());
	//				
	//				savingEmployee.setData(((Employee) employee).getData());
	//
	//				employee= employeeRepository.save(savingEmployee);
	//				savingEmployee.setESTUATE_ID("EST-"+((Employee) employee).getEmpId());
	//				savingEmployee.setFileName(((Employee) employee).getESTUATE_ID()+" "+((Employee) employee).getFirstName());
	//				
	//				logger.info("EmployeeService : add Employee : Working Successfully ");
	//				return employeeRepository.save(savingEmployee);
	//				
	//				
	//			} catch (IllegalArgumentException e) {
	//				logger.warn(" EmployeeService : IllegalArgumentException handled inside add Employee Method  ");
	//				throw new BusinessException("EmployeeService-addEmployee-2",
	//						"Not Valid Name, Please Enter Valid Name " + e.getMessage());
	//			} catch (Exception e) {
	//				logger.warn(" EmployeeService : Exception handled inside add Employee Method  ");
	//				throw new BusinessException("EmployeeService-addEmployee-3",
	//						"Something went wrong in service layer " + e.getMessage());
	//			}
	//		}else {
	//			throw new BusinessException("EmployeeService-addEmployee-1;",
	//					"Email Id or Mobile Already Exsists in DataBase ")	;
	//		}
	//
	//	}


	//------------------------------SET PROFILE PICTURE@PathVariable("empId") Long empId, Employee emp
	//	@Override
	//	public Employee setProfilePicture(@PathVariable("empId") Long empId,Employee employee, MultipartFile file) {
	//
	//		if (employeeRepository.findById(empId).get().equals(null)) {
	//			logger.error(" EmployeeService : Employee ID null  ");
	//			throw new BusinessException("EmployeeService-updateEmployee-1",
	//					"Entered null value , Please Enter Valid ID");
	//		}
	//
	//		Employee photoEmployee=employeeRepository.findById(empId).orElseThrow(()-> new BusinessException("Employee ID is not present in Database", "Please Enter valid ID"));
	//		//Employee photoEmployee=employeeRepository.getById(empId);
	//
	//		try {
	//			photoEmployee.setPhoto(Base64.getEncoder().encodeToString(file.getBytes()));
	//			employeeRepository.save(photoEmployee);
	//		} catch (IOException e) {
	//			new BusinessException("Employee Service - setProfilePicture ","Failed To Upload Profile picture");
	//		}
	//
	//		return photoEmployee ;
	//	}

	@Override
	public Employee updateEmployee(Long empId, Employee employee) {


		if (employeeRepository.findById(empId).get().equals(null)) {
			logger.error(" EmployeeService : Employee ID null  ");
			throw new BusinessException("EmployeeService-updateEmployee-1",
					"Entered null value , Please Enter Valid ID");
		}

		Employee existingEmployee=employeeRepository.findById(empId).orElseThrow(()-> new BusinessException("Employee ID is not present in Database", "Please Enter valid ID"));

		existingEmployee.setFirstName(employee.getFirstName());
		existingEmployee.setLastName(employee.getLastName());
		existingEmployee.setDateOfBirth(employee.getDateOfBirth());
		existingEmployee.setEmail(employee.getEmail());
		existingEmployee.setPhone(employee.getPhone());
		logger.info("EmployeeService : updateEmployee : Working Successfully " + empId );
		employeeRepository.save(existingEmployee);

		return existingEmployee;
	}	

	//---------------------VIEW ALL EMPLOYEE-----------------------------------------------------	
	@Override
	public List<Employee> getAllEmployees() {
		List<Employee> empoyeeList = null;
		try {
			logger.info("EmployeeService : getAllEmployees : Working Successfully ");
			empoyeeList = employeeRepository.findAll();
		} catch (Exception e) {
			logger.warn(" EmployeeService : Exception handled inside getAllEmployees Method  ");
			throw new BusinessException("EmployeeService-getAllEmployees-2",
					"Something went wrong in service layer while fetching all employee details " + e.getMessage());
		}
		if (empoyeeList.isEmpty()) {
			logger.error(" EmployeeService : Employee List Empty  ");
			throw new BusinessException("EmployeeService-getAllEmployees-1",
					" List is Empty, Add Some Data in Register Page... ");
		}
		return empoyeeList;

	}

	//---------------------VIEW 1 EMPLOYEE BY ID-----------------------------------------------------	
	@Override
	public Employee getEmployeeById(Long empId) {
		if (empId.equals(null)) {
			logger.error(" EmployeeService : Employee ID null  ");
			throw new BusinessException("EmployeeService-getEmployeeById-1",
					"You Entered a null value, please Enter Any int Value");
		}
		try {
			logger.info("EmployeeService : getEmployeeById : Working Successfully " + empId );
			return employeeRepository.findById(empId).get();
		} catch (NoSuchElementException e) {
			logger.warn(" EmployeeService : NoSuchElementException handled inside getEmployeeById Method  ");
			throw new BusinessException("EmployeeService-getEmployeeById-BE-2",
					"Employee ID Not found in DataBase, Please enter valid ID " + e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.warn(" EmployeeService : IllegalArgumentException handled inside getEmployeeById Method  ");
			throw new BusinessException("EmployeeService-getEmployeeById-BE-3",
					"Something went wrong in service layer " + e.getMessage());
		}

	}




	//---------------------DELETE 1 EMPLOYEE BY ID-----------------------------------------------------			
	@Override
	public Employee deleteEmployeeById(Long empId) {
		if (!employeeRepository.existsById(empId)) 
		{
			logger.error(" EmployeeService : Employee ID Not Present  " + empId );
			throw new BusinessException("EmployeeService-deleteEmployeeById-1",
					" Employee ID Not found in DataBase, Please enter valid ID");
		}
		Employee deletedEmp = employeeRepository.getById(empId);
		try {
			employeeRepository.deleteById(empId);
			logger.info("Inside the method: deleteEmployeeById,Employee Id is sucessfully deleted"+empId);
		} catch (NoSuchElementException e) {
			logger.warn(" EmployeeService : NoSuchElementException handled inside deleteEmployee Method  ");
			throw new BusinessException("EmployeeService-updateEmployee-2",
					"Employee ID Not found in DataBase, Please enter valid ID " + e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.warn(" EmployeeService : IllegalArgumentException handled inside deleteEmployee Method  ");
			throw new BusinessException("EmployeeService-updateEmployee-3",
					"Something went wrong in service layer " + e.getMessage());
		}
		return deletedEmp;
	}

	//---------------------LIST OF EMPLOYEES BY FIRST NAME-----------------------------------------------------	
	@Override
	public List<Employee> getEmployeeByFirstName(String firstName) {

		List<Employee> empoyeeListByName = null;
		try {
			logger.info("EmployeeService : getEmployeeByFirstName : Working Successfully ");
			empoyeeListByName = employeeRepository.findByFirstName(firstName);
		} catch (Exception e) {
			throw new BusinessException("EmployeeService-getEmployeeByFirstName-2",
					"Something went wrong in service layer while fetching all employee details " + e.getMessage());
		}
		if (empoyeeListByName.isEmpty()) {
			throw new BusinessException("EmployeeService-getEmployeeByFirstName-1",
					"Requested Data not found in List , Please enter some data in Register Page ");
		}
		return empoyeeListByName;
	}

	//---------------------LIST OF EMPLOYEES BY LAST NAME-----------------------------------------------------		
	@Override
	public List<Employee> getEmployeeByLastName(String lastName) {

		List<Employee> empoyeeListByName = null;
		try {
			logger.info("EmployeeService : getEmployeeByLastName : Working Successfully ");
			empoyeeListByName = employeeRepository.findByLastName(lastName);
		} catch (Exception e) {
			throw new BusinessException("EmployeeService-getEmployeeByLastName-2",
					"Something went wrong in service layer while fetching all employee details " + e.getMessage());
		}
		if (empoyeeListByName.isEmpty()) {
			throw new BusinessException("EmployeeService-getEmployeeByLastName-1",
					" Requested Data not found in List , Please enter some data in Register Page ");
		}
		return empoyeeListByName;
	}

	//---------------------VIEW 1 EMPLOYEE BY PHONE-----------------------------------------------------	
	@Override
	public Employee getEmployeeByPhone(Long phone) {
		Employee empoyeeListByPhone = null;
		try {
			logger.info("EmployeeService : getEmployeeByPhone : Working Successfully ");
			empoyeeListByPhone = employeeRepository.findByPhone(phone);
		} catch (Exception e) {
			throw new BusinessException("EmployeeService-getEmployeeByPhone-2",
					"Something went wrong in service layer while fetching all employee details " + e.getMessage());
		}
		if (employeeRepository.existsById(empoyeeListByPhone.getEmpId())) {
			throw new BusinessException("EmployeeService-getEmployeeByPhone-1",
					"Requested Data not found in List , Please enter some data in Register Page  ");
		}
		return empoyeeListByPhone;

	}

	//---------------------VIEW 1 EMPLOYEE BY EMAIL-----------------------------------------------------	
	@Override
	public Employee getEmployeeByEmail(String email) {
		Employee empoyeeListByEmail = null;
		try {
			logger.info("EmployeeService : getEmployeeByEmail : Working Successfully ");
			empoyeeListByEmail = employeeRepository.findByEmail(email);
		} catch (Exception e) {
			throw new BusinessException("EmployeeService-getEmployeeByEmail-2",
					"Something went wrong in service layer while fetching all employee details " + e.getMessage());
		}
		if (employeeRepository.existsById(empoyeeListByEmail.getEmpId())) {
			throw new BusinessException("EmployeeService-getEmployeeByEmail-1",
					"Requested Data not found in List , Please enter some data in Register Page  ");
		}
		return empoyeeListByEmail;
	}



	@Override
	public Optional<Employee> findById(Long id) {
		return employeeRepository.findById(id);
	}

	@Override
	public String uploadPhoto(String path, MultipartFile file,Long empId) {

		Employee emp = employeeRepository.getById(empId);
		
		if (emp==null) {
			new BusinessException("EMployee Id not found ", "Failed to ge employee Details");
		}
		//File name
		String fileName= emp.getFirstName();
		//Full path
		String filePath= path +  fileName;
		//Setting path 
		emp.setPhotoPath(filePath);
		//Setting photo name
		emp.setPhotoName(fileName);
		
		//setting photo
		try {
			
			emp.setPhoto(file.getBytes());
		} catch (IOException e1) {
			new BusinessException("Failed to add photo to Database ", "Error occured in Upload photo , setPhoto ");
			e1.printStackTrace();
		}
		//Create folder
		File f = new File(path);
		if (!f.exists()) {
			f.mkdir();
		}
		//file copy
		try {
			Files.copy(file.getInputStream(), Paths.get(filePath));
		} catch (IOException e) {
			new BusinessException("Something went wrong", "Failed to copy");
		}
		employeeRepository.save(emp);
		return fileName ;
	}
	
//	private String path = "C:\\Users\\ADevaraju\\Desktop\\Photos\\files";



	@Override

	public InputStream getResource(String path,Long empId) {
		Employee emp = employeeRepository.getById(empId);
		if(! (employeeRepository.existsById(empId))) {
			 new BusinessException("Employee Id Not Found", "Please Enter Valid Id");
		}

		String fullPath= path+File.separator+emp.getPhotoName();

		InputStream is=null;
		try {
			//DataBase logic to return inputstream
			is = new FileInputStream(fullPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return is;

	}

}