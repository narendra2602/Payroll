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


public class EsicList  extends WriteExcel
{    
   
   int r,i,sno;
   SimpleDateFormat sdf;
   String inputFile;
    
   private String  drvnm,flname,cmp_name,monthname; 
   private int depo_code, cmp_code, fyear, mnth_code,btnno,repno,opt;
   private double totadv,totloan,atten_days,extra_hrs,arrear_days,sterlite_days,absent_days,btotal,bgtotal;
   private double basic,emppf,emppf1,eps,epf,edli,net,epfc,epsc,tot;
   ArrayList<?> esicList;
   SheetSettings settings; 
   boolean print=false;

  public EsicList(Integer depo_code,Integer cmp_code,Integer fyear,Integer mnth_code,String cmp_name,String drvnm,String monthname,Integer btnno,Integer repno,Integer opt) 
  
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
    	sdf = new SimpleDateFormat("dd/MM/yyyy");
    	
    	
    	flname="ESICList-"+cmp_name.substring(0, 6)+"-"+monthname;
    	if(repno==2 && opt==1)
    		flname="PFList-"+cmp_name.substring(0, 6)+"-"+monthname;
    	else if(repno==2 && opt==2)
    		flname="ArrearList-"+cmp_name.substring(0, 6)+"-"+monthname;
    	else if(repno==3)
    		flname="LoanList-"+cmp_name.substring(0, 6)+"-"+monthname;
    	else if(repno==4)
    		flname="Sterile-"+cmp_name.substring(0, 6)+"-"+monthname;
    	else if(repno==5)
    		flname="AttenList-"+cmp_name.substring(0, 6)+"-"+monthname;
    	else if(repno==6)
    		flname="OTList-"+cmp_name.substring(0, 6)+"-"+monthname;
    	else if(repno==7)
    		flname="AbsentList-"+cmp_name.substring(0, 6)+"-"+monthname;
    	else if(repno==8)
    		flname="MiscList-"+cmp_name.substring(0, 6)+"-"+monthname;
    	else if(repno==10)
    		flname="BankList-"+cmp_name.substring(0, 6)+"-"+monthname;
    	else if(repno==11)
    		flname="CouponList-"+cmp_name.substring(0, 6)+"-"+monthname;
    	else if(repno==12)
    		flname="LTAList-"+cmp_name.substring(0, 6)+"-"+monthname;
    	else if(repno==13)
    		flname="LTAReg-"+cmp_name.substring(0, 6)+"-"+monthname;
    	else if(repno==14)
    		flname="UnPaidLta-"+cmp_name.substring(0, 6)+"-"+monthname;
    	else if(repno==15)
    		flname="Machine-"+cmp_name.substring(0, 6)+"-"+monthname;
    	else if(repno==16)
    		flname="BonusList-"+cmp_name.substring(0, 6)+"-"+(fyear+1);
    	else if(repno==17)
    		flname="EmpBasicList-"+cmp_name.substring(0, 6)+"-"+(fyear+1);
    	else if(repno==18)
    		flname="EmpEarningList-"+cmp_name.substring(0, 6)+"-"+(fyear+1);
    	else if(repno==19)
    		flname="SalaryDetail-"+cmp_name.substring(0, 6)+"-"+(fyear+1);




        jbInit();
  
    	File file=null;
    	
		if (Desktop.isDesktopSupported()) {
		
				
  				file=new File(drvnm+"\\"+flname+".xls");
  				if(repno==2 && btnno==1) // PF Upload CSV 
  				    createCSV(file);
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
	    	if(repno==19)
	    		esicList=pdao.getSalaryDetail(depo_code, cmp_code, fyear, repno,mnth_code);
	    	else if(repno==16)
	    		esicList=pdao.getBonusRegister(depo_code, cmp_code, fyear,repno);
	    	else if(repno==10 || repno==17)
	    		esicList= (ArrayList<?>) pdao.getSalaryRegister(depo_code, cmp_code, fyear, mnth_code,repno);
	    	else if(repno==13 || repno==14)
	    		esicList=pdao.getLTAList(depo_code, cmp_code, fyear, repno);
	    	else if(repno==18)
	    		esicList=pdao.getIncrementList(depo_code, cmp_code, fyear, repno,mnth_code);
	    	else
	    		esicList=pdao.getEsicList(depo_code, cmp_code, fyear, mnth_code,repno);
	    	
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
		   addCaption1(sheet, 0, 1, "  ESIC LIST FOR THE MONTH OF "+monthname,40);

		   addCaption2(sheet, 0, 3, "Sno",10);
		   addCaption2(sheet, 1, 3, "Employee Code",10);
		   addCaption2(sheet, 2, 3, "ESIC Number",10);
		   addCaption2(sheet, 3, 3, "Paid Days ",20);
		   addCaption2(sheet, 4, 3, "Gross Wages",20);
		   addCaption2(sheet, 5, 3, "IP Name",30);
		   addCaption2(sheet, 6, 3, "Total Wages",20);
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
		   else if(opt==2) // Arrear Radio Button (excel)
		   {
			   addCaption2(sheet, 0, 0, "EMP CODE",10);
			   addCaption2(sheet, 1, 0, "PF NO",10);
			   addCaption2(sheet, 2, 0, "UAN",15);
			   addCaption2(sheet, 3, 0, "PAN",15);
			   addCaption2(sheet, 4, 0, "AADHAR",15);
			   addCaption2(sheet, 5, 0, "MEMBER NAME ",30);
			   addCaption2(sheet, 6, 0, "ARREAR EPF WAGES",15);
			   addCaption2(sheet, 7, 0, "ARREAR EPS WAGES",15);
			   addCaption2(sheet, 8, 0, "ARREAR EDLI WAGES",15);
			   addCaption2(sheet, 9, 0, "ARREAR  WAGES",15);
			   addCaption2(sheet, 10, 0, "ARREAR EPF EE SHARE",15);
			   addCaption2(sheet, 11, 0, "ARREAR EPF ER SHARE",15);
			   addCaption2(sheet, 12, 0, "ARREAR EPS SHARE",15);
			   addCaption2(sheet, 13, 0, "ARREAR DAYS",15);
			   addCaption2(sheet, 14, 0, "PRESENT DAYS",15);
			   addCaption2(sheet, 15, 0, "NCP DAYS",15);
			   
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
	   
	   sheet.mergeCells(8, 3, 13, 3);
	   addCaption1(sheet, 8, 3, "Acual Salary", 45);

	   sheet.mergeCells(15, 3, 20, 3);
	   addCaption1(sheet, 15, 3, "Salary Got", 45);

	   
	   addCaption2(sheet, 0, 4, "Month",10);
	   addCaption2(sheet, 1, 4, "Year",10);
	   addCaption2(sheet, 2, 4, "Employee Code",10);
	   addCaption2(sheet, 3, 4, "PF No.",10);
	   addCaption2(sheet, 4, 4, "UAN No.",25);
	   addCaption2(sheet, 5, 4, "Esic No.",10);
	   addCaption2(sheet, 6, 4, "Employee Name",30);
	   addCaption2(sheet, 7, 4, "Category/Grade",30);
	   addCaption2(sheet, 8, 4, "Basic ",20);
	   addCaption2(sheet, 9, 4, "DA",20);
	   addCaption2(sheet, 10, 4, "HRA",20);
	   addCaption2(sheet, 11, 4, "A.HRA",20);
	   addCaption2(sheet, 12, 4, "INC",20);
	   addCaption2(sheet, 13, 4, "Total",20);
	   addCaption2(sheet, 14, 4, "Present Days",20);
	   addCaption2(sheet, 15, 4, "Basic ",20);
	   addCaption2(sheet, 16, 4, "DA",20);
	   addCaption2(sheet, 17, 4, "HRA",20);
	   addCaption2(sheet, 18, 4, "A.HRA",20);
	   addCaption2(sheet, 19, 4, "INC",20);
	   addCaption2(sheet, 20, 4, "Total",20);
	   addCaption2(sheet, 21, 4, "Arrear Days",20);
	   addCaption2(sheet, 22, 4, "Arrear Amount",20);
	   addCaption2(sheet, 23, 4, "OT Rates",20);
	   addCaption2(sheet, 24, 4, "OT Hours",20);
	   addCaption2(sheet, 25, 4, "OT Value",20);
	   addCaption2(sheet, 26, 4, "Total Salary",20);
	   addCaption2(sheet, 27, 4, "PF Ded",20);
	   addCaption2(sheet, 28, 4, "ESIC Ded",20);
	   addCaption2(sheet, 29, 4, "Total Ded",20);
	   addCaption2(sheet, 30, 4, "Net Payable Salary",20);


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
							addLabel(sheet, 0, r, " ",1);
							addLabel(sheet, 1, r, " ",1);
							addLabel(sheet, 2, r, " ",1);
							addLabel(sheet, 3, r, " ",1);
							addLabel(sheet, 4, r, " ",1);
							addLabel(sheet, 5, r, "Total ",1);
							addDouble(sheet, 6, r, btotal,1);
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
						addLabel(sheet, 0, r, " ",1);
						addLabel(sheet, 1, r, " ",1);
						addLabel(sheet, 2, r, " ",1);
						addLabel(sheet, 3, r, " ",1);
						addLabel(sheet, 4, r, " ",1);
						addLabel(sheet, 5, r, "Total ",1);
						addDouble(sheet, 6, r, btotal,1);
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
			  addLabel(sheet, 0, r, "",1);
			  addLabel(sheet, 1, r, "",1);
			  addLabel(sheet, 2, r, "",1);
			  addDouble(sheet, 3, r, atten_days,1);
			  addLabel(sheet, 4, r, "",1);
			  addLabel(sheet, 5, r, "Total",1);
			  addDouble(sheet, 6, r, basic,1);
			  addDouble(sheet, 7, r, emppf,1);
			  addDouble(sheet, 8, r, emppf1,1);
			  addDouble(sheet, 9, r, (emppf+emppf1),1);
		  }
		  else if(repno==2 && btnno==22)
		  {
			  addLabel(sheet, 0, r, "",1);
			  addLabel(sheet, 1, r, "",1);
			  addLabel(sheet, 2, r, "",1);
			  addLabel(sheet, 3, r, "Total",1);
			  addDouble(sheet, 4, r, atten_days,1);
			  addDouble(sheet, 5, r, basic,1);
			  addDouble(sheet, 6, r, emppf,1);
			  addDouble(sheet, 7, r, emppf1,1);
			  addDouble(sheet, 8, r, (emppf+emppf1),1);
			  addDouble(sheet, 9, r, eps,1);
			  addDouble(sheet, 10, r, (emppf+emppf1+eps),1);
		  }
		  else if(repno==3 || repno==12)
		  {
			  addLabel(sheet, 0, r, "",1);
			  addLabel(sheet, 1, r, "",1);
			  addLabel(sheet, 2, r, "Total",1);
			  addDouble(sheet, 3, r, totadv,1);
			  addDouble(sheet, 4, r, totloan,1);
		  }
		  else if(repno==4)
		  {
			  addLabel(sheet, 0, r, "",1);
			  addLabel(sheet, 1, r, "",1);
			  addLabel(sheet, 2, r, "Total",1);
			  addDouble(sheet, 3, r, sterlite_days,1);
			  addLabel(sheet, 4, r, "",1);
			  addDouble(sheet, 5, r, totadv,1);
			  addLabel(sheet, 6, r, "",1);
		  }
		  else if(repno==6)
		  {
			  addLabel(sheet, 0, r, "",1);
			  addLabel(sheet, 1, r, "",1);
			  addLabel(sheet, 2, r, "Total",1);
			  addDouble(sheet, 3, r, extra_hrs,1);
			  addLabel(sheet, 4, r, "",1);
			  addDouble(sheet, 5, r, totadv,1);
			  addLabel(sheet, 6, r, "",1);
		  }
		  else if(repno==5)
		  {
			  addLabel(sheet, 0, r, "",1);
			  addLabel(sheet, 1, r, "Total",1);
			  addDouble(sheet, 2, r, atten_days,1);
			  addDouble(sheet, 3, r, extra_hrs,1);
			  addDouble(sheet, 4, r, arrear_days,1);
			  addDouble(sheet, 5, r, sterlite_days,1);
			  addDouble(sheet, 6, r, totadv,1);
			  addDouble(sheet, 7, r, totloan,1);
		  }
		  else if(repno==7 || repno==9)
		  {
			  addLabel(sheet, 0, r, "",1);
			  addLabel(sheet, 1, r, "",1);
			  addLabel(sheet, 2, r, "Total",1);
			  addDouble(sheet, 3, r, absent_days,1); // absent/present days total 
		  }
		  else if(repno==8 || repno==11)
		  {
			  addLabel(sheet, 0, r, "",1);
			  addLabel(sheet, 1, r, "",1);
			  addLabel(sheet, 2, r, "Total",1);
			  addDouble(sheet, 3, r, totadv,1);
		  }
		  else if(repno==10 || repno==16)
		  {
			  	addLabel(sheet, 0, r, " ",1);
				addLabel(sheet, 1, r, " ",1);
				addLabel(sheet, 2, r, " ",1);
				addLabel(sheet, 3, r, " ",1);
				addLabel(sheet, 4, r, " ",1);
				addLabel(sheet, 5, r, "Total ",1);
				addDouble(sheet, 6, r, btotal,1);
				r++;
			  	addLabel(sheet, 0, r, " ",1);
				addLabel(sheet, 1, r, " ",1);
				addLabel(sheet, 2, r, " ",1);
				addLabel(sheet, 3, r, " ",1);
				addLabel(sheet, 4, r, " ",1);
				addLabel(sheet, 5, r, "Grand Total ",1);
				addDouble(sheet, 6, r, bgtotal,1);
				r++;
		  }
		  else if(repno==13 )
		  {
			  addLabel(sheet, 0, r, "",1);
			  addLabel(sheet, 1, r, "",1);
			  addLabel(sheet, 2, r, "",1);
			  addLabel(sheet, 3, r, "Total",1);
			  addDouble(sheet, 4, r, totadv,1);
			  addDouble(sheet, 5, r, totloan,1);
		  }
		  else if(repno==15)
		  {
			  addLabel(sheet, 0, r, "",1);
			  addLabel(sheet, 1, r, "",1);
			  addLabel(sheet, 2, r, "Total",1);
			  addDouble(sheet, 3, r, sterlite_days,1);
			  addDouble(sheet, 4, r, arrear_days,1);
			  addLabel(sheet, 5, r, "",1);
			  addLabel(sheet, 6, r, "",1);
			  addDouble(sheet, 7, r, totadv,1);
			  addDouble(sheet, 8, r, totloan,1);
			  addLabel(sheet, 9, r, "",1);
		  }

		  
	}

	public void createReport1(WritableSheet sheet,EmptranDto emp) throws WriteException,RowsExceededException
	{
	
		
			int dash=0;
			if(btnno==1)
			{
				
				if(emp.getDiffdays()==0 || (emp.getDiffdays()> -31 && emp.getDiffdays()< 0))
				{
					if(opt==1)
					{
						addLabel(sheet, 0, r, String.valueOf(emp.getEmp_code()),dash);
						addLabel(sheet, 1, r, String.valueOf(emp.getEsic_no()),dash);
						addLabel(sheet, 2, r, emp.getEmp_name(),dash);
						addLabel(sheet, 3, r, String.valueOf((int) emp.getAtten_days()),dash);
						addLabel(sheet, 4, r, String.valueOf(emp.getNet_value()),dash);
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
					else if(opt==2 && emp.getArrear_days()>0)
					{
						addLabel(sheet, 0, r, String.valueOf(emp.getEmp_code()),dash);
						addLabel(sheet, 1, r, String.valueOf(emp.getEsic_no()),dash);
						addLabel(sheet, 2, r, emp.getEmp_name(),dash);
						addLabel(sheet, 3, r, String.valueOf((int) emp.getAtten_days()),dash);
						addLabel(sheet, 4, r, String.valueOf(emp.getNet_value()),dash);
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
			else if(btnno==2)
			{
	
				if(opt==1)
				{
					addNumber(sheet, 0, r, emp.getSerialno(),dash);
					addNumber(sheet, 1, r, emp.getEmp_code(),dash);
					addLong(sheet, 2, r, emp.getEsic_no(),dash);
					addNumber(sheet, 3, r, ((int) emp.getAtten_days()),dash);
					addDouble(sheet, 4, r, emp.getGross(),dash);
					addLabel(sheet, 5, r, emp.getEmp_name(),dash);
					addDouble(sheet, 6, r, emp.getNet_value(),dash);
					addDouble(sheet, 7, r, emp.getEsis_value(),dash);
					addDouble(sheet, 8, r, emp.getEmployer_esis_value(),dash);
					addDouble(sheet, 9, r, (emp.getEsis_value()+emp.getEmployer_esis_value()),dash);
					r++;
					atten_days+=(int) emp.getAtten_days();
					basic+=(opt==1?emp.getNet_value():emp.getArrear_basic_value());
					emppf+=emp.getEsis_value();
					emppf1+=emp.getEmployer_esis_value();
				}
				else if(opt==2 && emp.getArrear_days()>0)
				{
					addNumber(sheet, 0, r, emp.getSerialno(),dash);
					addNumber(sheet, 1, r, emp.getEmp_code(),dash);
					addLong(sheet, 2, r, emp.getEsic_no(),dash);
					addNumber(sheet, 3, r, ((int) emp.getArrear_days()),dash);
					addDouble(sheet, 4, r, emp.getGross(),dash);
					addLabel(sheet, 5, r, emp.getEmp_name(),dash);
					addDouble(sheet, 6, r, emp.getArrear_basic_value(),dash);

					addDouble(sheet, 7, r, emp.getEsis_value(),dash);
					addDouble(sheet, 8, r, emp.getEmployer_esis_value(),dash);
					addDouble(sheet, 9, r, (emp.getEsis_value()+emp.getEmployer_esis_value()),dash);
					r++;
					atten_days+=(int) emp.getArrear_days();
					basic+=(opt==1?emp.getNet_value():emp.getArrear_basic_value());
					emppf+=emp.getEsis_value();
					emppf1+=emp.getEmployer_esis_value();
				}
				
//				atten_days+=emp.getAtten_days();
			}
	
			
			 
	
		 
	}

	
	public void createReport2(WritableSheet sheet,EmptranDto emp) throws WriteException,RowsExceededException
	{
	 
		
			int dash=0;
			double epfcontri=0.00;
			double epscontri=0.00;
			double basicValue=0.00;
			double netValue=0.00;
		
			
/*				addLabel(sheet, 0, r, String.valueOf(emp.getPf_no()),dash);
				addLabel(sheet, 1, r, String.valueOf(emp.getGross()),dash);
				addLabel(sheet, 2, r, String.valueOf(emp.getPf_value()),dash);
				addLabel(sheet, 3, r, String.valueOf(0.00),dash);
				addLabel(sheet, 4, r, String.valueOf(emp.getAtten_days()),dash);
				if(emp.getCreated_date()!=null)
					addLabel(sheet, 5, r, sdf.format(emp.getCreated_date()),dash);
				else
					addLabel(sheet, 5, r, "",dash);
				addLabel(sheet, 6, r, String.valueOf(emp.getCreated_by()),dash); // reason for leaving
				addLabel(sheet, 7, r, String.valueOf(0.00),dash);
				addLabel(sheet, 8, r, String.valueOf(0.00),dash);
				addLabel(sheet, 9, r, String.valueOf(0.00),dash);
				addLabel(sheet, 10, r, String.valueOf(0.00),dash);
*/				
			
			if(emp.getAtten_days()>0) // UPLOAD BUTTON
			{
						if(opt==1)
						{
							basicValue=emp.getBasic_value()-emp.getArrear_basic_value();
							netValue=emp.getNet_value()-emp.getArrear_net_value();
							
							if(emp.getBasicpf_value()>15000 && basicValue>0) //change on 04/03/2022 by Yashpal
//							if(emp.getGross()>15000 && basicValue>0)   //change on 06/04/2022 by Yashpal
							{
								epfcontri=roundTwoDecimals(15000*12/100);
								epscontri=roundTwoDecimals(15000*8.33/100);
							}
							else
							{
								epfcontri=roundTwoDecimals(basicValue*12/100);
								epscontri=roundTwoDecimals(basicValue*8.33/100);
							}
		
							if(emp.getMnth_code()>2020044)
								epfcontri=roundTwoDecimals(basicValue*10/100);
							
							//Change on 08/03/2022 by Yashpal (diff will be add in diff column
							if(emp.getMnth_code()>=202202 && epscontri>1250)
							{
								epscontri=1250;
							}
							
							if(btnno==1)
							{
//								addLong(sheet, 0, r, emp.getUan_no(),dash);
								addLabel(sheet, 0, r, String.valueOf(emp.getUan_no()),dash);
								addLabel(sheet, 1, r, String.valueOf(emp.getAdhar_no()),dash);
								addLabel(sheet, 2, r, emp.getPan_no(),dash);
								addLabel(sheet, 3, r, emp.getEmp_name(),dash);
								addNumber(sheet, 4, r, (int) netValue,dash);
//								addNumber(sheet, 5, r, (int) basicValue,dash); change on 28/05/2020 by Yashpal
								
//								change on 04/03/2022 by Yashpal again change on 06/04/2022			
								addNumber(sheet, 5, r, (int) (basicValue>15000?15000:basicValue),dash);
								addNumber(sheet, 6, r, (int) (basicValue>15000?15000:basicValue),dash);
								addNumber(sheet, 7, r, (int) (basicValue>15000?15000:basicValue),dash);
								
/*								addNumber(sheet, 5, r, (int) (emp.getGross()>15000?15000:basicValue),dash);
								addNumber(sheet, 6, r, (int) (emp.getGross()>15000?15000:basicValue),dash);
								addNumber(sheet, 7, r, (int) (basicValue>15000?15000:basicValue),dash);
*/								//change on 09/03/2022 by Yahpal
//								addNumber(sheet, 7, r, (int) (emp.getGross()>15000?15000:basicValue),dash);

								
								addNumber(sheet, 8, r, (int) epfcontri,dash);
								addNumber(sheet, 9, r, (int) epscontri,dash);
								addNumber(sheet, 10, r, (int) (epfcontri-epscontri),dash);
								addNumber(sheet, 11, r, (int) emp.getAbsent_days(),dash);
								addLabel(sheet, 12, r, String.valueOf(0),dash);
							}
							else if(btnno==2)
							{
								addNumber(sheet, 0, r, emp.getEmp_code(),dash);
								addLabel(sheet, 1, r, String.valueOf(emp.getPf_no()),dash);
								addLabel(sheet, 2, r, String.valueOf(emp.getUan_no()),dash);
//								addLong(sheet, 2, r, emp.getUan_no(),dash);
								addLabel(sheet, 3, r, emp.getEmp_name(),dash);
								addNumber(sheet, 4, r, (int) netValue,dash);
//								addNumber(sheet, 5, r, (int) basicValue,dash); change on 28/05/2020 by Yashpal
								
//								change on 04/03/2022 by Yashpal	and again change on 06/04/2022
								addNumber(sheet, 5, r, (int) (basicValue>15000?15000:basicValue),dash);
								addNumber(sheet, 6, r, (int) (basicValue>15000?15000:basicValue),dash);
								addNumber(sheet, 7, r, (int) (basicValue>15000?15000:basicValue),dash);
								
								//addNumber(sheet, 5, r, (int) (emp.getGross()>15000?15000:basicValue),dash);
								//addNumber(sheet, 6, r, (int) (emp.getGross()>15000?15000:basicValue),dash);
								//addNumber(sheet, 7, r, (int) (basicValue>15000?15000:basicValue),dash);
//								addNumber(sheet, 7, r, (int) (emp.getGross()>15000?15000:basicValue),dash); // comment on 09/03/200 by Yashpal

								addNumber(sheet, 8, r, (int) epfcontri,dash);
								addNumber(sheet, 9, r, (int) epscontri,dash);
								addNumber(sheet, 10, r, (int) (epfcontri-epscontri),dash);
								addNumber(sheet, 11, r, (int) emp.getAbsent_days(),dash);
								addLabel(sheet, 12, r, String.valueOf(0),dash);
							}
							r++;
						}
						
						else if(opt==2 && emp.getArrear_days()>=0 && emp.getArrear_basic_value()>0) // arrear button isSelected
						{
							basicValue=emp.getArrear_basic_value();
							netValue=emp.getArrear_net_value();
							//YEH LINE ABHI LAGAYO 15/05/2020	
							//basicValue=emp.getBasic_value()+emp.getArrear_basic_value();
							//netValue=emp.getNet_value()+emp.getArrear_net_value();
							
							if(emp.getBasicpf_value()>15000 && basicValue>0)

							{
								epfcontri=roundTwoDecimals(15000*12/100);
								epscontri=roundTwoDecimals(15000*8.33/100);
							}
							else
							{
								epfcontri=roundTwoDecimals(basicValue*12/100);
								epscontri=roundTwoDecimals(basicValue*8.33/100);
							}
							
		
							if(emp.getMnth_code()>2020044)
								epfcontri=roundTwoDecimals(basicValue*10/100);

		
							if(btnno==1) // upload button
							{
//								addLong(sheet, 0, r, emp.getUan_no(),dash);
								addLabel(sheet, 0, r, String.valueOf(emp.getUan_no()),dash);
								addLabel(sheet, 1, r, String.valueOf(emp.getAdhar_no()),dash);
								addLabel(sheet, 2, r, emp.getPan_no(),dash);
								addLabel(sheet, 3, r, emp.getEmp_name(),dash);
//								addNumber(sheet, 4, r, (int) netValue,dash);
								addNumber(sheet, 4, r, (int) (basicValue>15000?15000:basicValue),dash);
								addNumber(sheet, 5, r, (int) (basicValue>15000?15000:basicValue),dash);
								addNumber(sheet, 6, r, (int) (basicValue>15000?15000:basicValue),dash);
								addNumber(sheet, 7, r, (int) (basicValue>15000?15000:basicValue),dash);
								addNumber(sheet, 8, r, (int) epfcontri,dash);
								addNumber(sheet, 9, r, (int) epscontri,dash);
								addNumber(sheet, 10, r, (int) (epfcontri-epscontri),dash);					
								addNumber(sheet, 11, r, (int) emp.getNcp_days(),dash);
//								addNumber(sheet, 12, r, (int) emp.getPrev_days(),dash); //comment on 09/03/2022 by YashPal
								addNumber(sheet, 12, r, 0,dash); //fixed 0 on 09/03/2022 by YashPal
								
//								addNumber(sheet, 11, r, (int) emp.getAbsent_days(),dash);
//								addNumber(sheet, 12, r, (int) emp.getAtten_days(),dash);
							}
							else if(btnno==2) //excel button
							{
								addNumber(sheet, 0, r, emp.getEmp_code(),dash);
								addLabel(sheet, 1, r, String.valueOf(emp.getPf_no()),dash);
								addLabel(sheet, 2, r, String.valueOf(emp.getUan_no()),dash);
								addLabel(sheet, 3, r, emp.getPan_no(),dash);
								addLabel(sheet, 4, r, String.valueOf(emp.getAdhar_no()),dash);
								//addLong(sheet, 2, r, emp.getUan_no(),dash);
								addLabel(sheet, 5, r, emp.getEmp_name(),dash);
//								addNumber(sheet, 4, r, (int) netValue,dash);
								addNumber(sheet, 6, r, (int) (basicValue>15000?15000:basicValue),dash);
								addNumber(sheet, 7, r, (int) (basicValue>15000?15000:basicValue),dash);
								addNumber(sheet, 8, r, (int) (basicValue>15000?15000:basicValue),dash);
								addNumber(sheet, 9, r, (int) (basicValue>15000?15000:basicValue),dash);
								addNumber(sheet, 10, r, (int) epfcontri,dash);
								addNumber(sheet, 11, r, (int) epscontri,dash);
								addNumber(sheet, 12, r, (int) (epfcontri-epscontri),dash);					
								addNumber(sheet, 13, r, (int) emp.getArrear_days(),dash);
								addNumber(sheet, 14, r, (int) emp.getPrev_days(),dash);
								
								addNumber(sheet, 15, r, (int) emp.getNcp_days(),dash);
//								addNumber(sheet, 15, r, 0,dash); //fixed 0 on 09/03/2022 by YashPal
								
//								addNumber(sheet, 14, r, (int) emp.getAtten_days(),dash);
//								addNumber(sheet, 15, r, (int) emp.getAbsent_days(),dash);
							}
						 	r++;
		
						}
						
						net+= (int) netValue;
						basic+=(int) basicValue;
						epf+=(int) basicValue;
						
//						change on 04/03/2022 by Yashpal
/*						eps+=(int) (basicValue>15000?15000:basicValue);
						epf+=(int) (basicValue>15000?15000:basicValue);
*/						
						eps+=(int) (emp.getGross()>15000?15000:basicValue);
						epf+=(int) (emp.getGross()>15000?15000:basicValue);

						epfc+=epf;
						epsc+=eps;

						 

			}	
						
						
			 
			/*else if(btnno==2) // EXCEL BUTTON
			{
	
				addNumber(sheet, 0, r, emp.getSerialno(),dash);
				addNumber(sheet, 1, r, emp.getPf_no(),dash);
				addLong(sheet, 2, r, emp.getUan_no(),dash);
				addDouble(sheet, 3, r, emp.getBasic(),dash);
				addLabel(sheet, 4, r, emp.getEmp_name(),dash);
				addNumber(sheet, 5, r, ((int) emp.getAtten_days()),dash);
				addDouble(sheet, 6, r, emp.getBasic_value(),dash);
				addDouble(sheet, 7, r, emp.getEmployee_pf(),dash);
				addDouble(sheet, 8, r, emp.getEmployer_pf(),dash);
				addDouble(sheet, 9, r, (emp.getEmployee_pf()+emp.getEmployer_pf()),dash);
				addDouble(sheet, 10, r, emp.getEps_pf(),dash);
				addDouble(sheet, 11, r, (emp.getEmployee_pf()+emp.getEmployer_pf()+emp.getEps_pf()),dash);
				
				atten_days+=emp.getAtten_days();
				basic+=emp.getBasic_value();
				emppf+=emp.getEmployee_pf();
				emppf1+=emp.getEmployer_pf();
				eps+=emp.getEps_pf();
				r++;
			}
	*/
			 
		 
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
			double totearn=emp.getBasic_value()+emp.getDa_value()+emp.getHra_value()+emp.getAdd_hra_value()+emp.getIncentive_value()+emp.getSpl_incen_value()+emp.getOt_value()+emp.getLta_value()+emp.getMedical_value()+emp.getMisc_value()+emp.getStair_value()+emp.getMachine1_value()+emp.getMachine2_value()+emp.getFood_value();
//			double totearn=emp.getBasic_value()+emp.getDa_value()+emp.getIncentive_value()+emp.getOt_value()+emp.getHra_value()+emp.getAdd_hra_value()+emp.getSpl_incen_value()+emp.getMisc_value()+emp.getLta_value()+emp.getMedical_value()+emp.getStair_value();
			double totded=emp.getPf_value()+emp.getEsis_value()+emp.getAdvance()+emp.getCoupon_amt()+emp.getProf_tax();;
			double net = totearn-totded;
			
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
			{
				addLabel(sheet, 0, r, emp.getMonname(),dash);
				addNumber(sheet, 1, r, emp.getFin_year(),dash);
				addNumber(sheet, 2, r, emp.getEmp_code(),dash);
				addNumber(sheet, 3, r, emp.getPf_no(),dash);
				addLabel(sheet, 4, r, String.valueOf(emp.getUan_no()),dash);
				addLong(sheet, 5, r, emp.getEsic_no(),dash);
				addLabel(sheet, 6, r, emp.getEmp_name(),dash);
				addLabel(sheet, 7, r, emp.getDesignation(),dash);
				addDouble(sheet, 8, r, emp.getBasic(),dash);
				addDouble(sheet, 9, r, emp.getDa(),dash);
				addDouble(sheet,10, r, emp.getHra(),dash);
				addDouble(sheet,11, r, emp.getAdd_hra(),dash);
				addDouble(sheet,12, r, emp.getIncentive(),dash);
				addDouble(sheet,13, r, emp.getGross(),dash);
				addDouble(sheet,14, r, emp.getAtten_days(),dash);
				addDouble(sheet,15, r, emp.getBasic_value(),dash);
				addDouble(sheet,16, r, emp.getDa_value(),dash);
				addDouble(sheet,17, r, emp.getHra_value(),dash);
				addDouble(sheet,18, r, emp.getAdd_hra_value(),dash);
				addDouble(sheet,19, r, emp.getIncentive_value(),dash);
				addDouble(sheet,20, r, emp.getMisc_value(),dash);
				addDouble(sheet,21, r, emp.getArrear_days(),dash);
				addDouble(sheet,22, r, emp.getArrear_amt(),dash);
				addDouble(sheet,23, r, emp.getOt_rate(),dash);
				addDouble(sheet,24, r, emp.getExtra_hrs(),dash);
				addDouble(sheet,25, r, emp.getOt_value(),dash);
				addDouble(sheet,26, r, emp.getMisc_value()+emp.getOt_value(),dash);
				addDouble(sheet,27, r, emp.getPf_value(),dash);
				addDouble(sheet,28, r, emp.getEsis_value(),dash);
				addDouble(sheet,29, r, (emp.getPf_value()+emp.getEsis_value()),dash);
				addDouble(sheet,30, r, emp.getNet_value(),dash);

				
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
	    System.out.println("value of d is "+d); 
	    double roundval = Double.valueOf(twoDForm.format(d));
	    return ((int) (roundval+.50));
//	    return Double.valueOf(twoDForm.format((int) (d+.50)));
	}	
}
