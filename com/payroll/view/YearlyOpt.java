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

import com.payroll.dto.YearDto;

public class YearlyOpt extends BaseClass implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	private JLabel reportName ;
	private JLabel sdateLeb;
	private JButton exitButton,excelButton;
 
	private JComboBox year;
	String ClassNm,repNm;;
	private JRadioButton paidbtn,pendingbtn;
	 
	public  YearlyOpt(String nm,String repNm)
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

		
		
		paidbtn = new JRadioButton("Paid");
		paidbtn.setVisible(false);
		paidbtn.setSelected(true);
		paidbtn.setBounds(85, 77, 90, 23);
		getContentPane().add(paidbtn);

		pendingbtn = new JRadioButton("Pending");
		pendingbtn.setVisible(false);
		pendingbtn.setBounds(209, 77, 90, 23);
		getContentPane().add(pendingbtn);

		//Group the radio buttons.
	    ButtonGroup group = new ButtonGroup();
	    group.add(paidbtn);
	    group.add(pendingbtn);
		
		sdateLeb = new JLabel("Financial Year:");
		sdateLeb.setBounds(45, 114, 150, 20);
		getContentPane().add(sdateLeb);

		year = new JComboBox(loginDt.getFyear());
		year.setBounds(199, 114, 136, 23);
		getContentPane().add(year);
		year.setActionCommand("year");
		year.setMaximumRowCount(12);
		year.setActionCommand("month");

		
		
		///////////////////////////////////////////////////////////////////////////////////////////////

		exitButton = new JButton("Exit");
		exitButton.setBounds(220, 171, 70, 30);
		getContentPane().add(exitButton);


		excelButton = new JButton("Excel");
		excelButton.setBounds(118, 171, 82, 30);
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
				 paidbtn.setSelected(true);
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
				 
				if(pendingbtn.isSelected())
					repno=11;
				
				if(repNm.startsWith("Absent"))
					repno=1;
				else if(repNm.startsWith("OT"))
					repno=2;
				else if(repNm.startsWith("Sterile"))
					repno=3;
				else if(repNm.startsWith("Present"))
					repno=4;
				else if(repNm.startsWith("Professional"))
					repno=7;
				else if(repNm.startsWith("Bonus"))
				{
					if(pendingbtn.isSelected())
						repno=51;
					else 
						repno=5;
				}
				
				
				YearDto yd = (YearDto) year.getSelectedItem();

				Class<?> clazz = Class.forName(ClassNm);
				// create an instance
				Constructor<?> constructor = clazz.getConstructor(Integer.class,Integer.class,Integer.class, String.class,String.class,Integer.class);
				Object ob = (Object)constructor.newInstance(loginDt.getDepo_code(),loginDt.getCmp_code(),yd.getYearcode(),loginDt.getBrnnm(),loginDt.getDrvnm(),repno);
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
		 
		paidbtn.setVisible(false);
		pendingbtn.setVisible(false);
		if(repNm.equalsIgnoreCase("Arrear Report"))
		{
				paidbtn.setVisible(true);
				pendingbtn.setVisible(true);
		}
		else if(repNm.equalsIgnoreCase("Bonus Register"))
		{
				paidbtn.setText("Register");
				pendingbtn.setText("Form 4(C)");
				paidbtn.setVisible(true);
				pendingbtn.setVisible(true);
		}
		super.setVisible(b);
		 
	}
	
	
}


