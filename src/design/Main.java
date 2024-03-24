package design;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.text.ParseException;

import cvsreader.ReaderOpenCSV;

public class Main {
	public static final String EMPLOYEE_CSV_FILE_PATH_inprocess_1 = "\\src\\sources\\employee_data_777.csv";
	public static final String EMPLOYEE_CSV_FILE_PATH_success = "\\src\\sources\\employee_data_777.csv";
	public static final String  EMPLOYEE_CSV_FILE_PATH_inprocess= "\\src\\sources\\employee_data_777_1.csv";
	public Main() {
		super();
	}

	public static void main(String[] args) throws IOException, ParseException {
		
		ReaderOpenCSV readerOpenCSV = new ReaderOpenCSV();
		
		 FileSelector.fileWindow();
		
		
		readerOpenCSV.readEmployees(FileSystems.getDefault().getPath("").toAbsolutePath() + EMPLOYEE_CSV_FILE_PATH_inprocess);
	/*
		readerOpenCSV.readEmployees(FileSystems.getDefault().getPath("").toAbsolutePath() + EMPLOYEE_CSV_FILE_PATH_success);
			
		readerOpenCSV.readEmployees(FileSystems.getDefault().getPath("").toAbsolutePath() + EMPLOYEE_CSV_FILE_PATH_inprocess_1);
		*/
	}
}
