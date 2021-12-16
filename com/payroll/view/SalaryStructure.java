package com.payroll.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import com.payroll.dao.EmployeeDAO;
import com.payroll.dto.EmployeeMastDto;
import com.payroll.dto.YearDto;
import com.payroll.util.JDoubleField;

public class SalaryStructure extends BaseClass implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	private JLabel reportName ;
	private JButton exitButton,changetButton;
	String ClassNm,repNm;
	PreparedStatement curr=null;
	ResultSet rcurr=null;
	YearDto yd;
	private JLabel lblGross,lblBasic,lblDa,lblHra,lblAdd_hra,lblIncentive,lblSpl_incentive,lblLta,lblMedical,lblBonus,lblOtRate,lblSt_allow,lblFood_allowance;
	private  JDoubleField gross,basic,da,hra,add_hra,incentive,spl_incentive,lta,medical,bonus,ot_rate,st_allow,food_allownace;
	private EmployeeDAO empDao; 
	private JLabel lblPassword;	
	private JPasswordField passwordF;
	
	public  SalaryStructure(String nm,String repNm)
	{
		 
		ClassNm=nm;
		this.repNm=repNm;
		
		//setUndecorated(true);
		setResizable(false);
		setSize(587, 403);	
		setLocationRelativeTo(null);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		getContentPane().setLayout(null);
		///////////////////////////////////////////////////////////
		empDao = new EmployeeDAO();
		reportName = new JLabel(repNm);
		reportName.setHorizontalAlignment(SwingConstants.CENTER);
		reportName.setFont(new Font("Tahoma", Font.BOLD, 14));
		reportName.setBounds(125, 46, 303, 20);
		getContentPane().add(reportName);

		
		 
		lblPassword = new JLabel("Password:");
		lblPassword.setForeground(Color.BLUE);
		lblPassword.setBounds(207, 80, 70, 20);
		getContentPane().add(lblPassword);

		passwordF = new JPasswordField();
		passwordF.setBounds(308, 81, 159, 25);
		passwordF.setSelectionStart(0);
		getContentPane().add(passwordF);
		passwordF.addKeyListener(new KeyAdapter() 
		{
			public void keyPressed(KeyEvent evt) 
			{
				if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) 
				{
					resetAll();
					dispose();
				}
				
				if (evt.getKeyCode() == KeyEvent.VK_ENTER) 
				{
					checkPassword();
				}
			}
		});


	
		
		lblBasic = new JLabel("Basic");
		lblBasic.setBounds(79, 111, 171, 20);
		getContentPane().add(lblBasic);

		basic = new JDoubleField();
		basic.setHorizontalAlignment(SwingConstants.RIGHT);
		basic.setMaxLength(10); //Set maximum length             
		basic.setPrecision(2); //Set precision (1 in your case)              
		basic.setAllowNegative(false); //Set false to disable negatives
		basic.setBounds(193, 112, 86, 22);
		getContentPane().add(basic);


		lblDa = new JLabel("DA");
		lblDa.setBounds(79, 139, 171, 20);
		getContentPane().add(lblDa);

		da = new JDoubleField();
		da.setHorizontalAlignment(SwingConstants.RIGHT);
		da.setMaxLength(10); //Set maximum length             
		da.setPrecision(2); //Set precision (1 in your case)              
		da.setAllowNegative(false); //Set false to disable negatives
		da.setBounds(193, 140, 86, 22);
		getContentPane().add(da);


		lblHra = new JLabel("HRA");
		lblHra.setBounds(79, 167, 171, 20);
		getContentPane().add(lblHra);

		hra = new JDoubleField();
		hra.setHorizontalAlignment(SwingConstants.RIGHT);
		hra.setMaxLength(10); //Set maximum length             
		hra.setPrecision(2); //Set precision (1 in your case)              
		hra.setAllowNegative(false); //Set false to disable negatives
		hra.setBounds(193, 168,86, 22);
		getContentPane().add(hra);


		lblAdd_hra = new JLabel("Add. HRA");
		lblAdd_hra.setBounds(79, 195, 171, 20);
		getContentPane().add(lblAdd_hra);

		add_hra = new JDoubleField();
		add_hra.setHorizontalAlignment(SwingConstants.RIGHT);
		add_hra.setMaxLength(10); //Set maximum length             
		add_hra.setPrecision(2); //Set precision (1 in your case)              
		add_hra.setAllowNegative(false); //Set false to disable negatives
		add_hra.setBounds(193, 196, 86, 22);
		getContentPane().add(add_hra);

		
		lblIncentive = new JLabel("Incentive");
		lblIncentive.setBounds(79, 223, 171, 20);
		getContentPane().add(lblIncentive);

		incentive = new JDoubleField();
		incentive.setHorizontalAlignment(SwingConstants.RIGHT);
		incentive.setMaxLength(10); //Set maximum length             
		incentive.setPrecision(2); //Set precision (1 in your case)              
		incentive.setAllowNegative(false); //Set false to disable negatives
		incentive.setBounds(193, 224, 86, 22);
		getContentPane().add(incentive);
		
		
		lblSpl_incentive = new JLabel("Spl. Incentive");
		lblSpl_incentive.setBounds(79, 251, 89, 20);
		getContentPane().add(lblSpl_incentive);

		spl_incentive = new JDoubleField();
		spl_incentive.setHorizontalAlignment(SwingConstants.RIGHT);
		spl_incentive.setMaxLength(10); //Set maximum length             
		spl_incentive.setPrecision(2); //Set precision (1 in your case)              
		spl_incentive.setAllowNegative(false); //Set false to disable negatives
		spl_incentive.setBounds(193, 252, 86, 22);
		getContentPane().add(spl_incentive);
		

		lblFood_allowance = new JLabel("Food Allowance");
		lblFood_allowance.setBounds(79, 279, 114, 20);
		getContentPane().add(lblFood_allowance);

		food_allownace = new JDoubleField();
		food_allownace.setHorizontalAlignment(SwingConstants.RIGHT);
		food_allownace.setMaxLength(10); //Set maximum length             
		food_allownace.setPrecision(2); //Set precision (1 in your case)              
		food_allownace.setAllowNegative(false); //Set false to disable negatives
		food_allownace.setBounds(193, 280, 86, 22);
		getContentPane().add(food_allownace);

		
		lblLta = new JLabel("LTA");
		lblLta.setBounds(337, 111, 89, 20);
		getContentPane().add(lblLta);

		lta = new JDoubleField();
		lta.setHorizontalAlignment(SwingConstants.RIGHT);
		lta.setMaxLength(10); //Set maximum length             
		lta.setPrecision(2); //Set precision (1 in your case)              
		lta.setAllowNegative(false); //Set false to disable negatives
		lta.setBounds(434, 112, 86, 22);
		getContentPane().add(lta);


		lblMedical = new JLabel("Medical");
		lblMedical.setBounds(337, 139, 89, 20);
		getContentPane().add(lblMedical);

		medical = new JDoubleField();
		medical.setHorizontalAlignment(SwingConstants.RIGHT);
		medical.setMaxLength(10); //Set maximum length             
		medical.setPrecision(2); //Set precision (1 in your case)              
		medical.setAllowNegative(false); //Set false to disable negatives
		medical.setBounds(434, 140, 86, 22);
		getContentPane().add(medical);

		
		lblBonus = new JLabel("Bonus");
		lblBonus.setBounds(337, 167, 89, 20);
		getContentPane().add(lblBonus);

		bonus = new JDoubleField();
		bonus.setHorizontalAlignment(SwingConstants.RIGHT);
		bonus.setMaxLength(10); //Set maximum length             
		bonus.setPrecision(2); //Set precision (1 in your case)              
		bonus.setAllowNegative(false); //Set false to disable negatives
		bonus.setBounds(434, 168, 86, 22);
		getContentPane().add(bonus);
		
		lblGross = new JLabel("Gross");
		lblGross.setBounds(337, 195, 89, 20);
		getContentPane().add(lblGross);

		gross = new JDoubleField();
		gross.setHorizontalAlignment(SwingConstants.RIGHT);
		gross.setMaxLength(10); //Set maximum length             
		gross.setPrecision(2); //Set precision (1 in your case)              
		gross.setAllowNegative(false); //Set false to disable negatives
		gross.setBounds(434, 196, 86, 22);
		gross.setEditable(false);
		getContentPane().add(gross);

		
		lblOtRate = new JLabel("OT Rate");
		lblOtRate.setBounds(337, 223, 89, 20);
		getContentPane().add(lblOtRate);

		ot_rate = new JDoubleField();
		ot_rate.setHorizontalAlignment(SwingConstants.RIGHT);
		ot_rate.setMaxLength(10); //Set maximum length             
		ot_rate.setPrecision(2); //Set precision (1 in your case)              
		ot_rate.setAllowNegative(false); //Set false to disable negatives
		ot_rate.setBounds(434, 224, 86, 22);
		getContentPane().add(ot_rate);
		
		lblSt_allow = new JLabel("Sterlite Rate");
		lblSt_allow.setBounds(337, 251, 89, 20);
		getContentPane().add(lblSt_allow);

		st_allow = new JDoubleField();
		st_allow.setHorizontalAlignment(SwingConstants.RIGHT);
		st_allow.setMaxLength(10); //Set maximum length             
		st_allow.setPrecision(2); //Set precision (1 in your case)              
		st_allow.setAllowNegative(false); //Set false to disable negatives
		st_allow.setBounds(434, 252, 86, 22);
		getContentPane().add(st_allow);

		
		
		KeyListener keyListener = new KeyListener() 
		{
			public void keyPressed(KeyEvent keyEvent) 
			{
				int key = keyEvent.getKeyCode();
				if (key == KeyEvent.VK_ENTER) 
				{
					JTextField textField = (JTextField) keyEvent.getSource();
					int id = Integer.parseInt(textField.getName());
					
					switch (id) 
					{
					case 19:
						calfunction(basic);
						da.requestFocus();
						da.setSelectionStart(0);
						break;
					case 20:
						calfunction(da);
						hra.requestFocus();
						hra.setSelectionStart(0);
						break;
					case 21:
						calfunction(hra);
						add_hra.requestFocus();
						add_hra.setSelectionStart(0);
						break;
					case 22:
						calfunction(add_hra);
						incentive.requestFocus();
						incentive.setSelectionStart(0);
						break;
					case 23:
						calfunction(incentive);
						spl_incentive.requestFocus();
						spl_incentive.setSelectionStart(0);
						break;
					case 24:
						calfunction(spl_incentive);
						food_allownace.requestFocus();
						food_allownace.setSelectionStart(0);
						break;
					case 25:
						calfunction(food_allownace);
						lta.requestFocus();
						lta.setSelectionStart(0);
						break;
					case 26:
						calfunction(lta);
						medical.requestFocus();
						medical.setSelectionStart(0);
						break;
					case 27:
						bonus.requestFocus();
						bonus.setSelectionStart(0);
						break;
					case 28:
						gross.requestFocus();
						gross.setSelectionStart(0);
						break;
					case 29:
						ot_rate.requestFocus();
						ot_rate.setSelectionStart(0);
						break;
					case 30:
						st_allow.requestFocus();
						st_allow.setSelectionStart(0);
						break;
					case 31:
						changetButton.requestFocus();
						changetButton.setBackground(new Color(139, 153, 122));
						break;
					}
				}

				if (key == KeyEvent.VK_ESCAPE) 
				{
					resetAll();
					dispose();
				}
			}

			public void keyReleased(KeyEvent keyEvent) {
			}

			public void keyTyped(KeyEvent keyEvent) {
			}
		};

		// ///////////////////////////////////////////////		
		
		basic.setName("19");
		da.setName("20");
		hra.setName("21");
		add_hra.setName("22");
		incentive.setName("23");
		spl_incentive.setName("24");
		food_allownace.setName("25");
		lta.setName("25");
		medical.setName("26");
		bonus.setName("27");
		gross.setName("28");
		ot_rate.setName("29");
		st_allow.setName("30");
		
		
		gross.addKeyListener(keyListener);
		basic.addKeyListener(keyListener);
		da.addKeyListener(keyListener);
		hra.addKeyListener(keyListener);
		add_hra.addKeyListener(keyListener);
		incentive.addKeyListener(keyListener);
		spl_incentive.addKeyListener(keyListener);
		food_allownace.addKeyListener(keyListener);
		lta.addKeyListener(keyListener);
		medical.addKeyListener(keyListener);
		bonus.addKeyListener(keyListener);
		ot_rate.addKeyListener(keyListener);
		st_allow.addKeyListener(keyListener);		
		
		
		gross.addFocusListener(myFocusListener);
		basic.addFocusListener(myFocusListener);
		da.addFocusListener(myFocusListener);
		hra.addFocusListener(myFocusListener);
		add_hra.addFocusListener(myFocusListener);
		incentive.addFocusListener(myFocusListener);
		spl_incentive.addFocusListener(myFocusListener);
		food_allownace.addFocusListener(myFocusListener);
		lta.addFocusListener(myFocusListener);
		medical.addFocusListener(myFocusListener);
		bonus.addFocusListener(myFocusListener);
		ot_rate.addFocusListener(myFocusListener);
		st_allow.addFocusListener(myFocusListener);
		
		exitButton = new JButton("Exit");
		exitButton.setBounds(305, 319, 100, 30);
		getContentPane().add(exitButton);


		changetButton = new JButton("Change");
		changetButton.setBounds(185, 319, 100, 30);
		getContentPane().add(changetButton);
		changetButton.addKeyListener(new KeyAdapter() 
		{
			public void keyPressed(KeyEvent evt) 
			{
				if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) 
				{
					resetAll();
					dispose();
				}
				
				if (evt.getKeyCode() == KeyEvent.VK_ENTER) 
				{
			    	 setDataForDAO();
				}
			}
		});
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(10, 22, 560, 341);
		getContentPane().add(panel_1);

		exitButton.addActionListener(this);
	    changetButton.addActionListener(this);
		
	    
	    
		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) 
			{
				passwordF.requestFocus();
				passwordF.setSelectionStart(0);
			}
			
			public void windowClosed(WindowEvent e) 
			{
				resetAll();
			}
		});

		setAlwaysOnTop(true);
		
	}
	
	
	 

	 public void resetAll()
	 {
			gross.setText("");
			basic.setText("");
			da.setText("");
			hra.setText("");
			add_hra.setText("");
			incentive.setText("");
			spl_incentive.setText("");
			food_allownace.setText("");
			lta.setText("");
			medical.setText("");
			bonus.setText("");
			ot_rate.setText("");
			st_allow.setText("");
			passwordF.setText("");
		 
	 }		 
	 

	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equalsIgnoreCase("Exit"))
		{
			dispose();
			
		}
		 
	    if(e.getActionCommand().equalsIgnoreCase("Change") )
	    {
	    	 setDataForDAO();
		}

		
	}

	
	public void setDataForDAO()
	{
		EmployeeMastDto empDto = null;
		try
		{
			empDto = new EmployeeMastDto();
			empDto.setCmp_code(loginDt.getCmp_code());
			empDto.setDepo_code(loginDt.getDepo_code());
			empDto.setGross(setDoubleNumber(gross.getText().trim()));
			empDto.setBasic(setDoubleNumber(basic.getText().trim()));
			empDto.setDa(setDoubleNumber(da.getText().trim()));
			empDto.setHra(setDoubleNumber(hra.getText().trim()));
			empDto.setAdd_hra(setDoubleNumber(add_hra.getText().trim()));
			empDto.setIncentive(setDoubleNumber(incentive.getText().trim()));
			empDto.setSpl_incentive(setDoubleNumber(spl_incentive.getText().trim()));
			empDto.setFood_allowance(setDoubleNumber(food_allownace.getText().trim()));
			empDto.setLta(setDoubleNumber(lta.getText().trim()));
			empDto.setMedical(setDoubleNumber(medical.getText().trim()));
			empDto.setBonus(setDoubleNumber(bonus.getText().trim()));
			empDto.setOt_rate(setDoubleNumber(ot_rate.getText().trim()));
			empDto.setStair_alw(setDoubleNumber(st_allow.getText().trim()));
			int j= empDao.updateSalaryStructure(empDto);
			if(j>0)
				alertMessage(SalaryStructure.this, "Salary Updated Sucessfully!!!");
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		 

	}

	public void fillData(EmployeeMastDto empDto)
	{
		gross.setText(formatter.format(empDto.getGross()));
		basic.setText(formatter.format(empDto.getBasic()));
		da.setText(formatter.format(empDto.getDa()));
		hra.setText(formatter.format(empDto.getHra()));
		add_hra.setText(formatter.format(empDto.getAdd_hra()));
		incentive.setText(formatter.format(empDto.getIncentive()));
		spl_incentive.setText(formatter.format(empDto.getSpl_incentive()));
		food_allownace.setText(formatter.format(empDto.getFood_allowance()));
		lta.setText(formatter.format(empDto.getLta()));
		medical.setText(formatter.format(empDto.getMedical()));
		bonus.setText(formatter.format(empDto.getBonus()));
		ot_rate.setText(formatter.format(empDto.getOt_rate()));
		st_allow.setText(formatter.format(empDto.getStair_alw()));
		
	}
	

	public void setEditable(boolean b)
	{
		
		gross.setEnabled(b);
		basic.setEnabled(b);
		da.setEnabled(b);
		hra.setEnabled(b);
		add_hra.setEnabled(b);
		incentive.setEnabled(b);
		spl_incentive.setEnabled(b);
		food_allownace.setEnabled(b);
		lta.setEnabled(b);
		medical.setEnabled(b);
		bonus.setEnabled(b);
		ot_rate.setEnabled(b);
		st_allow.setEnabled(b);
		changetButton.setEnabled(b);
	}

	public void calfunction(JDoubleField dval)
	{
		dval.setText(formatter.format(setDoubleNumber(dval.getText())));
		double grossval=setDoubleNumber(basic.getText())+setDoubleNumber(da.getText())+setDoubleNumber(hra.getText())+setDoubleNumber(add_hra.getText())+setDoubleNumber(incentive.getText())+setDoubleNumber(spl_incentive.getText())+setDoubleNumber(food_allownace.getText())+setDoubleNumber(lta.getText());
		gross.setText(formatter.format(grossval));
		
	}	
	
	public void checkPassword()
	{
		String password = passwordF.getText().toString().trim();
		if(password.equals("Prompt"))
		{
			setEditable(true);
			basic.requestFocus();
			basic.setSelectionStart(0);
		}
		else
		{
			alertMessage(SalaryStructure.this, "Please Enter Correct Password");
			passwordF.setText("");
			passwordF.requestFocus();
		}
	}
	
	
	public void setVisible(boolean b)
	{
		EmployeeMastDto edto = empDao.getSalaryStructure(loginDt.getDepo_code(), loginDt.getCmp_code()); 
		if(edto!=null)
			fillData(edto);
		
		setEditable(false);
		
		super.setVisible(b);
		
	}
	
	public void dispose()
	{
		super.dispose();
	}
	

	 
	 
}


