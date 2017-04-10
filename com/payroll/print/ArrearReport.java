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


public class ArrearReport  extends WriteExcel
{    
   
   int r,i;
   SimpleDateFormat sdf;
   String inputFile;
    
   private String  drvnm,flname,cmp_name,monthname; 
   private int depo_code, cmp_code, fyear, mnth_code,btnno,repno;
   private double tot,totbonus;
   private double[] gtot,gtot1;
   ArrayList<?> esicList;
   SheetSettings settings; 
   
  public ArrearReport(Integer depo_code,Integer cmp_code,Integer fyear,String cmp_name,String drvnm,Integer repno) 
  
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
    	
    	flname="Arrear-"+cmp_name.substring(0, 6);
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
	    	esicList=pdao.getArrearReport(depo_code, cmp_code, fyear,(repno==1?"Y":"N"));
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
	   settings.setOrientation(PageOrientation.LANDSCAPE);
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
	    
			createHeader1(sheet); // Absent Report
		 
	
	   

	   settings.setHorizontalFreeze(3);
	   settings.setVerticalFreeze(5);
	   

}  


public void createHeader1(WritableSheet sheet)
		   throws WriteException {
 
	
	 	sheet.mergeCells(0, 0, 17, 0);
	   // Write a few headers
	   addCaption(sheet, 0, 0, cmp_name,40);


	 	sheet.mergeCells(0, 1, 17, 1);
	 	addCaption1(sheet, 0, 1, (repno==1?"[Paid]":"[Pending]")+" Arrear Payment Statement for the year "+fyear+"-"+(fyear+1),40);

		   addCaption2(sheet, 0, 3, "",10);
		   addCaption2(sheet, 1, 3, "",30);
		   addCaption2(sheet, 2, 3, " ",10);
		   addCaption2(sheet, 3, 3, "",10);
		   addCaption2(sheet, 4, 3, "",10);
		   addCaption2(sheet, 5, 3, "",10);
		   addCaption2(sheet, 6, 3, "",10);
		   sheet.mergeCells(7, 3, 13, 3);
		   addCaption2(sheet, 7, 3, "Arrears Earnings",10);
		   sheet.mergeCells(14, 3, 16, 3);
		   addCaption2(sheet, 14, 3, "Arrears Deduction",10);
		   addCaption2(sheet, 17, 3, "",10);
		   
		   
		   addCaption2(sheet, 0, 4, "Code",10);
		   addCaption2(sheet, 1, 4, "Name",30);
		   addCaption2(sheet, 2, 4, "Month ",10);
		   addCaption2(sheet, 3, 4, "Days",10);
		   addCaption2(sheet, 4, 4, "Arrear Days",10);
		   addCaption2(sheet, 5, 4, "Old Wages",10);
		   addCaption2(sheet, 6, 4, "New Wages",10);
		   addCaption2(sheet, 7, 4, "Basic",10);
		   addCaption2(sheet, 8, 4, "Da",10);
		   addCaption2(sheet, 9, 4, "Hra ",10);
		   addCaption2(sheet, 10, 4, "Add Hra ",10);
		   addCaption2(sheet, 11, 4, "Incentive ",10);
		   addCaption2(sheet, 12, 4, "Spl. Incentive ",10);
		   addCaption2(sheet, 13, 4, "Total Earning",10);
		   addCaption2(sheet, 14, 4, "PF",10);
		   addCaption2(sheet, 15, 4, "Esic",10);
		   addCaption2(sheet, 16, 4, "Total Deduction",10);
		   addCaption2(sheet, 17, 4, "Payable Amount ",10);
		   r=5;

}  



	
	
	public void createContent(WritableSheet sheet) throws WriteException,RowsExceededException
	{
	
		boolean first=true;
	
		// detail header
		int size=esicList.size();
	
		EmptranDto emp =null;
		int pgbrk=0;
		tot=0.00;
	
		
		int heightInPoints = 18*20;
		gtot= new double[12];
		gtot1= new double[12];
		
		for (i=0;i<size;i++)
		{
	
			emp = (EmptranDto) esicList.get(i);
			if(first)
			{
				createHeader(sheet);
				first=false;
			}
	
		    sheet.setRowView(r, heightInPoints);
		    createReport1(sheet, emp);
			 
			
			pgbrk++;
		}
		
		
 	  
	}

	public void createReport1(WritableSheet sheet,EmptranDto emp) throws WriteException,RowsExceededException
	{
	
		
			    int dash=0;
			    if(emp.getEmp_code()<0)
			    {
			    	dash=1;
			    }
			    tot=0.00;
			    
			    if(emp.getEmp_code()>0)
			    	addNumber(sheet, 0, r, emp.getEmp_code(),dash);
			    else
					addLabel(sheet, 0, r, "",dash);
				addLabel(sheet, 1, r, emp.getEmp_name(),dash);
				addLabel(sheet, 2, r, emp.getMonname(),dash);
				addDouble(sheet, 3, r, emp.getAtten_days(),dash);
				addDouble(sheet, 4, r, emp.getArrear_days(),dash);
				addDouble(sheet, 5, r, emp.getOldwages(),dash);
				addDouble(sheet, 6, r, emp.getNewwages(),dash);
				addDouble(sheet, 7, r, emp.getBasic_value(),dash);
				addDouble(sheet, 8, r, emp.getDa_value(),dash);
				addDouble(sheet, 9, r, emp.getHra_value(),dash);
				addDouble(sheet, 10, r, emp.getAdd_hra_value(),dash);
				addDouble(sheet, 11, r, emp.getIncentive_value(),dash);
				addDouble(sheet, 12, r, emp.getSpl_incen_value(),dash);
				addDouble(sheet, 13, r, emp.getTds_value(),dash); // totearn
				addDouble(sheet, 14, r, emp.getPf_value(),dash);
				addDouble(sheet, 15, r, emp.getEsis_value(),dash);
				addDouble(sheet, 16, r, emp.getMisc_value(),dash); // totded
				addDouble(sheet, 17, r, emp.getNet_value(),dash); // payable amt
				r++;
	 
		 
	}

	
	
}
