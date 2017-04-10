package com.payroll.view;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import com.payroll.dao.LoginDAO;
import com.payroll.dao.UserDAO;
import com.payroll.dto.ContractMastDto;
import com.payroll.dto.LoginDto;

public class PackageOpt extends BaseClass implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	 
	 
	private JLabel lblPromptSoftwareConsultants;
	private JLabel lblNewLabel;
	private JLabel lblPhone;
	private JPanel panel_1;
 
	private Font font; 
	LoginDAO ldao;
	private JButton exitButton,submitButton;
	private JLabel lblselect,lblpass;
	private JComboBox spackage ;
	private String uname,pass; 
    private Vector<?> packlst;
    private JPanel panel_2;
    private JPanel panel_3;
    private JLabel lblCompanySelection;
    int uid;
    private ContractMastDto	brn;
    private JPasswordField password;
    public PackageOpt(String uname,String pass)
	{
		font =new Font("Tahoma", Font.PLAIN, 11);
		///////// frame setting////////////////////////////////////////////////		
 
		
		this.uname=uname;
		this.pass=pass;
		
		UserDAO udao = new UserDAO();
		uid=udao.getUserId(uname,pass);

		packlst = udao.getBranch(uid);
		
 
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1024, 768);
		setResizable(false);
 
		getContentPane().setLayout(null);


		/////////////////////////////////////
		//arisLogo = new ImageIcon(getClass().getResource("/images/aris_log.png"));
	 
		
		///////////////////////////////////////////////////////////////////////////////////////////////

		exitButton = new JButton("Exit");
		exitButton.setBounds(549, 375, 82, 30);
		getContentPane().add(exitButton);
		exitButton.addActionListener(this);
		
		
		lblselect = new JLabel("Select:");
		lblselect.setBounds(344, 327, 80, 20);
		getContentPane().add(lblselect);
		
		spackage = new JComboBox(packlst);
		spackage.setActionCommand("division");
		spackage.setBounds(424, 327, 252, 23);
		getContentPane().add(spackage);
		spackage.addKeyListener(new KeyAdapter()
		{
			public void keyReleased(KeyEvent evt)
			{
				if(evt.getKeyCode() == KeyEvent.VK_ENTER)
				{
					brn = (ContractMastDto) spackage.getSelectedItem();
					if(brn.getCmp_code()==99)
					{
						password.requestFocus();
					}
					else
					{
						submitButton.requestFocus();
					}
				}
				evt.consume();
			}
		});

		spackage.addItemListener(new ItemListener() {
			
			public void itemStateChanged(ItemEvent itemEvent) {
				// TODO Auto-generated method stub
				int state = itemEvent.getStateChange();
				if(state == ItemEvent.SELECTED)
				{
					brn = (ContractMastDto) spackage.getSelectedItem();
					if(brn.getCmp_code()==99)
					{
						lblpass.setVisible(true);
						password.setVisible(true);
						submitButton.setBounds(454, 393, 82, 30);
						exitButton.setBounds(549, 393, 82, 30);
					}
					else
					{
						lblpass.setVisible(false);
						password.setVisible(false);
						submitButton.setBounds(454, 375, 82, 30);
						exitButton.setBounds(549, 375, 82, 30);
					}
				}
				
			}
	
		});
		
		
		lblpass = new JLabel("Password:");
		lblpass.setBounds(344, 355, 80, 20);
		lblpass.setVisible(false);
		getContentPane().add(lblpass);
		
		password = new JPasswordField();
		password.setBounds(424, 355, 252, 23);
		password.setVisible(false);
		getContentPane().add(password);
		
		password.addKeyListener(new KeyAdapter()
		{
			public void keyReleased(KeyEvent evt)
			{
				if(evt.getKeyCode() == KeyEvent.VK_ENTER)
				{
					submitButton.requestFocus();

				}
				evt.consume();
			}
		
		});
		
		submitButton = new JButton("Submit");
		submitButton.setBounds(454, 375, 82, 30);
		getContentPane().add(submitButton);
		submitButton.addActionListener(this);
		submitButton.addKeyListener(new KeyAdapter()
		{
			public void keyReleased(KeyEvent evt)
			{
				if(evt.getKeyCode() == KeyEvent.VK_ENTER)
				{
					checkPassword();
				}
				evt.consume();
			}
		});
		
		
		lblCompanySelection = new JLabel("Contractor Selection");
		//lblCompanySelection.setForeground(Color.WHITE);
		lblCompanySelection.setFont(new Font("Verdana", Font.BOLD, 13));
		lblCompanySelection.setBounds(341, 249, 155, 20);
		getContentPane().add(lblCompanySelection);

		//JLabel arisleb = new JLabel(arisLogo);
		//arisleb.setBounds(4, 7, 40, 52);
		//getContentPane().add(arisleb);

		/////////// footer///////////////////////////////////////////////////////////

		promLogo = new ImageIcon(getClass().getResource("/images/plogo.png"));
		JLabel promleb = new JLabel(promLogo);
		promleb.setBounds(12, 680, 35, 35);
		getContentPane().add(promleb);

		lblPromptSoftwareConsultants = new JLabel("Prompt Software Consultants");
		lblPromptSoftwareConsultants.setBounds(50, 678, 236, 14);
		getContentPane().add(lblPromptSoftwareConsultants);

		lblNewLabel = new JLabel("Aashish Vihar, 17/3 Old Palasia Indore 452001.");
		lblNewLabel.setFont(font);
		lblNewLabel.setBounds(50, 694, 236, 14);
		getContentPane().add(lblNewLabel);

		lblPhone = new JLabel("Phone: 0731-2539888, 4069098   Email: promptindore@gmail.com ");
		lblPhone.setFont(font);
		lblPhone.setBounds(50, 709, 347, 14);
		getContentPane().add(lblPhone);

		panel_1 = new JPanel();
		panel_1.setBackground(UIManager.getColor("TextPane.selectionBackground"));
		panel_1.setBounds(0, 666, 1018, 3);
		getContentPane().add(panel_1);

		
		clockLab = new JLabel();
		clockLab.setFont(new Font("Verdana", Font.BOLD, 14));
		clockLab.setBounds(794, 680, 217, 24);
		getContentPane().add(clockLab);
		// TODO VERSION CHANGE
		lblVersion = new JLabel(version);
		lblVersion.setForeground(new Color(0, 102, 0));
		lblVersion.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblVersion.setBounds(826, 709, 159, 14);
		getContentPane().add(lblVersion);

		

		ActionListener updateClockAction = new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				// Assumes clock is a custom component
				clockLab.setText(System.currentTimeMillis()+""); 
				// OR
				// Assumes clock is a JLabel
				Date dd = new Date();
				SimpleDateFormat sdff = new SimpleDateFormat("dd/MMM/yyyy hh:mm:ss");
				clockLab.setText(sdff.format(dd)); 
			}
		};
		 
		Timer t = new Timer(1000, updateClockAction);
		t.start();

		////////////////////////////


 
		JLabel label_2 = new JLabel(packageName);
		label_2.setForeground(new Color(56, 119, 128));
		label_2.setFont(new Font("Book Antiqua", Font.BOLD | Font.ITALIC, 22));
		label_2.setBounds(380, 28, 348, 22);
		getContentPane().add(label_2);

		JPanel panel = new JPanel();
		//panel.setBackground(new Color(0, 96, 192));
		panel.setBackground(new Color(148, 162, 115));
		panel.setBounds(0, 60, 1018, 3);
		getContentPane().add(panel);
		
		panel_2 = new JPanel();
		panel_2.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_2.setBounds(330, 277, 368, 173);
		getContentPane().add(panel_2);
		
		panel_3 = new JPanel();
		panel_3.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		//panel_3.setBackground(SystemColor.textHighlight);
		panel_3.setBackground(new Color(154, 168, 128));
		panel_3.setBounds(330, 244, 368, 37);
		getContentPane().add(panel_3);
		 
		setAlwaysOnTop(true);



	}
 
	
	public void resetAll()
	 {
		 
				 spackage.setSelectedIndex(0);
	 }		 
	 

	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equalsIgnoreCase("Exit"))
		{
			dispose();
		}
		
	    if(e.getActionCommand().equalsIgnoreCase("Submit") )
	    {
			try
			{
				checkPassword();
			}
			catch(Exception ez)
			{
				System.out.println("error "+ez.getStackTrace());
				ez.printStackTrace();
			}

		}

		
	}
	
	public void submitForm()
	{
		brn = (ContractMastDto) spackage.getSelectedItem();
		add=0;
		if(brn.getCmp_code()==99)
		{	
			add=1;
			loginDt = new LoginDto();
			loginDt.setFooter("");
			loginDt.setBrnnm(brn.getCmp_name());
			loginDt.setDepo_code(brn.getDepo_code());
			loginDt.setCmp_code(brn.getNewcode());
			new ContractorMaster().setVisible(true);
		}
		else
			new DivOpt(uname, pass,brn,uid).setVisible(true);
		resetAll();
	}

	
	public void checkPassword()
	{
		brn = (ContractMastDto) spackage.getSelectedItem();
		String pass= password.getText().toString().trim();
		if(brn.getCmp_code()!=99)
			pass="Prompt";
		
		if(pass.equals("Prompt"))
		{
			submitForm();
			dispose();
		}
		else
		{
			alertMessage(PackageOpt.this, "Please Enter Correct Password");
			password.setText("");
			password.requestFocus();
			//resetAll();
		}
	}
	
	
	public void setVisible(boolean b)
	{
		lblpass.setVisible(false);
		password.setVisible(false);
		submitButton.setBounds(454, 375, 82, 30);
		exitButton.setBounds(549, 375, 82, 30);
		super.setVisible(true);

	}
}