package com.payroll.view;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.Timer;
import javax.swing.UIManager;

import com.payroll.dao.MenuDAO;
import com.payroll.dto.ChildMenuDto;
import com.payroll.dto.ContractMastDto;
import com.payroll.dto.MenuDto;
import com.payroll.dto.SubMenuDto;

public class MenuFrame extends BaseClass implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	private JMenuBar menubar;
	private JToolBar toolbar;
	private ImageIcon exitIcon,switchIcon ;
	private JButton exitButton,switchButton ;
	private JLabel lblAristoPharmaceuticalsPvt;
	private JLabel lblPromptSoftwareConsultants;
	private JLabel lblNewLabel;
	private JLabel lblPhone;
	private JPanel panel_1;

	private Font font;
	 
	private JPanel panel_2;
	private JPanel panel;
	
	
	
	MenuFrame(ContractMastDto brn,int uid)
	{
		
		 System.out.println("MenuFrame called");
		
		 
		font =new Font("Tahoma", Font.PLAIN, 11);
		///////// frame setting////////////////////////////////////////////////		


//		setTitle("Aristo Pharmaceuticals Pvt. Ltd.");
//		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		setSize(1024, 768);
		setResizable(false);
		//setLocationRelativeTo(null);
		getContentPane().setLayout(null);


		/////////////////////////////////////
//		arisLogo = new ImageIcon(getClass().getResource("/images/aris_log.png"));
//		arisLogo1 = new ImageIcon(getClass().getResource("/images/alogo.png"));
 
		
		

		
		
		panel_2 = new JPanel();
		panel_2.setBounds(844, 239, 174, 31);
		panel_2.setBackground(SystemColor.textHighlight);
		getContentPane().add(panel_2);
		
		
		
		
//		JLabel arisleb = new JLabel(arisLogo);
//		arisleb.setBounds(4, 7, 40, 52);
//		getContentPane().add(arisleb);

		
		
		
		lblAristoPharmaceuticalsPvt = new JLabel(brn.getCmp_name()+(brn.getCmp_city()!=null?(","+brn.getCmp_city()):""));
		lblAristoPharmaceuticalsPvt.setForeground(new Color(56, 119, 128));
		lblAristoPharmaceuticalsPvt.setFont(new Font("Book Antiqua", Font.BOLD , 20));
		lblAristoPharmaceuticalsPvt.setBounds(420, 7, 550, 22);
		getContentPane().add(lblAristoPharmaceuticalsPvt);

		//lblAccountingYear = new JLabel("Accounting Year 2012-2013    Month: Apr-2012");
		
		lblAccountingYear = new JLabel(loginDt.getMessage());
		lblAccountingYear.setForeground(Color.BLACK);
		lblAccountingYear.setFont(new Font("Verdana", Font.BOLD, 14));
		lblAccountingYear.setBounds(614, 38, 394, 15);
		getContentPane().add(lblAccountingYear);

		
		
		lblFinancialAccountingYear = new JLabel(loginDt.getFooter());
		lblFinancialAccountingYear.setForeground(Color.BLACK);
		lblFinancialAccountingYear.setBounds(314, 672, 477, 15);
		getContentPane().add(lblFinancialAccountingYear);
		lblFinancialAccountingYear.setFont(new Font("Tahoma", Font.BOLD, 13));
		 
   		stock_status = new JLabel(loginDt.getTotinv());
		stock_status.setForeground(Color.RED);
		stock_status.setBounds(66, 179, 255, 20);
		stock_status.setVisible(false);
		stock_status.setFont(new Font("Tahoma", Font.BOLD, 13));
		getContentPane().add(stock_status);	
		
		//getContentPane().add(arilogo);

		///// add some glue so subsequent items are pushed to the right
		toolbar = new JToolBar();
		toolbar.setSize(1018, 65);
		toolbar.setLocation(0, 90);
		toolbar.setFloatable(false);


		exitIcon = new ImageIcon(getClass().getResource("/images/exit.png"));
		switchIcon = new ImageIcon(getClass().getResource("/images/switch.png"));
		 


		exitButton = new JButton(exitIcon);
		switchButton = new JButton(switchIcon);
		 
		//////////////////////////////////////////////////////////////////
		toolbar.add(Box.createHorizontalGlue());   
		
		
		if(userID!=1) ///for admin no need to switch button.
		{
			//toolbar.add(favButton);
			toolbar.add(switchButton);
		}
		
		toolbar.add(exitButton);
		getContentPane().add(toolbar, null);


		lblFinancialAccountingYear.setVisible(false);




		///////////menubar/////////////////////////////
		menubar = new JMenuBar();
		//menubar.setBounds(0, 65, 1018, 30);
		menubar.setSize(1018, 30);
		menubar.setLocation(0, 60);

		MenuDAO mdao = new MenuDAO();
		MenuDto mdto =null;
		int pack=1;
		if(uid==99)
			pack=5;
		
		ArrayList<?> list = mdao.getMainMenu(pack,uid);

		int ls=list.size();
		for(int i =0;i<ls;i++)
		{
			mdto = (MenuDto) list.get(i);
			JMenu myMenu = getFileMenu(mdto.getTab_name(),mdto.getMenu_name());
			
			menubar.add(myMenu);
			//setJMenuBar(menubar);
		}

		getContentPane().add(menubar);



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

		exitButton.setActionCommand("Exit");
		switchButton.setActionCommand("Switch");
		 
		
		
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
		
		panel = new JPanel();
		panel.setBackground(SystemColor.textHighlight);
		panel.setBounds(842, 239, 2, 428);
		getContentPane().add(panel);
		exitButton.addActionListener(this);
		switchButton.addActionListener(this);
		 
 	

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
		
		//setAlwaysOnTop(true);
		
		
		panel.setVisible(false);
		panel_2.setVisible(false);
		

	}
	
	


	private JMenu getFileMenu(String tab,List<?> menuItem) 
	{
		JMenu myMenu = new JMenu("  "+tab+"  ");
		SubMenuDto smdto=null;
		ChildMenuDto cmdto=null;
		int smenusize=0;
		
		myMenu.setMnemonic(tab.charAt(0));
	
		if (tab.startsWith("Tools"))
			myMenu.setMnemonic(tab.charAt(1));

		
		int sz=menuItem.size();
		for(int j=0;j<sz;j++) 
		{
			smdto = (SubMenuDto) menuItem.get(j);
			smenusize=smdto.getSmenu_name().size();
			if (smenusize==0)
			{
				MyMenuItem myItem = new MyMenuItem(smdto.getMenu_name(),smdto.getClass_name(),smdto.getSubclass_name());
				
				if (smdto.getClass_name().startsWith("com"))
				{
				  myItem.setForeground(Color.black);
				  myMenu.add(myItem);
				}
//				else
//				  myItem.setForeground(Color.GRAY);
					
			}
			else
			{
				MyMenu menu = new MyMenu(smdto.getMenu_name());;
				for(int k=0;k<smenusize;k++)
				{
					cmdto =(ChildMenuDto) smdto.getSmenu_name().get(k);
					MyMenuItem myItem = new MyMenuItem(cmdto.getSmenu_name(),cmdto.getClass_name(),cmdto.getSubclass_name());
					 
					if (cmdto.getClass_name().startsWith("com"))
					{
					    myItem.setForeground(Color.black);
						menu.add(myItem);
					}
//					else
//						myItem.setForeground(Color.RED);

				}
				myMenu.add(menu);

			}
		}
		return myMenu;
	}


	private class MyMenu extends JMenu implements ActionListener 
	{
		private static final long serialVersionUID = 1L;
		public MyMenu(String text) 
		{
			super(text);
			addActionListener(this);
		}
		public void actionPerformed(ActionEvent e) 
		{
			System.out.println("Menu clicked: "+e.getActionCommand());
		}
	}

	private class MyMenuItem extends JMenuItem implements ActionListener 
	{
		private static final long serialVersionUID = 1L;
		private String nam;
		private String repName;
		public MyMenuItem(String text,String code,String subclass) 
		{
			super(text);
			setActionCommand(code);
			nam=subclass;
			repName=text;
			addActionListener(this);
		}
		public void actionPerformed(ActionEvent evt) 
		{
			System.out.println("Item clicked: "+evt.getActionCommand());
			String c=evt.getActionCommand();
			try 
			{
				//map.get(c);
				//String nam="com.aristo.print.MISRepo19";

				
				panel.setVisible(false);
				panel_2.setVisible(false);
				
				
				
				JFrame invoker=(JFrame) map.get(repName);
				infoName=null;
				
				if(invoker==null)
				{
					Class<?> clazz = Class.forName(c);
					
						if(nam==null)
						{
							invoker =(JFrame) clazz.newInstance();
							System.out.println("No Name invoker");
							map.put(repName, invoker);
							System.out.println("yaha aaya 3");

						}
						else
						{
							Constructor<?> constructor = clazz.getConstructor(String.class,String.class);
							//create an instance
							
							invoker =(JFrame) constructor.newInstance(nam,repName);
							map.put(repName, invoker);
							System.out.println("yaha aaya 4*****");
						}
				}
				
				//setting keyboard caps lock button 
				if(!Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK))
				{
					Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_CAPS_LOCK, true);
				} 
				
				
				invoker.repaint();
				invoker.setVisible(true);
				invoker.toFront();
				
			} 
			catch(Exception e) 
			{
			    	e.printStackTrace();
			} 
		}
	}

	public void actionPerformed(ActionEvent e) 
	{
		System.out.println(e.getActionCommand());
		
		panel.setVisible(false);
		panel_2.setVisible(false);
		
		if(e.getActionCommand().equalsIgnoreCase("Exit"))
		{
			dispose();
			System.exit(0);
		}
		
		
		
		
		
		if(e.getActionCommand().equalsIgnoreCase("Switch"))
		{
			map=null;
			map = new HashMap();
			System.gc(); 
			java.awt.Window win[] = java.awt.Window.getWindows();
			System.out.println("no. of windows is "+win.length);
			int visibleWins=0;

			for(int i=0;i<win.length;i++)
			{ 
				if(win[i].isVisible())
				{
					visibleWins++;
				}
			}
			
			System.out.println("no. of windows Visible wins "+visibleWins);
			if(visibleWins>1)
			{
				int answer = JOptionPane.showOptionDialog(this, "Close all opened windows before switching \n or you might loose some data","Switching Yes/No",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null,  new String[] {"Yes", "No"}, "No");

				if (answer == JOptionPane.YES_OPTION) 
				{
					for(int i=0;i<win.length;i++)
					{
						win[i].dispose();
						win[i]=null;
					}
					//DivOpt divOpt=null;
					new PackageOpt(loginDt.getLogin_name(), loginDt.getLogin_pass()).setVisible(true);
//					divOpt =new DivOpt(loginDt.getLogin_name(), loginDt.getLogin_pass(),loginDt.getPack_code(),loginDt.getLogin_id());
//					divOpt.setVisible(true);
					dispose();
				}
			}
			else
			{
				//DivOpt divOpt=null;
				new PackageOpt(loginDt.getLogin_name(), loginDt.getLogin_pass()).setVisible(true);
//				divOpt =new DivOpt(loginDt.getLogin_name(), loginDt.getLogin_pass(),loginDt.getPack_code(),loginDt.getLogin_id());
//				divOpt.setVisible(true);
				dispose();
			}
			
			
			
		}
		
	}
	
	
	public void dispose()
	{
		super.dispose();
		//setting keyboard caps lock button 
		Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_CAPS_LOCK, false);
	}
}