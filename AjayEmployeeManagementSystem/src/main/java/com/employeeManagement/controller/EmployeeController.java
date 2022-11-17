package com.employeeManagement.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.hibernate.engine.jdbc.StreamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.employeeManagement.custom.exception.BusinessException;
import com.employeeManagement.custom.exception.ControllerException;
import com.employeeManagement.entity.Employee;
import com.employeeManagement.entity.FileResponse;
import com.employeeManagement.service.EmployeeServiceInterface;
import com.employeemanagment.util.PdfViewById;
import com.employeemanagment.util.PdfViewTable;
import com.lowagie.text.DocumentException;


@RestController
@RequestMapping("/")
public class EmployeeController {
	Logger logger = LoggerFactory.getLogger( EmployeeController.class);
	@Autowired
	private EmployeeServiceInterface employeeServiceInterface;

	ConcurrentMap<String, Employee> conMap = new ConcurrentHashMap<>();

	/*
	 * Register page there we can add employee details and storing in Employee repository
	 * It is mapped to add employee method in employee service interface
	 */
	
	 
//		@PostMapping("/register")
//		public ResponseEntity<?> addEmployee(@Valid @RequestBody Employee employee){
//			try {
//				Employee savedEmployee = employeeServiceInterface.addEmployee(employee);
//				return new ResponseEntity<Employee>(savedEmployee, HttpStatus.CREATED);
//			} catch (BusinessException e) {
//				ControllerException ce = new ControllerException(e.getErrorCode(),e.getErrorMessage());
//				return new ResponseEntity<ControllerException>(ce, HttpStatus.BAD_REQUEST);
//			} catch (Exception e) {
//				ControllerException ce = new ControllerException("EmployeeController-addEmployee","Something went wrong on Controller");
//				return new ResponseEntity<ControllerException>(ce, HttpStatus.BAD_REQUEST);
//			}
//		}
	@PostMapping("/register")
	public ResponseEntity<?> addEmployee(@RequestBody Employee employee){

		try {
			//	logger.info(" Controller class :add Employee Method Working : Successfully ");
			Employee savedEmployee = employeeServiceInterface.addEmployee(employee);
			return new ResponseEntity<Employee>(savedEmployee, HttpStatus.CREATED);
		} catch (BusinessException e) {
			//	logger.warn(" Controller class : BusinessException handled inside add Employee Method  ");
			ControllerException ce = new ControllerException(e.getErrorCode(),e.getErrorMessage());
			return new ResponseEntity<ControllerException>(ce, HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			logger.warn(" Controller class : Exception handled inside add Employee Method  ");
			ControllerException ce = new ControllerException("EmployeeController-addEmployee","Something went wrong on Controller");
			return new ResponseEntity<ControllerException>( HttpStatus.BAD_REQUEST);
		}
	}
	
	//	public ResponseEntity<?> addEmployee(@RequestParam ("data") MultipartFile data, Long empId, String eSTUATE_ID, @NotNull String firstName, String lastName,
	//			String dateOfBirth, @Email String email, Long phone, String fileName, String type){
	//		
	//		try {
	//		//	logger.info(" Controller class :add Employee Method Working : Successfully ");
	//			Employee savedEmployee = employeeServiceInterface.addEmployee( eSTUATE_ID, firstName, lastName, dateOfBirth, email, phone, fileName, type, data);
	//			return new ResponseEntity<Employee>(savedEmployee, HttpStatus.CREATED);
	//		} catch (BusinessException e) {
	//		//	logger.warn(" Controller class : BusinessException handled inside add Employee Method  ");
	//			ControllerException ce = new ControllerException(e.getErrorCode(),e.getErrorMessage());
	//			return new ResponseEntity<ControllerException>(ce, HttpStatus.BAD_REQUEST);
	//		}
	////		} catch (Exception e) {
	////		//	logger.warn(" Controller class : Exception handled inside add Employee Method  ");
	////		//	ControllerException ce = new ControllerException("EmployeeController-addEmployee","Something went wrong on Controller");
	////			return new ResponseEntity<ControllerException>( HttpStatus.BAD_REQUEST);
	////		}
	//	}


	/*
	 * Fetching ALl employees details present in DataBase
	 * It is mapped to getAllEmployees method in employee service interface
	 * It will fetch a All Employees detail present in Data Base
	 */
	@GetMapping("/allEmployees")
	public ResponseEntity<List<Employee>> getAllEmployees(){
		logger.info(" Controller class : getAllEmployees Method Working :Successfully ");
		List<Employee> listOfEmployees = employeeServiceInterface.getAllEmployees();
		return new ResponseEntity<List<Employee>>(listOfEmployees, HttpStatus.OK) ;
	}

	/*
	 * Fetching one employee details by Using Id 
	 * It is mapped to getEmployeeById method in employee service interface
	 * It will fetch a Particular Employee details by ID
	 */

	@GetMapping("/employeeById/{empId}")
	public ResponseEntity<?> getEmployeeById(@PathVariable("empId") Long empId){		
		try {
			logger.info(" Controller class : getEmployeeById Method Working  : Based on Employee ID "+ empId);
			Employee employeeObtained = employeeServiceInterface.getEmployeeById(empId);
			return new ResponseEntity<Employee>(employeeObtained, HttpStatus.OK) ;
		} catch (BusinessException e) {
			logger.warn(" Controller class : BusinessException handled inside getEmployeeById Method ");
			ControllerException ce = new ControllerException(e.getErrorCode(),e.getErrorMessage());
			return new ResponseEntity<ControllerException>(ce, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.warn(" Controller class : Exception handled inside getEmployeeById Method  ");
			ControllerException ce = new ControllerException("EmployeeController-getEmployeeById","Something went wrong on Controller");
			return new ResponseEntity<ControllerException>(ce, HttpStatus.BAD_REQUEST);
		}

	}


	/*
	 * Fetching one employee details by Using Id 
	 * if id is present it will update else it will throw exception
	 * It is mapped to update method in employee service interface
	 * It will fetch a Particular Employee details by ID and UPDATE the employee details
	 */

	@PutMapping("/update/{empId}")
	public ResponseEntity<?> updateEmployee(@Valid @PathVariable long empId,@RequestBody Employee employee){
		try {
			logger.info(" Controller Class : Update Employee method  working : Based on "+ empId);
			Employee savedEmployee = employeeServiceInterface.updateEmployee( empId ,employee);
			return new ResponseEntity<Employee>(savedEmployee, HttpStatus.CREATED);

		} catch (BusinessException e) {
			logger.warn("  Controller class : BusinessException handled inside  updateEmployee Method  ");
			ControllerException ce = new ControllerException(e.getErrorCode(),e.getErrorMessage());
			return new ResponseEntity<ControllerException>(ce, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.warn("Controller class : Exception handled inside getEmployeeById Method ");
			ControllerException ce = new ControllerException("Employeecontroller.Update.1","Employee ID is not present in Database, please Enter valid Employee ID");
			return new ResponseEntity<ControllerException>(ce, HttpStatus.BAD_REQUEST);
		}
	}

	/*
	 * Fetching one employee details by Using Id and Deleting that particular Employee Object
	 * It is mapped to deleteEmployeeById method in employee service interface
	 * It will Delete that Employee Object from Data Base
	 */


	@DeleteMapping("/delete/{empId}")

	public ResponseEntity<?> deleteEmployeeById(@PathVariable("empId") Long empId){
		try {
			logger.info(" Controller Class : Delete Employee method  working :  Based on "+ empId);
			Employee deletedEmp=	employeeServiceInterface.deleteEmployeeById(empId);

			return new ResponseEntity<String>("Employee_Id is Successfully Deleted From the data Base   Deleted Details = "+deletedEmp,HttpStatus.ACCEPTED) ;
		} catch (BusinessException e) {
			logger.warn(" Controller class : BusinessException handled inside  deleteEmployee Method  ");
			ControllerException ce = new ControllerException(e.getErrorCode(),e.getErrorMessage());
			return new ResponseEntity<ControllerException>(ce, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.warn("Controller class : Exception handled inside deleteEmployee Method ");
			ControllerException ce = new ControllerException("EmployeeController-deleteEmployeeById","Something went wrong on Controller");
			return new ResponseEntity<ControllerException>(ce, HttpStatus.BAD_REQUEST);
		}
	}


	/*
	 * Fetching all the employee details by Using firstName 
	 * It is mapped to getEmployeeByFirstName method in EmployeeService class
	 * It will get all  that Employee details with first name
	 */
	@GetMapping("/employeeByFirstName/{firstName}")
	public ResponseEntity<?> getEmployeeByFirstName(@PathVariable("firstName") String firstName){		
		try {
			logger.info("Controller class : getEmployeeByFirstName : Working Successfully ");
			List<Employee> listOfEmployees = employeeServiceInterface.getEmployeeByFirstName(firstName);
			return new ResponseEntity<List<Employee>>(listOfEmployees, HttpStatus.OK) ;
		} catch (BusinessException e) {
			ControllerException ce = new ControllerException(e.getErrorCode(),e.getErrorMessage());
			return new ResponseEntity<ControllerException>(ce,HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			ControllerException ce = new ControllerException("EmployeeController-getEmployeeByFirstName","Something went wrong on Controller");
			return new ResponseEntity<ControllerException>(ce,HttpStatus.BAD_REQUEST);
		}

	}

	/*
	 * Fetching all the employee details by Using lastName 
	 * It is mapped to getEmployeeByLastName method in EmployeeService class
	 * It will get all  that Employee details with last name
	 */
	@GetMapping("/employeeByLastName/{lastName}")
	public ResponseEntity<?> getEmployeeByLastName(@PathVariable("lastName") String lastName){		
		try {
			logger.info("Controller class : getEmployeeByLastName : Working Successfully ");
			List<Employee> listOfEmployees = employeeServiceInterface.getEmployeeByLastName(lastName);
			return new ResponseEntity<List<Employee>>(listOfEmployees, HttpStatus.OK) ;
		} catch (BusinessException e) {
			ControllerException ce = new ControllerException(e.getErrorCode(),e.getErrorMessage());
			return new ResponseEntity<ControllerException>(ce,HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			ControllerException ce = new ControllerException("EmployeeController-getEmployeeByLastName","Something went wrong on Controller");
			return new ResponseEntity<ControllerException>(ce,HttpStatus.BAD_REQUEST);
		}

	}

	/*
	 * Fetching all the employee details by Using phone 
	 * It is mapped to getEmployeeByPhone method in EmployeeService class
	 * It will get all  that Employee details with phone
	 */
	@GetMapping("/employeeByPhone/{phone}")
	public ResponseEntity<?> getEmployeeByPhone(@PathVariable("phone") Long phone){		
		try {
			logger.info("Controller class : getEmployeeByPhone : Working Successfully ");
			Employee listOfEmployees = employeeServiceInterface.getEmployeeByPhone(phone);
			return new ResponseEntity<Employee>(listOfEmployees, HttpStatus.OK) ;
		} catch (BusinessException e) {
			ControllerException ce = new ControllerException(e.getErrorCode(),e.getErrorMessage());
			return new ResponseEntity<ControllerException>(ce,HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			ControllerException ce = new ControllerException("EmployeeController-getEmployeeByPhone","Something went wrong on Controller");
			return new ResponseEntity<ControllerException>(ce,HttpStatus.BAD_REQUEST);
		}

	}

	/*
	 * Fetching all the employee details by Using email 
	 * It is mapped to getEmployeeByEmail method in EmployeeService class
	 * It will get all  that Employee details with email
	 */
	@GetMapping("/employeeByEmail/{email}")
	public ResponseEntity<?> getEmployeeByEmail(@PathVariable("email") String email){		
		try {
			logger.info("Controller class : getEmployeeByEmail : Working Successfully ");
			Employee listOfEmployees = employeeServiceInterface.getEmployeeByEmail(email);
			return new ResponseEntity<Employee>(listOfEmployees, HttpStatus.OK) ;
		} catch (BusinessException e) {
			ControllerException ce = new ControllerException(e.getErrorCode(),e.getErrorMessage());
			return new ResponseEntity<ControllerException>(ce,HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			ControllerException ce = new ControllerException("EmployeeController-getEmployeeByEmail","Something went wrong on Controller");
			return new ResponseEntity<ControllerException>(ce,HttpStatus.BAD_REQUEST);
		}
	}
	/*
	 * Printing All the employee details in PDF
	 */
	@GetMapping("/pdfDownload")
	public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=employeeView.pdf";
		response.setHeader(headerKey, headerValue);
		List<Employee> empList = employeeServiceInterface.getAllEmployees();
		PdfViewTable exporter = new PdfViewTable(empList);
		exporter.employeePdfDownload(response);

	}

	/*
	 * Printing One  employee details in PDF by using ID
	 */

	@RequestMapping(path = "/employee/{empId}", method = RequestMethod.GET)
	public void getEmployeePdfById(@PathVariable("empId") Long empId, HttpServletResponse response)
			throws IOException {
		response.setContentType("application/pdf");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=employeeView.pdf";
		response.setHeader(headerKey, headerValue);
		Optional<Employee> optionalEmployee = employeeServiceInterface.findById(empId);
		PdfViewById exporter = new PdfViewById(Collections.singletonList(optionalEmployee));
		exporter.employeePdfDownloadById(empId, response);
	}

//	   @PathVariable("config") final String config,
//       @RequestPart(value = "configuration") final Configuration configuration,
//       @RequestPart(value = "file") final MultipartFile aFile
//	@PutMapping("/addPhoto/{empId}")
//	public ResponseEntity<?> setProfilePicture( @PathVariable("empId") Long empId,@RequestBody Employee employee,@ModelAttribute   MultipartFile file){
//		try {
//			logger.info(" Controller Class : Update Employee method  working : Based on "+ empId);
//		Employee savedEmployee = employeeServiceInterface.setProfilePicture(empId, employee, file);
//			return new ResponseEntity<Employee>(savedEmployee, HttpStatus.CREATED);
//
//		} catch (BusinessException e) {
//			logger.warn("  Controller class : BusinessException handled inside  updateEmployee Method  ");
//			ControllerException ce = new ControllerException(e.getErrorCode(),e.getErrorMessage());
//			return new ResponseEntity<ControllerException>(ce, HttpStatus.BAD_REQUEST);
//		} catch (Exception e) {
//			logger.warn("Controller class : Exception handled inside getEmployeeById Method ");
//			ControllerException ce = new ControllerException("Employeecontroller.Update.1","Employee ID is not present in Database, please Enter valid Employee ID");
//			return new ResponseEntity<ControllerException>(ce, HttpStatus.BAD_REQUEST);
//		}
//	}

	@Value("${project.images}")
	
	private String path;// = "C:\\Users\\ADevaraju\\Desktop\\Photos\\files";
	
	@PostMapping("/upload/{empId}")
	public ResponseEntity<?> fileUpload(@RequestParam ("empId")Long empId, MultipartFile file ){
		try {
			String fileName = this.employeeServiceInterface.uploadPhoto(path, file,empId);
			return new ResponseEntity<>(new FileResponse(fileName, "Image Uploaded") , HttpStatus.CREATED);
		} catch (Exception e) {
			ControllerException ce = new ControllerException("EmployeeController-Upload ","Something went wrong on Controller");
			return new ResponseEntity<ControllerException>(ce,HttpStatus.BAD_REQUEST);
		}
	}
	

	//Method to serve file
		@GetMapping(value ="/getImages/{empId}" , produces = MediaType.IMAGE_JPEG_VALUE )
		public void downloadImage(@PathVariable("empId") Long empId, HttpServletResponse response ) throws IOException {
		InputStream resource =	this.employeeServiceInterface.getResource(path, empId);
		//response.setContentType(MediaType.IMAGE_JPEG_VALUE);
			StreamUtils.copy(resource, response.getOutputStream());
		}

}
