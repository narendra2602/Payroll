package com.payroll.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;

import com.payroll.dao.UserDAO;

public class ChangePassword extends BaseClass implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	private JLabel reportName ;
	private JButton submitButton,cancelButton;
	private JPasswordField passwordF,oldpasswordF,confirmpasswordF;
	private JLabel lblPassword,lblOldPassword,lblNewPassword;
	private JLabel hintTextLabel;
	UserDAO udao=null;
	public ChangePassword()
	{
		//setUndecorated(true);
		setResizable(false);
		setSize(400, 283);	
		setLocationRelativeTo(null);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		getContentPane().setLayout(null);
		
		udao = new UserDAO();
		
		///////////////////////////////////////////////////////////

		
		
		reportName = new JLabel("Change Password");
		reportName.setHorizontalAlignment(SwingConstants.CENTER);
		reportName.setFont(new Font("Tahoma", Font.BOLD, 14));
		reportName.setBounds(48, 19, 303, 20);
		getContentPane().add(reportName);
		

		lblOldPassword = new JLabel("Old Password:");
		lblOldPassword.setBounds(48, 76, 127, 20);
		getContentPane().add(lblOldPassword);
		
		
		hintTextLabel = new JLabel("CapsLock is ON");
		hintTextLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		hintTextLabel.setForeground(Color.GRAY);
		hintTextLabel.setBounds(185, 80, 159, 20);
		getContentPane().add(hintTextLabel);
		
		
		oldpasswordF = new JPasswordField();
		oldpasswordF.setBounds(180, 76, 159, 25);
		oldpasswordF.setSelectionStart(0);
		getContentPane().add(oldpasswordF);
		oldpasswordF.addKeyListener(new KeyAdapter() 
		{
			
			public void keyReleased(KeyEvent keyEvent) 
			{
				if(oldpasswordF.getText().trim().length()>=1)
				{
					hintTextLabel.setVisible(false);
				}
				else
				{
					hintTextLabel.setVisible(true);
				}
				
			}

			public void keyPressed(KeyEvent evt) 
			{
				if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) 
				{
					resetAll();
					dispose();
				}
				
				if (evt.getKeyCode() == KeyEvent.VK_CAPS_LOCK) 
				{
					if (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK))
					{
						hintTextLabel.setVisible(true);
						hintTextLabel.setText("CapsLock is ON");
					}
					else
					{
						hintTextLabel.setVisible(true);
						hintTextLabel.setText("CapsLock is OFF");
					}
				}
				if (evt.getKeyCode() == KeyEvent.VK_ALPHANUMERIC)
				{
					hintTextLabel.setVisible(false);
				}

				
				if (evt.getKeyCode() == KeyEvent.VK_ENTER) 
				{
					String password = oldpasswordF.getText().toString().trim();
					if(password.equals(loginDt.getLogin_pass()))
					{
						passwordF.requestFocus();
					}
					else
					{
						JOptionPane.showMessageDialog(ChangePassword.this,"Please Enter Correct Password","Incorrect Password",JOptionPane.INFORMATION_MESSAGE);
						oldpasswordF.setText("");
						//oldpasswordF.requestFocus();
					}
					evt.consume();
				}
				
			}
		});

		
		lblNewPassword = new JLabel("New  Password:");
		lblNewPassword.setBounds(48, 112, 127, 20);
		getContentPane().add(lblNewPassword);


		lblPassword = new JLabel("Confirm Password:");
		lblPassword.setBounds(48, 150, 127, 20);
		getContentPane().add(lblPassword);

		
		passwordF = new JPasswordField();
		passwordF.setBounds(180, 112, 159, 25);
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
					confirmpasswordF.requestFocus();
				}
			}
		});
		
		

		
		confirmpasswordF = new JPasswordField();
		confirmpasswordF.setBounds(180, 148, 159, 25);
		confirmpasswordF.setSelectionStart(0);
		getContentPane().add(confirmpasswordF);
		confirmpasswordF.addKeyListener(new KeyAdapter() 
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
					String password = passwordF.getText().toString().trim();
					String newpassword = confirmpasswordF.getText().toString().trim();
					if(password.equals(newpassword))
					{
						submitButton.requestFocus();
					}
					else
					{
						confirmpasswordF.setText("");
						//confirmpasswordF.requestFocus();
						alertMessage(ChangePassword.this, "Password mismatch!!!");

					}
					
				}
			}
		});
		
		
		submitButton = new JButton("Submit");
		submitButton.setBounds(96, 209, 90, 30);
//		submitButton.setIcon(submitIcon);
		getContentPane().add(submitButton);
		submitButton.addKeyListener(new KeyAdapter() 
		{
			public void keyReleased(KeyEvent evt) 
			{
				if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) 
				{
					resetAll();
					dispose();
				}
				
				if (evt.getKeyCode() == KeyEvent.VK_ENTER) 
				{
					changePassword();
				}
			}
		});
		
		
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(199, 209, 90, 30);
//		cancelButton.setIcon(cancelIcon);
		getContentPane().add(cancelButton);
		 
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(29, 59, 338, 139);
		getContentPane().add(panel);

 
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("cancel");
		submitButton.addActionListener(this);
		submitButton.setActionCommand("submit");
		 
		setAlwaysOnTop(true);
		
		
	}

	public void changePassword()
	{
			int i=udao.changePasword(loginDt.getLogin_id(), confirmpasswordF.getText().trim().toLowerCase());
	
			if (i>0)
				alertMessage(ChangePassword.this, "Password Changed successfully");
			
			resetAll();
			dispose();

			
			
	}
	
	 public void resetAll()
	 {
		  oldpasswordF.setText("");
		  passwordF.setText("");
		  confirmpasswordF.setText("");
		  
	 }
	 

	public void actionPerformed(ActionEvent e) 
	{
		System.out.println("Enter key pressed "+e.getActionCommand());
		 
		if(e.getActionCommand().equalsIgnoreCase("cancel"))
		{
			resetAll();
			dispose();
		}
		
	    if(e.getActionCommand().equalsIgnoreCase("submit"))
	    {
			try
			{
				changePassword();
				 
			}
			catch(Exception ez)
			{
				System.out.println("error");
				ez.printStackTrace();
			}

		}

		 
		
		 
	}
}


