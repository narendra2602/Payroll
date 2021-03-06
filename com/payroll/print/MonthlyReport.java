package com.payroll.print;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

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


public class MonthlyReport  extends WriteExcel
{    
   
   int r,i;
   SimpleDateFormat sdf;
   String inputFile;
    
   private String  drvnm,flname,cmp_name,monthname; 
   private int depo_code, cmp_code, fyear, emp_code,btnno,repno;
   private double totadv,totloan,sterlite_days,extra_hrs,absent_days;
   private double[] gtotal;
   ArrayList<?> esicList;
   SheetSettings settings; 
   
  public MonthlyReport(Integer depo_code,Integer cmp_code,Integer fyear,Integer emp_code,String cmp_name,String drvnm,Integer repno) 
  
  {
    try 
    {
    	this.r=0;
    	 
    	this.drvnm=drvnm;
	    this.cmp_name=cmp_name;
    	this.depo_code=depo_code;
    	this.cmp_code=cmp_code;
    	this.fyear=fyear;
    	this.repno=repno;
    	this.emp_code=emp_code;
    	gtotal= new double[12];
    	sdf = new SimpleDateFormat("dd/MM/yyyy");
    	
    	flname="Absent-"+cmp_name.substring(0, 6);
    	if(repno==2)
    		flname="OT-"+cmp_name.substring(0, 6);
    	else if(repno==3)
    		flname="Sterlite-"+cmp_name.substring(0, 6);
    	else if(repno==4)
    		flname="Present-"+cmp_name.substring(0, 6);
    	else if(repno==5)
    		flname="EmpBasic-"+cmp_name.substring(0, 6);

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
	    	esicList=pdao.getMonthlyAbsentReport(depo_code, cmp_code, fyear,emp_code,repno);
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
	   workbook.createSheet((flname.length()<15?flname:flname.substring(0, 15)), 0);
	   WritableSheet excelSheet = workbook.getSheet(0);
	   settings = excelSheet.getSettings();
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
	    

		switch(repno)
		{
			case 1:
					createHeader1(sheet); // Absent Report Monthly
					break;
			case 2:
					createHeader2(sheet); // OT Report Monthly
					break;
			case 3:
					createHeader3(sheet); // Sterlite Days Report Monthly
					break;
			case 4:
					createHeader4(sheet); // Present  Days Report Monthly
					break;
			case 5:
					createHeader5(sheet); // Present  Days Report Monthly
					break;
		}
	
	   

//	   settings.setHorizontalFreeze(3);
	   settings.setVerticalFreeze(4);
	   

}  


	public void createHeader1(WritableSheet sheet)
			   throws WriteException {
	 
		
		 	sheet.mergeCells(0, 0, 3, 0);
		   // Write a few headers
		    addCaption(sheet, 0, 0, cmp_name,40);
	
	
		 	sheet.mergeCells(0, 1, 3, 1);
		    addCaption1(sheet, 0, 1, " Absent Report for the period Apr-"+fyear+" to Mar-"+(fyear+1),40);
	
			   addCaption2(sheet, 0, 3, "Month",15);
			   addCaption2(sheet, 1, 3, "Employee Code",15);
			   addCaption2(sheet, 2, 3, "Employee  Name",40);
			   addCaption2(sheet, 3, 3, "Absent Days ",10);
			r=4;
	
	}  

	public void createHeader2(WritableSheet sheet)  throws WriteException {
	
		 
		 
				sheet.mergeCells(0, 0, 5, 0);
				   // Write a few headers
			   addCaption(sheet, 0, 0, cmp_name,40);
	
			 	sheet.mergeCells(0, 1, 5, 1);
				addCaption1(sheet, 0, 1, " OT Report Employee Wise period from  Apr-"+fyear+" to Mar-"+(fyear+1),40);
	
			   addCaption2(sheet, 0, 3, "Month",15);
			   addCaption2(sheet, 1, 3, "Employee Code",15);
			   addCaption2(sheet, 2, 3, "Employee  Name",40);
			   addCaption2(sheet, 3, 3, "OT Hrs ",10);
			   addCaption2(sheet, 4, 3, "OT Rate ",10);
			   addCaption2(sheet, 5, 3, "OT Amount ",10);
				r=4;
		    
	
	}  


	public void createHeader3(WritableSheet sheet)  throws WriteException {
	
		 
		   
		   
				sheet.mergeCells(0, 0, 5, 0);
				   // Write a few headers
			   addCaption(sheet, 0, 0, cmp_name,40);
			   
			 	sheet.mergeCells(0, 1, 5, 1);
				addCaption1(sheet, 0, 1, " Sterile Report Employee Wise period from  Apr-"+fyear+" to Mar-"+(fyear+1),40);
	
			   addCaption2(sheet, 0, 3, "Month",15);
			   addCaption2(sheet, 1, 3, "Employee Code",15);
			   addCaption2(sheet, 2, 3, "Employee  Name",40);
			   addCaption2(sheet, 3, 3, "Sterile Days ",10);
			   addCaption2(sheet, 4, 3, "Sterile Rate ",10);
			   addCaption2(sheet, 5, 3, "Sterile Amount ",10);
	
				r=4;
		    
	
	}  


	public void createHeader4(WritableSheet sheet)
			   throws WriteException {
	 
		
		 	sheet.mergeCells(0, 0, 3, 0);
		   // Write a few headers
		    addCaption(sheet, 0, 0, cmp_name,40);
	
	
		 	sheet.mergeCells(0, 1, 3, 1);
		    addCaption1(sheet, 0, 1, " Present Report for the period Apr-"+fyear+" to Mar-"+(fyear+1),40);
	
			   addCaption2(sheet, 0, 3, "Month",15);
			   addCaption2(sheet, 1, 3, "Employee Code",15);
			   addCaption2(sheet, 2, 3, "Employee  Name",40);
			   addCaption2(sheet, 3, 3, "Present Days ",10);
			r=4;
	
	}  

	public void createHeader5(WritableSheet sheet)
			   throws WriteException {
	 
		
		 	sheet.mergeCells(0, 0, 15, 0);
		   // Write a few headers
		    addCaption(sheet, 0, 0, cmp_name,40);
	
	
		 	sheet.mergeCells(0, 1, 15, 1);
		    addCaption1(sheet, 0, 1, " Employee Basic Detail Report for the period Apr-"+fyear+" to Mar-"+(fyear+1),40);
	
			   addCaption2(sheet, 0, 3, "Month",15);
			   addCaption2(sheet, 1, 3, "Employee Code",15);
			   addCaption2(sheet, 2, 3, "Employee  Name",40);
			   addCaption2(sheet, 3, 3, "ESIC No",20);
			   addCaption2(sheet, 4, 3, "PF No",20);
			   addCaption2(sheet, 5, 3, "Aadhar No",20);
			   addCaption2(sheet, 6, 3, "Basic",15);
			   addCaption2(sheet, 7, 3, "DA",15);
			   addCaption2(sheet, 8, 3, "HRA ",15);
			   addCaption2(sheet, 9, 3, "Add. HRA ",15);
			   addCaption2(sheet, 10, 3, "Incentive ",15);
			   addCaption2(sheet, 11, 3, "Sp. Incentive ",15);
			   addCaption2(sheet, 12, 3, "Food Allowance ",15);
			   addCaption2(sheet, 13, 3, "LTA ",15);
			   addCaption2(sheet, 14, 3, "Medical ",15);
			   addCaption2(sheet, 15, 3, "OT Rate ",15);
			   addCaption2(sheet, 16, 3, "Sterile Rate ",15);
			   addCaption2(sheet, 17, 3, "Total ",15);
			r=4;
	
	}  
	
	
	
	public void createContent(WritableSheet sheet) throws WriteException,RowsExceededException
	{
	
		boolean first=true;
	
		// detail header
		int size=esicList.size();
	
		EmptranDto emp =null;
		int pgbrk=0;
		totadv=0.00;
		totloan=0.00;
	
		
		int heightInPoints = 18*20;
		 
		for (i=0;i<size;i++)
		{
	
			emp = (EmptranDto) esicList.get(i);
			if(first)
			{
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
			}
			
			pgbrk++;
	
			/*if(pgbrk>55)
			{
				sheet.addRowPageBreak(r);
				pgbrk=0;
			}*/
	
		}
		
		  if(repno==2 || repno==3)
		  {
			  addLabel(sheet, 0, r, "",1);
			  addLabel(sheet, 1, r, "",1);
			  addLabel(sheet, 2, r, "Total",1);
			  if(repno==2)
				  addDouble(sheet, 3, r,extra_hrs,1);
			  else
				  addNumber(sheet, 3, r,(int) sterlite_days,1);
			  addLabel(sheet, 4, r, "",1);
			  addNumber(sheet, 5, r,(int) totadv,1);
		  }
		  else if(repno==1 || repno==4)
		  {
			  addLabel(sheet, 0, r, "",1);
			  addLabel(sheet, 1, r, "",1);
			  addLabel(sheet, 2, r, "Total",1);
			  addDouble(sheet, 3, r, absent_days,1);
		  }
		  else if(repno==5)
		  {
			  addLabel(sheet, 0, r, "",1);
			  addLabel(sheet, 1, r, "",1);
			  addLabel(sheet, 2, r, "",1);
			  addLabel(sheet, 3, r, "",1);
			  addLabel(sheet, 4, r, "",1);
			  addLabel(sheet, 5, r, "",1);
			  addLabel(sheet, 6, r, "Total",1);
			  for(int i=0;i<12;i++)
				  addDouble(sheet, i+6, r, gtotal[i],1);
		  }
		  
	}

	public void createReport1(WritableSheet sheet,EmptranDto emp) throws WriteException,RowsExceededException
	{
	
		
			    int dash=0;
			    addLabel(sheet, 0, r, emp.getMonname(),dash);
				addNumber(sheet, 1, r, emp.getEmp_code(),dash);
				addLabel(sheet, 2, r, emp.getEmp_name(),dash);
				addDouble(sheet, 3, r, emp.getAbsent_days(),dash);
				absent_days+=emp.getAbsent_days();
				r++;
			 
	
		 
	}

	
	public void createReport2(WritableSheet sheet,EmptranDto emp) throws WriteException,RowsExceededException
	{
	
		
			int dash=0;
			addLabel(sheet, 0, r, emp.getMonname(),dash);
			addNumber(sheet, 1, r, emp.getEmp_code(),dash);
			addLabel(sheet, 2, r, emp.getEmp_name(),dash);
			addNumber(sheet, 3, r, (int) emp.getExtra_hrs(),dash);
			addDouble(sheet, 4, r, emp.getOt_rate(),dash);
			addDouble(sheet, 5, r, emp.getOt_value(),dash);
			totadv+=emp.getOt_value();
			extra_hrs+=emp.getExtra_hrs();
			r++;
			 
	}
	
	/**
	 * Sterlite Days Report
	 * @param sheet
	 * @param emp
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	public void createReport3(WritableSheet sheet,EmptranDto emp) throws WriteException,RowsExceededException
	{
	
		
			int dash=0;
			addLabel(sheet, 0, r, emp.getMonname(),dash);
			addNumber(sheet, 1, r, emp.getEmp_code(),dash);
			addLabel(sheet, 2, r, emp.getEmp_name(),dash);
			addNumber(sheet, 3, r, emp.getStair_days(),dash);
			addDouble(sheet, 4, r, emp.getStair_alw(),dash);
			addNumber(sheet, 5, r, (int) (emp.getStair_value()+.50),dash);
			totadv+=(int) (emp.getStair_value()+.50);
			sterlite_days+=emp.getStair_days();
			r++;
			 
		 
	}
	
	 
	public void createReport4(WritableSheet sheet,EmptranDto emp) throws WriteException,RowsExceededException
	{
	
		
			    int dash=0;
			    addLabel(sheet, 0, r, emp.getMonname(),dash);
				addNumber(sheet, 1, r, emp.getEmp_code(),dash);
				addLabel(sheet, 2, r, emp.getEmp_name(),dash);
				addDouble(sheet, 3, r, emp.getAtten_days(),dash);
				absent_days+=emp.getAtten_days();
				r++;
			 
	
		 
	}
	
	public void createReport5(WritableSheet sheet,EmptranDto emp) throws WriteException,RowsExceededException
	{
	
		
			    int dash=0;
			    totadv=0.00;
			    totadv=emp.getBasic()+emp.getDa()+emp.getHra()+emp.getAdd_hra()+emp.getIncentive()+emp.getSpl_incentive()+emp.getFood_value();
			    addLabel(sheet, 0, r, emp.getMonname(),dash);
				addNumber(sheet, 1, r, emp.getEmp_code(),dash);
				addLabel(sheet, 2, r, emp.getEmp_name(),dash);
				addLong(sheet, 3, r, emp.getEsic_no(),dash);
				addNumber(sheet, 4, r, emp.getPf_no(),dash);
				addLabel(sheet, 5, r, String.valueOf(emp.getAdhar_no()),dash);
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
				addDouble(sheet, 17, r, totadv,dash);

				gtotal[0]+=emp.getBasic();	
				gtotal[1]+=emp.getDa();
				gtotal[2]+=emp.getHra();
				gtotal[3]+=emp.getAdd_hra();
				gtotal[4]+=emp.getIncentive();
				gtotal[5]+=emp.getSpl_incentive();
				gtotal[6]+=emp.getLta();
				gtotal[7]+=emp.getMedical();
				gtotal[8]+=emp.getOt_rate();
				gtotal[9]+=emp.getStair_alw();
				gtotal[10]+=emp.getFood_value();
				gtotal[11]+=totadv;
				r++;
			 
	
		 
	}
	 
}
