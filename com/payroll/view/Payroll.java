package com.payroll.view;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import com.payroll.dao.ConnectionFactory;
import com.payroll.dao.LoginDAO;
import com.payroll.dto.ContractMastDto;
import com.payroll.dto.LoginDto;

import de.javasoft.plaf.synthetica.SyntheticaGreenDreamLookAndFeel;

 

public class Payroll extends BaseClass  
{
	private static final long serialVersionUID = 1L;
	 
	 
	private JLabel lblPromptSoftwareConsultants;
	private JLabel lblNewLabel;
	private JLabel lblPhone;
	private JPanel panel_1;
	//	private JLabel lblAccountingYear;
	private Font font; 
	

	private JLabel userName ,password;
	private JTextField usernameF;
	private JPasswordField passwordF;
	private JButton loginButton,cancelButton;
	LoginDAO ldao;
	private JPanel panel_3;
	private Icon loadingIcon;
	private JLabel lodingLabel;
	  
	  
	Payroll()
	{
		font =new Font("Tahoma", Font.PLAIN, 11);
		///////// frame setting////////////////////////////////////////////////		
	 
		ldao = new LoginDAO();
 
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1024, 768);
		setResizable(false);
 
		getContentPane().setLayout(null);


		/////////////////////////////////////
//		arisLogo = new ImageIcon(getClass().getResource("/images/aris_log.png"));
//		JLabel arisleb = new JLabel(arisLogo);
//		arisleb.setBounds(4, 7, 40, 52);
//		getContentPane().add(arisleb);

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


		////////////////////////////
		

		userName = new JLabel("Username: ");
		userName.setBounds(406, 317, 72, 20);
		getContentPane().add(userName);

		usernameF = new JTextField();
		usernameF.setBounds(486, 315, 192, 25);
		getContentPane().add(usernameF);

		password = new JLabel("Password: ");
		password.setBounds(406, 353, 70, 20);
		getContentPane().add(password);

		passwordF = new JPasswordField();
		passwordF.setBounds(486, 351, 192, 25);
		getContentPane().add(passwordF);

		
		/**
		 * setting default password for developers (prompt) 
		 */
		String uiNamePassword[] = ConnectionFactory.getUINamePassword();
		if(uiNamePassword[0]!=null)
		{
			usernameF.setText(uiNamePassword[0]);
			passwordF.setText(uiNamePassword[1]);
		}
		
		
		
		usernameF.addKeyListener(new KeyAdapter()
		{
			public void keyReleased(KeyEvent evt)
			{
				if(evt.getKeyCode() == KeyEvent.VK_ENTER)
				{
					passwordF.requestFocus();
				}
				evt.consume();
			}
		});


		passwordF.addKeyListener(new KeyAdapter()
		{
			public void keyReleased(KeyEvent evt)
			{
				if(evt.getKeyCode() == KeyEvent.VK_ENTER)
				{
					loginButton.requestFocus();

				}
				evt.consume();
			}
		});

  

		Icon icon = new ImageIcon(getClass().getResource("/images/key.png"));
		JLabel label = new JLabel(icon);
		label.setBounds(339, 314, 64, 64);
		getContentPane().add(label);

		loadingIcon = new ImageIcon(getClass().getResource("/images/loader.gif"));
		lodingLabel = new JLabel(loadingIcon);
		lodingLabel.setBounds(474, 383, 48, 48);
		lodingLabel.setVisible(false);
		getContentPane().add(lodingLabel);
		
		 
		
		
		loginButton = new JButton(" Login ");
		loginButton.setBounds(531, 389, 72, 36);
		getContentPane().add(loginButton);

		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(606, 389, 72, 36);
		getContentPane().add(cancelButton);

 
		JLabel label_2 = new JLabel(packageName);
		label_2.setForeground(new Color(56, 119, 128));
		label_2.setFont(new Font("Book Antiqua", Font.BOLD | Font.ITALIC, 22));
		label_2.setBounds(380, 28, 348, 22);
		getContentPane().add(label_2);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(148, 162, 115));
		panel.setBounds(0, 60, 1018, 3);
		getContentPane().add(panel);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_2.setBounds(330, 277, 368, 173);
		getContentPane().add(panel_2);
		
		JLabel lblLogin = new JLabel("Login");
		//lblLogin.setForeground(new Color(255, 255, 255));
		lblLogin.setFont(new Font("Verdana", Font.BOLD, 13));
		lblLogin.setBounds(341, 249, 72, 20);
		getContentPane().add(lblLogin);
		
		panel_3 = new JPanel();
		panel_3.setBackground(new Color(154, 168, 128));
		panel_3.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel_3.setBounds(330, 244, 368, 37);
		getContentPane().add(panel_3);
		
		JLabel lblBestViewedWith = new JLabel("Best viewed in 1024x768  resolution");
		lblBestViewedWith.setHorizontalAlignment(SwingConstants.CENTER);
		lblBestViewedWith.setBounds(236, 635, 589, 20);
		getContentPane().add(lblBestViewedWith);


		loginButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				lodingLabel.setVisible(true);
				String uname = usernameF.getText().trim();
				@SuppressWarnings("deprecation")
				String pass = passwordF.getText().trim();
				if(authentification(uname, pass))
				{
					 
					if(uname.equalsIgnoreCase("admin"))
					{
						userID=1;
						ContractMastDto bdto= new ContractMastDto();
						loginDt = new LoginDto();
						loginDt.setMessage("");
						loginDt.setFooter("");
						
						bdto.setCmp_name("Admin");
						new MenuFrame(bdto,99).setVisible(true);
					}
					else
					{
						new PackageOpt(uname, pass).setVisible(true);
						dispose();
					}
				}
				else
				{
					JOptionPane.showMessageDialog(Payroll.this,"Incorrect Username/Password ","Login fail",JOptionPane.INFORMATION_MESSAGE);
					usernameF.requestFocus();
				}

			}
		});

		loginButton.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent evt)
			{
				if(evt.getKeyCode() == KeyEvent.VK_ENTER)
				{
					String uname = usernameF.getText().trim();
					@SuppressWarnings("deprecation")
					String pass = passwordF.getText().trim();
					if(authentification(uname, pass))
					{
						if(uname.equalsIgnoreCase("admin"))
						{
							userID=1;
							ContractMastDto bdto= new ContractMastDto();
							loginDt = new LoginDto();
							loginDt.setMessage("");
							loginDt.setFooter("");
							
							bdto.setCmp_name("Admin");
							new MenuFrame(bdto,99).setVisible(true);
						}
						else
						{
							new PackageOpt(uname, pass).setVisible(true);
							dispose();
						}
					}
					else
					{
						JOptionPane.showMessageDialog(Payroll.this,"Incorrect Username/Password ","Login fail",JOptionPane.INFORMATION_MESSAGE);
						usernameF.requestFocus();
					}

					evt.consume();
				}
			}
		});

		cancelButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				dispose();
			}
		});


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
 
		

	}

	public boolean authentification(String userNm,String Pass) 
	{
		boolean res;
		res =ldao.authenticateUser(userNm, Pass);
		return res;
	}


	public static void main(String ars[])
	{
		
		SwingUtilities.invokeLater(new Runnable() {
		      public void run() {
		    	  try 
		  		{
		  			//UIManager.setLookAndFeel(new SyntheticaBlueMoonLookAndFeel());
		  			//UIManager.setLookAndFeel(new SyntheticaMauveMetallicLookAndFeel());
		  			UIManager.setLookAndFeel(new SyntheticaGreenDreamLookAndFeel());

		  		} 
		  		catch (Exception e) 
		  		{
		  			e.printStackTrace();
		  		}		    	  
		    		 
		        new Payroll().setVisible(true);
		      }
		    });
		
		 
	}




	
	 
}