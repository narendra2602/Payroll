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

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
import com.payroll.dto.MonthDto;
import com.payroll.print.AttendanceList;
import com.payroll.util.OverWriteTableCellEditor;

public class AttendanceEntry extends BaseClass implements ActionListener 
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
 
	 
	MonthDto mdto=null;
	PayrollDAO pdao=null;

	private JComboBox month;

	private JLabel lblSelectMonth;

	public AttendanceEntry()
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

		
	 
		 		
		
		//label = new JLabel("ARISTO");
		//label.setHorizontalAlignment(SwingConstants.LEFT);
		//label.setForeground(new Color(220, 20, 60));
		//label.setFont(new Font("Bookman Old Style", Font.BOLD, 23));
		//label.setBounds(10, 48, 184, 22);
		//getContentPane().add(label);

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
		branch.setBounds(397, 146, 418, 22);
		getContentPane().add(branch);

	 
		panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_2.setBackground(SystemColor.activeCaptionBorder);
		panel_2.setBounds(224, 657, 578, 48);
		getContentPane().add(panel_2);

		lblDispatchEntry = new JLabel("Attendance Entry");
		lblDispatchEntry.setHorizontalAlignment(SwingConstants.CENTER);
		lblDispatchEntry.setForeground(Color.BLACK);
		lblDispatchEntry.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblDispatchEntry.setBounds(357, 188, 418, 22);
		getContentPane().add(lblDispatchEntry);

		
		lblSelectMonth = new JLabel("Select Month");
		lblSelectMonth.setBounds(401, 232, 86, 22);
		getContentPane().add(lblSelectMonth);
		
		month = new JComboBox((loginDt.getFmonth()));
		month.setActionCommand("Month");
		month.setBounds(492, 232, 154, 22);
		getContentPane().add(month);
	
		

		
		//////////////////////////////////////////////////////////////////////
		String[] crDrColName = {"Sno","Employee Name","ESIC No","PF No.","Code","Attn","Extra Hrs","Extra Days","Remark",""};
		String cdDrData[][] = {};
		AttdTableModel = new DefaultTableModel(cdDrData, crDrColName) 
		{
			private static final long serialVersionUID = 1L;
			
 			public boolean isCellEditable(int row, int column) 
			{
				boolean ress = true;
				int col=AttdTable.getSelectedColumn();
				if (col<=4) 
				{
					ress = false;
				}
				return ress;
			}
 			
			
			public Class<?> getColumnClass(int column) 
			{
				switch (column) 
				{
					case 5:
						return Double.class;
					case 6:
						return Double.class;
					case 7:
						return Double.class;
					case 8:
						return String.class;
					case 9:
						return EmptranDto.class;
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
		AttdTable.getColumnModel().getColumn(1).setPreferredWidth(250);//name
		AttdTable.getColumnModel().getColumn(2).setPreferredWidth(100);//ESIC no
		AttdTable.getColumnModel().getColumn(3).setPreferredWidth(80);//PF No
		AttdTable.getColumnModel().getColumn(4).setPreferredWidth(60);//code
		AttdTable.getColumnModel().getColumn(5).setPreferredWidth(60);//Attendance
		AttdTable.getColumnModel().getColumn(6).setPreferredWidth(74);//Extra Hrs
		AttdTable.getColumnModel().getColumn(7).setPreferredWidth(74);//Arrears
		AttdTable.getColumnModel().getColumn(8).setPreferredWidth(155);//remark
		
		AttdTable.getColumnModel().getColumn(9).setPreferredWidth(0);
		AttdTable.getColumnModel().getColumn(9).setMinWidth(0); // hidden emptranDto
		AttdTable.getColumnModel().getColumn(9).setMaxWidth(0);

	 
		AttdTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
		AttdTable.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
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
		
		 
		AttdTable.getColumnModel().getColumn(0).setCellRenderer(dtc);
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
						AttdTable.changeSelection(row, 7, false, false);
						AttdTable.editCellAt(row, 7);
					}
					if (column == 7) 
					{
						AttdTable.changeSelection(row, 8, false, false);
						AttdTable.editCellAt(row, 8);
					}
					if (column == 8) 
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
		month.addActionListener(this); 
		
		
	}

	
	 
	

	
	public ArrayList attendanceAddUpdate() 
	{
		  
		Vector col = null;
		EmptranDto emp = null;
		ArrayList attnList = new ArrayList();
		Vector attnData = AttdTableModel.getDataVector();
		Date createdDate = new Date();
		double attenDays=0;
		try {
			   int s = attnData.size();
			   for (int i = 0; i < s; i++) 
			   {
				   col = (Vector) attnData.get(i);
				   emp = (EmptranDto) col.get(9);
				   emp.setAtten_days(setDoubleNumber(col.get(5).toString()));
				   emp.setExtra_hrs(setDoubleNumber(col.get(6).toString()));
				   emp.setArrear_days(setDoubleNumber(col.get(7).toString()));
				   emp.setRemark(col.get(8).toString());
				   emp.setFin_year(loginDt.getFin_year());
				   emp.setDepo_code(loginDt.getDepo_code());
				   emp.setCmp_code(loginDt.getCmp_code());
				   emp.setMnth_code(mdto.getMnthcode());
				   emp.setCreated_by(loginDt.getLogin_id());
				   emp.setCreated_date(createdDate);
				   emp.setSerialno(setIntNumber(col.get(0).toString()));
				   emp.setDoc_type(40);
				   emp.setDoc_no(setIntNumber(col.get(0).toString()));
				   emp.setDoc_date(mdto.getEdate());
				   emp.setAbsent_days(mdto.getMnthdays()-(setDoubleNumber(col.get(5).toString())));
				   emp.setMnthdays(mdto.getMnthdays());
				   attnList.add(emp);
				} // end of for loop 
				  
		} 
		catch (Exception e) 
		{
			System.out.println(e);
			e.printStackTrace();
		}
		
		return attnList;		
		
		     
	}
	
	
 	
	public void fillTable()
	{
		AttdTableModel.getDataVector().removeAllElements();
		Vector<?> c = null;
		 mdto=(MonthDto)month.getSelectedItem();
		Vector<?> v = pdao.getAttendanceList(loginDt.getDepo_code(),loginDt.getCmp_code(), loginDt.getFin_year(), mdto.getMnthcode()); 
		int size = v.size();
		for(int i =0;i<size;i++)
		{
			c =(Vector<?>) v.get(i);
			AttdTableModel.addRow(c);
		}
	
		 
//		month.setSelectedIndex(getIndex(mnthcode, month, 5));
		
	}
	
	
	public void actionPerformed(ActionEvent e) 
	{
		System.out.println(e.getActionCommand());

		if(e.getActionCommand().equalsIgnoreCase("Exit"))
		{
			dispose();
		}

		
		if(e.getActionCommand().equalsIgnoreCase("Month"))
		{

			 mdto=(MonthDto) month.getSelectedItem();
			 fillTable();
			 AttdTable.requestFocus();
			 AttdTable.changeSelection(0, 5, false, false);
			 AttdTable.editCellAt(0, 5);
			// AttdTable.changeSelection(0, 5, false, false);
			 
		}
			
	
	
		
		if(e.getActionCommand().equalsIgnoreCase("Save"))
		{
			int h=0;
			ArrayList l=null;
			AttdTable.requestFocus();
			AttdTable.changeSelection(0, 0, false, false);
			AttdTable.editCellAt(0, 0); 
			l=attendanceAddUpdate();
			if (!l.isEmpty()) 
			{
				h = pdao.addAttendance(l);
				alertMessage(this, "Attendance Saved successfully for the Month "+ mdto.getMnthname());
			}

			 System.out.println("record update "+h);
			 
		}		
		

		if(e.getActionCommand().equalsIgnoreCase("Excel"))
		{
			int h=0;
			ArrayList l=null;

			l=attendanceAddUpdate();
			if (!l.isEmpty()) 
			{
				new AttendanceList(loginDt.getBrnnm(), loginDt.getDrvnm(),l, mdto.getMnthname());
			}

			 
		}		
	}
	
	
	public void setVisible(boolean b)
	{
		 month.setSelectedIndex(loginDt.getMno());
		 fillTable();
		 super.setVisible(b);
	}

}


