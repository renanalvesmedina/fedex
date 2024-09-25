package com.mercurio.lms.util.zebra;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;



/**
 * Classe utilitaria para impressao de dados via LPT1.
 *  
 * @author Luis Carlos Poletto
 */
public class LptPrinter {
    private static final long serialVersionUID = -2663136692606812716L;
    private Executor executor;
    public static long lastPrint = 0;
    
    /**
     * Construtor padrão
     */
    public LptPrinter() {
        executor = Executors.newCachedThreadPool();
    }

    public void print(String buffer, Boolean useDecodeBase64, Integer lptNumber) {
        List<String> buffers = new ArrayList<String>(1);
        buffers.add(buffer);
        print(buffers, useDecodeBase64, lptNumber);
    }
    
    /**
     * Manda os dados recebidos como parametro para a impressora na porta LPT1.
     * 
     * @param buffer dados para serem impressos codificados na Base 64.
     */
    public void print(String buffer) {
    	print(buffer, true, 1);
    }
    
    /**
     * Manda os dados recebidos como parametro para a impressora na porta especificada.
     * 
     * @param buffer dados para serem impressos codificados na Base 64.
     */
    public void print(String buffer, Integer lptNumber) {
    	print(buffer, true, lptNumber);
    }
    
    /**
     * Manda os dados recebidos como parametro para a impressora na porta LPT1.
     * 
     * @param buffer dados para serem impressos codificados na Base 64.
     */
    public void print(final List<String> buffers) {
    	print(buffers, true, 1);
    }

    public void print(final List<String> buffers, Integer lptNumber) {
    	print(buffers, true, lptNumber);
    }

    /**
     * Manda os dados recebidos como parametro para a impressora na ordem
     * recebida.
     * 
     * @param buffers lista de dados para serem impressos codificados na Base 64.
     */
    public void print(final List<String> buffers, final Boolean useDecodeBase64, final Integer lptNumber) {
    	final Runnable r = new Runnable() {
            public void run() {
                try {
                	FileOutputStream fos = null;
                	if(lptNumber >= 1000 && lptNumber <= 2000) {
                		fos = new FileOutputStream("\\\\10.44.68.101\\zebra");
                	} else if(lptNumber == 9999) {
                		Calendar dt = Calendar.getInstance();
                		fos = new FileOutputStream("C:\\temp\\lms_print_"+dt.get(Calendar.YEAR)+"-"+(dt.get(Calendar.MONTH)+1)+"-"+dt.get(Calendar.DATE)+"-"+dt.get(Calendar.HOUR)+"-"+dt.get(Calendar.MINUTE)+"-"+dt.get(Calendar.MILLISECOND)+".txt");
                	} else {
                		fos = new FileOutputStream(lptNumber != null ? "lpt" + lptNumber.toString() : "lpt1");
                	}
                    final PrintStream ps = new PrintStream(fos);
                    
                    for (String buffer : buffers) {
                    	System.out.println("Iniciando impressao...");
                    	if(useDecodeBase64) {
                        	ps.print(new String(Base64Util.decode(buffer)));
                    	} else {
                    		ps.print(buffer);
                    	}
                    	System.out.println("Impressao finalizada...");
                    	lastPrint = System.currentTimeMillis();

                    }
                    ps.close();
                    fos.close();
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        this.executor.execute(r);
    }
}