package design;

import java.nio.file.FileSystems;
import java.text.ParseException;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import cvsreader.ReaderOpenCSV;

class FileSelector extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static JLabel fileEmployee = new JLabel();

	private static String filePath;

	private static final String lblReadButton = "Choose And Open CVS File.";

	public static final String EMPLOYEE_CSV_FILE_PATH = "\\src\\sources\\employee_data_777.csv";

	public static JFrame fileWindow() {

		JFrame frameFile = new JFrame("Sirma Interview CSV file Selector");
		frameFile.setSize(777, 444);
		frameFile.setVisible(true);
		frameFile.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameFile.setLocation(300, 300);

		JButton readButton = new JButton(lblReadButton);

		FileSelector fileChoser = new FileSelector();
		readButton.addActionListener(fileChoser);

		JPanel filePanel = new JPanel();
		filePanel.add(readButton);
		fileEmployee.setText("no file selected");

		filePanel.add(fileEmployee);
		frameFile.add(filePanel);
		frameFile.show();
		return frameFile;
	}

	public void actionPerformed(ActionEvent evt) {
		String com = evt.getActionCommand();

		if (com.equals(lblReadButton)) {

			// ;FileSystemView.getFileSystemView().getDefaultDirectory()
			File file_data = new File(FileSystems.getDefault().getPath("").toAbsolutePath() + EMPLOYEE_CSV_FILE_PATH);
			JFileChooser cvsChooser = new JFileChooser(file_data, FileSystemView.getFileSystemView());

			// JFileChooser cvsChooser = new JFileChooser(new File());
			cvsChooser.setAcceptAllFileFilterUsed(false);
			cvsChooser.setDialogTitle("Select employees data .csv file ");
			FileNameExtensionFilter restrict = new FileNameExtensionFilter(".csv files", "csv");
			cvsChooser.addChoosableFileFilter(restrict);
			int cvsFileDialog = cvsChooser.showOpenDialog(null);

			if (cvsFileDialog == JFileChooser.APPROVE_OPTION) {

				TableEmployeesLayout tableProjects = new TableEmployeesLayout();
				ReaderOpenCSV readerOpenCSV = new ReaderOpenCSV();

				filePath = cvsChooser.getSelectedFile().getAbsolutePath();
				fileEmployee.setText(filePath);
				if (FileSelector.filePath != null && !FileSelector.filePath.equals(""))
					try {
					 tableProjects.projectsTable(readerOpenCSV.readEmployees(FileSelector.filePath));
					} catch (IOException | ParseException e) {
						e.printStackTrace();
					}

			} else
				fileEmployee.setText("a file wasnt't chosen");
		}
	}
}
