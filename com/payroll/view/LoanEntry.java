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
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import com.payroll.dao.EmployeeDAO;
import com.payroll.dto.EmployeeMastDto;
import com.payroll.dto.EmptranDto;
import com.payroll.dto.MonthDto;
import com.payroll.util.JDoubleField;

public class LoanEntry extends BaseClass implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	private JLabel reportName ;
	private JButton exitButton,changetButton;
	String ClassNm,repNm;
	 
	
	
	 
	private JLabel lblempcode,lblloan,lblinstallment;	
	private JTextField emp_code,emp_name;
	private JDoubleField loan_amt,inst_amt;
	private JLabel sdateLeb;
	private JComboBox smonth;
	MonthDto mdto=null;
	private JList empList;
	private JScrollPane Emppane;
	EmployeeMastDto empDto;
	private EmployeeDAO empDao; 
	private boolean fill=false;
	
	public  LoanEntry(String nm,String repNm)
	{
		 
		ClassNm=nm;
		this.repNm=repNm;
		
		//setUndecorated(true);
		setResizable(false);
		setSize(586, 374);	
		setLocationRelativeTo(null);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		getContentPane().setLayout(null);
		///////////////////////////////////////////////////////////
		empDao = new EmployeeDAO();
		
		
		empList = new JList(loginDt.getEmpList());
		//partyList.setFont(font);
		empList.setSelectedIndex(0);
		empList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Emppane = new JScrollPane(empList,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		Emppane.setBounds(305, 138, 260, 185);
		getContentPane().add(Emppane);
		Emppane.setVisible(false);
		
		reportName = new JLabel(repNm);
		reportName.setHorizontalAlignment(SwingConstants.CENTER);
		reportName.setFont(new Font("Tahoma", Font.BOLD, 14));
		reportName.setBounds(125, 46, 303, 20);
		getContentPane().add(reportName);

		
		empList.addKeyListener(new KeyAdapter() 
		{
			public void keyPressed(KeyEvent evt) 
			{
				if (evt.getKeyCode() == KeyEvent.VK_ENTER) 
				{
					
					empDto = (EmployeeMastDto) empList.getSelectedValue();;
					
					emp_code.setText(String.valueOf(empDto.getEmp_code()));
					emp_name.setText(empDto.getEmp_name());
					fillData();
					loan_amt.requestFocus();
					loan_amt.selectAll();
					// evt.consume();
					Emppane.setVisible(false);
					 
				}
				
				if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) 
				{
					Emppane.setVisible(false);
					empList.setVisible(false);
					emp_code.requestFocus();
					emp_code.setText("");
					emp_name.setText("");
				}

			}
		});
		
		sdateLeb = new JLabel("Select Month:");
		sdateLeb.setBounds(71,80, 110, 20);
		getContentPane().add(sdateLeb);

		smonth = new JComboBox(loginDt.getFmonth()); 
		smonth.setBounds(217, 81, 159, 23);
		getContentPane().add(smonth);
		smonth.setActionCommand("month");
		smonth.setMaximumRowCount(12);

		smonth.setSelectedIndex(loginDt.getMno());
		smonth.addKeyListener(new KeyAdapter() 
		{
			public void keyPressed(KeyEvent evt) 
			{
				if (evt.getKeyCode() == KeyEvent.VK_ENTER) 
				{
					
					emp_code.requestFocus();
					emp_code.selectAll();
					// evt.consume();
					 
				}
			}
		});

		 
		lblempcode = new JLabel("Employee Code:");
		lblempcode.setBounds(71, 114, 126, 20);
		getContentPane().add(lblempcode);

		emp_code = new JTextField();
		emp_code.setBounds(217, 114, 79, 23);
		getContentPane().add(emp_code);
		emp_code.addKeyListener(new KeyAdapter() 
		{
			public void keyPressed(KeyEvent evt) 
			{
				if (evt.getKeyCode() == KeyEvent.VK_ENTER) 
				{
					
					Emppane.setVisible(true);
					empList.setVisible(true);
					empList.requestFocus();
					empList.setSelectedIndex(0);
					
					// evt.consume();
					 
				}
			}
		});
		
		emp_name = new JTextField();
		emp_name.setBounds(305, 114, 177, 23);
		getContentPane().add(emp_name);
		
		
		lblloan = new JLabel("Loan Amount :");
		lblloan.setBounds(71, 147, 126, 20);
		getContentPane().add(lblloan);
		
		loan_amt = new JDoubleField();
		loan_amt.setHorizontalAlignment(SwingConstants.RIGHT);
		loan_amt.setMaxLength(10); //Set maximum length             
		loan_amt.setPrecision(2); //Set precision (1 in your case)              
		loan_amt.setAllowNegative(false); //Set false to disable negatives
		loan_amt.setBounds(217, 147, 100, 23);
		getContentPane().add(loan_amt);

		lblinstallment = new JLabel("Installment  Amount :");
		lblinstallment.setBounds(71, 180, 145, 20);
		getContentPane().add(lblinstallment);
		
		inst_amt = new JDoubleField();
		inst_amt.setHorizontalAlignment(SwingConstants.RIGHT);
		inst_amt.setMaxLength(10); //Set maximum length             
		inst_amt.setPrecision(2); //Set precision (1 in your case)              
		inst_amt.setAllowNegative(false); //Set false to disable negatives
		inst_amt.setBounds(217, 180, 100, 23);
		getContentPane().add(inst_amt);

		
		exitButton = new JButton("Exit");
		exitButton.setBounds(305, 285, 100, 30);
		getContentPane().add(exitButton);
		exitButton.addKeyListener(new KeyAdapter() 
		{
			public void keyPressed(KeyEvent evt) 
			{
				if (evt.getKeyCode() == KeyEvent.VK_ENTER) 
				{
					resetAll();
					dispose();
				}
			 
			}
		});


		changetButton = new JButton("Save");
		changetButton.setBounds(185, 285, 100, 30);
		changetButton.setEnabled(false);
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
		panel_1.setBounds(10, 22, 560, 312);
		getContentPane().add(panel_1);

		exitButton.addActionListener(this);
	    changetButton.addActionListener(this);
		
	    
	    
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
					case 0:
						emp_code.requestFocus();
						emp_code.setSelectionStart(0);
						break;
					case 1:
						 
						break;
					case 2:
						loan_amt.requestFocus();
						loan_amt.setSelectionStart(0);
						break;
					case 3:
						loan_amt.setText(formatter.format(setDoubleNumber(loan_amt.getText())));
						if(setDoubleNumber(loan_amt.getText())>0)
						{
							inst_amt.requestFocus();
							inst_amt.setSelectionStart(0);
						}
						else
						{
							loan_amt.requestFocus();
							loan_amt.setSelectionStart(0);
						}
						break;
					case 4:
						inst_amt.setText(formatter.format(setDoubleNumber(inst_amt.getText())));
						if(setDoubleNumber(inst_amt.getText())>0 )
						{
							if(fill)
							{
								exitButton.requestFocus();
								exitButton.setBackground(new Color(139, 153, 122));
							}
							else
							{
								changetButton.setEnabled(true);
								changetButton.requestFocus();
								changetButton.setBackground(new Color(139, 153, 122));
							}
							
						}
						else
						{
							inst_amt.requestFocus();
							inst_amt.setSelectionStart(0);
						}
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
	    
	    smonth.setName("0");
	    emp_code.setName("1");
		emp_name.setName("2");
		loan_amt.setName("3");
		inst_amt.setName("4");
		
		emp_code.addKeyListener(keyListener);
		emp_name.addKeyListener(keyListener);
		loan_amt.addKeyListener(keyListener);
		inst_amt.addKeyListener(keyListener);
		

	    
	    
		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) 
			{
				 
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
		 smonth.setSelectedIndex(smonth.getSelectedIndex());
		 emp_code.setText("");
		 emp_name.setText("");
		 loan_amt.setText("");
		 inst_amt.setText("");
		 empList.setVisible(false);
		 changetButton.setEnabled(false);

	 }		 
	 

	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equalsIgnoreCase("Exit"))
		{
			dispose();
			
		}
		 
	  

	    if(e.getActionCommand().equalsIgnoreCase("Save"))
		{
	    	setDataForDAO();
			 
		}	
	    
		
	}

	
	public void setDataForDAO()
	{
		EmptranDto empDto = null;
		try
		{
			mdto = (MonthDto) smonth.getSelectedItem();
			empDto = new EmptranDto();
			empDto.setCmp_code(loginDt.getCmp_code());
			empDto.setDepo_code(loginDt.getDepo_code());
			empDto.setEmp_code(setIntNumber(emp_code.getText().trim()));
			empDto.setLoan(setDoubleNumber(loan_amt.getText().trim()));
			empDto.setInstamt(setDoubleNumber(inst_amt.getText().trim()));
			empDto.setFin_year(loginDt.getFin_year());
			empDto.setMnth_code(mdto.getMnthcode());
			
			int j= empDao.insertLoanEntry(empDto);
			if(j>0)
			{
				alertMessage(LoanEntry.this, "Data Saved Sucessfully!!!");
				resetAll();
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	
	
	public void fillData()
	{
		mdto = (MonthDto) smonth.getSelectedItem();
		EmptranDto edto = (EmptranDto) empDao.getLoanEntry(loginDt.getFin_year(),loginDt.getDepo_code(),loginDt.getCmp_code(),setIntNumber(emp_code.getText().trim()),mdto.getMnthcode());
		loan_amt.setText(formatter.format(edto.getLoan()));
		inst_amt.setText(formatter.format(edto.getInstamt()));
		changetButton.setEnabled(false);
		if(edto.getLoan()>0)
		{
			loan_amt.setEditable(false);
			inst_amt.setEditable(false);
			fill=true;
		}
		else
		{
			loan_amt.setEditable(true);
			inst_amt.setEditable(true);
			fill=false;
		}
		
	}
	
	
	public ArrayList Update() 
	{
		  
		Vector col = null;
		EmptranDto emp = null;
		ArrayList attnList = new ArrayList();
		
		try {

				   emp.setDepo_code(loginDt.getDepo_code());
				   emp.setCmp_code(loginDt.getCmp_code());
				   emp.setMnth_code(mdto.getMnthcode());
				   attnList.add(emp);
				  
		} 
		catch (Exception e) 
		{
			System.out.println(e);
		}
		
		return attnList;		
		
		     
	}


	 
	
	
	
	public void setVisible(boolean b)
	{
		super.setVisible(b);
		
	}
	
	public void dispose()
	{
		super.dispose();
	}
	

	 
	 
}


