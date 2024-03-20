package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ProjectPairWork {

	private Long projectID;

	/*
	 * I made the record for more than a pair because of the example row. At
	 * least... there are cases I guess where there can exist more periods working on
	 * the same project by different employees, or... should be done a comparison of
	 * the period... if the period only includes two people but sometimes they can
	 * work on the project alone too.
	 * 
	 * @ArrayList<Long> is for one employee different periods of joining and with
	 * its days (although) it is not so important for the current requirement
	 */
	private Map<Long, ArrayList<Long>> employees = new HashMap<Long, ArrayList<Long>>();

	private Long daysWorkedAll;

	public ProjectPairWork(Long projectID) {
		super();
		this.projectID = projectID;
		this.daysWorkedAll = (long) 0;
	}

	public Map<Long, ArrayList<Long>> getEmployees() {
		return employees;
	}

	public Long getDaysWorkedAll() {
		return daysWorkedAll;
	}

	public void addEmployee(Long empID, Long daysWorked) {

		if (employees.containsKey(empID)) {
			employees.get(empID).add(daysWorked);
		} else {
			ArrayList<Long> lst = new ArrayList<>();
			lst.add(daysWorked);
			employees.put(empID, lst);
		}
		this.addDays(daysWorked);
	}

	/*
	 * sum the days with a new sorted
	 */
	public void addDays(Long days) {

		daysWorkedAll += days;
	}

	/*
	 * Generate record of Project with  Employees and Days worked on it.
	 */
	public Object[] createProjectRow() {

		Object[] rowDataGrid;

		String employeesIDs = "";
		for (Long employeeID : employees.keySet()) {
			employeesIDs += Headers.EmployeeID.toString() + employeeID + ",	";
		}
		rowDataGrid = new Object[] { employeesIDs, projectID.toString(), daysWorkedAll.toString() };
		return rowDataGrid;
	}

	@Override
	public String toString() {

		String employeesIDs = "";
		for (Long employeeID : employees.keySet()) {
			employeesIDs += Headers.EmployeeID.toString() + employeeID + ", ";
		}
		return "\n" + employeesIDs + projectID + ", " + daysWorkedAll + "\n";
	}

}
