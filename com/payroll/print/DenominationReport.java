package com.payroll.print;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
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


public class DenominationReport  extends WriteExcel
{    
   
   int r,i;
   SimpleDateFormat sdf;
   String inputFile;
    
   private String  drvnm,flname,cmp_name,monthname; 
   private int depo_code, cmp_code, fyear, mnth_code,btnno,repno;
   private double totadv,totloan;
   ArrayList<?> esicList;
   SheetSettings settings; 
   int twothsnd,thsnd,fhrd,hrd,fty,twty,ten,five,two,one;
   int gtwothsnd,gthsnd,gfhrd,ghrd,gfty,gtwty,gten,gfive,gtwo,gone;
   double tot,totnet;
   HashMap checkList;
   
  public DenominationReport(Integer depo_code,Integer cmp_code,Integer fyear,Integer mnth_code,String cmp_name,String drvnm,String monthname,Integer btnno,HashMap checkList) 
  
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
    	this.checkList=checkList;
    	
    	sdf = new SimpleDateFormat("dd/MM/yyyy");
    	
    	flname="DenominationReport-"+cmp_name.substring(0, 6)+"-"+monthname;
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
	    	esicList=pdao.getSalaryRegister(depo_code, cmp_code, fyear, mnth_code,0);
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
	   workbook.createSheet("Denomination", 0);
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
	    
		createHeader1(sheet); // ESIC List
	 
//	   settings.setHorizontalFreeze(3);
	   settings.setVerticalFreeze(5);
	   

}  


public void createHeader1(WritableSheet sheet)
		   throws WriteException {
 
	
	 	sheet.mergeCells(0, 0, 13, 0);
	   // Write a few headers
	   addCaption(sheet, 0, 0, cmp_name,40);
	 	sheet.mergeCells(0, 1, 13, 1);
	   addCaption(sheet, 0, 1, " Denomination Calculation "+monthname,40);
	   

		   sheet.mergeCells(4, 3, 13, 3);
		   addCaption1(sheet, 4, 3, "<----------------------------- D E N O M I N A T I O N ------------------------------------->",40);
		   
		   addCaption2(sheet, 0, 4, "Sno",10);
		   addCaption2(sheet, 1, 4, "Emp Code",10);
		   addCaption2(sheet, 2, 4, "Emp Name",30);
		   addCaption2(sheet, 3, 4, "Net Salary ",15);
		   addCaption2(sheet, 4, 4, "2000",10);
		   addCaption2(sheet, 5, 4, "1000",10);
		   addCaption2(sheet, 6, 4, "500",10);
		   addCaption2(sheet, 7, 4, "100",10);
		   addCaption2(sheet, 8, 4, "50",10);
		   addCaption2(sheet, 9, 4, "20",10);
		   addCaption2(sheet, 10, 4, "10",10);
		   addCaption2(sheet, 11,4, "5",10);
		   addCaption2(sheet, 12, 4, "2",10);
		   addCaption2(sheet, 13, 4, "1",10);




		r=5;

}  

	
	public void createContent(WritableSheet sheet) throws WriteException,RowsExceededException
	{
	
		boolean first=true;
	
		// detail header
		int size=esicList.size();
	
		EmptranDto emp =null;
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
		    createReport1(sheet, emp);
		    
	
		}
		
			  int dash=0;
			  addLabel(sheet, 0, r, "",dash);
			  addLabel(sheet, 1, r, "",dash);
			  addLabel(sheet, 2, r, "Total",dash);
			  addDouble(sheet, 3, r, totnet,dash);
			  addNumber(sheet, 4, r, gtwothsnd,dash);
			  addNumber(sheet, 5, r, gthsnd,dash);
			  addNumber(sheet, 6, r, gfhrd,dash);
			  addNumber(sheet, 7, r, ghrd,dash);
			  addNumber(sheet, 8, r, gfty,dash);
			  addNumber(sheet, 9, r, gtwty,dash);
			  addNumber(sheet, 10, r, gten,dash);
			  addNumber(sheet, 11, r,gfive,dash);
			  addNumber(sheet, 12, r,gtwo,dash);
			  addNumber(sheet, 13, r,gone,dash);

			  
		  
	}

	public void createReport1(WritableSheet sheet,EmptranDto emp) throws WriteException,RowsExceededException
	{
	
		
			int dash=0;
//			double totearn=emp.getBasic_value()+emp.getDa_value()+emp.getIncentive_value()+emp.getOt_value();
//			double totded=emp.getPf_value()+emp.getEsis_value()+emp.getAdvance();
//			double net = totearn-totded;

			double totearn=emp.getBasic_value()+emp.getDa_value()+emp.getIncentive_value()+emp.getOt_value()+emp.getHra_value()+emp.getAdd_hra_value()+emp.getSpl_incen_value()+emp.getMisc_value()+emp.getLta_value()+emp.getMedical_value()+emp.getStair_value();
			double totded=emp.getPf_value()+emp.getEsis_value()+emp.getAdvance();
			double net = totearn-totded;
			boolean check=false;
			
			if(emp.getSerialno()>0)
			{
				addNumber(sheet, 0, r, emp.getSerialno(),dash);
				addNumber(sheet, 1, r, emp.getEmp_code(),dash);
				addLabel(sheet, 2, r, emp.getEmp_name(),dash);
				addDouble(sheet, 3, r, net,dash);

				tot=net;

				check=(Boolean) checkList.get("2000");
				
				if(check)
				{
					twothsnd = (int) (tot/2000);
					gtwothsnd+=twothsnd;
					tot = tot-(twothsnd*2000);
				}

				check=(Boolean) checkList.get("1000");

				if(check)
				{
					thsnd = (int) (tot/1000);
					gthsnd+=thsnd;
					tot = tot-(thsnd*1000);
				}
				
				check=(Boolean) checkList.get("500");
				if(check)
				{
					fhrd = (int) (tot/500);
					gfhrd+=fhrd;
					tot = tot-(fhrd*500);
				}

				check=(Boolean) checkList.get("100");
				if(check)
				{
					hrd = (int) (tot/100);
					ghrd+=hrd;
					tot = tot-(hrd*100);
				}
				
				
				check=(Boolean) checkList.get("50");
				if(check)
				{
					fty = (int) (tot/50);
					gfty+=fty;
					tot = tot-(fty*50);
				}
				
				check=(Boolean) checkList.get("20");
				if(check)
				{
					twty = (int) (tot/20);
					gtwty+=twty;
					tot = tot-(twty*20);
				}

				
				check=(Boolean) checkList.get("10");
				if(check)
				{
					ten = (int) (tot/10);
					gten+=ten;
					tot = tot-(ten*10);
				}
				
				check=(Boolean) checkList.get("5");
				if(check)
				{
					five = (int) (tot/5);
					gfive+=five;
					tot = tot-(five*5);
				}
				
				
				check=(Boolean) checkList.get("2");
				if(check)
				{
					two = (int) (tot/2);
					gtwo+=two;
					tot = tot-(two*2);
				}

				one=(int) tot;
				gone+=one;

				totnet+=net;

				addNumber(sheet, 4, r, twothsnd,dash);
				addNumber(sheet, 5, r, thsnd,dash);
				addNumber(sheet, 6, r, fhrd,dash);
				addNumber(sheet, 7, r, hrd,dash);
				addNumber(sheet, 8, r, fty,dash);
				addNumber(sheet, 9, r, twty,dash);
				addNumber(sheet, 10, r, ten,dash);
				addNumber(sheet, 11, r, five,dash);
				addNumber(sheet, 12, r, two,dash);
				addNumber(sheet, 13, r, one,dash);
				r++;
			}
	}

	
	
}
