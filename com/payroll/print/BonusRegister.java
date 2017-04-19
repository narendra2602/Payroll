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


public class BonusRegister  extends WriteExcel
{    
   
   int r,i;
   SimpleDateFormat sdf;
   String inputFile;
    
   private String  drvnm,flname,cmp_name,monthname; 
   private int depo_code, cmp_code, fyear, mnth_code,btnno,repno,sno;
   private double tot,totbonus,totatten;
   private double[] gtot,gtot1,gtot2;
   ArrayList<?> esicList;
   SheetSettings settings; 
   
  public BonusRegister(Integer depo_code,Integer cmp_code,Integer fyear,String cmp_name,String drvnm,Integer repno) 
  
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
    	sdf = new SimpleDateFormat("dd/MM/yyyy");
    	
    	flname="Bonus-"+cmp_name.substring(0, 6);
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
	    	esicList=pdao.getBonusRegister(depo_code, cmp_code, fyear);
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
	   settings.setPrintTitlesRow(0, repno==5?3:6);
	   settings.setOrientation(PageOrientation.LANDSCAPE);
	   settings.setLeftMargin(0);
	   settings.setRightMargin(0);
	   settings.setFitWidth(1);

	   

	   
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
	    
			createHeader1(sheet); // Absent Report
		 
	
	   

//	   settings.setHorizontalFreeze(3);
	   if(repno==5)
		   settings.setVerticalFreeze(4);
	   else
		   settings.setVerticalFreeze(7);
	   

}  


public void createHeader1(WritableSheet sheet)
		   throws WriteException {
 
	


	 	
	 	if(repno==5)
	 	{
		 	sheet.mergeCells(0, 0, 16, 0);
			   // Write a few headers
			   addCaption(sheet, 0, 0, cmp_name,40);
		 	sheet.mergeCells(0, 1, 16, 1);
	 		addCaption1(sheet, 0, 1, " Bonus Register for the year "+fyear+"-"+(fyear+1),40);

	 		addCaption2(sheet, 0, 3, "Sno",10);
	 		addCaption2(sheet, 1, 3, "Employee Code",10);
	 		addCaption2(sheet, 2, 3, "Employee  Name",30);
	 		addCaption2(sheet, 3, 3, "April ",10);
	 		addCaption2(sheet, 4, 3, "May",10);
	 		addCaption2(sheet, 5, 3, "June",10);
	 		addCaption2(sheet, 6, 3, "July",10);
	 		addCaption2(sheet, 7, 3, "August ",10);
	 		addCaption2(sheet, 8, 3, "September ",10);
	 		addCaption2(sheet, 9, 3, "October ",10);
	 		addCaption2(sheet, 10, 3, "November ",10);
	 		addCaption2(sheet, 11, 3, "December",10);
	 		addCaption2(sheet, 12, 3, "January",10);
	 		addCaption2(sheet, 13, 3, "February",10);
	 		addCaption2(sheet, 14, 3, "March ",10);
	 		addCaption2(sheet, 15, 3, "Total Payment ",10);
	 		addCaption2(sheet, 16, 3, "Bonus Amount ",10);


	 		r=4;
	 	}
	 	else if(repno==51)
	 	{
		 	sheet.mergeCells(0, 0, 16, 0);
			addCaption(sheet, 0, 0, "FORM C",40);
			
		 	sheet.mergeCells(0, 1, 16, 1);
			addCaption(sheet, 0, 1, "[See RULE 4(C)]",40);

			sheet.mergeCells(0, 2, 16, 2);
	 		addCaption1(sheet, 0, 2, " BONUS PAID TO EMPLOYEES FOR THE ACCOUNTING YEAR ENDING ON THE MAR-"+fyear,40);

	 		sheet.mergeCells(0, 3, 3, 3);
	 		addCaption3(sheet, 0, 3, "Name of the establishment :  "+cmp_name,10,0);
			sheet.mergeCells(0, 4, 3, 4);
	 		addCaption3(sheet, 0, 4, "No of Working days in the year ",10,0);
	 		
	 		
	 		
	 		
	 		addCaption2(sheet, 0, 5, "Sl No",10);
	 		addCaption2(sheet, 1, 5, "Name of the Employee",30);
	 		addCaption2(sheet, 2, 5, "Father's Name",20);
	 		addCaption2(sheet, 3, 5, "Whether he has completed 15 years of age at the begining of the accounting year ",10);
	 		addCaption2(sheet, 4, 5, "Designation",12);
	 		addCaption2(sheet, 5, 5, "No. of days worked in the year",10);
	 		addCaption2(sheet, 6, 5, "Total Salary or wage in respect of the accounting year",10);
	 		addCaption2(sheet, 7, 5, "Amount of bonus payable under section 10 or 11 as the case may be ",10);
	 		addCaption2(sheet, 8, 5, "Pooja bonus other custom mary during the accounting year ",10);
	 		addCaption2(sheet, 9, 5, "Interim bonus or bonus paid advance ",10);
	 		addCaption2(sheet, 10,5, "Amount of income e-tax deducted ",10);
	 		addCaption2(sheet, 11,5, "Deduction on account of financial loss, if any, cause by misconduct of the employee",10);
	 		addCaption2(sheet, 12,5, "Total sum deducted under column 9,10,10A and 11",10);
	 		addCaption2(sheet, 13,5, "Net Amount payable (Columns 8 minus 12)",10);
	 		addCaption2(sheet, 14,5, "Amount actually paid",10);
	 		addCaption2(sheet, 15,5, "Date on which paid ",10);
	 		addCaption2(sheet, 16,5, "Signature/Thumb impression of the employee ",10);

	 		addCaption2(sheet, 0, 6, "1",10);
	 		addCaption2(sheet, 1, 6, "2",30);
	 		addCaption2(sheet, 2, 6, "3",20);
	 		addCaption2(sheet, 3, 6, "4",10);
	 		addCaption2(sheet, 4, 6, "5",12);
	 		addCaption2(sheet, 5, 6, "6",10);
	 		addCaption2(sheet, 6, 6, "7",10);
	 		addCaption2(sheet, 7, 6, "8",10);
	 		addCaption2(sheet, 8, 6, "9",10);
	 		addCaption2(sheet, 9, 6, "10",10);
	 		addCaption2(sheet, 10,6, "10A",10);
	 		addCaption2(sheet, 11,6, "11",10);
	 		addCaption2(sheet, 12,6, "12",10);
	 		addCaption2(sheet, 13,6, "13",10);
	 		addCaption2(sheet, 14,6, "14",10);
	 		addCaption2(sheet, 15,6, "15",10);
	 		addCaption2(sheet, 16,6, " ",9);

	 		r=7;
	 	}

}  



	
	
	public void createContent(WritableSheet sheet) throws WriteException,RowsExceededException
	{
	
		boolean first=true;
		boolean print=false;
		// detail header
		int size=esicList.size();
	
		EmptranDto emp =null;
		int pgbrk=0;
		tot=0.00;
	
		
		int heightInPoints = 18*20;
		gtot= new double[12];
		gtot1= new double[12];
		gtot2= new double[12];
		
		for (i=0;i<size;i++)
		{
	
			emp = (EmptranDto) esicList.get(i);
			if(first)
			{
				createHeader(sheet);
				first=false;
			}
	
			tot=0.00;
			print=false;
			for (int i=0;i<12;i++)
			{
				tot+=emp.getAtten()[i];
			}

			if(tot>=31)
				print=true;
		    sheet.setRowView(r, heightInPoints);
		    	if(repno==5)
		    	{
				    	createReport1(sheet, emp);
		    	}
		    	else
		    	{
				    if(print)
				    {
				    	createReport2(sheet, emp);
				    }
		    	}
			pgbrk++;
		}

		if(repno==5) //total for bonus register
		{
			int k=3;
			tot=0.00;
			addLabel(sheet, 0, r, "",1);
			addLabel(sheet, 1, r, "",1);
			addLabel(sheet, 2, r, "Total",1);
			for (int i=0;i<12;i++)
			{
				addDouble(sheet, k++, r,  gtot[i],1);
				tot+=gtot[i];
			}
			addDouble(sheet, k++, r,  tot,1);
			addDouble(sheet, k++, r,  totbonus,1);
			r++;

			k=3;
			tot=0.00;
			addLabel(sheet, 0, r, "",1);
			addLabel(sheet, 1, r, "",1);
			addLabel(sheet, 2, r, "",1);
			for (int i=0;i<12;i++)
			{
				addDouble(sheet, k++, r,  gtot1[i],1);
				tot+=gtot1[i];
			}
			addDouble(sheet, k++, r,  tot,1);
			addLabel(sheet, k++, r, "",1);
			r++;
			
			k=3;
			tot=0.00;
			addLabel(sheet, 0, r, "",1);
			addLabel(sheet, 1, r, "",1);
			addLabel(sheet, 2, r, "",1);
			for (int i=0;i<12;i++)
			{
				addDouble(sheet, k++, r,  gtot2[i],1);
				tot+=gtot2[i];
			}
			addDouble(sheet, k++, r,  tot,1);
			addLabel(sheet, k++, r, "",1);
		}
			 
	
		
		
 	  
	}

	public void createReport1(WritableSheet sheet,EmptranDto emp) throws WriteException,RowsExceededException
	{
	
		
			    int dash=0;
			    int k=3;
			    tot=0.00;
				addNumber(sheet, 0, r, emp.getSerialno(),dash);
				addNumber(sheet, 1, r, emp.getEmp_code(),dash);
				addLabel(sheet, 2, r, emp.getEmp_name(),dash);
				
				for (int i=0;i<12;i++)
				{
					addDouble(sheet, k++, r, emp.getBonusval()[i],dash);
					
					tot+=emp.getBonusval()[i];
					gtot[i]+=emp.getBonusval()[i];;
				}
					addDouble(sheet, k++, r,  tot,dash);
					addDouble(sheet, k++, r,  emp.getBonus_value(),dash);
					totbonus+=emp.getBonus_value();
				r++;
			 
				k=3;
				tot=0.00;
				for (int i=0;i<12;i++)
				{

					addDouble(sheet, k++, r, emp.getAtten()[i],dash);
					tot+=emp.getAtten()[i];
					gtot1[i]+=emp.getAtten()[i];;
				}
				addDouble(sheet, k++, r,  tot,dash);
				addLabel(sheet, k++, r, "",dash);
				r++;

				k=3;
				tot=0.00;
				for (int i=0;i<12;i++)
				{

					addDouble(sheet, k++, r, emp.getArrdays()[i],dash);
					tot+=emp.getArrdays()[i];
					gtot2[i]+=emp.getArrdays()[i];;
				}
				addDouble(sheet, k++, r,  tot,dash);
				addLabel(sheet, k++, r, "",dash);
				r++;
		 
	}

	public void createReport2(WritableSheet sheet,EmptranDto emp) throws WriteException,RowsExceededException
	{
	
		
			    int dash=0;
			    int k=5;
			    tot=0.00;
			    totatten=0;
//				addNumber(sheet, 0, r, emp.getSerialno(),dash);
				addNumber(sheet, 0, r, ++sno,dash);
				addLabel(sheet, 1, r, emp.getEmp_name(),dash);
				addLabel(sheet, 2, r, "",dash);
				addLabel(sheet, 3, r, "",dash);
				addLabel(sheet, 4, r, "",dash);
				
				for (int i=0;i<12;i++)
				{
					tot+=emp.getBonusval()[i];
					totatten+=emp.getAtten()[i];
					gtot[i]+=emp.getBonusval()[i];;
				}
					addDouble(sheet, k++, r,  totatten,dash);
					addDouble(sheet, k++, r,  tot,dash);
					totbonus+=emp.getBonus_value();
			 
					addLabel(sheet, k++, r, "",dash);
					addLabel(sheet, k++, r, "",dash);
					addLabel(sheet, k++, r, "",dash);
					addLabel(sheet, k++, r, "",dash);
					addLabel(sheet, k++, r, "",dash);
					addLabel(sheet, k++, r, "",dash);
					addLabel(sheet, k++, r, "",dash);
					addDouble(sheet, k++, r,  emp.getBonus_value(),dash);
					addLabel(sheet, k++, r, "",dash);
					addLabel(sheet, k++, r, "",dash);
					r++;
	
		 
	}
	
	
}
