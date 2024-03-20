package cvsreader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import model.Employee;
import model.ProjectPairWork;

public class ReaderOpenCSV {

	public static Map<Long, ProjectPairWork> projectID_Workers = new HashMap<>();

	/*
	 * public class CustomCSVParser extends CSVParser { public CustomCSVParser(char
	 * separator) { super();//"\\;", "\\;", "\\;", false, false, false,
	 * nullFieldIndicator(), errorLocale); }
	 * 
	 * @Override public String[] parseLine(String nextLine) throws IOException {
	 * 
	 * return null; } }
	 */

	public Map<Long, ProjectPairWork> readEmployees(String path) throws IOException {

		// CSVReader reader = new CSVReader(new FileReader("your_csv_file.csv"), new
		// CustomCSVParser(";"));

		CSVReader reader = new CSVReader(new FileReader(path));
		//CSVParser parser = new CSVParserBuilder().withSeparator(';') // Specify the delimiter (e.g., tab)
		//		.withIgnoreQuotations(true) // Ignore quoted fields (optional)
		//		.build();

		List<Employee> employees = new ArrayList<Employee>();
		String[] cells = null;

		try {
			cells = reader.readNext();
			while ((cells = reader.readNext()) != null) {

				//String[] fields = parser.parseLine(cells.toString());

				Long emplID = Long.parseLong(cells[0]);
				Long proID = Long.parseLong(cells[1]);

				Date from = DateHelper.convertDate(cells[2]);
				Date to = DateHelper.convertDate(cells[3]);

				Employee employee = new Employee();
				employee.setEmpID(emplID);
				employee.setProjectID(proID);
				employee.setDateFrom(from);
				employee.setDateTo(to);

				Long daysworked;

				Long dateInMillies = Math.abs(employee.getDateTo().getTime() - employee.getDateFrom().getTime());

				daysworked = TimeUnit.DAYS.convert(dateInMillies, TimeUnit.MILLISECONDS);

				employee.setDaysWorked(daysworked);

				if (projectID_Workers.containsKey(proID)) {
					projectID_Workers.get(proID).addEmployee(emplID, daysworked);
					System.out.println("repeated" + proID);
				} else {
					ProjectPairWork conjuctions = new ProjectPairWork(proID);
					conjuctions.addEmployee(emplID, daysworked);
					projectID_Workers.put(proID, conjuctions);
					System.out.println(proID);
				}
				employees.add(employee);
			}

		} catch (CsvValidationException | IOException e) {
			e.printStackTrace();
		}
		reader.close();

		return projectID_Workers;
	}
}