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


public class YearlyReport  extends WriteExcel
{    

	int r,i;
	SimpleDateFormat sdf;
	String inputFile;

	private String  drvnm,flname,cmp_name,monthname; 
	private int depo_code, cmp_code, fyear, mnth_code,btnno,repno;
	private double tot;
	private double[] gtot;
	ArrayList<?> esicList;
	SheetSettings settings; 

	public YearlyReport(Integer depo_code,Integer cmp_code,Integer fyear,String cmp_name,String drvnm,Integer repno) 

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

			flname="Absent-"+cmp_name.substring(0, 6);
			if(repno==2)
				flname="OT-"+cmp_name.substring(0, 6);
			else if(repno==3)
				flname="Sterlite-"+cmp_name.substring(0, 6);
			else if(repno==4)
				flname="Present-"+cmp_name.substring(0, 6);
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
			esicList=pdao.getYearlyAbsentReport(depo_code, cmp_code, fyear,repno);
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


		switch(repno)
		{
		case 1:
			createHeader1(sheet); // Absent Report
			break;
		case 2:
			createHeader2(sheet); // OT Report Yearly
			break;
		case 3:
			createHeader3(sheet); // Sterlite days Report
			break;
		case 4:
			createHeader1(sheet); // Present Report
			break;					
		}



		//	   settings.setHorizontalFreeze(3);
		settings.setVerticalFreeze(4);


	}  


	public void createHeader1(WritableSheet sheet)
			throws WriteException {


		sheet.mergeCells(0, 0, 14, 0);
		// Write a few headers
		addCaption(sheet, 0, 0, cmp_name,40);


		sheet.mergeCells(0, 1, 14, 1);
		if(repno==1)
			addCaption1(sheet, 0, 1, " Absent Report for the year "+fyear+"-"+(fyear+1),40);
		else
			addCaption1(sheet, 0, 1, " Present Report for the year "+fyear+"-"+(fyear+1),40);

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
		addCaption2(sheet, 15, 3, "Total ",10);

		r=4;

	}  

	public void createHeader2(WritableSheet sheet)  throws WriteException {



		sheet.mergeCells(0, 0, 14, 0);
		// Write a few headers
		addCaption(sheet, 0, 0, cmp_name,40);


		sheet.mergeCells(0, 1, 14, 1);
		addCaption1(sheet, 0, 1, " OT Report for the year "+fyear+"-"+(fyear+1),40);

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
		addCaption2(sheet, 15, 3, "Total ",10);
		r=4;
	}  


	public void createHeader3(WritableSheet sheet)  throws WriteException {




		sheet.mergeCells(0, 0, 14, 0);
		// Write a few headers
		addCaption(sheet, 0, 0, cmp_name,40);


		sheet.mergeCells(0, 1, 14, 1);
		addCaption1(sheet, 0, 1, " Sterlite Report for the year "+fyear+"-"+(fyear+1),40);

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
		addCaption2(sheet, 15, 3, "Total ",10);
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

			switch(repno)
			{
			case 1:
				createReport1(sheet, emp);
				break;
			case 2:
				createReport1(sheet, emp);
				break;
			case 3:
				createReport1(sheet, emp);
				break;
			case 4:
				createReport1(sheet, emp);
				break;
			}

			pgbrk++;

			int k=3;
			tot=0.00;
			addLabel(sheet, 0, r, "",1);
			addLabel(sheet, 1, r, "",1);
			addLabel(sheet, 2, r, "Total",1);
			for (int i=0;i<12;i++)
			{
				addNumber(sheet, k++, r, (int) gtot[i],1);
				tot+=gtot[i];
			}
			addNumber(sheet, k++, r, (int) tot,1);


			/*if(pgbrk>55)
			{
				sheet.addRowPageBreak(r);
				pgbrk=0;
			}*/

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
			addNumber(sheet, k++, r, emp.getMon()[i],dash);
			tot+=emp.getMon()[i];
			gtot[i]+=emp.getMon()[i];;
		}
		addNumber(sheet, k++, r, (int) tot,dash);
		r++;



	}



}
