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

public class Regeneration extends BaseClass implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	private JLabel reportName ;
	private JLabel sdateLeb;
	private JButton exitButton,changetButton;
 
	private JComboBox smonth;
	String ClassNm,repNm;
	PreparedStatement curr=null;
	ResultSet rcurr=null;
	private JPasswordField passwordF;
	private JLabel lblEndingNo;	
	YearDto yd;
	 
	public  Regeneration(String nm,String repNm)
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

		sdateLeb = new JLabel("Select Month:");
		sdateLeb.setBounds(72, 114, 110, 20);
		getContentPane().add(sdateLeb);

		smonth = new JComboBox(loginDt.getFmonth());
		smonth.setBounds(176, 111, 159, 23);
		getContentPane().add(smonth);
		smonth.setActionCommand("month");
		smonth.setMaximumRowCount(12);

		
		Vector<?> v = (Vector<?>) loginDt.getFmon().get(loginDt.getFin_year());
		smonth.removeAllItems();
		MonthDto mn=null;
		for (int i=0; i<v.size();i++)
		{
			 mn = (MonthDto) v.get(i);
			 smonth.addItem(mn);
			 
		}
		mn =(MonthDto) smonth.getSelectedItem();
		
		smonth.setSelectedIndex(loginDt.getMno());
		
		
		///////////////////////////////////////////////////////////////////////////////////////////////
		
		lblEndingNo = new JLabel("Password:");
		lblEndingNo.setBounds(75, 142, 70, 20);
		getContentPane().add(lblEndingNo);

		passwordF = new JPasswordField();
		passwordF.setBounds(176, 142, 159, 25);
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
					changetButton.requestFocus();
				}
			}
		});

		
		
		exitButton = new JButton("Exit");
		exitButton.setBounds(215, 177, 100, 30);
		getContentPane().add(exitButton);


		changetButton = new JButton("Regenerate");
		changetButton.setBounds(95, 177, 100, 30);
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
		panel_1.setBounds(10, 22, 366, 208);
		getContentPane().add(panel_1);

		exitButton.addActionListener(this);
	    changetButton.addActionListener(this);
		smonth.addActionListener(this);
		
		setAlwaysOnTop(true);
		
	}
	
	public void checkPassword()
	{
		String password = passwordF.getText().toString().trim();
		if(password.equals("Prompt"))
		{
			try {
				PayrollDAO pdao = new PayrollDAO();
				MonthDto sdt = (MonthDto) smonth.getSelectedItem();
				int i=0;
				if(repNm.contains("Regeneration"))
				{
					i = pdao.salaryRegeneration(loginDt.getFin_year(), loginDt.getDepo_code(), loginDt.getCmp_code(), sdt.getMnthcode());
					if(i>0)
					{
						alertMessage(Regeneration.this, "Re-generate Sucessfully!!!!");
						resetAll();
						dispose();
					}
					else
					{
						alertMessage(Regeneration.this, "No Records for Regeneration" );
					}
				}
				else if(repNm.contains("Unlock Attendance Entry"))
				{
					i = pdao.lockAttendance(loginDt.getFin_year(), loginDt.getDepo_code(), loginDt.getCmp_code(), sdt.getMnthcode(),0);
					if(i>0)
					{
						alertMessage(Regeneration.this, "Unlock Attendance Sucessfully!!!!");
						resetAll();
						dispose();
					}
					else
					{
						alertMessage(Regeneration.this, "No Records for Unlock" );
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		else
		{
			alertMessage(Regeneration.this, "Please Enter Correct Password");
			passwordF.setText("");
			passwordF.requestFocus();
			//resetAll();
		}
	}
	
	 

	 public void resetAll()
	 {
		 
		 smonth.setSelectedIndex(smonth.getSelectedIndex());
		 passwordF.setText("");
	 }		 
	 

	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equalsIgnoreCase("Exit"))
		{
			dispose();
			
		}
		 
	    if(e.getActionCommand().equalsIgnoreCase("Regeneration") || e.getActionCommand().equalsIgnoreCase("Unlock") )
	    {
	    	checkPassword();
		}

		
	}
 
	
	
	public void dispose()
	{
		smonth.setSelectedIndex(loginDt.getMno());
		super.dispose();
	}
	

	public void setVisible(boolean b)
	{
		if(repNm.contains("Regeneration"))
		{
			changetButton.setText("Regeneration");
			changetButton.setBounds(95, 177, 120, 30);		}
		else
		{
			changetButton.setText("Unlock");
			changetButton.setBounds(95, 177, 120, 30);
		}
		super.setVisible(b);
	}
	 
}


