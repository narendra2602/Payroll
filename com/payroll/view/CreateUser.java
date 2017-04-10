package com.payroll.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

import com.payroll.dao.UserDAO;
import com.payroll.dto.ContractMastDto;
import com.payroll.dto.LoginDto;
import com.payroll.dto.UserDto;

public class CreateUser extends BaseClass  implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	private JLabel lblBankers;
	private JLabel lblOrderNo;
	private JLabel lblChallanNo;
	private JLabel lblInvoiceDate;
	// //////////// textField on Screen//////////////////////////
	private JTextField fname;
	private JTextField lname;
	private JTextField department;
	private JTextField emailId;
	private JLabel lblNewLabel;
	private JLabel lblInoviceNo;
	private JButton saveButton;
	private JButton exitButton;

	
	private JTextField designation;
	private JTextField username;
	private JPasswordField password;
	private JPasswordField cpassword;
	private JLabel lblPersonalInformation;
	private JLabel lblUserType;
	private JLabel lblUserPackage;
	private JPanel panel_1;
	private JPanel panel_5;
	private JPanel panel_6;
	private JPanel panel_7;
	private JPanel panel_8;
	private JPanel panel_2;
	private DefaultTableModel branchTableModel;
	private JTable branchTable;
	private JScrollPane branchPane;
	private JScrollPane reportPane;
	private JPanel panel_11;
	private JLabel lblReports;
	private JScrollPane entryPane;
	private DefaultTableModel EntryTableModel;
	private JTable entryTable;
	private DefaultTableModel ReportTableModel;
	private JTable reportTable;
	private JRadioButton entrySelective;
	private JRadioButton entryAll;
	private JRadioButton reportSelective;
	private JRadioButton reportAll;
	private JComboBox isActivated;
	UserDAO userDao=null;
	private Vector reportVector;
	private Vector entryVector;
	private JCheckBox salePack;
	
	private Vector tempReportVector;
	private Vector tempEntryVector;
	private JComboBox prefex;
	// ///////////Constructor ////////////////////////
	public CreateUser() 
	{
		getContentPane().setLayout(null);
		//setUndecorated(true);
		setResizable(false);
		setSize(1024, 768);	
//		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setTitle("New User ");
	
		JLabel promleb = new JLabel(promLogo);
		promleb.setBounds(32, 681, 35, 35);
		getContentPane().add(promleb);

		userDao = new UserDAO();
		tempReportVector = new Vector();
        tempEntryVector = new Vector();
		
		// /////////////// form buttons//////////////////////
		saveButton = new JButton("Save");
		saveButton.setBounds(798, 686, 88, 30);
		
		getContentPane().add(saveButton);
		saveButton.addKeyListener(new KeyAdapter() 
		{
			public void keyPressed(KeyEvent evt) 
			{
				if (evt.getKeyCode() == KeyEvent.VK_ENTER) 
				{
					//clearAll();
				}
				if (evt.getKeyCode() == KeyEvent.VK_RIGHT) 
				{
					saveButton.setBackground(null);
					exitButton.setBackground(null);
				}
			}
		});
		
		
		exitButton = new JButton("Exit");
		exitButton.setBounds(896, 686, 88, 30);
		getContentPane().add(exitButton);
		
		saveButton.addActionListener(this);
		saveButton.setActionCommand("saveButton");
		
		/*exitButton.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent evt) {
				if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
					 
					//clearAll();
					 
					dispose();
				}
				if (evt.getKeyCode() == KeyEvent.VK_LEFT) 
				{
					saveButton.setBackground(null);
					exitButton.setBackground(null); 
				}
			}
		});		*/
 		exitButton.addActionListener(new ActionListener() 
 		{
			public void actionPerformed(ActionEvent arg0) 
			{
				dispose();
				clearAll();
			}
		});

		// ///////////////////////////////////////////////
 
		// ////////////////////////////////////////////////////////////////

		lblNewLabel = new JLabel("Title:");
		lblNewLabel.setBounds(19, 45, 110, 20);
		getContentPane().add(lblNewLabel);

		
		prefex = new JComboBox();
		prefex.setModel(new DefaultComboBoxModel(new String[] {"Mr.", "Ms."}));
		prefex.setBounds(140, 45, 59, 20);
		getContentPane().add(prefex);
		
		
		
		
		lblInoviceNo = new JLabel("First Name:");
		lblInoviceNo.setBounds(19, 70, 110, 20);
		getContentPane().add(lblInoviceNo);

		
		
		
		fname = new JTextField();
		fname.setBounds(140, 71, 190, 22);
		getContentPane().add(fname);
		
		
		
		
		lblInvoiceDate = new JLabel("Designation:");
		lblInvoiceDate.setBounds(19, 96, 110, 20);
		getContentPane().add(lblInvoiceDate);

		lblBankers = new JLabel("Department:");
		lblBankers.setBounds(342, 96, 98, 20);
		getContentPane().add(lblBankers);

		department = new JTextField();
		department.setBounds(440, 98, 190, 22);
		getContentPane().add(department);

		lblOrderNo = new JLabel("Last Name:");
		lblOrderNo.setBounds(342, 70, 98, 20);
		getContentPane().add(lblOrderNo);

		lname = new JTextField();
		lname.setBounds(440, 71, 190, 22);
		getContentPane().add(lname);

		emailId = new JTextField();
		emailId.setBounds(140, 125, 490, 22);
		getContentPane().add(emailId);


		lblChallanNo = new JLabel("Email Id:");
		lblChallanNo.setBounds(19, 124, 110, 20);
		getContentPane().add(lblChallanNo);

	
		


		
		

		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(10, 668, 998, 60);
		getContentPane().add(panel);
		
		 
		designation = new JTextField();
		designation.setBounds(140, 98, 190, 22);
		getContentPane().add(designation);
		
		JLabel lblLoginId = new JLabel("Username:");
		lblLoginId.setBounds(662, 45, 110, 20);
		getContentPane().add(lblLoginId);
		
		username = new JTextField();
		username.setBounds(783, 44, 190, 22);
		getContentPane().add(username);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(662, 70, 110, 20);
		getContentPane().add(lblPassword);
		
		password = new JPasswordField();
		password.setBounds(783, 71, 190, 22);
		getContentPane().add(password);
		
		JLabel lblConfirmPassword = new JLabel("Confirm Password:");
		lblConfirmPassword.setBounds(662, 96, 133, 20);
		getContentPane().add(lblConfirmPassword);
		
		cpassword = new JPasswordField();
		cpassword.setBounds(783, 98, 190, 22);
		getContentPane().add(cpassword);
		
		JLabel lblLoginInformation = new JLabel("Login Information");
		lblLoginInformation.setForeground(Color.WHITE);
		lblLoginInformation.setBounds(670, 16, 133, 20);
		getContentPane().add(lblLoginInformation);
		
		lblPersonalInformation = new JLabel("Personal Information");
		lblPersonalInformation.setForeground(Color.WHITE);
		lblPersonalInformation.setBounds(23, 14, 133, 20);
		getContentPane().add(lblPersonalInformation);
		
		lblUserType = new JLabel("Entry");
		lblUserType.setForeground(Color.WHITE);
		lblUserType.setBounds(23, 308, 149, 20);
		getContentPane().add(lblUserType);
		
		JLabel lblStatusActive = new JLabel("Status Active:");
		lblStatusActive.setBounds(662, 124, 110, 20);
		getContentPane().add(lblStatusActive);
		
		isActivated = new JComboBox();
		isActivated.setModel(new DefaultComboBoxModel(new String[] {"Yes", "No"}));
		isActivated.setBounds(783, 125, 59, 20);
		getContentPane().add(isActivated);
		
		salePack = new JCheckBox("Payroll");
		salePack.setBounds(671, 199, 88, 23);
		getContentPane().add(salePack);
		salePack.addActionListener(this);
		salePack.setActionCommand("salePack");
		
		lblUserPackage = new JLabel("User Package:");
		lblUserPackage.setForeground(Color.WHITE);
		lblUserPackage.setBounds(670, 163, 97, 20);
		getContentPane().add(lblUserPackage);
		
		panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setBackground(new Color(139, 153, 122));
		panel_1.setBounds(10, 304, 495, 27);
		getContentPane().add(panel_1);
		
		panel_5 = new JPanel();
		panel_5.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_5.setBackground(new Color(139, 153, 122));
		panel_5.setBounds(653, 159, 353, 27);
		getContentPane().add(panel_5);
		
		panel_7 = new JPanel();
		panel_7.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_7.setBounds(766, 184, 2, 113);
		getContentPane().add(panel_7);
		
		panel_6 = new JPanel();
		panel_6.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_6.setBounds(653, 184, 353, 113);
		getContentPane().add(panel_6);
		
		panel_8 = new JPanel();
		panel_8.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_8.setBackground(new Color(139, 153, 122));
		panel_8.setBounds(653, 11, 354, 27);
		getContentPane().add(panel_8);
		
		panel_2 = new JPanel();
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_2.setBackground(new Color(139, 153, 122));
		panel_2.setBounds(10, 11, 635, 27);
		getContentPane().add(panel_2);
		
		prefex.setName("0");
		fname.setName("1");
		lname.setName("2");
		designation.setName("3");
		department.setName("4");
		emailId.setName("5");
		username.setName("6");
		password.setName("7");
		cpassword.setName("8");
		
		
		// /////////////////////////////////////////////////////////////////////////////////////////////
		KeyListener keyListener = new KeyListener() {
			public void keyPressed(KeyEvent keyEvent) {
				int key = keyEvent.getKeyCode();
				int id =0;
				try
				{
					JTextField textField = (JTextField) keyEvent.getSource();
					id= Integer.parseInt(textField.getName());
				}
				catch(Exception e)
				{
					JComboBox textField = (JComboBox) keyEvent.getSource();
					id= Integer.parseInt(textField.getName());	
				}
				
				
				if (key == KeyEvent.VK_ENTER) 
				{
					switch (id) 
					{
					case 0:
						fname.requestFocus();
						fname.setSelectionStart(0);
						break;
					case 1:
						lname.requestFocus();
						lname.setSelectionStart(0);
						break;
					case 2:
						designation.requestFocus();
						designation.setSelectionStart(0);
						break;
					case 3:
						department.requestFocus();
						department.setSelectionStart(0);
						break;
					case 4:
						emailId.requestFocus();
						emailId.setSelectionStart(0);
						break;
					case 5:
						username.requestFocus();
						username.setSelectionStart(0);
						break;	
					case 6:
						password.requestFocus();
						password.setSelectionStart(0);
						break;	
					case 7:
						cpassword.requestFocus();
						cpassword.setSelectionStart(0);
						break;	
					case 8:
				 		isActivated.requestFocus();
						break;
					}
				}

				///// up aerrow  ///   
				if (key == KeyEvent.VK_UP)
				{
					switch (id)
					{
					case 1:
						prefex.requestFocus();
						break;
					case 2:
						fname.requestFocus();
						break;
					case 3:
						lname.requestFocus();
						break;
					case 4:
						designation.requestFocus();
						break;
					case 5:
						department.requestFocus();
						break;
					case 6:
						emailId.requestFocus();
						break;
					case 7:
						username .requestFocus();
						break;

					case 8:
						password.requestFocus();
						password.setSelectionStart(0);
						break;
					 
					 
					}
				}
				
				if (key == KeyEvent.VK_ESCAPE) 
				{
					clearAll();
					
					dispose();
					//System.exit(0);
					
				}
			}

			public void keyReleased(KeyEvent keyEvent) {
			}

			public void keyTyped(KeyEvent keyEvent) {
			}
		};
		prefex.addKeyListener(keyListener);
		fname.addKeyListener(keyListener);
		lname.addKeyListener(keyListener);
		designation.addKeyListener(keyListener);
		department.addKeyListener(keyListener);
		emailId.addKeyListener(keyListener);
		username.addKeyListener(keyListener);
		password.addKeyListener(keyListener);
		cpassword.addKeyListener(keyListener);

		//////////////////////////////////////////////////////////////////////
		String[] crDrColName = {"","Code","Contractor Name",""};
		String cdDrData[][] = {};
		branchTableModel = new DefaultTableModel(cdDrData, crDrColName) 
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

		branchTable = new JTable(branchTableModel);
		branchTable.setColumnSelectionAllowed(false);
		branchTable.setCellSelectionEnabled(false);
		branchTable.getTableHeader().setReorderingAllowed(false);
		branchTable.getTableHeader().setResizingAllowed(false);
		branchTable.setRowHeight(20);
		branchTable.getTableHeader().setPreferredSize(new Dimension(25, 25));

		branchTable.getColumnModel().getColumn(0).setPreferredWidth(30);
		branchTable.getColumnModel().getColumn(1).setPreferredWidth(170);
		branchTable.getColumnModel().getColumn(2).setPreferredWidth(700);

		branchTable.getColumnModel().getColumn(3).setWidth(0); // hidden field
		branchTable.getColumnModel().getColumn(3).setMinWidth(0); // no
		branchTable.getColumnModel().getColumn(3).setMaxWidth(0); // no

		//////////////////////////////////////////////////////////////////////////
		branchPane = new JScrollPane(branchTable,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		branchPane.setBounds(10, 159, 635, 139);
		getContentPane().add(branchPane);

		fillTable();
		
		//////////////////////////////////////////////////////////////////////
		String[] entryColo = {"","Pack Name","Tab Name","Report Name",""};
		String entryData[][] = {};
		EntryTableModel = new DefaultTableModel(entryData, entryColo) 
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

		entryVector = userDao.getUserRights("E");
		
		entryTable = new JTable(EntryTableModel);
		entryTable.setColumnSelectionAllowed(false);
		entryTable.setCellSelectionEnabled(false);
		entryTable.getTableHeader().setReorderingAllowed(false);
		entryTable.getTableHeader().setResizingAllowed(false);
		entryTable.setRowHeight(20);
		entryTable.getTableHeader().setPreferredSize(new Dimension(25, 25));

		entryTable.getColumnModel().getColumn(0).setPreferredWidth(30);
		entryTable.getColumnModel().getColumn(1).setPreferredWidth(170);
		entryTable.getColumnModel().getColumn(2).setPreferredWidth(250);
		entryTable.getColumnModel().getColumn(3).setPreferredWidth(620);

		entryTable.getColumnModel().getColumn(4).setWidth(0); // hidden field
		entryTable.getColumnModel().getColumn(4).setMinWidth(0); // no
		entryTable.getColumnModel().getColumn(4).setMaxWidth(0); // no

		//fillEntryTable();
		
		
		//////////////////////////////////////////////////////////////////////////
		entryPane = new JScrollPane(entryTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		entryPane.setBounds(10, 369, 495, 285);
		getContentPane().add(entryPane);
		
		JPanel panel_9 = new JPanel();
		panel_9.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_9.setBounds(653, 11, 354, 144);
		getContentPane().add(panel_9);
		
	 
		//////////////////////////////////////////////////////////////////////
		String[] reportCol = {"","Pack Name","Tab Name","Report Name",""};
		String reportData[][] = {};
		ReportTableModel = new DefaultTableModel(reportData, reportCol) 
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
		
		reportVector = userDao.getUserRights("R");
		
		reportTable = new JTable(ReportTableModel);
		reportTable.setColumnSelectionAllowed(false);
		reportTable.setCellSelectionEnabled(false);
		reportTable.getTableHeader().setReorderingAllowed(false);
		reportTable.getTableHeader().setResizingAllowed(false);
		reportTable.setRowHeight(20);
		reportTable.getTableHeader().setPreferredSize(new Dimension(25, 25));

		reportTable.getColumnModel().getColumn(0).setPreferredWidth(30);
		reportTable.getColumnModel().getColumn(1).setPreferredWidth(170);
		reportTable.getColumnModel().getColumn(2).setPreferredWidth(170);
		reportTable.getColumnModel().getColumn(3).setPreferredWidth(700);

		reportTable.getColumnModel().getColumn(4).setWidth(0); // hidden field
		reportTable.getColumnModel().getColumn(4).setMinWidth(0); // no
		reportTable.getColumnModel().getColumn(4).setMaxWidth(0); // no

		//fillReportTable();
		
		reportPane = new JScrollPane(reportTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		reportPane.setBounds(511, 369, 495, 285);
		getContentPane().add(reportPane);
		/////
		
		lblReports = new JLabel("Reports");
		lblReports.setForeground(Color.WHITE);
		lblReports.setBounds(523, 308, 149, 20);
		getContentPane().add(lblReports);
		
		panel_11 = new JPanel();
		panel_11.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_11.setBackground(new Color(139, 153, 122));
		panel_11.setBounds(511, 304, 495, 27);
		getContentPane().add(panel_11);
		
		entrySelective = new JRadioButton("Selective");
		entrySelective.setSelected(true);
		entrySelective.setBounds(20, 335, 109, 23);
		getContentPane().add(entrySelective);
		
		entryAll = new JRadioButton("All");
		entryAll.setBounds(138, 335, 109, 23);
		getContentPane().add(entryAll);
		
		
		//Group the radio buttons.
	    ButtonGroup entryGrp = new ButtonGroup();
	    entryGrp.add(entrySelective);
	    entryGrp.add(entryAll);
		
		
		
		reportAll = new JRadioButton("All");
		reportAll.setBounds(639, 335, 109, 23);
		getContentPane().add(reportAll);
		
		reportSelective = new JRadioButton("Selective");
		reportSelective.setSelected(true);
		reportSelective.setBounds(521, 335, 109, 23);
		getContentPane().add(reportSelective);
		
		//Group the radio buttons.
	    ButtonGroup reportGrp = new ButtonGroup();
	    reportGrp.add(reportSelective);
	    reportGrp.add(reportAll);
		
	    
	    entrySelective.addActionListener(this);
	    entryAll.addActionListener(this);
	    entrySelective.setActionCommand("entrySelective");
	    entryAll.setActionCommand("entryAll");
	    
	    
	    reportSelective.addActionListener(this);
	    reportAll.addActionListener(this);
	    reportSelective.setActionCommand("reportSelective");
	    reportAll.setActionCommand("reportAll");
	    
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_4.setBounds(10, 330, 495, 35);
		getContentPane().add(panel_4);
	    
	    
		JPanel panel_10 = new JPanel();
		panel_10.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_10.setBounds(511, 330, 495, 35);
		getContentPane().add(panel_10);
		
		
		/////////////////////////////////////////////////
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_3.setBounds(10, 11, 635, 144);
		getContentPane().add(panel_3);

		
	
		// / setting default focus request
		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) 
			{
			 
					//party_code.requestFocus();
				salePack.setSelected(true);
				filterData();
				reportTableSelection(false);
				entryTableSelection(false);
				entrySelective.setSelected(true);
				reportSelective.setSelected(true);		
				
			}
			
			public void windowClosed(WindowEvent e) {
				clearAll();
			}
		});

		// setAlwaysOnTop(true);
	 

	}

	 
	

  

	

	public void clearAll() 
	{
		prefex.setSelectedIndex(0);
		fname.setText("");
		lname.setText("");
		designation.setText("");
		department.setText("");
		emailId.setText("");
		username.setText("");
		password.setText("");
		cpassword.setText("");
		isActivated.setSelectedIndex(0);
		
		
		salePack.setSelected(true);
		
		branchTableModel.getDataVector().removeAllElements();
		branchTableModel.fireTableDataChanged();

		tempReportVector.removeAllElements();
		tempEntryVector.removeAllElements();

		EntryTableModel.getDataVector().removeAllElements();
		EntryTableModel.fireTableDataChanged();

		ReportTableModel.getDataVector().removeAllElements();
		ReportTableModel.fireTableDataChanged();


		//fill default branch table..
		fillTable();
		filterData();
		reportTableSelection(false);
		entryTableSelection(false);
		entrySelective.setSelected(true);
		reportSelective.setSelected(true);	
		
	}

	public void fillTable()
	{
		branchTableModel.getDataVector().removeAllElements();
		Vector<?> v = new UserDAO().getBranch(99);
		ContractMastDto brnDt = null;
		int s =v.size()-1; 
		for(int i =0;i<s;i++)
		{
			brnDt =(ContractMastDto) v.get(i);
			branchTableModel.addRow(new Object[]{false,brnDt.getCmp_code(),brnDt.getCmp_name(),brnDt});
		} 
	}
	
	
	public void actionPerformed(ActionEvent e) 
	{
		
		String command = e.getActionCommand();
		
		//Entries
		if(command.equalsIgnoreCase("entrySelective"))
		{
			entryTableSelection(false);
		}
		if(command.equalsIgnoreCase("entryAll"))
		{
			entryTableSelection(true);
		}
		
		//Reports
		if(command.equalsIgnoreCase("reportSelective"))
		{
			 
			reportTableSelection(false);
			
		}
		if(command.equalsIgnoreCase("reportAll"))
		{
			reportTableSelection(true);
		}

		//package
		if(command.equalsIgnoreCase("salePack"))
		{
			
			filterData();
			reportTableSelection(false);
			entryTableSelection(false);
			entrySelective.setSelected(true);
			reportSelective.setSelected(true);
		}
		//TODO
		//submit
		if(command.equalsIgnoreCase("saveButton"))
		{
			boolean check=false;
			LoginDto loginDto = validatePersonalInfo();
			
			if(loginDto!=null)
			{

				Object[] branchResponse = validateBranchSelection();
				if((Boolean)branchResponse[0])
				{
					loginDto.setBranchList((List<ContractMastDto>)branchResponse[1]);
					check=true;
					
				}
				else
				{
					JOptionPane.showMessageDialog(this, "Please Select Branch");
					check=false;
				}
				
				 
				
				
				Object[] reportResponse = validateReportSelection();
				if((Boolean)reportResponse[0])
				{
					loginDto.setReportList((List)reportResponse[1]);
					loginDto.setPackageList(packageList());
					
					System.out.println("packList "+loginDto.getPackageList());
					System.out.println("divList "+loginDto.getDivisionList());
					
					int userId= userDao.createNewUser(loginDto);
					if(userId!=0)
					{
						JOptionPane.showMessageDialog(this, "User Created Successfully");
						clearAll();
						
					}
				}
				else
				{
					if(check)
					{
						JOptionPane.showMessageDialog(this, "Select entry/report");
					}
				}
			}
			
			
		}
		
		
	     
	}
	
	
	
	public void filterData()
	{
		
		tempReportVector.removeAllElements();
        tempEntryVector.removeAllElements();
		
		Vector<?> v = reportVector;
		UserDto userDto = null;
		int s =v.size(); 
		for(int i =0;i<s;i++)
		{
			userDto =(UserDto) v.get(i);
			
			if(salePack.isSelected())
			{
				if(userDto.getPack_code()==1)
				{
					tempReportVector.add(userDto);
				}
			}
			 
		} 
        
		Vector<?> v1 = entryVector;
		UserDto userDto1 = null;
		int s1 =v1.size(); 
		for(int i =0;i<s1;i++)
		{
			userDto1 =(UserDto) v1.get(i);
			
			if(salePack.isSelected())
			{
				if(userDto1.getPack_code()==1)
				{
					tempEntryVector.add(userDto1);
				}
			}
			
		} 
		
		
		
	}
	
	public List packageList()
	{
		List packList=new ArrayList();

		if(salePack.isSelected())
			packList.add(1);

		return packList; 
	}
	
	
 
	
	
	public void entryTableSelection(boolean res)
	{
		EntryTableModel.getDataVector().removeAllElements();
		EntryTableModel.fireTableDataChanged();
		
		Vector v = tempEntryVector;
		int size= v.size();
		UserDto userDto = null;
		
		for(int i =0;i<size;i++)
		{
			userDto =(UserDto) v.get(i);
			EntryTableModel.addRow(new Object[]{res,userDto.getPack_name(),userDto.getTab_name(),userDto.getMenu_name(),userDto});
		}
	}
	
	
	public void reportTableSelection(boolean res)
	{
		ReportTableModel.getDataVector().removeAllElements();
		ReportTableModel.fireTableDataChanged();

		Vector v = tempReportVector;
		int size= v.size();
		UserDto userDto = null;
		
		for(int i =0;i<size;i++)
		{
			userDto =(UserDto) v.get(i);
			ReportTableModel.addRow(new Object[]{res,userDto.getPack_name(),userDto.getTab_name(),userDto.getMenu_name(),userDto});
		}
	}
	
	public LoginDto validatePersonalInfo()
	{
		if(fname.getText().trim().length()==0)
		{
			JOptionPane.showMessageDialog(this, "First Name cannot be blank");
			fname.requestFocus();
			return null;
		}
		if(lname.getText().trim().length()==0)
		{
			JOptionPane.showMessageDialog(this, "Last Name cannot be blank");
			lname.requestFocus();
			return null;
		}
		if(designation.getText().trim().length()==0)
		{
			JOptionPane.showMessageDialog(this, "Designation cannot be blank");
			designation.requestFocus();
			return null;
		}
		if(department.getText().trim().length()==0)
		{
			JOptionPane.showMessageDialog(this, "Department cannot be blank");
			department.requestFocus();
			return null;
		}
		if(emailId.getText().trim().length()==0)
		{
			JOptionPane.showMessageDialog(this, "Email Id cannot be blank");
			emailId.requestFocus();
			return null;
		}
		if(username.getText().trim().length()==0)
		{
			JOptionPane.showMessageDialog(this, "Username cannot be blank");
			username.requestFocus();
			return null;
		}
		if(password.getText().trim().length()==0)
		{
			JOptionPane.showMessageDialog(this, "Password cannot be blank");
			password.requestFocus();
			return null;
		}
		if(cpassword.getText().trim().length()==0)
		{
			JOptionPane.showMessageDialog(this, "Confirm password cannot be blank");
			cpassword.requestFocus();
			return null;
		}
		if(!password.getText().trim().equals(cpassword.getText().trim()))
		{
			JOptionPane.showMessageDialog(this, "Password and confirm password mismatch!");
			return null;
		}
		
		LoginDto loginDto=new LoginDto();
		loginDto.setPrefix(prefex.getSelectedIndex()==0 ? "Mr." : "Ms.");
		loginDto.setFname(fname.getText());
		loginDto.setLname(lname.getText());
		loginDto.setDesignation(designation.getText());
		loginDto.setDepartment(department.getText());
		loginDto.setEmaiId(emailId.getText());
		loginDto.setLogin_name(username.getText());
		loginDto.setLogin_pass(password.getText());
		loginDto.setIsActive(isActivated.getSelectedIndex()==0 ? "Y" : "N");


		return loginDto;

	}
 
	
	
	
	
	public Object[] validateBranchSelection()
	{
		
		Object[] response=new Object[2];

		Vector col = null;
		List<ContractMastDto> dataList = new ArrayList<ContractMastDto>();
		Vector data = branchTableModel.getDataVector();
		int size=0;
		size=data.size();
		boolean selection=false;
		ContractMastDto brnDto=null;
		for (int i = 0; i < size; i++) 
		{
			col = (Vector) data.get(i);
			brnDto = (ContractMastDto)col.get(3);
			
			if ((Boolean) col.get(0)) 
			{
				brnDto.setStatus("Y");		
				selection=true;
				dataList.add(brnDto);//return branchdto object

			}
/*			else
			{
				brnDto.setStatus("N");
			}
			
			dataList.add(brnDto);//return branchdto object
*/		}
		
		response[1]=dataList;
		response[0]=selection;
		
		
		return response;
	}
	
	
	public Object[] validateReportSelection()
	{
		Object[] response=new Object[2];
		Vector col = null;
		ArrayList dataList = new ArrayList();
		boolean selection=false;
		int size=0;
		Vector data =null;
		
		
		data = ReportTableModel.getDataVector();
		size=data.size();
		for (int i = 0; i < size; i++) 
		{
			col = (Vector) data.get(i);
			
			UserDto udt = (UserDto)col.get(4);
			if ((Boolean) col.get(0)) 
			{
				udt.setStatus("Y");
				selection=true;
				dataList.add(udt);//return UserDTO object
			}
/*			else
				udt.setStatus("N");
			
			dataList.add(udt);//return UserDTO object
*/			
		}
		
		data = EntryTableModel.getDataVector();
		size=data.size();
		
		for (int i = 0; i < size; i++) 
		{
			col = (Vector) data.get(i);
			UserDto udt = (UserDto)col.get(4);
			if ((Boolean) col.get(0)) 
			{
				udt.setStatus("Y");
				selection=true;
				dataList.add(udt);//return UserDTO object
			}
/*			else
				udt.setStatus("N");
			
			dataList.add(udt);//return UserDTO object
*/		}
		
		System.out.println("Selected Report count is "+dataList.size());
		
		response[1]=dataList;
		response[0]=selection;
		
		
		return response;
	}
	
 
	 
}// / class end////////////////




