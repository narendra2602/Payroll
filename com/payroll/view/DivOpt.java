package com.payroll.view;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import com.payroll.dao.LoginDAO;
import com.payroll.dto.ContractMastDto;

public class DivOpt extends BaseClass implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	 
	private SwingWorker<?,?> worker;
	private JLabel lblPromptSoftwareConsultants;
	private JLabel lblNewLabel;
	private JLabel lblPhone;
	private JPanel panel_1;
	 
	private Font font; 
	LoginDAO ldao;

	 
    
    private JPanel panel_2;
    private JPanel panel_3;
    private JLabel lblPackageSelection;
    private ContractMastDto brn;
	private String uname,pass; 
    private int uid;
    private Icon loadingIcon;
	private JLabel lodingLabel;
	private JProgressBar pb;
	final int MAX=100;
    public DivOpt(String uname,String pass,ContractMastDto brn,int uid)
	{
		this.uname=uname;
		this.pass=pass;
		this.brn=brn;
		this.uid=uid;
		
		font =new Font("Tahoma", Font.PLAIN, 11);
		///////// frame setting////////////////////////////////////////////////		
		 
		
		 
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1024, 768);
 
		setResizable(false);
		getContentPane().setLayout(null);


		
		pb = new JProgressBar();
		pb.setBounds(370, 320, 310, 20);
        pb.setMinimum(0);
        pb.setMaximum(MAX);
        pb.setStringPainted(true);
        pb.setVisible(false);
        getContentPane().add(pb);
        
		/////////////////////////////////////
		//arisLogo = new ImageIcon(getClass().getResource("/images/aris_log.png"));
		loadingIcon = new ImageIcon(getClass().getResource("/images/loader.gif"));
		
		lodingLabel = new JLabel(loadingIcon);
		lodingLabel.setBounds(500, 377, 48, 48);
		lodingLabel.setVisible(false);
		getContentPane().add(lodingLabel);
		
		lblPackageSelection = new JLabel("Branch Selection");
		//lblPackageSelection.setForeground(Color.WHITE);
		lblPackageSelection.setFont(new Font("Verdana", Font.BOLD, 13));
		lblPackageSelection.setBounds(341, 249, 180, 20);
		getContentPane().add(lblPackageSelection);
		
//		JLabel arisleb = new JLabel(arisLogo);
//		arisleb.setBounds(12, 15, 30, 37);
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


		 


 
		JLabel label_2 = new JLabel(brn.getCmp_name());
		label_2.setForeground(new Color(56, 119, 128));
		label_2.setFont(new Font("Book Antiqua", Font.BOLD | Font.ITALIC, 22));
		label_2.setBounds(420, 28, 348, 22);
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
		 
		
		///////////////////////////////////////////////////////////////////////////////////////////////

			pb.setVisible(true);
			lblPackageSelection.setText("Loading Please Wait.....");
			submitForm();
			setAlwaysOnTop(true);
		


	}
 
	
	 

	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equalsIgnoreCase("Exit"))
		{
			dispose();
		}
		
	    if(e.getActionCommand().equalsIgnoreCase("Submit") )
	    {
	    	
	    	submitForm();

		}

		
	}
	
	public class NumberWorker extends SwingWorker<String, Object> 
	{
		protected String doInBackground() throws Exception 
		{
			try
			{
					// update progressbar
					for (int i = 0; i <= MAX; i++) {
						final int currentValue = i;
						try {
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									pb.setValue(currentValue);

								}
							});
							java.lang.Thread.sleep(50);
						} catch (InterruptedException e) {
							JOptionPane.showMessageDialog(DivOpt.this, e.getMessage());
						}
					}
				 

				int div=brn.getDepo_code();
				int depo=brn.getCmp_code();
				
				
				System.out.println("div "+div+" dep "+depo);
				ldao = new LoginDAO();
				
				loginDt = ldao.getLoginInfo(uname,pass, div, depo,uid);
				loginDt.setDivnm(" ");
				loginDt.setDepo_code(div);
				loginDt.setCmp_code(depo);
				loginDt.setBdto(brn); 
				if(brn.getCmp_city()!=null)
					loginDt.setBrnnm(brn.getCmp_name()+","+brn.getCmp_city());
				else
					loginDt.setBrnnm(brn.getCmp_name());
				loginDt.setLogin_name(uname);
				loginDt.setLogin_pass(pass);
				loginDt.setPack_code(1);
				
				
				//UIManager.setLookAndFeel(new SyntheticaBlueMoonLookAndFeel());

				new MenuFrame(brn,loginDt.getLogin_id()).setVisible(true);
				lodingLabel.setVisible(false);
				dispose();
			}
			catch(Exception ez)
			{
				ez.printStackTrace();
			}
			
			return null;
		}
 
		 
		protected void done() 
		{
			try 
			{
				worker = null;
				worker.cancel(true);
				lodingLabel.setVisible(false);
			} 
			catch (Exception ignore) 
			{
				
			}
		}
	}
	
	public void submitForm()
	{
		
		
		lodingLabel.setVisible(true);
		worker = new NumberWorker();
		worker.execute();
		
	}			
	
}