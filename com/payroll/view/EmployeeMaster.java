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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.payroll.dao.EmployeeDAO;
import com.payroll.dto.BankDto;
import com.payroll.dto.EmployeeMastDto;
import com.payroll.print.EmployeeList;
import com.payroll.util.JDoubleField;
import com.payroll.util.TableDataSorter;
import com.payroll.util.TextField;

public class EmployeeMaster extends BaseClass implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	private JComboBox bankList;
	private JScrollPane  bankdPane;

	private JLabel label_12;
	private JLabel branch;
	private JLabel lblDispatchEntry;
	private JPanel panel_2;
	private JButton exitButton;
	private JButton btnSave,btnAdd,btnExcel,btnResign,btnResignation,btnRefresh;
	private JComboBox labelprint,emp_status;
	private JLabel lblGroupCode;
	private JLabel lblAcCode,lblEsiCode,lblUanNo;
	private JTextField mac_code,esic_no,uan_no;
	private JLabel lblName,lblFatherName;
	private JLabel lblAddress;
	private JTextField madd1,fname;
	private JTextField emp_name,dsCombo;
	private JTextField mobile;
	private JLabel lblMobileNo;
	private JTextField mphone;
	private JLabel lblPhone;
	private JLabel lblCity;
	private JLabel lblAddress_1;
	private JTextField madd3;
	private JTextField mcity;
	private JLabel lblEmail;
	private JTextField memail;
	private JLabel lblGross,lblBasic,lblDa,lblHra,lblAdd_hra,lblIncentive,lblSpl_incentive,lblLta,lblMedical,lblBonus,lblOtRate,lblSt_allow,lblBonusper,lblBonuschk;
	private  JDoubleField gross,basic,da,hra,add_hra,incentive,spl_incentive,lta,medical,bonus,ot_rate,st_allow,bonus_per,bonus_limit,bonus_check;
	private JLabel lblPinCode,lblPanNo,lblAcNo,lblBankName,lblAddress_2,lblIfscCode;
	private JTextField mpin,pan_no,bank_accno,bank_name,bank_add1,ifsc_code;
	private JTextField madd2;
	private JPanel panel_4;
	private JPanel panel_5;
	private JLabel lblName_1;
	private JFormattedTextField party_name;
	private JScrollPane scrollPane;
	private JPanel panel_6,panel_7,panel_8;
	private JLabel lblSearch,lblPdetail,lblSdetail;
	private Font fontPlan,font;
	NumberFormat formatter;
	SimpleDateFormat sdf;
	String option=null;
	private JTextField mstate;
	private JTextField emp_code;
	private JFormattedTextField dobirth;
	private JFormattedTextField dojoin;
	private JFormattedTextField doresign;
	private DefaultTableModel emplyeeTableModel;
	private JTable emplyeeTable;
	private EmployeeDAO empDao;
	private TableModel myTableModel;
	private TableRowSorter<TableModel> sorter; 
	int stcd,arcd,rgcd,trcd,msrcd,lvlcd;
	private EmployeeMastDto empDto;
	Vector data;
	String repNm;;
	int repno;
	public EmployeeMaster(String nm,String repNm)
	{
		setResizable(false);
		setSize(1024, 768);		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

		this.repNm=repNm;
		option="";
		formatter = new DecimalFormat("0.00");     // Decimal Value format
		sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.setLenient(true);
		
		empDao = new EmployeeDAO();
		
		lblFinancialAccountingYear = new JLabel(loginDt.getFooter());
		lblFinancialAccountingYear.setForeground(Color.BLACK);
		lblFinancialAccountingYear.setBounds(400, 672, 477, 15);
		getContentPane().add(lblFinancialAccountingYear);

		fontPlan =new Font("Tahoma", Font.PLAIN, 11);
		font = new Font("Tahoma", Font.BOLD, 11);

		JLabel promleb = new JLabel(promLogo);
		promleb.setBounds(10, 670, 35, 35);
		getContentPane().add(promleb);

//		JLabel arisleb = new JLabel(arisLogo);
//		arisleb.setBounds(10, 11, 35, 37);
//		getContentPane().add(arisleb);

		label_12 = new JLabel((Icon) null);
		label_12.setBounds(10, 649, 35, 35);
		getContentPane().add(label_12);

		branch = new JLabel(loginDt.getBrnnm());
		branch.setForeground(Color.BLACK);
		branch.setFont(new Font("Tahoma", Font.BOLD, 11));
		branch.setBounds(10, 86, 195, 14);
		getContentPane().add(branch);

		lblDispatchEntry = new JLabel(repNm);
		lblDispatchEntry.setHorizontalAlignment(SwingConstants.CENTER);
		lblDispatchEntry.setForeground(Color.BLACK);
		lblDispatchEntry.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblDispatchEntry.setBounds(517, 17, 251, 22);
		getContentPane().add(lblDispatchEntry);


		btnAdd = new JButton("Add");
		btnAdd.setMnemonic(KeyEvent.VK_A);
		btnAdd.setBounds(704, 616, 86, 30);
		getContentPane().add(btnAdd);
		
		
		btnExcel = new JButton("Excel");
		btnExcel.setMnemonic(KeyEvent.VK_E);
		btnExcel.setBounds(263, 616, 86, 30);
		getContentPane().add(btnExcel);


		btnResign = new JButton("Resign List");
		btnResign.setMnemonic(KeyEvent.VK_R);
		btnResign.setBounds(358, 616, 100, 30);
		getContentPane().add(btnResign);

		btnResignation = new JButton("Resignation");
		btnResignation.setMnemonic(KeyEvent.VK_G);
		btnResignation.setBounds(465, 616, 105, 30);
		getContentPane().add(btnResignation);
		
		btnRefresh = new JButton("Refresh");
		btnRefresh.setMnemonic(KeyEvent.VK_F);
		btnRefresh.setBounds(465, 616, 105, 30);
		getContentPane().add(btnRefresh);

		btnSave= new JButton("Save");
		btnSave.setBounds(799, 616, 86, 30);
		getContentPane().add(btnSave);
		btnSave.addKeyListener(new KeyAdapter() 
		{
			public void keyPressed(KeyEvent evt) 
			{
				if(evt.getKeyCode() == KeyEvent.VK_ENTER)
				{
					saveData();
				}
			}
		});
		
		
		
		exitButton = new JButton("Exit");
		exitButton.setBounds(891, 616, 86, 30);
		getContentPane().add(exitButton);
		
		
		dsCombo = new JTextField();
		dsCombo.setName("0");
		dsCombo.setBounds(393, 60, 153, 22);
		getContentPane().add(dsCombo);
		dsCombo.addKeyListener(new KeyAdapter() 
		{
			public void keyPressed(KeyEvent evt) 
			{
				if(evt.getKeyCode() == KeyEvent.VK_ENTER)
				{
					emp_code.requestFocus();
					emp_code.setSelectionStart(0);
				}
			}
		});
		
		lblGroupCode = new JLabel("Designation :");
		lblGroupCode.setBounds(279, 61, 171, 20);
		getContentPane().add(lblGroupCode);

		lblAcCode = new JLabel("PF:");
		lblAcCode.setBounds(648, 89, 65, 20);
		getContentPane().add(lblAcCode);

		mac_code = new TextField(10);
		mac_code.setBounds(687, 88, 95, 22);
		getContentPane().add(mac_code);

		
		lblEsiCode = new JLabel("ESIC:");
		lblEsiCode.setBounds(798, 89, 65, 20);
		getContentPane().add(lblEsiCode);

		esic_no = new TextField(10);
		esic_no.setBounds(860, 88, 116, 22);
		getContentPane().add(esic_no);


		lblUanNo = new JLabel("UAN:");
		lblUanNo.setBounds(798, 117, 65, 20);
		getContentPane().add(lblUanNo);

		uan_no = new TextField(12);
		uan_no.setBounds(860, 116, 116, 22);
		getContentPane().add(uan_no);

		
		lblName = new JLabel("Name:");
		lblName.setBounds(279, 148, 171, 20);
		getContentPane().add(lblName);

		lblFatherName = new JLabel("Father's Name:");
		lblFatherName.setBounds(279, 175, 171, 20);
		getContentPane().add(lblFatherName);
		
		lblAddress = new JLabel("Address 1:");
		lblAddress.setBounds(279, 205, 171, 20);
		getContentPane().add(lblAddress);

		fname = new TextField(30);
		fname.setBounds(393, 175, 277, 22);
		getContentPane().add(fname);

		
		madd1 = new TextField(40);
		madd1.setBounds(393, 204, 277, 22);
		getContentPane().add(madd1);

		madd2 = new TextField(40);
		madd2.setBounds(695, 204, 277, 22);
		getContentPane().add(madd2);

		madd3 = new TextField(40);
		madd3.setBounds(393, 232, 277, 22);
		getContentPane().add(madd3);

		mcity = new TextField(25);
		mcity.setBounds(819, 232, 153, 22);
		getContentPane().add(mcity);


		emp_name = new TextField(30);
		emp_name.setBounds(393, 147, 277, 22);
		getContentPane().add(emp_name);

		mobile = new TextField(21);
		mobile.setBounds(819, 293, 153, 22);
		getContentPane().add(mobile);

		lblMobileNo = new JLabel("Mobile No:");
		lblMobileNo.setBounds(695, 293, 84, 20);
		getContentPane().add(lblMobileNo);

		mphone = new TextField(20);
		mphone.setBounds(393, 293, 153, 22);
		getContentPane().add(mphone);

		lblPhone = new JLabel("Phone:");
		lblPhone.setBounds(279, 293, 171, 20);
		getContentPane().add(lblPhone);

		lblCity = new JLabel("City:");
		lblCity.setBounds(695, 233, 101, 20);
		getContentPane().add(lblCity);

		lblAddress_1 = new JLabel("Address 3:");
		lblAddress_1.setBounds(279, 233, 171, 20);
		getContentPane().add(lblAddress_1);

		lblEmail = new JLabel("Email:");
		lblEmail.setBounds(279, 320, 171, 20);
		getContentPane().add(lblEmail);

		memail = new TextField(100);
		memail.setBounds(393, 320, 277, 22);
		getContentPane().add(memail);

		

		lblGross = new JLabel("Gross");
		lblGross.setBounds(487, 518, 89, 20);
		getContentPane().add(lblGross);

		gross = new JDoubleField();
		gross.setHorizontalAlignment(SwingConstants.RIGHT);
		gross.setMaxLength(10); //Set maximum length             
		gross.setPrecision(2); //Set precision (1 in your case)              
		gross.setAllowNegative(false); //Set false to disable negatives
		gross.setBounds(584, 518, 86, 22);
		getContentPane().add(gross);

		
		lblBasic = new JLabel("Basic");
		lblBasic.setBounds(279, 433, 171, 20);
		getContentPane().add(lblBasic);

		basic = new JDoubleField();
		basic.setHorizontalAlignment(SwingConstants.RIGHT);
		basic.setMaxLength(10); //Set maximum length             
		basic.setPrecision(2); //Set precision (1 in your case)              
		basic.setAllowNegative(false); //Set false to disable negatives
		basic.setBounds(393, 432, 86, 22);
		getContentPane().add(basic);


		lblDa = new JLabel("DA");
		lblDa.setBounds(279, 461, 171, 20);
		getContentPane().add(lblDa);

		da = new JDoubleField();
		da.setHorizontalAlignment(SwingConstants.RIGHT);
		da.setMaxLength(10); //Set maximum length             
		da.setPrecision(2); //Set precision (1 in your case)              
		da.setAllowNegative(false); //Set false to disable negatives
		da.setBounds(393, 462, 86, 22);
		getContentPane().add(da);


		lblHra = new JLabel("HRA");
		lblHra.setBounds(279, 491, 171, 20);
		getContentPane().add(lblHra);

		hra = new JDoubleField();
		hra.setHorizontalAlignment(SwingConstants.RIGHT);
		hra.setMaxLength(10); //Set maximum length             
		hra.setPrecision(2); //Set precision (1 in your case)              
		hra.setAllowNegative(false); //Set false to disable negatives
		hra.setBounds(393, 490, 86, 22);
		getContentPane().add(hra);


		lblAdd_hra = new JLabel("Add. HRA");
		lblAdd_hra.setBounds(279, 517, 171, 20);
		getContentPane().add(lblAdd_hra);

		add_hra = new JDoubleField();
		add_hra.setHorizontalAlignment(SwingConstants.RIGHT);
		add_hra.setMaxLength(10); //Set maximum length             
		add_hra.setPrecision(2); //Set precision (1 in your case)              
		add_hra.setAllowNegative(false); //Set false to disable negatives
		add_hra.setBounds(393, 518, 86, 22);
		getContentPane().add(add_hra);

		
		lblIncentive = new JLabel("Incentive");
		lblIncentive.setBounds(279, 545, 171, 20);
		getContentPane().add(lblIncentive);

		incentive = new JDoubleField();
		incentive.setHorizontalAlignment(SwingConstants.RIGHT);
		incentive.setMaxLength(10); //Set maximum length             
		incentive.setPrecision(2); //Set precision (1 in your case)              
		incentive.setAllowNegative(false); //Set false to disable negatives
		incentive.setBounds(393, 546, 86, 22);
		getContentPane().add(incentive);
		
		
		lblSpl_incentive = new JLabel("Spl. Incentive");
		lblSpl_incentive.setBounds(279, 573, 89, 20);
		getContentPane().add(lblSpl_incentive);

		spl_incentive = new JDoubleField();
		spl_incentive.setHorizontalAlignment(SwingConstants.RIGHT);
		spl_incentive.setMaxLength(10); //Set maximum length             
		spl_incentive.setPrecision(2); //Set precision (1 in your case)              
		spl_incentive.setAllowNegative(false); //Set false to disable negatives
		spl_incentive.setBounds(393, 574, 86, 22);
		getContentPane().add(spl_incentive);
		

		lblLta = new JLabel("LTA");
		lblLta.setBounds(487, 433, 89, 20);
		getContentPane().add(lblLta);

		lta = new JDoubleField();
		lta.setHorizontalAlignment(SwingConstants.RIGHT);
		lta.setMaxLength(10); //Set maximum length             
		lta.setPrecision(2); //Set precision (1 in your case)              
		lta.setAllowNegative(false); //Set false to disable negatives
		lta.setBounds(584, 432, 86, 22);
		getContentPane().add(lta);


		lblMedical = new JLabel("Medical");
		lblMedical.setBounds(487, 461, 89, 20);
		getContentPane().add(lblMedical);

		medical = new JDoubleField();
		medical.setHorizontalAlignment(SwingConstants.RIGHT);
		medical.setMaxLength(10); //Set maximum length             
		medical.setPrecision(2); //Set precision (1 in your case)              
		medical.setAllowNegative(false); //Set false to disable negatives
		medical.setBounds(584, 462, 86, 22);
		getContentPane().add(medical);

		
		lblBonus = new JLabel("Bonus");
		lblBonus.setBounds(487, 491, 89, 20);
		getContentPane().add(lblBonus);

		bonus = new JDoubleField();
		bonus.setHorizontalAlignment(SwingConstants.RIGHT);
		bonus.setMaxLength(10); //Set maximum length             
		bonus.setPrecision(2); //Set precision (1 in your case)              
		bonus.setAllowNegative(false); //Set false to disable negatives
		bonus.setBounds(584, 490, 86, 22);
		getContentPane().add(bonus);
		
		lblOtRate = new JLabel("OT Rate");
		lblOtRate.setBounds(487, 545, 89, 20);
		getContentPane().add(lblOtRate);

		ot_rate = new JDoubleField();
		ot_rate.setHorizontalAlignment(SwingConstants.RIGHT);
		ot_rate.setMaxLength(10); //Set maximum length             
		ot_rate.setPrecision(2); //Set precision (1 in your case)              
		ot_rate.setAllowNegative(false); //Set false to disable negatives
		ot_rate.setBounds(584, 546, 86, 22);
		getContentPane().add(ot_rate);
		
		lblSt_allow = new JLabel("Sterile Rate");
		lblSt_allow.setBounds(487, 573, 89, 20);
		getContentPane().add(lblSt_allow);

		st_allow = new JDoubleField();
		st_allow.setHorizontalAlignment(SwingConstants.RIGHT);
		st_allow.setMaxLength(10); //Set maximum length             
		st_allow.setPrecision(2); //Set precision (1 in your case)              
		st_allow.setAllowNegative(false); //Set false to disable negatives
		st_allow.setBounds(584, 574, 86, 22);
		getContentPane().add(st_allow);
		
		lblPinCode = new JLabel("Pin Code:");
		lblPinCode.setBounds(695, 261, 67, 20);
		getContentPane().add(lblPinCode);

		mpin = new TextField(10);
		mpin.setBounds(819, 261, 153, 22);
		getContentPane().add(mpin);


		lblPanNo = new JLabel("Pan No:");
		lblPanNo.setBounds(695, 320, 67, 20);
		getContentPane().add(lblPanNo);

		pan_no = new TextField(10);
		pan_no.setBounds(819, 320, 153, 22);
		getContentPane().add(pan_no);
		
		panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_2.setBackground(SystemColor.activeCaptionBorder);
		panel_2.setBounds(367, 657, 609, 48);
		getContentPane().add(panel_2);

		lblName_1 = new JLabel("Name:");
		
		lblName_1.setBounds(22, 161, 83, 20);
		getContentPane().add(lblName_1);

		party_name = new JFormattedTextField();
		party_name.setBounds(110, 161, 130, 22);
		getContentPane().add(party_name);
		 
		
		lblAcNo = new JLabel("A/c No");
		lblAcNo.setBounds(695, 434, 101, 20);
		getContentPane().add(lblAcNo);
		
		lblBankName = new JLabel("Bank Name");
		lblBankName.setBounds(695, 462, 101, 20);
		getContentPane().add(lblBankName);
		
		lblAddress_2 = new JLabel("Address");
		lblAddress_2.setBounds(695, 492, 101, 20);
		getContentPane().add(lblAddress_2);
		
		lblIfscCode = new JLabel("IFSC Code");
		lblIfscCode.setBounds(695, 518, 101, 20);
		getContentPane().add(lblIfscCode);
		
		bank_accno = new TextField(20);
		bank_accno.setName("9");
		bank_accno.setBounds(824, 430, 153, 22);
		getContentPane().add(bank_accno);
		
/*		bank_name = new TextField(20);
		bank_name.setName("11");
		bank_name.setBounds(824, 459, 153, 22);
		getContentPane().add(bank_name);
		
*/		
		
		bankList = new JComboBox(loginDt.getBankList());
		bankList.setName("0");
		bankList.setBounds(824, 459, 153, 22);
		getContentPane().add(bankList);
		

		
		
		bank_add1 = new TextField(20);
		bank_add1.setName("13");
		bank_add1.setBounds(824, 491, 153, 22);
		getContentPane().add(bank_add1);
		
		ifsc_code = new TextField(20);
		ifsc_code.setName("15");
		ifsc_code.setBounds(824, 518, 153, 22);
		getContentPane().add(ifsc_code);
		
		lblBonusper = new JLabel("Bonus % & Limit");
		lblBonusper.setBounds(695, 545, 119, 20);
		getContentPane().add(lblBonusper);

		bonus_per = new JDoubleField();
		bonus_per.setHorizontalAlignment(SwingConstants.RIGHT);
		bonus_per.setMaxLength(10); //Set maximum length             
		bonus_per.setPrecision(2); //Set precision (1 in your case)              
		bonus_per.setAllowNegative(false); //Set false to disable negatives
		bonus_per.setBounds(824, 546, 56, 22);
		getContentPane().add(bonus_per);
		
		bonus_limit = new JDoubleField();
		bonus_limit.setHorizontalAlignment(SwingConstants.RIGHT);
		bonus_limit.setMaxLength(10); //Set maximum length             
		bonus_limit.setPrecision(2); //Set precision (1 in your case)              
		bonus_limit.setAllowNegative(false); //Set false to disable negatives
		bonus_limit.setBounds(891, 546, 86, 22);
		getContentPane().add(bonus_limit);

		
		lblBonuschk = new JLabel("Bonus Calculation");
		lblBonuschk.setBounds(695, 574, 119, 20);
		getContentPane().add(lblBonuschk);

		bonus_check = new JDoubleField();
		bonus_check.setHorizontalAlignment(SwingConstants.RIGHT);
		bonus_check.setMaxLength(10); //Set maximum length             
		bonus_check.setPrecision(2); //Set precision (1 in your case)              
		bonus_check.setAllowNegative(false); //Set false to disable negatives
		bonus_check.setBounds(824, 575, 153, 22);
		getContentPane().add(bonus_check);

		
		////////////// invoce no table model/////////////////////
		String [] partyColName=	{"Code.", "Employee Name",""};
		String datax[][]={{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{}};
		emplyeeTableModel=  new DefaultTableModel(datax,partyColName)
		{
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) 
			{
				return false;
			}
		};

		emplyeeTable = new JTable(emplyeeTableModel);
		emplyeeTable.setFont(fontPlan);
		emplyeeTable.setColumnSelectionAllowed(false);
		emplyeeTable.setCellSelectionEnabled(false);
		emplyeeTable.getTableHeader().setReorderingAllowed(false);
		emplyeeTable.getTableHeader().setResizingAllowed(false);
		emplyeeTable.getTableHeader().setFont(font);
		emplyeeTable.setRowHeight(20);
		emplyeeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//		emplyeeTable.getTableHeader().setPreferredSize(new Dimension(25,25));
		///////////////////////////////////////////////////////////////////////////
		emplyeeTable.getColumnModel().getColumn(0).setPreferredWidth(65);   //contact inv no
		emplyeeTable.getColumnModel().getColumn(1).setPreferredWidth(280);  //party name/////
		emplyeeTable.getColumnModel().getColumn(2).setMinWidth(0);  //inv_no/////
		emplyeeTable.getColumnModel().getColumn(2).setMaxWidth(0);  //inv_no/////
		
		fillInvTable("");

		scrollPane = new JScrollPane(emplyeeTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(16, 189, 232, 434);
		getContentPane().add(scrollPane);
		
		DefaultTableCellRenderer dtc = new DefaultTableCellRenderer() 
		{
			public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) 
			{
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				if (emplyeeTable.getSelectedRow()==row )
				{
					  c.setBackground(new Color(170, 181, 157));
					  c.setForeground(Color.BLACK); 
				}
				else
				{
					  c.setBackground(Color.WHITE);
					  c.setForeground(Color.BLACK);

				}
		        return c;
			}
		};
		
		emplyeeTable.getColumnModel().getColumn(0).setCellRenderer(dtc);
		emplyeeTable.getColumnModel().getColumn(1).setCellRenderer(dtc);

		

		myTableModel = emplyeeTable.getModel();
        sorter = new TableRowSorter<TableModel>(myTableModel);
        emplyeeTable.setRowSorter(sorter);
		party_name.getDocument().addDocumentListener(TableDataSorter.getTableSorter(party_name, sorter,emplyeeTable, 2,false));

 
		
		emplyeeTable.addKeyListener(new KeyAdapter() 
		{
			public void keyPressed(KeyEvent evt) 
			{
				int viewRow = emplyeeTable.getSelectedRow();
				if(evt.getKeyCode() == KeyEvent.VK_ENTER)
				{
                    if (viewRow < 0) {
                        //Selection got filtered away.
                        //statusText.setText("");
                    } else
                    {
                        int modelRow = emplyeeTable.convertRowIndexToModel(viewRow);

                        empDto = (EmployeeMastDto) myTableModel.getValueAt(modelRow, 2);
    				 	clearAll();
    					fillData(empDto);
    					
                    }
					evt.consume();
				}
				
			}
			
			
			public void keyReleased(KeyEvent evt) 
			{
				int viewRow = emplyeeTable.getSelectedRow();
				if((evt.getKeyCode() == KeyEvent.VK_DOWN ) || (evt.getKeyCode() == KeyEvent.VK_UP ) || (evt.getKeyCode() == KeyEvent.VK_PAGE_UP) || (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN))
				{
                    if (viewRow < 0) {
                        //Selection got filtered away.
                        //statusText.setText("");
                    } else
                    {
                        int modelRow = emplyeeTable.convertRowIndexToModel(viewRow);

                        empDto = (EmployeeMastDto) myTableModel.getValueAt(modelRow, 2);
    				 	clearAll();
    					fillData(empDto);

    					
                    }

					evt.consume();
				}
				
			}
			
			
		});		
		
		emplyeeTable.addMouseListener(new MouseListener() 
		{
			public void mouseReleased(MouseEvent e){}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}

			public void mouseClicked(MouseEvent ee) 
			{
				int viewRow = emplyeeTable.getSelectedRow();
				 
				if (viewRow < 0) {
                    //Selection got filtered away.
                    //statusText.setText("");
                } else {
                    
                    int modelRow = emplyeeTable.convertRowIndexToModel(viewRow);

                    empDto = (EmployeeMastDto) myTableModel.getValueAt(modelRow, 2);
				 	clearAll();
					fillData(empDto);

					
                } 
				
 
				 
				ee.consume();
			}
		});
		
		
		
		

		panel_6 = new JPanel();
		panel_6.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_6.setBackground(new Color(139, 153, 122));
		panel_6.setBounds(10, 126, 243, 28);
		getContentPane().add(panel_6);

		lblSearch = new JLabel("Search");
		panel_6.add(lblSearch);
		lblSearch.setForeground(Color.WHITE);
		lblSearch.setFont(new Font("Tahoma", Font.BOLD, 12));

		panel_7 = new JPanel();
		panel_7.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_7.setBackground(new Color(139, 153, 122));
		panel_7.setBounds(279, 115, 180, 27);
		getContentPane().add(panel_7);

		lblPdetail = new JLabel("Personal Details");
		panel_7.add(lblPdetail);
		lblPdetail.setForeground(Color.WHITE);
		lblPdetail.setFont(new Font("Tahoma", Font.BOLD, 12));


		panel_8 = new JPanel();
		panel_8.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_8.setBackground(new Color(139, 153, 122));
		panel_8.setBounds(279, 394, 180, 27);
		getContentPane().add(panel_8);

		lblSdetail = new JLabel("Salary Detail");
		panel_8.add(lblSdetail);
		lblSdetail.setForeground(Color.WHITE);
		lblSdetail.setFont(new Font("Tahoma", Font.BOLD, 12));

		
		
		panel_5 = new JPanel();
		panel_5.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_5.setBounds(10, 126, 243, 508);
		getContentPane().add(panel_5);
			

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
						//mac_code.requestFocus();
						//mac_code.setSelectionStart(0);
						break;
					case 2:
						esic_no.requestFocus();
						esic_no.setSelectionStart(0);
						break;
					case 3:
						emp_name.requestFocus();
						emp_name.setSelectionStart(0);
						break;
					case 4:
						fname.requestFocus();
						fname.setSelectionStart(0);
						break;
					case 5:
						madd1.requestFocus();
						madd1.setSelectionStart(0);
						break;
					case 6:
						madd2.requestFocus();
						madd2.setSelectionStart(0);
						break;
					case 7:
						madd3.requestFocus();
						madd3.setSelectionStart(0);
						break;
					case 8:
						mcity.requestFocus();
						mcity.setSelectionStart(0);
						break;
					case 9:
				 		mstate.requestFocus();
						mstate.setSelectionStart(0);
						break;
					case 10:
						mpin.requestFocus();
						mpin.setSelectionStart(0);
						break;
					case 11:
				 		mphone.requestFocus();
						mphone.setSelectionStart(0);
						break;
					case 12:
				 		mobile.requestFocus();
						mobile.setSelectionStart(0);
						break;
					case 13:
				 		memail.requestFocus();
						memail.setSelectionStart(0);
						break;
					case 14:
				 		pan_no.requestFocus();
						pan_no.setSelectionStart(0);
						break;
					case 15:
						dobirth.requestFocus();
						dobirth.setSelectionStart(0);
						break;
					case 16:
						if(isValidDate(dobirth.getText()) || isValidBlankDate(dobirth.getText()))
						{
							dojoin.requestFocus();
							dojoin.setSelectionStart(0);
						}
						else
						{
							dobirth.requestFocus();
							dobirth.setValue(null);
							checkDate(dobirth);
							dobirth.setSelectionStart(0);
						}
						break;
					case 17:
						if(isValidDate(dojoin.getText()) || isValidBlankDate(dojoin.getText()))
						{
							doresign.requestFocus();
							doresign.setSelectionStart(0);
						}
						else
						{
							dojoin.requestFocus();
							dojoin.setValue(null);
							checkDate(dojoin);
							dojoin.setSelectionStart(0);
						}
						
						break;
					case 18:
						
						if(isValidDate(doresign.getText()) || isValidBlankDate(doresign.getText()))
						{
							basic.requestFocus();
							basic.setSelectionStart(0);
						}
						else
						{
							doresign.requestFocus();
							doresign.setValue(null);
							checkDate(doresign);
							doresign.setSelectionStart(0);
						}
					
						break;
					case 19:
						calfunction(basic);
						da.requestFocus();
						da.setSelectionStart(0);
						break;
					case 20:
						calfunction(da);
						hra.requestFocus();
						hra.setSelectionStart(0);
						break;
					case 21:
						calfunction(hra);
						add_hra.requestFocus();
						add_hra.setSelectionStart(0);
						break;
					case 22:
						calfunction(add_hra);
						incentive.requestFocus();
						incentive.setSelectionStart(0);
						break;
					case 23:
						calfunction(incentive);
						spl_incentive.requestFocus();
						spl_incentive.setSelectionStart(0);
						break;
					case 24:
						calfunction(spl_incentive);
						lta.requestFocus();
						lta.setSelectionStart(0);
						break;
					case 25:
						calfunction(lta);
						medical.requestFocus();
						medical.setSelectionStart(0);
						break;
					case 26:
						bonus.requestFocus();
						bonus.setSelectionStart(0);
						break;
					case 27:
						gross.requestFocus();
						gross.setSelectionStart(0);
						break;
					case 28:
						ot_rate.requestFocus();
						ot_rate.setSelectionStart(0);
						break;
					case 29:
						st_allow.requestFocus();
						st_allow.setSelectionStart(0);
						break;
					case 30:
						bank_accno.requestFocus();
						bank_accno.setSelectionStart(0);
						break;
					case 31:
						bankList.requestFocus();
						//bank_name.setSelectionStart(0);
						break;
					case 32:
						bank_add1.requestFocus();
						bank_add1.setSelectionStart(0);
						break;
					case 33:
						ifsc_code.requestFocus();
						ifsc_code.setSelectionStart(0);
						break;
					case 34:
						bonus_per.requestFocus();
						bonus_per.setSelectionStart(0);
						break;
					case 35:
						bonus_per.setText(formatter.format(setDoubleNumber((bonus_per.getText()))));
						bonus_limit.requestFocus();
						bonus_limit.setSelectionStart(0);
						break;
					case 36:
						bonus_limit.setText(formatter.format(setDoubleNumber((bonus_limit.getText()))));
						bonus_check.requestFocus();
						bonus_check.setSelectionStart(0);
						break;
					case 37:
						bonus_check.setText(formatter.format(setDoubleNumber((bonus_check.getText()))));
				 		btnSave.requestFocus();
				 		btnSave.setBackground(new Color(139, 153, 122));
						break;
					}
				}
				
				
				if (key == KeyEvent.VK_UP) 
				{
					JTextField textField = (JTextField) keyEvent.getSource();
					int id = Integer.parseInt(textField.getName());
					
					switch (id) 
					{
					case 2:
						emp_code.requestFocus();
						emp_code.setSelectionStart(0);
						break;
					case 3:
						mac_code.requestFocus();
						mac_code.setSelectionStart(0);
						break;
					case 4:
						esic_no.requestFocus();
						esic_no.setSelectionStart(0);
						break;
					case 5:
						emp_name.requestFocus();
						emp_name.setSelectionStart(0);
						break;
					case 6:
						fname.requestFocus();
						fname.setSelectionStart(0);
						break;
					case 7:
						madd1.requestFocus();
						madd1.setSelectionStart(0);
						break;
					case 8:
						madd2.requestFocus();
						madd2.setSelectionStart(0);
						break;
					case 9:
						madd3.requestFocus();
						madd3.setSelectionStart(0);
						break;
					case 10:
						mcity.requestFocus();
						mcity.setSelectionStart(0);
						break;
					case 11:
				 		mstate.requestFocus();
						mstate.setSelectionStart(0);
						break;
					case 12:
						mpin.requestFocus();
						mpin.setSelectionStart(0);
						break;
					case 13:
				 		mphone.requestFocus();
						mphone.setSelectionStart(0);
						break;
					case 14:
				 		mobile.requestFocus();
						mobile.setSelectionStart(0);
						break;
					case 15:
				 		memail.requestFocus();
						memail.setSelectionStart(0);
						break;
					case 16:
				 		pan_no.requestFocus();
						pan_no.setSelectionStart(0);
						break;
					case 17:
						dobirth.requestFocus();
						dobirth.setSelectionStart(0);
						break;
					case 18:
						dojoin.requestFocus();
						dojoin.setSelectionStart(0);
						break;
					case 19:
						doresign.requestFocus();
						doresign.setSelectionStart(0);
						break;
					case 20:
						basic.requestFocus();
						basic.setSelectionStart(0);
						break;
					case 21:
						da.requestFocus();
						da.setSelectionStart(0);
						break;
					case 22:
						hra.requestFocus();
						hra.setSelectionStart(0);
						break;
					case 23:
						add_hra.requestFocus();
						add_hra.setSelectionStart(0);
						break;
					case 24:
						incentive.requestFocus();
						incentive.setSelectionStart(0);
						break;
					case 25:
						spl_incentive.requestFocus();
						spl_incentive.setSelectionStart(0);
						break;
					case 26:
						lta.requestFocus();
						lta.setSelectionStart(0);
						break;
					case 27:
						medical.requestFocus();
						medical.setSelectionStart(0);
						break;
					case 28:
						bonus.requestFocus();
						bonus.setSelectionStart(0);
						break;
					case 29:
						gross.requestFocus();
						gross.setSelectionStart(0);
						break;
					case 30:
						ot_rate.requestFocus();
						ot_rate.setSelectionStart(0);
						break;
					case 31:
						st_allow.requestFocus();
						st_allow.setSelectionStart(0);
						break;
					case 32:
						bank_accno.requestFocus();
						bank_accno.setSelectionStart(0);
						break;
					case 33:
						bankList.requestFocus();
						//bank_name.setSelectionStart(0);
						break;
					case 34:
						bank_add1.requestFocus();
						bank_add1.setSelectionStart(0);
						break;
					case 35:
						ifsc_code.requestFocus();
						ifsc_code.setSelectionStart(0);
						break;
					case 36:
						bonus_per.requestFocus();
						bonus_per.setSelectionStart(0);
						break;
					case 37:
						bonus_limit.requestFocus();
						bonus_limit.setSelectionStart(0);
						break;
					case 38:
						bonus_check.requestFocus();
						bonus_check.setSelectionStart(0);
						break;
					case 39:
				 		btnSave.requestFocus();
				 		btnSave.setBackground(new Color(139, 153, 122));
						break;
					}
				}


				if (key == KeyEvent.VK_ESCAPE) 
				{
					clearAll();
					dispose();
				}
			}

			public void keyReleased(KeyEvent keyEvent) {
			}

			public void keyTyped(KeyEvent keyEvent) {
			}
		};

		// ///////////////////////////////////////////////		
		
		
						
		mstate = new TextField(15);
		mstate.setBounds(393, 261, 153, 22);
		getContentPane().add(mstate);

		JLabel lblState = new JLabel("State:");
		lblState.setBounds(279, 261, 171, 20);
		getContentPane().add(lblState);

		JLabel lblEmpCode = new JLabel("Employee  Code:");
		lblEmpCode.setBounds(279, 90, 171, 20);
		getContentPane().add(lblEmpCode);

		emp_code = new TextField(10);
		emp_code.setBounds(393, 89, 153, 22);
		getContentPane().add(emp_code);

		JLabel lblResignationDt = new JLabel("Resign Dt:");
		lblResignationDt.setBounds(695, 352, 78, 20);
		getContentPane().add(lblResignationDt);

		JLabel lblJoiningDate = new JLabel("Joining Date:");
		lblJoiningDate.setBounds(487, 352, 89, 20);
		getContentPane().add(lblJoiningDate);

		JLabel lblDateOfBirth = new JLabel("Date of Birth:");
		lblDateOfBirth.setBounds(279, 352, 171, 20);
		getContentPane().add(lblDateOfBirth);

		dobirth = new JFormattedTextField(sdf);
		checkDate(dobirth);
		dobirth.setBounds(393, 351, 86, 22);
		getContentPane().add(dobirth);

		dojoin = new JFormattedTextField(sdf);
		checkDate(dojoin);
		dojoin.setBounds(584, 351, 86, 22);
		getContentPane().add(dojoin);

		doresign = new JFormattedTextField(sdf);
		checkDate(doresign);
		doresign.setBounds(819, 351, 153, 22);
		getContentPane().add(doresign);

		
		emp_code.addKeyListener(new KeyAdapter() 
		{
			public void keyPressed(KeyEvent evt) 
			{
				if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) 
				{
					clearAll();
					dispose();
					
				}
				if(evt.getKeyCode() == KeyEvent.VK_ENTER)
				{
					 boolean chk=false;
					
				   if (option.equals("A"))
				   {
					   
						try
						{
							String mac_cd=emp_code.getText();
							 
							chk = empDao.checkEmployeeExists(loginDt.getDepo_code(), loginDt.getCmp_code(), setIntNumber(mac_cd));
							
							if (chk)
							{
								JOptionPane.showMessageDialog(EmployeeMaster.this,"Code Already Exists " ,"Duplicate Code",JOptionPane.INFORMATION_MESSAGE);						
								emp_code.setText("");
								emp_code.requestFocus();							
								emp_code.setSelectionStart(0);
							}
							else
							{
								mac_code.requestFocus();
								mac_code.setSelectionStart(0);
							}
						}
						catch(Exception e)
						{
							System.out.println(e);
						}
				   }
				   
					
					if (emp_code.getText().length()==0 && !chk)
					{
						alertMessage(EmployeeMaster.this, "Emp Code Can not be blank");
						emp_code.setText("");
						emp_code.requestFocus();
						emp_code.setSelectionStart(0);
					}


					evt.consume();
				}
				
			}
		});		
			


		//desg_code.setName("0");
		emp_code.setName("1");
		mac_code.setName("2");
		esic_no.setName("3");
		emp_name.setName("4");
		fname.setName("5");
		madd1.setName("6");
		madd2.setName("7");
		madd3.setName("8");
		mcity.setName("9");
		mstate.setName("10");
		mpin.setName("11");
		mphone.setName("12");
		mobile.setName("13");
		memail.setName("14");
		pan_no.setName("15");
		dobirth.setName("16");
		dojoin.setName("17");
		doresign.setName("18");
		basic.setName("19");
		da.setName("20");
		hra.setName("21");
		add_hra.setName("22");
		incentive.setName("23");
		spl_incentive.setName("24");
		lta.setName("25");
		medical.setName("26");
		bonus.setName("27");
		gross.setName("28");
		ot_rate.setName("29");
		st_allow.setName("30");
		bank_accno.setName("31");
//		bank_name.setName("32");
		bank_add1.setName("33");
		ifsc_code.setName("34");
		bonus_per.setName("35");
		bonus_limit.setName("36");
		bonus_check.setName("37");
		
		
		//desg_code.addKeyListener(keyListener);
		emp_code.addKeyListener(keyListener);
		mac_code.addKeyListener(keyListener);
		esic_no.addKeyListener(keyListener);
		emp_name.addKeyListener(keyListener);
		fname.addKeyListener(keyListener);
		madd1.addKeyListener(keyListener);
		madd2.addKeyListener(keyListener);
		madd3.addKeyListener(keyListener);
		mcity.addKeyListener(keyListener);
		mstate.addKeyListener(keyListener);
		mpin.addKeyListener(keyListener);
		mphone.addKeyListener(keyListener);
		mobile.addKeyListener(keyListener);
		memail.addKeyListener(keyListener);
		pan_no.addKeyListener(keyListener);
		dobirth.addKeyListener(keyListener);
		dojoin.addKeyListener(keyListener);
		doresign.addKeyListener(keyListener);
		gross.addKeyListener(keyListener);
		basic.addKeyListener(keyListener);
		da.addKeyListener(keyListener);
		hra.addKeyListener(keyListener);
		add_hra.addKeyListener(keyListener);
		incentive.addKeyListener(keyListener);
		spl_incentive.addKeyListener(keyListener);
		lta.addKeyListener(keyListener);
		medical.addKeyListener(keyListener);
		bonus.addKeyListener(keyListener);
		ot_rate.addKeyListener(keyListener);
		st_allow.addKeyListener(keyListener);
		bank_accno.addKeyListener(keyListener);
//		bank_name.addKeyListener(keyListener);
		bankList.addKeyListener(keyListener);
		bank_add1.addKeyListener(keyListener);
		ifsc_code.addKeyListener(keyListener);
		bonus_per.addKeyListener(keyListener);
		bonus_limit.addKeyListener(keyListener);
		bonus_check.addKeyListener(keyListener);

		

		emp_code.addFocusListener(myFocusListener);
		mac_code.addFocusListener(myFocusListener);
		esic_no.addFocusListener(myFocusListener);
		uan_no.addFocusListener(myFocusListener);
		emp_name.addFocusListener(myFocusListener);
		fname.addFocusListener(myFocusListener);
		madd1.addFocusListener(myFocusListener);
		madd2.addFocusListener(myFocusListener);
		madd3.addFocusListener(myFocusListener);
		mcity.addFocusListener(myFocusListener);
		mstate.addFocusListener(myFocusListener);
		mpin.addFocusListener(myFocusListener);
		mphone.addFocusListener(myFocusListener);
		mobile.addFocusListener(myFocusListener);
		memail.addFocusListener(myFocusListener);
		pan_no.addFocusListener(myFocusListener);
		dobirth.addFocusListener(myFocusListener);
		dojoin.addFocusListener(myFocusListener);
		doresign.addFocusListener(myFocusListener);
		gross.addFocusListener(myFocusListener);
		basic.addFocusListener(myFocusListener);
		da.addFocusListener(myFocusListener);
		hra.addFocusListener(myFocusListener);
		add_hra.addFocusListener(myFocusListener);
		incentive.addFocusListener(myFocusListener);
		spl_incentive.addFocusListener(myFocusListener);
		lta.addFocusListener(myFocusListener);
		medical.addFocusListener(myFocusListener);
		bonus.addFocusListener(myFocusListener);
		ot_rate.addFocusListener(myFocusListener);
		st_allow.addFocusListener(myFocusListener);
		bank_accno.addFocusListener(myFocusListener);
//		bank_name.addFocusListener(myFocusListener);
		bankList.addFocusListener(myFocusListener);
		bank_add1.addFocusListener(myFocusListener);
		ifsc_code.addFocusListener(myFocusListener);
		bonus_per.addFocusListener(myFocusListener);
		bonus_limit.addFocusListener(myFocusListener);
		bonus_check.addFocusListener(myFocusListener);
		
		btnAdd.setActionCommand("Add");
		btnSave.setActionCommand("Save");
		btnExcel.setActionCommand("Excel");
		exitButton.setActionCommand("Exit");
		btnResign.setActionCommand("Resign");
		btnResignation.setActionCommand("Resignation");
		btnRefresh.setActionCommand("Refresh");
		
 
		 
		btnAdd.setVisible(true);
		btnSave.setVisible(true);
		 
		
		JLabel lblLabel = new JLabel("Label Print :");
		lblLabel.setBounds(645, 60, 86, 20);
		getContentPane().add(lblLabel);
		
		labelprint =  new JComboBox();
		labelprint.setModel(new DefaultComboBoxModel(new String[] {"Y", "N"}));
		labelprint.setBounds(742, 60, 38, 22);
		getContentPane().add(labelprint);
		
		emp_status =  new JComboBox();
		//emp_status.setBackground(Color.LIGHT_GRAY);
		emp_status.setModel(new DefaultComboBoxModel(new String[] {"Active", "Deactive"}));
		emp_status.setBounds(880, 60, 96, 22);
		getContentPane().add(emp_status);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBackground(new Color(139, 153, 122));
		panel.setBounds(695, 394, 180, 27);
		getContentPane().add(panel);
		
		JLabel lblBankDetail = new JLabel("Bank Detail");
		lblBankDetail.setForeground(Color.WHITE);
		lblBankDetail.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel.add(lblBankDetail);
		
		
		
		panel_4 = new JPanel();
		panel_4.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_4.setBounds(263, 48, 745, 557);
		getContentPane().add(panel_4);
		
		
		btnAdd.addActionListener(this);
		btnSave.addActionListener(this);
		btnExcel.addActionListener(this);
		exitButton.addActionListener(this);
		btnResign.addActionListener(this);
		btnResignation.addActionListener(this);
		btnRefresh.addActionListener(this);
	}





	public void actionPerformed(ActionEvent e) 
	{
		System.out.println(e.getActionCommand());
		if(e.getActionCommand().equalsIgnoreCase("Exit"))
		{
			clearAll();
			mac_code.setEditable(false);
			btnAdd.setEnabled(true);
			btnExcel.setEnabled(true);
			dispose();
		}

		if(e.getActionCommand().equalsIgnoreCase("Add")  )
		{
			clearAll();
			option="A";
			mac_code.setEditable(true);
			dsCombo.requestFocus();
			mac_code.setEditable(true);
			btnAdd.setEnabled(false);
			btnExcel.setEnabled(false);
			
		}


		if(e.getActionCommand().equalsIgnoreCase("Excel"))
		{
			new EmployeeList(loginDt.getBrnnm(), loginDt.getDrvnm(),data,repno);
			clearAll();
			
		}

		if(e.getActionCommand().equalsIgnoreCase("Resign"))
		{
			new EmployeeList(loginDt.getBrnnm(), loginDt.getDrvnm(),data,3);
			clearAll();
			
		}
		if(e.getActionCommand().equalsIgnoreCase("Resignation"))
		{
			fillInvTable("Resignation");
			btnResignation.setVisible(false);
			btnRefresh.setVisible(true);
		}
		
		if(e.getActionCommand().equalsIgnoreCase("Refresh"))
		{
			fillInvTable("");
			btnResignation.setVisible(true);
			btnRefresh.setVisible(false);
		}
		
		if(e.getActionCommand().equalsIgnoreCase("Save"))
		{
			saveData();
		}		
	}

	
	public void saveData()
	{
		if(option.equals("A"))
		{
			if (mac_code.getText().length()>0)
			{
				boolean chk=false;
				//boolean chk = empDao.checkEmployeeExists(loginDt.getDepo_code(), loginDt.getDiv_code(), mac_code.getText());
				if (chk)
				{
					JOptionPane.showMessageDialog(EmployeeMaster.this,"Code Already Exists " ,"Duplicate Code",JOptionPane.INFORMATION_MESSAGE);						
					mac_code.setText("");
					mac_code.requestFocus();							
				}
				else
				{
					empDao.addEmployee(setDataForDAO());
				}
			}
			else
				alertMessage(EmployeeMaster.this, "A/c Code Can not be blank");
		}
		else
		{
			EmployeeMastDto edto = setDataForDAO();
			if(edto!=null)
				empDao.updateEmployee(edto,repno);
			else
			{
				System.out.println("edrror in setdaomethore ");
			}
		}
		
//		loginDt.setPrtList(prtdao.getEmpList(loginDt.getDepo_code(), loginDt.getDiv_code()));
//		loginDt.setPrtmap(prtdao.getEmpMap(loginDt.getDepo_code(), loginDt.getDiv_code()));


		fillInvTable("");
		clearAll();
	}
	public EmployeeMastDto setDataForDAO()
	{
		EmployeeMastDto empDto = null;
		try
		{
			empDto = new EmployeeMastDto();
			empDto.setCmp_code(loginDt.getCmp_code());
			empDto.setDepo_code(loginDt.getDepo_code());
			empDto.setDepartment(dsCombo.getText());
			empDto.setEmp_code(setIntNumber(emp_code.getText().trim()));
			empDto.setLabel_print(labelprint.getSelectedItem().toString());
			empDto.setEmp_status(emp_status.getSelectedIndex()==0?"Y":"N");

			empDto.setPf_no(setIntNumber(mac_code.getText().trim()));
			empDto.setEsic_no(setLongNumber(esic_no.getText().trim()));
			empDto.setUan_no(setLongNumber(uan_no.getText().trim()));
			empDto.setEmp_name(emp_name.getText().trim());
			empDto.setFather_name(fname.getText().trim());
			empDto.setMadd1(madd1.getText().trim());
			empDto.setMadd2(madd2.getText().trim());
			empDto.setMadd3(madd3.getText().trim());
			empDto.setMcity(mcity.getText().trim());
			empDto.setMstate(mstate.getText().trim());
			empDto.setMpin(mpin.getText().trim());
			empDto.setMphone(mphone.getText().trim());
			empDto.setMobile(mobile.getText().trim());
			empDto.setMemail(memail.getText().trim());
			empDto.setPan_no(pan_no.getText().trim());
			
			empDto.setDobirth(formatDate(dobirth.getText().trim()));
			empDto.setDojoin(formatDate(dojoin.getText().trim()));
			empDto.setDoresign(formatDate(doresign.getText().trim()));
			
			empDto.setGross(setDoubleNumber(gross.getText().trim()));
			empDto.setBasic(setDoubleNumber(basic.getText().trim()));
			empDto.setDa(setDoubleNumber(da.getText().trim()));
			empDto.setHra(setDoubleNumber(hra.getText().trim()));
			empDto.setAdd_hra(setDoubleNumber(add_hra.getText().trim()));
			empDto.setIncentive(setDoubleNumber(incentive.getText().trim()));
			empDto.setSpl_incentive(setDoubleNumber(spl_incentive.getText().trim()));
			empDto.setLta(setDoubleNumber(lta.getText().trim()));
			empDto.setMedical(setDoubleNumber(medical.getText().trim()));
			empDto.setBonus(setDoubleNumber(bonus.getText().trim()));
			empDto.setOt_rate(setDoubleNumber(ot_rate.getText().trim()));
			empDto.setStair_alw(setDoubleNumber(st_allow.getText().trim()));
			
			// set bank detail
			empDto.setBank_accno(bank_accno.getText().trim());
			empDto.setBank_add1(bank_add1.getText().trim());
			empDto.setIfsc_code(ifsc_code.getText().trim());
			
			empDto.setBank_code(((BankDto) bankList.getSelectedItem()).getBank_code());
			empDto.setBank(((BankDto) bankList.getSelectedItem()).getBank_name());

			empDto.setBonus_per(setDoubleNumber(bonus_per.getText().trim()));
			empDto.setBonus_limit(setDoubleNumber(bonus_limit.getText().trim()));
			empDto.setBonus_check(setDoubleNumber(bonus_check.getText().trim()));

			
		}
		catch(Exception e)
		{
			empDto = null;
			e.printStackTrace();
		}

		return empDto;

	}



	public void fillData(EmployeeMastDto empDto)
	{
		
		labelprint.setSelectedIndex(empDto.getLabel_print().equalsIgnoreCase("Y")?0:1);
		emp_status.setSelectedIndex(empDto.getEmp_status().equalsIgnoreCase("Y")?0:1);
		dsCombo.setText(empDto.getDesignation());
		emp_code.setText(String.valueOf(empDto.getEmp_code()));
		esic_no.setText(String.valueOf(empDto.getEsic_no()));
		uan_no.setText(String.valueOf(empDto.getUan_no()));
		mac_code.setText(String.valueOf(empDto.getPf_no()));
		emp_name.setText(empDto.getEmp_name());
		fname.setText(empDto.getFather_name());
		madd1.setText(empDto.getMadd1());
		madd2.setText(empDto.getMadd2());
		madd3.setText(empDto.getMadd3());
		mcity.setText(empDto.getMcity());
		mstate.setText(empDto.getMstate());
		mpin.setText(empDto.getMpin());
		mphone.setText(empDto.getMphone());
		mobile.setText(empDto.getMobile());
		memail.setText(empDto.getMemail());
		pan_no.setText(empDto.getPan_no());
		dobirth.setText(formatDateScreen(empDto.getDobirth(),dobirth));
		dojoin.setText(formatDateScreen(empDto.getDojoin(),dojoin));
		doresign.setText(formatDateScreen(empDto.getDoresign(),doresign));
		
		gross.setText(formatter.format(empDto.getGross()));
		basic.setText(formatter.format(empDto.getBasic()));
		da.setText(formatter.format(empDto.getDa()));
		hra.setText(formatter.format(empDto.getHra()));
		add_hra.setText(formatter.format(empDto.getAdd_hra()));
		incentive.setText(formatter.format(empDto.getIncentive()));
		spl_incentive.setText(formatter.format(empDto.getSpl_incentive()));
		lta.setText(formatter.format(empDto.getLta()));
		medical.setText(formatter.format(empDto.getMedical()));
		bonus.setText(formatter.format(empDto.getBonus()));
		ot_rate.setText(formatter.format(empDto.getOt_rate()));
		st_allow.setText(formatter.format(empDto.getStair_alw()));
		
		//setting bank details
		bank_accno.setText(empDto.getBank_accno());
//		bank_name.setText(empDto.getBank());
		bankList.setSelectedIndex(getIndex(empDto.getBank_code()));
		bank_add1.setText(empDto.getBank_add1());
		ifsc_code.setText(empDto.getIfsc_code());
		bonus_per.setText(formatter.format(empDto.getBonus_per()));
		bonus_limit.setText(formatter.format(empDto.getBonus_limit()));
		bonus_check.setText(formatter.format(empDto.getBonus_check()));
		
		btnAdd.setEnabled(true);
		btnExcel.setEnabled(true);
		option="";
		
	}
	
	 
	
	public void fillInvTable(String search)
	{
		emplyeeTableModel.getDataVector().removeAllElements();
		emplyeeTableModel.fireTableDataChanged();
		
		data = (Vector) empDao.getEmployeeList(loginDt.getDepo_code(),loginDt.getCmp_code());
		
		 
		
		Vector c = null;
		int s = data.size();
		
		for(int i =0;i<s;i++)
		{
			c =(Vector) data.get(i);
			empDto = (EmployeeMastDto) c.get(2);
			if(search.equalsIgnoreCase("Resignation"))
			{
				if(empDto.getDoresign()!=null)
					emplyeeTableModel.addRow(c);
			}
			else
			{
				if(empDto.getDoresign()==null)
					emplyeeTableModel.addRow(c);
			}
		}
		
		if (s==0)
		{
			for(int i =0;i<30;i++)
			{
				emplyeeTableModel.addRow(new Object[2][]);
			}
		}

	}

 

	public void calfunction(JDoubleField dval)
	{
		dval.setText(formatter.format(setDoubleNumber(dval.getText())));
		double grossval=setDoubleNumber(basic.getText())+setDoubleNumber(da.getText())+setDoubleNumber(hra.getText())+setDoubleNumber(add_hra.getText())+setDoubleNumber(incentive.getText())+setDoubleNumber(spl_incentive.getText());
		gross.setText(formatter.format(grossval));
		
	}

	public void clearAll() 
	{
	
		dsCombo.setText("");
		emp_code.setText("");
		mac_code.setText("");
		esic_no.setText("");
		uan_no.setText("");
		emp_name.setText("");
		fname.setText("");
		madd1.setText("");
		madd2.setText("");
		madd3.setText("");
		mcity.setText("");
		mstate.setText("");
		mpin.setText("");
		mphone.setText("");
		mobile.setText("");
		memail.setText("");
		pan_no.setText("");
		dobirth.setValue(null);
		dojoin.setValue(null);
		doresign.setValue(null);
		gross.setText("");
		basic.setText("");
		da.setText("");
		hra.setText("");
		add_hra.setText("");
		incentive.setText("");
		spl_incentive.setText("");
		lta.setText("");
		medical.setText("");
		bonus.setText("");
		ot_rate.setText("");
		st_allow.setText("");
		bank_accno.setText("");
//		bank_name.setText("");
//		bankList.setSelectedIndex(0);
		bankList.setSelectedIndex(5);

		bank_add1.setText("");
		ifsc_code.setText("");
		bonus_per.setText("");
		bonus_limit.setText("");
		bonus_check.setText("");
		
		//party_name.setText("");
		option="A";
		btnSave.setBackground(null);
		btnAdd.setEnabled(true);
		btnExcel.setEnabled(true);
		//bankList.setSelectedIndex(0);
		
	}


	public void setFieldEnabled(boolean b)
	{
		dsCombo.setEnabled(b);
		emp_code.setEnabled(b);
		mac_code.setEnabled(b);
		esic_no.setEnabled(b);
		uan_no.setEnabled(b);
		emp_name.setEnabled(b);
		fname.setEnabled(b);
		madd1.setEnabled(b);
		madd2.setEnabled(b);
		madd3.setEnabled(b);
		mcity.setEnabled(b);
		mstate.setEnabled(b);
		mpin.setEnabled(b);
		mphone.setEnabled(b);
		mobile.setEnabled(b);
		memail.setEnabled(b);
		pan_no.setEnabled(b);
		dobirth.setEnabled(b);
		dojoin.setEnabled(b);
		doresign.setEnabled(b);
		gross.setEnabled(b);
		basic.setEnabled(b);
		da.setEnabled(b);
		hra.setEnabled(b);
		add_hra.setEnabled(b);
		incentive.setEnabled(b);
		spl_incentive.setEnabled(b);
		lta.setEnabled(b);
		medical.setEnabled(b);
		bonus.setEnabled(b);
		ot_rate.setEnabled(b);
		st_allow.setEnabled(b);


	}
	
	public void setVisible(boolean b)
	{
		if(repNm.equalsIgnoreCase("Worker Master"))
		{
			setFieldEnabled(true);
			bank_accno.setEnabled(false);
			bankList.setEnabled(false);
			bank_add1.setEnabled(false);
			ifsc_code.setEnabled(false);
			bonus_per.setEnabled(false);
			bonus_limit.setEnabled(false);
			bonus_check.setEnabled(false);
			btnResign.setVisible(true);
			bankList.setSelectedIndex(5);

			repno=1;
		}
		else if(repNm.equalsIgnoreCase("Worker Bank Master"))
		{
			setFieldEnabled(false);
			bank_accno.setEnabled(b);
			bankList.setEnabled(b);
			bank_add1.setEnabled(b);
			ifsc_code.setEnabled(b);
			bonus_per.setEnabled(b);
			bonus_limit.setEnabled(b);
			bonus_check.setEnabled(b);

			btnResign.setVisible(false);
			bankList.setSelectedIndex(5);
			repno=2;
		}
		fillInvTable("");
		super.setVisible(b);
	}
}
 