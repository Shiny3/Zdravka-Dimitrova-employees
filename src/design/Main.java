package design;

import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;

import cvsreader.ReaderOpenCSV;
import model.Employee;
import model.ProjectPairWork;

public class Main {

	public Main() {
		super();
	}

	public static void main(String[] args) throws IOException {
		FileSelector.fileWindow();
	}
}
