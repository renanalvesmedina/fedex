package com.mercurio.lms.tracking;

import java.io.Serializable;


public class Consignment implements Serializable{
	
	
	private static final long serialVersionUID = 1L;

	private String number;
    private String status;
    private String estimatedDateDelivery;
    private String pickupDate;
    private String deliveryDate;
    private String modal;

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getEstimatedDateDelivery() {
        return estimatedDateDelivery;
    }

    public void setEstimatedDateDelivery(String estimatedDateDelivery) {
        this.estimatedDateDelivery = estimatedDateDelivery;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	public String getModal() {
		return modal;
	}

	public void setModal(String modal) {
		this.modal = modal;
	}
}
