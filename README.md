# Employee Management
The state of employee can be changed according to the predefined state transition rules by using State machine model.  

The states for a given employee are;
- `ADDED`
- `IN-CHECK`
- `APPROVED`
- `ACTIVE`

`IN-CHECK` state is special and has the following orthogonal child substates (also called orthogonal regions);

- `SECURITY_CHECK_STARTED`
- `SECURITY_CHECK_FINISHED`
- `WORK_PERMIT_CHECK_STARTED`
- `WORK_PERMIT_CHECK_PENDING_VERIFICATION`
- `WORK_PERMIT_CHECK_FINISHED`

State transition events as follows;
- `BEGIN CHECK`  
- `FINISH SECURITY CHECK`  
- `COMPLETE INITIAL WORK PERMIT CHECK`  
- `FINISH WORK PERMIT CHECK`  
- `ACTIVATE`  

## Build and Run  
Build the project  
`mvn clean package`

Docker build and run  
`docker build -t employeestate-0.0.1-SNAPSHOT.jar`  
`docker run -p 8080:8080 employeestate-0.0.1-SNAPSHOT.jar`

Run with Swagger  
`http://localhost:8080/swagger-ui.html`  

## Notes    
Postgres is used to store the following entities;
- `employeemanagement.employee`  
- `employeemanagement.employee_state`  

