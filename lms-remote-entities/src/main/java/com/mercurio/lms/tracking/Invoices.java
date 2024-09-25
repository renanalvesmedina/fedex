package com.mercurio.lms.tracking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Invoices implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	
	private List<Invoice> invoices = new ArrayList<Invoice>();

	public List<Invoice> getInvoices() {
		return invoices;
	}
	
	public void addInvoice(Invoice invoice){
		this.invoices.add(invoice);
	}

}
