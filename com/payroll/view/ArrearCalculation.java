package com.payroll.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.payroll.dao.PayrollDAO;
import com.payroll.dto.EmptranDto;
import com.payroll.dto.YearDto;
import com.payroll.print.ArrearReport;
import com.payroll.util.OverWriteTableCellEditor;

public class ArrearCalculation extends BaseClass implements ActionListener 
{


	private static final long serialVersionUID = 1L;
	
	Font font;
	private JLabel label;
	private JLabel label_2;
	private JLabel label_12;
	private JLabel branch;
	private JLabel lblDispatchEntry;
	private JPanel panel_2;
	private JButton exitButton;
	private JButton btnSave,btnExcel;
	private DefaultTableModel AttdTableModel;
	private DefaultTableCellRenderer rightRenderer;
	private JTable AttdTable;
	private JScrollPane budPane ;
	NumberFormat formatter;
	boolean oneRow=false;
	int rrow;
 
	 
	YearDto ydto=null;
	PayrollDAO pdao=null;

	private JComboBox fyear;
	private JRadioButton rdbtnPending,rdbtnPaid;

	private JLabel lblSelectMonth;

	public ArrearCalculation()
	{

		rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		
		 
		pdao = new PayrollDAO();
		formatter = new DecimalFormat("0.00");
		
		//setUndecorated(true);
		setResizable(false);
		setSize(1024, 768);		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		
		



		lblFinancialAccountingYear = new JLabel(loginDt.getFooter());
		lblFinancialAccountingYear.setForeground(Color.BLACK);
		lblFinancialAccountingYear.setBounds(314, 672, 477, 15);
		getContentPane().add(lblFinancialAccountingYear);

		
	 
		 		
		
//		label = new JLabel("ARISTO");
//		label.setHorizontalAlignment(SwingConstants.LEFT);
//		label.setForeground(new Color(220, 20, 60));
//		label.setFont(new Font("Bookman Old Style", Font.BOLD, 23));
//		label.setBounds(10, 48, 184, 22);
//		getContentPane().add(label);

		JLabel promleb = new JLabel(promLogo);
		promleb.setBounds(10, 670, 35, 35);
		getContentPane().add(promleb);

//		JLabel arisleb = new JLabel(arisLogo);
//		arisleb.setBounds(10, 11, 35, 37);
//		getContentPane().add(arisleb);

//		label_2 = new JLabel("PHARMACEUTICALS PVT. LTD.");
//		label_2.setForeground(Color.BLACK);
//		label_2.setFont(new Font("Tahoma", Font.BOLD, 11));
//		label_2.setBounds(10, 71, 195, 14);
//		getContentPane().add(label_2);

		label_12 = new JLabel((Icon) null);
		label_12.setBounds(10, 649, 35, 35);
		getContentPane().add(label_12);

		branch = new JLabel(loginDt.getBrnnm());
		branch.setForeground(new Color(56, 119, 128));
		branch.setFont(new Font("Tahoma", Font.BOLD, 20));
		branch.setBounds(377, 146, 418, 22);
		getContentPane().add(branch);
	 
		panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_2.setBackground(SystemColor.activeCaptionBorder);
		panel_2.setBounds(224, 657, 578, 48);
		getContentPane().add(panel_2);

		lblDispatchEntry = new JLabel("Pending Arrear Calculation");
		lblDispatchEntry.setHorizontalAlignment(SwingConstants.CENTER);
		lblDispatchEntry.setForeground(Color.BLACK);
		lblDispatchEntry.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblDispatchEntry.setBounds(287, 188, 418, 22);
		getContentPane().add(lblDispatchEntry);

		
		lblSelectMonth = new JLabel("Select Year");
		lblSelectMonth.setBounds(401, 232, 86, 22);
		getContentPane().add(lblSelectMonth);
		
		fyear = new JComboBox((loginDt.getFyear()));
		fyear.setActionCommand("Year");
		fyear.setBounds(492, 232, 154, 22);
		getContentPane().add(fyear);
	
		rdbtnPending = new JRadioButton("Pending");
		rdbtnPending.setSelected(true);
		rdbtnPending.setBounds(692, 232, 92, 22);
		getContentPane().add(rdbtnPending);
		
		rdbtnPaid = new JRadioButton("Paid");
		rdbtnPaid.setBounds(800, 232, 92, 22);
		getContentPane().add(rdbtnPaid);
	
		ButtonGroup b = new ButtonGroup();
		b.add(rdbtnPending);
		b.add(rdbtnPaid);
		

		
		//////////////////////////////////////////////////////////////////////
		String[] crDrColName = {"Sl","Sno","Employee Name","Month","ESIC No","PF No.","Code","Arrear Amt","Arrear Sanction","",""};
		String cdDrData[][] = {};
		AttdTableModel = new DefaultTableModel(cdDrData, crDrColName) 
		{
			private static final long serialVersionUID = 1L;
			
 			public boolean isCellEditable(int row, int column) 
			{
				if(column!=0)
				{
					return false;
				}

				else
				{
					return true;
				}
			}
 			
			
			public Class<?> getColumnClass(int column) 
			{
				switch (column) 
				{
					case 0:
						return Boolean.class;
					case 7:
						return Double.class;
					case 6:
						return Integer.class;
					case 8:
						return Double.class;
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
		
		
		AttdTable.getColumnModel().getColumn(0).setPreferredWidth(45);//select
		AttdTable.getColumnModel().getColumn(1).setPreferredWidth(35);//select
		AttdTable.getColumnModel().getColumn(2).setPreferredWidth(200);//name
		AttdTable.getColumnModel().getColumn(3).setPreferredWidth(90);//Month
		AttdTable.getColumnModel().getColumn(4).setPreferredWidth(120);//ESIC no
		AttdTable.getColumnModel().getColumn(5).setPreferredWidth(90);//PF No
		AttdTable.getColumnModel().getColumn(6).setPreferredWidth(80);//code
		AttdTable.getColumnModel().getColumn(7).setPreferredWidth(100);//Advance
		AttdTable.getColumnModel().getColumn(8).setPreferredWidth(115);//Loan
		
		AttdTable.getColumnModel().getColumn(9).setPreferredWidth(0);
		AttdTable.getColumnModel().getColumn(9).setMinWidth(0); // hidden emptranDto
		AttdTable.getColumnModel().getColumn(9).setMaxWidth(0);

		AttdTable.getColumnModel().getColumn(10).setPreferredWidth(0);
		AttdTable.getColumnModel().getColumn(10).setMinWidth(0); // hidden bonus_applicable
		AttdTable.getColumnModel().getColumn(10).setMaxWidth(0);
	 
		AttdTable.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
		

		AttdTable.setDefaultEditor(Integer.class, new OverWriteTableCellEditor());
		AttdTable.setDefaultEditor(Double.class, new OverWriteTableCellEditor());
		
		AttdTable.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), "selectNextColumnCell");

		//////////////////////////////////////////////////////////////////////////
		//budPane = new JScrollPane(AttdTable,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		budPane = new JScrollPane(AttdTable,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		budPane.setBounds(49, 265, 904, 326);
		getContentPane().add(budPane);
		
 
		
		DefaultTableCellRenderer dtc = new DefaultTableCellRenderer() 
		{
			public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) 
			{
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (AttdTable.getSelectedRow()==row )
				{
					  c.setBackground(new Color(170, 181, 157));
					  c.setForeground(Color.BLACK);
					  if( AttdTable.getSelectedColumn()==column)
					  {
						  c.setBackground(Color.YELLOW);
						  c.setForeground(Color.RED);
						  c.setFont(new Font("Tahoma", Font.BOLD, 12));
					  }
				}
				else
				{
					  c.setBackground(Color.WHITE);
					  c.setForeground(Color.BLACK);

				}

				
		        return c;
			}
		};
		
		 
//		AttdTable.getColumnModel().getColumn(0).setCellRenderer(dtc);
		AttdTable.getColumnModel().getColumn(1).setCellRenderer(dtc);
		AttdTable.getColumnModel().getColumn(2).setCellRenderer(dtc);
		AttdTable.getColumnModel().getColumn(3).setCellRenderer(dtc);
		AttdTable.getColumnModel().getColumn(4).setCellRenderer(dtc);
		AttdTable.getColumnModel().getColumn(5).setCellRenderer(dtc);
		AttdTable.getColumnModel().getColumn(6).setCellRenderer(dtc);
		AttdTable.getColumnModel().getColumn(7).setCellRenderer(dtc);
		AttdTable.getColumnModel().getColumn(8).setCellRenderer(dtc);
		
		fillTable(); 
		
		 
		
		KeyListener AttdTableListener = new KeyAdapter() 
		{
			public void keyPressed(KeyEvent evt) {
				int column = AttdTable.getSelectedColumn();
				int row = AttdTable.getSelectedRow();
				int totRow=AttdTable.getRowCount();
				rrow = row;
				
				
				
				if (evt.getKeyCode() == KeyEvent.VK_ENTER) 
				{
					
					if (column == 1) 
					{
						AttdTable.changeSelection(row, 2, false, false);
						AttdTable.editCellAt(row, 2);
					}
					if (column == 2) 
					{
						AttdTable.changeSelection(row, 3, false, false);
						AttdTable.editCellAt(row, 3);
					}
					if (column == 3) 
					{
						AttdTable.changeSelection(row, 4, false, false);
						AttdTable.editCellAt(row, 4);
					}

					if (column == 4) 
					{
						AttdTable.changeSelection(row, 5, false, false);
						AttdTable.editCellAt(row, 5);
					}

					if (column == 5) 
					{
						AttdTable.changeSelection(row, 6, false, false);
						AttdTable.editCellAt(row, 6);
					}
					 
					if (column == 6) 
					{
						AttdTable.changeSelection(row, 2, false, false);
						AttdTable.editCellAt(row,2);
						if(row<totRow-1)
						{
							AttdTable.changeSelection(row+1, 5, false, false);
							AttdTable.editCellAt(row+1, 5);
						}
						else
						{
							AttdTable.changeSelection(row, 5, false, false);
							AttdTable.editCellAt(row, 5);
						}						

					}				 
					 
					
					evt.consume();
				}

			

			}// /// keypressed

		};
		
		AttdTable.addKeyListener(AttdTableListener);
		
		
	  
		btnExcel= new JButton("Excel");
		btnExcel.setActionCommand("Excel");
		btnExcel.setBounds(49, 595, 86, 30);
		getContentPane().add(btnExcel);
		
		btnSave= new JButton("Save");
		btnSave.setActionCommand("Save");
		btnSave.setBounds(770, 595, 86, 30);
		getContentPane().add(btnSave);
		
		exitButton = new JButton("Exit");
		exitButton.setActionCommand("Exit");
		exitButton.setBounds(862, 595, 86, 30);
		getContentPane().add(exitButton);
		
		
		
		exitButton.addActionListener(this);
		btnSave.addActionListener(this);
		btnExcel.addActionListener(this);
		fyear.addActionListener(this); 
		rdbtnPaid.addActionListener(this);
		rdbtnPending.addActionListener(this);
		
		
	}

	
	
	public ArrayList Update() 
	{
		  
		Vector col = null;
		EmptranDto emp = null;
		ArrayList attnList = new ArrayList();
		Vector attnData = AttdTableModel.getDataVector();
		Date createdDate = new Date();
		
		try {
			   int s = attnData.size();
			   for (int i = 0; i < s; i++) 
			   {
				   col = (Vector) attnData.get(i);
				   if((Boolean) col.get(0) || col.get(10).toString().equals("O"))
				   {
					   emp = new EmptranDto();
					   emp.setEmp_code(setIntNumber(col.get(6).toString()));
					   emp.setArrear_amt(setDoubleNumber(col.get(7).toString()));
					   emp.setArrear_sanc(setDoubleNumber(col.get(8).toString()));
					   //emp.setMnthdays(setIntNumber(col.get(7).toString())); // month days
					   emp.setMnthdays(loginDt.getMnth_code()); // month days jis month mein arrear paid kiya
					   emp.setFin_year(ydto.getYearcode());
					   emp.setDepo_code(loginDt.getDepo_code());
					   emp.setCmp_code(loginDt.getCmp_code());
					   emp.setMnth_code(setIntNumber(col.get(9).toString()));
					   //emp.setMnth_code(loginDt.getMnth_code());
					   emp.setModified_by(loginDt.getLogin_id());
					   emp.setModified_date(createdDate);
					   emp.setSerialno(setIntNumber(col.get(1).toString()));
					   emp.setPaid("N");
					   if((Boolean) col.get(0))
						   emp.setPaid("O");
					   attnList.add(emp);
				   }
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
		ydto=(YearDto)fyear.getSelectedItem();
		int mncode=0;
		Vector<?> v = pdao.getPendingArrearList(loginDt.getDepo_code(),loginDt.getCmp_code(), ydto.getYearcode(), loginDt.getMnth_code(),loginDt.getFin_year(),rdbtnPaid.isSelected()?"O":"N"); 
		int size = 0;
		if(v!=null)
			size = v.size();
		if(size>0)
		{
			for(int i =0;i<size;i++)
			{
				c =(Vector<?>) v.get(i);
				AttdTableModel.addRow(c);
				mncode=setIntNumber(c.get(7).toString());
			}
		}
		else
		{
			for (int i = 0; i < 15; i++) 
			{
				AttdTableModel.addRow(c);
			}
		}

		 //getMonthIndex(mncode);
		 //fyear.setSelectedIndex(getMonthIndex(mncode));
		
	}
	
	
	public void actionPerformed(ActionEvent e) 
	{
		System.out.println(e.getActionCommand());

		if(e.getActionCommand().equalsIgnoreCase("Exit"))
		{
			rdbtnPending.setSelected(true);
			dispose();
		}

		
		if(e.getActionCommand().equalsIgnoreCase("Year"))
		{

			 ydto=(YearDto) fyear.getSelectedItem();
			 AttdTable.requestFocus();
			 AttdTable.changeSelection(0, 7, false, false);
			 AttdTable.editCellAt(0, 7);
			 AttdTable.changeSelection(0, 7, false, false);

			 fillTable();
			 AttdTable.requestFocus();
			 AttdTable.changeSelection(0, 5, false, false);
			 AttdTable.editCellAt(0, 5);
			 AttdTable.changeSelection(0, 5, false, false);
			 
		}
			
		if(e.getActionCommand().equalsIgnoreCase("Paid") || e.getActionCommand().equalsIgnoreCase("Pending"))
		{

			 fillTable();
			 AttdTable.requestFocus();
			 AttdTable.changeSelection(0, 5, false, false);
			 AttdTable.editCellAt(0, 5);
			 AttdTable.changeSelection(0, 5, false, false);
		}
	
	
		
		if(e.getActionCommand().equalsIgnoreCase("Save"))
		{
			int h=0;
			ArrayList l=null;
			AttdTable.requestFocus();
			AttdTable.changeSelection(0, 7, false, false);
			AttdTable.editCellAt(0, 7); 

			l=Update();
			if (!l.isEmpty()) 
			{
				String password = confirmationDialongPassword();
				
				if(password!=null && password.equalsIgnoreCase("Prompt"))
				{
					h = pdao.updateArrearList(l);
					alertMessage(this, "Saved successfully in Month "+ loginDt.getMnthName());
					//alertMessage(this, "Updation Not Done????");
					fillTable();
				}
				else
					alertMessage(this, "Incorrect Password ");
			}

			 System.out.println("record update "+h);
			 
		}		
		

		if(e.getActionCommand().equalsIgnoreCase("Excel"))
		{
			int smon = Integer.parseInt(ydto.getYearcode()+"04");
			int emon = Integer.parseInt((ydto.getYearcode()+1)+"03");
			int repno=11;
			repno=rdbtnPaid.isSelected()?99:11;
			
		//	new EsicList(loginDt.getDepo_code(), loginDt.getCmp_code(), loginDt.getFin_year(), mdto.getMnthcode(), loginDt.getBrnnm(), loginDt.getDrvnm(), mdto.getMnthname(), 2, 3,0);
			new ArrearReport(loginDt.getDepo_code(), loginDt.getCmp_code(), ydto.getYearcode(),loginDt.getBrnnm(),loginDt.getDrvnm(),repno,smon,emon);
		}		
	}
	
	
	public void setVisible(boolean b)
	{
		 fyear.setSelectedIndex(1);
		 fillTable();
		 super.setVisible(b);
	}

}


