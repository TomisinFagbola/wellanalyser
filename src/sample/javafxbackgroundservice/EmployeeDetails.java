package sample.javafxbackgroundservice;

import java.util.Date;

public class EmployeeDetails {
	
    private String empName;
    private String empMail;
    private Date date;
 
    public EmployeeDetails() {
    }
 
    public EmployeeDetails(String name, String email, Date date) {
        this.empName = name;
        this.empMail = email;
        this.date = date;
    }
    
 
    public Date getDate() {
        return date;
    }
 
    public void setDate(Date date) {
        this.date = date;
    }

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getEmpMail() {
		return empMail;
	}

	public void setEmpMail(String empMail) {
		this.empMail = empMail;
	}
	
	

}
