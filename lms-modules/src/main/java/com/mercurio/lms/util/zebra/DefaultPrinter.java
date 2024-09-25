package com.mercurio.lms.util.zebra;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

public class DefaultPrinter {

    /**
     * Manda os dados recebidos como parametro para a impressora.
     * 
     * @param buffer
     */
    public void print(final String buffer) {
    	PrintService printer = getPrintService();
		DocPrintJob docPrintJob = printer.createPrintJob();
		Doc doc = new SimpleDoc(buffer.getBytes(), DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
		try {
			docPrintJob.print(doc, null);
		} catch (PrintException e) {
			System.out.println(e);
		}
    }
    
    /**
     * Retorna o servico referente a impressora Default da maquina.
     * 
     * @return PrintService
     */
	public PrintService getPrintService() {
		PrintService printer = PrintServiceLookup.lookupDefaultPrintService();
		if (printer == null) {
			System.out.println("erDefaultPrinterNotFound");
		}
		return printer;
	}
}