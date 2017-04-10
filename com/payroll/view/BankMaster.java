package com.payroll.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.payroll.dao.BankDAO;
import com.payroll.dto.BankDto;

public class BankMaster extends BaseClass implements ActionListener {


	private static final long serialVersionUID = 1L;
	Font font;
	private JLabel label_12;
	private JLabel branch;
	private JLabel lblDispatchEntry,lblName;
	private JPanel panel_2;
	private JButton exitButton;
	private JButton btnSave,btnPrint,btnAdd,btnDelete;
	private DefaultTableModel stateTableModel;
	private DefaultTableCellRenderer rightRenderer;
	private JTable stateTable;
	private JScrollPane statePane;
	String option=null;
	BankDAO sdao=null;
	int rrow;
	boolean oneRow=false;
	public BankMaster()
	{

		 
		
		rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		
		option="";
		sdao= new BankDAO();
		//setUndecorated(true);
		setResizable(false);
		setSize(1024, 768);		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);



		lblFinancialAccountingYear = new JLabel(loginDt.getFooter());
		lblFinancialAccountingYear.setForeground(Color.BLACK);
		lblFinancialAccountingYear.setBounds(314, 672, 477, 15);
		getContentPane().add(lblFinancialAccountingYear);


		JLabel promleb = new JLabel(promLogo);
		promleb.setBounds(10, 670, 35, 35);
		getContentPane().add(promleb);

		JLabel arisleb = new JLabel(arisLogo);
		arisleb.setBounds(10, 11, 35, 37);
		getContentPane().add(arisleb);

		label_12 = new JLabel((Icon) null);
		label_12.setBounds(10, 649, 35, 35);
		getContentPane().add(label_12);

		branch = new JLabel(loginDt.getBrnnm());
		branch.setForeground(Color.BLACK);
		branch.setFont(new Font("Tahoma", Font.BOLD, 11));
		branch.setBounds(10, 86, 195, 14);
		getContentPane().add(branch);

		panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_2.setBackground(SystemColor.activeCaptionBorder);
		panel_2.setBounds(274, 657, 578, 48);
		getContentPane().add(panel_2);

		lblName = new JLabel("Bank Master");
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		lblName.setForeground(Color.BLACK);
		lblName.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblName.setBounds(419, 83, 400, 22);
		getContentPane().add(lblName);



		
		//////////////////////////////////////////////////////////////////////
		String[] crDrColName = {"Select","Bank Code","Bank Name",""};
		String cdDrData[][] = {};
		stateTableModel = new DefaultTableModel(cdDrData, crDrColName) 
		{
			private static final long serialVersionUID = 1L;
			
			public Class<?> getColumnClass(int column) 
			{
				switch (column) 
				{
				case 0:
					return Boolean.class;
				default:
					return String.class;
				}
			}
		};
		
		stateTable = new JTable(stateTableModel);
		stateTable.setColumnSelectionAllowed(false);
		stateTable.setCellSelectionEnabled(false);
		stateTable.getTableHeader().setReorderingAllowed(false);
		stateTable.getTableHeader().setResizingAllowed(false);
		stateTable.setRowHeight(20);
		stateTable.getTableHeader().setPreferredSize(new Dimension(25, 25));
		stateTable.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		stateTable.getColumnModel().getColumn(0).setPreferredWidth(78);
		stateTable.getColumnModel().getColumn(1).setPreferredWidth(150);
		stateTable.getColumnModel().getColumn(2).setPreferredWidth(350);
		
		stateTable.getColumnModel().getColumn(3).setWidth(0); // hidden field
		stateTable.getColumnModel().getColumn(3).setMinWidth(0); // no
		stateTable.getColumnModel().getColumn(3).setMaxWidth(0); // no
		
		//////////////////////////////////////////////////////////////////////////
		statePane = new JScrollPane(stateTable,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		statePane.setBounds(274, 136, 578, 457);
		getContentPane().add(statePane);
		
		fillTable();
		
		
		KeyListener stateTableListener = new KeyAdapter() 
		{
			public void keyPressed(KeyEvent evt) {
				int column = stateTable.getSelectedColumn();
				int row = stateTable.getSelectedRow();
				int totRow=stateTable.getRowCount();
				rrow = row;
				
				
				if (evt.getKeyCode() == KeyEvent.VK_ENTER) 
				{
					
				   if (!option.equals("A"))	                 // put A for add and E for edit in last field
					   stateTable.setValueAt("E", row, 3);
//				   else
//						stateTable.setValueAt("E", row, 3);
				   
					   
					if (column == 1) 
					{
						stateTable.changeSelection(row, 2, false, false);
						stateTable.editCellAt(row, 2);
					}

					if (column == 2) 
					{
						if(row<totRow-1)
						{
							stateTable.changeSelection(row+1, 1, false, false);
							stateTable.editCellAt(row+1, 1);
						}
						else
						{
							stateTable.changeSelection(row, 1, false, false);
							stateTable.editCellAt(row, 1);
						}
					}
					
					evt.consume();
				}

				/*if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) 
				{
					oedPane.setVisible(false);
					btnSave.requestFocus();
				}*/

			}// /// keypressed

		};
		
		stateTable.addKeyListener(stateTableListener);
		

		btnSave= new JButton("Save");
		btnSave.setBounds(674, 604, 86, 30);
		btnSave.setActionCommand("Save");
		getContentPane().add(btnSave);

		exitButton = new JButton("Exit");
		exitButton.setBounds(766, 604, 86, 30);
		exitButton.setActionCommand("exit");
		getContentPane().add(exitButton);

		
		btnAdd = new JButton("Add");
		btnAdd.setActionCommand("Add");
		btnAdd.setBounds(274, 604, 86, 30);
		getContentPane().add(btnAdd);
		
		btnDelete = new JButton("Delete");
		btnDelete.setActionCommand("Delete");
		btnDelete.setBounds(365, 604, 86, 30);
		getContentPane().add(btnDelete);
		
		btnPrint = new JButton("Print");
		btnPrint.setActionCommand("Print");
		btnPrint.setBounds(456, 604, 86, 30);
		getContentPane().add(btnPrint);
		
		exitButton.addActionListener(this);
		btnSave.addActionListener(this);
		btnDelete.addActionListener(this);
		btnPrint.addActionListener(this);
		btnAdd.addActionListener(this);
		 
	 
			btnAdd.setVisible(true);
			btnDelete.setVisible(false);
			btnPrint.setVisible(false);
			btnSave.setVisible(true);
		 
		/*stateTable.addMouseListener(new MouseAdapter() 
		{
			public void mouseClicked(MouseEvent e) 
			{
				int row = stateTable.getSelectedRow();
				for(int x = 0, y = stateTable.getRowCount(); x < y; x++)   
				{   
					stateTable.setValueAt(new Boolean(false),x,0);
				}
				
				stateTable.setValueAt(new Boolean(true),row,0);
				
				state_code.setText(stateTable.getValueAt(row, 1).toString());
				state_name.setText(stateTable.getValueAt(row, 2).toString());
			}
		});*/
		
		
		
		
		 
		
	}

	
	class MyItemListener implements ItemListener   
	  {   
	    public void itemStateChanged(ItemEvent e) {   
	      Object source = e.getSource();   
	      if (source instanceof AbstractButton == false) return;   
	      boolean checked = e.getStateChange() == ItemEvent.SELECTED;   
	      for(int x = 0, y = stateTable.getRowCount(); x < y; x++)   
	      {   
	    	  stateTable.setValueAt(new Boolean(checked),x,0);   
	      }   
	    }   
	  } 
	
	
 

	
	 

	public ArrayList bankUpdate() 
	{
		  
		Vector col = null;
		BankDto bd = null;

		ArrayList BankList = new ArrayList();
		Vector bankData = stateTableModel.getDataVector();
		try {
			   int s = bankData.size();
			for (int i = 0; i < s; i++) {
				col = (Vector) bankData.get(i);
				 
				  if(col.get(3).equals("E"))	
				  {
					bd = new BankDto();
					bd.setBank_code(setIntNumber(col.get(1).toString()));
					bd.setBank_name(col.get(2).toString());
					BankList.add(bd);
				  }

				} // end of for loop 
				  
		} 
		catch (Exception e) 
		{
			System.out.println(e);
		}
		
		return BankList;		
		
		     
	}	
	public ArrayList bankAdd() 
	{
		  
		Vector col = null;
		BankDto bd = null;

		ArrayList BankList = new ArrayList();
		Vector bankData = stateTableModel.getDataVector();
		try {
			   int s = bankData.size();
			for (int i = 0; i < s; i++) {
				col = (Vector) bankData.get(i);
				 
				  if(col.get(3).equals("A") && setIntNumber(col.get(1).toString())!=0)	
				  {
					bd = new BankDto();
					bd.setBank_code(setIntNumber(col.get(1).toString()));
					bd.setBank_name(col.get(2).toString());
					BankList.add(bd);
				  }
				  stateTable.setValueAt(" ", i, 3);

				} // end of for loop 
				  
		} 
		catch (Exception e) 
		{
			System.out.println("data add nahi haua liasst mein affdf bad hai deklhaiasd ");
			System.out.println(e);
		}
		
		return BankList;		
		
		     
	}
	
	
	public ArrayList getSelectedRow()
	{
		Vector col = null;
		BankDto bd = null;
		ArrayList dataList = new ArrayList();
		Vector data = stateTableModel.getDataVector();
		int s = data.size();
		try 
		{
			for (int i = 0; i<s; i++) 
			{

				col = (Vector) data.get(i);
				if ((Boolean) col.get(0)) 
				{
					bd=new BankDto();
					bd.setBank_code(setIntNumber(col.get(1).toString()));
					dataList.add(bd);
				}
				 
			}
			return dataList;
			

		}

		catch(Exception e)
		{
			System.out.println(e);
		}
		 return null;
		
	}		
	
	public void fillTable()
	{
		stateTableModel.getDataVector().removeAllElements();
		Vector<?> c = null;
	
		 
		Vector<?> v = sdao.getBankList(1,loginDt.getDepo_code(),0);

		 
		int s =v.size(); 
		for(int i =0;i<s;i++)
		{
			c =(Vector<?>) v.get(i);
			stateTableModel.addRow(c);
		}

	}
	
	
	public void actionPerformed(ActionEvent e) 
	{
		System.out.println(e.getActionCommand());
		if(e.getActionCommand().equalsIgnoreCase("Exit"))
		{
			 
			fillTable(); 
			dispose();
		}
		

		if(e.getActionCommand().equalsIgnoreCase("Add"))
		{
			option="A";
			int row =stateTable.getRowCount(); 
			String ss =null;
			try
			{
				ss = stateTableModel.getValueAt(row-1, 2).toString();
			}
			catch(Exception exp)
			{
				ss="";
				stateTable.requestFocus();
				stateTable.changeSelection(row, 1, false, false);
				stateTable.editCellAt(row, 1);
			}
			if(!ss.equalsIgnoreCase(""))
			{
				stateTableModel.addRow(new Object[] {});
				stateTable.requestFocus();
				stateTable.changeSelection(row, 1, false, false);
				stateTable.editCellAt(row, 1);
				stateTable.setValueAt(new Boolean(false), row, 0);
				stateTable.setValueAt("A", row, 3);
				stateTable.setValueAt("", row, 1);
			}
			else
			{
				stateTable.requestFocus();
				stateTable.changeSelection(row-1, 1, false, false);
				stateTable.editCellAt(row-1, 1);
			}
			
		}
		
		
		if(e.getActionCommand().equalsIgnoreCase("Delete"))
		{
			option="D";
			 int h=0;
			 
			 int answer = 0;
			 
  	    	if (getSelectedRow().isEmpty())
  	    		   JOptionPane.showMessageDialog(BankMaster.this, "Please Select Record First!!!!");
  	    	else
  	    	{ 
  	    		answer=JOptionPane.showConfirmDialog(BankMaster.this, "Are You Sure : ");
			    if (answer == JOptionPane.YES_OPTION) {
			      // User clicked YES.
			    	h = sdao.deleteBank(getSelectedRow());
			    	fillTable();
			    } else if (answer == JOptionPane.NO_OPTION) {
			      // User clicked NO.
			    }
  	    	} 
			 
			 
			 System.out.println("record deleted "+h);
			 
		}	
		
		
		if(e.getActionCommand().equalsIgnoreCase("Save"))
		{
			 int h=0;
			 ArrayList l=null;
			 if (option.equals("A")){
				 l=bankAdd();
				 if (!l.isEmpty()) 		
					 h = sdao.addBank(l);
			 }else{
				 l=bankUpdate();
				 if (!l.isEmpty()) 				 
					 h = sdao.updateBank(l);
			 }
			 option="";
			 fillTable(); 
			 System.out.println("record update "+h);
			
		}				

	}
	
	
	 
	
 
	
 
}


