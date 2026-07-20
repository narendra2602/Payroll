package com.payroll.print;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;


import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;

import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;


//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.pdf.PdfPCell;
//import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;

public class SalaryRegisterPDF {

    public static void main(String[] args) {
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream("Salary_Register_May_2025.pdf"));
            
            document.open();

            // Title
            document.add(new Paragraph("M/s PANKAJ ENTERPRISES"));
            document.add(new Paragraph("Salary Register for the month of May-2025"));
            document.add(new Paragraph("Address: EWS-266-A, INDRANAGAR, MANDIDEEP - 462046"));
            document.add(new Paragraph("GSTIN: 23AQCPK3527A1ZK\n\n"));

            // Create table with 9 columns
            PdfPTable table = new PdfPTable(9);
            table.setWidthPercentage(100);

            // Table Header
            String[] headers = {
                "Emp Code", "Name", "Designation", "Gross Amount",
                "Deductions", "Arrears", "Total Ded.", "Net Payable", "Bank A/c"
            };
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Paragraph(header));
                table.addCell(cell);
            }

            // Row 1 - Pradeep Kumar Jain
            table.addCell("10");
            table.addCell("Pradeep Kumar Jain");
            table.addCell("Worker");
            table.addCell("20535.00");
            table.addCell("2362.00");
            table.addCell("5291.00");
            table.addCell("2897.00");
            table.addCell("24640.00");
            table.addCell("20248444560");

            // Row 2 - Satyam Kumar
            table.addCell("86");
            table.addCell("Satyam Kumar");
            table.addCell("Worker");
            table.addCell("20535.00");
            table.addCell("2102.00");
            table.addCell("4826.00");
            table.addCell("2295.00");
            table.addCell("25395.00");
            table.addCell("20248443758");

            document.add(table);
            document.close();

            System.out.println("PDF created successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
