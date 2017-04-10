package com.payroll.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import com.payroll.dto.EmployeeMastDto;
import com.payroll.dto.YearDto;

public class MonthlyOpt extends BaseClass implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	private JLabel reportName ;
	private JLabel sdateLeb,lblmonth;
	private JButton exitButton,excelButton;
 
	private JComboBox year,employee;
	String ClassNm,repNm;;
  
	 
	public  MonthlyOpt(String nm,String repNm)
	{
		 
		ClassNm=nm;
		this.repNm=repNm;
		
		//setUndecorated(true);
		setResizable(false);
		setSize(400, 285);	
		setLocationRelativeTo(null);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		getContentPane().setLayout(null);
		 
		///////////////////////////////////////////////////////////

		reportName = new JLabel(repNm);
		reportName.setHorizontalAlignment(SwingConstants.CENTER);
		reportName.setFont(new Font("Tahoma", Font.BOLD, 14));
		reportName.setBounds(49, 46, 303, 20);
		getContentPane().add(reportName);

		sdateLeb = new JLabel("Financial Year:");
		sdateLeb.setBounds(45, 100, 117, 20);
		getContentPane().add(sdateLeb);

		year = new JComboBox(loginDt.getFyear());
		year.setBounds(172, 100, 198, 23);
		getContentPane().add(year);
		year.setActionCommand("year");
		year.setMaximumRowCount(12);


		lblmonth = new JLabel("Employee:");
		lblmonth.setBounds(45, 129, 117, 20);
		getContentPane().add(lblmonth);

		employee = new JComboBox(loginDt.getEmpList());
		employee.setBounds(172, 129, 198, 23);
		getContentPane().add(employee);
		year.setActionCommand("month");
		employee.setMaximumRowCount(12);

		
		
		///////////////////////////////////////////////////////////////////////////////////////////////

		exitButton = new JButton("Exit");
		exitButton.setBounds(216, 177, 70, 30);
		getContentPane().add(exitButton);


		excelButton = new JButton("Excel");
		excelButton.setBounds(114, 177, 82, 30);
		getContentPane().add(excelButton);

		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(15, 23, 366, 208);
		getContentPane().add(panel_1);

		exitButton.addActionListener(this);
		excelButton.addActionListener(this);
		year.addActionListener(this);
		
		setAlwaysOnTop(true);
		
	}

	 public void resetAll()
	 {
		 
				 year.setSelectedIndex(0);
	 }		 
	 

	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equalsIgnoreCase("Exit"))
		{
			dispose();
		}
		
		if(e.getActionCommand().equalsIgnoreCase("Excel") || e.getActionCommand().equalsIgnoreCase("Upload") ||  e.getActionCommand().equalsIgnoreCase("PDF") || e.getActionCommand().equalsIgnoreCase("View"))
		{
			try
			{
				int repno=1;

				if(repNm.startsWith("Absent"))
					repno=1;
				else if(repNm.startsWith("OT"))
					repno=2;
				else if(repNm.startsWith("Sterile"))
					repno=3;
				if(repNm.startsWith("Present"))
					repno=4;
				
				
				YearDto yd = (YearDto) year.getSelectedItem();
				EmployeeMastDto edto = (EmployeeMastDto) employee.getSelectedItem();

				System.out.println("ecode "+edto.getEmp_code());
				Class<?> clazz = Class.forName(ClassNm);
				// create an instance
				Constructor<?> constructor = clazz.getConstructor(Integer.class,Integer.class,Integer.class,Integer.class, String.class,String.class,Integer.class);
				Object ob = (Object)constructor.newInstance(loginDt.getDepo_code(),loginDt.getCmp_code(),yd.getYearcode(),edto.getEmp_code(),loginDt.getBrnnm(),loginDt.getDrvnm(),repno);
				resetAll();
				dispose();
			}
			catch(Exception ez)
			{
				System.out.println(ez);
				 ez.printStackTrace();

			}
		}


 
	}
 
	
	
	
}


