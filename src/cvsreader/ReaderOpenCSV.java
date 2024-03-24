package cvsreader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.opencsv.CSVReader;

import model.Employee;
import model.ProjectPairWork;

public class ReaderOpenCSV {

	public static Map<Long, ProjectPairWork> projectID_Workers = new HashMap<>();

	private List<Employee> employees = new ArrayList<Employee>();

	public Map<Long, ProjectPairWork> readEmployees(String path) throws IOException, ParseException {

		CSVReader employee_reader = new CSVReader(new FileReader(path));
		char delimiter = ',';
		try (BufferedReader buffered_reader = new BufferedReader(new FileReader(path))) {

			String employee_record = buffered_reader.readLine();

			System.out.println(employee_record);

			while ((employee_record = buffered_reader.readLine()) != null) {
				
				if (employee_record.startsWith("//")) {
					continue;
				}
				
				if (employee_record.startsWith("*")) {
					// Unparseable date: "02001.December.18 AD 12:08 PM"
					continue;
				}
				
				String[] splitted_record = ReadHelper.splitter(employee_record, delimiter, 3);
				
				Long emplID = Long.parseLong(splitted_record[0]);
				Long proID = Long.parseLong(splitted_record[1]);
				String dates_csv = splitted_record[2];

				Date from = null;
				Date to = null;

				long daysworked = (long) 0;
				Employee employee = new Employee();

				ArrayList<Date> dates = DateHelper.matchestDates(dates_csv);
				if (dates == null || dates.size() == 0) {
					System.out.println("ERROOOOR - not parsed ");
				} else if (dates.size() == 2) {
					from = dates.get(0);
					to = dates.get(1);
				} else {
					from = new Date();
					to = new Date();
				}

				System.out.println("dates-from:	" + from + "		date-to:	" + to);
				employee.setDateFrom(from);
				employee.setDateTo(to);
				Long dateInMillies = Math.abs(employee.getDateTo().getTime() - employee.getDateFrom().getTime());
				daysworked = TimeUnit.DAYS.convert(dateInMillies, TimeUnit.MILLISECONDS);
				employee.setDaysWorked(daysworked);

				employee.setEmpID(emplID);
				employee.setProjectID(proID);
				employee.setDateFrom(from);
				employee.setDateTo(to);

				if (projectID_Workers.containsKey(proID)) {
					projectID_Workers.get(proID).addEmployee(emplID, daysworked);
				} else {
					ProjectPairWork conjuctions = new ProjectPairWork(proID);
					conjuctions.addEmployee(emplID, daysworked);
					projectID_Workers.put(proID, conjuctions);
				}
				employees.add(employee);

				System.out.println();
				System.out.println();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		employee_reader.close();

		return projectID_Workers;
	}
}