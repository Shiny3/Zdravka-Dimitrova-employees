package model;

public enum Headers {
	EmpID("EmpID"),
	 
	DateFrom("DateFrom"),
	DateTo("DateTo"),
	
	EmployeeID("Employee ID#"),
	EmployeeIDs("Employee IDs"),
	ProjectID("ProjectID"),
	Days("DaysWorkOn");
	
	private final String value;       

    private Headers(String header) {
    	value = header;
    }

    public String toString() {
       return this.value;
    }
}
