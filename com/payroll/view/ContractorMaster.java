package com.payroll.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

import com.payroll.dao.CmpMsflDAO;
import com.payroll.dto.ContractMastDto;
import com.payroll.util.JDoubleField;
import com.payroll.util.JIntegerField;
import com.payroll.util.TextField;

public class ContractorMaster extends BaseClass implements ActionListener {


	private static final long serialVersionUID = 1L;
	Font font;
	private JIntegerField cmp_code;
	private JTextField address2;
	private JTextField address3;
	private JTextField city;
	private JTextField phone;
	private JTextField fax,panno,email_id,pin;
	private JTextField address1,employer_name;
	
	private JLabel lblInvoiceNo ;
	private JLabel lblPartyCode; 
	private JLabel lblDispatchDate;
	private JLabel lblInvoiceDate;
	private JLabel lblLrDate;
	private JLabel lblFax;
	private JLabel lblCity,lblEmployerName;
	private JLabel lblDispatchEntry;
	private JPanel panel_2;
	private JButton exitButton,saveButton;
	private SimpleDateFormat sdf;

	
	
	NumberFormat formatter;	
	private JLabel lblDlno;
	private TextField emptype;
	private JTextField cmp_name;
	private JLabel lblDlno2;
	private JLabel lblDldate;
	private JLabel lblFoodno;
	private TextField bank;
	private JTextField licenseno,sertaxno;
	private JLabel lblCinNo,lblSertaxNo;
	private CmpMsflDAO cmpDao=null;
	private ContractMastDto cmpDto;
	private JDoubleField empcont,empcont1; 
	
	
	
	public ContractorMaster()
	{
		 
		
		cmpDao = new CmpMsflDAO();
		
		sdf = new SimpleDateFormat("dd/MM/yyyy");  // Date Format

		formatter = new DecimalFormat("0.00");     // Decimal Value format
		//setUndecorated(true);
		setResizable(false);
		setSize(1024, 768);		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		
		
		
		
		
		// ///////////////////////////////////////////////

		phone = new TextField(30);
		phone.setBounds(404, 279, 208, 23);
		getContentPane().add(phone);

		address1 = new TextField(40);
		address1.setBounds(404, 167, 374, 23);
		getContentPane().add(address1);

		employer_name = new TextField(40);
		employer_name.setBounds(404, 138, 374, 23);
		getContentPane().add(employer_name);

		
		lblFinancialAccountingYear = new JLabel(loginDt.getFooter());
		lblFinancialAccountingYear.setForeground(Color.BLACK);
		lblFinancialAccountingYear.setBounds(315, 672, 477, 15);
		getContentPane().add(lblFinancialAccountingYear);


		lblInvoiceNo = new JLabel("Address:");
		lblInvoiceNo.setBounds(181, 196, 198, 20);
		getContentPane().add(lblInvoiceNo);


		address2 = new TextField(40);
		address2.setBounds(404, 195,374, 23);
		getContentPane().add(address2);


		lblPartyCode = new JLabel("Code");
		lblPartyCode.setBounds(181, 112, 198, 20);
		getContentPane().add(lblPartyCode);

		cmp_code = new JIntegerField();
		cmp_code.setEditable(false);
		cmp_code.setBounds(404, 111,70, 23);
		getContentPane().add(cmp_code);

		lblDispatchDate = new JLabel("Address:");
		lblDispatchDate.setBounds(181, 224, 198, 20);
		getContentPane().add(lblDispatchDate);

		lblInvoiceDate = new JLabel("City:");
		lblInvoiceDate.setBounds(181, 252, 198, 20);
		getContentPane().add(lblInvoiceDate);

		address3 = new TextField(40);
		address3.setBounds(404, 224, 374, 23);
		getContentPane().add(address3);
	
	    lblDlno = new JLabel("Employer Type.:");
		lblDlno.setBounds(181, 430, 198, 20);
		getContentPane().add(lblDlno);
		
		emptype = new TextField(30);
		emptype.setBounds(404, 429, 208, 23);
		getContentPane().add(emptype);
		
		
		city = new TextField(20);
		city.setBounds(404, 251, 208, 23);
		getContentPane().add(city);

		lblLrDate = new JLabel("Phone No.");
		lblLrDate.setBounds(181, 280, 198, 20);
		getContentPane().add(lblLrDate);
				
		
		lblFax = new JLabel("Fax:");
		lblFax.setBounds(181, 308, 198, 20);
		getContentPane().add(lblFax);

		lblCity = new JLabel("Address:");
		lblCity.setBounds(181, 168, 198, 20);
		getContentPane().add(lblCity);

		lblEmployerName = new JLabel("Employer Name:");
		lblEmployerName.setBounds(181, 139, 198, 20);
		getContentPane().add(lblEmployerName);

		
		JLabel promleb = new JLabel(promLogo);
		promleb.setBounds(10, 670, 35, 35);
		getContentPane().add(promleb);

//		JLabel arisleb = new JLabel(arisLogo);
//		arisleb.setBounds(10, 11, 35, 37);
//		getContentPane().add(arisleb);

		lblDispatchEntry = new JLabel(loginDt.getBrnnm());
		lblDispatchEntry.setForeground(Color.BLACK);
		lblDispatchEntry.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblDispatchEntry.setBounds(414, 55, 381, 22);
		getContentPane().add(lblDispatchEntry);
		
		fax = new TextField(30);
		fax.setBounds(404, 307, 208, 23);
		getContentPane().add(fax);

		JLabel lblPanNo = new JLabel("PAN No:");
		lblPanNo.setBounds(181, 374, 198, 20);
		getContentPane().add(lblPanNo);
		
		panno = new TextField(15);
		panno.setBounds(404, 373, 208, 23);
		getContentPane().add(panno);
		
		JLabel lblEmail = new JLabel("Email :");
		lblEmail.setBounds(181, 340, 198, 20);
		getContentPane().add(lblEmail);
		
		email_id = new TextField(50);
		email_id.setBounds(404, 339, 208, 23);
		getContentPane().add(email_id);
		
		
		
		
		
		
		
		 
		
		exitButton = new JButton("Exit");
		exitButton.setBounds(802, 613, 86, 30);
		getContentPane().add(exitButton);
		
		saveButton = new JButton("Save");
		saveButton.setBounds(710, 613, 86, 30);
		getContentPane().add(saveButton);
		
		saveButton.addKeyListener(new KeyAdapter() 
		{
			public void keyPressed(KeyEvent evt) 
			{
				if(evt.getKeyCode() == KeyEvent.VK_ENTER)
				{
					saveData();
				}
			}
		});
		
		
		
		cmp_name = new TextField(40);
		cmp_name.setEditable(false);
		cmp_name.setBounds(480, 111, 298, 23);
		getContentPane().add(cmp_name);

		 
		
		
		lblDlno2 = new JLabel("Employer Contribution");
		lblDlno2.setBounds(181, 458, 198, 20);
		getContentPane().add(lblDlno2);
		
		empcont = new JDoubleField(); 
		empcont.setBounds(404, 457, 208, 23);
		getContentPane().add(empcont);
		
		
		
		lblDldate = new JLabel("Employee Contributon:");
		lblDldate.setBounds(181, 486, 198, 20);
		getContentPane().add(lblDldate);
		
		empcont1 = new JDoubleField();
		empcont1.setBounds(404, 485, 208, 23);
		getContentPane().add(empcont1);
		
		lblFoodno = new JLabel("Bank:");
		lblFoodno.setBounds(181, 514, 198, 20);
		getContentPane().add(lblFoodno);
		
		bank = new TextField(30);
		bank.setBounds(404, 513, 208, 23);
		getContentPane().add(bank);
		
		
		licenseno = new TextField(100);
		licenseno.setBounds(404, 401, 208, 23);
		getContentPane().add(licenseno);
		
		lblCinNo = new JLabel("License No:");
		lblCinNo.setBounds(181, 402, 198, 20);
		getContentPane().add(lblCinNo);
		
		
		JLabel lblPin = new JLabel("Pin:");
		lblPin.setBounds(622, 253, 70, 20);
		getContentPane().add(lblPin);
		
		pin = new TextField(6);
		pin.setBounds(699, 252, 79, 23);
		getContentPane().add(pin);
		
		
		lblSertaxNo = new JLabel("Service Tax No:");
		lblSertaxNo.setBounds(181, 542, 198, 20);
		getContentPane().add(lblSertaxNo);
		
		sertaxno = new TextField(30);
		sertaxno.setBounds(404, 541, 208, 23);
		getContentPane().add(sertaxno);

		
		panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		panel_2.setBackground(SystemColor.activeCaptionBorder);
		panel_2.setBounds(218, 657, 598, 48);
		getContentPane().add(panel_2);
		
		
		KeyListener keyListener = new KeyListener() 
		{
			public void keyPressed(KeyEvent keyEvent) 
			{
				int key = keyEvent.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					JTextField textField = (JTextField) keyEvent.getSource();
					int id = Integer.parseInt(textField.getName());
					switch (id) 
					{
					case 0:
						cmp_code.requestFocus();
						cmp_code.setSelectionStart(0);
						break;
					case 1:
						cmp_name.requestFocus();
						cmp_name.setSelectionStart(0);
						break;
					case 2:
						employer_name.requestFocus();
						employer_name.setSelectionStart(0);
						break;
					case 3:
						address1.requestFocus();
						address1.setSelectionStart(0);
						break;
					case 4:
						address2.requestFocus();
						address2.setSelectionStart(0);
						break;
					case 5:
						address3.requestFocus();
						address3.setSelectionStart(0);
						break;
					case 6:
						city.requestFocus();
						city.setSelectionStart(0);
						break;
					case 7:
						pin.requestFocus();
						pin.setSelectionStart(0);
						break;
					case 8:
						phone.requestFocus();
						phone.setSelectionStart(0);
						break;
					case 9:
						fax.requestFocus();
						fax.setSelectionStart(0);
						break;
					case 10:
						email_id.requestFocus();
						email_id.setSelectionStart(0);
						break;
					case 11:
						panno.requestFocus();
						panno.setSelectionStart(0);
						break;
					case 12:
						licenseno.requestFocus();
						licenseno.setSelectionStart(0);
						break;
					case 13:
						emptype.requestFocus();
						emptype.setSelectionStart(0);
						break;
					case 14:
						empcont.requestFocus();
						empcont.setSelectionStart(0);
						break;
					case 15:
						empcont1.requestFocus();
						empcont1.setSelectionStart(0);
				 		break;
					case 16:
						bank.requestFocus();
						bank.setSelectionStart(0);
				 		break;
					case 17:
						sertaxno.requestFocus();
						sertaxno.setSelectionStart(0);
				 		break;
					case 18:
						saveButton.requestFocus();
						saveButton.setBackground(new Color(139, 153, 122));
						exitButton.setBackground(null);

						break;
					}
				}

				if (key == KeyEvent.VK_ESCAPE) {
//					dispose();
					//System.exit(0);
					
				}
			}

			public void keyReleased(KeyEvent keyEvent) {
			}

			public void keyTyped(KeyEvent keyEvent) {
			}
		};
		
		
		
		
		
		
		
		cmp_code.setName("1");
		cmp_name.setName("2");
		employer_name.setName("3");
		address1.setName("4");
		address2.setName("5");
		address3.setName("6");
		city.setName("7");
		pin.setName("8");
		phone.setName("9");
		fax.setName("10");
		email_id.setName("11");
		panno.setName("12");
		licenseno.setName("13");
		emptype.setName("14");
		empcont.setName("15");
		empcont1.setName("16");
		bank.setName("17");
		sertaxno.setName("18");

		
		
		
		cmp_code.addKeyListener(keyListener);
		cmp_name.addKeyListener(keyListener);
		employer_name.addKeyListener(keyListener);
		address1.addKeyListener(keyListener);
		address2.addKeyListener(keyListener);
		address3.addKeyListener(keyListener);
		city.addKeyListener(keyListener);
		pin.addKeyListener(keyListener);
		phone.addKeyListener(keyListener);
		fax.addKeyListener(keyListener);
		email_id.addKeyListener(keyListener);
		panno.addKeyListener(keyListener);
		licenseno.addKeyListener(keyListener);
		emptype.addKeyListener(keyListener);
		empcont.addKeyListener(keyListener);
		empcont1.addKeyListener(keyListener);
		bank.addKeyListener(keyListener);
		sertaxno.addKeyListener(keyListener);
		
		exitButton.addActionListener(this);
		exitButton.setActionCommand("exit");
		
		saveButton.addActionListener(this);
		saveButton.setActionCommand("save");
		
		
		
		
		
				
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setBounds(146, 99, 742, 506);
		getContentPane().add(panel_1);
		
		
		
		
		
	}

	
 

	
	
	

	
	
	public void actionPerformed(ActionEvent e) 
	{
		
		if(e.getActionCommand().equalsIgnoreCase("exit"))
		{
			dispose();
		}
		
		 
		
		if(e.getActionCommand().equalsIgnoreCase("save"))
		{
			saveData();
		}
		
		 
		

	}
	
	
	public void saveData()
	{
	 
			getData();
			if(add==0)
			{
				cmpDao.updateCompanyInfo(cmpDto);
				alertMessage(ContractorMaster.this, "Record Saved Sucessfully!!!!");
			}
			else
			{	
				if(cmp_name.getText().length()>0)
				{
					int i=cmpDao.addCompanyInfo(cmpDto);
					if(i>0)
					{
						alertMessage(ContractorMaster.this, "Record Added Sucessfully!!!!");
						add=0;
						System.exit(1);
					}
					else
						alertMessage(ContractorMaster.this, "Error While Add Record!!!!");
				}
			}
	}
	
 
	
	
	
	
	
	public void fillData()
	{

		cmp_code.setText(String.valueOf(loginDt.getCmp_code()));
		cmp_name.setText(cmpDto.getCmp_name());
		address1.setText(cmpDto.getCmp_add1());
		address2.setText(cmpDto.getCmp_add2());
		address3.setText(cmpDto.getCmp_add3());
		city.setText(cmpDto.getCmp_city());
		pin.setText(String.valueOf(cmpDto.getCmp_pin()));
		phone.setText(cmpDto.getCmp_phone());
		fax.setText(cmpDto.getCmp_fax());
		email_id.setText(cmpDto.getCmp_email());
		panno.setText(cmpDto.getCmp_panno());
		licenseno.setText(cmpDto.getLicense_no());
		employer_name.setText(cmpDto.getEmployer_name());
		emptype.setText(cmpDto.getEmployer_type());
		empcont.setText(formatter.format(cmpDto.getEmployer_cont()));
		empcont1.setText(formatter.format(cmpDto.getEmployee_cont()));
		bank.setText(cmpDto.getBanker());
		sertaxno.setText(cmpDto.getSertax_no());
	}
		

	public void getData()
	{

		cmpDto.setCmp_code(loginDt.getCmp_code());
		cmpDto.setCmp_name(cmp_name.getText());
		cmpDto.setCmp_add1(address1.getText());
		cmpDto.setCmp_add2(address2.getText());
		cmpDto.setCmp_add3(address3.getText());
		cmpDto.setCmp_city(city.getText());
		cmpDto.setCmp_pin(setIntNumber(pin.getText()));
		cmpDto.setCmp_phone(phone.getText());
		cmpDto.setCmp_fax(fax.getText());
		cmpDto.setCmp_email(email_id.getText());
		cmpDto.setCmp_panno(panno.getText());
		cmpDto.setLicense_no(licenseno.getText());
		cmpDto.setEmployer_name(employer_name.getText());
		cmpDto.setEmployer_type(emptype.getText());
		cmpDto.setEmployer_cont(setDoubleNumber(empcont.getText()));
		cmpDto.setEmployee_cont(setDoubleNumber(empcont1.getText()));
		cmpDto.setBanker(bank.getText());
		cmpDto.setSertax_no(sertaxno.getText());
		cmpDto.setDepo_code(loginDt.getDepo_code());
		
	}
	
	 
	public void setVisible(boolean b)
	{
		if(add==0)
		{
			cmpDto = cmpDao.getCompInfo(loginDt.getDepo_code(),loginDt.getCmp_code());
			cmp_name.setEditable(false);
			fillData();
		}
		else
		{
			cmp_code.setText(String.valueOf(loginDt.getCmp_code()));
			cmp_name.setEditable(true);
			cmp_name.requestFocus();
			cmpDto = new ContractMastDto();
		}
		
		super.setVisible(b);
	}
}


