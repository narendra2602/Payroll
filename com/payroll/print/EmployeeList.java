package com.payroll.print;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import jxl.SheetSettings;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.payroll.dto.EmployeeMastDto;
import com.payroll.excel.WriteExcel;


public class EmployeeList  extends WriteExcel
{    
   int r,i ;
   String inputFile;
   private String  drvnm,flname,cmp_name; 
   Vector<?> empList;
   SheetSettings settings;
   int repno;
   SimpleDateFormat sdf;
  public EmployeeList(String cmp_name,String drvnm,Vector<?> empList,int repno) 
  
  {
    try 
    {
    	this.r=0;
    	this.repno=repno; 
    	this.drvnm=drvnm;
	    this.cmp_name=cmp_name;
		this.empList=empList; 
    	flname="EmpList-"+cmp_name.substring(0, 6);
    	sdf= new SimpleDateFormat("dd/MM/yyyy");
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
	   addCaption(sheet, 0, 0, cmp_name,40);
	   
	   sheet.mergeCells(0, 1, 8, 1);
	   // Write a few headers
	   addCaption(sheet, 0, 1, "Employee List ",40);

	   if(repno==1 || repno==3)
	   {
		   addCaption2(sheet, 0, 3, "Emp Code",6);
		   addCaption2(sheet, 1, 3, "Employee Name",30);
		   addCaption2(sheet, 2, 3, "Father's Name",30);
		   addCaption2(sheet, 3, 3, "Address1",30);
		   addCaption2(sheet, 4, 3, "Address2",30);
		   addCaption2(sheet, 5, 3, "Address3",30);
		   addCaption2(sheet, 6, 3, "City",15);
		   addCaption2(sheet, 7, 3, "State",15);
		   addCaption2(sheet, 8, 3, "Pin",15);
		   addCaption2(sheet, 9, 3, "Phone No",15);
		   addCaption2(sheet, 10, 3, "Mobile",15);
		   addCaption2(sheet, 11, 3, "Email",30);
		   addCaption2(sheet, 12, 3, "Pan No",15);
		   addCaption2(sheet, 13, 3, "DOB",15);
		   addCaption2(sheet, 14, 3, "DOJ",15);
		   addCaption2(sheet, 15, 3, "Resign Date",15);
		   addCaption2(sheet, 16, 3, "ESIC No",10);
		   addCaption2(sheet, 17, 3, "PF No",10);
		   addCaption2(sheet, 18, 3, "Basic",15);
		   addCaption2(sheet, 19, 3, "DA",15);
		   addCaption2(sheet, 20, 3, "HRA",15);
		   addCaption2(sheet, 21, 3, "Add HRA",15);
		   addCaption2(sheet, 22, 3, "Incentive",15);
		   addCaption2(sheet, 23, 3, "Sp. Incentive",15);
		   addCaption2(sheet, 24, 3, "Gross",15);
		   addCaption2(sheet, 25, 3, "LTA",15);
		   addCaption2(sheet, 26, 3, "Medical",15);
		   addCaption2(sheet, 27, 3, "Bonus",15);
		   addCaption2(sheet, 28, 3, "OT Rate",15);
		   addCaption2(sheet, 29, 3, "Sterile Rate",15);

		   addCaption2(sheet, 30, 3, "A/c No",15);
		   addCaption2(sheet, 31, 3, "Bank Name",30);
		   addCaption2(sheet, 32, 3, "Address",20);
		   addCaption2(sheet, 33, 3, "IFSC Code",15);
		   addCaption2(sheet, 34, 3, "UAN No",12);
		   addCaption2(sheet, 35, 3, "Bonus %",12);
		   addCaption2(sheet, 36, 3, "Bonus Limit",12);
		   addCaption2(sheet, 37, 3, "Bonus Check",12);

	   }
	   else
	   {
		   addCaption2(sheet, 0, 3, "Emp Code",6);
		   addCaption2(sheet, 1, 3, "Employee Name",30);
		   addCaption2(sheet, 2, 3, "A/c No",15);
		   addCaption2(sheet, 3, 3, "Bank Name",30);
		   addCaption2(sheet, 4, 3, "Address",20);
		   addCaption2(sheet, 5, 3, "IFSC Code",15);
		   
	   }
	   settings.setHorizontalFreeze(3);
	   settings.setVerticalFreeze(4);
	   

}  


public void createContent(WritableSheet sheet) throws WriteException,RowsExceededException
{

	   boolean first=true;
	    
	   r=4;
	   int dash=0;
	   // detail header
	   int size=empList.size();
	    
	   EmployeeMastDto emp =null;
	   Vector<?> col=null;
	   Date curdate = new Date();
	   for (i=0;i<size;i++)
	   {
		   col = (Vector<?>) empList.get(i); 
		   emp = (EmployeeMastDto) col.get(2);
		   if(first)
		   {
			   createHeader(sheet);
			   first=false;
		   }

			   dash=0;
			   if(repno==3 && emp.getDoresign()!=null)
			   {
				   addNumber(sheet, 0, r, emp.getEmp_code(),dash);
				   addLabel(sheet, 1, r, emp.getEmp_name(),dash);
				   addLabel(sheet, 2, r, emp.getFather_name(),dash);
				   addLabel(sheet, 3, r, emp.getMadd1(),dash);
				   addLabel(sheet, 4, r, emp.getMadd2(),dash);
				   addLabel(sheet, 5, r, emp.getMadd3(),dash);
				   addLabel(sheet, 6, r, emp.getMcity(),dash);
				   addLabel(sheet, 7, r, emp.getMstate(),dash);
				   addLabel(sheet, 8, r, emp.getMpin(),dash);
				   addLabel(sheet, 9, r, emp.getMphone(),dash);
				   addLabel(sheet, 10, r, emp.getMobile(),dash);
				   addLabel(sheet, 11, r, emp.getMemail(),dash);
				   addLabel(sheet, 12, r, emp.getPan_no(),dash);
				   addLabel(sheet, 13, r, ""+emp.getDobirth(),dash);
				   addLabel(sheet, 14, r, ""+emp.getDojoin(),dash);
				   addLabel(sheet, 15, r, ""+emp.getDoresign(),dash);
				   addLabel(sheet,16, r, ""+emp.getEsic_no(),dash);
				   addNumber(sheet,17, r, emp.getPf_no(),dash);
				   addDouble(sheet,18, r, emp.getBasic(),dash);
				   addDouble(sheet,19, r, emp.getDa(),dash);
				   addDouble(sheet,20, r, emp.getHra(),dash);
				   addDouble(sheet,21, r, emp.getAdd_hra(),dash);
				   addDouble(sheet,22, r, emp.getIncentive(),dash);
				   addDouble(sheet,23, r, emp.getSpl_incentive(),dash);
				   addDouble(sheet,24, r, emp.getGross(),dash);
				   addDouble(sheet,25, r, emp.getLta(),dash);
				   addDouble(sheet,26, r, emp.getMedical(),dash);
				   addDouble(sheet,27, r, emp.getBonus(),dash);
				   addDouble(sheet,28, r, emp.getOt_rate(),dash);
				   addDouble(sheet,29, r, emp.getStair_alw(),dash);
				   addLabel(sheet, 30, r, emp.getBank_accno(),dash);
				   addLabel(sheet, 31, r, emp.getBank(),dash);
				   addLabel(sheet, 32, r, emp.getBank_add1(),dash);
				   addLabel(sheet, 33, r, emp.getIfsc_code(),dash);
				   addLabel(sheet,34, r, ""+emp.getUan_no(),dash);
				   addDouble(sheet,35, r, emp.getBonus_per(),dash);
				   addDouble(sheet,36, r, emp.getBonus_limit(),dash);
				   addDouble(sheet,37, r, emp.getBonus_check(),dash);

				   r++;
			   }
			   else if(emp.getDoresign()==null || emp.getDoresign().after(curdate))
			   {
				   if(repno==1)
				   {
					   addNumber(sheet, 0, r, emp.getEmp_code(),dash);
					   addLabel(sheet, 1, r, emp.getEmp_name(),dash);
					   addLabel(sheet, 2, r, emp.getFather_name(),dash);
					   addLabel(sheet, 3, r, emp.getMadd1(),dash);
					   addLabel(sheet, 4, r, emp.getMadd2(),dash);
					   addLabel(sheet, 5, r, emp.getMadd3(),dash);
					   addLabel(sheet, 6, r, emp.getMcity(),dash);
					   addLabel(sheet, 7, r, emp.getMstate(),dash);
					   addLabel(sheet, 8, r, emp.getMpin(),dash);
					   addLabel(sheet, 9, r, emp.getMphone(),dash);
					   addLabel(sheet, 10, r, emp.getMobile(),dash);
					   addLabel(sheet, 11, r, emp.getMemail(),dash);
					   addLabel(sheet, 12, r, emp.getPan_no(),dash);
					   addLabel(sheet, 13, r, ""+emp.getDobirth(),dash);
					   addLabel(sheet, 14, r, ""+emp.getDojoin(),dash);
					   addLabel(sheet, 15, r, ""+emp.getDoresign(),dash);
					   addLabel(sheet,16, r, ""+emp.getEsic_no(),dash);
					   addNumber(sheet,17, r, emp.getPf_no(),dash);
					   addDouble(sheet,18, r, emp.getBasic(),dash);
					   addDouble(sheet,19, r, emp.getDa(),dash);
					   addDouble(sheet,20, r, emp.getHra(),dash);
					   addDouble(sheet,21, r, emp.getAdd_hra(),dash);
					   addDouble(sheet,22, r, emp.getIncentive(),dash);
					   addDouble(sheet,23, r, emp.getSpl_incentive(),dash);
					   addDouble(sheet,24, r, emp.getGross(),dash);
					   addDouble(sheet,25, r, emp.getLta(),dash);
					   addDouble(sheet,26, r, emp.getMedical(),dash);
					   addDouble(sheet,27, r, emp.getBonus(),dash);
					   addDouble(sheet,28, r, emp.getOt_rate(),dash);
					   addDouble(sheet,29, r, emp.getStair_alw(),dash);
					   addLabel(sheet, 30, r, emp.getBank_accno(),dash);
					   addLabel(sheet, 31, r, emp.getBank(),dash);
					   addLabel(sheet, 32, r, emp.getBank_add1(),dash);
					   addLabel(sheet, 33, r, emp.getIfsc_code(),dash);
					   addLabel(sheet,34, r, ""+emp.getUan_no(),dash);
					   addDouble(sheet,35, r, emp.getBonus_per(),dash);
					   addDouble(sheet,36, r, emp.getBonus_limit(),dash);
					   addDouble(sheet,37, r, emp.getBonus_check(),dash);


					   r++;
				   }
				   else if(repno==2)
				   {
					   addNumber(sheet, 0, r, emp.getEmp_code(),dash);
					   addLabel(sheet, 1, r, emp.getEmp_name(),dash);
					   addLabel(sheet, 2, r, emp.getBank_accno(),dash);
					   addLabel(sheet, 3, r, emp.getBank(),dash);
					   addLabel(sheet, 4, r, emp.getBank_add1(),dash);
					   addLabel(sheet, 5, r, emp.getIfsc_code(),dash);
					   r++;
				   }
				  
			   }

	   }
}
}
