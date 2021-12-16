package com.payroll.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import com.payroll.dao.PayrollDAO;
import com.payroll.dto.EmptranDto;
import com.payroll.dto.MonthDto;

public class UnlockEntries extends BaseClass implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	private JLabel reportName ;
	private JButton exitButton,changetButton;
	String ClassNm,repNm;
	 
	
	
	 
	private JLabel lblPassword;	
	private JPasswordField passwordF;
	private DefaultTableModel AttdTableModel;
	private JTable AttdTable;
	private JScrollPane budPane ;
	private JLabel sdateLeb;
	private JComboBox smonth;
	MonthDto mdto=null;
	PayrollDAO pdao=null;
	public  UnlockEntries(String nm,String repNm)
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
		pdao = new PayrollDAO();
		reportName = new JLabel(repNm);
		reportName.setHorizontalAlignment(SwingConstants.CENTER);
		reportName.setFont(new Font("Tahoma", Font.BOLD, 14));
		reportName.setBounds(125, 46, 303, 20);
		getContentPane().add(reportName);

		
		sdateLeb = new JLabel("Select Month:");
		sdateLeb.setBounds(207,80, 110, 20);
		getContentPane().add(sdateLeb);

		smonth = new JComboBox(loginDt.getFmonth());
		smonth.setBounds(308, 81, 159, 23);
		getContentPane().add(smonth);
		smonth.setActionCommand("month");
		smonth.setMaximumRowCount(12);

		smonth.setSelectedIndex(loginDt.getMno());

		 
		lblPassword = new JLabel("Password:");
		lblPassword.setForeground(Color.BLUE);
		lblPassword.setBounds(207, 114, 70, 20);
		getContentPane().add(lblPassword);

		passwordF = new JPasswordField();
		passwordF.setBounds(308, 111, 159, 25);
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


		//////////////////////////////////////////////////////////////////////
		String[] crDrColName = {"Sno","Description","Locked by","Date/Time","Lock",""};
		String cdDrData[][] = {};
		AttdTableModel = new DefaultTableModel(cdDrData, crDrColName) 
		{
			private static final long serialVersionUID = 1L;
			
 			public boolean isCellEditable(int row, int column) 
			{
				boolean ress = true;
				int col=AttdTable.getSelectedColumn();
				if (col<=3) 
				{
					ress = false;
				}
				 
				return ress;
			}
 			
			
			public Class<?> getColumnClass(int column) 
			{
				switch (column) 
				{
					case 4:
						return Boolean.class;
					default:
						return String.class;
				}
			}
			
			
			
		};
		
		AttdTable = new JTable(AttdTableModel);
		AttdTable.setColumnSelectionAllowed(false);
		AttdTable.setCellSelectionEnabled(false);
		AttdTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		AttdTable.getTableHeader().setReorderingAllowed(false);
		AttdTable.getTableHeader().setResizingAllowed(false);
		AttdTable.setRowHeight(20);
		//AttdTable.getTableHeader().setPreferredSize(new Dimension(25, 25));
		AttdTable.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		
		AttdTable.getColumnModel().getColumn(0).setPreferredWidth(35);//select
		AttdTable.getColumnModel().getColumn(1).setPreferredWidth(170);//name
		AttdTable.getColumnModel().getColumn(2).setPreferredWidth(95);//locked by
		AttdTable.getColumnModel().getColumn(3).setPreferredWidth(150);// datetime
		AttdTable.getColumnModel().getColumn(4).setPreferredWidth(50);//code
		
		AttdTable.getColumnModel().getColumn(5).setPreferredWidth(0);
		AttdTable.getColumnModel().getColumn(5).setMinWidth(0); // hidden lock 
		AttdTable.getColumnModel().getColumn(5).setMaxWidth(0);

	 
		 
		

		
		AttdTable.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), "selectNextColumnCell");

		//////////////////////////////////////////////////////////////////////////
		//budPane = new JScrollPane(AttdTable,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		budPane = new JScrollPane(AttdTable,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		budPane.setBounds(41, 150, 504, 123);
		getContentPane().add(budPane);
		
	
		
		
			
		exitButton = new JButton("Exit");
		exitButton.setBounds(305, 285, 100, 30);
		getContentPane().add(exitButton);


		changetButton = new JButton("Save");
		changetButton.setBounds(185, 285, 100, 30);
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
			    	// setDataForDAO();
				}
			}
		});
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(10, 22, 560, 312);
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
		 AttdTableModel.getDataVector().removeAllElements();	 
		 smonth.setSelectedIndex(smonth.getSelectedIndex());
		 passwordF.setText("");
	 }		 
	 

	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equalsIgnoreCase("Exit"))
		{
			dispose();
			
		}
		 
	  

	    if(e.getActionCommand().equalsIgnoreCase("Save"))
		{
			int h=0;
			ArrayList l=null;
			AttdTable.requestFocus();
			AttdTable.changeSelection(0, 4, false, false);
			AttdTable.editCellAt(0, 4); 

			l=Update();
			if (!l.isEmpty()) 
			{
				h = pdao.updateUnlockList(l);
				alertMessage(this, "Saved successfully for the Month "+ mdto.getMnthname());
			}

			 System.out.println("record update "+h);
			 
		}	
	    
		
	}

	
	public ArrayList Update() 
	{
		  
		Vector col = null;
		EmptranDto emp = null;
		ArrayList attnList = new ArrayList();
		Vector attnData = AttdTableModel.getDataVector();
		
		try {
			   int s = attnData.size();
			   for (int i = 0; i < s; i++) 
			   {
				   col = (Vector) attnData.get(i);
				   emp = new EmptranDto();
				   emp.setSerialno(setIntNumber(col.get(0).toString()));
				   if((Boolean) col.get(4))
					   emp.setAtten_lock(1);
				   else
					   emp.setAtten_lock(0);

				   emp.setDepo_code(loginDt.getDepo_code());
				   emp.setCmp_code(loginDt.getCmp_code());
				   emp.setMnth_code(mdto.getMnthcode());
				   attnList.add(emp);
				} // end of for loop 
				  
		} 
		catch (Exception e) 
		{
			System.out.println(e);
		}
		
		return attnList;		
		
		     
	}

	public void fillTable()
	{
		AttdTableModel.getDataVector().removeAllElements();
		AttdTableModel.fireTableDataChanged();

		Vector<?> c = null;
		 mdto=(MonthDto)smonth.getSelectedItem();
		Vector<?> v = pdao.getUnlockList(loginDt.getDepo_code(),loginDt.getCmp_code(), mdto.getMnthcode()); 
		int size = v.size();
		if(size>0)
		{
			for(int i =0;i<size;i++)
			{
				c =(Vector<?>) v.get(i);
				AttdTableModel.addRow(c);
			}
			 
		}
		else
		{
			for (int i = 0; i < 5; i++) 
			{
				AttdTableModel.addRow(c);
			}
		}

		 
		
	}

	 
	
	public void checkPassword()
	{
		String password = passwordF.getText().toString().trim();
		if(password.equals("Prompt"))
		{
			 fillTable();
		}
		else
		{
			alertMessage(UnlockEntries.this, "Please Enter Correct Password");
			passwordF.setText("");
			passwordF.requestFocus();
		}
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


