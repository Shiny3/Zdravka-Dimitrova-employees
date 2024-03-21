package cvsreader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.opencsv.CSVReader;
import com.opencsv.stream.reader.LineReader;

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

	public static String[] splitter(String sentence, char delimiter, int LIMIT_COUNTER_WORDS) {

		int index_beginning = 0;
		int cnt_word = 0;
		int condition = LIMIT_COUNTER_WORDS-1;
		String[] words = new String[LIMIT_COUNTER_WORDS+1];

		for (Integer position_delimiter = 0; position_delimiter < sentence.length(); position_delimiter++) {

			if (sentence.charAt(position_delimiter) == delimiter) {
				words[cnt_word] = sentence.substring(index_beginning, position_delimiter);
				index_beginning = position_delimiter+ 1;
				cnt_word++;
			}
			if (cnt_word == condition) {
				words[cnt_word] = sentence.substring(index_beginning, sentence.length());
				//number_of_words++;
				break;
			}	

		}

		for (String word : words) {
			System.out.println(word);
		}
		return words;
	}

	public Map<Long, ProjectPairWork> readEmployees(String path) throws IOException {

		CSVReader employee_reader = new CSVReader(new FileReader(path));

		List<Employee> employees = new ArrayList<Employee>();
		char delimiter = ',';
		try (BufferedReader buffered_reader = new BufferedReader(new FileReader(path))) {

			String employee_record;

			buffered_reader.readLine();
			
			while ((employee_record = buffered_reader.readLine()) != null) {

				String[] splitted_record = splitter(employee_record, delimiter, 3);

				Long emplID = Long.parseLong(splitted_record[0]);
				Long proID = Long.parseLong(splitted_record[1]);
				String dates_csv = splitted_record[2];
				
				Date from = null;
				Date to = null;

				ArrayList<String> dates = DateHelper.matchestDates(dates_csv);
				
				int index = dates_csv.indexOf(delimiter);
				int length = dates_csv.length();
				System.out.println(dates_csv);		
					
				/*
				if(index!=0 && dates_csv.substring(index+1, length).indexOf(delimiter)!=0)
				{
					if (dates.size() == 2) {
						from = DateHelper.convertDate(dates.get(0), dates.get(2));
						to = DateHelper.convertDate(dates.get(1), dates.get(2));
					}  
				}
				else {

				}			
				*
				*/
				String dateFrom = dates_csv.substring(0, index);
				System.out.println(dateFrom);	
				String dateTo = dates_csv.substring(index+1, length);
				System.out.println(dateTo);	
				from = DateHelper.convertDate(dateFrom);
				to = DateHelper.convertDate(dateTo);


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
				} else {
					
					ProjectPairWork conjuctions = new ProjectPairWork(proID);
					conjuctions.addEmployee(emplID, daysworked);
					projectID_Workers.put(proID, conjuctions);
				}
				employees.add(employee);
			}
			 
		} catch (IOException e) {
			e.printStackTrace();
		}
		employee_reader.close();

		return projectID_Workers;
	}
}