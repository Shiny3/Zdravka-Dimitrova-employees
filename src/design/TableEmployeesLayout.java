package design;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import cvsreader.ReaderOpenCSV;
import model.ProjectPairWork;
import model.Headers;

public class TableEmployeesLayout {

	private JFrame frameProjects;

	private JTable projectsTable;

	private static DefaultTableModel tableModel = new DefaultTableModel();

	public JFrame projectsTable(Map<Long, ProjectPairWork> projectID_Workers) {
		
		frameProjects = new JFrame();
		frameProjects.setTitle("Sirma Projects");

		tableModel.addColumn(Headers.EmployeeIDs.toString());
		tableModel.addColumn(Headers.ProjectID.toString());
		tableModel.addColumn(Headers.Days.toString());

		for (ProjectPairWork project : ReaderOpenCSV.projectID_Workers.values()) {
			System.out.println(project.toString());
			tableModel.addRow(project.createProjectRow());
		}

		projectsTable = new JTable(tableModel);
		projectsTable.setBounds(600, 400, 500, 700);

		JScrollPane sp = new JScrollPane(projectsTable);
		frameProjects.add(sp, BorderLayout.CENTER);
		frameProjects.pack();
		frameProjects.setVisible(true);
		//sp.setBounds(new Rectangle(0, 0, 1000, 600));
		frameProjects.add(sp);
		//frameProjects.setSize(500, 200);
		frameProjects.setVisible(true);
		frameProjects.setLocation(800, 400);
		
		frameProjects.setAlwaysOnTop(true);
		frameProjects.setBackground(Color.PINK);
		frameProjects.setBackground(Color.GREEN);
		frameProjects.setLayout(new GridLayout(0, 4));
		
		frameProjects.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frameProjects.setSize(500, 600);
		frameProjects.pack();
		frameProjects.setVisible(true);
		frameProjects.show();
		return frameProjects;
		
		
	}

}
