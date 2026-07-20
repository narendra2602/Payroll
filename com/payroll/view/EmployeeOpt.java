package com.payroll.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import com.payroll.dto.EmployeeMastDto;
import com.payroll.dto.MonthDto;
import com.payroll.dto.YearDto;
import com.payroll.print.SalaryList;

public class EmployeeOpt extends BaseClass implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	private JLabel reportName ;
	private JLabel sdateLeb,lblmonth,lbltomonth,lblemployee;
	private JButton exitButton,excelButton;
 
	private JComboBox year,smonth,emonth,employee;
	String ClassNm,repNm;

	 
	public  EmployeeOpt(String nm,String repNm)
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
		reportName.setBounds(49, 44, 310, 20);
		getContentPane().add(reportName);

		
		//Group the radio buttons.
	    ButtonGroup group = new ButtonGroup();
	    
	    
		sdateLeb = new JLabel("Financial Year:");
		sdateLeb.setBounds(45, 76, 117, 20);
		getContentPane().add(sdateLeb);

		year = new JComboBox(loginDt.getFyear());
		year.setBounds(167, 76, 198, 23);
		getContentPane().add(year);
		year.setActionCommand("year");
		year.setMaximumRowCount(12);



		
		lblmonth = new JLabel("From Month:");
		lblmonth.setBounds(45, 105, 117, 20);
		getContentPane().add(lblmonth);

		smonth = new JComboBox(new Vector(loginDt.getFmonth()));
		smonth.setBounds(167, 105, 198, 23);
		getContentPane().add(smonth);
		smonth.setActionCommand("month");
		smonth.setMaximumRowCount(12);
		

		lbltomonth = new JLabel("To Month:");
		lbltomonth.setBounds(45, 134, 117, 20);
		getContentPane().add(lbltomonth);

		emonth = new JComboBox(new Vector(loginDt.getFmonth()));
		emonth.setBounds(167, 134, 198, 23);
		getContentPane().add(emonth);
		emonth.setActionCommand("emonth");
		emonth.setMaximumRowCount(12);

		
		lblemployee = new JLabel("Employee:");
		lblemployee.setBounds(45, 163, 117, 20);
		getContentPane().add(lblemployee);

		employee = new JComboBox(loginDt.getEmpList());
		employee.setBounds(167, 163, 198, 23);
		getContentPane().add(employee);
		year.setActionCommand("year");
		employee.setMaximumRowCount(12);
		
		///////////////////////////////////////////////////////////////////////////////////////////////

		exitButton = new JButton("Exit");
		exitButton.setBounds(238, 207, 70, 30);
		getContentPane().add(exitButton);


		excelButton = new JButton("Excel");
		excelButton.setBounds(136, 207, 82, 30);
		getContentPane().add(excelButton);

		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(15, 23, 366, 222);
		getContentPane().add(panel_1);

		exitButton.addActionListener(this);
		excelButton.addActionListener(this);
		year.addActionListener(this);
		setAlwaysOnTop(true);
		
	}

	 public void resetAll()
	 {
		 
		 		
				 year.setSelectedIndex(0);
				 smonth.setSelectedIndex(0);
				 emonth.setSelectedIndex(11);
				 employee.setSelectedIndex(0);
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
			System.out.println("YEAR OPTION IS CHAGNED OR SELECTED ");
			
			YearDto yd = (YearDto) year.getSelectedItem();
			Vector v = (Vector) loginDt.getFmon().get(yd.getYearcode());
			System.out.println("**** "+v.size());
			
			smonth.removeAllItems();
			emonth.removeAllItems();
			MonthDto mn=null;
			for (int i=0; i<v.size();i++)
			{
				 mn = (MonthDto) v.get(i);
				 smonth.addItem(mn);
				 emonth.addItem(mn);
				 
			}
			smonth.setSelectedIndex(0);
			emonth.setSelectedIndex(11);
		}

				
		if(e.getActionCommand().equalsIgnoreCase("Excel") || e.getActionCommand().equalsIgnoreCase("Upload") ||  e.getActionCommand().equalsIgnoreCase("PDF") || e.getActionCommand().equalsIgnoreCase("View") || e.getActionCommand().equalsIgnoreCase("Print"))
		{
			try
			{
				int btnno=1;
				int repno=20;
				int opt=1;
				
				if(e.getActionCommand().equalsIgnoreCase("Excel") || e.getActionCommand().equalsIgnoreCase("Print"))
					btnno=2;
				
				
				YearDto yd = (YearDto) year.getSelectedItem();
				MonthDto mdto = (MonthDto) smonth.getSelectedItem();
				MonthDto emdto = (MonthDto) emonth.getSelectedItem();
				EmployeeMastDto edto = (EmployeeMastDto) employee.getSelectedItem();
				Class<?> clazz = Class.forName(ClassNm);
				// create an instance
				
					Constructor<?> constructor = clazz.getConstructor(Integer.class,Integer.class,Integer.class,Integer.class, String.class,String.class,String.class,Integer.class,Integer.class,Integer.class,String.class,Integer.class,Integer.class);
					Object ob = (Object)constructor.newInstance(loginDt.getDepo_code(),loginDt.getCmp_code(),yd.getYearcode(),mdto.getMnthcode(),loginDt.getBrnnm(),loginDt.getDrvnm(),mdto.getMnthname(),btnno,repno,opt,loginDt.getAddress(),emdto.getMnthcode(),edto.getEmp_code());
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
 
	
	public void setVisible(boolean b)
	{
        smonth.setSelectedIndex(0);
		emonth.setSelectedIndex(loginDt.getMno());

		super.setVisible(b);
		 
	}
	
	
}


