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


public class LoanRepaymentReport  extends WriteExcel
{    

	int r,i;
	SimpleDateFormat sdf;
	String inputFile;

	private String  drvnm,flname,cmp_name,monthname,emp_name; 
	private int depo_code, cmp_code, emp_code,btnno,repno;
	private double tot;
	private double[] gtot;
	ArrayList<?> esicList;
	SheetSettings settings; 

	public LoanRepaymentReport(Integer depo_code,Integer cmp_code,Integer emp_code,String cmp_name,String emp_name,String drvnm) 

	{
		try 
		{
			this.r=0;

			this.drvnm=drvnm;
			this.cmp_name=cmp_name;
			this.emp_name=emp_name;
			this.depo_code=depo_code;
			this.cmp_code=cmp_code;
			this.emp_code=emp_code;
			sdf = new SimpleDateFormat("dd/MM/yyyy");

			flname="Loan Repayment-"+emp_name;
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
			esicList=pdao.getLoanRepyamentReport(depo_code, cmp_code, emp_code);
			
			System.out.println("size of loan list "+esicList);
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

		//	   settings.setHorizontalFreeze(3);
		settings.setVerticalFreeze(4);


	}  


	public void createHeader1(WritableSheet sheet)
			throws WriteException {


		
		sheet.mergeCells(0, 0, 8, 0);
		   // Write a few headers
	   addCaption(sheet, 0, 0, cmp_name,40);
	   
	   sheet.mergeCells(0, 1, 8, 1);
	   addCaption1(sheet, 0, 1, " Loan Repyament Report of "+emp_name,40);

	   
		addCaption2(sheet, 0, 3, "Period",10);
		addCaption2(sheet, 1, 3, "Year",10);
		addCaption2(sheet, 2, 3, "Employee Code",10);
		addCaption2(sheet, 3, 3, "Employee  Name",30);
		addCaption2(sheet, 4, 3, "Loan Amount",12);
		addCaption2(sheet, 5, 3, "Repayment Amount",12);
		addCaption2(sheet, 6, 3, "Balance",10);

		r=4;

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


	}

	public void createReport1(WritableSheet sheet,EmptranDto emp) throws WriteException,RowsExceededException
	{


		int dash=0;

		addLabel(sheet, 0, r, emp.getBank(),dash);
		addNumber(sheet, 1, r, emp.getFin_year(),dash);
		addNumber(sheet, 2, r, emp.getEmp_code(),dash);
		addLabel(sheet, 3, r, emp.getEmp_name(),dash);
		addDouble(sheet, 4, r, emp.getLoan(), dash);
		addDouble(sheet, 5, r, emp.getLta(), dash);
		addDouble(sheet, 6, r, emp.getLta_value(), dash);
		r++;



	}



}
