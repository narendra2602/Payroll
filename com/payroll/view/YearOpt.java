package com.payroll.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import com.payroll.dao.MonthDAO;
import com.payroll.dto.YearDto;

public class YearOpt extends BaseClass implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	private JLabel reportName ;
	private JLabel sdateLeb;
	private JButton exitButton,changetButton;
 
	private JComboBox year;
	String ClassNm;
 
	 
	YearDto yd;
	MonthDAO mdao =null;
	 
	public  YearOpt(String nm,String repNm)
	{
		 
		ClassNm=nm;

		
		//setUndecorated(true);
		setResizable(false);
		setSize(400, 285);	
		setLocationRelativeTo(null);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		getContentPane().setLayout(null);
		mdao = new MonthDAO();
		 
		///////////////////////////////////////////////////////////

		reportName = new JLabel(repNm);
		reportName.setHorizontalAlignment(SwingConstants.CENTER);
		reportName.setFont(new Font("Tahoma", Font.BOLD, 14));
		reportName.setBounds(49, 46, 303, 20);
		getContentPane().add(reportName);

		sdateLeb = new JLabel("Select Financial Year:");
		sdateLeb.setBounds(45, 119, 150, 20);
		getContentPane().add(sdateLeb);

		year = new JComboBox(loginDt.getNewyear());
		year.setBounds(199, 119, 136, 23);
		getContentPane().add(year);
		year.setActionCommand("year");
		year.setMaximumRowCount(12);

		
		
		///////////////////////////////////////////////////////////////////////////////////////////////

		exitButton = new JButton("Exit");
		exitButton.setBounds(215, 177, 70, 30);
		getContentPane().add(exitButton);


		changetButton = new JButton("Change");
		changetButton.setBounds(113, 177, 82, 30);
		getContentPane().add(changetButton);
		
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(15, 23, 366, 208);
		getContentPane().add(panel_1);

		exitButton.addActionListener(this);
	    changetButton.addActionListener(this);
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
		
	    if(e.getActionCommand().equalsIgnoreCase("Change") )
	    {
			try
			{
				System.gc(); 
				java.awt.Window win[] = java.awt.Window.getWindows();
				System.out.println("no. of windows is "+win.length);
				int visibleWins=0;

				for(int i=0;i<win.length;i++)
				{ 
					if(win[i].isVisible())
					{
						visibleWins++;
					}
				}

				System.out.println("no. of windows Visible wins "+visibleWins);
				if(visibleWins>2)
				{

					int answer = JOptionPane.showOptionDialog(this, "Close all opened windows before Changing month \n or you might loose some data","Changing Month Yes/No",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null,  new String[] {"Yes", "No"}, "No");

					if (answer == JOptionPane.YES_OPTION) 
					{

						for(int i=0;i<win.length;i++)
						{
							
							String className = win[i].getClass().getSimpleName().toString().trim();
							if(!className.equalsIgnoreCase("MenuFrame"))
							{	
								win[i].dispose();
								win[i]=null;
							}
						}
						
						map=null;
						map = new HashMap();
						YearDto sdt = (YearDto) year.getSelectedItem();
 
						int i = mdao.setYear(sdt.getYearcode(), loginDt);
						lblAccountingYear.setText(loginDt.getMessage());
						lblFinancialAccountingYear.setText(loginDt.getFooter());
						 

						resetAll();
						dispose();
					}
				}
				else
				{
					map=null;
					map = new HashMap();
					YearDto sdt = (YearDto) year.getSelectedItem();

					int i = mdao.setYear(sdt.getYearcode(), loginDt);
					lblAccountingYear.setText(loginDt.getMessage());
					lblFinancialAccountingYear.setText(loginDt.getFooter());
				 
					resetAll();
					dispose();
				}
				
			}
			catch(Exception ez)
			{
				System.out.println("error");
			}

		}

		
	}
 
	
	
	
}


