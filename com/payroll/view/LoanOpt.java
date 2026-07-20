package com.payroll.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import com.payroll.dto.EmployeeMastDto;
import com.payroll.dto.MonthDto;
import com.payroll.dto.YearDto;
import com.payroll.print.SalaryList;

public class LoanOpt extends BaseClass implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	private JLabel reportName ;
	private JLabel sdateLeb,lblmonth;
	private JButton exitButton,excelButton,uploadButton;
 
	private JComboBox year,month;
	String ClassNm,repNm;;
	

	 
	public  LoanOpt(String nm,String repNm)
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
		reportName.setBounds(49, 44, 303, 20);
		getContentPane().add(reportName);

		
			    
		sdateLeb = new JLabel("Select Employee:");
		sdateLeb.setBounds(33, 101, 150, 20);
		getContentPane().add(sdateLeb);

		year = new JComboBox(loginDt.getEmpList());
		year.setBounds(143, 100, 209, 23);
		getContentPane().add(year);
		year.setActionCommand("year");

		
		///////////////////////////////////////////////////////////////////////////////////////////////

		exitButton = new JButton("Exit");
		exitButton.setBounds(260, 177, 70, 30);
		getContentPane().add(exitButton);


		excelButton = new JButton("Excel");
		excelButton.setBounds(143, 177, 82, 30);
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
			resetAll();
			dispose();
		}

		if(e.getActionCommand().equalsIgnoreCase("Year"))
		{
			
			EmployeeMastDto empDto= (EmployeeMastDto) year.getSelectedItem();
		
		}

				
		if(e.getActionCommand().equalsIgnoreCase("Excel") )
		{
			try
			{
				
				EmployeeMastDto empDto= (EmployeeMastDto) year.getSelectedItem();

				System.out.println(empDto.getEmp_name());
				Class<?> clazz = Class.forName(ClassNm);
				// create an instance
				
					Constructor<?> constructor = clazz.getConstructor(Integer.class,Integer.class,Integer.class, String.class,String.class,String.class);
					Object ob = (Object)constructor.newInstance(loginDt.getDepo_code(),loginDt.getCmp_code(),empDto.getEmp_code(),loginDt.getBrnnm(),empDto.getEmp_name(),loginDt.getDrvnm());


					
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


