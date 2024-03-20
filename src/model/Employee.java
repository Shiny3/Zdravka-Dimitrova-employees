package model;

import java.util.Date;
 
public class Employee {
	
	//@NotNull
	private Long EmpID; 
	
	private Long ProjectID;
	
	private Date DateFrom = null;
	
	private Date DateTo = null;
	
	private long daysWorked;
	
	public Employee() {
		super();
	}

	public Employee(Long empID, Long projectID, Date dateFrom, Date dateTo) {
		super();
		EmpID = empID;
		ProjectID = projectID;
		DateFrom = dateFrom;
		DateTo = dateTo;
	}

	public Long getEmpID() {
		return EmpID;
	}

	public void setEmpID(Long empID) {
		EmpID = empID;
	}

	public Long getProjectID() {
		return ProjectID;
	}

	public void setProjectID(Long projectID) {
		ProjectID = projectID;
	}

	public Date getDateFrom() {
		return DateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		DateFrom = dateFrom;
	}

	public Date getDateTo() {
		return DateTo;
	}

	public void setDateTo(Date dateTo) {
		DateTo = dateTo;
	}
	
	public long getDaysWorked() {
		return daysWorked;
	}

	public void setDaysWorked(long daysWorked) {
		this.daysWorked = daysWorked;
	}
}
