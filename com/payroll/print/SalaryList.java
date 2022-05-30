package com.payroll.print;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.payroll.dao.PayrollDAO;
import com.payroll.dto.EmptranDto;
import com.payroll.util.FigToWord;
import com.payroll.util.WritePDF;


public class SalaryList extends WritePDF{
   
 

	ArrayList<?> salList;  
 
	SimpleDateFormat sdf=null;
	SimpleDateFormat sdf2=null;
	DecimalFormat df = null;
	private String drvnm; 
	int year,depo,div,sinv,einv,doc_tp,mncode;
	boolean first=true;
	boolean beginPage=true; 

	int y,sno;
	 
	 
	EmptranDto emp;
	FigToWord fg;  
	String word="";
	int  pg,rde;
	 
 
	int depo_code,cmp_code,fyear,mnth_code,repno;
	String cmp_name,monthname;
	EmptranDto vd;
	PayrollDAO pdao;
	double total,tot;
	int bkcode;
	boolean print=false;
	
	public SalaryList(Integer depo_code,Integer cmp_code,Integer fyear,Integer mnth_code,String cmp_name,String drvnm,String monthname,Integer btnno,Integer repno)
		{
		
		this.depo_code=depo_code;
		this.cmp_code=cmp_code;
		this.fyear=fyear;
		this.mnth_code=mnth_code;
		this.drvnm=drvnm;
		this.cmp_name=cmp_name;
		this.monthname=monthname;
		this.repno=repno;
		
		String pdfFilename = "";
		y=747;
		pg=1;
		sdf2 = new SimpleDateFormat("dd-MM-yy_HH-mma");
		if(repno==16)
			pdfFilename="BonusList #"+" "+sdf2.format(new Date())+".pdf";
		else
			pdfFilename="SalaryList #"+" "+sdf2.format(new Date())+".pdf";

		createPDF(pdfFilename);
        ///// View Report on screen
        File file=null;

        	if (Desktop.isDesktopSupported()) {
        		file=new File(drvnm+"\\"+ pdfFilename);
        		try {
						Desktop.getDesktop().open(file);
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
        	}
		
	}

	private void createPDF (String pdfFilename){

		Document doc = new Document();
		PdfWriter docWriter = null;
		initializeFonts();

		try {
			String path = drvnm+"\\"+ pdfFilename;
			docWriter = PdfWriter.getInstance(doc , new FileOutputStream(path));
			doc.addAuthor("Aristo");
			doc.addCreationDate();
			doc.addProducer();
			doc.addCreator("aristopharma.org");
			doc.addTitle("Salary Register");
			doc.setPageSize(PageSize.A4);
			 
			
			doc.open();
			PdfContentByte cb = docWriter.getDirectContent();
			
			
			fg = new FigToWord();
			pdao = new PayrollDAO();
//			salList=pdao.getEsicList(depo_code, cmp_code, fyear, mnth_code,10);
			if(repno==16)
				salList= (ArrayList<?>) pdao.getBonusRegister(depo_code, cmp_code, fyear, repno);
			else if(mnth_code>=201611)
				salList= (ArrayList<?>) pdao.getSalaryRegister(depo_code, cmp_code, fyear, mnth_code,10);

			
			int lsize=0;
			sdf = new SimpleDateFormat("dd/MM/yyyy");
			df = new DecimalFormat("0.00");
			beginPage = true;
			total=0.00;
			int i=0;
			if(salList!=null)
			{
				if(repno==16)
					lsize=salList.size();
				else
					lsize=salList.size()-1;
			}

			bkcode=0;
			sno=1;
			for(i=0; i < lsize; i++ )
			{
				vd= (EmptranDto) salList.get(i);
				if(beginPage)
				{
					bkcode=vd.getBank_code();
					beginPage = false;
					generateLayout(doc, cb);
				}

				if(bkcode!=vd.getBank_code())
				{
					generateFooter(doc, cb);
					pg=1;
					generateLayout(doc, cb);
				}
				
				if(vd.getSerialno()>0)
				{
					generateDetail1(doc, cb);
				}
				
			} // for loop for products

	 
			generateFooter(doc, cb);
			
		doc.newPage();
		beginPage = true;
		}
		catch (DocumentException dex)
		{
			dex.printStackTrace();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			if (doc != null)
			{
				doc.close();
			}
			if (docWriter != null)
			{
				docWriter.close();
			}
		}
	}
		
	private void generateLayout(Document doc, PdfContentByte cb)  {

		try {

			cb.setLineWidth(1f);
			y=717;
		 
			createHeadings(cb,30,747,"M/s. "+cmp_name);
			if(repno==16)
			{
				createContent2(cb,30,737,"Bonus List of Employees Having There A/c in ",PdfContentByte.ALIGN_LEFT);
				createContent2(cb,30,727,"Staff Bonus By "+vd.getBank(),PdfContentByte.ALIGN_LEFT);
				createContent2(cb,30,717,"Bonus for the year "+(fyear+1),PdfContentByte.ALIGN_LEFT);
			}
			else
			{
				createContent2(cb,30,737,"Salary List of Employees Having There A/c in ",PdfContentByte.ALIGN_LEFT);
				createContent2(cb,30,727,"Staff Salary By "+vd.getBank(),PdfContentByte.ALIGN_LEFT);
				createContent2(cb,30,717,"Salary for the month "+monthname,PdfContentByte.ALIGN_LEFT);
			}
			
			createtext4(cb,525,737,"Page # "+(pg++));
			 
 
			cb.moveTo(20,707);  // horizontal lines 
			cb.lineTo(550,707);
			

			 
			
			/// end of product details vertical lines

				createtext2(cb,25,697,"Sr No");
				createtext2(cb,72,697,"Emp Code");
				createtext2(cb,170,697,"Name of the Employee");
				createtext2(cb,325,697,"Account No.");
				createtext2(cb,480,697,repno==16?"Bonus Paid":"Salary Amount ");

			  
				cb.moveTo(20,688);  // horizontal lines 
				cb.lineTo(550,688);
				cb.stroke();
				y=678;				
			

		}

		catch (Exception ex){
			ex.printStackTrace();
		}

	}
	
		
	 

 
private void generateDetail1(Document doc, PdfContentByte cb)  {
	

	try {
//		rde=0;
 
		double totearn=vd.getBasic_value()+vd.getDa_value()+vd.getHra_value()+vd.getAdd_hra_value()+vd.getIncentive_value()+vd.getSpl_incen_value()+vd.getOt_value()+vd.getLta_value()+vd.getMedical_value()+vd.getMisc_value()+vd.getStair_value()+vd.getMachine1_value()+vd.getMachine2_value()+vd.getFood_value();
		double totded=vd.getPf_value()+vd.getEsis_value()+vd.getAdvance()+vd.getCoupon_amt()+vd.getProf_tax();;

		double net = totearn-totded;
		 
		rde=y;
		checkLn(doc, cb);
		 
		if(repno==16)
		{
			tot=0.00;
			print=false;
			for (int i=0;i<12;i++)
			{
				tot+=vd.getAtten()[i];
			}

			if(tot>=31)
				print=true;

			if(print)
			{
				createContent3(cb,30,y,String.valueOf(sno),PdfContentByte.ALIGN_LEFT);
				createContent3(cb,90,y,String.valueOf(vd.getEmp_code()),PdfContentByte.ALIGN_LEFT);
				createContent3(cb,170,y,vd.getEmp_name(),PdfContentByte.ALIGN_LEFT);
				createContent3(cb,325,y,vd.getBank_accno(),PdfContentByte.ALIGN_LEFT);
				createContent3(cb,525,y,df.format(vd.getBonus_value()),PdfContentByte.ALIGN_RIGHT);
				total+=vd.getBonus_value();
				sno++;
				y=y-14;
			}
		}
		else if(net>0 && mnth_code>=201611)
		{
			createContent3(cb,30,y,String.valueOf(sno),PdfContentByte.ALIGN_LEFT);
			createContent3(cb,90,y,String.valueOf(vd.getEmp_code()),PdfContentByte.ALIGN_LEFT);
			createContent3(cb,170,y,vd.getEmp_name(),PdfContentByte.ALIGN_LEFT);
			createContent3(cb,325,y,vd.getBank_accno(),PdfContentByte.ALIGN_LEFT);
			createContent3(cb,525,y,df.format(net),PdfContentByte.ALIGN_RIGHT);
			total+=net;
			sno++;
			y=y-14;
		}
		
		
		 

 
		
	}

	catch (Exception ex){
		ex.printStackTrace();
	}

}
 
	

private void generateFooter(Document doc, PdfContentByte cb)  {

	try {

		cb.moveTo(20,y);  // horizontal lines 
		cb.lineTo(550,y);

		y=y-14;
		word=fg.numWord(total);
		createContent1(cb,325,y,"Total ",PdfContentByte.ALIGN_LEFT);
		createContent1(cb,525,y,df.format(total),PdfContentByte.ALIGN_RIGHT);
		y=y-10;
		total=0.00;
		sno=1;
		cb.moveTo(20,y);  // horizontal lines 
		cb.lineTo(550,y);
		cb.stroke();
		y=y-10;
		
		createContent1(cb,25,y,word,PdfContentByte.ALIGN_LEFT);
		y=y-40;

		//createContent1(cb,225,66,"Authorised Signatory ",PdfContentByte.ALIGN_LEFT);
		createContent1(cb,425,y,"Authorised Signatory ",PdfContentByte.ALIGN_LEFT);
		y=y-40;
		createContent1(cb,425,y,cmp_name.split(",")[0],PdfContentByte.ALIGN_LEFT);
		try {
			bkcode=vd.getBank_code();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			bkcode=0;
		}
		doc.newPage();

	}

	catch (Exception ex){
		ex.printStackTrace();
	}

}

	
	
	public void checkLn(Document doc, PdfContentByte cb)
	{
		if (rde==0)
			rde=y;
		   
		if(rde<=60)
		{
			cb.moveTo(20,y);  // horizontal lines 
			cb.lineTo(550,y);
			cb.stroke();
			y=y-10;
			createContent3(cb,30,y,"Continued to Pg # "+pg,PdfContentByte.ALIGN_LEFT);
			doc.newPage();
			//beginPage = true;
			generateLayout(doc, cb);
			//generateDetail1(doc, cb); 
		}
	}
	
 

	public void LinePrint(PdfContentByte cb,int start,int end )
	{
		
		// PRODUCT DETAIL VERTICAL LINES 
		cb.moveTo(45,727); // vertical line after marks and nos
		cb.lineTo(45,y);
		
		cb.moveTo(170,727); // vertical line after marks and nos
		cb.lineTo(170,y);

		cb.moveTo(210,727); // vertical line after marks and nos
		cb.lineTo(210,y);
		
		cb.moveTo(250,727); // vertical line after no and kind of pkgs
		cb.lineTo(250,y);
		
		cb.moveTo(290,710); // vertical line after description of goods
		cb.lineTo(290,y);
		
		cb.moveTo(330,710); // vertical line after qty
		cb.lineTo(330,y);

		cb.moveTo(375,727); // vertical line after qty
		cb.lineTo(375,y);
		
		cb.moveTo(410,710); // vertical line after qty
		cb.lineTo(410,y);

		cb.moveTo(450,710); // vertical line after qty
		cb.lineTo(450,y);

		cb.moveTo(490,727); // vertical line after qty
		cb.lineTo(490,y);

		cb.moveTo(540,727); // vertical line after qty
		cb.lineTo(540,y);
		//cb.stroke();
		
	}

	
	public void createContent3(PdfContentByte cb, float x, float y, String text, int align){


		cb.beginText();
		cb.setFontAndSize(bf, 10);
		//cb.setRGBColorFill(27, 109, 203);  // BLUE
		cb.showTextAligned(align, text.trim(), x , y, 0);
		cb.endText(); 

	}
}