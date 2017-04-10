package com.payroll.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import com.payroll.dto.MonthDto;
import com.payroll.dto.YearDto;
import com.payroll.print.SalaryList;

public class PayrollOpt extends BaseClass implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	private JLabel reportName ;
	private JLabel sdateLeb,lblmonth;
	private JButton exitButton,excelButton,uploadButton;
 
	private JComboBox year,month;
	String ClassNm,repNm;;
	private JRadioButton pfbtn,arrearbtn;

	 
	public  PayrollOpt(String nm,String repNm)
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

		
		pfbtn = new JRadioButton("PF");
		pfbtn.setSelected(true);
		pfbtn.setBounds(85, 67, 70, 23);
		getContentPane().add(pfbtn);

		arrearbtn = new JRadioButton("Arrear");
		arrearbtn.setBounds(209, 67, 80, 23);
		getContentPane().add(arrearbtn);

		//Group the radio buttons.
	    ButtonGroup group = new ButtonGroup();
	    group.add(pfbtn);
	    group.add(arrearbtn);
	    
	    
		sdateLeb = new JLabel("Financial Year:");
		sdateLeb.setBounds(45, 100, 150, 20);
		getContentPane().add(sdateLeb);

		year = new JComboBox(loginDt.getFyear());
		year.setBounds(199, 100, 136, 23);
		getContentPane().add(year);
		year.setActionCommand("year");
		year.setMaximumRowCount(12);


		lblmonth = new JLabel("Month:");
		lblmonth.setBounds(45, 129, 150, 20);
		getContentPane().add(lblmonth);

		month = new JComboBox(loginDt.getFmonth());
		month.setBounds(199, 129, 136, 23);
		getContentPane().add(month);
		year.setActionCommand("month");
		month.setMaximumRowCount(12);
		
		
		///////////////////////////////////////////////////////////////////////////////////////////////

		exitButton = new JButton("Exit");
		exitButton.setBounds(260, 177, 70, 30);
		getContentPane().add(exitButton);


		excelButton = new JButton("Excel");
		excelButton.setBounds(158, 177, 82, 30);
		getContentPane().add(excelButton);
		
		uploadButton = new JButton("Upload");
		uploadButton.setBounds(55, 177, 82, 30);
		getContentPane().add(uploadButton);

		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(15, 23, 366, 208);
		getContentPane().add(panel_1);

		exitButton.addActionListener(this);
		excelButton.addActionListener(this);
		uploadButton.addActionListener(this);
		year.addActionListener(this);
		
		pfbtn.addActionListener(this);
		pfbtn.setActionCommand("1");
		arrearbtn.addActionListener(this);
		arrearbtn.setActionCommand("2");
		setAlwaysOnTop(true);
		
	}

	 public void resetAll()
	 {
		 
		 		 pfbtn.setSelected(true);
				 year.setSelectedIndex(0);
	 }		 
	 

	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equalsIgnoreCase("Exit"))
		{
			dispose();
		}
		
		if(e.getActionCommand().equalsIgnoreCase("Excel") || e.getActionCommand().equalsIgnoreCase("Upload") ||  e.getActionCommand().equalsIgnoreCase("PDF") || e.getActionCommand().equalsIgnoreCase("View") || e.getActionCommand().equalsIgnoreCase("Print"))
		{
			try
			{
				int btnno=1;
				int repno=1;
				int opt=1;
				if(arrearbtn.isSelected())
					opt=2;
				
				if(e.getActionCommand().equalsIgnoreCase("Excel") || e.getActionCommand().equalsIgnoreCase("Print"))
					btnno=2;
				
				if(repNm.equalsIgnoreCase("PF List"))
					repno=2;
				else if(repNm.equalsIgnoreCase("Loan & Advance List"))
					repno=3;
				else if(repNm.equalsIgnoreCase("Sterile Days Report"))
					repno=4;
				else if(repNm.equalsIgnoreCase("Attendance Check List"))
					repno=5;
				else if(repNm.equalsIgnoreCase("OT Hrs Report"))
					repno=6;
				else if(repNm.startsWith("Absent"))
					repno=7;
				else if(repNm.startsWith("Misc"))
					repno=8;
				else if(repNm.startsWith("Present"))
					repno=9;
				else if(repNm.startsWith("Salary List"))
					repno=10;
				else if(repNm.startsWith("LTA/Medical"))
					repno=13;
				else if(repNm.startsWith("UnPaid LTA/Medical"))
					repno=14;
				
				YearDto yd = (YearDto) year.getSelectedItem();
				MonthDto mdto = (MonthDto) month.getSelectedItem();

				Class<?> clazz = Class.forName(ClassNm);
				// create an instance
				
				if(repno==10 && e.getActionCommand().equalsIgnoreCase("PDF"))
				{
					new SalaryList(loginDt.getDepo_code(),loginDt.getCmp_code(),yd.getYearcode(),mdto.getMnthcode(),loginDt.getBrnnm(),loginDt.getDrvnm(),mdto.getMnthname(),btnno,repno);
				}
				else if(repNm.contains("Label Print"))
				{
					Constructor<?> constructor = clazz.getConstructor(Integer.class,Integer.class,Integer.class,Integer.class, String.class,String.class,String.class,Integer.class,Integer.class,String.class);
					Object ob = (Object)constructor.newInstance(loginDt.getDepo_code(),loginDt.getCmp_code(),yd.getYearcode(),mdto.getMnthcode(),loginDt.getBrnnm(),loginDt.getDrvnm(),mdto.getMnthname(),btnno,repno,loginDt.getPrinternm());
				}
				else
				{
					Constructor<?> constructor = clazz.getConstructor(Integer.class,Integer.class,Integer.class,Integer.class, String.class,String.class,String.class,Integer.class,Integer.class,Integer.class);
					Object ob = (Object)constructor.newInstance(loginDt.getDepo_code(),loginDt.getCmp_code(),yd.getYearcode(),mdto.getMnthcode(),loginDt.getBrnnm(),loginDt.getDrvnm(),mdto.getMnthname(),btnno,repno,opt);
				}
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
		month.setSelectedIndex(loginDt.getMno());
		uploadButton.setText("View");
		pfbtn.setVisible(false);
		arrearbtn.setVisible(false);
		if(repNm.startsWith("Salary List"))
		{
			uploadButton.setText("PDF");
		}
		else if(repNm.equalsIgnoreCase("Salary Register") || repNm.equalsIgnoreCase("Salary Slip") )
		{
			excelButton.setText("PDF");
		}
		else if(repNm.contains("Label Printing"))
		{						   
			excelButton.setText("Print");
		}
		else if(repNm.equalsIgnoreCase("ESIC List"))
		{
			pfbtn.setText("ESIC");
			pfbtn.setVisible(true);
			arrearbtn.setVisible(true);
			uploadButton.setText("Upload");	
		}
		else if(repNm.equalsIgnoreCase("PF List"))
		{
			pfbtn.setVisible(true);
			arrearbtn.setVisible(true);
			uploadButton.setText("Upload");	
		}
		else if(repNm.startsWith("LTA/Medical") || repNm.startsWith("UnPaid LTA/Medical"))
		{
			month.setVisible(false);
			lblmonth.setVisible(false);
		}
		super.setVisible(b);
		 
	}
	
	
}


