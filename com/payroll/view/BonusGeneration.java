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
 

public class BonusGeneration extends BaseClass implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	private JLabel reportName ;
	private JLabel sdateLeb,lblMonth;
	private JButton exitButton,changetButton;
 
	private JComboBox smonth,fyear;
	String ClassNm,repNm;
	PreparedStatement curr=null;
	ResultSet rcurr=null;
	private JLabel lblPassword;	
	private JPasswordField passwordF;

	YearDto yd;
	 
	 
	public  BonusGeneration(String nm,String repNm)
	{
		 
		ClassNm=nm;
		this.repNm=repNm;
		
		//setUndecorated(true);
		setResizable(false);
		setSize(413, 269);	
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
		sdateLeb.setBounds(72, 108, 110, 20);
		getContentPane().add(sdateLeb);

		fyear = new JComboBox(loginDt.getFyear());
		fyear.setBounds(176, 105, 159, 23);
		getContentPane().add(fyear);
		fyear.setActionCommand("fyear");
		fyear.setMaximumRowCount(12);

		lblMonth = new JLabel("Select Month:");
		lblMonth.setBounds(72, 136, 110, 20);
		getContentPane().add(lblMonth);

		smonth = new JComboBox(new Vector(loginDt.getFmonth()));
		smonth.setBounds(176, 134, 159, 23);
		getContentPane().add(smonth);
		smonth.setActionCommand("month");
		smonth.setMaximumRowCount(12);
		
		lblPassword = new JLabel("Password:");
		lblPassword.setForeground(Color.BLUE);
		lblPassword.setBounds(72, 75, 70, 20);
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

		
		
		exitButton = new JButton("Exit");
		exitButton.setBounds(215, 175, 100, 30);
		getContentPane().add(exitButton);


		changetButton = new JButton("Submit");
		changetButton.setBounds(95, 175, 100, 30);
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
		panel_1.setBounds(10, 21, 387, 205);
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
		}
		else
		{
			alertMessage(BonusGeneration.this, "Please Enter Correct Password");
			passwordF.setText("");
			passwordF.requestFocus();
		}
	}
	
	public void setEditable(boolean b)
	{
		smonth.setEnabled(b);
		fyear.setEnabled(b);
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
				Vector v = pdao.getBonusList(loginDt.getDepo_code(), loginDt.getCmp_code(), yd.getYearcode(), sdt.getMnthcode(), 1);
				if(v==null)
				{
					i = pdao.bonusGeneration(yd.getYearcode(), loginDt.getDepo_code(), loginDt.getCmp_code(), sdt.getMnthcode(),0.00,0.00);
					
					if(i>0)
					{
						alertMessage(BonusGeneration.this, "Bonus Generation Sucessfully!!!!");
						resetAll();
						dispose();
					}
					else
					{
						alertMessage(BonusGeneration.this, "No Records for Bonus Generation" );
					}
				}
				else
				{
					int ans=confirmationDialongYes("Bonus");
					if(ans==1)
					{
						i = pdao.bonusGeneration(yd.getYearcode(), loginDt.getDepo_code(), loginDt.getCmp_code(), sdt.getMnthcode(),0.00,0.00);

						if(i>0)
						{
							alertMessage(BonusGeneration.this, "Bonus Generation Sucessfully!!!!");
							resetAll();
							dispose();
						}
						else
						{
							alertMessage(BonusGeneration.this, "No Records for Bonus Generation" );
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
			alertMessage(BonusGeneration.this, "Please Enter Correct Password");
		}
	}
	
	 

	 public void resetAll()
	 {
		 smonth.setSelectedIndex(loginDt.getMno());
		 fyear.setSelectedIndex(0);
		 passwordF.setText("");
	 }		 
	 

	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equalsIgnoreCase("Exit"))
		{
			resetAll();
			dispose();
			
		}
		 
	    if(e.getActionCommand().equalsIgnoreCase("Submit") )
	    {
	    	checkPassword();
		}

	    if(e.getActionCommand().equalsIgnoreCase("fyear"))
	    {
		    yd = (YearDto) fyear.getSelectedItem();
			System.out.println(yd.getYearcode());
			
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


