package com.payroll.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import com.payroll.dto.MonthDto;
import com.payroll.dto.YearDto;

public class DenominationOpt extends BaseClass implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	private JLabel reportName ;
	private JLabel sdateLeb,lblmonth;
	private JButton exitButton,excelButton;
 
	private JComboBox year,month;
	String ClassNm,repNm;;
	private JCheckBox box1,box2,box3,box4,box5,box6,box7,box8,box9,box10;
	 
	public  DenominationOpt(String nm,String repNm)
	{
		 
		ClassNm=nm;
		this.repNm=repNm;
		
		//setUndecorated(true);
		setResizable(false);
		setSize(541, 470);	
		setLocationRelativeTo(null);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		getContentPane().setLayout(null);
		 
		///////////////////////////////////////////////////////////

		reportName = new JLabel(repNm);
		reportName.setHorizontalAlignment(SwingConstants.CENTER);
		reportName.setFont(new Font("Tahoma", Font.BOLD, 14));
		reportName.setBounds(100, 46, 303, 20);
		getContentPane().add(reportName);



		box1 = new JCheckBox("2000",true);
		box1.setBounds(122, 74, 150, 20);
		getContentPane().add(box1);   
		 
		box2 = new JCheckBox("1000",true);
		box2.setBounds(122, 99, 150, 20);
		getContentPane().add(box2);   
		
		box3 = new JCheckBox("500",true);
		box3.setBounds(122, 124, 150, 20);
		getContentPane().add(box3);   
		
		box4 = new JCheckBox("100",true);
		box4.setBounds(122, 149, 150, 20);
		getContentPane().add(box4);   
		
		box5 = new JCheckBox("50",true);
		box5.setBounds(122, 174, 150, 20);
		getContentPane().add(box5);   
		
		box6 = new JCheckBox("20",true);
		box6.setBounds(122, 199, 150, 20);
		getContentPane().add(box6);   
		
		box7 = new JCheckBox("10",true);
		box7.setBounds(122, 224, 150, 20);
		getContentPane().add(box7,true);   
		
		box8 = new JCheckBox("5",true);
		box8.setBounds(122, 249, 150, 20);
		getContentPane().add(box8);   
		
		box9 = new JCheckBox("2",true);
		box9.setBounds(122, 274, 150, 20);
		getContentPane().add(box9);   

		box10 = new JCheckBox("1",true);
		box10.setBounds(122, 299, 150, 20);
		getContentPane().add(box10);   

		
		sdateLeb = new JLabel("Financial Year:");
		sdateLeb.setBounds(122, 326, 150, 20);
		getContentPane().add(sdateLeb);

		year = new JComboBox(loginDt.getFyear());
		year.setBounds(276, 326, 136, 23);
		getContentPane().add(year);
		year.setActionCommand("year");
		year.setMaximumRowCount(12);


		lblmonth = new JLabel("Month:");
		lblmonth.setBounds(122, 355, 150, 20);
		getContentPane().add(lblmonth);

		month = new JComboBox(loginDt.getFmonth());
		month.setBounds(276, 355, 136, 23);
		getContentPane().add(month);
		year.setActionCommand("month");
		month.setMaximumRowCount(12);
		
		
		///////////////////////////////////////////////////////////////////////////////////////////////

		exitButton = new JButton("Exit");
		exitButton.setBounds(283, 387, 70, 30);
		getContentPane().add(exitButton);


		excelButton = new JButton("Submit");
		excelButton.setBounds(181, 387, 82, 30);
		getContentPane().add(excelButton);
		
				
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
				panel_1.setBounds(15, 23, 510, 407);
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
		
		if(e.getActionCommand().equalsIgnoreCase("Submit"))
		{
			try
			{
				int btnno=1;
				
				YearDto yd = (YearDto) year.getSelectedItem();
				MonthDto mdto = (MonthDto) month.getSelectedItem();

				Class<?> clazz = Class.forName(ClassNm);
				// create an instance
	 
				Constructor<?> constructor = clazz.getConstructor(Integer.class,Integer.class,Integer.class,Integer.class, String.class,String.class,String.class,Integer.class,HashMap.class);
				Object ob = (Object)constructor.newInstance(loginDt.getDepo_code(),loginDt.getCmp_code(),yd.getYearcode(),mdto.getMnthcode(),loginDt.getBrnnm(),loginDt.getDrvnm(),mdto.getMnthname(),btnno,getCheckBoxValues());
				 
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
 
	
	
	public HashMap getCheckBoxValues()
	{
		HashMap checkList = new HashMap();
		
		checkList.put("2000",false);
		checkList.put("1000",false);
		checkList.put("500",false);
		checkList.put("100",false);
		checkList.put("50",false);
		checkList.put("20",false);
		checkList.put("10",false);
		checkList.put("5",false);
		checkList.put("2",false);
		checkList.put("1",false);
		
		if(box1.isSelected())
		{
			checkList.put("2000",true);
		}
		if(box2.isSelected())
		{
			checkList.put("1000",true);
		}
		if(box3.isSelected())
		{
			checkList.put("500",true);
		}
		if(box4.isSelected())
		{
			checkList.put("100",true);
		}
		if(box5.isSelected())
		{
			checkList.put("50",true);
		}
		if(box6.isSelected())
		{
			checkList.put("20",true);
		}
		if(box7.isSelected())
		{
			checkList.put("10",true);
		}
		if(box8.isSelected())
		{
			checkList.put("5",true);
		}
		if(box9.isSelected())
		{
			checkList.put("2",true);
		}
		if(box10.isSelected())
		{
			checkList.put("1",true);
		}
		
		return checkList;
		
		
	}
	
	public void setVisible(boolean b)
	{
		month.setSelectedIndex(loginDt.getMno());
		 
		super.setVisible(b);
		 
	}
	
	
}


