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
import com.payroll.dto.ContractMastDto;
import com.payroll.dto.EmptranDto;
import com.payroll.util.WritePDF;
import com.payroll.view.BaseClass;


public class SalarySlip extends WritePDF{
   
 

	ArrayList<?> salList;  
 
	SimpleDateFormat sdf=null;
	SimpleDateFormat sdf2=null;
	DecimalFormat df = null;
	private String drvnm; 
	int year,depo,div,sinv,einv,doc_tp,mncode;
	boolean first=true;
     

	int y,sno;
	 
	 
	EmptranDto emp;
	 
	int  pg,rde;
	 
 
	int depo_code,cmp_code,fyear,mnth_code;
	String cmp_name,monthname;
	EmptranDto vd;
	PayrollDAO pdao;
	ContractMastDto cmpdto;
	
	public SalarySlip(Integer depo_code,Integer cmp_code,Integer fyear,Integer mnth_code,String cmp_name,String drvnm,String monthname,Integer btnno,Integer repno,Integer opt)
		{
		
		this.depo_code=depo_code;
		this.cmp_code=cmp_code;
		this.fyear=fyear;
		this.mnth_code=mnth_code;
		this.drvnm=drvnm;
		this.cmp_name=cmp_name;
		this.monthname=monthname;
		cmpdto = (ContractMastDto) BaseClass.loginDt.getBdto();
		String pdfFilename = "";
		y=747;
		pg=1;
		sdf2 = new SimpleDateFormat("dd-MM-yy_HH-mma");
		pdfFilename="SalarySlip #"+" "+sdf2.format(new Date())+".pdf";

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
			
			
			
			pdao = new PayrollDAO();
			salList= (ArrayList<?>) pdao.getSalaryRegister(depo_code, cmp_code, fyear, mnth_code,0);
			int lsize=0;
			sdf = new SimpleDateFormat("dd/MM/yyyy");
			df = new DecimalFormat("0.00");
			boolean beginPage = true;
		 
			int i=0;
			if(salList!=null)
			{
				lsize=salList.size();
			}

			for(i=0; i < lsize; i++ )
			{
				 vd= (EmptranDto) salList.get(i);
				if(vd.getSerialno()>0)
				{
				  generateDetail1(doc, cb);
				}
				
			} // for loop for products

	 
			
			if(beginPage)
			{
				beginPage = false;
				//generateLayout(doc, cb);
				
			}
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
		
	private void generateLayout123(Document doc, PdfContentByte cb)  {

		try {

			cb.setLineWidth(1f);
			y=747;
		 
			createHeadings(cb,30,747,"M/s. "+cmp_name);
			createContent2(cb,30,737,"Name :"+vd.getEmp_name(),PdfContentByte.ALIGN_LEFT);
			createContent2(cb,500,737,"Month :"+monthname,PdfContentByte.ALIGN_LEFT);
			createContent2(cb,30,y,"Name :"+vd.getEmp_name(),PdfContentByte.ALIGN_LEFT);
			
			createtext4(cb,530,737,"Page # "+(pg++));
			 
 
			cb.moveTo(20,727);  // horizontal lines 
			cb.lineTo(595,727);
			

			// PRODUCT DETAIL VERTICAL LINES 
			cb.moveTo(45,727); // vertical line after marks and nos
			cb.lineTo(45,615);
			
			cb.moveTo(170,727); // vertical line after marks and nos
			cb.lineTo(170,615);

			cb.moveTo(210,727); // vertical line after marks and nos
			cb.lineTo(210,615);
			
			cb.moveTo(250,727); // vertical line after no and kind of pkgs
			cb.lineTo(250,615);
			
			cb.moveTo(290,710); // vertical line after description of goods
			cb.lineTo(290,615);
			
			cb.moveTo(330,710); // vertical line after qty
			cb.lineTo(330,615);

			cb.moveTo(375,727); // vertical line after qty
			cb.lineTo(375,615);
			
			cb.moveTo(410,710); // vertical line after qty
			cb.lineTo(410,615);

			cb.moveTo(450,710); // vertical line after qty
			cb.lineTo(450,615);

			cb.moveTo(490,727); // vertical line after qty
			cb.lineTo(490,615);

			cb.moveTo(540,727); // vertical line after qty
			cb.lineTo(540,615);

		//	cb.moveTo(585,727); // vertical line after qty
		//	cb.lineTo(585,22);

			
			/// end of product details vertical lines

				createtext4(cb,25,715,"Emp");
				createtext4(cb,50,715,"Name of the Employee");
				createtext4(cb,215,715,"Rates");
				createtext4(cb,285,715,"E A R N I N G ");
				createtext4(cb,405,715,"D E D U C T I O N");

				createtext4(cb,25,708,"Code");
				cb.moveTo(210,710);  // horizontal lines 
				cb.lineTo(490,710);
				
				createtext4(cb,50,695,"Designation");
				createtext4(cb,175,695,"Present");
				createtext4(cb,215,695,"Basic R/T");
				createtext4(cb,255,695,"Ern Basic");
				createtext4(cb,295,695,"Add. Hra");
				createtext4(cb,335,695,"Comm off");
				createtext4(cb,380,695,"P.F.");
				createtext4(cb,415,695,"Advance");
				createtext4(cb,455,695,"Other ded");
				createtext4(cb,495,695,"Net");
				createtext4(cb,545,695,"Receiver");


				createtext4(cb,50,685,"PF.No.");
				createtext4(cb,175,685,"P.L.");
				createtext4(cb,215,685,"D.A. R/T");
				createtext4(cb,495,685,"Payable");
				createtext4(cb,545,685,"Signature");

				createtext4(cb,50,675,"Esis.No.");
				createtext4(cb,175,675,"C.L.");
				createtext4(cb,215,675,"Hra 5%");

				createtext4(cb,175,665,"Arr.Days");
				createtext4(cb,215,665,"Add.Hra");
				createtext4(cb,255,665,"Ern D.A.");
				createtext4(cb,295,665,"Incentive");
				createtext4(cb,335,665,"Medical");
				createtext4(cb,380,665,"ESIS");
				createtext4(cb,415,665,"Loan");

				createtext4(cb,175,655,"P.H.");
				createtext4(cb,215,655,"Incentive");
				createtext4(cb,335,655,"Lta");
				
				createtext4(cb,50,645,"Sr.No.");
				createtext4(cb,150,645,"Paid");
				createtext4(cb,175,645,"Off");
				createtext4(cb,215,645,"Comm.Rate");
				createtext4(cb,335,645,"Inct");

				createtext4(cb,150,635,"Days");
				createtext4(cb,175,635,"Absent");
				createtext4(cb,255,635,"Hra 5%");
				createtext4(cb,295,635,"Spl.Inc");
				createtext4(cb,335,635,"Gross");
				createtext4(cb,380,635,"P.Tax");
				createtext4(cb,415,635,"Tds");
				createtext4(cb,455,635,"Total ded");

				createtext4(cb,175,625,"Extra.Hr");
				createtext4(cb,335,625,"Amount");

				
				
			 
			  
				cb.moveTo(20,615);  // horizontal lines 
				cb.lineTo(595,615);
				cb.stroke();
				y=605;				
			

		}

		catch (Exception ex){
			ex.printStackTrace();
		}

	}
	
		
	 

 
private void generateDetail1(Document doc, PdfContentByte cb)  {
	

	try {
//		rde=0;
 
		double totearn=vd.getBasic_value()+vd.getDa_value()+vd.getHra_value()+vd.getAdd_hra_value()+vd.getIncentive_value()+vd.getSpl_incen_value()+vd.getOt_value()+vd.getLta_value()+vd.getMedical_value()+vd.getMisc_value()+vd.getStair_value()+vd.getMachine1_value()+vd.getMachine2_value()+vd.getFood_value();

		//double totearn=vd.getBasic_value()+vd.getDa_value()+vd.getIncentive_value()+vd.getOt_value()+vd.getHra_value()+vd.getAdd_hra_value()+vd.getSpl_incen_value()+vd.getMisc_value()+vd.getLta_value()+vd.getMedical_value();
		double totded=vd.getPf_value()+vd.getEsis_value()+vd.getAdvance()+vd.getCoupon_amt();
		double net = totearn-totded;
		double totbasic=vd.getBasic()+vd.getDa()+vd.getHra()+vd.getAdd_hra()+vd.getIncentive();
		rde=y;
		checkLn(doc, cb);
		 
		createHeadings(cb,30,y,"M/s. "+cmp_name);
		y=y-10;
		createHeadings(cb,30,y,cmpdto.getCmp_add1());
		y=y-12;
		createContent2(cb,30,y,"Emp Code : "+vd.getEmp_code(),PdfContentByte.ALIGN_LEFT);
		createContent2(cb,430,y,"Month : "+monthname,PdfContentByte.ALIGN_LEFT);
		y=y-10;
		createContent2(cb,30,y,"Name : "+vd.getEmp_name(),PdfContentByte.ALIGN_LEFT);
		createContent3(cb,430,y,"ESIC No : "+vd.getEsic_no(),PdfContentByte.ALIGN_LEFT);
//		createContent3(cb,430,y,"Emp Code : "+vd.getEmp_code(),PdfContentByte.ALIGN_LEFT);
		y=y-10;
		createContent2(cb,30,y,"UAN No : "+vd.getUan_no(),PdfContentByte.ALIGN_LEFT);
		createContent3(cb,430,y,"PF No : "+vd.getPf_no(),PdfContentByte.ALIGN_LEFT);
		y=y-10;
		
		cb.moveTo(20,y);  // horizontal lines 
		cb.lineTo(575,y);
		y=y-10;
		
		
		createContent3(cb,30,y,"Ern.Basic",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,80,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,140,y,df.format(vd.getBasic_value()),PdfContentByte.ALIGN_RIGHT);
		createContent3(cb,230,y,"P.F.",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,290,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,340,y,df.format(vd.getPf_value()),PdfContentByte.ALIGN_RIGHT);
		createContent3(cb,430,y,"Pres.",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,480,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,540,y,df.format(vd.getAtten_days()),PdfContentByte.ALIGN_RIGHT);

		
		y=y-10;
		
		createContent3(cb,30,y,"Ern.D.A." ,PdfContentByte.ALIGN_LEFT);
		createContent3(cb,80,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,140,y,df.format(vd.getDa_value()),PdfContentByte.ALIGN_RIGHT);
		createContent3(cb,230,y,"ADVANCE " ,PdfContentByte.ALIGN_LEFT);
		createContent3(cb,290,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,340,y,df.format(vd.getAdvance()),PdfContentByte.ALIGN_RIGHT);
		createContent3(cb,430,y,"P.L." ,PdfContentByte.ALIGN_LEFT);
		createContent3(cb,480,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,540,y,df.format(0.00),PdfContentByte.ALIGN_RIGHT);
		y=y-10;


		createContent3(cb,30,y,"HRA Amt.",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,80,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,140,y,df.format(vd.getHra_value()),PdfContentByte.ALIGN_RIGHT);
		createContent3(cb,230,y,"LOAN",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,290,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,340,y,df.format(vd.getLoan()),PdfContentByte.ALIGN_RIGHT);
		createContent3(cb,430,y,"C.L.",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,480,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,540,y,df.format(0.00),PdfContentByte.ALIGN_RIGHT);
		y=y-10;


		createContent3(cb,30,y,"Add.HRA ",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,80,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,140,y,df.format(vd.getAdd_hra_value()),PdfContentByte.ALIGN_RIGHT);
		createContent3(cb,230,y,"ESIS",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,290,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,340,y,df.format(vd.getEsis_value()),PdfContentByte.ALIGN_RIGHT);
		createContent3(cb,430,y,"C.L.",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,480,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,540,y,df.format(0.00),PdfContentByte.ALIGN_RIGHT);
		y=y-10;


		createContent3(cb,30,y,"Incentive",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,80,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,140,y,df.format(vd.getIncentive_value()),PdfContentByte.ALIGN_RIGHT);
		createContent3(cb,230,y,"P.TAX",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,290,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,340,y,df.format(vd.getProf_tax()),PdfContentByte.ALIGN_RIGHT);
		createContent3(cb,430,y,"Arr.D",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,480,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,540,y,df.format(vd.getArrear_days()),PdfContentByte.ALIGN_RIGHT);
		y=y-10;

		createContent3(cb,30,y,"Spl.Incen",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,80,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,140,y,df.format(vd.getSpl_incen_value()+vd.getMisc_value()+vd.getStair_value()),PdfContentByte.ALIGN_RIGHT);
		createContent3(cb,230,y,"T.D.S ",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,290,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,340,y,df.format(vd.getTds_value()),PdfContentByte.ALIGN_RIGHT);
		createContent3(cb,430,y,"OFF ",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,480,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,540,y,df.format(0.00),PdfContentByte.ALIGN_RIGHT);
		y=y-10;


		createContent3(cb,30,y,"Comm.off ",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,80,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,140,y,df.format(vd.getOt_value()),PdfContentByte.ALIGN_RIGHT);
		createContent3(cb,230,y,"Other Ded ",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,290,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,340,y,df.format(0.00),PdfContentByte.ALIGN_RIGHT);
		createContent3(cb,430,y,"AB ",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,480,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,540,y,df.format(vd.getAbsent_days()),PdfContentByte.ALIGN_RIGHT);
		y=y-10;

		createContent3(cb,30,y,"L.T.A ",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,80,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,140,y,df.format(vd.getLta_value()),PdfContentByte.ALIGN_RIGHT);
		createContent3(cb,230,y,"Coupon Amt ",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,290,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,340,y,df.format(vd.getCoupon_amt()),PdfContentByte.ALIGN_RIGHT);
		createContent3(cb,430,y,"PH ",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,480,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,540,y,df.format(0.00),PdfContentByte.ALIGN_RIGHT);
		y=y-10;

		
		createContent3(cb,30,y,"Medical ",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,80,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,140,y,df.format(vd.getMedical_value()),PdfContentByte.ALIGN_RIGHT);
		createContent3(cb,430,y,"PAID",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,480,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,540,y,df.format(vd.getAtten_days()),PdfContentByte.ALIGN_RIGHT);
		y=y-10;


		createContent3(cb,30,y,"Opt1 Allow ",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,80,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,140,y,df.format(vd.getMachine1_value()),PdfContentByte.ALIGN_RIGHT);
		createContent3(cb,430,y,"Opt Days",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,480,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,540,y,df.format(vd.getMachine1_days()),PdfContentByte.ALIGN_RIGHT);
		y=y-10;

		createContent3(cb,30,y,"Opt2 Allow ",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,80,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,140,y,df.format(vd.getMachine2_value()),PdfContentByte.ALIGN_RIGHT);
		createContent3(cb,430,y,"Opt Days",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,480,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,540,y,df.format(vd.getMachine2_days()),PdfContentByte.ALIGN_RIGHT);
		y=y-10;
		
		createContent3(cb,30,y,"Food Allow ",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,80,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,140,y,df.format(vd.getFood_value()),PdfContentByte.ALIGN_RIGHT);
		createContent3(cb,430,y,"Extra Hrs",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,480,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,540,y,df.format(vd.getExtra_hrs()),PdfContentByte.ALIGN_RIGHT);
		y=y-10;

		createContent3(cb,30,y,"Subtotal",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,80,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,140,y,df.format(totearn),PdfContentByte.ALIGN_RIGHT);
		createContent3(cb,230,y,"Subtotal",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,290,y,":",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,340,y,df.format(totded),PdfContentByte.ALIGN_RIGHT);
		y=y-10;

		cb.moveTo(20,y);  // horizontal lines 
		cb.lineTo(380,y);
		y=y-10;

		createContent3(cb,30,y,"Gross Earned ",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,140,y,"Total Dedu.",PdfContentByte.ALIGN_LEFT);
		createContent3(cb,250,y,"Net Pay ",PdfContentByte.ALIGN_LEFT);
		y=y-10;

		createContent3(cb,70,y,df.format(totearn),PdfContentByte.ALIGN_RIGHT);
		createContent3(cb,180,y,df.format(totded),PdfContentByte.ALIGN_RIGHT);
		createContent1(cb,280,y,df.format(net),PdfContentByte.ALIGN_RIGHT);
		createContent3(cb,400,y,"Signature",PdfContentByte.ALIGN_LEFT);
		y=y-10;
		
		
		cb.moveTo(20,y);  // horizontal lines 
		cb.lineTo(575,y);
		y=y-2;
		cb.moveTo(20,y);  // horizontal lines 
		cb.lineTo(575,y);
		y=y-30;
		
		cb.stroke();

 
		
	}

	catch (Exception ex){
		ex.printStackTrace();
	}

}
 
	
	
	public void checkLn(Document doc, PdfContentByte cb)
	{
		if (rde==0)
			rde=y;
		   
		if(rde<=110)
		{
			doc.newPage();
			y=747;
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