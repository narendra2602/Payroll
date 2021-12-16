package com.payroll.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import com.payroll.dao.PayrollDAO;
import com.payroll.dto.MonthDto;
import com.payroll.dto.YearDto;
import com.payroll.util.JDoubleField;

public class LTAGeneration extends BaseClass implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	private JLabel reportName ;
	private JLabel sdateLeb,lblMonth;
	private JButton exitButton,changetButton;
 
	private JComboBox smonth,fyear;
	String ClassNm,repNm;
	PreparedStatement curr=null;
	ResultSet rcurr=null;
	private JDoubleField medical,lta;
	private JLabel lblEndingNo,lblBonusper;
	private JLabel lblPassword;	
	private JPasswordField passwordF;

	YearDto yd;
	 
	public  LTAGeneration(String nm,String repNm)
	{
		 
		ClassNm=nm;
		this.repNm=repNm;
		
		//setUndecorated(true);
		setResizable(false);
		setSize(413, 306);	
		setLocationRelativeTo(null);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		getContentPane().setLayout(null);
		///////////////////////////////////////////////////////////

		reportName = new JLabel(repNm);
		reportName.setHorizontalAlignment(SwingConstants.CENTER);
		reportName.setFont(new Font("Tahoma", Font.BOLD, 14));
		reportName.setBounds(49, 46, 303, 20);
		getContentPane().add(reportName);

		sdateLeb = new JLabel("Select Year:");
		sdateLeb.setBounds(61, 108, 110, 20);
		getContentPane().add(sdateLeb);

		fyear = new JComboBox(loginDt.getFyear());
		fyear.setBounds(176, 105, 159, 23);
		getContentPane().add(fyear);
		fyear.setActionCommand("fyear");
		fyear.setMaximumRowCount(12);

		lblMonth = new JLabel("Select Month:");
		lblMonth.setBounds(61, 136, 110, 20);
		getContentPane().add(lblMonth);

		smonth = new JComboBox(new Vector(loginDt.getFmonth()));
		smonth.setBounds(176, 134, 159, 23);
		getContentPane().add(smonth);
		smonth.setActionCommand("month");
		smonth.setMaximumRowCount(12);
		
		lblPassword = new JLabel("Password:");
		lblPassword.setForeground(Color.BLUE);
		lblPassword.setBounds(61, 75, 70, 20);
		getContentPane().add(lblPassword);

		passwordF = new JPasswordField();
		passwordF.setBounds(176, 75, 159, 25);
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
					String password = passwordF.getText().toString().trim();
					checkPassword(password);
				}
			}
		});

		
		
		///////////////////////////////////////////////////////////////////////////////////////////////

		lblBonusper = new JLabel("LTA Amount :");
		lblBonusper.setBounds(61, 166, 100, 20);
		getContentPane().add(lblBonusper);
		
		lta = new JDoubleField();
		lta.setHorizontalAlignment(SwingConstants.RIGHT);
		lta.setMaxLength(5); //Set maximum length             
		lta.setPrecision(2); //Set precision (1 in your case)              
		lta.setAllowNegative(false); //Set false to disable negatives
		lta.setBounds(176, 163, 159, 25);
		lta.setSelectionStart(0);
		getContentPane().add(lta);
		lta.addKeyListener(new KeyAdapter() 
		{
			public void keyPressed(KeyEvent evt) 
			{
				if (evt.getKeyCode() == KeyEvent.VK_ENTER) 
				{
					medical.requestFocus();
					medical.selectAll();
				}
			}
		});

		lblEndingNo = new JLabel("Medical Amount:");
		lblEndingNo.setBounds(61, 192, 111, 20);
		getContentPane().add(lblEndingNo);
 

		
		medical = new JDoubleField();
		medical.setHorizontalAlignment(SwingConstants.RIGHT);
		medical.setMaxLength(10); //Set maximum length             
		medical.setPrecision(2); //Set precision (1 in your case)              
		medical.setAllowNegative(false); //Set false to disable negatives
		medical.setBounds(176, 192, 159, 25);
		medical.setSelectionStart(0);
		getContentPane().add(medical);
		medical.addKeyListener(new KeyAdapter() 
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
					changetButton.requestFocus();
				}
			}
		});

		
		
		exitButton = new JButton("Exit");
		exitButton.setBounds(215, 227, 100, 30);
		getContentPane().add(exitButton);


		changetButton = new JButton("Submit");
		changetButton.setBounds(95, 227, 100, 30);
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
					checkPassword();
				}
			}
		});
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(10, 21, 387, 245);
		getContentPane().add(panel_1);

		exitButton.addActionListener(this);
	    changetButton.addActionListener(this);
		smonth.addActionListener(this);
		fyear.addActionListener(this);
		setAlwaysOnTop(true);
		
	}
	
	
	public void checkPassword(String password)
	{
		if(password.equals("Prompt"))
		{
			setEditable(true);
			lta.requestFocus();
			lta.setSelectionStart(0);
		}
		else
		{
			alertMessage(LTAGeneration.this, "Please Enter Correct Password");
			passwordF.setText("");
			passwordF.requestFocus();
		}
	}
	
	public void setEditable(boolean b)
	{
		smonth.setEnabled(b);
		fyear.setEnabled(b);
		medical.setEnabled(b);
		lta.setEnabled(b);
		changetButton.setEnabled(b);
	}
	
	public void checkPassword()
	{
		//String password = "Prompt";
		String password = passwordF.getText().toString().trim();
		if(password.equals("Prompt"))
		{
			try {
				PayrollDAO pdao = new PayrollDAO();
				MonthDto sdt = (MonthDto) smonth.getSelectedItem();
				YearDto yd = (YearDto) fyear.getSelectedItem();
				int i=0;
				Vector v = pdao.getLTAList(loginDt.getDepo_code(), loginDt.getCmp_code(), yd.getYearcode(), sdt.getMnthcode(), 2);
				System.out.println("year "+yd.getYearcode()+" month "+sdt.getMnthcode());
				
				if(v==null)
				{
					i = pdao.ltaGeneration(yd.getYearcode(), loginDt.getDepo_code(), loginDt.getCmp_code(), sdt.getMnthcode(),setDoubleNumber(lta.getText()),setDoubleNumber(medical.getText()));
					
					if(i>0)
					{
						alertMessage(LTAGeneration.this, "LTA/Medical Generation Sucessfully!!!!");
						resetAll();
						dispose();
					}
					else
					{
						alertMessage(LTAGeneration.this, "No Records for LTA/Medical Generation" );
					}
				}
				else
				{
					
					int ans=confirmationDialongYes("LTA/Medical");
					if(ans==1)
					{
						Vector c =(Vector<?>) v.get(0);
						int mncode=setIntNumber(c.get(7).toString());
						
						System.out.println("mncode is "+mncode);
						
						if(mncode==sdt.getMnthcode())
						{
							i = pdao.ltaGeneration(yd.getYearcode(), loginDt.getDepo_code(), loginDt.getCmp_code(), sdt.getMnthcode(),setDoubleNumber(lta.getText()),setDoubleNumber(medical.getText()));

							if(i>0)
							{
								alertMessage(LTAGeneration.this, "LTA/Medical Generation Sucessfully!!!!");
								resetAll();
								dispose();
							}
							else
							{
								alertMessage(LTAGeneration.this, "No Records for LTA/Medical Generation" );
							}
						}
						else
						{
							alertMessage(LTAGeneration.this, "LTA/Medical Already Generated in Month "+getMonthName(mncode));
						}
					}
				}
			 

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		else
		{
			alertMessage(LTAGeneration.this, "Please Enter Correct Password");
		}
	}
	
	 

	 public void resetAll()
	 {
		 smonth.setSelectedIndex(loginDt.getMno());
		 fyear.setSelectedIndex(0);
		 medical.setText("0.00");
		 lta.setText("0.00");
		 passwordF.setText("");
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
			
			YearDto yd = (YearDto) fyear.getSelectedItem();
			Vector v = (Vector) loginDt.getFmon().get(yd.getYearcode());
			System.out.println(v.size());
			
			smonth.removeAllItems();
			MonthDto mn=null;
			for (int i=0; i<v.size();i++)
			{
				 mn = (MonthDto) v.get(i);
				 smonth.addItem(mn);
				 
			}
		}

		 
	    if(e.getActionCommand().equalsIgnoreCase("Submit") )
	    {
	    	checkPassword();
		}

		
	}
 
	public void setVisible(boolean b)
	{
		setEditable(false);
		super.setVisible(b);
	}
	
	public void dispose()
	{
		 smonth.setSelectedIndex(loginDt.getMno());
		 fyear.setSelectedIndex(0);
		 super.dispose();
	}
	

	 
}


