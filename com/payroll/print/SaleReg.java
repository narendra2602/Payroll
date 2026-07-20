package com.payroll.print;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import jxl.SheetSettings;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.format.PageOrientation;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.payroll.dao.PayrollDAO;
import com.payroll.dto.EmptranDto;
import com.payroll.excel.WriteExcel;


public class SaleReg  extends WriteExcel
{    
   /**   
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	ArrayList<?> salList;  
	 
	SimpleDateFormat sdf=null;
	SimpleDateFormat sdf2=null;
	DecimalFormat df = null;

	
   int r,i,col,pg;
   int dash;
   
   
   EmptranDto inv;
   String wtxt,monNm,sdate,edate, smon,inputFile,btnname,drvnm,printernm,brnm,cmp_name,monthname,address;
   int year,depo_code,cmp_code,fyear,mnth_code,div,optn,doc_type;
   private Date stdt,eddt;
   ArrayList partyList;
   SheetSettings settings;
   String flnm;
   PayrollDAO pdao;

private WritableWorkbook workbook;
	public SaleReg(Integer depo_code,Integer cmp_code,Integer fyear,Integer mnth_code,String cmp_name,String drvnm,String monthname,Integer btnno,Integer repno,Integer opt,String address)
	{ 
    try {
    	
		this.depo_code=depo_code;
		this.cmp_code=cmp_code;
		this.fyear=fyear;
		this.mnth_code=mnth_code;
		this.drvnm=drvnm;
		this.cmp_name=cmp_name;
		this.monthname=monthname;
		this.address=address;
		sdf2 = new SimpleDateFormat("dd-MM-yy_HH-mma");
    	flnm="SalaryReg #"+" "+sdf2.format(new Date());

    	jbInit();
    	
        File file=null;

  		if (Desktop.isDesktopSupported()) 
  		{
  		
//  			if (btnname.equalsIgnoreCase("View"))
  			{
  				file=new File(drvnm+"\\"+flnm+".xls");
  				Desktop.getDesktop().open(file);
  				
  			}
  		}
    	
    	
   

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


    private void jbInit() throws Exception {
    	 
    	   
        try {

          this.order();


        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }    
    

   /**
    * test characters set conversion
    */
   public void order() {

    try {
    	
 			pdao = new PayrollDAO();
 			if(mnth_code>202505)
 				salList= (ArrayList<?>) pdao.getSalaryRegisterNew(depo_code, cmp_code, fyear, mnth_code,11,0,0);
 			else
 				salList= (ArrayList<?>) pdao.getSalaryRegister(depo_code, cmp_code, fyear, mnth_code,1);
			sdf = new SimpleDateFormat("dd/MM/yyyy");
			df = new DecimalFormat("0.00");
			boolean beginPage = true;
		 

		   createExcel();

		  } catch (Exception e) {
			  e.printStackTrace();
		  }
	
	  }
	  /////// print file option //////////////  
   
   
   
   
//////excel file generation report ////////   
   
public void createExcel() throws WriteException, IOException {

	   setOutputFile(drvnm+"\\"+flnm+".xls");
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

	   workbook.createSheet(flnm, 0);
	   WritableSheet excelSheet = workbook.getSheet(0);
	   settings = excelSheet.getSettings();
	   settings.setPrintTitlesRow(0, 4);
	   settings.setOrientation(PageOrientation.PORTRAIT);
	   settings.setDefaultRowHeight(260);
	   settings.setLeftMargin(0); 
	   settings.setRightMargin(0);
	   settings.setTopMargin(0.40); // 0.60  for mandideep printer
	   settings.setBottomMargin(0);  //0
	   settings.setFitWidth(1);

	   
//	   settings.setProtected(true);
	   excelSheet.setRowView(0, 360);
	   createLabel(excelSheet);
	   createContent(excelSheet);
	   

	   workbook.write();
	   workbook.close();

	   

}


public void createHeader(WritableSheet sheet)
		   throws WriteException {
	    
	   //Merge col[0-11] and row[0]
	   sheet.mergeCells(0, 0, 12, 0);
	   // Write a few headers
	   addCaption(sheet, 0, 0, cmp_name,10);
	   
	   sheet.mergeCells(0, 1, 12, 1);
		   addCaption1(sheet, 0, 1, "Salary Register for the month of "+monthname,30);
	   sheet.mergeCells(0, 2, 12, 2);
		   addCaption1(sheet, 0, 2, address,30);


		   
	   sheet.mergeCells(0, 3, 4, 3);
		   addCaption2(sheet, 0, 3, " ",40);
	      
	   sheet.mergeCells(5, 3, 7, 3);
	   
	   addCaption2(sheet, 5, 3, "E A R N I N G",20);
   sheet.mergeCells(8, 3, 11, 3);
	   addCaption2(sheet, 8, 3, "D E D U C T I O N",12);
	   addCaption2(sheet, 12, 3, "",12);


	   addCaption2(sheet, 0, 4, "Emp Code",6);
	   addCaption2(sheet, 1, 4, "Particular",9);
	   addCaption2(sheet, 2, 4, "",17);
	   sheet.mergeCells(3, 4, 4, 4);
	   addCaption2(sheet, 3, 4, "Rates",8);
	   addCaption2(sheet, 5, 4, "Earning Amount",7);
	   addCaption2(sheet, 6, 4, "Arears-1",6);
	   addCaption2(sheet, 7, 4, "Arears-2",7);
	   addCaption2(sheet, 8, 4, "Deductions",8);
	   addCaption2(sheet, 9, 4, "Ded.Amt",7);
	   addCaption2(sheet, 10, 4, "Arrears Ded.-1",7);
	   addCaption2(sheet, 11, 4, "Arrears Ded.-2",7);
	   addCaption2(sheet, 12, 4, "Net Payable",7);

	   
//	   settings.setHorizontalFreeze(2);
	   settings.setVerticalFreeze(4);

}  


public void createContent(WritableSheet sheet) throws WriteException,RowsExceededException
{

	   boolean first=true;
	   col=0;
	   r=5;
	   pg=1;
	   int dash=0;
	   double tot=0.00;
	   double tot4=0.00;
	   double tot5=0.00;
	   double tot6=0.00;
	   double tot7=0.00;
	   double tot9=0.00;
	   double tot10=0.00;
	   double tot11=0.00;
	   double tot12=0.00;
	   double gtot=0.00;
	   double ltot4=0.00;
	   double ltot5=0.00;
	   double ltot6=0.00;
	   double ltot7=0.00;
	   double ltot9=0.00;
	   double ltot10=0.00;
	   double ltot11=0.00;

	   // detail header
	   int size=salList.size();
	   int wr=0;
	   int cnt=0;
	   for (i=0;i<size;i++)
	   {
		   inv = (EmptranDto) salList.get(i);
		   wr=i+1;
		   if(first)
		   {
			   createHeader(sheet);
			   first=false;
		   }
/*		   cnt=cnt+1;
		   if(cnt>4)
		   {
			   sheet.mergeCells(0, r, 12, r);
			   // Write a few headers
			   addCaption(sheet, 0, r, cmp_name,10);
			   r++;
			   sheet.mergeCells(0, r, 12, r);
				   addCaption1(sheet, 0, r, "Salary Register for the month of "+monthname,30);
			   r++;	
				   sheet.mergeCells(0, r, 11, r);
				   addCaption1(sheet, 0, r, address,30);
				   addCaption1(sheet, 12, r,"Page # "+pg++,10);
			   r++;	   
				   
				   sheet.mergeCells(0, r, 4, r);
				   addCaption2(sheet, 0, r, " ",40);
			      
				   sheet.mergeCells(5, r, 7, r);
			   
			   addCaption2(sheet, 5, r, "E A R N I N G",20);
			   sheet.mergeCells(8, r, 11, r);
			   addCaption2(sheet, 8, r, "D E D U C T I O N",12);
			   addCaption2(sheet, 12, r, "",12);
			   r++;

			   addCaption2(sheet, 0, r, "Emp Code",6);
			   addCaption2(sheet, 1, r, "Particular",9);
			   addCaption2(sheet, 2, r, "",17);
			   sheet.mergeCells(3, r, 4, r);
			   addCaption2(sheet, 3, r, "Rates",8);
			   addCaption2(sheet, 5, r, "Earning Amount",7);
			   addCaption2(sheet, 6, r, "Arears-1",6);
			   addCaption2(sheet, 7, r, "Arears-2",7);
			   addCaption2(sheet, 8, r, "Deductions",8);
			   addCaption2(sheet, 9, r, "Ded.Amt",7);
			   addCaption2(sheet, 10, r, "Arrears Ded.-1",7);
			   addCaption2(sheet, 11, r, "Arrears Ded.-2",7);
			   addCaption2(sheet, 12, r, "Net Payable",7);
			   r++;
			   cnt=0;
		   }
*/		   		
			   dash=1;
			   addLabel(sheet, 0, r, "",dash);
			   addLabel(sheet, 1, r, "Emp.Name",dash);
			   addLabel(sheet, 2, r, inv.getEmp_name(),dash);
			   addLabel(sheet, 3, r, "Basic",dash);
			   addDouble(sheet, 4, r, inv.getBasic(),dash);
			   addDouble(sheet, 5, r, inv.getBasic_value(),dash);
			   addDouble(sheet, 6, r, inv.getArear_basic_value(),dash);
			   addDouble(sheet, 7, r, inv.getArear2_basic_value(),dash);
			   addLabel(sheet, 8, r, "PF",dash);
			   addDouble(sheet, 9, r, inv.getPf_value(),dash);
			   addDouble(sheet, 10, r, inv.getArear1_pf_value(),dash);
			   addDouble(sheet, 11, r, inv.getArear2_pf_value(),dash);
			   addLabel(sheet, 12, r, "",dash);
			   r++;
			   addLabel(sheet, 0, r, "",dash);
			   addLabel(sheet, 1, r, "Designation",dash);
			   addLabel(sheet, 2, r, inv.getDesignation(),dash);
			   addLabel(sheet, 3, r, "DA",dash);
			   addDouble(sheet, 4, r, inv.getDa(),dash);
			   addDouble(sheet, 5, r, inv.getDa_value(),dash);
			   addDouble(sheet, 6, r, inv.getArear_da_value(),dash);
			   addDouble(sheet, 7, r, inv.getArear2_da_value(),dash);
			   addLabel(sheet, 8, r, "Esic",dash);
			   addDouble(sheet, 9, r, inv.getEsis_value(),dash);
			   addDouble(sheet, 10, r, inv.getArear1_esic_value(),dash);
			   addDouble(sheet, 11, r, inv.getArear2_esic_value(),dash);
			   addLabel(sheet, 12, r, "",dash);
			   r++;
			   addLabel(sheet, 0, r, "",dash);
			   addLabel(sheet, 1, r, "PF No",dash);
			   addLabel(sheet, 2, r, ""+inv.getPf_no(),dash);
			   addLabel(sheet, 3, r, "HRA",dash);
			   addDouble(sheet, 4, r, inv.getHra(),dash);
			   addDouble(sheet, 5, r, inv.getHra_value(),dash);
			   addDouble(sheet, 6, r, inv.getArear_hra_value(),dash);
			   addDouble(sheet, 7, r, inv.getArear2_hra_value(),dash);
			   addLabel(sheet, 8, r, "P.Tax",dash);
			   addDouble(sheet, 9, r, inv.getProf_tax(),dash);
			   addDouble(sheet, 10, r, 0.00,dash);
			   addDouble(sheet, 11, r, inv.getArear2_prof_value(),dash);
			   addLabel(sheet, 12, r, "",dash);
			   r++;
			   addLabel(sheet, 0, r, "",dash);
			   addLabel(sheet, 1, r, "ESIC No",dash);
			   addLabel(sheet, 2, r, String.valueOf(inv.getEsic_no()),dash);
			   addLabel(sheet, 3, r, "Incentive",dash);
			   addDouble(sheet, 4, r, inv.getIncentive(),dash);
			   addDouble(sheet, 5, r, inv.getIncentive_value(),dash);
			   addDouble(sheet, 6, r, inv.getArear_incentive_value(),dash);
			   addDouble(sheet, 7, r, inv.getArear2_incentive_value(),dash);
			   addLabel(sheet, 8, r, "Advance",dash);
			   addDouble(sheet, 9, r, inv.getAdvance(),dash);
			   addDouble(sheet, 10, r, 0.00,dash);
			   addDouble(sheet, 11, r, 0.00,dash);
			   addLabel(sheet, 12, r, "",dash);
			   r++;
			   addLabel(sheet, 0, r, "",dash);
			   addLabel(sheet, 1, r, "Bank Name",dash);
			   addLabel(sheet, 2, r, inv.getBank(),dash);
			   addLabel(sheet, 3, r, "Medical",dash);
			   addDouble(sheet, 4, r, inv.getMedical(),dash);
			   addDouble(sheet, 5, r, inv.getMedical_value(),dash);
			   addDouble(sheet, 6, r, inv.getArear_medical_value(),dash);
			   addDouble(sheet, 7, r, 0.00,dash);
			   addLabel(sheet, 8, r, "Loan",dash);
			   addDouble(sheet, 9, r, inv.getLoan(),dash);
			   addDouble(sheet, 10, r, 0.00,dash);
			   addDouble(sheet, 11, r, 0.00,dash);
			   addLabel(sheet, 12, r, "",dash);
			   r++;
			   addLabel(sheet, 0, r, "",dash);
			   addLabel(sheet, 1, r, "Bank A/c No.",dash);
			   addLabel(sheet, 2, r, inv.getBank_accno(),dash);
			   addLabel(sheet, 3, r, "Food Allow.",dash);
			   addDouble(sheet, 4, r, inv.getFood_alw(),dash);
			   addDouble(sheet, 5, r, inv.getFood_value(),dash);
			   addDouble(sheet, 6, r, 0.00,dash);
			   addDouble(sheet, 7, r, 0.00,dash);
			   addLabel(sheet, 8, r, "Other Ded.",dash);
			   addDouble(sheet, 9, r, 0.00,dash);   
			   addDouble(sheet, 10, r, 0.00,dash);
			   addDouble(sheet, 11, r, 0.00,dash);
			   addLabel(sheet, 12, r, "",dash);
			   r++;
			   addLabel(sheet, 0, r, "",dash);
			   addLabel(sheet, 0, r, String.valueOf("    "+inv.getEmp_code()),dash);
			   addLabel(sheet, 1, r, "Present Days",dash);
			   addLabel(sheet, 2, r, String.valueOf(inv.getAtten_days()),dash);
			   addLabel(sheet, 3, r, "LTA",dash);
			   addDouble(sheet, 4, r, inv.getLta(),dash);
			   addDouble(sheet, 5, r, inv.getLta_value(),dash);
			   addDouble(sheet, 6, r, 0.00,dash);
			   addDouble(sheet, 7, r, 0.00,dash);
			   addLabel(sheet, 8, r, "",dash);
			   addDouble(sheet, 9, r, 0.00,dash);   
			   addDouble(sheet, 10, r, 0.00,dash);
			   addDouble(sheet, 11, r, 0.00,dash);
			   addLabel(sheet, 12, r, "",dash);
			   r++;
			   addLabel(sheet, 0, r, "",dash);
			   addLabel(sheet, 1, r, "Arrear Days",dash);
			   addLabel(sheet, 2, r, String.valueOf(inv.getArrear_days()),dash);
			   addLabel(sheet, 3, r, "Spl.Incen.",dash);
			   addDouble(sheet, 4, r, inv.getSpl_incentive(),dash);
			   addDouble(sheet, 5, r, inv.getSpl_incen_value(),dash);
			   addDouble(sheet, 6, r, 0.00,dash);
			   addDouble(sheet, 7, r, 0.00,dash);
			   addLabel(sheet, 8, r, "",dash);
			   addDouble(sheet, 9, r, 0.00,dash);   
			   addDouble(sheet, 10, r, 0.00,dash);
			   addDouble(sheet, 11, r, 0.00,dash);
			   addLabel(sheet, 12, r, "",dash);
			   r++;
			   addLabel(sheet, 0, r, "",dash);
			   addLabel(sheet, 1, r, "Comm.Hrs.",dash);
			   addLabel(sheet, 2, r, String.valueOf(inv.getExtra_hrs()),dash);
			   addLabel(sheet, 3, r, "Comm.Rate",dash);
			   addDouble(sheet, 4, r, inv.getOt_rate(),dash);
			   addDouble(sheet, 5, r, inv.getOt_value(),dash);
			   addDouble(sheet, 6, r, 0.00,dash);
			   addDouble(sheet, 7, r, 0.00,dash);
			   addLabel(sheet, 8, r, "",dash);
			   addDouble(sheet, 9, r, 0.00,dash);   
			   addDouble(sheet, 10, r, 0.00,dash);
			   addDouble(sheet, 11, r, 0.00,dash);
			   addLabel(sheet, 12, r, "",dash);
			   r++;
			   addLabel(sheet, 0, r, "",dash);
			   addLabel(sheet, 1, r, "Sterile Days",dash);
			   addLabel(sheet, 2, r, String.valueOf(inv.getStair_days()),dash);
			   addLabel(sheet, 3, r, "Sterile Rate",dash);
			   addDouble(sheet, 4, r, inv.getStair_alw(),dash);
			   addDouble(sheet, 5, r, inv.getStair_value(),dash);
			   addDouble(sheet, 6, r, 0.00,dash);
			   addDouble(sheet, 7, r, 0.00,dash);
			   addLabel(sheet, 8, r, "",dash);
			   addDouble(sheet, 9, r, 0.00,dash);   
			   addDouble(sheet, 10, r, 0.00,dash);
			   addDouble(sheet, 11, r, 0.00,dash);
			   addLabel(sheet, 12, r, "",dash);
			   r++;
			   addLabel(sheet, 0, r, "",dash);
			   addLabel(sheet, 1, r, "Opt1 Days",dash);
			   addLabel(sheet, 2, r, String.valueOf(inv.getMachine1_days()),dash);
			   addLabel(sheet, 3, r, "Opt1 Allow",dash);
			   addDouble(sheet, 4, r, inv.getMachine1_rate(),dash);
			   addDouble(sheet, 5, r, inv.getMachine1_value(),dash);
			   addDouble(sheet, 6, r, 0.00,dash);
			   addDouble(sheet, 7, r, 0.00,dash);
			   addLabel(sheet, 8, r, "",dash);
			   addDouble(sheet, 9, r, 0.00,dash);   
			   addDouble(sheet, 10, r, 0.00,dash);
			   addDouble(sheet, 11, r, 0.00,dash);
			   addLabel(sheet, 12, r, "",dash);
			   r++;
			   addLabel(sheet, 0, r, "",dash);
			   addLabel(sheet, 1, r, "Opt2 Days",dash);
			   addLabel(sheet, 2, r, String.valueOf(inv.getMachine2_days()),dash);
			   addLabel(sheet, 3, r, "Opt2 Allow",dash);
			   addDouble(sheet, 4, r, inv.getMachine2_rate(),dash);
			   addDouble(sheet, 5, r, inv.getMachine2_value(),dash);
			   addDouble(sheet, 6, r, 0.00,dash);
			   addDouble(sheet, 7, r, 0.00,dash);
			   addLabel(sheet, 8, r, "",dash);
			   addDouble(sheet, 9, r, 0.00,dash);   
			   addDouble(sheet, 10, r, 0.00,dash);
			   addDouble(sheet, 11, r, 0.00,dash);
			   addLabel(sheet, 12, r, "",dash);
			   r++;
			   addLabel(sheet, 0, r, "",dash);
			   addLabel(sheet, 1, r, "Absent Days",dash);
			   addLabel(sheet, 2, r, String.valueOf(inv.getAbsent_days()),dash);
			   addLabel(sheet, 3, r, "",dash);
			   addDouble(sheet, 4, r, 0.00,dash);
			   addDouble(sheet, 5, r, 0.00,dash);
			   addDouble(sheet, 6, r, 0.00,dash);
			   addDouble(sheet, 7, r, 0.00,dash);
			   addLabel(sheet, 8, r, "",dash);
			   addDouble(sheet, 9, r, 0.00,dash);   
			   addDouble(sheet, 10, r, 0.00,dash);
			   addDouble(sheet, 11, r, 0.00,dash);
			   addLabel(sheet, 12, r, "",dash);
			   r++;
			   addLabel(sheet, 0, r, "",dash);
			   addLabel(sheet, 1, r, "UAN No.",dash);
			   addLabel(sheet, 2, r, String.valueOf(inv.getUan_no()),dash);
			   addLabel(sheet, 3, r, "",dash);
			   addDouble(sheet, 4, r, 0.00,dash);
			   addDouble(sheet, 5, r, 0.00,dash);
			   addDouble(sheet, 6, r, 0.00,dash);
			   addDouble(sheet, 7, r, 0.00,dash);
			   addLabel(sheet, 8, r, "",dash);
			   addDouble(sheet, 9, r, 0.00,dash);
			   addDouble(sheet, 10, r, 0.00,dash);
			   addDouble(sheet, 11, r, 0.00,dash);
			   addLabel(sheet, 12, r, "",dash);
			   r++;
			   dash=3;
			   if(inv.getDash()==0)
			   {
				   tot=(int) ((inv.getBasic_value()+inv.getDa_value()+inv.getHra_value()+inv.getIncentive_value()+inv.getMedical_value()+inv.getOt_value()+inv.getMachine1_value()+inv.getMachine2_value()+inv.getStair_value()+inv.getLta_value())+0.50);
				   tot+=(int) ((inv.getArear_basic_value()+inv.getArear_da_value()+inv.getArear_hra_value()+inv.getArear_incentive_value()+inv.getArear_medical_value())+0.50);
				   tot+=(int) ((inv.getArear2_basic_value()+inv.getArear2_da_value()+inv.getArear2_hra_value()+inv.getArear2_incentive_value())+0.50);

				   tot4+=(int) ((inv.getBasic()+inv.getDa()+inv.getHra()+inv.getIncentive()+inv.getMedical())+0.50);
				   tot5+=(int) ((inv.getBasic_value()+inv.getDa_value()+inv.getHra_value()+inv.getIncentive_value()+inv.getMedical_value()+inv.getOt_value()+inv.getMachine1_value()+inv.getMachine2_value()+inv.getStair_value()+inv.getSpl_incen_value()+inv.getLta_value())+0.50);
				   tot6+=(int) ((inv.getArear_basic_value()+inv.getArear_da_value()+inv.getArear_hra_value()+inv.getArear_incentive_value()+inv.getArear_medical_value())+0.50);
				   tot7+=(int) ((inv.getArear2_basic_value()+inv.getArear2_da_value()+inv.getArear2_hra_value()+inv.getArear2_incentive_value())+0.50);

				   tot9+=(int) ((inv.getPf_value()+inv.getEsis_value()+inv.getProf_tax()+inv.getLoan()+inv.getAdvance())+0.50);   
				   tot10+=(int)((inv.getArear1_pf_value()+inv.getArear1_esic_value())+0.50);   
				   tot11+=(int)((inv.getArear2_pf_value()+inv.getArear2_esic_value()+inv.getArear2_prof_value())+0.50);   

				   ltot4+=(int) ((inv.getBasic()+inv.getDa()+inv.getHra()+inv.getIncentive()+inv.getMedical())+0.50);
				   ltot5+=(int) ((inv.getBasic_value()+inv.getDa_value()+inv.getHra_value()+inv.getIncentive_value()+inv.getMedical_value()+inv.getOt_value()+inv.getMachine1_value()+inv.getMachine2_value()+inv.getStair_value()+inv.getSpl_incen_value()+inv.getLta_value())+0.50);
				   ltot6+=(int)((inv.getArear_basic_value()+inv.getArear_da_value()+inv.getArear_hra_value()+inv.getArear_incentive_value()+inv.getArear_medical_value())+0.50);
				   ltot7+=(int)((inv.getArear2_basic_value()+inv.getArear2_da_value()+inv.getArear2_hra_value()+inv.getArear2_incentive_value())+0.50);

				   ltot9+=(int) ((inv.getPf_value()+inv.getEsis_value()+inv.getProf_tax()+inv.getLoan()+inv.getAdvance())+0.50);   
				   ltot10+=(int) ((inv.getArear1_pf_value()+inv.getArear1_esic_value())+0.50);   
				   ltot11+=(int)((inv.getArear2_pf_value()+inv.getArear2_esic_value()+inv.getArear2_prof_value())+0.50);   

				   tot-=(int) ((inv.getPf_value()+inv.getEsis_value()+inv.getProf_tax()+inv.getLoan()+inv.getAdvance()+inv.getArear2_prof_value())+0.50);
				   tot-=(int) ((inv.getArear1_pf_value()+inv.getArear1_esic_value())+0.50);
				   tot-=(int) ((inv.getArear2_pf_value()+inv.getArear2_esic_value())+0.50);
				   tot=ltot5+ltot6+ltot7-ltot9-ltot10-ltot11;
				   tot12+=tot;
				   
			   addLabel(sheet, 0, r, String.valueOf(wr),dash);
			   addLabel(sheet, 1, r, "",dash);
			   addLabel(sheet, 2, r, "",dash);
			   addLabel(sheet, 3, r, "Gross Amt",dash);
			   addNumber(sheet, 4, r,(int) ltot4 ,dash);
			   addNumber(sheet, 5, r,(int) ltot5,dash);
			   addNumber(sheet, 6, r,(int) ltot6,dash);
			   addNumber(sheet, 7, r,(int) ltot7,dash);
			   addLabel(sheet, 8, r, "Total Ded.",dash);
			   addNumber(sheet, 9, r,(int) ltot9,dash);   
			   addNumber(sheet, 10, r,(int) ltot10,dash);   
			   addNumber(sheet, 11, r,(int) ltot11,dash);   
			   addNumber(sheet, 12, r,(int) tot,dash);
			   ltot4=0.00;
			   ltot5=0.00;
			   ltot6=0.00;
			   ltot7=0.00;
			   ltot9=0.00;
			   ltot10=0.00;
			   ltot11=0.00;
			   }
			   else
			   {
				   addLabel(sheet, 0, r, "",dash);
				   addLabel(sheet, 1, r, "",dash);
				   addLabel(sheet, 2, r, "",dash);
				   addLabel(sheet, 3, r, "Grand Total",dash);
//				   addNumber(sheet, 4, r,(int) tot4,dash);
				   addNumber(sheet, 4, r,0,dash);
				   addNumber(sheet, 5, r,(int) tot5,dash);
				   addNumber(sheet, 6, r,(int) tot6,dash);
				   addNumber(sheet, 7, r,(int) tot7,dash);
				   addLabel(sheet, 8, r, "Total Ded.",dash);
				   addNumber(sheet, 9, r,(int) tot9,dash);   
				   addNumber(sheet, 10, r,(int) tot10,dash);   
				   addNumber(sheet, 11, r,(int) tot11,dash);   
				   addNumber(sheet, 12, r,(int) (tot5+tot6+tot7-tot9-tot10-tot11),dash);
				   
			   }
			   r++;
			   dash=1;

	   }	   


}



}
