package com.payroll.print;
import java.awt.Desktop;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

import com.java4less.textprinter.JobProperties;
import com.java4less.textprinter.PrinterFactory;
import com.java4less.textprinter.TextPrinter;
import com.java4less.textprinter.TextProperties;
import com.java4less.textprinter.ports.FilePort;
import com.payroll.dao.PayrollDAO;
import com.payroll.dto.EmptranDto;
import com.payroll.util.FigToWord;
import com.payroll.view.BaseClass;


public class LabelPrint  extends BaseClass
{    
	 
	 
	FilePort port;
	TextPrinter printer;
	JobProperties job;
	TextProperties prop,propBold,p,propItalic;
	int r,i;
	int dash;
	SimpleDateFormat sdf;
	DecimalFormat df;
	int sinv,depo,einv,year,div,optn;
	ArrayList<?> esicList;
	EmptranDto vd;
	private String  drvnm,printernm; 
	 
	FigToWord fg; 	
	private Vector<?> col;
	int ln,mncode,doc_tp,lotno;	
	Calendar cal;
	String brnnm,divnm;
	 
	int depo_code,cmp_code,fyear,mnth_code,btnno;
	private String  monthname;
	String emp_name,emp_code;
	double salary;
	public LabelPrint(Integer depo_code,Integer cmp_code,Integer fyear,Integer mnth_code,String cmp_name,String drvnm,String monthname,Integer btnno,Integer repno,String printernm)
	{
		try {

			
			this.r=0;
			this.i=0;
		    
	    	this.monthname=monthname;
	    	this.depo_code=depo_code;
	    	this.printernm=printernm;
	    	this.cmp_code=cmp_code;
	    	this.fyear=fyear;
	    	this.mnth_code=mnth_code;
	    	this.drvnm=drvnm;
	    	this.btnno=btnno;
			this.dash=102;
			 
			
			jbInit();

			
			File file=null;

			if (Desktop.isDesktopSupported()) {
				if (btnno==1)
				{
					file=new File(drvnm+"\\label.txt");
					Desktop.getDesktop().edit(file);
					
				}

	       
			}

 		 

		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
 

 
	  private void jbInit() throws Exception {
		  
		   
		    try {
 
		 
		    	if (btnno==1)
		        {
		 	        this.port=new FilePort(drvnm+"\\label.txt");
		 	        this.printer=PrinterFactory.getPrinter("PLAIN");
		        } 	

		    	if (btnno==2)
		        {
		        	this.port=new FilePort(printernm);
		 	        this.printer=PrinterFactory.getPrinter("PLAIN");
		        } 	

		        
		        this.invoicePrint();


		    } catch (Exception ex) {
		      ex.printStackTrace();
		    }
		  }
	 
	 

	/**
	 * test characters set conversion
	 */
	 
	public void invoicePrint() {

		try {

	    	PayrollDAO pdao = new PayrollDAO();
	    	esicList=pdao.getSalaryRegister(depo_code, cmp_code, fyear, mnth_code,0);

			int lsize=esicList.size()-1;
			sdf = new SimpleDateFormat("dd/MM/yyyy"); 
			df = new DecimalFormat("0.00");
			
			job= printer.getDefaultJobProperties();
			job.draftQuality=true;


			job.cols=90;
			job.rows=72;
			job.pitch=10;

			printer.startJob(port,job);


			p=printer.getDefaultTextProperties();
			p.fontName="Arial";
			p.bold=true;
			p.proportional=true;
			p.doubleWide=true;




			prop=printer.getDefaultTextProperties();
			propBold=printer.getDefaultTextProperties();
			propBold.bold=true;
			propItalic=printer.getDefaultTextProperties();
			propItalic.italic=true;

			r=3;
			
		for(int z=0;z<lsize;z++) // first file loop
		{
            vd= (EmptranDto) esicList.get(z);
            
    		//double totearn=vd.getBasic_value()+vd.getDa_value()+vd.getIncentive_value()+vd.getOt_value();
    		//double totded=vd.getPf_value()+vd.getEsis_value()+vd.getAdvance();
    		//double net = totearn-totded;

    		double totearn=vd.getBasic_value()+vd.getDa_value()+vd.getHra_value()+vd.getAdd_hra_value()+vd.getIncentive_value()+vd.getSpl_incen_value()+vd.getOt_value()+vd.getLta_value()+vd.getMedical_value()+vd.getMisc_value()+vd.getStair_value();
    		double totded=vd.getPf_value()+vd.getEsis_value()+vd.getAdvance();
    		double net = totearn-totded;
    		
    		 
    		
    		if(z%2==0)
    		{
    			//emp_name=vd.getEmp_code()+" - "+vd.getEmp_name();
    			emp_code=String.valueOf(vd.getEmp_code());
    			emp_name=vd.getEmp_name();
    			salary=net;
    		}
    		else 
    		{
    			printer.printString(emp_code, r,6,prop);
    			printer.printString(String.valueOf(vd.getEmp_code()), r,48,prop);
    			printer.printString(df.format(salary), r,30,prop);
    			printer.printString(df.format(net), r,70,prop);
    			r++;
    			r++;
    			r++;
    			printer.printString(emp_name, r,6,prop);
    			printer.printString(vd.getEmp_name(), r,48,prop);
    			r++;
    			r++;

    			printer.printString(monthname, r,6,prop);
    			printer.printString(monthname, r,48,prop);
    			r++;
    			r=r+6;
    			emp_code="";
    			emp_name="";
    			salary=0.00;
    		}
    		
    		if(r>72)
    		{
    			r=3;
    			printer.newPage();
    			
    		}
            
		}
				if(salary>0)
				{
					printer.printString(emp_code, r,6,prop);
					printer.printString(df.format(salary), r,30,prop);
					r++;
	    			r++;
	    			r++;
					printer.printString(emp_name, r,6,prop);
					r++;
	    			r++;
					printer.printString(monthname, r,6,prop);
	    			r++;
				}
		
			printer.endJob();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
