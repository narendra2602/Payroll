package com.payroll.view;
import java.awt.Desktop;
import java.io.File;

public class PayrollFormula {

    public static void main(String[] args) {

        try {

            // Current application folder
            String appPath = System.getProperty("user.dir");

            // PDF inside application folder
            File pdfFile = new File(appPath + File.separator + "PayrollSalaryCalculation.pdf");

            System.out.println("PDF Path : " + pdfFile.getAbsolutePath());

            if (pdfFile.exists()) {

                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(pdfFile);
                }

            } else {
                System.out.println("PDF file not found");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}