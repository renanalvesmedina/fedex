package com.mercurio.lms.util.zebra;

import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class IpPrinter {
    
    /**
     * Manda os dados recebidos como parametro para a impressora.
     * 
     * @param buffer dados para serem impressos codificados na Base 64.
     */
    public void print(final String buffer, final String ip, final int port) {
        final List<String> buffers = new ArrayList<String>(1);
        buffers.add(buffer);
        print(buffers, ip, port);
    }

    /**
     * Manda os dados recebidos como parametro para a impressora na ordem
     * recebida.
     * 
     * @param buffers lista de dados para serem impressos codificados na Base 64.
     */
    public void print(final List<String> buffers, final String ip, final int port) {
    	
        Runnable r = new Runnable() {
            public void run() {
            	try {
	                Socket client = null;
	                PrintWriter out = null;
            		
            		for (String dados : buffers) {
    	                try {
                			System.out.println("Iniciando impressao...");
                				
    	                	client = new Socket(Proxy.NO_PROXY);
    	        			client.connect(new InetSocketAddress(ip, port));
    	        			out = new PrintWriter(client.getOutputStream());
                			out.write(dados);
    	                    out.flush();
    	                } finally {
    	                	if(out != null) out.close();
    	                	if(client != null) client.close();
    	                	System.out.println("Impressao finalizada...");
    	                }
					}
                } catch (Exception e) {
                	System.out.println(e);
                }
            }
        };
        Thread thread = new Thread(r);
        thread.start();
    }
}