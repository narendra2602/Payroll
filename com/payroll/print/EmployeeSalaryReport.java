package com.payroll.print;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.print.attribute.standard.MediaSize.Other;

import jxl.Cell;
import jxl.Sheet;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.PageOrientation;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.payroll.dao.PayrollDAO;
import com.payroll.dto.EmptranDto;
import com.payroll.excel.WriteExcel;


public class EmployeeSalaryReport  extends WriteExcel
{    
   
   int r,i,sno;
   SimpleDateFormat sdf;
   String inputFile;
    
   private String  drvnm,flname,cmp_name,monthname,address; 
   private int depo_code, cmp_code, fyear, mnth_code,btnno,repno,opt,emnth_code,code;
   private double totadv,totloan,atten_days,extra_hrs,arrear_days,sterlite_days,absent_days,btotal,bgtotal,ncp_days;
   private double basic,emppf,emppf1,eps,epf,edli,net,epfc,epsc,tot,epswages,edlwages;
   ArrayList<?> esicList;
   SheetSettings settings; 
   boolean print=false;
   int size=0;
private double tot4;
private double ntot4;
private double tot5;
private double ntot5;
private int tot6;
private int tot7;
private int ltot9;
private int ltot10;
private int ltot11;
  public EmployeeSalaryReport(Integer depo_code,Integer cmp_code,Integer fyear,Integer mnth_code,String cmp_name,String drvnm,String monthname,Integer btnno,Integer repno,Integer opt,String address,Integer emnth_code,Integer code) 
  
  {
    try 
    {
    	this.r=0;
    	 
    	this.drvnm=drvnm;
	    this.cmp_name=cmp_name;
    	this.monthname=monthname;
    	this.depo_code=depo_code;
    	this.cmp_code=cmp_code;
    	this.fyear=fyear;
    	this.mnth_code=mnth_code;
    	this.btnno=btnno;
    	this.repno=repno;
    	this.opt=opt;
    	this.address=address;
    	this.emnth_code=emnth_code;
    	this.code=code;
    	sdf = new SimpleDateFormat("dd/MM/yyyy");
    	
    	
    	flname="EmployeeSalaryList-"+cmp_name.substring(0, 6)+"-"+monthname;
/*    	else if(repno==19)
    		flname="SalaryDetail-"+cmp_name.substring(0, 6)+"-"+(fyear+1);
*/


        jbInit();
  
    	File file=null;
    	
		if (Desktop.isDesktopSupported()) {
		
				
  				file=new File(drvnm+"\\"+flname+".xls");
  				Desktop.getDesktop().open(file);
  				
		}
        
         

    }
    catch(Exception e) 
    {
      e.printStackTrace();
    }
    
  }
  

  private void jbInit() throws Exception {
	  
	    try 
	    {
	    	PayrollDAO pdao = new PayrollDAO();
	    	
	    	size=monthname.length()-5;
	    	System.out.println("VALUE OF OPT "+opt+" repno "+repno+"  "+ monthname);
	    		esicList= (ArrayList<?>) pdao.getSalaryRegisterNew(depo_code, cmp_code, fyear, mnth_code,1903,emnth_code,code);
	    		repno=19;
	    	createExcel();

	    } catch (Exception ex) {
	      ex.printStackTrace();
	    }
	  }



   
//////excel file generation report ////////   
   
public void createExcel() throws WriteException, IOException {

	    
		setOutputFile(drvnm+"\\"+flname+".xls");
	   write();
}


public  void setOutputFile(String inputFile) {
	   this.inputFile = inputFile;
} 

public void write() throws IOException, WriteException {
	   File file = new File(inputFile);
	   WorkbookSettings wbSettings = new WorkbookSettings();

	   wbSettings.setLocale(new Locale("en", "EN"));

	   WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
//	   workbook.createSheet((flname.length()<15?flname:flname.substring(0, 15)), 0);
	   workbook.createSheet("Sheet1", 0);
	   WritableSheet excelSheet = workbook.getSheet(0);
	   settings = excelSheet.getSettings();

	   if(repno==1 || repno==2)
		  settings.setPrintTitlesRow(0, 0);
	   else
		   settings.setPrintTitlesRow(0, 4);
	   settings.setOrientation(PageOrientation.PORTRAIT);
	   settings.setLeftMargin(0);
	   settings.setRightMargin(0);
	   //settings.setFitWidth(1);

	   createLabel(excelSheet);
	   //settings.setDefaultRowHeight(360);

	   createContent(excelSheet);
	//   OutputStream out = new ByteArrayOutputStream();
	   
//	   CSV(workbook, out, "UTF8", false);
	   workbook.write();
	   workbook.close();
}


public void createHeader(WritableSheet sheet)
		   throws WriteException {
	    
	  
	   
//	   sheet.mergeCells(0, 1, 8, 1);
//	   addCaption1(sheet, 0, 1, "To: Accounts Deptt., Aristo Pharmaceuticals Pvt Ltd., Mandideep",30);

		switch(repno)
		{
			case 1:
					createHeader1(sheet); // ESIC List
					break;
			case 2:
					createHeader2(sheet); // PF List
					break;
			case 3:
					createHeader3(sheet); // Advance & Loan
					break;
			case 4:
					createHeader4(sheet); // Sterlite Days Report
					break;
			case 5:
					createHeader5(sheet); // Attendance Check List
					break;
			case 6:
					createHeader6(sheet); // OT Hrs Report
					break;
			case 7:
					createHeader7(sheet); // Absent Check List
					break;
			case 8:
					createHeader8(sheet); // Misc List
					break;
			case 9:
					createHeader9(sheet); // Present Check List
					break;					
			case 10:
					createHeader10(sheet); // Salary List Bank Details
					break;					
			case 11:
				createHeader11(sheet); // Coupon List
				break;
			case 12:
				createHeader12(sheet); // LTA/Medical List
				break;
			case 13:
				createHeader13(sheet); // LTA/Medical Register
				break;
			case 14:
				createHeader14(sheet); // UnPaid LTA/Medical Register
				break;
			case 15:
				createHeader15(sheet); // Machine Operator Register
				break;
			case 16:
				createHeader16(sheet); // Bonus List Bank Details
				break;					
			case 17:
				createHeader17(sheet); // Employee Basic Details
				break;					
			case 18:
				createHeader18(sheet); // Employee Earning Details
				break;					
			case 19:
				createHeader19(sheet); // Salary Detail Register
				break;					
		}
	
	   

//	   settings.setHorizontalFreeze(3);

		 if((repno==1 || repno==2) && btnno==1)
			 settings.setVerticalFreeze(1);
		 else if(repno==2)
			 settings.setVerticalFreeze(1);
		 else if(repno==19)
			 settings.setVerticalFreeze(5);
		 else
			 settings.setVerticalFreeze(4);

}  


public void createHeader1(WritableSheet sheet)
		   throws WriteException {
 
	
//	 	sheet.mergeCells(0, 0, 8, 0);
	   // Write a few headers
//	 	addCaption1(sheet, 0, 0, cmp_name+" ESIC List "+monthname,40);
	   if(btnno==1) // upload button
	   {
		   addCaption2(sheet, 0, 0, "Employee Code",10);
		   addCaption2(sheet, 1, 0, "IP Number",10);
		   addCaption2(sheet, 2, 0, "IP Name",30);
		   addCaption2(sheet, 3, 0, "No of Days for which wages paid/payable during the month",20);
		   addCaption2(sheet, 4, 0, "Total Monthly Wages",20);
		   addCaption2(sheet, 5, 0, "Reason Code for Zero workings days/numeric only: provide 0 for all other reasons",20);
		   addCaption2(sheet, 6, 0, "Last Working Day",20);
		   r=1;
	   }
	   else if(btnno==2) // excel button
	   {

			sheet.mergeCells(0, 0, 9, 0);
			   // Write a few headers
		   addCaption(sheet, 0, 0, cmp_name,40);
		   
		   sheet.mergeCells(0, 1, 9, 1);
		   addCaption1(sheet, 0, 1, "  ESIC "+(opt==2?"(Arrear)":opt==3?"Consolidated":"")+" LIST FOR THE MONTH OF "+monthname,40);

		   addCaption2(sheet, 0, 3, "Sno",10);
		   addCaption2(sheet, 1, 3, "Employee Code",10);
		   addCaption2(sheet, 2, 3, "ESIC Number",12);
		   addCaption2(sheet, 3, 3, "Paid Days ",20);
		   addCaption2(sheet, 4, 3, "Basic Wages",20);
		   addCaption2(sheet, 5, 3, "IP Name",30);
		   addCaption2(sheet, 6, 3, "Gross Wages",20);
		   if(mnth_code>201906)
		   {
			   addCaption2(sheet, 7, 3, "Employee Share 0.75%",15);
			   addCaption2(sheet, 8, 3, "Employer Share 3.25%",15);
		   }
		   else
		   {
			   addCaption2(sheet, 7, 3, "Employee Share 1.75%",15);
			   addCaption2(sheet, 8, 3, "Employer Share 4.75%",15);
		   }
		 

		   addCaption2(sheet, 9, 3, "Total Share ",15);
		   r=5;
	   }
		

}  

public void createHeader2(WritableSheet sheet)  throws WriteException {

	 
	   
	   if(btnno==2) // excel button
	   {
/*		   addCaption2(sheet, 0, 0, "Employee Number",10);
		   addCaption2(sheet, 1, 0, "Wages",12);
		   addCaption2(sheet, 2, 0, "EPF Contribution EE",15);
		   addCaption2(sheet, 3, 0, "Refund of EE",15);
		   addCaption2(sheet, 4, 0, "NCP days",12);
		   addCaption2(sheet, 5, 0, "Date of Leaving",12);
		   addCaption2(sheet, 6, 0, "Reason for Leaving",15);
		   addCaption2(sheet, 7, 0, "Wage arrears",12);
		   addCaption2(sheet, 8, 0, "EPF Contribution EE arrears",15);
		   addCaption2(sheet, 9, 0, "EPF Contribtuion ER arrears",15);
		   addCaption2(sheet, 10, 0, "EPS Contribution arrears",15);
			r=1;
*/
		   
		   
		   if(opt==1) // PF Radio Button
		   {
			   addCaption2(sheet, 0, 0, "EMP CODE",10);
			   addCaption2(sheet, 1, 0, "PF NO",10);
			   addCaption2(sheet, 2, 0, "UAN",15);
			   addCaption2(sheet, 3, 0, "AADHAR",15);
			   addCaption2(sheet, 4, 0, "PAN",15);

			   addCaption2(sheet, 5, 0, "MEMBER NAME ",30);
			   addCaption2(sheet, 6, 0, "GROSS WAGES",15);
			   addCaption2(sheet, 7, 0, "EPF WAGES",15);
			   addCaption2(sheet, 8, 0, "EPS WAGES",15);
			   addCaption2(sheet, 9, 0, "EDLI WAGES",15);
//			   addCaption2(sheet, 10, 0, "EPF CONTRIBUTUION REMITTED",15);
//			   addCaption2(sheet, 11, 0, "EPS CONTRIBUTUION REMITTED",15);
//			   addCaption2(sheet, 12, 0, "EPF EPS DIFF REMITTED",15);

			   addCaption2(sheet, 10, 0, "EPF EE SHARE 12 % CONTRIBUTION REMITTED",15); // heading change by yashpal on 13/07/2024
			   addCaption2(sheet, 11, 0, "EPS SHARE 8.33% CONTRIBUTUION REMITTED",15); // heading change by yashpal on 13/07/2024
			   addCaption2(sheet, 12, 0, "EPF ER SHARE 3.67%  DIFF REMITTED",15); // heading change by yashpal on 13/07/2024

			   addCaption2(sheet, 13, 0, "NCP DAYS",15);
			   addCaption2(sheet, 14, 0, "REFUND OF ADVANCES",15);
		   }
		   else if(opt==2) // Arrear Radio Button (excel)
		   {
			   addCaption2(sheet, 0, 0, "EMP CODE",10);
			   addCaption2(sheet, 1, 0, "PF NO",10);
			   addCaption2(sheet, 2, 0, "UAN",15);
			   addCaption2(sheet, 3, 0, "AADHAR",15);
			   addCaption2(sheet, 4, 0, "PAN",15);

			   addCaption2(sheet, 5, 0, "MEMBER NAME ",30);

			   addCaption2(sheet, 6, 0, "ARREAR GROSS WAGES",15); // heading change by yashpal on 13/07/2024
			   addCaption2(sheet, 7, 0, "ARREAR EPF WAGES",15); // heading change by yashpal on 13/07/2024
			   addCaption2(sheet, 8, 0, "ARREAR EPS WAGES",15); // heading change by yashpal on 13/07/2024
			   addCaption2(sheet, 9, 0, "ARREAR EDLI WAGES",15); // heading change by yashpal on 13/07/2024
			   addCaption2(sheet, 10, 0, "ARREAR EPF EE SHARE 12%",15); // heading change by yashpal on 13/07/2024
			   addCaption2(sheet, 11, 0, "ARREAR EPS  SHARE 8.33%",15); // heading change by yashpal on 13/07/2024
			   addCaption2(sheet, 12, 0, "ARREAR EPF ER  SHARE 3.67%",15); // heading change by yashpal on 13/07/2024
			   addCaption2(sheet, 13, 0, "NCP DAYS",15);
			   addCaption2(sheet, 14, 0, "ARREAR DAYS",15);
			   addCaption2(sheet, 15, 0, "PRESENT DAYS",15);

			   
/*			   addCaption2(sheet, 6, 0, "ARREAR EPF WAGES",15);
			   addCaption2(sheet, 7, 0, "ARREAR EPS WAGES",15);
			   addCaption2(sheet, 8, 0, "ARREAR EDLI WAGES",15);
			   addCaption2(sheet, 9, 0, "ARREAR  WAGES",15);
			   addCaption2(sheet, 10, 0, "ARREAR EPF EE SHARE",15);
			   addCaption2(sheet, 11, 0, "ARREAR EPF ER SHARE",15);
			   addCaption2(sheet, 12, 0, "ARREAR EPS SHARE",15);  
			   addCaption2(sheet, 13, 0, "ARREAR DAYS",15);
			   addCaption2(sheet, 14, 0, "PRESENT DAYS",15);
			   addCaption2(sheet, 15, 0, "NCP DAYS",15);*/
			   
		   }
		   else if(opt==3) // Consolidated Radio Button (excel)
		   {
			   addCaption2(sheet, 0, 0, "EMP CODE",10);
			   addCaption2(sheet, 1, 0, "PF NO",10);
			   addCaption2(sheet, 2, 0, "UAN",15);
			   addCaption2(sheet, 3, 0, "AADHAR",15);
			   addCaption2(sheet, 4, 0, "PAN",15);

			   addCaption2(sheet, 5, 0, "MEMBER NAME ",30);

			   addCaption2(sheet, 6, 0, "GROSS WAGES",15); // heading change by yashpal on 13/07/2024
			   addCaption2(sheet, 7, 0, "EPF WAGES",15); // heading change by yashpal on 13/07/2024
			   addCaption2(sheet, 8, 0, "EPS WAGES",15); // heading change by yashpal on 13/07/2024
			   addCaption2(sheet, 9, 0, "EDLI WAGES",15); // heading change by yashpal on 13/07/2024
			   addCaption2(sheet, 10, 0, "EPF EE SHARE 12 % CONTRIBUTION REMITTED",15); // heading change by yashpal on 13/07/2024
			   addCaption2(sheet, 11, 0, "EPS SHARE 8.33% CONTRIBUTUION REMITTED",15); // heading change by yashpal on 13/07/2024
			   addCaption2(sheet, 12, 0, "EPF ER  SHARE 3.67% DIFF REMITTED",15); // heading change by yashpal on 13/07/2024
			   addCaption2(sheet, 13, 0, "NCP DAYS",15);

			   addCaption2(sheet, 14, 0, "REDFUND OF ADVANCES",15);
			   addCaption2(sheet, 15, 0, "ARREAR NCP DAYS",15);
			   
			   addCaption2(sheet, 16, 0, "ARREAR DAYS",15);
			   addCaption2(sheet, 17, 0, "PRESENT DAYS",15);

			   
			   
		   }
		   r=1;

			
			
	   }
	   else if(btnno==1) // upload button
	   {
/*			sheet.mergeCells(0, 0, 8, 0);
			   // Write a few headers
		   addCaption(sheet, 0, 0, cmp_name,40);
		   
		   sheet.mergeCells(0, 1, 8, 1);
		   addCaption1(sheet, 0, 1, "  PF LIST FOR THE MONTH OF "+monthname,40);
		   
		   addCaption2(sheet, 0, 3, "Sno",10);
		   addCaption2(sheet, 1, 3, "PF Number",10);
		   addCaption2(sheet, 2, 3, "UID Number",12);
		   addCaption2(sheet, 3, 3, "Full Salary ",10);
		   addCaption2(sheet, 4, 3, "Employee Name",30);
		   addCaption2(sheet, 5, 3, "Paid Days ",20);
		   addCaption2(sheet, 6, 3, "Total Monthly Wages",20);
		   addCaption2(sheet, 7, 3, "Employee Share 12%",15);
		   addCaption2(sheet, 8, 3, "Employer Share 3.67%",15);
		   addCaption2(sheet, 9, 3, "Total Share (12%+3.67%)",15);
		   addCaption2(sheet, 10, 3, "EPS 8.33%",15);
		   addCaption2(sheet, 11, 3, "Total Share ",15);
			r=5;
*/			
			
			 if(opt==1) // PF Radio Button
			   {
				   addCaption2(sheet, 0, 0, "EMP CODE",10);
				   addCaption2(sheet, 0, 0, "UAN",15);
				   addCaption2(sheet, 1, 0, "AADHAR",15);
				   addCaption2(sheet, 2, 0, "PAN",15);
				   addCaption2(sheet, 3, 0, "MEMBER NAME ",30);
				   addCaption2(sheet, 4, 0, "GROSS WAGES",15);
				   addCaption2(sheet, 5, 0, "EPF WAGES",15);
				   addCaption2(sheet, 6, 0, "EPS WAGES",15);
				   addCaption2(sheet, 7, 0, "EDLI WAGES",15);
				   addCaption2(sheet, 8, 0, "EPF CONTRIBUTUION REMITTED",15);
				   addCaption2(sheet, 9, 0, "EPS CONTRIBUTUION REMITTED",15);
				   addCaption2(sheet, 10, 0, "EPF EPS DIFF REMITTED",15);
				   addCaption2(sheet, 11, 0, "NCP DAYS",15);
				   addCaption2(sheet, 12, 0, "REFUND OF ADVANCES",15);
			   }
			   else if(opt==2) // PF Arrear Upload  
			   {
				   addCaption2(sheet, 0, 0, "UAN",15);
				   addCaption2(sheet, 1, 0, "AADHAR",15);
				   addCaption2(sheet, 2, 0, "PAN",15);
				   addCaption2(sheet, 3, 0, "MEMBER NAME ",30);
				   addCaption2(sheet, 4, 0, "ARREAR  WAGES",15);
				   addCaption2(sheet, 5, 0, "ARREAR EPF WAGES",15);
				   addCaption2(sheet, 6, 0, "ARREAR EPS WAGES",15);
				   addCaption2(sheet, 7, 0, "ARREAR EDLI WAGES",15);
				   addCaption2(sheet, 8, 0, "ARREAR EPF EE SHARE",15);
				   addCaption2(sheet, 9, 0, "ARREAR EPS SHARE",15);
				   addCaption2(sheet, 10, 0, "ARREAR EPF ER SHARE",15);
				   addCaption2(sheet, 11, 0, "NCP DAYS",15);
				   addCaption2(sheet, 12, 0, "PRESENT DAYS",15);
				   
			   }
			   r=1;

	   }

}  


public void createHeader3(WritableSheet sheet)  throws WriteException {

	 
	   
	   
			sheet.mergeCells(0, 0, 4, 0);
			   // Write a few headers
		   addCaption(sheet, 0, 0, cmp_name,40);
		   
		   sheet.mergeCells(0, 1, 4, 1);
		   addCaption1(sheet, 0, 1, " ADVANCE/LOAN REPORT FOR THE MONTH OF "+monthname,40);
		   
		   addCaption2(sheet, 0, 3, "Sno",10);
		   addCaption2(sheet, 1, 3, "Employee Code",10);
		   addCaption2(sheet, 2, 3, "Employee Name",30);
		   addCaption2(sheet, 3, 3, "Advance ",20);
		   addCaption2(sheet, 4, 3, "Loan",20);
			r=5;
	    

}  


	public void createHeader4(WritableSheet sheet)  throws WriteException {
	
		sheet.mergeCells(0, 0, 6, 0);
		   // Write a few headers
	   addCaption(sheet, 0, 0, cmp_name,40);
	   
	   sheet.mergeCells(0, 1, 6, 1);
	   addCaption1(sheet, 0, 1, " STERILE AREA REPORT FOR THE MONTH OF "+monthname,40);
	   
	   addCaption2(sheet, 0, 3, "Sno",10);
	   addCaption2(sheet, 1, 3, "Employee Code",10);
	   addCaption2(sheet, 2, 3, "Employee Name",30);
	   addCaption2(sheet, 3, 3, "No. Of Days ",20);
	   addCaption2(sheet, 4, 3, "Rate",20);
	   addCaption2(sheet, 5, 3, "Amount",20);
	   addCaption2(sheet, 6, 3, "Signature",20);
		r=5;
	
	
	}
	
	
	public void createHeader5(WritableSheet sheet)  throws WriteException {
		
		sheet.mergeCells(0, 0, 7, 0);
		   // Write a few headers
	   addCaption(sheet, 0, 0, cmp_name,40);
	   
	   sheet.mergeCells(0, 1, 7, 1);
	   addCaption1(sheet, 0, 1, " ATTENDANCE CHECKLIST MONTH OF "+monthname,40);
	   
	   addCaption2(sheet, 0, 3, "Employee Code",10);
	   addCaption2(sheet, 1, 3, "Employee Name",30);
	   addCaption2(sheet, 2, 3, "Present Days ",15);
	   addCaption2(sheet, 3, 3, "O.T. Hrs",15);
	   addCaption2(sheet, 4, 3, "Extra Days",15);
	   addCaption2(sheet, 5, 3, "Sterlite Days",15);
	   addCaption2(sheet, 6, 3, "Advance ",15);
	   addCaption2(sheet, 7, 3, "Loan ",15);
		r=5;
	
	
	}
	

	public void createHeader6(WritableSheet sheet)  throws WriteException {
	
		sheet.mergeCells(0, 0, 6, 0);
		   // Write a few headers
	   addCaption(sheet, 0, 0, cmp_name,40);
	   
	   sheet.mergeCells(0, 1, 6, 1);
	   addCaption1(sheet, 0, 1, " OT Hrs REPORT FOR THE MONTH OF "+monthname,40);
	   
	   addCaption2(sheet, 0, 3, "Sno",10);
	   addCaption2(sheet, 1, 3, "Employee Code",10);
	   addCaption2(sheet, 2, 3, "Employee Name",30);
	   addCaption2(sheet, 3, 3, "OT Hrs ",20);
	   addCaption2(sheet, 4, 3, "OT Rate",20);
	   addCaption2(sheet, 5, 3, "Amount",20);
	   addCaption2(sheet, 6, 3, "Signature",20);
		r=5;
	
	
	}
	
	public void createHeader7(WritableSheet sheet)  throws WriteException {
		
		sheet.mergeCells(0, 0, 3, 0);
		   // Write a few headers
	   addCaption(sheet, 0, 0, cmp_name,40);
	   
	   sheet.mergeCells(0, 1, 3, 1);
	   addCaption1(sheet, 0, 1, " ABSENT REPORT FOR THE MONTH OF "+monthname,40);
	   
	   addCaption2(sheet, 0, 3, "Sno",10);
	   addCaption2(sheet, 1, 3, "Employee Code",20);
	   addCaption2(sheet, 2, 3, "Employee Name",50);
	   addCaption2(sheet, 3, 3, "Absent Days ",20);
		r=5;
	
	
	}
	
	public void createHeader8(WritableSheet sheet)  throws WriteException 
	{

		sheet.mergeCells(0, 0, 3, 0);
		   // Write a few headers
	   addCaption(sheet, 0, 0, cmp_name,40);
	   
	   sheet.mergeCells(0, 1, 3, 1);
	   addCaption1(sheet, 0, 1, " OTHER ALLOWANCE REPORT FOR THE MONTH OF "+monthname,40);
	   
	   addCaption2(sheet, 0, 3, "Sno",10);
	   addCaption2(sheet, 1, 3, "Employee Code",10);
	   addCaption2(sheet, 2, 3, "Employee Name",30);
	   addCaption2(sheet, 3, 3, "Other Allowance",20);
	   r=4;
	}  
	
	public void createHeader9(WritableSheet sheet)  throws WriteException {
		
		sheet.mergeCells(0, 0, 3, 0);
		   // Write a few headers
	   addCaption(sheet, 0, 0, cmp_name,40);
	   
	   sheet.mergeCells(0, 1, 3, 1);
	   addCaption1(sheet, 0, 1, " PRESENT REPORT FOR THE MONTH OF "+monthname,40);
	   
	   addCaption2(sheet, 0, 3, "Sno",10);
	   addCaption2(sheet, 1, 3, "Employee Code",20);
	   addCaption2(sheet, 2, 3, "Employee Name",50);
	   addCaption2(sheet, 3, 3, "Present Days ",20);
		r=5;
	
	
	}
	
	public void createHeader10(WritableSheet sheet)  throws WriteException {

		sheet.mergeCells(0, 0, 3, 0);
		// Write a few headers
		addCaption(sheet, 0, 0, cmp_name,40);

		sheet.mergeCells(0, 1, 3, 1);
		addCaption1(sheet, 0, 1, " Salary List (Bank Details) FOR THE MONTH OF "+monthname,40);

		addCaption2(sheet, 0, 3, "Sno",10);
		addCaption2(sheet, 1, 3, "Employee Code",20);
		addCaption2(sheet, 2, 3, "Employee Name",40);
		addCaption2(sheet, 3, 3, "A/c No",20);
		addCaption2(sheet, 4, 3, "Bank Name",30);
		addCaption2(sheet, 5, 3, "IFSC Code",20);
		addCaption2(sheet, 6, 3, "Net Salary ",20);
		r=5;


	}	
	
	public void createHeader11(WritableSheet sheet)  throws WriteException 
	{

		sheet.mergeCells(0, 0, 3, 0);
		   // Write a few headers
	   addCaption(sheet, 0, 0, cmp_name,40);
	   
	   sheet.mergeCells(0, 1, 3, 1);
	   addCaption1(sheet, 0, 1, " CANTEEN COUPON REPORT FOR THE MONTH OF "+monthname,40);
	   
	   addCaption2(sheet, 0, 3, "Sno",10);
	   addCaption2(sheet, 1, 3, "Employee Code",10);
	   addCaption2(sheet, 2, 3, "Employee Name",30);
	   addCaption2(sheet, 3, 3, "Coupon Amount",20);
	   r=4;
	}  


	public void createHeader12(WritableSheet sheet)  throws WriteException 
	{

		   
		sheet.mergeCells(0, 0, 4, 0);
		   // Write a few headers
	   addCaption(sheet, 0, 0, cmp_name,40);
	   
	   sheet.mergeCells(0, 1, 4, 1);
	   addCaption1(sheet, 0, 1, " LTA/MEDICAL REPORT FOR THE MONTH OF "+monthname,40);
	   
	   addCaption2(sheet, 0, 3, "Sno",10);
	   addCaption2(sheet, 1, 3, "Employee Code",10);
	   addCaption2(sheet, 2, 3, "Employee Name",30);
	   addCaption2(sheet, 3, 3, "LTA ",20);
	   addCaption2(sheet, 4, 3, "MEDICAL",20);
		r=5;
    

}  

	
	public void createHeader13(WritableSheet sheet)  throws WriteException 
	{

		   
		sheet.mergeCells(0, 0, 4, 0);
		   // Write a few headers
	   addCaption(sheet, 0, 0, cmp_name,40);
	   
	   sheet.mergeCells(0, 1, 4, 1);
	   addCaption1(sheet, 0, 1, " LTA/MEDICAL REGISTGER FOR THE YEAR "+fyear+"-"+(fyear+1),40);
	   
	   addCaption2(sheet, 0, 3, "Sno",10);
	   addCaption2(sheet, 1, 3, "Employee Code",10);
	   addCaption2(sheet, 2, 3, "Employee Name",30);
	   addCaption2(sheet, 3, 3, "Paid Month",30);
	   addCaption2(sheet, 4, 3, "LTA ",20);
	   addCaption2(sheet, 5, 3, "MEDICAL",20);
		r=5;
    
	}
	
	public void createHeader14(WritableSheet sheet)  throws WriteException 
	{

		   
		sheet.mergeCells(0, 0, 4, 0);
		   // Write a few headers
	   addCaption(sheet, 0, 0, cmp_name,40);
	   
	   sheet.mergeCells(0, 1, 4, 1);
	   addCaption1(sheet, 0, 1, "UNPAID LTA/MEDICAL LIST FOR THE YEAR "+fyear+"-"+(fyear+1),40);
	   
	   addCaption2(sheet, 0, 3, "Sno",10);
	   addCaption2(sheet, 1, 3, "Employee Code",10);
	   addCaption2(sheet, 2, 3, "Employee Name",30);
	   addCaption2(sheet, 3, 3, "Paid Month",30);
	   addCaption2(sheet, 4, 3, "LTA ",20);
	   addCaption2(sheet, 5, 3, "MEDICAL",20);
		r=5;
    
	}

	public void createHeader15(WritableSheet sheet)  throws WriteException {
		
		sheet.mergeCells(0, 0, 6, 0);
		   // Write a few headers
	   addCaption(sheet, 0, 0, cmp_name,40);
	   
	   sheet.mergeCells(0, 1, 6, 1);
	   addCaption1(sheet, 0, 1, " MACHINE OPERATOR REPORT FOR THE MONTH OF "+monthname,40);
	   
	   addCaption2(sheet, 0, 3, "Sno",10);
	   addCaption2(sheet, 1, 3, "Employee Code",10);
	   addCaption2(sheet, 2, 3, "Employee Name",30);
	   addCaption2(sheet, 3, 3, "Machine1 Days ",20);
	   addCaption2(sheet, 4, 3, "Machine2 Days ",20);
	   addCaption2(sheet, 5, 3, "Machine1 Rate",20);
	   addCaption2(sheet, 6, 3, "Machine2 Rate",20);
	   addCaption2(sheet, 7, 3, "Machine1 Amount",20);
	   addCaption2(sheet, 8, 3, "Machine2 Amount",20);
	   addCaption2(sheet, 9, 3, "Signature",20);
		r=5;
	
	
	}

	public void createHeader16(WritableSheet sheet)  throws WriteException {

		sheet.mergeCells(0, 0, 3, 0);
		// Write a few headers
		addCaption(sheet, 0, 0, cmp_name,40);

		sheet.mergeCells(0, 1, 3, 1);
		addCaption1(sheet, 0, 1, " Bonus List (Bank Details) FOR THE YEAR OF "+(fyear+1),40);

		addCaption2(sheet, 0, 3, "Sno",10);
		addCaption2(sheet, 1, 3, "Employee Code",20);
		addCaption2(sheet, 2, 3, "Employee Name",40);
		addCaption2(sheet, 3, 3, "A/c No",20);
		addCaption2(sheet, 4, 3, "Bank Name",30);
		addCaption2(sheet, 5, 3, "IFSC Code",20);
		addCaption2(sheet, 6, 3, "Bonus Paid ",20);
		r=5;


	}	

	public void createHeader17(WritableSheet sheet)  throws WriteException {

		sheet.mergeCells(0, 0, 15, 0);
		// Write a few headers
		addCaption(sheet, 0, 0, cmp_name,40);

		sheet.mergeCells(0, 1, 15, 1);
		addCaption1(sheet, 0, 1, " Employee Basic Details For The Month "+monthname,40);

		addCaption2(sheet, 0, 3, "Sno",10);
		addCaption2(sheet, 1, 3, "Employee Code",20);
		addCaption2(sheet, 2, 3, "Employee Name",40);
		addCaption2(sheet, 3, 3, "ESIC No",20);
		addCaption2(sheet, 4, 3, "PF No",20);
		addCaption2(sheet, 5, 3, "Aadhar No",20);
		addCaption2(sheet, 6, 3, "Basic ",15);
		addCaption2(sheet, 7, 3, "DA",15);
		addCaption2(sheet, 8, 3, "HRA",15);
		addCaption2(sheet, 9, 3, "Add HRA",15);
		addCaption2(sheet, 10, 3, "Incentive",15);
		addCaption2(sheet, 11, 3, "Sp. Incentive",15);
		addCaption2(sheet, 12, 3, "Food Allowance",15);
		addCaption2(sheet, 13, 3, "LTA",15);
		addCaption2(sheet, 14, 3, "Medical",15);
		addCaption2(sheet, 15, 3, "OT Rate",15);
		addCaption2(sheet, 16, 3, "Sterile Rate",15);
		addCaption2(sheet, 17, 3, "Total",20);
		r=5;


	}	


	public void createHeader18(WritableSheet sheet)  throws WriteException 
	{

		   
		sheet.mergeCells(0, 0, 4, 0);
		   // Write a few headers
	   addCaption(sheet, 0, 0, cmp_name,40);
	   
	   sheet.mergeCells(0, 1, 4, 1);
	   addCaption1(sheet, 0, 1, " Employee Increment Detail FOR THE YEAR "+fyear+"-"+(fyear+1),40);
	   
	   addCaption2(sheet, 0, 3, "Sno",10);
	   addCaption2(sheet, 1, 3, "Employee Code",10);
	   addCaption2(sheet, 2, 3, "Employee Name",30);
	   addCaption2(sheet, 3, 3, "Month",30);
	   addCaption2(sheet, 4, 3, "Gross ",20);
	   addCaption2(sheet, 5, 3, "Difference",20);
		r=5;
    
	}

	public void createHeader19(WritableSheet sheet)  throws WriteException 
	{

		   
		sheet.mergeCells(0, 0, 4, 0);
		   // Write a few headers
	   addCaption(sheet, 0, 0, cmp_name,40);
	   
	   sheet.mergeCells(0, 1, 4, 1);
	   addCaption1(sheet, 0, 1, " Salary Detail Register FOR THE YEAR "+fyear+"-"+(fyear+1),40);
	   
	   sheet.mergeCells(2, 3, 16, 3);
//	   addCaption1(sheet, 2, 3, "<--------------------------------------------------------------------------------------------------------------------------------------------------- Basic Data ------------------------------------------------------------------------------------------------------------------------------------- >", 45);
	   addCaptionNew1(sheet, 2, 3, "B A S I C   D A T A", 45,1);

	   
	   sheet.mergeCells(17, 3, 29, 3);
//	   addCaption1(sheet, 17, 3, "<-------------------------------------------------------------------------------- Basic Salary ------------------------------------------------------------------ >", 45);
	   addCaptionNew1(sheet, 17, 3, "B A S I C   S A L A R Y", 45,2);

	   sheet.mergeCells(30, 3, 65, 3);
//	   addCaption1(sheet, 30, 3, "<-------------------------------------------------------------------------------------------------------------------- Earned Salary --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- >", 45);
	   addCaptionNew1(sheet, 30, 3, "E A R N E D    S A L A R Y", 45,3);

	   sheet.mergeCells(69, 3, 89, 3);
//	   addCaption1(sheet, 69, 3, "<-------------------------------------------------------------------------------------------------------- Total Deduction -------------------------------------------------------------------------------------------- >", 45);
	   addCaptionNew1(sheet, 69, 3, "T O T A L    D E D U C T I O N ", 45,4);

	   
	   addCaption2(sheet, 0, 4, "Month",16);
	   addCaption2(sheet, 1, 4, "Year",10);
	   addCaptionNew1(sheet, 2, 4, "Employee Code",10,1);
	   addCaption2(sheet, 3, 4, "Employee Name",30);
	   addCaption2(sheet, 4, 4, "Designation",15);
	   addCaption2(sheet, 5, 4, "PF No.",10);
	   addCaption2(sheet, 6, 4, "Esic No.",15);
	   addCaption2(sheet, 7, 4, "Bank Name ",30);
	   addCaption2(sheet, 8, 4, "Bank A/c No.",15);
	   addCaption2(sheet, 9, 4, "Present Days",15);
	   addCaption2(sheet, 10, 4, "Arrear Days",15);
	   addCaption2(sheet, 11, 4, "Comm.Hrs",15);
	   addCaption2(sheet, 12, 4, "Sterile Days",15);
	   addCaption2(sheet, 13, 4, "Opt1 Days",15);
	   addCaption2(sheet, 14, 4, "Opt2 Days",15);
	   addCaption2(sheet, 15, 4, "Absent Days",15);
	   addCaptionNew1(sheet, 16, 4, "UAN No.",25,1);

	   addCaptionNew1(sheet, 17, 4, "Basic ",20,2);
	   addCaption2(sheet, 18, 4, "DA",20);
	   addCaption2(sheet, 19, 4, "HRA",20);
	   addCaption2(sheet, 20, 4, "INC",20);
	   addCaption2(sheet, 21, 4, "MEDICAL",20);
	   addCaption2(sheet, 22, 4, "Food Allow",20);
	   addCaption2(sheet, 23, 4, "LTA",20);
	   addCaption2(sheet, 24, 4, "Spl.Inc",20);
	   addCaption2(sheet, 25, 4, "Comm.Rate",20);
	   addCaption2(sheet, 26, 4, "Sterile Rate",20);
	   addCaption2(sheet, 27, 4, "Opt1 Allow",20);
	   addCaption2(sheet, 28, 4, "Opt2 Allow",20);
	   addCaptionNew1(sheet, 29, 4, "Total",20,2);
	   // earned salary 	      
	   addCaptionNew1(sheet, 30, 4, "Basic ",20,3);
	   addCaption2(sheet, 31, 4, "Basic Arear1",20);
	   addCaption2(sheet, 32, 4, "Basic Arear2",20);
	   addCaption2(sheet, 33, 4, "DA",20);
	   addCaption2(sheet, 34, 4, "DA Arear1",20);
	   addCaption2(sheet, 35, 4, "DA Arear2",20);
	   addCaption2(sheet, 36, 4, "HRA",20);
	   addCaption2(sheet, 37, 4, "HRA Arear1",20);
	   addCaption2(sheet, 38, 4, "HRA Arear2",20);
	   addCaption2(sheet, 39, 4, "INC",20);
	   addCaption2(sheet, 40, 4, "INC Arear1",20);
	   addCaption2(sheet, 41, 4, "INC Arear2",20);
	   addCaption2(sheet, 42, 4, "Medical",20);
	   addCaption2(sheet, 43, 4, "Medical Arear1",20);
	   addCaption2(sheet, 44, 4, "Medical Arear2",20);
	   addCaption2(sheet, 45, 4, "Food Allow.",20);
	   addCaption2(sheet, 46, 4, "Food Allow. Arear1",20);
	   addCaption2(sheet, 47, 4, "Food Allow. Arear2",20);
	   addCaption2(sheet, 48, 4, "LTA",20);
	   addCaption2(sheet, 49, 4, "LTA Arear1",20);
	   addCaption2(sheet, 50, 4, "LTA Arear2",20);
	   addCaption2(sheet, 51, 4, "Spl.Incen",20);
	   addCaption2(sheet, 52, 4, "Spl.Incen Arear1",20);
	   addCaption2(sheet, 53, 4, "Spl.Incen Arear2",20);
	   addCaption2(sheet, 54, 4, "Comm.Rate",20);
	   addCaption2(sheet, 55, 4, "Comm.Rate Arear1",20);
	   addCaption2(sheet, 56, 4, "Comm.Rate Arear2",20);
	   addCaption2(sheet, 57, 4, "Sterile Rate",20);
	   addCaption2(sheet, 58, 4, "Sterile Rate Arear1",20);
	   addCaption2(sheet, 59, 4, "Sterile Rate Arear2",20);
	   addCaption2(sheet, 60, 4, "Opt1 Allow",20);
	   addCaption2(sheet, 61, 4, "Opt1 Allow Arear1",20);
	   addCaption2(sheet, 62, 4, "Opt1 Allow Arear2",20);
	   addCaption2(sheet, 63, 4, "Opt2 Allow",20);
	   addCaption2(sheet, 64, 4, "Opt2 Allow Arear1",20);
	   addCaptionNew1(sheet, 65, 4, "Opt2 Allow Arear2",20,3);
	   // 
	   
	   addCaption2(sheet, 66, 4, "Total Salary",20);
	   addCaption2(sheet, 67, 4, "Arear Salary",20);
	   addCaption2(sheet, 68, 4, "Total Salary",20);
	   
	   // deduction
	   addCaptionNew1(sheet, 69, 4, "PF",20,4);
	   addCaption2(sheet, 70, 4, "PF Arear1",20);
	   addCaption2(sheet, 71, 4, "PF Arear2",20);
	   addCaption2(sheet, 72, 4, "Esic",20);
	   addCaption2(sheet, 73, 4, "Esic Arear1",20);
	   addCaption2(sheet, 74, 4, "Esic Arear2",20);
	   addCaption2(sheet, 75, 4, "P.Tax",20);
	   addCaption2(sheet, 76, 4, "P.Tax Arear1",20);
	   addCaption2(sheet, 77, 4, "P.Tax Arear2",20);
	   addCaption2(sheet, 78, 4, "Advance",20);
	   addCaption2(sheet, 79, 4, "Advance Arear1",20);
	   addCaption2(sheet, 80, 4, "Advance Arear2",20);
	   addCaption2(sheet, 81, 4, "Loan",20);
	   addCaption2(sheet, 82, 4, "Loan Arear1",20);
	   addCaption2(sheet, 83, 4, "Loan Arear2",20);
	   addCaption2(sheet, 84, 4, "Other Ded.",20);
	   addCaption2(sheet, 85, 4, "Other Ded. Arear1",20);
	   addCaption2(sheet, 86, 4, "Other Ded. Arear2",20);
	   addCaption2(sheet, 87, 4, "Deduction",20);
	   addCaption2(sheet, 88, 4, "Arears Deduction",20);
	   addCaptionNew1(sheet, 89, 4, "Total Deduction",20,4);
	   //
	   addCaption2(sheet, 90, 4, "Net Payable Salary",20);

	   settings.setHorizontalFreeze(5);
//	   settings.setVerticalFreeze(7);

		r=5;
    
	}

	public void createHeader19Old(WritableSheet sheet)  throws WriteException 
	{

		   
		sheet.mergeCells(0, 0, 4, 0);
		   // Write a few headers
	   addCaption(sheet, 0, 0, cmp_name,40);
	   
	   sheet.mergeCells(0, 1, 4, 1);
	   addCaption1(sheet, 0, 1, " Salary Detail Register FOR THE YEAR "+fyear+"-"+(fyear+1),40);
	   
	   sheet.mergeCells(16, 3, 21, 3);
	   addCaption1(sheet, 16, 3, "<-------------------------------------------------------------------------------- Acual Salary ------------------------------------------------------------------ >", 45);

	   sheet.mergeCells(22, 3, 35, 3);
	   addCaption1(sheet, 22, 3, "<-------------------------------------------------------------------------------------------------------------------- Salary Got --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- >", 45);

	   sheet.mergeCells(36, 3, 43, 3);
	   addCaption1(sheet, 36, 3, "<-------------------------------------------------------------------------------------------------------- Total Deduction -------------------------------------------------------------------------------------------- >", 45);
	   
	   addCaption2(sheet, 0, 4, "Month",10);
	   addCaption2(sheet, 1, 4, "Year",10);
	   addCaption2(sheet, 2, 4, "Employee Code",10);
	   addCaption2(sheet, 3, 4, "PF No.",10);
	   addCaption2(sheet, 4, 4, "UAN No.",25);
	   addCaption2(sheet, 5, 4, "Esic No.",10);
	   addCaption2(sheet, 6, 4, "Employee Name",30);
	   addCaption2(sheet, 7, 4, "Category/Grade",30);
	   addCaption2(sheet, 8, 4, "Present Days",20);
	   addCaption2(sheet, 9, 4, "PL",20);
	   addCaption2(sheet, 10, 4, "CL",20);
	   addCaption2(sheet, 11, 4, "Arrear Days",20);
	   addCaption2(sheet, 12, 4, "P.H.",20);
	   addCaption2(sheet, 13, 4, "OFF",20);
	   addCaption2(sheet, 14, 4, "ABSENT",20);
	   addCaption2(sheet, 15, 4, "OT Hours",20);
	   addCaption2(sheet, 16, 4, "Basic ",20);
	   addCaption2(sheet, 17, 4, "DA",20);
	   addCaption2(sheet, 18, 4, "HRA",20);
	   addCaption2(sheet, 19, 4, "A.HRA",20);
	   addCaption2(sheet, 20, 4, "INC",20);
	   addCaption2(sheet, 21, 4, "Total",20);
	   addCaption2(sheet, 22, 4, "Basic ",20);
	   addCaption2(sheet, 23, 4, "DA",20);
	   addCaption2(sheet, 24, 4, "HRA",20);
	   addCaption2(sheet, 25, 4, "A.HRA",20);
	   addCaption2(sheet, 26, 4, "INC",20);
	   addCaption2(sheet, 27, 4, "Spl.Inc",20);
	   addCaption2(sheet, 28, 4, "Food Allow",20);
	   
	   addCaption2(sheet, 29, 4, "OT Value/Comm off",20);
	   addCaption2(sheet, 30, 4, "Opt1 Allow",20);
	   addCaption2(sheet, 31, 4, "Opt2 Allow",20);
	   addCaption2(sheet, 32, 4, "Medical",20);
	   addCaption2(sheet, 33, 4, "LTA",20);
	   addCaption2(sheet, 34, 4, "Incl",20);

	   addCaption2(sheet, 35, 4, "Total Salary",20);
	   addCaption2(sheet, 36, 4, "PF Ded",20);
	   addCaption2(sheet, 37, 4, "ESIC Ded",20);
	   addCaption2(sheet, 38, 4, "P.TAX",20);
	   addCaption2(sheet, 39, 4, "Advance",20);
	   addCaption2(sheet, 40, 4, "Loan",20);
	   addCaption2(sheet, 41, 4, "TDS",20);
	   addCaption2(sheet, 42, 4, "Other Ded",20);

	   addCaption2(sheet, 43, 4, "Total Ded",20);
	   addCaption2(sheet, 44, 4, "Net Payable Salary",20);


		r=5;
    
	}

	public void createContent(WritableSheet sheet) throws WriteException,RowsExceededException
	{
	
		boolean first=true;
	
		// detail header
		int size=esicList.size();
	
		if (repno==10)
			size=size-1;
		EmptranDto emp =null;
		totadv=0.00;
		totloan=0.00;
		atten_days=0.00;
		sno=1;
		 
		int bkcode=0;
		int empcode=0;
		
		int heightInPoints = 18*20;
		 
		System.out.println("SIZE OF THE DATA IS "+size);
		for (i=0;i<size;i++)
		{
	
			emp = (EmptranDto) esicList.get(i);
			if(first)
			{
				bkcode=emp.getBank_code();
				empcode=emp.getEmp_code();
				createHeader(sheet);
				first=false;
			}
	
		    sheet.setRowView(r, heightInPoints);

			switch(repno)
			{
				case 1:
						createReport1(sheet, emp);
						break;
				case 2:
						createReport2(sheet, emp);
						break;
				case 3:
						createReport3(sheet, emp);
						break;
				case 4:
						createReport4(sheet, emp);
						break;
				case 5:
						createReport5(sheet, emp);
						break;
				case 6:
						createReport6(sheet, emp);
						break;
				case 7:
						createReport7(sheet, emp);
						break;
				case 8:
						createReport8(sheet, emp);
						break;
				case 9:
						createReport9(sheet, emp);
						break;						
				case 10:
						if(bkcode!=emp.getBank_code())
						{
							addLabel(sheet, 0, r, " ",3);
							addLabel(sheet, 1, r, " ",3);
							addLabel(sheet, 2, r, " ",3);
							addLabel(sheet, 3, r, " ",3);
							addLabel(sheet, 4, r, " ",3);
							addLabel(sheet, 5, r, "Total ",3);
							addDouble(sheet, 6, r, btotal,3);
							btotal=0.00;
							r++;

							bkcode=emp.getBank_code();
						}
						createReport10(sheet, emp);
						break;	
				case 11:
					createReport11(sheet, emp);
					break;
				case 12:
					createReport12(sheet, emp);
					break;
				case 13:
					createReport13(sheet, emp); // LTA/Medical (Paid)
					break;
				case 14:
					createReport13(sheet, emp); // Same 13 LTA/Medical (Unpaid)
					break;
				case 15:
					createReport15(sheet, emp); // Machine Operator Report
					break;
				case 16:
					if(bkcode!=emp.getBank_code())
					{
						addLabel(sheet, 0, r, " ",3);
						addLabel(sheet, 1, r, " ",3);
						addLabel(sheet, 2, r, " ",3);
						addLabel(sheet, 3, r, " ",3);
						addLabel(sheet, 4, r, " ",3);
						addLabel(sheet, 5, r, "Total ",3);
						addDouble(sheet, 6, r, btotal,3);
						btotal=0.00;
						r++;

						bkcode=emp.getBank_code();
					}
					createReport16(sheet, emp);
					break;	
				case 17:
					createReport17(sheet, emp); // Employee Basic Details
					break;
				case 18:
					if(empcode!=emp.getEmp_code())
					{
						r++;
						empcode=emp.getEmp_code();
					}
					createReport18(sheet, emp); // Employee Earning Details
					break;
					
				case 19:
					createReport19(sheet, emp); // salary Detail Register
					break;
					
			}
			
			
	
			/*if(pgbrk>55)
			{
				sheet.addRowPageBreak(r);
				pgbrk=0;
			}*/
	
		}
		
		
		  if(repno==1 && btnno==2)
		  {
			  addLabel(sheet, 0, r, "",3);
			  addLabel(sheet, 1, r, "",3);
			  addLabel(sheet, 2, r, "",3);
			  addDouble(sheet, 3, r, atten_days,3);
			  addLabel(sheet, 4, r, "",3);
			  addLabel(sheet, 5, r, "Total",3);
			  addDouble(sheet, 6, r, basic,3);
			  addDouble(sheet, 7, r, emppf,3);
			  addDouble(sheet, 8, r, emppf1,3);
			  System.out.println("emppf "+emppf+" emppf1 "+emppf1+" total "+emppf+emppf1);
			  addDouble(sheet, 9, r, (emppf+emppf1),3);
		  }
		  else if(repno==2 && btnno==22)
		  {
			  addLabel(sheet, 0, r, "",3);
			  addLabel(sheet, 1, r, "",3);
			  addLabel(sheet, 2, r, "",3);
			  addLabel(sheet, 3, r, "Total",3);
			  addDouble(sheet, 4, r, atten_days,3);
			  addDouble(sheet, 5, r, basic,3);
			  addDouble(sheet, 6, r, emppf,3);
			  addDouble(sheet, 7, r, emppf1,3);
			  addDouble(sheet, 8, r, (emppf+emppf1),3);
			  addDouble(sheet, 9, r, eps,3);
			  addDouble(sheet, 10, r, (emppf+emppf1+eps),3);
		  }
		  else if(repno==2 && btnno==2) //add total in PF list on 13/07/2024 by Yashpal
		  {
			  addLabel(sheet, 0, r, "",3);
			  addLabel(sheet, 1, r, "",3);
			  addLabel(sheet, 2, r, "",3);
			  addLabel(sheet, 3, r, "",3);
			  addLabel(sheet, 4, r, "",3);
			  addLabel(sheet, 5, r, "Total",3);
			  addDouble(sheet, 6, r, net,3);
			  addDouble(sheet, 7, r, basic,3);
			  addDouble(sheet, 8, r, epswages,3);
			  addDouble(sheet, 9, r, edlwages,3);
			  addDouble(sheet, 10, r, epfc,3);
			  addDouble(sheet, 11, r, epsc,3);
			  addDouble(sheet, 12, r, (epfc-epsc),3);
			  addDouble(sheet, 13, r, absent_days,3);
			  addLabel(sheet, 14, r, "",3);
			  if(opt==2)
				  addLabel(sheet, 15, r, "",3);

		  }
		  else if(repno==3 || repno==12)
		  {
			  addLabel(sheet, 0, r, "",3);
			  addLabel(sheet, 1, r, "",3);
			  addLabel(sheet, 2, r, "Total",3);
			  addDouble(sheet, 3, r, totadv,3);
			  addDouble(sheet, 4, r, totloan,3);
		  }
		  else if(repno==4)
		  {
			  addLabel(sheet, 0, r, "",3);
			  addLabel(sheet, 1, r, "",3);
			  addLabel(sheet, 2, r, "Total",3);
			  addDouble(sheet, 3, r, sterlite_days,3);
			  addLabel(sheet, 4, r, "",3);
			  addDouble(sheet, 5, r, totadv,3);
			  addLabel(sheet, 6, r, "",3);
		  }
		  else if(repno==6)
		  {
			  addLabel(sheet, 0, r, "",3);
			  addLabel(sheet, 1, r, "",3);
			  addLabel(sheet, 2, r, "Total",3);
			  addDouble(sheet, 3, r, extra_hrs,3);
			  addLabel(sheet, 4, r, "",3);
			  addDouble(sheet, 5, r, totadv,3);
			  addLabel(sheet, 6, r, "",3);
		  }
		  else if(repno==5)
		  {
			  addLabel(sheet, 0, r, "",3);
			  addLabel(sheet, 1, r, "Total",3);
			  addDouble(sheet, 2, r, atten_days,3);
			  addDouble(sheet, 3, r, extra_hrs,3);
			  addDouble(sheet, 4, r, arrear_days,3);
			  addDouble(sheet, 5, r, sterlite_days,3);
			  addDouble(sheet, 6, r, totadv,3);
			  addDouble(sheet, 7, r, totloan,3);
		  }
		  else if(repno==7 || repno==9)
		  {
			  addLabel(sheet, 0, r, "",3);
			  addLabel(sheet, 1, r, "",3);
			  addLabel(sheet, 2, r, "Total",3);
			  addDouble(sheet, 3, r, absent_days,3); // absent/present days total 
		  }
		  else if(repno==8 || repno==11)
		  {
			  addLabel(sheet, 0, r, "",3);
			  addLabel(sheet, 1, r, "",3);
			  addLabel(sheet, 2, r, "Total",3);
			  addDouble(sheet, 3, r, totadv,3);
		  }
		  else if(repno==10 || repno==16)
		  {
			  	addLabel(sheet, 0, r, " ",3);
				addLabel(sheet, 1, r, " ",3);
				addLabel(sheet, 2, r, " ",3);
				addLabel(sheet, 3, r, " ",3);
				addLabel(sheet, 4, r, " ",3);
				addLabel(sheet, 5, r, "Total ",3);
				addDouble(sheet, 6, r, btotal,3);
				r++;
			  	addLabel(sheet, 0, r, " ",3);
				addLabel(sheet, 1, r, " ",3);
				addLabel(sheet, 2, r, " ",3);
				addLabel(sheet, 3, r, " ",3);
				addLabel(sheet, 4, r, " ",3);
				addLabel(sheet, 5, r, "Grand Total ",3);
				addDouble(sheet, 6, r, bgtotal,3);
				r++;
		  }
		  else if(repno==13 )
		  {
			  addLabel(sheet, 0, r, "",3);
			  addLabel(sheet, 1, r, "",3);
			  addLabel(sheet, 2, r, "",3);
			  addLabel(sheet, 3, r, "Total",3);
			  addDouble(sheet, 4, r, totadv,3);
			  addDouble(sheet, 5, r, totloan,3);
		  }
		  else if(repno==15)
		  {
			  addLabel(sheet, 0, r, "",3);
			  addLabel(sheet, 1, r, "",3);
			  addLabel(sheet, 2, r, "Total",3);
			  addDouble(sheet, 3, r, sterlite_days,3);
			  addDouble(sheet, 4, r, arrear_days,3);
			  addLabel(sheet, 5, r, "",3);
			  addLabel(sheet, 6, r, "",3);
			  addDouble(sheet, 7, r, totadv,3);
			  addDouble(sheet, 8, r, totloan,3);
			  addLabel(sheet, 9, r, "",3);
		  }

		  
	}

	public void createReport1(WritableSheet sheet,EmptranDto emp) throws WriteException,RowsExceededException
	{
	
		
			int dash=0;
			if(btnno==1) // UPLOAD
			{
				
				if(emp.getDiffdays()==0 || (emp.getDiffdays()> -31 && emp.getDiffdays()< 0) && emp.getDash()==0)
				{
					if(opt==1)
					{
						addLabel(sheet, 0, r, String.valueOf(emp.getEmp_code()),dash);
						addLabel(sheet, 1, r, String.valueOf(emp.getEsic_no()),dash);
						addLabel(sheet, 2, r, emp.getEmp_name(),dash);
						addLabel(sheet, 3, r, String.valueOf((int) emp.getAtten_days()),dash);
						addLabel(sheet, 5, r, emp.getEmp_name(),dash);
						if(emp.getEsis_value()==0 && emp.getArear1_esic_value()>0 && mnth_code==202507)
							addLabel(sheet, 4, r, String.valueOf((int) (emp.getArrear1_earning()+emp.getArrear2_earning())),dash); // less arrear_basic_value on 24/06/2024
						else
							addLabel(sheet, 4, r, String.valueOf((int) (emp.getBasic_earning()+emp.getArrear2_earning())),dash); // less arrear_basic_value on 17/08/2024 BY YASHPAL
						if(emp.getCreated_date()!=null)
							addLabel(sheet, 5, r, "2",dash);
						else if(emp.getAtten_days()==0)
							addLabel(sheet, 5, r, "11",dash);
						else
							addLabel(sheet, 5, r, "",dash);
						if(emp.getCreated_date()!=null)
							addLabel(sheet, 6, r, sdf.format(emp.getCreated_date()),dash);
						else
							addLabel(sheet, 6, r, "",dash);

						r++;
					}
					else if(opt==2 && emp.getArrear_days()>0)
					{
						addLabel(sheet, 0, r, String.valueOf(emp.getEmp_code()),dash);
						addLabel(sheet, 1, r, String.valueOf(emp.getEsic_no()),dash);
						addLabel(sheet, 2, r, ""+emp.getEmp_name(),dash);
						addLabel(sheet, 3, r, String.valueOf((int) emp.getArrear_days()+emp.getPrev_days()),dash);
						addLabel(sheet, 4, r, String.valueOf((int) (emp.getArrear1_earning()+emp.getPrevious_arrear_basic())),dash);  // CHANGED ON 17/08/2024 BY YASHPAL

						//addLabel(sheet, 4, r, String.valueOf(emp.getCreated_by()),dash);
						if(emp.getCreated_date()!=null)
							addLabel(sheet, 5, r, "2",dash);
						else if(emp.getAtten_days()==0)
							addLabel(sheet, 5, r, "11",dash);
						else
							addLabel(sheet, 5, r, "",dash);
						if(emp.getCreated_date()!=null)
							addLabel(sheet, 6, r, sdf.format(emp.getCreated_date()),dash);
						else
							addLabel(sheet, 6, r, "",dash);

						r++;
					}

				}
			}
			else if(btnno==2 && emp.getDash()==0) // EXCEL
			{
	
				if(opt==1)
				{
						addNumber(sheet, 0, r, emp.getSerialno(),dash);
						addNumber(sheet, 1, r, emp.getEmp_code(),dash);
						addLong(sheet, 2, r, emp.getEsic_no(),dash);
						addNumber(sheet, 3, r, ((int) emp.getAtten_days()),dash);
						addDouble(sheet, 4, r, emp.getBasic_total(),dash);
						addLabel(sheet, 5, r, emp.getEmp_name(),dash);
						if(emp.getEsis_value()==0 && emp.getArear1_esic_value()>0 && mnth_code==202507)
						{
							System.out.println(emp.getEmp_code()+" iske andar hai kya");
							addDouble(sheet, 6, r, emp.getArrear1_earning()+emp.getArrear2_earning(),dash); // less arrear_basic_value on 24/06/2024
							addDouble(sheet, 7, r, emp.getArear1_esic_value()+emp.getArrear2_esis_value(),dash);
//							emppf+=emp.getArear1_esic_value()+emp.getArrear2_esis_value();
							double tot=(int) Math.ceil(((emp.getArrear1_earning()+emp.getArrear2_earning())*3.25)/100); 
							emp.setEmployer_esis_value(tot);
						}
						else
						{
							addDouble(sheet, 6, r, emp.getBasic_earning()+emp.getArrear2_earning(),dash); // less arrear_basic_value on 24/06/2024
							addDouble(sheet, 7, r, emp.getBasic_esis_value()+emp.getArrear2_esis_value(),dash);
							emppf+=emp.getBasic_esis_value()+emp.getArrear2_esis_value();
						}
						addDouble(sheet, 8, r, emp.getEmployer_esis_value()+emp.getArrear2_employer_esis_value(),dash);
						addDouble(sheet, 9, r, (emp.getBasic_esis_value()+emp.getEmployer_esis_value()+emp.getArrear2_esis_value()+emp.getArrear2_employer_esis_value()),dash);
						r++;
						atten_days+=(int) emp.getAtten_days();
						basic+=emp.getBasic_earning()+emp.getArrear2_earning();
						
						emppf1+=emp.getEmployer_esis_value()+emp.getArrear2_employer_esis_value();

				}
				else if(opt==2 && emp.getArrear_days()>0)
				{
					
					addNumber(sheet, 0, r, emp.getSerialno(),dash);
					addNumber(sheet, 1, r, emp.getEmp_code(),dash);
					addLong(sheet, 2, r, emp.getEsic_no(),dash);
					addNumber(sheet, 3, r, ((int) (emp.getArrear_days()+emp.getPrev_days())),dash);
					addDouble(sheet, 4, r, emp.getBasic_total(),dash);
					addLabel(sheet, 5, r, emp.getEmp_name(),dash);
					addDouble(sheet, 6, r, emp.getArrear1_earning(),dash);
					addDouble(sheet, 7, r, emp.getArrear1_esis_value(),dash);
					addDouble(sheet, 8, r, emp.getArrear1_employer_esis_value(),dash);
					addDouble(sheet, 9, r, (emp.getArrear1_esis_value()+emp.getArrear1_employer_esis_value()),dash);
					r++;
					atten_days+=(int) emp.getArrear_days()+emp.getPrev_days();
					basic+=emp.getArrear1_earning();
					emppf+=emp.getArrear1_esis_value();
					emppf1+=emp.getArrear1_employer_esis_value();
				}
				else if(opt==3)  // Consolidated new option (17/08/2024 by Yashpal)
				{
						  
						  addNumber(sheet, 0, r, emp.getSerialno(),dash);
						addNumber(sheet, 1, r, emp.getEmp_code(),dash);
						addLong(sheet, 2, r, emp.getEsic_no(),dash);
						addNumber(sheet, 3, r, ((int) emp.getAtten_days()),dash);
						addDouble(sheet, 4, r, emp.getBasic_total(),dash);
						addLabel(sheet, 5, r, emp.getEmp_name(),dash);
						addDouble(sheet, 6, r, (emp.getBasic_earning()+emp.getArrear1_earning()+emp.getArrear2_earning()),dash); // Consolidated value on 17/08/2024
						addDouble(sheet, 7, r, (emp.getBasic_esis_value()+emp.getArrear1_esis_value()+emp.getArrear2_esis_value()),dash);

						addDouble(sheet, 8, r, (emp.getEmployer_esis_value()+emp.getArrear1_employer_esis_value()+emp.getArrear2_employer_esis_value()),dash);
						addDouble(sheet, 9, r, (emp.getBasic_esis_value()+emp.getArrear1_esis_value()+emp.getArrear2_esis_value()+emp.getEmployer_esis_value()+emp.getArrear1_employer_esis_value()+emp.getArrear2_employer_esis_value()),dash);
						r++;
						atten_days+=(int) emp.getAtten_days();
						basic+=(emp.getBasic_earning()+emp.getArrear1_earning()+emp.getArrear2_earning());
						emppf+=(emp.getBasic_esis_value()+emp.getArrear1_esis_value()+emp.getArrear2_esis_value());
						emppf1+=(emp.getEmployer_esis_value()+emp.getArrear1_employer_esis_value()+emp.getArrear2_employer_esis_value());

				}

			}
	
			
			 
	
		 
	}

	
	public void createReport2(WritableSheet sheet,EmptranDto emp) throws WriteException,RowsExceededException
	{
	 
		
			int dash=0;
			double epfcontri=0.00;
			double epscontri=0.00;
			double basicValue=0.00;
			double netValue=0.00;
			
			double epfcontriArrear=0.00;
			double epscontriArrear=0.00;
			double basicValueArrear=0.00;
			double netValueArrear=0.00;
			boolean check=true;
		
			
			if(emp.getAtten_days()>0 && emp.getDash()==0) // UPLOAD BUTTON
			{
						if(opt==1)
						{
							
							
							if(btnno==1)
							{
//								addLong(sheet, 0, r, emp.getUan_no(),dash);
								addLabel(sheet, 0, r, String.valueOf(emp.getUan_no()),dash);
								addLabel(sheet, 1, r, String.valueOf(emp.getAdhar_no()),dash);
								addLabel(sheet, 2, r, emp.getPan_no(),dash);
								addLabel(sheet, 3, r, emp.getEmp_name(),dash);
								addNumber(sheet, 4, r, (int) emp.getPf_gross_wages(),dash);
								addNumber(sheet, 5, r, (int) emp.getEpf_wages(),dash);
								    addNumber(sheet, 6, r, (int) emp.getEps_wages(),dash);
								addNumber(sheet, 7, r, (int) emp.getEdl_wages(),dash);
								addNumber(sheet, 8, r, (int) emp.getEpf_share(),dash);
								  addNumber(sheet, 9, r, (int) emp.getEps_share(),dash);
								addNumber(sheet, 10, r, (int) emp.getEpf_er_share(),dash);
								addNumber(sheet, 11, r, (int) emp.getAbsent_days(),dash);
								addLabel(sheet, 12, r, String.valueOf(0),dash);
							}
							else if(btnno==2)
							{
								addNumber(sheet, 0, r, emp.getEmp_code(),dash);
								addLabel(sheet, 1, r, String.valueOf(emp.getPf_no()),dash);
								addLabel(sheet, 2, r, String.valueOf(emp.getUan_no()),dash);
								addLabel(sheet, 3, r, String.valueOf(emp.getAdhar_no()),dash);
								addLabel(sheet, 4, r, emp.getPan_no(),dash);
								addLabel(sheet, 5, r, emp.getEmp_name(),dash);
								addNumber(sheet, 6, r, (int) emp.getPf_gross_wages(),dash);
								addNumber(sheet, 7, r, (int) emp.getEpf_wages(),dash);
								addNumber(sheet, 8, r, (int) emp.getEps_wages(),dash);
								addNumber(sheet, 9, r, (int) emp.getEdl_wages(),dash);
								addNumber(sheet, 10, r, (int) emp.getEpf_share(),dash);
								addNumber(sheet, 11, r, (int) emp.getEps_share(),dash);
								addNumber(sheet, 12, r, (int) emp.getEpf_er_share(),dash);
								addNumber(sheet, 13, r, (int) emp.getAbsent_days(),dash);
								addLabel(sheet, 14, r, String.valueOf(0),dash);
								
								net+=emp.getPf_gross_wages();
								basic+=emp.getEpf_wages();
								epfc+=emp.getEpf_share();
								epsc+=emp.getEps_share();
								absent_days+=emp.getAbsent_days();
								epswages+=emp.getEps_wages();
								edlwages+=emp.getEdl_wages();

								
								
								
							}
							r++;
						}
						
						else if(opt==2 && emp.getArrear_days()>0) // arrear button isSelected
						{
		

		
							if(btnno==1) // upload button
							{
								addLabel(sheet, 0, r, String.valueOf(emp.getUan_no()),dash);
								addLabel(sheet, 1, r, String.valueOf(emp.getAdhar_no()),dash);
								addLabel(sheet, 2, r, emp.getPan_no(),dash);
								addLabel(sheet, 3, r, emp.getEmp_name(),dash);
								addNumber(sheet, 4, r, (int) emp.getArrear_gross_wages(),dash);
								addNumber(sheet, 5, r, (int) emp.getArrear_epf_wages(),dash);
								addNumber(sheet, 6, r, (int) emp.getArrear_eps_wages(),dash);
								addNumber(sheet, 7, r, (int) emp.getArrear_edl_wages(),dash);
								addNumber(sheet, 8, r, (int) emp.getArrear_epf_share(),dash);
								addNumber(sheet, 9, r, (int) emp.getArrear_eps_share(),dash);
								addNumber(sheet, 10, r, (int) emp.getArrear_epf_er_share(),dash);
								addNumber(sheet, 11, r, (int) emp.getNcp_days(),dash);
								addNumber(sheet, 12, r, 0,dash); //fixed 0 on 09/03/2022 by YashPal
							}
							else if(btnno==2) //excel button
							{
								addNumber(sheet, 0, r, emp.getEmp_code(),dash);
								addLabel(sheet, 1, r, String.valueOf(emp.getPf_no()),dash);
								addLabel(sheet, 2, r, String.valueOf(emp.getUan_no()),dash);
								addLabel(sheet, 3, r, String.valueOf(emp.getAdhar_no()),dash);
								addLabel(sheet, 4, r, emp.getPan_no(),dash);
								addLabel(sheet, 5, r, emp.getEmp_name(),dash);
								addNumber(sheet, 6, r, (int) emp.getArrear_gross_wages(),dash);
								addNumber(sheet, 7, r, (int) emp.getArrear_epf_wages(),dash);
								addNumber(sheet, 8, r, (int) emp.getArrear_eps_wages(),dash);
								addNumber(sheet, 9, r, (int) emp.getArrear_edl_wages(),dash);
								addNumber(sheet, 10, r, (int) emp.getArrear_epf_share(),dash);
								addNumber(sheet, 11, r, (int) emp.getArrear_eps_share(),dash);
								addNumber(sheet, 12, r, (int) emp.getArrear_epf_er_share(),dash);
								addNumber(sheet, 13, r, (int) emp.getNcp_days(),dash);
								addNumber(sheet, 14, r, (int) emp.getArrear_days(),dash);
								addNumber(sheet, 15, r, (int) emp.getPrev_days(),dash);
								
								net+=emp.getArrear_gross_wages();
								basic+=emp.getArrear_epf_wages();
								epfc+=emp.getArrear_epf_share();
								epsc+=emp.getArrear_eps_share();
								absent_days+=emp.getNcp_days();
								epswages+=emp.getArrear_eps_wages();
								edlwages+=emp.getArrear_edl_wages();

							}
						 	r++;
		
						}

//=============================================================================
// Consolidated option value is 3 						
//=============================================================================
						else if(opt==3 ) // Consolidated button isSelected
						{

							if(btnno==2) //excel button
							{
								addNumber(sheet, 0, r, emp.getEmp_code(),dash);
								addLabel(sheet, 1, r, String.valueOf(emp.getPf_no()),dash);
								addLabel(sheet, 2, r, String.valueOf(emp.getUan_no()),dash);
								addLabel(sheet, 3, r, String.valueOf(emp.getAdhar_no()),dash);
								addLabel(sheet, 4, r, emp.getPan_no(),dash);
								addLabel(sheet, 5, r, emp.getEmp_name(),dash);
								addNumber(sheet, 6, r, (int) (emp.getPf_gross_wages()+emp.getArrear_gross_wages()),dash);
								addNumber(sheet, 7, r, (int) (emp.getEpf_wages()+emp.getArrear_epf_wages()),dash);
								if((emp.getEpf_wages()+emp.getArrear_epf_wages())>150000)
								{
									addNumber(sheet, 8, r, (int) (emp.getEps_wages()),dash);
									addNumber(sheet, 9, r, (int) (emp.getEdl_wages()),dash);
									addNumber(sheet, 11, r, (int) (emp.getEps_share()),dash);
									addNumber(sheet, 12, r, (int) (emp.getEpf_share()+emp.getArrear_epf_share()-emp.getEps_share()),dash);
									
									epsc+=emp.getEps_share();
									epswages+=emp.getEps_wages();
									edlwages+=emp.getEdl_wages();
								}
								else
								{
									addNumber(sheet, 8, r, (int) (emp.getEps_wages()+emp.getArrear_eps_wages()),dash);
									addNumber(sheet, 9, r, (int) (emp.getEdl_wages()+emp.getArrear_edl_wages()),dash);
									addNumber(sheet, 11, r, (int) (emp.getEps_share()+emp.getArrear_eps_share()),dash);
									addNumber(sheet, 12, r, (int) (emp.getEpf_er_share()+emp.getArrear_epf_er_share()),dash);
								
									epsc+=emp.getEps_share()+emp.getArrear_eps_share();
									epswages+=emp.getEps_wages()+emp.getArrear_eps_wages();
									edlwages+=emp.getEdl_wages()+emp.getArrear_edl_wages();

								}
								addNumber(sheet, 10, r, (int) (emp.getEpf_share()+emp.getArrear_epf_share()),dash);
//								addNumber(sheet, 10, r, (int) (emp.getPf_value()+emp.getArear1_pf_value()+emp.getArear2_pf_value()),dash);
								addNumber(sheet, 13, r, (int) emp.getAbsent_days(),dash);
								addLabel(sheet, 14, r, String.valueOf(0),dash);

								addNumber(sheet, 15, r, (int) emp.getNcp_days(),dash);
								addNumber(sheet, 16, r, (int) emp.getArrear_days(),dash);
								addNumber(sheet, 17, r, (int) emp.getPrev_days(),dash);
								
								net+=emp.getPf_gross_wages()+emp.getArrear_gross_wages();
								basic+=emp.getEpf_wages()+emp.getArrear_epf_wages();
								epfc+=emp.getEpf_share()+emp.getArrear_epf_share();
								absent_days+=emp.getAbsent_days();
								ncp_days+=emp.getNcp_days();

							}
						 	r++;
		
						}
						
		
						
//=============================================================================						
						
						 

			}	
						
		 
	}

	
	
	/**
	 * Advance & Loan Report
	 * @param sheet
	 * @param emp
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	public void createReport3(WritableSheet sheet,EmptranDto emp) throws WriteException,RowsExceededException
	{
	
		
			int dash=0;
			if(emp.getAdvance()+emp.getLoan()>0)
			{
//				addNumber(sheet, 0, r, emp.getSerialno(),dash);
				addNumber(sheet, 0, r, sno,dash);
				addNumber(sheet, 1, r, emp.getEmp_code(),dash);
				addLabel(sheet, 2, r, emp.getEmp_name(),dash);
				addDouble(sheet, 3, r, emp.getAdvance(),dash);
				addDouble(sheet, 4, r, emp.getLoan(),dash);
				totadv+=emp.getAdvance();
				totloan+=emp.getLoan();
				r++;
				sno++;
			}
			 
		 
	}
	
	/**
	 * Sterlite Days Report
	 * @param sheet
	 * @param emp
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	public void createReport4(WritableSheet sheet,EmptranDto emp) throws WriteException,RowsExceededException
	{
	
		
			int dash=0;
			if(emp.getStair_days()>0)
			{
//				addNumber(sheet, 0, r, emp.getSerialno(),dash);
				addNumber(sheet, 0, r, sno,dash);
				addNumber(sheet, 1, r, emp.getEmp_code(),dash);
				addLabel(sheet, 2, r, emp.getEmp_name(),dash);
				addNumber(sheet, 3, r, emp.getStair_days(),dash);
				addDouble(sheet, 4, r, emp.getStair_alw(),dash);
				addDouble(sheet, 5, r, emp.getStair_value(),dash);
				addLabel(sheet, 6, r, "",dash);
				sterlite_days+=emp.getStair_days();
				totadv+=emp.getStair_value();
				r++;
				sno++;
			}
	}
	
	/**
	 * Attendance Check List 
	 * @param sheet
	 * @param emp
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	
	public void createReport5(WritableSheet sheet,EmptranDto emp) throws WriteException,RowsExceededException
	{
	
		
			int dash=0;

			addNumber(sheet, 0, r, emp.getEmp_code(),dash);
			addLabel(sheet, 1, r, emp.getEmp_name(),dash);
			addDouble(sheet, 2, r, emp.getAtten_days(),dash);
			addDouble(sheet, 3, r, emp.getExtra_hrs(),dash);
			addDouble(sheet, 4, r, emp.getArrear_days(),dash);
			addNumber(sheet, 5, r, emp.getStair_days(),dash);
			addDouble(sheet, 6, r, emp.getAdvance(),dash);
			addDouble(sheet, 7, r, emp.getLoan(),dash);
			r++;
			atten_days+=emp.getAtten_days();
			extra_hrs+=emp.getExtra_hrs();
			arrear_days+=emp.getArrear_days();
			sterlite_days+=emp.getStair_days();
			totadv+=emp.getAdvance();
			totloan+=emp.getLoan();
	}
	
	
	/**
	 * OT HRs Report
	 * @param sheet
	 * @param emp
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	public void createReport6(WritableSheet sheet,EmptranDto emp) throws WriteException,RowsExceededException
	{
	
		
			int dash=0;
			if(emp.getExtra_hrs()>0)
			{
				addNumber(sheet, 0, r, sno,dash);
				addNumber(sheet, 1, r, emp.getEmp_code(),dash);
				addLabel(sheet, 2, r, emp.getEmp_name(),dash);
				addDouble(sheet, 3, r, emp.getExtra_hrs(),dash);
				addDouble(sheet, 4, r, emp.getOt_rate(),dash);
				addDouble(sheet, 5, r, emp.getOt_value(),dash);
				addLabel(sheet, 6, r, "",dash);
				extra_hrs+=emp.getExtra_hrs();
				totadv+=emp.getOt_value();
				r++;
				sno++;
			}
	}
	
	/**
	 * Absent Report
	 * @param sheet
	 * @param emp
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	public void createReport7(WritableSheet sheet,EmptranDto emp) throws WriteException,RowsExceededException
	{
	
		
			int dash=0;
			addNumber(sheet, 0, r, emp.getSerialno(),dash);
			addNumber(sheet, 1, r, emp.getEmp_code(),dash);
			addLabel(sheet, 2, r, emp.getEmp_name(),dash);
			addDouble(sheet, 3, r, emp.getAbsent_days(),dash);
			absent_days+=emp.getAbsent_days();
			r++;
	}

	
	/**
	 * Misc  Report
	 * @param sheet
	 * @param emp
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	public void createReport8(WritableSheet sheet,EmptranDto emp) throws WriteException,RowsExceededException
	{
	
		
			int dash=0;
			if(emp.getMisc_value()>0)
			{
//				addNumber(sheet, 0, r, emp.getSerialno(),dash);
				addNumber(sheet, 0, r, sno,dash);
				addNumber(sheet, 1, r, emp.getEmp_code(),dash);
				addLabel(sheet, 2, r, emp.getEmp_name(),dash);
				addDouble(sheet, 3, r, emp.getMisc_value(),dash);
				totadv+=emp.getMisc_value();
				r++;
				sno++;
			}
			 
		 
	}

	
	/**
	 * Present Report
	 * @param sheet
	 * @param emp
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	public void createReport9(WritableSheet sheet,EmptranDto emp) throws WriteException,RowsExceededException
	{
	
		
			int dash=0;
			addNumber(sheet, 0, r, emp.getSerialno(),dash);
			addNumber(sheet, 1, r, emp.getEmp_code(),dash);
			addLabel(sheet, 2, r, emp.getEmp_name(),dash);
			addDouble(sheet, 3, r, emp.getAtten_days(),dash);
			absent_days+=emp.getAtten_days(); // present days 
			r++;
	}
	
	
	/**
	 * Salary List with Bank Details
	 * @param sheet
	 * @param emp
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	public void createReport10(WritableSheet sheet,EmptranDto emp) throws WriteException,RowsExceededException
	{
	
		
			int dash=0;
/*			double totearn=emp.getBasic_value()+emp.getDa_value()+emp.getHra_value()+emp.getAdd_hra_value()+emp.getIncentive_value()+emp.getSpl_incen_value()+emp.getOt_value()+emp.getLta_value()+emp.getMedical_value()+emp.getMisc_value()+emp.getStair_value()+emp.getMachine1_value()+emp.getMachine2_value()+emp.getFood_value();
			double totded=emp.getPf_value()+emp.getEsis_value()+emp.getAdvance()+emp.getCoupon_amt()+emp.getProf_tax()+emp.getLoan();;
			double net = totearn-totded;
*/
			net=emp.getNet_value();
			if(net>0)
			{
				addNumber(sheet, 0, r, emp.getSerialno(),dash);
				addNumber(sheet, 1, r, emp.getEmp_code(),dash);
				addLabel(sheet, 2, r, emp.getEmp_name(),dash);
				addLabel(sheet, 3, r, emp.getBank_accno(),dash);
				addLabel(sheet, 4, r, emp.getBank(),dash);
				addLabel(sheet, 5, r, emp.getIfsc_code(),dash);
				addDouble(sheet, 6, r, net,dash);
				btotal+=net;
				bgtotal+=net;
				r++;
			}
	}
	
	/**
	 * Coupon  Report
	 * @param sheet
	 * @param emp
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	public void createReport11(WritableSheet sheet,EmptranDto emp) throws WriteException,RowsExceededException
	{
	
		
			int dash=0;
			if(emp.getCoupon_amt()>0)
			{
//				addNumber(sheet, 0, r, emp.getSerialno(),dash);
				addNumber(sheet, 0, r, sno,dash);
				addNumber(sheet, 1, r, emp.getEmp_code(),dash);
				addLabel(sheet, 2, r, emp.getEmp_name(),dash);
				addDouble(sheet, 3, r, emp.getCoupon_amt(),dash);
				totadv+=emp.getCoupon_amt();
				r++;
				sno++;
			}
			 
		 
	}

	/**
	 * LTA & MEDICAL Report
	 * @param sheet
	 * @param emp
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	public void createReport12(WritableSheet sheet,EmptranDto emp) throws WriteException,RowsExceededException
	{
	
		
			int dash=0;
			if(emp.getLta_value()+emp.getMedical_value()>0)
			{
//				addNumber(sheet, 0, r, emp.getSerialno(),dash);
				addNumber(sheet, 0, r, sno,dash);
				addNumber(sheet, 1, r, emp.getEmp_code(),dash);
				addLabel(sheet, 2, r, emp.getEmp_name(),dash);
				addDouble(sheet, 3, r, emp.getLta_value(),dash);
				addDouble(sheet, 4, r, emp.getMedical_value(),dash);
				totadv+=emp.getLta_value();
				totloan+=emp.getMedical_value();
				r++;
				sno++;
			}
			 
		 
	}
	

	/**
	 * LTA & MEDICAL Register / UnPaid LTA/Medical List
	 * @param sheet
	 * @param emp
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	public void createReport13(WritableSheet sheet,EmptranDto emp) throws WriteException,RowsExceededException
	{
	
		
			int dash=0;
			//if(emp.getLta_value()+emp.getMedical_value()>0)
			{
//				addNumber(sheet, 0, r, emp.getSerialno(),dash);
				addNumber(sheet, 0, r, sno,dash);
				addNumber(sheet, 1, r, emp.getEmp_code(),dash);
				addLabel(sheet, 2, r, emp.getEmp_name(),dash);
				addLabel(sheet, 3, r, emp.getMonname(),dash);
				addDouble(sheet, 4, r, emp.getLta_value(),dash);
				addDouble(sheet, 5, r, emp.getMedical_value(),dash);
				totadv+=emp.getLta_value();
				totloan+=emp.getMedical_value();
				r++;
				sno++;
			}
			 
		 
	}

	
	/**
	 * Machine Operator Report
	 * @param sheet
	 * @param emp
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	public void createReport15(WritableSheet sheet,EmptranDto emp) throws WriteException,RowsExceededException
	{
	
		
			int dash=0;
			if(emp.getMachine1_days()+emp.getMachine2_days()>0)
			{
//				addNumber(sheet, 0, r, emp.getSerialno(),dash);
				addNumber(sheet, 0, r, sno,dash);
				addNumber(sheet, 1, r, emp.getEmp_code(),dash);
				addLabel(sheet, 2, r, emp.getEmp_name(),dash);
				addDouble(sheet, 3, r, emp.getMachine1_days(),dash);
				addDouble(sheet, 4, r, emp.getMachine2_days(),dash);
				addDouble(sheet, 5, r, emp.getMachine1_rate(),dash);
				addDouble(sheet, 6, r, emp.getMachine2_rate(),dash);
				addDouble(sheet, 7, r, emp.getMachine1_value(),dash);
				addDouble(sheet, 8, r, emp.getMachine2_value(),dash);
				addLabel(sheet, 9, r, "",dash);
				sterlite_days+=emp.getMachine1_days();
				arrear_days+=emp.getMachine2_days();
				totadv+=emp.getMachine1_value();
				totloan+=emp.getMachine2_value();
				r++;
				sno++;
			}
	}

	/**
	 * Bonus List with Bank Details
	 * @param sheet
	 * @param emp
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	public void createReport16(WritableSheet sheet,EmptranDto emp) throws WriteException,RowsExceededException
	{
	
		
			int dash=0;
			
			tot=0.00;
			print=false;
			for (int i=0;i<12;i++)
			{
				tot+=emp.getAtten()[i];
			}

			if(tot>=30)
				print=true;

			if(print)
			{
				addNumber(sheet, 0, r, emp.getSerialno(),dash);
				addNumber(sheet, 1, r, emp.getEmp_code(),dash);
				addLabel(sheet, 2, r, emp.getEmp_name(),dash);
				addLabel(sheet, 3, r, emp.getBank_accno(),dash);
				addLabel(sheet, 4, r, emp.getBank(),dash);
				addLabel(sheet, 5, r, emp.getIfsc_code(),dash);
				addDouble(sheet, 6, r, emp.getBonus_value(),dash);
				btotal+=emp.getBonus_value();
				bgtotal+=emp.getBonus_value();
				r++;
			}
	}
	

	/**
	 * Employee List with Basic Details
	 * @param sheet
	 * @param emp
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	public void createReport17(WritableSheet sheet,EmptranDto emp) throws WriteException,RowsExceededException
	{
	
		
			int dash=0;
			
			tot=0.00;
			tot=emp.getBasic()+emp.getDa()+emp.getHra()+emp.getAdd_hra()+emp.getIncentive()+emp.getSpl_incen_value()+emp.getFood_value();

			addNumber(sheet, 0, r, emp.getSerialno(),dash);
			addNumber(sheet, 1, r, emp.getEmp_code(),dash);
			addLabel(sheet, 2, r, emp.getEmp_name(),dash);
			addLong(sheet, 3, r, emp.getEsic_no(),dash);
			addNumber(sheet,4, r, emp.getPf_no(),dash);
			addLong(sheet, 5, r, emp.getAdhar_no(),dash);
			addDouble(sheet, 6, r, emp.getBasic(),dash);
			addDouble(sheet, 7, r, emp.getDa(),dash);
			addDouble(sheet, 8, r, emp.getHra(),dash);
			addDouble(sheet, 9, r, emp.getAdd_hra(),dash);
			addDouble(sheet, 10, r, emp.getIncentive(),dash);
			addDouble(sheet, 11, r, emp.getSpl_incentive(),dash);
			addDouble(sheet, 12, r, emp.getFood_value(),dash);
			addDouble(sheet, 13, r, emp.getLta(),dash);
			addDouble(sheet, 14, r, emp.getMedical(),dash);
			addDouble(sheet, 15, r, emp.getOt_rate(),dash);
			addDouble(sheet, 16, r, emp.getStair_alw(),dash);
			addDouble(sheet, 17, r, tot,dash);
			r++;
	}
	

	/**
	 * Employee Earning List
	 * @param sheet
	 * @param emp
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	public void createReport18(WritableSheet sheet,EmptranDto emp) throws WriteException,RowsExceededException
	{
	
		
			int dash=0;
			{
				addNumber(sheet, 0, r, sno,dash);
				addNumber(sheet, 1, r, emp.getEmp_code(),dash);
				addLabel(sheet, 2, r, emp.getEmp_name(),dash);
				addLabel(sheet, 3, r, emp.getMonname(),dash);
				addDouble(sheet, 4, r, emp.getGross(),dash);
				addDouble(sheet, 5, r, emp.getMedical_value(),dash);
				r++;
				sno++;
			}
			 
		 
	}

	/**
	 * Salary Detail Register
	 * @param sheet
	 * @param emp
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	public void createReport19(WritableSheet sheet,EmptranDto emp) throws WriteException,RowsExceededException
	{

			
			
			int dash=0;
			if(emp.getEmp_name().contains("Grand"))
				dash=3;
			{
			double totearn=emp.getBasic_value()+emp.getDa_value()+emp.getHra_value()+emp.getAdd_hra_value()+emp.getIncentive_value()+emp.getSpl_incen_value()+emp.getOt_value()+emp.getLta_value()+emp.getMedical_value()+emp.getMisc_value()+emp.getStair_value()+emp.getMachine1_value()+emp.getMachine2_value()+emp.getFood_value();
			double totded=emp.getPf_value()+emp.getEsis_value()+emp.getAdvance()+emp.getCoupon_amt()+emp.getProf_tax()+emp.getLoan();
			double net = totearn-totded;
			if(opt==1 || monthname.startsWith("April"))
				addLabel(sheet, 0, r, emp.getMonname(),dash);
			else
				addLabel(sheet, 0, r, "Upto "+monthname.substring(0, size),dash);
				addNumber(sheet, 1, r, emp.getFin_year(),dash);

				addNumber(sheet, 2, r, emp.getEmp_code(),dash);
				addLabel(sheet, 3, r, emp.getEmp_name(),dash);
				addLabel(sheet, 4, r, emp.getDesignation(),dash);
				addNumber(sheet, 5, r, emp.getPf_no(),dash);
				addLong(sheet, 6, r, emp.getEsic_no(),dash);
				addLabel(sheet, 7, r, emp.getBank(),dash);
				addLabel(sheet, 8, r, emp.getBank_accno(),dash);
				addDouble(sheet,9, r, emp.getAtten_days(),dash);
				addLabel(sheet, 10, r, String.valueOf(emp.getArrear_days()),dash);
				addLabel(sheet, 11, r, String.valueOf(emp.getExtra_hrs()),dash);
				addLabel(sheet, 12, r, String.valueOf(emp.getStair_days()),dash);
				addLabel(sheet, 13, r, String.valueOf(emp.getMachine1_days()),dash);
				addLabel(sheet, 14, r, String.valueOf(emp.getMachine2_days()),dash);
				addLabel(sheet, 15, r, String.valueOf(emp.getAbsent_days()),dash);
				addLabel(sheet, 16, r, String.valueOf(emp.getUan_no()),dash);


				 addDouble(sheet, 17, r, emp.getBasic(),dash);
				 addDouble(sheet, 18, r, emp.getDa(),dash);
				 addDouble(sheet, 19, r, emp.getHra(),dash);
				 addDouble(sheet, 20, r, emp.getIncentive(),dash);
				 addDouble(sheet, 21, r, emp.getMedical(),dash);
				 addDouble(sheet, 22, r, emp.getFood_alw(),dash);
				 addDouble(sheet, 23, r, emp.getLta(),dash);
				 addDouble(sheet, 24, r, emp.getSpl_incentive(),dash);
				 addDouble(sheet, 25, r, emp.getOt_rate(),dash);
				 addDouble(sheet, 26, r, emp.getStair_alw(),dash);
				 addDouble(sheet, 27, r, emp.getMachine1_rate(),dash);
				 addDouble(sheet, 28, r, emp.getMachine2_rate(),dash);
				 tot4=0;
				   tot4+=(int) ((emp.getBasic()+emp.getDa()+emp.getHra()+emp.getIncentive()+emp.getMedical())+0.50);

				   if(dash==0)
					   ntot4+=tot4;
				   
				   if(emp.getEmp_code()==24)
					   System.out.println("total value is "+tot4);

/*				   if(dash==3)
					   tot4=ntot4;
*/				   addDouble(sheet, 29, r,tot4,dash);


				   // earned salary 
				   addDouble(sheet, 30, r, emp.getBasic_value(),dash);
				   addDouble(sheet, 31, r, emp.getArear_basic_value(),dash);
				   addDouble(sheet, 32, r, emp.getArear2_basic_value(),dash);
				   addDouble(sheet, 33, r, emp.getDa_value(),dash);
				   addDouble(sheet, 34, r, emp.getArear_da_value(),dash);
				   addDouble(sheet, 35, r, emp.getArear2_da_value(),dash);
				   addDouble(sheet, 36, r, emp.getHra_value(),dash);
				   addDouble(sheet, 37, r, emp.getArear_hra_value(),dash);
				   addDouble(sheet, 38, r, emp.getArear2_hra_value(),dash);
				   addDouble(sheet, 39, r, emp.getIncentive_value(),dash);
				   addDouble(sheet, 40, r, emp.getArear_incentive_value(),dash);
				   addDouble(sheet, 41, r, emp.getArear2_incentive_value(),dash);
				   addDouble(sheet, 42, r, emp.getMedical_value(),dash);
				   addDouble(sheet, 43, r, emp.getArear_medical_value(),dash);
				   addDouble(sheet, 44, r, 0.00,dash);
				   addDouble(sheet, 45, r, emp.getFood_value(),dash);
				   addDouble(sheet, 46, r, 0.00,dash);
				   addDouble(sheet, 47, r, 0.00,dash);
				   addDouble(sheet, 48, r, emp.getLta_value(),dash);
				   addDouble(sheet, 49, r, 0.00,dash);
				   addDouble(sheet, 50, r, 0.00,dash);
				   addDouble(sheet, 51, r, emp.getSpl_incen_value(),dash);
				   addDouble(sheet, 52, r, 0.00,dash);
				   addDouble(sheet, 53, r, 0.00,dash);
				   addDouble(sheet, 54, r, emp.getOt_value(),dash);
				   addDouble(sheet, 55, r, 0.00,dash);
				   addDouble(sheet, 56, r, 0.00,dash);
				   addDouble(sheet, 57, r, emp.getStair_value(),dash);
				   addDouble(sheet, 58, r, 0.00,dash);
				   addDouble(sheet, 59, r, 0.00,dash);
				   addDouble(sheet, 60, r, emp.getMachine1_value(),dash);
				   addDouble(sheet, 61, r, 0.00,dash);
				   addDouble(sheet, 62, r, 0.00,dash);
				   addDouble(sheet, 63, r, emp.getMachine2_value(),dash);
				   addDouble(sheet, 64, r, 0.00,dash);
				   addDouble(sheet, 65, r, 0.00,dash);
			   // 
				   tot5+=(int) ((emp.getBasic_value()+emp.getDa_value()+emp.getHra_value()+emp.getIncentive_value()+emp.getMedical_value()+emp.getOt_value()+emp.getMachine1_value()+emp.getMachine2_value()+emp.getStair_value()+emp.getSpl_incen_value())+0.50);
				   tot6+=(int) ((emp.getArear_basic_value()+emp.getArear_da_value()+emp.getArear_hra_value()+emp.getArear_incentive_value()+emp.getArear_medical_value())+0.50);
				   tot6+=(int) ((emp.getArear2_basic_value()+emp.getArear2_da_value()+emp.getArear2_hra_value()+emp.getArear2_incentive_value())+0.50);

				   if(dash==0)
					   ntot5+=tot5;
				   if(dash==3)
				     tot5=ntot5;
				   addDouble(sheet, 66, r,tot5,dash);
				   addNumber(sheet, 67, r,tot6,dash);
				   addDouble(sheet, 68, r,(tot5+tot6),dash);

			   // deduction
				   addDouble(sheet, 69, r, emp.getPf_value(),dash);
				   addDouble(sheet, 70, r, emp.getArear1_pf_value(),dash);
				   addDouble(sheet, 71, r, emp.getArear2_pf_value(),dash);
				   
				   addDouble(sheet, 72, r, emp.getEsis_value(),dash);
				   addDouble(sheet, 73, r, emp.getArear1_esic_value(),dash);
				   addDouble(sheet, 74, r, emp.getArear2_esic_value(),dash);

				   addDouble(sheet, 75, r, emp.getProf_tax(),dash);
				   addDouble(sheet, 76, r, 0.00,dash);
				   addDouble(sheet, 77, r, emp.getArear2_prof_value(),dash);

				   addDouble(sheet, 78, r, emp.getAdvance(),dash);
				   addDouble(sheet, 79, r, 0.00,dash);
				   addDouble(sheet, 80, r, 0.00,dash);
				   
				   addDouble(sheet, 81, r, emp.getLoan(),dash);
				   addDouble(sheet, 82, r, 0.00,dash);
				   addDouble(sheet, 83, r, 0.00,dash);
				   
				   addDouble(sheet, 84, r, 0.00,dash);   
				   addDouble(sheet, 85, r, 0.00,dash);
				   addDouble(sheet, 86, r, 0.00,dash);

				   
				   ltot9+=(int) ((emp.getPf_value()+emp.getEsis_value()+emp.getProf_tax()+emp.getLoan()+emp.getAdvance())+0.50);   
				   ltot10+=(int) ((emp.getArear1_pf_value()+emp.getArear1_esic_value())+0.50);   
				   ltot10+=(int)((emp.getArear2_pf_value()+emp.getArear2_esic_value()+emp.getArear2_prof_value())+0.50);   

				   
				   addNumber(sheet, 87, r,ltot9,dash);
				   addNumber(sheet, 88, r,ltot10,dash);
				   addNumber(sheet, 89, r,(ltot9+ltot10),dash);
			   //
				   addDouble(sheet, 90, r, emp.getNet_value(),dash);

				   tot4=0;
				   tot5=0;
				   tot6=0;
				   ltot9=0;
				   ltot10=0;
				r++;
				
			}
		 
	}
	public void createReport19Old(WritableSheet sheet,EmptranDto emp) throws WriteException,RowsExceededException
	{

			
		
			int dash=0;
			{
			double totearn=emp.getBasic_value()+emp.getDa_value()+emp.getHra_value()+emp.getAdd_hra_value()+emp.getIncentive_value()+emp.getSpl_incen_value()+emp.getOt_value()+emp.getLta_value()+emp.getMedical_value()+emp.getMisc_value()+emp.getStair_value()+emp.getMachine1_value()+emp.getMachine2_value()+emp.getFood_value();
			double totded=emp.getPf_value()+emp.getEsis_value()+emp.getAdvance()+emp.getCoupon_amt()+emp.getProf_tax()+emp.getLoan();
			double net = totearn-totded;
			if(opt==1 || monthname.startsWith("April"))
				addLabel(sheet, 0, r, emp.getMonname(),dash);
			else
				addLabel(sheet, 0, r, "Upto "+monthname.substring(0, size),dash);
				addNumber(sheet, 1, r, emp.getFin_year(),dash);
				addNumber(sheet, 2, r, emp.getEmp_code(),dash);
				addNumber(sheet, 3, r, emp.getPf_no(),dash);
				addLabel(sheet, 4, r, String.valueOf(emp.getUan_no()),dash);
				addLong(sheet, 5, r, emp.getEsic_no(),dash);
				addLabel(sheet, 6, r, emp.getEmp_name(),dash);
				addLabel(sheet, 7, r, emp.getDesignation(),dash);
				addDouble(sheet,8, r, emp.getAtten_days(),dash);
				addDouble(sheet,9, r, 0.00,dash);  // pl
				addDouble(sheet,10, r, 0.00,dash);  // cl
				
				addDouble(sheet,11, r, emp.getArrear_days(),dash);
				addDouble(sheet,12, r, 0.00,dash);  // p.h.
				addDouble(sheet,13, r, emp.getOt_rate(),dash);
				addDouble(sheet,14, r, emp.getAbsent_days(),dash);  // absemt

				addDouble(sheet,15, r, emp.getExtra_hrs(),dash);

				
				addDouble(sheet, 16, r, emp.getBasic(),dash);
				addDouble(sheet, 17, r, emp.getDa(),dash);
				addDouble(sheet,18, r, emp.getHra(),dash);
				addDouble(sheet,19, r, emp.getAdd_hra(),dash);
				addDouble(sheet,20, r, emp.getIncentive(),dash);
				
				addDouble(sheet,21, r, emp.getGross(),dash);

				

				
				addDouble(sheet,22, r, emp.getBasic_value(),dash);
				addDouble(sheet,23, r, emp.getDa_value(),dash);
				addDouble(sheet,24, r, emp.getHra_value(),dash);
				addDouble(sheet,25, r, emp.getAdd_hra_value(),dash);
				addDouble(sheet,26, r, emp.getIncentive_value(),dash);
				addDouble(sheet,27, r, (emp.getSpl_incen_value()+emp.getStair_value()),dash);
				addDouble(sheet,28, r, emp.getFood_value(),dash);
				addDouble(sheet,29, r, emp.getOt_value(),dash);
				addDouble(sheet,30, r, emp.getMachine1_value(),dash);
				addDouble(sheet,31, r, emp.getMachine2_value(),dash);
				addDouble(sheet,32, r, emp.getMedical_value(),dash);
				addDouble(sheet,33, r, emp.getLta_value(),dash);
				addDouble(sheet,34, r, emp.getMisc_value(),dash);

				addDouble(sheet,35, r, (emp.getBasic_value()+emp.getDa_value()+emp.getHra_value()+emp.getAdd_hra_value()+emp.getIncentive_value()+emp.getSpl_incen_value()+emp.getFood_value()+emp.getOt_value()+emp.getMachine1_value()+emp.getMachine2_value()+emp.getMedical_value()+emp.getLta_value()+emp.getStair_value()+emp.getMisc_value()),dash);
				addDouble(sheet,36, r, emp.getPf_value(),dash);
				addDouble(sheet,37, r, emp.getEsis_value(),dash);
				addDouble(sheet,38, r, emp.getProf_tax(),dash);
				
				addDouble(sheet,39, r, emp.getAdvance(),dash);
				addDouble(sheet,40, r, emp.getLoan(),dash);
				addDouble(sheet,41, r, emp.getTds_value(),dash);
				addDouble(sheet,42, r, emp.getCoupon_amt(),dash);
				
				addDouble(sheet,43, r, (emp.getPf_value()+emp.getEsis_value()+emp.getAdvance()+emp.getProf_tax()+emp.getLoan()+emp.getTds_value()+emp.getCoupon_amt()),dash);
				addDouble(sheet,44, r, net,dash);

				
				r++;
				
			}
			 
		 
	}
	
	
	public void createCSV(File filename)
	{
		try
	    {
	      //File to store data in form of CSV
	    //  File f = new File(drvnm+"\\input.csv");
			
		  String fname="ECR-"+cmp_name.substring(0, 6)+"-"+monthname;
		  if(opt==2)
			  fname="Arrear-"+cmp_name.substring(0, 6)+"-"+monthname;
	      File f = new File(drvnm+"\\"+fname+".txt");
	 
	      OutputStream os = (OutputStream)new FileOutputStream(f);
	      String encoding = "UTF8";
	      OutputStreamWriter osw = new OutputStreamWriter(os, encoding);
	      BufferedWriter bw = new BufferedWriter(osw);
	 
	      //Excel document to be imported
	     // String filename = "input.xls";
	      WorkbookSettings ws = new WorkbookSettings();
	      ws.setLocale(new Locale("en", "EN"));
//	      Workbook w = Workbook.getWorkbook(new File(filename),ws);
	      Workbook w = Workbook.getWorkbook(filename,ws);
	 
	      // Gets the sheets from workbook
	      for (int sheet = 0; sheet < w.getNumberOfSheets(); sheet++)
	      {
	        Sheet s = w.getSheet(sheet);
	 
	       // bw.write(s.getName());
	       // bw.newLine();
	 
	        Cell[] row = null;
	         
//	        for (int i = 0 ; i < s.getRows() ; i++)
	        // Gets the cells from sheet
	        for (int i = 1 ; i < s.getRows() ; i++)
	        {
	          row = s.getRow(i);
	 
	          if (row.length > 0)
	          {
	            bw.write(row[0].getContents());
	            for (int j = 3; j < row.length; j++)
	            {
//	              bw.write(',');
	              bw.write("#~#");
	              bw.write(row[j].getContents());
	            }
	          }
	          bw.newLine();
	        }
	      }
	      bw.flush();
	      bw.close();
	       
	    }
	    catch (UnsupportedEncodingException e)
	    {
	      System.err.println(e.toString());
	    }
	    catch (IOException e)
	    {
	      System.err.println(e.toString());
	    }
	    catch (Exception e)
	    {
	      System.err.println(e.toString());
	    }
	}
	
	
	public double roundTwoDecimals(double d) 
	{
	    DecimalFormat twoDForm = new DecimalFormat("0.00");
	    
	    double roundval = Double.valueOf(twoDForm.format(d));
	    return ((int) (roundval+.50));
//	    return Double.valueOf(twoDForm.format((int) (d+.50)));
	}	
}
