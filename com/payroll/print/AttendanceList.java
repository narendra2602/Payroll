package com.payroll.print;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import jxl.SheetSettings;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.payroll.dto.EmptranDto;
import com.payroll.excel.WriteExcel;


public class AttendanceList  extends WriteExcel
{    
   int r,i ;
   String inputFile;
   private String  drvnm,flname,cmp_name,monthname; 
   ArrayList<?> attnList;
   SheetSettings settings;
   
  public AttendanceList(String cmp_name,String drvnm,ArrayList<?> attnList,String monthname) 
  
  {
    try 
    {
    	this.r=0;
    	 
    	this.drvnm=drvnm;
	    this.cmp_name=cmp_name;
		this.attnList=attnList; 
    	this.monthname=monthname;
    	flname="AttnList-"+cmp_name.substring(0, 6)+"-"+monthname;
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
	  
	    try {
	    	createExcel();

	    } catch (Exception ex) {
	      ex.printStackTrace();
	    }
	  }



   
//////excel file generation report ////////   
   
public void createExcel() throws WriteException, IOException {

	    System.out.println(drvnm+"\\"+flname+".xls");
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
	   workbook.createSheet(flname, 0);
	   WritableSheet excelSheet = workbook.getSheet(0);
	   settings = excelSheet.getSettings();
	   createLabel(excelSheet);
	   createContent(excelSheet);

	   workbook.write();
	   workbook.close();
}


public void createHeader(WritableSheet sheet)
		   throws WriteException {
	    
	   sheet.mergeCells(0, 0, 8, 0);
	   // Write a few headers
	   addCaption(sheet, 0, 0, cmp_name+" Attendance Report "+monthname,40);
	   
	   sheet.mergeCells(0, 1, 8, 1);
	   addCaption1(sheet, 0, 1, "To: Accounts Deptt., Aristo Pharmaceuticals Pvt Ltd., Mandideep",30);

	   addCaption2(sheet, 0, 3, "SNo",6);
	   addCaption2(sheet, 1, 3, "Employee Name",30);
	   addCaption2(sheet, 2, 3, "Employee Code",10);
	   addCaption2(sheet, 3, 3, "Attendance",10);
	   addCaption2(sheet, 4, 3, "Extra Hrs",10);
	   addCaption2(sheet, 5, 3, "Arrears",10);
	   addCaption2(sheet, 6, 3, "Remark",40);

//	   addCaption2(sheet, 2, 3, "ESIC No",12);
//	   addCaption2(sheet, 3, 3, "PF No",10);






//	   settings.setHorizontalFreeze(3);
	   settings.setVerticalFreeze(4);
	   

}  


public void createContent(WritableSheet sheet) throws WriteException,RowsExceededException
{

	   boolean first=true;
	    
	   r=5;
	   int dash=0;
	   // detail header
	   int size=attnList.size();
	    
	   EmptranDto emp =null;
	    
	   for (i=0;i<size;i++)
	   {
		    
		   emp = (EmptranDto) attnList.get(i);
		   if(first)
		   {
			   createHeader(sheet);
			   first=false;
		   }

			   dash=0;
			   addNumber(sheet, 0, r, emp.getSerialno(),dash);
			   addLabel(sheet, 1, r, emp.getEmp_name(),dash);
			   addNumber(sheet, 2, r, emp.getEmp_code(),dash);
			   addDouble(sheet, 3, r, emp.getAtten_days(),dash);
			   addDouble(sheet, 4, r, emp.getExtra_hrs(),dash);
			   addDouble(sheet, 5, r, emp.getArrear_days(),dash);
			   addLabel(sheet, 6, r, emp.getRemark(),dash);

//			   addNumber(sheet, 2, r, emp.getEsic_no(),dash);
//			   addNumber(sheet, 3, r, emp.getPf_no(),dash);

			   r++;

	   }
}
}
