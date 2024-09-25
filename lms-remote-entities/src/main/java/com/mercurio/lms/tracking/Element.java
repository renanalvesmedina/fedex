package com.mercurio.lms.tracking;

import java.io.Serializable;


public class Element implements Serializable{
	
	private static final long serialVersionUID = 1L;

    private Consignment consignment;
    private Invoices invoices;
    private Events events;
    private Schedulings schedulings;
    private DeliveryLocation deliveryLocation;

    public Consignment getConsignment() {
        return consignment;
    }

    public void setConsignment(Consignment consignment) {
        this.consignment = consignment;
    }

	public Invoices getListInvoice() {
		return invoices;
	}

	public void setListInvoice(Invoices listInvoice) {
		this.invoices = listInvoice;
	}

	public Events getListEvent() {
		return events;
	}

	public void setListEvent(Events listEvent) {
		this.events = listEvent;
	}

	public Schedulings getListSchedulings() {
		return schedulings;
	}

	public void setListSchedulings(Schedulings listSchedulings) {
		this.schedulings = listSchedulings;
	}

	public DeliveryLocation getDeliveryLocation() {
		return deliveryLocation;
	}
	
	public void setDeliveryLocation(DeliveryLocation deliveryLocation) {
		this.deliveryLocation = deliveryLocation;
	}


}
